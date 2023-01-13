package finalAssessment;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a Product in the inventory.
 */
public class Product
{
	private int id;
	private String name;
	private double price;
	private int stock;
	private int min;
	private int max;
	private HashSet<Part> associatedParts;
	
	/**
	 * Sets Product attributes to the provided values.
	 * @param id			id of this product
	 * @param name			name of this product
	 * @param description	description of this product
	 * @param price			price of this product
	 * @param stock			stock of this product
	 * @param min			minimum of this product
	 * @param max			maximum of this product
	 */
	public Product(int id, String name, double price, int stock, int min, int max)
	{
		setId(id);
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.min = min;
		this.max = max;
		associatedParts = new HashSet<Part>();
	}
	
	/**
	 * Sets Product attributes to the provided values.
	 * @param id				id of this product
	 * @param name				name of this product
	 * @param description		description of this product
	 * @param price				price of this product
	 * @param stock				stock of this product
	 * @param min				minimum of this product
	 * @param max				maximum of this product
	 * @param associatedParts	collection of parts associated with this product
	 */
	public Product(int id, String name, double price, int stock, int min, int max, Collection<Part> associatedParts)
	{
		setId(id);
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.min = min;
		this.max = max;
		this.associatedParts = new HashSet<Part>(associatedParts);
	}
	
	/**
	 * Sets the name of this product.
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Sets the price of this product.
	 * @param price	the price to set
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}
	
	/**
	 * Sets the stock of this product.
	 * @param stock	the stock to set
	 */
	public void setStock(int stock)
	{
		this.stock = stock;
	}
	
	/**
	 * Sets the minimum inventory of this product.
	 * @param min	the min to set
	 */
	public void setMin(int min)
	{
		this.min = min;
	}
	
	/**
	 * Sets the maximum inventory of this product.
	 * @param max	the max to set
	 */
	public void setMax(int max)
	{
		this.max = max;
	}
	
	/**
	 * Returns the name of this product.
	 * @return the product's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the price of this product.
	 * @return the product's price
	 */
	public double getPrice()
	{
		return price;
	}
	
	/**
	 * Returns the stock of this product.
	 * @return the product's stock
	 */
	public int getStock()
	{
		return stock;
	}
	
	/**
	 * Returns the minimum inventory of this product.
	 * @return the product's min
	 */
	public int getMin()
	{
		return min;
	}
	
	/**
	 * Returns the maximum inventory of this product.
	 * @return the product's max
	 */
	public int getMax()
	{
		return max;
	}
	
	/**
	 * Adds a Part to the set of parts associated with this product.
	 * @return true if the part was added successfully and false otherwise
	 */
	public boolean addAssociatedPart(Part part)
	{
		return associatedParts.add(part);
	}
	
	/**
	 * Sets the products associated parts to the provided collection.
	 * @param part	the collection of parts to set
	 */
	public void setAssociatedParts(Collection<Part> parts)
	{
		associatedParts = new HashSet<Part>(parts);
	}
	
	/**
	 * Removes the provided part from the products set of associated parts.
	 * @param part	the part to be removed
	 * @return	true if the part was successfully removed and false otherwise
	 */
	public boolean deleteAssociatedPart(Part part)
	{
		return associatedParts.remove(part);
	}
	
	/**
	 * Returns an ObservableList<Part> of the parts associated with this product.
	 * @return	the parts associated with this product
	 */
	public ObservableList<Part> getAllAssociatedParts()
	{
		return FXCollections.observableArrayList(associatedParts);
	}
	
	/**
	 * Sets the integer id of this item
	 * @param i	this item's id
	 */
	public void setId(int i)
	{
		id = i;
	}
	
	/**
	 * Returns the integer id of this item
	 * @return	this item's id
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Returns true if this object is equal to the given object. This function has been overridden 
	 * to compare items based on their id's rather than their memory address.
	 * 
	 * @param obj	the Object for comparison
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(!(obj instanceof Product))
			return false;
		return ((Product)obj).getId() == this.getId();
	}
}