package finalAssessment;

/**
 *	Represents and outsourced (i.e. provided by an external organization) part in the inventory.
 */
public class OutSourced extends Part
{
	private String companyName;	//Name of the external company which supplies this part
	private int numProducts;//Number of products using this part as a component part
	
	/**
	 * Set all part attributes to the provided values
	 * @param id		integer id of this part
	 * @param name		name of this part
	 * @param price		price of this part
	 * @param stock		current inventory of this part
	 * @param min		minimum inventory of this part
	 * @param max		maximum inventory of this part
	 * @param compName	name of organization to supply this part
	 */
	public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) 
	{
		super(id, name, price, stock, min, max);
		this.companyName = companyName;
	}
	
	/**
	 * Returns the name of the organization supplying this part.
	 * @return companyName	name of the external organization
	 */
	public String getCompanyName()
	{
		return companyName;
	}
	
	/**
	 * Sets the name of the organization supplying this part.
	 * @param companyName	name of the external organization
	 */
	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}
	
	/**
	 * Returns the number of products using this part as a component part.
	 * @return	the number of products
	 */
	public int getNumProducts()
	{
		return numProducts;
	}
	
	/**
	 * Sets the number of products using this part as a component part.
	 * @param num	the number of products
	 */
	public void setNumProducts(int num)
	{
		numProducts = num;
	}
	
	/**
	 * Increments the number of products associated with this part
	 */
	public void incNumProducts()
	{
		numProducts++;
	}
	
	/**
	 * Decrements the number of products associated with this part
	 */
	public void decNumProducts()
	{
		numProducts--;
	}
}