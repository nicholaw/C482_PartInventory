package finalAssessment;

import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 *	Form which gives an overview of the parts and products in the inventory. Also allows user to search for or delete parts
 *	and products as well as link to forms to add or modify parts and products. User may exit the application from this form.
 */
public class MainForm extends Form
{	
	private InventoryApp controller;
	
	//Declare all controls on the main form
	private Label 		sceneLabel;						//Labels, Fields, and Buttons
	private Label 		partListLabel;
	private Label		productListLabel;
	private TextField 	partSearchField;
	private TextField	productSearchField;
	//private Button		partSearchButton;
	//private Button		productSearchButton;
	//private Button		clearPartSearchButton;
	//private Button		clearProductSearchButton;
	private Button 		addPartButton;
	private Button 		modifyPartButton;
	private Button 		deletePartButton;
	private Button		addProductButton;
	private Button		modifyProductButton;
	private Button		deleteProductButton;
	private Button		exitButton;
	private Alert		deleteErrorAlert;
	private Alert		noMatchesAlert;
	private Alert		confirmDeleteAlert;
	
	private TableView<Part>	partList;					//Table for displaying parts
	private TableColumn<Part, String> partIdCol;
	private TableColumn<Part, String> partNameCol;
	private TableColumn<Part, String> partPriceCol;
	private TableColumn<Part, String> partMinCol;
	private TableColumn<Part, String> partMaxCol;
	private TableColumn<Part, String> partStockCol;
	private TableColumn<Part, String> partSourceCol;
	
	private TableView<Product> productList;				//Table for displaying products
	private TableColumn<Product, String> productIdCol;
	private TableColumn<Product, String> productNameCol;
	private TableColumn<Product, String> productPriceCol;
	private TableColumn<Product, String> productMinCol;
	private TableColumn<Product, String> productMaxCol;
	private TableColumn<Product, String> productStockCol;
	
	//Declare session variables
	private ObservableList<Part>	selectedPart;
	private ObservableList<Product>	selectedProduct;
	private Border tableBorder;
	
	//Declare all containers used for organizing nodes on the main form
	private BorderPane	contentPane;
	private BorderPane	partsPane;			private BorderPane	productsPane;
	private HBox 		partButtonsPane;	private HBox		productButtonsPane;
	private HBox		partSearchPane;		private HBox		productSearchPane;
	private GridPane	partHeaderPane;		private GridPane	productHeaderPane;
	private HBox		tablesPane;
	private HBox		terminationPane;
	
