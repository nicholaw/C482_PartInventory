package finalAssessment;

/**
 *	Represents a part in the inventory which is produced in house.
 */
public class InHouse extends Part
{
	private int machineId;	//Id of the machine which produces this part
	private int numProducts;//Number of products using this part as a component part
	
	/**
	 * Instantiates an InHouse part by setting each of its attributes.
	 * 
	 * @param id	Part ID
	 * @param name	Part name
	 * @param price	Part price in USD
	 * @param stock	Quantity of part currently in stock
	 * @param min	Minimum acceptable quantity of part in stock
	 * @param max	Maximum acceptable quantity of part in stock
	 * @param machineId	Id of machine which produces this part
	 */
	public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) 
	{
		super(id, name, price, stock, min, max);
		this.machineId = machineId;
		numProducts = 0;
	}

	/**
	 * Returns the id of the machine used to produce this part.
	 * @return	Integer id
	 */
	public int getMachineId()
	{
		return machineId;
	}
	
	/**
	 * Sets the id of the machine used to produce this part.
	 * @param machineId	Integer id
	 */
	public void setMachineId(int machineId)
	{
		this.machineId = machineId;
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
