package finalAssessment;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Form which allows user to modify an existing Part in the inventory.
 * 
 * If I were to update this application in the future, I would conflate this class with the AddPart class (and the ModifyProduct class
 * with the AddProduct class) to reduce redundancy and overhead.
 */
public class ModifyPartForm extends Form
{
	//Declare core scene attributes
	private InventoryApp controller;
	private BorderPane contentContainer;
	
	//Declare scene controls
	private	Label 		sceneLabel;			private Label		sourcingLabel;
	private RadioButton	inHouseRadio;		private RadioButton outsourcedRadio;	private ToggleGroup partSourceGroup;
	private Label 		idLabel;			private TextField 	idField;			private Label idErrorLabel;
	private Label 		nameLabel;			private TextField 	nameField;			private Label nameErrorLabel;
	private Label 		priceLabel;			private TextField 	priceField;			private Label priceErrorLabel;
	private Label 		minInventoryLabel;	private TextField 	minInventoryField;	private Label minErrorLabel;
	private Label 		maxInventoryLabel;	private TextField 	maxInventoryField;	private Label maxErrorLabel;
	private Label 		currentStockLabel;	private TextField 	currentStockField;	private Label stockErrorLabel;
	private Label 		sourceIdLabel;		private TextField 	sourceIdField;		private Label sourceErrorLabel;
	private Button 		saveButton;			private Button 		cancelButton;	
	
	//Declare session attributes
	private Part	partToModify;
	private int 	machineId;
	private String 	companyName;
	
