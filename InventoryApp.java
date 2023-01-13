package finalAssessment;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Represents an inventory management system which utilizes various forms to add, remove, and modify 
 * component parts and their associated products. In addition to launching the application, this class
 * serves as a controller for communicating between the inventory and the forms with which a user
 * will interact.
 * 
 * @author Nicholas Warner 	001447619
 */
public class InventoryApp extends Application 
{	
	//Integer constants for identifying forms
	public final static int MAIN_FORM = 0;
	public final static int ADD_PART_FORM = 1;
	public final static int MODIFY_PART_FORM = 2;
	public final static int ADD_PRODUCT_FORM = 3;
	public final static int MODIFY_PRODUCT_FORM = 4;
	
	//Integers constants for ensuring auto-generated part id's never overlap auto-generated product id's
	public final static int STARTING_PART_ID = 0;
	public final static int STARTING_PRODUCT_ID = 1000;
	
	private	Stage				stage;
	private Scene				scene;
	private Inventory			inventory;
	private	MainForm			main;
	private AddPartForm			addPart;
	private ModifyPartForm		modifyPart;
	private AddProductForm		addProduct;
	private ModifyProductForm	modifyProduct;
	private int					nextPartId;
	private int 				nextProductId;
	
	private ArrayList<Part> testParts;
	private ArrayList<Product> testProducts;
	
