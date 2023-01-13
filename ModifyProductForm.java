package finalAssessment;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *	Form that allows user to modify a Product that exists in the inventory.
 */
public class ModifyProductForm extends Form
{
	//Integer constants for identifying columns in the part tables
	private final static int ID_COLUMN_INDEX = 0;
	private final static int NAME_COLUMN_INDEX = 1;
	private final static int DESC_COLUMN_INDEX = 2;
	
	//Declare core scene attributes
	private InventoryApp controller;
	private BorderPane contentContainer;
	
	//Declare scene controls
	private	Label 	sceneLabel;
	private Label 	idLabel;			private TextField 	idField;			private Label idErrorLabel;
	private Label 	nameLabel;			private TextField 	nameField;			private Label nameErrorLabel;
	private Label 	priceLabel;			private TextField 	priceField;			private Label priceErrorLabel;
	private Label 	minInventoryLabel;	private TextField 	minInventoryField;	private Label minErrorLabel;
	private Label 	maxInventoryLabel;	private TextField 	maxInventoryField;	private Label maxErrorLabel;
	private Label 	currentStockLabel;	private TextField 	currentStockField;	private Label stockErrorLabel;
	private Button	addPartButton;		private Button		removePartButton;
	private Button 	saveButton;			private Button 		cancelButton;
	private TableView<Part>	availablePartsTable;
	private TableColumn<Part, String> availableIdColumn;
	private TableColumn<Part, String> availableNameColumn;
	private TableView<Part> associatedPartsTable;
	private TableColumn<Part, String> associatedIdColumn;
	private TableColumn<Part, String> associatedNameColumn;
	private Image	  arrowURL;
	private ImageView addArrow;
	private ImageView removeArrow; 
	
	//Declare session variables
	private Product productToModify;
	private ObservableList<Part> startingParts;
	private ObservableList<Part> availableParts;
	private ObservableList<Part> associatedParts;
	private ObservableList<Part> selectedPart;
	private ArrayList<TableColumn<Part, String>> availableTableColumns;
	private ArrayList<TableColumn<Part, String>> associatedTableColumns;
	