	/**
	 * Instantiates all scene controls and attaches event handlers where appropriate.
	 * @param app	InventoryApp to be used as the controller for this form
	 */
	public ModifyPartForm(InventoryApp app)
	{
		controller = app;
		setSceneTitle("Add a new Part");
		
		//Instantiate scene controls
		sceneLabel 				= new Label("Modify Part");
		sourcingLabel			= new Label("Sourcing: ");
		partSourceGroup 		= new ToggleGroup();
		inHouseRadio 			= new RadioButton("in house");
		outsourcedRadio 		= new RadioButton("outsourced");
		idLabel 				= new Label("ID");
		idField 				= new TextField("");
		idErrorLabel			= new Label("");
		nameLabel 				= new Label("Name");
		nameField 				= new TextField();
		nameErrorLabel			= new Label("");
		priceLabel 				= new Label("Unit Price");
		priceField 				= new TextField("");
		priceErrorLabel			= new Label("");
		minInventoryLabel 		= new Label("Min");
		minInventoryField 		= new TextField("");
		minErrorLabel			= new Label("");
		maxInventoryLabel 		= new Label("Max");
		maxInventoryField 		= new TextField("");
		maxErrorLabel			= new Label("");
		currentStockLabel		= new Label("Inv");
		currentStockField		= new TextField("");
		stockErrorLabel			= new Label("");
		sourceIdLabel			= new Label("Source");
		sourceIdField			= new TextField("");
		sourceErrorLabel		= new Label("");
		saveButton 				= new Button("Save");
		cancelButton 			= new Button("Cancel");
		
		//Set initial states
		inHouseRadio.setToggleGroup(partSourceGroup);
		outsourcedRadio.setToggleGroup(partSourceGroup);
		inHouseRadio.setSelected(true);
		idField.setDisable(true);
		machineId = 0;
		companyName = "";
		
		//Add event handlers to controls
		inHouseRadio.setOnAction(event -> 		//In house radio button
		{
			inHouseRadio.setSelected(true);
			sourceIdLabel.setText("Machine ID");
			sourceIdField.setText(Integer.toString(machineId));
		});
		outsourcedRadio.setOnAction(event -> 	//outsourced radio button
		{
			outsourcedRadio.setSelected(true);
			sourceIdLabel.setText("Company Name");
			sourceIdField.setText(companyName);
		});
		sourceIdField.setOnKeyReleased(event -> //Text field for entering source
		{
			if(inHouseRadio.isSelected())
			{
				try
				{
					machineId = Integer.parseInt(sourceIdField.getText());
				}
				catch(NumberFormatException e)
				{
					machineId = 0;
				}
			}
			else
				companyName = sourceIdField.getText();
		});
		cancelButton.setOnAction(event -> 		//Cancel button to return to main form
		{
			this.clear();
			controller.changeScene(InventoryApp.MAIN_FORM);
		});
		saveButton.setOnAction(event -> 		//Save button to validate form and save changes
		{	
			//Disable form controls while the form is validating
			this.disableForm();
			
			//Store input from form fields
			String partName			= nameField.getText();
			String partPrice		= priceField.getText();
			String partMin			= minInventoryField.getText();
			String partMax			= maxInventoryField.getText();
			String partStock		= currentStockField.getText();
			String partSource		= sourceIdField.getText();
			boolean isInHouse		= inHouseRadio.isSelected();
			
			//Revert any error borders back to the default border
			resetDefaultBorders();
			
			//Validate field entries
			if(validatePartForm(partName, partPrice, partMin, partMax, partStock, partSource, isInHouse))
			{
				/**
				 * If the form validated successfully, the following switch statement is included to allow for converting an InHouse
				 * part to an OutSourced part and vice versa. However, while the following code was indeed converting parts, it was
				 * not keeping track of the number of products associated with the part being converted. As it was written therefore,
				 * any part could be deleted after being converted regardless of if it was associated with any products.
				 * 
				 * This error was corrected by providing the part being converted to the controller so the controller could extract
				 * relevant information (i.e. the number or associated parts) before converting the part. The controller then uses
				 * that information to set the appropriate value in the converted part.
				 */
				if(partToModify instanceof InHouse)
				{
					if(isInHouse)	//Part source is unchanged; update part attributes
					{
						partToModify.setName(partName);
						partToModify.setPrice(Double.parseDouble(partPrice));
						partToModify.setMin(Integer.parseInt(partMin));
						partToModify.setMax(Integer.parseInt(partMax));
						partToModify.setStock(Integer.parseInt(partStock));
						((InHouse)partToModify).setMachineId(Integer.parseInt(partSource));
					}
					else			//Part source changed; delete existing part and create new part with correct source
					{
						controller.switchInHouseToOutsourced(partToModify, partName, Double.parseDouble(partPrice), Integer.parseInt(partStock), Integer.parseInt(partMin), Integer.parseInt(partMax), partSource);
					}
				}
				else
				{
					if(!isInHouse)	//Part source is unchanged; update part attributes
					{
						partToModify.setName(partName);
						partToModify.setPrice(Double.parseDouble(partPrice));
						partToModify.setMin(Integer.parseInt(partMin));
						partToModify.setMax(Integer.parseInt(partMax));
						partToModify.setStock(Integer.parseInt(partStock));
						((OutSourced)partToModify).setCompanyName(partSource);
					}
					else			//Part source changed; delete existing part and create new part with correct source
					{
						controller.switchOutsourcedToInHouse(partToModify, partName, Double.parseDouble(partPrice), Integer.parseInt(partStock), Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partSource));
					}
				}
				controller.changeScene(InventoryApp.MAIN_FORM);
				this.clear();
			}
			
			//Re-enable controls
			this.enableForm();
		});//SaveButton
		
		//Add controls to containers
		GridPane formFieldsPane = new GridPane();
		formFieldsPane.addRow(1, sourcingLabel, 	inHouseRadio, 		outsourcedRadio);
		formFieldsPane.addRow(2, idLabel, 			idField, 			idErrorLabel);
		formFieldsPane.addRow(3, nameLabel, 		nameField, 			nameErrorLabel);
		formFieldsPane.addRow(5, priceLabel, 		priceField, 		priceErrorLabel);
		formFieldsPane.addRow(6, minInventoryLabel,	minInventoryField, 	minErrorLabel);
		formFieldsPane.addRow(7, maxInventoryLabel, maxInventoryField, 	maxErrorLabel);
		formFieldsPane.addRow(8, currentStockLabel, currentStockField, 	stockErrorLabel);
		formFieldsPane.addRow(9, sourceIdLabel, 	sourceIdField, 		sourceErrorLabel);
		