	/**
	 * Launches this application and adds some initial parts to the inventory.
	 * 
	 * @param stg	Stage used to display windows
	 */
	@Override
	public void start(Stage stg) throws Exception 
	{
		stage = stg;
		scene = new Scene(new BorderPane());
		inventory = new Inventory();
		testParts = new ArrayList<Part>();
		testProducts = new ArrayList<Product>();
		
		/////////////FOR TESTING////////////////////////////////////////////////////////////////////////////////////////////////////
		testParts.add(new OutSourced(0, "Rucksack", 30.00, 56, 12, 60, "Bottemless Bags LTD."));
		testParts.add(new InHouse(1, "Rope (8 meters)", 12.00, 18, 6, 24, 1));
		testParts.add(new OutSourced(2, "Torches", 8.50, 84, 60, 120, "Illumination Solutions"));
		testParts.add(new OutSourced(3, "Rations", 5.00, 180, 90, 240, "Adventurer's Pantry"));
		testParts.add(new InHouse(4, "Menacing Letter Opener", 11.00, 7, 4, 8, 2));
		testParts.add(new InHouse(5, "Toy Crossbow", 13.00, 10, 6, 12, 1));
		testParts.add(new InHouse(6, "Hefty Stick", 12.50, 6, 12, 7, 3));
		testParts.add(new InHouse(7, "Barrel Lid", 10.00, 7, 4, 8, 4));
		testParts.add(new InHouse(8, "Baby's First Wand", 8.00, 12, 6, 12, 3));
		for(Part p : testParts)
		{
			inventory.addPart(p);
		}
		
		ArrayList<Part> assTestParts = new ArrayList<Part>();
		assTestParts.add(inventory.lookupPart(0));assTestParts.add(inventory.lookupPart(1));assTestParts.add(inventory.lookupPart(2));assTestParts.add(inventory.lookupPart(3));
		testProducts.add(new Product(1000, "Basic Adventuring Kit", 100.00, 4, 4, 16, assTestParts));
		assTestParts = new ArrayList<Part>();
		assTestParts.add(inventory.lookupPart(1));assTestParts.add(inventory.lookupPart(2));
		testProducts.add(new Product(1001, "Spelunking Kit", 55.00, 2, 2, 8, assTestParts));
		assTestParts = new ArrayList<Part>();
		assTestParts.add(inventory.lookupPart(4));assTestParts.add(inventory.lookupPart(5));
		testProducts.add(new Product(1002, "Beginner Rogue Set", 21.00, 5, 4, 6, assTestParts));
		assTestParts = new ArrayList<Part>();
		assTestParts.add(inventory.lookupPart(6));assTestParts.add(inventory.lookupPart(7));
		testProducts.add(new Product(1003, "Beginner Paladin Set", 20.00, 6, 4, 6, assTestParts));
		for(Product p : testProducts)
		{
			inventory.addProduct(p);
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		nextPartId = InventoryApp.STARTING_PART_ID + testParts.size();
		nextProductId = InventoryApp.STARTING_PRODUCT_ID + testProducts.size();
		loadMainForm();
		stage.show();
	}
	
	/**
	 * Displays the Main form which the user will use to select search for parts or select parts and 
	 * products for removal and modification.
	 */
	private void loadMainForm()
	{
		if(main == null)
			main = new MainForm(this);
		main.clear();
		scene.setRoot(main.getScenePane());
		stage.setScene(scene);
		stage.setTitle(main.getSceneTitle());
	}
	
	/**
	 * Displays the form which will allow the user to add a new part to the inventory.
	 */
	private void loadAddPartForm()
	{
		if(addPart == null)
			addPart = new AddPartForm(this);
		scene.setRoot(addPart.getScenePane());
		stage.setScene(scene);
		stage.setTitle(addPart.getSceneTitle());
	}
	
	/**
	 * Displays the form which will allow the user to modify a part existing in the inventory.
	 */
	private void loadModifyPartForm()
	{
		if(modifyPart == null)
			modifyPart = new ModifyPartForm(this);
		modifyPart.setPartToModify(main.getSelectedPart());
		scene.setRoot(modifyPart.getScenePane());
		stage.setScene(scene);
		stage.setTitle(modifyPart.getSceneTitle());
	}
	
	/**
	 * Displays the form which will allow the user to add a new part to the inventory.
	 */
	private void loadAddProductForm()
	{
		if(addProduct == null)
			addProduct = new AddProductForm(this);
		addProduct.clear();
		scene.setRoot(addProduct.getScenePane());
		stage.setScene(scene);
		stage.setTitle(addProduct.getSceneTitle());
	}
	
	/**
	 * Displays the form which will allow the user to modify a product that exists in the inventory.
	 */
	private void loadModifyProductForm()
	{
		if(modifyProduct == null)
			modifyProduct = new ModifyProductForm(this);
		modifyProduct.setProductToModify(main.getSelectedProduct());
		scene.setRoot(modifyProduct.getScenePane());
		stage.setScene(scene);
		stage.setTitle(modifyProduct.getSceneTitle());
	}
	
	/**
	 * Changes the root node of the scene (i.e. changes which form is being displayed) based on the 
	 * provided code.
	 * @param sceneCode	Integer code of the form to be displayed.
	 * @return	boolean denoting if the form was successfully changed.
	 */
	public boolean changeScene(int sceneCode)
	{
		switch(sceneCode)
		{
		case MAIN_FORM :
			loadMainForm();
			return true;
		case ADD_PART_FORM :
			loadAddPartForm();
			return true;
		case MODIFY_PART_FORM :
			loadModifyPartForm();
			return true;
		case ADD_PRODUCT_FORM :
			loadAddProductForm();
			return true;
		case MODIFY_PRODUCT_FORM :
			loadModifyProductForm();
			return true;
		}
		return false;
	}//changeScene
	
	/**
	 * Returns an ObservableList of the parts stored in the inventory.
	 * @return	ObservableList<Part> of the Parts stored in the inventory
	 */
	public ObservableList<Part> getInventoryParts()
	{
		return inventory.getAllParts();
	}
	
	/**
	 * Returns an ObservableList of the products stored in the inventory. 
	 * @return	ObservableList<Product> containing the Products stored in the inventory
	 */
	public ObservableList<Product> getInventoryProducts()
	{
		return inventory.getAllProducts();
	}
	
	/**
	 * Instantiates and adds a new in house part to the inventory. Returns true and increments the next available part id
	 * if the part was successfully added. Returns false otherwise.
	 *  
	 * @param name			name of the part to be added
	 * @param description	description of the part to be added
	 * @param price			price of the part to be added
	 * @param stock			current inventory of the part to be added
	 * @param min			minimum inventory of the part to be added
	 * @param max			maximum inventory of the part to be added
	 * @param machineId		id of the machine used to produce this part
	 * @return	boolean denoting if the part was successfully added
	 */
	public boolean addInHousePart(String name, double price, int stock, int min, int max, int machineId)
	{
		if(inventory.addPart(new InHouse(nextPartId, name, price, stock, min, max, machineId)))
		{
			
			incPartId();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Instantiates and adds a new outsourced part to the inventory. Returns true and increments the next available part id
	 * if the part was successfully added. Returns false otherwise.
	 *  
	 * @param name			name of the part to be added
	 * @param description	description of the part to be added
	 * @param price			price of the part to be added
	 * @param stock			current inventory of the part to be added
	 * @param min			minimum inventory of the part to be added
	 * @param max			maximum inventory of the part to be added
	 * @param companyName	name of the external organization supplying this part
	 * @return				boolean denoting if the part was successfully added
	 */
	public boolean addOutsourcedPart(String name, double price, int stock, int min, int max, String companyName)
	{
		if(inventory.addPart(new OutSourced(nextPartId, name, price, stock, min, max, companyName)))
		{
			incPartId();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Switches an existing in house part in the inventory to an outsourced part by removing the in house part
	 * and adding a new outsourced part.
	 * 
	 * @param p				the part to be switched
	 * @param name			name of the part to be switched
	 * @param description	description of the part to be switched
	 * @param price			price of the part to be switched
	 * @param stock			current inventory of the part to be switched
	 * @param min			minimum inventory of the part to be switched
	 * @param max			maximum inventory of the part to be switched
	 * @param companyName	name of supplying company of the part to be switched
	 * @return	boolean denoting if the new outsourced part was successfully added to the inventory
	 */
	public boolean switchInHouseToOutsourced(Part p, String name, double price, int stock, int min, int max, String companyName)
	{
		int numProducts;
		if(p instanceof InHouse)
			numProducts = ((InHouse)p).getNumProducts();
		else
			numProducts = ((OutSourced)p).getNumProducts();
		int partId = p.getId();
		Part newPart;
		
		inventory.deletePart(p);
		newPart = new OutSourced(partId, name, price, stock, min, max, companyName);
		((OutSourced)newPart).setNumProducts(numProducts);
		return inventory.addPart(newPart);
	}
	
	/**
	 * Switches an existing outsourced part in the inventory to an in house part by removing the outsourced part
	 * and adding a new in house part.
	 * 
	 * @param p				the part to be switched
	 * @param name			name of the part to be switched
	 * @param description	description of the part to be switched
	 * @param price			price of the part to be switched
	 * @param stock			current inventory of the part to be switched
	 * @param min			minimum inventory of the part to be switched
	 * @param max			maximum inventory of the part to be switched
	 * @param machineId		id of the machine used to produce this part
	 * @return	boolean denoting if the new in house part was successfully added to the inventory
	 */
	public boolean switchOutsourcedToInHouse(Part p, String name, double price, int stock, int min, int max, int machineId)
	{
		int numProducts;
		if(p instanceof InHouse)
			numProducts = ((InHouse)p).getNumProducts();
		else
			numProducts = ((OutSourced)p).getNumProducts();
		int partId = p.getId();
		Part newPart;
		
		inventory.deletePart(p);
		newPart = new InHouse(partId, name, price, stock, min, max, machineId);
		((InHouse)newPart).setNumProducts(numProducts);
		return inventory.addPart(newPart);
	}
	
	/**
	 * Removes the given part from the inventory if no products are using the given part. Returns true if the 
	 * part was removed successfully and false otherwise.
	 * 
	 * @param p	the part to be removed
	 * @return	boolean denoting if the part was successfully removed
	 */
	public boolean deletePart(Part p)
	{
		int associatedProducts;
		if(p instanceof InHouse)
			associatedProducts = ((InHouse)p).getNumProducts();
		else
			associatedProducts = ((OutSourced)p).getNumProducts();
		if(associatedProducts <= 0)
		{
			return inventory.deletePart(p);
		}
		else
			return false;
	}
	
	/**
	 * Searches the inventory for a part whose id matches the id provided by the user
	 * 
	 * @param id	id of the part for which the user is searching
	 * @return 		an ObsrevableList<Part> which contains all the parts with an id matching the provided id
	 */
	public ObservableList<Part> searchPartById(int id)
	{
		ArrayList<Part> matchingParts = new ArrayList<Part>();
		for(Part p : inventory.getAllParts())
		{
			if(p.getId() == id)
				matchingParts.add(p); //TODO: make not linear; only one matching id(?) so stop when found
		}
		return FXCollections.observableArrayList(new ArrayList<Part>(matchingParts));
	}
	
	/**
	 * Returns an ObservableList of all the parts whose name contains the given String.
	 * 
	 * @param 	str the string to search for among the names of all the parts in the inventory
	 * @return	ObservableList<Part> of Parts whose names contain the given String
	 */
	public ObservableList<Part> searchPartByName(String str)
	{
		str = str.trim();
		str = str.toLowerCase();
		ArrayList<Part> matchingParts = new ArrayList<Part>();
		for(Part p : inventory.getAllParts())
		{
			String partName = p.getName();
			partName = partName.trim();
			partName = partName.toLowerCase();
			if(partName.contains(str))
				matchingParts.add(p);
		}
		return FXCollections.observableArrayList(new ArrayList<Part>(matchingParts));
	}
	
	/**
	 * Instantiates and adds a new product to the inventory. Returns true and increments the next available product id
	 * if the part was successfully added. Returns false otherwise.
	 *  
	 * @param name				name of the product to be added
	 * @param description		description of the product to be added
	 * @param price				price of the product to be added
	 * @param stock				current inventory of the product to be added
	 * @param min				minimum inventory of the product to be added
	 * @param max				maximum inventory of the product to be added
	 * @param associatedParts	list of parts used in/associated with this product
	 * @return					boolean denoting if the product was successfully added
	 */
	public boolean addProduct(String name, double price, int stock, int min, int max, ObservableList<Part> associatedParts)
	{
		if(inventory.addProduct(new Product(nextProductId, name, price, stock, min, max, associatedParts)))
		{
			incProductId();
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Removes the given product from the inventory. Returns true if the 
	 * part was removed successfully and false otherwise.
	 * 
	 * @param p	the product to be removed
	 * @return	boolean denoting if the product was successfully removed
	 */
	public boolean deleteProduct(Product product)
	{
		if(inventory.deleteProduct(product))
		{
			for(Part part : product.getAllAssociatedParts())
			{
				if(part instanceof InHouse)
					((InHouse)part).decNumProducts();
				else
					((OutSourced)part).decNumProducts();
			}
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Searches the inventory for a part whose id matches the id provided by the user
	 * 
	 * @param id	id of the part for which the user is searching
	 * @return 		an ObsrevableList<Part> which contains all the parts with an id matching the provided id
	 */
	public ObservableList<Product> searchProductById(int id)
	{
		ArrayList<Product> matchingParts = new ArrayList<Product>();
		for(Product p : inventory.getAllProducts())
		{
			if(p.getId() == id)
				matchingParts.add(p); //TODO: make not linear; only one matching id(?) so stop when found
		}
		return FXCollections.observableArrayList(new ArrayList<Product>(matchingParts));
	}
	
	/**
	 * Returns an ObservableList of all products whose name contains the given String.
	 * 
	 * @param 	str the string to search for among the names of all products in the inventory
	 * @return	ObservableList<Product> of Products whose names contain the given String
	 */
	public ObservableList<Product> searchProductByName(String str)
	{
		str = str.trim();
		str = str.toLowerCase();
		ArrayList<Product> matchingProducts = new ArrayList<Product>();
		for(Product p : inventory.getAllProducts())
		{
			String partName = p.getName();
			partName = partName.trim();
			partName = partName.toLowerCase();
			if(partName.contains(str))
				matchingProducts.add(p);
		}
		return FXCollections.observableArrayList(new ArrayList<Product>(matchingProducts));
	}
	
	/**
	 * Increases the auto-generated part id and checks that it has not overlapped with the
	 * auto-generated product id.
	 */
	private void incPartId()
	{
		nextPartId++;
		if(nextPartId % STARTING_PRODUCT_ID == 0)
		{
			nextPartId += 1000;
		}
	}
	
	/**
	 * Increases the auto-generated product id and checks that it has not overlapped with the
	 * auto-generated part id.
	 */
	private void incProductId()
	{
		nextProductId++;
		if(nextProductId % STARTING_PRODUCT_ID == 0)
		{
			nextProductId += 1000;
		}
	}
	
	/**
	 * Closes this application.
	 */
	public void terminate()
	{
		Platform.exit();
	}
	
	/**
	 * Launches this application.
	 * @param args
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
}
