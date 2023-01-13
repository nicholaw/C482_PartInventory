package finalAssessment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *	Represents organization inventory, which can contain both parts and products.
 */
public class Inventory implements Comparator<Product>
{
	/**
	 * The purpose of choosing to use HashSets instead of ObservableLists to store parts and products in the
	 * inventory was to increase speed of searching for those parts and products. However, InventoryItems currently
	 * generate their hash codes from their id's, but I hypothesize a typical user is more likely to search for an
	 * item by name rather than id. Therefore, if I were to update this application in the future, I would perform
	 * tests to determine if the improved search speed is worth the extra time and overhead incurred by converting 
	 * the HashSets into ObservableLists for use in the tables and also for updating the tables independently from
	 * the sets. Furthermore, if the HashSet structure was deemed worthwhile and a typical user does indeed search
	 * by name more often than id, I would generate the hash codes from item names rather than id's. 
	 */
	private ObservableList<Part> allParts;
	private ObservableList<Product> allProducts;
	
	/**
	 * Instantiates an Inventory by instantiating the underlying data structures.
	 */
	public Inventory()
	{
		allParts = FXCollections.observableArrayList(new ArrayList<Part>());
		allProducts = FXCollections.observableArrayList(new ArrayList<Product>());
	}
	
	/**
	 * Instantiates an Inventory and populates it with the given collections
	 * @param parts		collection of Parts
	 * @param products	collection of Products
	 */
	public Inventory(Collection<Part> parts, Collection<Product> products)
	{
		allParts = FXCollections.observableArrayList(new ArrayList<Part>(parts));
		allProducts = FXCollections.observableArrayList(new ArrayList<Product>(products));
	}
	
	/**
	 * Attempts to add a part to the inventory. Returns true of the given part was successfully added and false otherwise.
	 * 
	 * @param p	the Part to be added
	 * @return	boolean denoting if the part was successfully added
	 */
	public boolean addPart(Part p)
	{
		return allParts.add(p);
	}
	
	/**
	 * Attempts to add a product to the inventory. Returns true and updates any parts associated with the given product to reflect
	 * their association and false otherwise.
	 * 
	 * @param p	the Product to be added
	 * @return	boolean denoting if the product was successfully added
	 */
	public boolean addProduct(Product product)
	{
		if(allProducts.add(product))
		{
			for(Part part : product.getAllAssociatedParts())
			{
				if(part instanceof InHouse)
					((InHouse)part).incNumProducts();
				else
					((OutSourced)part).incNumProducts();
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Returns the Part with the given id if it exists in the inventory and null otherwise.
	 * 
	 * @param id	Id of the part to be found
	 * @return		the Part with the given id if it exist or null if it does not
	 */
	public Part lookupPart(int id)
	{
		for(Part p : allParts)
		{
			if(p.getId() == id)
				return p;
		}
		return null;
	}
	
	/**
	 * Returns the Product with the given id if it exists in the inventory and null otherwise.
	 * 
	 * @param id	Id of the product to be found
	 * @return		Product with the given id if it exist or null if it does not
	 */
	public Product lookupProduct(int id)
	{
		for(Product p : allProducts)
		{
			if(p.getId() == id)
				return p;
		}
		return null;
	}
	
	/**
	 * Returns the part whose name contains the provided String if it exists in the inventory and null otherwise.
	 * 
	 * @param name	Name of the part to be found
	 * @return		the Part with the given name if it exist or null if it does not
	 */
	public Part lookupPart(String name)
	{
		for(Part p : allParts)
		{
			if(p.getName().contains(name))
				return p;
		}
		return null;
	}
	
	/**
	 * Returns the product whose name contains the provided String if it exists in the inventory and null otherwise.
	 * 
	 * @param name	Name of the product to be found
	 * @return		the product with the given name if it exist or null if it does not
	 */
	public Product lookupProduct(String name)
	{
		for(Product p : allProducts)
		{
			if(p.getName() == name)
				return p;
		}
		return null;
	}
	
	/**
	 * Replaces the product at the given index with the given part
	 * 
	 * @param index	index of the part to be replaced
	 * @param p	the replacement part
	 */
	public void updatePart(int index, Part p)
	{
		if(lookupPart(p.getId()) != null)
		{
			allParts.remove(lookupPart(p.getId()));
			allParts.add(p);
		}
	}
	
	/**
	 * Replaces the product at the given index with the given part
	 * 
	 * @param index	index of the part to be replaced
	 * @param p	the replacement part
	 */
	public void updateProduct(int index, Product p)
	{
		if(lookupPart(p.getId()) != null)
		{
			allParts.remove(lookupPart(p.getId()));
			allProducts.add(p);
		}
	}
	
	/**
	 * Removes the given part from the inventory. Returns true if the part was successfully removed and false otherwise.
	 * 
	 * @param p	the part to be removed
	 * @return	boolean denoting whether the part was successfully removed
	 */
	public boolean deletePart(Part p)
	{
		return allParts.remove(p);
	}//deletePart
	
	/**
	 * Removes the given product from the inventory. Returns true if the product was successfully removed and false otherwise.
	 * 
	 * @param p	the product to be removed
	 * @return	boolean denoting whether the part was successfully removed
	 */
	public boolean deleteProduct(Product p)
	{
		return allProducts.remove(p);
	}//deleteProduct
	
	/**
	 * Returns and ObservableList of all the parts stored in this inventory.
	 * @return	ObservableList<Part> of all stored parts
	 */
	public ObservableList<Part> getAllParts()
	{
		//return FXCollections.observableArrayList(new ArrayList<Part>(allParts));
		return allParts;
	}
	
	/**
	 * Returns and ObservableList of all the parts stored in this inventory.
	 * @return	ObservableList<Part> of all stored parts
	 */
	public ObservableList<Product> getAllProducts()
	{
		//return FXCollections.observableArrayList(new ArrayList<Product>(allProducts));
		return allProducts;
	}
	
	/**
	 * Used for sorting items which can be stored in this inventory (i.e. Parts and Products). Returns the difference of the 
	 * id's of the provided items
	 * 
	 * @param p1	the first item to be compared
	 * @param p2	the second item to be compared
	 * @return		the difference of the items' id's
	 */
	@Override
	public int compare(Product p1, Product p2) 
	{
		return p1.getId() - p2.getId();
	}
}