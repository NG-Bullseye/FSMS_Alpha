/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kickstart.catalog;

import forms.ReorderForm;
import kickstart.articles.Article;
import kickstart.articles.Comment;
import kickstart.inventory.ReorderableInventoryItem;

import org.hibernate.validator.constraints.Range;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.Inventory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.salespointframework.time.BusinessTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Controller
public class CatalogController {

	private final CatalogManager manager;
	private final BusinessTime businessTime;

	CatalogController(WebshopCatalog catalog, Inventory<ReorderableInventoryItem> inventory, BusinessTime businessTime){
	this.manager = new CatalogManager(catalog, inventory);
	this.businessTime=businessTime;
	}

	@ModelAttribute("colours")
	public String[] colours() {
		return new String[] {
				"schwarz","blau","weiß","rot","braun","grün"
		};
	}
	@ModelAttribute("categories")
	public String[] categories() {
		return new String[] {
				"Tisch","Schrank","Stuhl","Regal","Bett"
		};
	}

	@RequestMapping("/catalog")
	String catalog(Model model){


		model.addAttribute("catalog", manager.getVisibleCatalog());
		model.addAttribute("filterform", new Filterform());

		return "catalog";
	}
	@PostMapping("/catalog")
	String catalogFiltered (@ModelAttribute("filterform") Filterform filterform, @RequestParam(required = false, name="reset") String reset, Model model){
		if(reset.equals("Filter zurücksetzen")){
			return "redirect:/catalog/";
		}
		model.addAttribute("catalog", manager.filteredCatalog(filterform));
		return "catalog";
	}
	@GetMapping("catalog/all")
	public String completeCatalog(Model model){
		model.addAttribute("catalog", manager.getWholeCatalog());
		model.addAttribute("filterform",new Filterform());
		return "catalog";
	}
	@GetMapping("artikel/{identifier}")
	public String detail(@PathVariable ProductIdentifier identifier, Model model){

		model.addAttribute("article", manager.getArticle(identifier));
		System.out.println(manager.getArticle(identifier).getDescription());

		return "article";
	}
	@PostMapping("artikel/{identifier}/comment")
	public String comment(@PathVariable("identifier") ProductIdentifier identifier, @Valid CommentAndRating payload, Model model){
		Article article = manager.getArticle(identifier);
		article.addComment(payload.toComment(businessTime.getTime()));
		manager.saveArticle(article);

		return "redirect:/artikel/"+ article.getId();


	}
	@GetMapping("edit/{identifier}")
	public String detailEdit(@PathVariable ProductIdentifier identifier, Model model){

		model.addAttribute("article", manager.getArticle(identifier));
		HashSet<String> articleCategories = new HashSet<>();
		manager.getArticle(identifier).getCategories().forEach(articleCategories::add);
		model.addAttribute("articleCategories", articleCategories);
		model.addAttribute("form",new Form()); //Damit man im folgenden form bearbeiten kann
		return "edit";
	}

	@PostMapping("edit/{identifier}")
	public String editArticle(@PathVariable ProductIdentifier identifier, @Valid @ModelAttribute("form") Form form, BindingResult bindingResult, Model model){
		model.addAttribute("article",manager.getArticle(identifier));
		if(bindingResult.hasErrors()){
			return "edit";
		}
		manager.editArticle(form, identifier);

		return "redirect:/article/"+ manager.getArticle(identifier);
	}
	@GetMapping("catalog/part/new")
	public String showNew(Model model){
		model.addAttribute("form",new Form());
		return"newPart";
	}
	@PostMapping("catalog/part/new")
	public String editNew(@ModelAttribute("form") Form form, Model model){
		manager.newPart(form);
		model.addAttribute("catalog", manager.getWholeCatalog());
		return"redirect:/catalog";
	}
	@GetMapping("catalog/composite/new")
	public String newComposite(Model model){

		CompositeForm composite = new CompositeForm();
		model.addAttribute("compositeForm",composite);
		model.addAttribute("catalog", manager.getWholeCatalog());
		//model.addAttribute("form", new ReorderForm());
		return"newComposite";
	}
	@PostMapping("catalog/composite/new")
	public String newCompositeFinished(@ModelAttribute("compositeForm") CompositeForm form, Model model,@NotNull @RequestParam Map<String,String> partsMapping){

		model.addAttribute("compositeForm",new CompositeForm());

		manager.newComposite(form,partsMapping);

		model.addAttribute("catalog", manager.getWholeCatalog());

		return"redirect:/catalog";
	}
	@GetMapping("hide/{identifier}")
	public String hide(@PathVariable ProductIdentifier identifier, Model model){
		manager.hideArticle(identifier);
		return "redirect:/catalog/";
	}



	interface CommentAndRating {

		/*	@NotEmpty*/
		String getComment();

		@Range(min = 1, max = 10)
		int getRating();

		default Comment toComment(LocalDateTime time) {
			return new Comment(getComment(), getRating(), time);
		}
	}
}