	/**
	 * Instantiates all scene controls and attaches event handlers where appropriate.
	 * @param app	InventoryApp to be used as the controller for this form
	 */
	public ModifyProductForm(InventoryApp app)
	{
		controller = app;
		setSceneTitle("Add a new Product");
		
		//Instantiate scene controls
		sceneLabel 				= new Label("Add Product");		//Labels and Fields
		idLabel 				= new Label("ID");
		idField 				= new TextField();
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
		
		addPartButton			= new Button("Add Part");		//Buttons
		removePartButton		= new Button("Remove Part");
		saveButton 				= new Button("Save");
		cancelButton 			= new Button("Cancel");
		
		availablePartsTable 	= new TableView<Part>();						//Tables
		availableIdColumn		= new TableColumn<Part, String>("ID");
		availableNameColumn		= new TableColumn<Part, String>("Name");
		associatedPartsTable 	= new TableView<Part>();
		associatedIdColumn		= new TableColumn<Part, String>("ID");
		associatedNameColumn	= new TableColumn<Part, String>("Name");
		
		availableTableColumns = new ArrayList<TableColumn<Part, String>>();		//Table Columns
		availableTableColumns.add(ID_COLUMN_INDEX, availableIdColumn);
		availableTableColumns.add(NAME_COLUMN_INDEX, availableNameColumn);
		associatedTableColumns = new ArrayList<TableColumn<Part, String>>();
		associatedTableColumns.add(ID_COLUMN_INDEX, associatedIdColumn);
		associatedTableColumns.add(NAME_COLUMN_INDEX, associatedNameColumn);
		
		arrowURL = new Image("file:images/Arrow.png");		//Images
		addArrow = new ImageView(arrowURL);
		removeArrow = new ImageView(arrowURL);
		addArrow.setRotate(180);
		
		//Set initial states
		idField.setDisable(true);
		addPartButton.setDisable(true);
		removePartButton.setDisable(true);
		availableParts = controller.getInventoryParts();
		startingParts = associatedParts = FXCollections.observableArrayList(new ArrayList<Part>());
		updateFields();
		
		//Add event handlers to controls
		cancelButton.setOnAction(event -> 					//Cancel Button
		{
			this.clear();
			controller.changeScene(InventoryApp.MAIN_FORM);
		});
		availablePartsTable.setOnMouseClicked(event -> 		//TableView for available parts
		{
			selectedPart = availablePartsTable.getSelectionModel().getSelectedItems();
			if(selectedPart != null)
				addPartButton.setDisable(false);
		});
		associatedPartsTable.setOnMouseClicked(event ->		//TableView for currently associated parts 
		{
			selectedPart = associatedPartsTable.getSelectionModel().getSelectedItems();
			if(selectedPart != null)
				removePartButton.setDisable(false);
		});
		addPartButton.setOnAction(event -> 					//Button for moving a part from available parts to associated parts
		{
			Part p = getSelectedPart();
			if(p != null)
			{
				availableParts.remove(p);
				associatedParts.add(p);
				associatedPartsTable.sort();
				if(availableParts.isEmpty())
					addPartButton.setDisable(true);
				removePartButton.setDisable(false);
			}
			selectedPart = null;
		});
		removePartButton.setOnAction(event -> 				//Button for moving a part from associated parts to available parts 
		{
			Part p = getSelectedPart();
			if(p != null)
			{
				associatedParts.remove(p);
				availableParts.add(p);
				availablePartsTable.sort();
				if(associatedParts.isEmpty())
					removePartButton.setDisable(true);
				addPartButton.setDisable(false);
			}
			selectedPart = null;
		});
		saveButton.setOnAction(event -> 					//Button for validating form and saving changes
		{	
			//Disable form controls while the form is validating
			this.disableForm();
			
			//Store information which was entered in form fields
			String productName = nameField.getText();
			String productPrice = priceField.getText();
			String productStock = currentStockField.getText();
			String productMin = minInventoryField.getText();
			String productMax = maxInventoryField.getText();
			
			//Reset error borders to default borders
			resetDefaultBorders();
			resetErrorMessages();
			
			//Validate field entries
			if(validateProductForm(productName, productPrice, productMin, productMax, productStock, associatedParts))
			{
				productToModify.setName(productName);
				productToModify.setPrice(Double.parseDouble(productPrice));
				productToModify.setMin(Integer.parseInt(productMin));
				productToModify.setMax(Integer.parseInt(productMax));
				productToModify.setStock(Integer.parseInt(productStock));
				productToModify.setAssociatedParts(associatedParts);
				//find which parts were removed from starting parts and decrease
				for(Part p : startingParts) //TODO: surely there is a better way
				{
					if(!associatedParts.contains(p))
					{
						if(p instanceof InHouse)
							((InHouse)p).decNumProducts();
						else
							((OutSourced)p).decNumProducts();
					}
				}
				//find which parts were added to starting parts and increase
				for(Part p : associatedParts)
				{
					if(!startingParts.contains(p))
					{
						if(p instanceof InHouse)
							((InHouse)p).incNumProducts();
						else
							((OutSourced)p).incNumProducts();
					}
				}
				controller.changeScene(InventoryApp.MAIN_FORM);
				this.clear();
			}
			
			//Re-enable controls
			this.enableForm();
		});//saveButton
		
		//Add controls to containers
		GridPane formFieldsPane = new GridPane();
		formFieldsPane.addRow(1, sceneLabel);
		formFieldsPane.addRow(2, idLabel, 			idField, 			idErrorLabel);
		formFieldsPane.addRow(3, nameLabel, 		nameField, 			nameErrorLabel);
		formFieldsPane.addRow(5, priceLabel, 		priceField, 		priceErrorLabel);
		formFieldsPane.addRow(6, minInventoryLabel,	minInventoryField, 	minErrorLabel);
		formFieldsPane.addRow(7, maxInventoryLabel, maxInventoryField, 	maxErrorLabel);
		formFieldsPane.addRow(8, currentStockLabel, currentStockField, 	stockErrorLabel);
		
		HBox upperPartsContainer = new HBox(addArrow, addPartButton);
		HBox lowerPartsContainer = new HBox(removePartButton, removeArrow);
		VBox partsButtonContainer = new VBox(upperPartsContainer, lowerPartsContainer);
		HBox partsContainer = new HBox(associatedPartsTable, partsButtonContainer, availablePartsTable);
		HBox formButtonsContainer = new HBox(saveButton, cancelButton);
		
		contentContainer = new BorderPane();
		contentContainer.setTop(formFieldsPane);
		contentContainer.setCenter(partsContainer);
		contentContainer.setBottom(formButtonsContainer);
		setScenePane(contentContainer);
	}//Constructor
	
