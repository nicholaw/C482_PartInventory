package finalAssessment;

import java.util.Collection;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Represents a form (window) the user interacts with to observe and modify the inventory.
 */
public abstract class Form 
{
	//Constants used for identifying controls on relevant forms
	protected static final int NAME_FIELD	= 0;
	protected static final int PRICE_FIELD	= 1;
	protected static final int MIN_FIELD	= 2;
	protected static final int MAX_FIELD	= 3;
	protected static final int INV_FIELD	= 4;
	protected static final int SOURCE_FIELD = 5;
	protected static final Border defaultBorder	= null;
	protected static final Border errorBorder 	= new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));;
	
	private	String		sceneTitle;
	private int			xDimension;
	private int 		yDimension;
	private Pane		topScenePane;
	//private Border		defaultBorder	= null;
	//private Border		errorBorder 	= new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));;
	
	/**
	 * Sets the title to be displayed on the window containing this form.
	 * @param str	the String to be displayed
	 */
	public void setSceneTitle(String str)
	{
		sceneTitle = str;
	}
	
	/**
	 * Returns the title to be displayed on the window containing this form.
	 * @return	the String to be displayed
	 */
	public String getSceneTitle()
	{
		return sceneTitle;
	}
	
	/**
	 * Sets the preferred width of this form.
	 * @param x	Integer of preferred width in pixels 
	 */
	public void setX(int x)
	{
		xDimension = x;
	}
	
	/**
	 * Returns the preferred width of this form.
	 * @return	the preferred width of this form in pixels
	 */
	public int getX()
	{
		return xDimension;
	}
	
	/**
	 * Sets the preferred height of this form.
	 * @param y	Integer of the preferred height in pixels
	 */
	public void setY(int y)
	{
		yDimension = y;
	}
	
	/**
	 * Returns the preferred height of this form.
	 * @return	the preferred height of this form in pixels
	 */
	public int getY()
	{
		return yDimension;
	}
	
	/**
	 * Sets the top most pane to be used as the root node of the form.
	 * @param p	Pane to be used as a root node
	 */
	public void setScenePane(Pane p)
	{
		topScenePane = p;
	}
	
	/**
	 * Returns the top most pane to be used as the root node of the form.
	 * @return	Pane to be used as a root node
	 */
	public Pane getScenePane()
	{
		return topScenePane;
	}
	
	/**
	 * Returns the Border used by fields which do not contain invalid input.
	 * @return	Border used by valid form fields
	 */
	protected Border getDefaultBorder()
	{
		return defaultBorder;
	}
	
	/**
	 * Returns the Border used to identify fields which contain invalid input.
	 * @return	Border used by invalid form fields
	 */
	protected Border getErrorBorder()
	{
		return errorBorder;
	}
	
	/**
	 * Checks that the input in each field for adding or modifying a part is valid. 
	 * Returns true if every input is valid and false otherwise
	 * 
	 * @param name			String retrieved from the name field
	 * @param description 	String retrieved from the description area
	 * @param price 		String retrieved from the price field
	 * @param min			String retrieved from the minimum inventory field
	 * @param max			String retrieved from the maximum inventory field
	 * @param stock			String retrieved from the current inventory field
	 * @param source		String retrieved from the company name/machine id field
	 * @param inHouse		boolean denoting if the in house radio button was selected
	 * @return	boolean representing whether all fields on the form contain valid input
	 */
	protected boolean validatePartForm(String name, String price, String min, String max, String stock, String source, boolean inHouse)
	{
		boolean valid = true;
		
		//Check that the name isn't blank and is alphanumeric
		if(name.isEmpty() || name.isBlank())
		{
			flag(NAME_FIELD, "name is required");
			valid = false;
		}
		else if(!this.isValidName(name))
		{
			flag(NAME_FIELD, "name can only contain letters or numbers");
			valid = false;
		}
		
		//Check that the price is a double and is non-negative
		double partPrice;
		try
		{
			partPrice = Double.parseDouble(price);
			if(partPrice < 0)
			{
				flag(PRICE_FIELD, "price cannot be negative");
				valid = false;
			}
		}
		catch(NumberFormatException e)
		{
			flag(PRICE_FIELD, "price must be entered as a double");
			valid = false;
		}
		
		//check that the minimum inventory is an integer and is greater than or equal to zero
		Integer partMin;
		try
		{
			partMin = Integer.parseInt(min);
			if(partMin < 0)
			{
				flag(MIN_FIELD, "minimum inventory cannot be negative");
				valid = false;
			}
		}
		catch(NumberFormatException e)
		{
			flag(MIN_FIELD, "minimum inventory must be entered as an integer");
			partMin = null;
			valid = false;
		}
		
		//check that the maximum inventory is an integer and is greater than or equal to minimum inventory
		Integer partMax;
		try
		{
			partMax = Integer.parseInt(max);
			if(partMin != null && partMax < partMin)
			{
				flag(MAX_FIELD, "maximum inventory cannot be less than minimum inventory");
				valid = false;
			}
		}
		catch(NumberFormatException e)
		{
			flag(MAX_FIELD, "maximum inventory must be entered as an integer");
			partMax = null;
			valid = false;
		}
		
		//check that the current inventory is an integer and is greater than minimum inventory and less than maximum inventory
		int partInv;
		try
		{
			partInv = Integer.parseInt(stock);
			if(partInv < 0)
			{
				flag(INV_FIELD, "current inventory cannot be negative");
				valid = false;
			}
			else if(partMin != null && (partInv < partMin))
			{
				flag(INV_FIELD, "current inventory cannot be less than minimum inventory");
				valid = false;
			}
			else if(partMax != null && (partInv > partMax))
			{
				flag(INV_FIELD, "current inventory cannot be more than maximum inventory");
				valid = false;
			}
		}
		catch(NumberFormatException e)
		{
			flag(INV_FIELD, "current inventory must be entered as an integer");
			valid = false;
		}
		return valid;
	}//validatePartForm
	
	/**
	 * Checks that the input in each field for adding or modifying a product is valid. 
	 * Returns true if every input is valid and false otherwise
	 * 
	 * @param name			String retrieved from the name field
	 * @param description 	String retrieved from the description area
	 * @param price 		String retrieved from the price field
	 * @param min			String retrieved from the minimum inventory field
	 * @param max			String retrieved from the maximum inventory field
	 * @param stock			String retrieved from the current inventory field
	 * @param source		String retrieved from the company name/machine id field
	 * @param inHouse		boolean denoting if the in house radio button was selected
	 * @return	boolean representing whether all fields on the form contain valid input
	 */
	protected boolean validateProductForm(String name, String price, String min, String max, String stock, Collection<Part> associatedParts)
	{
		boolean valid = true;
		
		//Check that the name isn't blank and is alphanumeric
		if(name.isEmpty() || name.isBlank())
		{
			flag(NAME_FIELD, "name is required");
			valid = false;
		}
		else if(!this.isValidName(name))
		{
			flag(NAME_FIELD, "name can only contain letters or numbers");
			valid = false;
		}
		
		//Check that the price is a double and is non-negative
		double productPrice;
		try
		{
			 productPrice = Double.parseDouble(price);
			if(productPrice < 0)
			{
				flag(PRICE_FIELD, "price cannot be negative");
				valid = false;
			}
		}
		catch(NumberFormatException e)
		{
			flag(PRICE_FIELD, "price must be entered as a double");
			valid = false;
		}
		
		//check that the minimum inventory is an integer and is greater than or equal to zero
		Integer productMin;
		try
		{
			productMin = Integer.parseInt(min);
			if(productMin < 0)
			{
				flag(MIN_FIELD, "minimum inventory cannot be negative");
				valid = false;
			}
		}
		catch(NumberFormatException e)
		{
			flag(MIN_FIELD, "minimum inventory must be entered as an integer");
			productMin = null;
			valid = false;
		}
		
		//check that the maximum inventory is an integer and is greater than or equal to minimum inventory
		Integer productMax;
		try
		{
			productMax = Integer.parseInt(max);
			if(productMin != null && productMax < productMin)
			{
				flag(MAX_FIELD, "maximum inventory cannot be less than minimum inventory");
				valid = false;
			}
		}
		catch(NumberFormatException e)
		{
			flag(MAX_FIELD, "maximum inventory must be entered as an integer");
			productMax = null;
			valid = false;
		}
		
		//check that the current inventory is an integer and is greater minimum and less than maximum
		int productInv;
		try
		{
			productInv = Integer.parseInt(stock);
			if(productInv < 0)
			{
				flag(INV_FIELD, "current inventory cannot be negative");
				valid = false;
			}
			else if(productMin != null && (productInv < productMin))
			{
				flag(INV_FIELD, "current inventory cannot be less than minimum inventory");
				valid = false;
			}
			else if(productMax != null && (productInv > productMax))
			{
				flag(INV_FIELD, "current inventory cannot be more than maximum inventory");
				valid = false;
			}
		}
		catch(NumberFormatException e)
		{
			flag(INV_FIELD, "current inventory must be entered as an integer");
			valid = false;
		}
		return valid;
	}//validateProductForm
	
	/**
	 * Returns true if the provided String contains only letters or digits and false otherwise.
	 * 
	 * @param str
	 * @return	boolean denoting if a name is valid
	 */
	private boolean isValidName(String str)
	{
		return str.matches("^[a-zA-Z0-9 ]*$");
	}
	
	/**
	 * Disables the root node of this form and, by extension, all other form controls.
	 */
	public void disableForm()
	{
		topScenePane.setDisable(true);
	}
	
	/**
	 * Enables the root node of this form and, by extension, all other form controls.
	 */
	public void enableForm()
	{
		topScenePane.setDisable(false);
	}
	
	protected abstract void flag(int controlCode, String errorMessage);
	
	public abstract void clear();
}