		HBox sceneLabelPane	= new HBox(sceneLabel);
		HBox buttonPane 	= new HBox(saveButton, cancelButton);
		
		contentContainer = new BorderPane();
		contentContainer.setTop(sceneLabelPane);
		contentContainer.setCenter(formFieldsPane);
		contentContainer.setBottom(buttonPane);
		setScenePane(contentContainer);
	}//Controller
	
	/**
	 * Restores form to its default state by clearing all text fields and setting the 'in house'
	 * radio control to be selected
	 */
	public void clear()
	{
		nameErrorLabel.setText("");
		priceErrorLabel.setText("");
		minErrorLabel.setText("");
		maxErrorLabel.setText("");
		stockErrorLabel.setText("");
		sourceErrorLabel.setText("");
		companyName = "";
		machineId = -1;
		resetDefaultBorders();
	}
	
	/**
	 * Sets the borders of all fields to the default border
	 */
	private void resetDefaultBorders()
	{
		nameField.setBorder(getDefaultBorder());
		priceField.setBorder(getDefaultBorder());
		minInventoryField.setBorder(getDefaultBorder());
		maxInventoryField.setBorder(getDefaultBorder());
		currentStockField.setBorder(getDefaultBorder());
		sourceIdField.setBorder(getDefaultBorder());
	}
	
	/**
	 * Sets the part which can be modified by this form
	 * 
	 * @param p
	 */
	public void setPartToModify(Part p)
	{
		partToModify = p;
		updateFields();
	}
	
	/**
	 * Updates all the form text fields with information from the part the 
	 * user selected to modify
	 */
	private void updateFields()
	{
		if(partToModify instanceof InHouse)
		{
			inHouseRadio.setSelected(true);
			sourceIdLabel.setText("Machine ID");
			sourceIdField.setText("" + ((InHouse) partToModify).getMachineId());
			machineId = ((InHouse)partToModify).getMachineId();
			companyName = "";
		}
		else
		{
			outsourcedRadio.setSelected(true);
			sourceIdLabel.setText("Company Name");
			sourceIdField.setText(((OutSourced)partToModify).getCompanyName());
			machineId = 0;
			companyName = ((OutSourced)partToModify).getCompanyName();
		}
		idField.setText("" + partToModify.getId());
		nameField.setText(partToModify.getName());
		priceField.setText("" + partToModify.getPrice());
		minInventoryField.setText("" + partToModify.getMin());
		maxInventoryField.setText("" + partToModify.getMax());
		currentStockField.setText("" + partToModify.getStock());
	}
	
	/**
	 * Highlights a field that contains invalid input and displays a message detailing why
	 * the input is invalid.
	 * 
	 * @param controlCode	Integer code identifying the field with invalid input
	 * @param message		String to be displayed explaining why the input is invalid	
	 */
	@Override
	protected void flag(int controlCode, String message)
	{
		switch(controlCode)
		{
		case Form.NAME_FIELD :
			nameErrorLabel.setText(message);
			nameField.setBorder(getErrorBorder());
			break;
		case Form.PRICE_FIELD :
			priceErrorLabel.setText(message);
			priceField.setBorder(getErrorBorder());
			break;
		case Form.MIN_FIELD :
			minErrorLabel.setText(message);
			minInventoryField.setBorder(getErrorBorder());
			break;
		case Form.MAX_FIELD :
			maxErrorLabel.setText(message);
			maxInventoryField.setBorder(getErrorBorder());
			break;
		case Form.INV_FIELD :
			stockErrorLabel.setText(message);
			currentStockField.setBorder(getErrorBorder());
			break;
		case Form.SOURCE_FIELD :
			sourceErrorLabel.setText(message);
			sourceIdField.setBorder(getErrorBorder());
			break;
		}
	}//flag
}