	/**
	 * Updates the provided part table to display the provided parts.
	 * 
	 * @param tableToUpdate		the part table to be updated.
	 * @param columnsToUpdate	the columns of the table to be updated.
	 * @param items				the parts for the table to display.
	 */
	private void updateTable(TableView<Part> tableToUpdate, ArrayList<TableColumn<Part, String>> columnsToUpdate, ObservableList<Part> items)
	{
		tableToUpdate.setItems(items);
		
		columnsToUpdate.get(ID_COLUMN_INDEX).setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + p.getValue().getId());
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		columnsToUpdate.get(NAME_COLUMN_INDEX).setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + p.getValue().getName());
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		
		tableToUpdate.getColumns().setAll(columnsToUpdate.get(ID_COLUMN_INDEX), columnsToUpdate.get(NAME_COLUMN_INDEX));
		tableToUpdate.sort();
	}//updateTable
	
	/**
	 *  Sets the form fields with the information of the product to be modified.
	 */
	private void updateFields()
	{
		if(productToModify == null)
			return;
		
		idField.setText("" + productToModify.getId());
		nameField.setText(productToModify.getName());
		priceField.setText("" + productToModify.getPrice());
		minInventoryField.setText("" + productToModify.getMin());
		maxInventoryField.setText("" + productToModify.getMax());
		currentStockField.setText("" + productToModify.getStock());
		
		availableParts = controller.getInventoryParts();
		associatedParts = productToModify.getAllAssociatedParts();
		startingParts = productToModify.getAllAssociatedParts();
		for(Part p : associatedParts)
		{
			availableParts.remove(p);
		}
		updateTable(availablePartsTable, availableTableColumns, availableParts);
		updateTable(associatedPartsTable, associatedTableColumns, associatedParts);
	}//updateFields
	
	/**
	 * Returns the part currently selected by the user either from the available parts table or the
	 * associated parts table.
	 * @return Part that is currently selected
	 */
	public Part getSelectedPart()
	{
		if(selectedPart == null)
			return null;
		else if(!selectedPart.isEmpty())
			return selectedPart.get(0);
		else
			return null;
	}
	
	/**
	 * Sets the product to be modified by this form.
	 * @param p	Product to be modified
	 */
	public void setProductToModify(Product p)
	{
		productToModify = p;
		updateFields();
	}
	
	/**
	 * Restores form to its default state by clearing all text fields and setting the 'in house'
	 * radio control to be selected
	 */
	@Override
	public void clear()
	{
		//Set all fields to be blank and in house radio button to be selected
		nameField.setText("");
		priceField.setText("");
		minInventoryField.setText("");
		maxInventoryField.setText("");
		currentStockField.setText("");
		
		//Set all error labels to be blank
		resetErrorMessages();
		
		//Display the parts tables
		availableParts = controller.getInventoryParts(); //TODO: ???
		associatedParts = FXCollections.observableArrayList(new ArrayList<Part>());
		updateTable(availablePartsTable, availableTableColumns, availableParts);
		updateTable(associatedPartsTable, associatedTableColumns, associatedParts);
		
		//Revert all fields to the default border
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
	}
	
	/**
	 * Clears the error message label by setting the text to the empty String
	 */
	private void resetErrorMessages()
	{
		nameErrorLabel.setText("");
		priceErrorLabel.setText("");
		minErrorLabel.setText("");
		maxErrorLabel.setText("");
		stockErrorLabel.setText("");
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
		//TODO: use css border control in red
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
		}
	}//flag
}