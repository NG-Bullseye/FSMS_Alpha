package kickstart.carManagement;

public class TruckClassForm {

	private String name;
	private int capacity;
	private int price;


	public String getName() {
		return name;
	}

	/**
	 * @param name name of truck
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return price the user has to pay in order to rent the truck
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price price the user has to pay in order to rent the truck
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the capacity the truck can carry at max
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity maximum weight of truckload
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}