	/**
	 * Instantiates all form controls and attaches event handlers where appropriate. Also sets the controller 
	 * for this form and set any initial control states.
	 * 
	 * @param app	InventoryApp to be used as the controller
	 */
	public MainForm(InventoryApp app)
	{	
		//Set controller
		controller = app;
		
		//Instantiate scene controls 
		sceneLabel			= 	new Label("Inventory Management System");	//Labels and Fields
		partListLabel		=	new Label("Parts");
		partSearchField		=	new TextField("");
		productListLabel	= 	new	Label("Products");
		productSearchField	=	new TextField("");
		
		partList			= 	new TableView<Part>(controller.getInventoryParts());	//Part Table
		partIdCol			= 	new TableColumn<Part, String>("ID");
		partNameCol			= 	new TableColumn<Part, String>("Name");
		partPriceCol		= 	new TableColumn<Part, String>("Price");
		partMinCol			= 	new TableColumn<Part, String>("Min");
		partMaxCol			= 	new TableColumn<Part, String>("Max");
		partStockCol		= 	new TableColumn<Part, String>("Stock");
		partSourceCol		= 	new TableColumn<Part, String>("Source");
		
		productList			=	new TableView<Product>(controller.getInventoryProducts());	//Product Table
		productIdCol		= 	new TableColumn<Product, String>("ID");
		productNameCol		= 	new TableColumn<Product, String>("Name");
		productPriceCol		= 	new TableColumn<Product, String>("Price");
		productMinCol		= 	new TableColumn<Product, String>("Min");
		productMaxCol		= 	new TableColumn<Product, String>("Max");
		productStockCol		= 	new TableColumn<Product, String>("Stock");
		
		//partSearchButton		= 	new Button("Search");			//Part Buttons
		//clearPartSearchButton	= 	new Button("Clear Search");
		addPartButton 			=	new Button("Add");
		modifyPartButton 		=	new Button("Modify");
		deletePartButton		=	new Button("Delete");
		
		//productSearchButton		= 	new Button("Search");			//Product Buttons
		//clearProductSearchButton= 	new Button("Clear Search");
		addProductButton 		=	new Button("Add");
		modifyProductButton 	=	new Button("Modify");
		deleteProductButton		=	new Button("Delete");
		
		exitButton				= 	new Button("Exit");				//Exit Button and Alerts
		deleteErrorAlert		= 	new Alert(AlertType.INFORMATION, "That part could not be deleted " +
												"because it is being used in a product");
		noMatchesAlert			= 	new Alert(AlertType.INFORMATION, "");
		confirmDeleteAlert		=	new Alert(AlertType.CONFIRMATION, "Are you sure you would like to delete this item?");
		
		//Add event handlers to controls
		addPartButton.setOnAction(event -> 				//addPartButton 
		{
			controller.changeScene(InventoryApp.ADD_PART_FORM);
		});
		modifyPartButton.setOnAction(event -> 			//modifyPartButton
		{
			controller.changeScene(InventoryApp.MODIFY_PART_FORM);
		});
		deletePartButton.setOnAction(event ->			//deletePartButton
		{
			Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
			if (result.get() == ButtonType.OK)
			{
				if(!(controller.deletePart(getSelectedPart())))
				{
					deleteErrorAlert.showAndWait();
				}
				else
				{
					this.updatePartList(controller.getInventoryParts());
				}
			}
		});
		addProductButton.setOnAction(event -> 			//addProductButton
		{
			controller.changeScene(InventoryApp.ADD_PRODUCT_FORM);
		});
		modifyProductButton.setOnAction(event -> 		//modifyProductButton
		{
			controller.changeScene(InventoryApp.MODIFY_PRODUCT_FORM);
		});
		deleteProductButton.setOnAction(event -> 		//deleteProductButton
		{
			Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
			if (result.get() == ButtonType.OK)
			{
				controller.deleteProduct(getSelectedProduct());
				this.updateProductList(controller.getInventoryProducts());
			}
		});
		exitButton.setOnAction(event ->					//exitButton
		{
			controller.terminate();
		});
		/*partSearchButton.setOnAction(event ->			//partSearchButton
		{
			disableForm();
			ObservableList<Part> matchingItems;
			String searchParam = partSearchField.getText();
			if(!(searchParam.isEmpty()))
			{
				try
				{	matchingItems = controller.searchPartById(Integer.parseInt(searchParam));
					if(matchingItems != null && matchingItems.isEmpty())
					{
						updatePartList(matchingItems);
					}
					else
					{
						this.showSearchError(searchParam);
					}
				}
				catch(NumberFormatException e)
				{
					matchingItems = controller.searchPartByName(searchParam);
					if(matchingItems != null && !matchingItems.isEmpty())
					{
						updatePartList(matchingItems);
					}
					else
					{
						showSearchError(searchParam);
					}
				}
			}
			enableForm();
			clearPartSearchButton.setDisable(false);
		});*///partSearchButton	
		partSearchField.setOnKeyReleased(event -> 		//searchPartField
		{
			if(partSearchField.getText().isEmpty())
			{
				//clearPartSearchButton.setDisable(true);
				updatePartList(controller.getInventoryParts());
			}
		});
		/*clearPartSearchButton.setOnAction(event -> 		//clearPartSearchButton
		{
			clearPartSearchButton.setDisable(true);
			updatePartList(controller.getInventoryParts());
		});*/
		/*productSearchButton.setOnAction(event -> 		//productSearchButton
		{
			disableForm();
			String searchParam = productSearchField.getText();
			ObservableList<Product> matchingItems;
			try
			{
				matchingItems = controller.searchProductById(Integer.parseInt(searchParam));
			}
			catch(NumberFormatException e)
			{
				matchingItems = controller.searchProductByName(searchParam);
			}
			if(matchingItems != null && !matchingItems.isEmpty())
			{
				updateProductList(matchingItems);
			}
			else
			{
				showSearchError(searchParam);
			}
			clearProductSearchButton.setDisable(false);
			enableForm();
		});*/
		productSearchField.setOnKeyReleased(event -> 		//searchProductField
		{
			if(productSearchField.getText().isEmpty())
			{
				//clearProductSearchButton.setDisable(true);
				updateProductList(controller.getInventoryProducts());
			}
		});
		partList.setOnMouseClicked(event -> 				//partTable
		{
			selectedPart = partList.getSelectionModel().getSelectedItems();
			if(!selectedPart.isEmpty())
			{
				modifyPartButton.setDisable(false);
				deletePartButton.setDisable(false);
			}
		});
		productList.setOnMouseClicked(event -> 				//productTable
		{
			selectedProduct = productList.getSelectionModel().getSelectedItems();
			if(!selectedProduct.isEmpty())
			{
				modifyProductButton.setDisable(false);
				deleteProductButton.setDisable(false);
			}
		});
		
		//Set initial control states
		modifyPartButton.setDisable(true);
		deletePartButton.setDisable(true);
		//clearPartSearchButton.setDisable(true);
		modifyProductButton.setDisable(true);
		deleteProductButton.setDisable(true);
		//clearProductSearchButton.setDisable(true);
		partSearchField.setPromptText("Enter part name or id");
		productSearchField.setPromptText("Enter product name or id");
		deleteErrorAlert.setHeaderText(null);
		deleteErrorAlert.setTitle("Deletion Error");
		noMatchesAlert.setHeaderText(null);
		noMatchesAlert.setTitle("No Matches Found");
		confirmDeleteAlert.setHeaderText(null);
		confirmDeleteAlert.setTitle("Confirm Delete Action");
		
		//Place nodes in containers
		partSearchPane		= new HBox(partSearchField);
		partHeaderPane		= new GridPane();
		partHeaderPane.add(partListLabel, 0, 0);
		partHeaderPane.add(partSearchPane, 1, 0);
		partButtonsPane		= new HBox(addPartButton, modifyPartButton, deletePartButton);
		partsPane 			= new BorderPane();
		partsPane.setTop(partHeaderPane);
		partsPane.setCenter(partList);
		partsPane.setBottom(partButtonsPane);
		productSearchPane	= new HBox(productSearchField);
		productHeaderPane	= new GridPane();
		productHeaderPane.add(productListLabel, 0, 0);
		productHeaderPane.add(productSearchPane, 1, 0);
		productButtonsPane	= new HBox(addProductButton, modifyProductButton, deleteProductButton);
		productsPane 		= new BorderPane();
		productsPane.setTop(productHeaderPane);
		productsPane.setCenter(productList);
		productsPane.setBottom(productButtonsPane);
		tablesPane			= new HBox(partsPane, productsPane);
		terminationPane 	= new HBox(exitButton);
		contentPane			= new BorderPane();
		contentPane.setTop(sceneLabel);
		contentPane.setCenter(tablesPane);
		contentPane.setBottom(terminationPane);
		
		//Set basic scene attributes
		setSceneTitle("Inventory Orverview");
		contentPane.setPrefSize(1000, 300);
		setScenePane(contentPane);
		
		//Stylize nodes and containers TODO:
		tableBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5.0), new BorderWidths(2.0)));
		partsPane.setBorder(tableBorder);
		productsPane.setBorder(tableBorder);
		partHeaderPane.setBorder(tableBorder);
		partSearchPane.setBorder(Form.errorBorder);
		partSearchPane.setAlignment(Pos.CENTER_RIGHT);
		partButtonsPane.setAlignment(Pos.CENTER_RIGHT);
		productSearchPane.setAlignment(Pos.CENTER_RIGHT);
		productButtonsPane.setAlignment(Pos.CENTER_RIGHT);
		terminationPane.setAlignment(Pos.CENTER_RIGHT);
		contentPane.setStyle("-fx-padding:15px;");
		tablesPane.setStyle("-fx-padding:100px;");
	}//constructor
	
	/**
	 * Updates the TableView which displays parts in the inventory with the provided ObservableList.
	 * 
	 * @param	List of items to be displayed
	 */
	private void updatePartList(ObservableList<Part> list)
	{
		//Update the associated observable list
		partList.setItems(list);
		
		//Display cell values
		partIdCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
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
		partNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
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
		partPriceCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + (p.getValue().getPrice()));
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		partMinCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + (p.getValue().getMin()));
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		partMaxCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + (p.getValue().getMax()));
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		partStockCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + (p.getValue().getStock()));
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		partSourceCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Part, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Part, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		        	if(p.getValue() instanceof InHouse)
		        		return new SimpleStringProperty("" + (((InHouse)(p.getValue())).getMachineId()));
		        	else
		        		return new SimpleStringProperty(((OutSourced)(p.getValue())).getCompanyName());
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		partList.getColumns().setAll(partIdCol, partNameCol, partPriceCol, partMinCol, partMaxCol, partStockCol, partSourceCol);
		partList.sort();
	}//updatePartList
	
	/**
	 * Updates the TableView which displays products in the inventory with the provided ObservableList.
	 * 
	 * @param	List of items to be displayed
	 */
	private void updateProductList(ObservableList<Product> list)
	{
		//Update the associated observable list
		productList.setItems(list);
		
		//Display cell values
		productIdCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> p) 
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
		productNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> p) 
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
		productPriceCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + (p.getValue().getPrice()));
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		productMinCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + (p.getValue().getMin()));
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		productMaxCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + (p.getValue().getMax()));
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		productStockCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() 
		{
		    @Override
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Product, String> p) 
		    {
		        if (p.getValue() != null) 
		        {
		            return new SimpleStringProperty("" + (p.getValue().getStock()));
		        } 
		        else 
		        {
		            return new SimpleStringProperty("--");
		        }
		    }
		});
		productList.getColumns().setAll(productIdCol, productNameCol, productPriceCol, productMinCol, productMaxCol, productStockCol);
		productList.sort();
	}//updateProductList
	
	private void showSearchError(String searchParam)
	{
		noMatchesAlert.setContentText("No matches were found for '" + searchParam + "'");
		noMatchesAlert.showAndWait();
	}//showSearchError
	
	/**
	 * Returns the part currently selected by the user in the part table.
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
	 * Returns the product currently selected by the user in the product table.
	 */
	public Product getSelectedProduct()
	{
		if(selectedProduct == null)
			return null;
		else if(!selectedProduct.isEmpty())
			return selectedProduct.get(0);
		else
			return null;
	}
	
	/**
	 * Highlights a field that contains invalid input.
	 * @param controlCode	Integer code identifying the field with invalid input
	 * @param message		String to be displayed explaining why the input is invalid	
	 */
	@Override
	protected void flag(int controlCode, String message)
	{
		return;
	}
	
	/**
	 * Resets this form to its default state.
	 */
	@Override
	public void clear()
	{
		updatePartList(controller.getInventoryParts());
		updateProductList(controller.getInventoryProducts());
		//clearPartSearchButton.setDisable(true);
		//clearProductSearchButton.setDisable(true);
		deletePartButton.setDisable(true);
		modifyPartButton.setDisable(true);
		deleteProductButton.setDisable(true);
		modifyProductButton.setDisable(true);
		selectedPart = null;
		selectedProduct = null;
	}
}