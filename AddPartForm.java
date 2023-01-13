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
 * Form which allows user to add a new part to the inventory. 
 */
public class AddPartForm extends Form
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
	
	//Declare temporary variables
	private String machineId;
	private String companyName;
	
	/**
	 * Instantiates all scene controls and assigns event handlers to those controls where applicable.
	 * 
	 * @param app 	The InventoryApp which instantiated this form; also used as the controller to interact
	 * 				with other parts of the application
	 */
	public AddPartForm(InventoryApp app)
	{
		controller = app;
		setSceneTitle("Add a new Part");
		
		//Instantiate scene controls
		sceneLabel 				= new Label("Add Part");
		sourcingLabel			= new Label("Sourcing: ");
		partSourceGroup 		= new ToggleGroup();
		inHouseRadio 			= new RadioButton("in house");
		outsourcedRadio 		= new RadioButton("outsourced");
		idLabel 				= new Label("ID");
		idField 				= new TextField("auto-generated id");
		idErrorLabel			= new Label("");
		nameLabel 				= new Label("Name");
		nameField 				= new TextField();
		nameErrorLabel			= new Label("");
		priceLabel 				= new Label("Unit Price");
		priceField 				= new TextField();
		priceErrorLabel			= new Label("");
		minInventoryLabel 		= new Label("Min");
		minInventoryField 		= new TextField();
		minErrorLabel			= new Label("");
		maxInventoryLabel 		= new Label("Max");
		maxInventoryField 		= new TextField();
		maxErrorLabel			= new Label("");
		currentStockLabel		= new Label("Inv");
		currentStockField		= new TextField();
		stockErrorLabel			= new Label("");
		sourceIdLabel 			= new Label("Machine ID");
		sourceIdField 			= new TextField();
		sourceErrorLabel		= new Label("");
		saveButton 				= new Button("Save");
		cancelButton 			= new Button("Cancel");
		
		//Set initial states
		inHouseRadio.setToggleGroup(partSourceGroup);
		outsourcedRadio.setToggleGroup(partSourceGroup);
		inHouseRadio.setSelected(true);
		idField.setDisable(true);
		machineId = "";
		companyName = "";
		
		//Add event handlers to controls
		inHouseRadio.setOnAction(event -> 
		{
				sourceIdLabel.setText("Machine ID");
				sourceIdField.setText(machineId);
		});
		outsourcedRadio.setOnAction(event -> 
		{
				sourceIdLabel.setText("Company Name");
				sourceIdField.setText(companyName);
		});
		sourceIdField.setOnKeyReleased(event -> 
		{
			if(inHouseRadio.isSelected())
			{
				try
				{
					machineId = sourceIdField.getText();
				}
				catch(NumberFormatException e)
				{
					machineId = "" + 0;
				}
			}
			else
				companyName = sourceIdField.getText();
		});
		cancelButton.setOnAction(event -> 
		{
			this.clear();
			controller.changeScene(InventoryApp.MAIN_FORM);
		});
		saveButton.setOnAction(event -> 
		{	
			//Disable form controls while the form is validating
			this.disableForm();
			
			//Store information which was entered in form fields
			String partName = nameField.getText();
			String partPrice = priceField.getText();
			String partStock = currentStockField.getText();
			String partMin = minInventoryField.getText();
			String partMax = maxInventoryField.getText();
			String partSource = sourceIdField.getText();
			boolean inHouse = inHouseRadio.isSelected();
			
			//Reset error borders to default borders
			resetDefaultBorders();
			
			//Validate field entries
			if(validatePartForm(partName, partPrice, partMin, partMax, partStock, partSource, inHouse))
			{
				//Update inventory and return to main form if no flags are present
				if(inHouse)
					controller.addInHousePart(partName, Double.parseDouble(partPrice), Integer.parseInt(partStock), Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partSource));
				else
					controller.addOutsourcedPart(partName, Double.parseDouble(partPrice), Integer.parseInt(partStock), Integer.parseInt(partMin), Integer.parseInt(partMax), partSource);
				
				controller.changeScene(InventoryApp.MAIN_FORM);
				this.clear();
			}
			
			//Re-enable controls
			this.enableForm();
		});//saveButton
		
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
	}//AddPartForm
	
	/**
	 * Restores form to its default state by clearing all text fields and setting the 'in house'
	 * radio control to be selected.
	 */
	@Override
	public void clear()
	{
		//Set all fields to be blank and in house radio button to be selected
		inHouseRadio.setSelected(true);
		nameField.setText("");
		priceField.setText("");
		minInventoryField.setText("");
		maxInventoryField.setText("");
		currentStockField.setText("");
		sourceIdField.setText("");
		
		//Set all error labels to be blank
		nameErrorLabel.setText("");
		priceErrorLabel.setText("");
		minErrorLabel.setText("");
		maxErrorLabel.setText("");
		stockErrorLabel.setText("");
		sourceErrorLabel.setText("");
		
		//Revert all fields to the default border
		resetDefaultBorders();
	}
	
	/**
	 * Sets the borders of all fields to the default border.
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
	 * Flags field input as invalid by setting the filed's border to the error border and displaying a message
	 * explaining why the input is invalid.
	 * 
	 * @param controlCode Integer code denoting which field is being flagged
	 * @param message String explaining why the input is invalid
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
