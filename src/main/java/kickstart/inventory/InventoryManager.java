package kickstart.inventory;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kickstart.accountancy.AccountancyManager;
import kickstart.articles.Article;
import lombok.Getter;

/**
 * 
 * This class manages all aspects of the inventory/stock. It stores the amount
 * and can change the amount by reorders to the stock or by orders of a
 * customer.
 */
@Component
@Transactional
public class InventoryManager {

	// Stores the amount of each article.
	@Getter
	private final Inventory<ReorderableInventoryItem> inventory;

	@Getter
	private AccountancyManager accountancy;

	// The time difference (in days) until a reorder is completed
	@Getter
	private final long reorderTime = 4;

	/**
	 * 
	 * @param inventory   The repository where InventoryItems are saved
	 * @param accountancy This class gives the class the current time and is used to
	 *                    add expenses
	 */
	public InventoryManager(@NotNull Inventory<ReorderableInventoryItem> inventory,
							@NotNull AccountancyManager accountancy) {
		this.inventory = inventory;
		this.accountancy = accountancy;
	}

	public BusinessTime getTime() {
		return accountancy.getBusinessTime();
	}

	/**
	 * 
	 * @param newArticle A new article that should get added to the inventory. Has
	 *                   to be saved in a repository before using this method.
	 * @throws NullPointerException If newArticle is null
	 */
	public void addArticle(@NotNull Article newArticle) {
		if (!inventory.findByProductIdentifier(newArticle.getId()).isPresent()) {
			inventory.save(new ReorderableInventoryItem(newArticle, Quantity.of(0, Metric.UNIT)));
		}
	}

	/**
	 * 
	 * @param article  The article whose amount should be checked.
	 * @param quantity The desired quantity
	 * @return True, if the quantity in the inventory is greater or equal to the
	 *         desired quantity. false if the article is not present or if the
	 *         quantity in the inventory is less then the desired quantity
	 * @throws IllegalArgumentException If Quantity hasn't the Metric
	 *                                  {@literal Metric.UNIT}
	 */
	public boolean hasSufficientQuantity(@NotNull Article article, @NotNull Quantity quantity)
			throws IllegalArgumentException {
		if (quantity.getMetric() != Metric.UNIT) {
			throw new IllegalArgumentException();
		}

		Optional<ReorderableInventoryItem> item = inventory.findByProduct(article);

		if (item.isPresent()) {
			return item.get().hasSufficientQuantity(quantity);
		} else {
			return false;
		}
	}

	/**
	 * Creates a new reorder for the article so that after the specified reorder
	 * time, the quantity increases. It also adds an AccountancyEntry to the
	 * {@link AccountancyManager}
	 * 
	 * @param article  The article that should get reordered.
	 * @param quantity The desired quantity
	 */
	public void reorder(@NotNull Article article, @NotNull Quantity quantity) {
		Optional<ReorderableInventoryItem> item = inventory.findByProduct(article);

		if (item.isPresent()) {
			item.get().addReorder(
					Interval.from(accountancy.getTime()).to(accountancy.getTime().plusDays(reorderTime)).getEnd(),
					quantity);
			inventory.save(item.get());

			accountancy.addEntry(
					item.get().getProduct().getPrice()
							.multiply(item.get().getQuantity().getAmount().multiply(BigDecimal.valueOf(-1))),
					accountancy.getTime(), "Reordered " + item.get().getProduct().getName() + " "
							+ item.get().getQuantity().toString() + " " + "times");

		}

	}

	/**
	 * Creates a new reorder for the article so that after the specified reorder
	 * time, the quantity increases. It also adds an AccountancyEntry to the
	 * {@link AccountancyManager}
	 *
	 * @param id       The ProductIdentifier of the article that should get
	 *                 reordered.
	 * @param quantity The desired quantity
	 */
	public void reorder(@NotNull ProductIdentifier id, @NotNull Quantity quantity) {
		Optional<ReorderableInventoryItem> item = inventory.findByProductIdentifier(id);

		if (item.isPresent()) {
			item.get().addReorder(
					Interval.from(accountancy.getTime()).to(accountancy.getTime().plusDays(reorderTime)).getEnd(),
					quantity);
			inventory.save(item.get());

			accountancy.addEntry(
					item.get().getProduct().getPrice()
							.multiply(item.get().getQuantity().getAmount().multiply(BigDecimal.valueOf(-1))),
					accountancy.getTime(), "Reordered " + item.get().getProduct().getName() + " "
							+ item.get().getQuantity().toString() + " " + "times");
		}
	}

	/**
	 * This method decreases the amount of this article in process of e.g. an order.
	 * 
	 * @param article  The article whose amount should be decreased.
	 * @param quantity The desired quantity
	 * @throws IllegalArgumentException If Quantity hasn't the Metric
	 *                                  {@literal Metric.UNIT} the article exists
	 *                                  and if not add it with {@link addArticle}
	 */
	public void decreaseQuantity(@NotNull Article article, @NotNull Quantity quantity) throws IllegalArgumentException {
		if (inventory.findByProduct(article).isPresent() == false) {
			return;
		}
		if (!quantity.isCompatibleWith(Metric.UNIT)) {
			throw new IllegalArgumentException();
		}
		if (quantity.isGreaterThan(inventory.findByProduct(article).get().getQuantity())) {
			return;
		}

		ReorderableInventoryItem item = inventory.findByProduct(article).get();
		item.decreaseQuantity(quantity);
		inventory.save(item);
	}

	/**
	 * @param article
	 * @return Returns if there exists an InventoryItem for this article. Otherwise
	 *         false
	 */
	public boolean isPresent(@NotNull Article article) {
		return inventory.findByProduct(article).isPresent();
	}

	/**
	 * @param id The ProductIdentifier of the asked id
	 * @return Returns if there exists an InventoryItem for this article. Otherwise
	 *         false
	 */
	public boolean isPresent(@NotNull ProductIdentifier id) {
		return inventory.findByProductIdentifier(id).isPresent();
	}

	/**
	 * This functions runs the update method of the class
	 * {@link ReorderableInventoryItem} for all currently stored reorders. If update
	 * on a reorders returns true, the reorder is deleted from the repository.
	 */
	@Scheduled(fixedRate = 5000)
	public void update() {
		Iterable<ReorderableInventoryItem> items = inventory.findAll();

		for (ReorderableInventoryItem item : items) {
			if (item.update(accountancy.getTime())) {
				inventory.save(item);
			}
		}
	}
}
