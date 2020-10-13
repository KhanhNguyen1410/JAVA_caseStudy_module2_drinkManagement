package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.controller.account.MyAdminControl;
import sample.controller.login.file.ReadFile;
import sample.model.Product;

import java.io.*;
import java.net.URL;
//import java.sql.Struct;
import java.text.NumberFormat;
import java.util.*;

public class AdminController implements Initializable, MyAdminControl {
    @FXML
    private TableView<Product> tableView;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Integer> priceColumn;

    @FXML
    private TableColumn<Product, String> typeColumn;

    @FXML
    private TextField idText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField priceText;
    @FXML
    private ChoiceBox typeText;
    @FXML
    private Text userText;
    @FXML
    private Button editButton;
    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;

    @FXML
    private TextField searchText;

    private ObservableList<Product> productList;
    private ObservableList<String> menuType;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productList = FXCollections.observableArrayList(
                ReadFile.productMenu()
        );
        menuType = FXCollections.observableArrayList("Soft drink","Alcohol","energy drink");

        typeText.setValue("Soft drink");
        typeText.setItems(menuType);

        idColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("price"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("typeDrink"));
        tableView.setItems(productList);
        this.search();
    }

//    public int autoId(){
//
//    }
    @Override
    public void add() {
        Product newPro = new Product();
        try {
            int id = Integer.parseInt(idText.getText());
            String name = nameText.getText();
//            NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
//            NumberFormat numberFormat= NumberFormat.getNumberInstance(new Locale("Us", "en"));
            newPro.setId(id);
            newPro.setName(name);
            newPro.setPrice(Integer.parseInt(priceText.getText()));
            newPro.setTypeDrink((String) typeText.getValue());
            if (!checkId(id) && !checkName(name)) {
                productList.add(newPro);
                reset();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("product is existed");
                alert.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("plz enter enough element");
            alert.show();
        }

        saveFile();
    }
    @Override
    public void reset() {
        idText.clear();
        nameText.clear();
        priceText.clear();
    }
    @Override
    public void edit() {
        Product selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            idText.setText(String.valueOf(selected.getId()));
            nameText.setText(selected.getName());
            typeText.setValue(selected.getTypeDrink());
            priceText.setText(String.valueOf(selected.getPrice()));
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("plz choose a product");
            alert.show();
        }
//        updateButton.setDisable(false);
        addButton.setDisable(true);
        deleteButton.setDisable(true);
        editButton.setDisable(true);
    }
    @Override
    public void update() {
        try {
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getId()==Integer.parseInt(idText.getText())) {
                    productList.get(i).setId(Integer.parseInt(idText.getText()));
                    productList.get(i).setName(nameText.getText());
                    productList.get(i).setPrice(Integer.parseInt(priceText.getText()));
                    productList.get(i).setTypeDrink((String) typeText.getValue());
                    productList.set(i, productList.get(i));
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("plz enter enough element");
            alert.show();
        }
        addButton.setDisable(false);
        deleteButton.setDisable(false);
        editButton.setDisable(false);
    }

    @Override
    public void delete(ActionEvent event) {
        Product selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure?");
            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> choice = alert.showAndWait();
            if (choice.get().getButtonData() == ButtonBar.ButtonData.YES) {
                productList.remove(selected);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText(null);
            alert.setContentText("Please choose one!");
            alert.showAndWait();
        }

    }
    public void saveFile() {
        try {
            FileOutputStream fos = new FileOutputStream("drink.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Product product : productList) {
                oos.writeObject(product);
            }
//            oos.writeObject(productList);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean checkId(int id) {
        for (int i = 0; i < productList.size(); i++) {
            if (id == productList.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkName(String name) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void logOut(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/LoginView.fxml"));
        Parent viewParent = loader.load();
        Scene scene = new Scene(viewParent);
        stage.setScene(scene);
        stage.show();

    }
    void search() {
        FilteredList<Product> searchList = new FilteredList<>(productList, b-> true);
        searchText.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchList.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowercaseValue = newValue.toLowerCase();
                if (String.valueOf(product.getId()).indexOf(lowercaseValue) != -1) return true;
                else if (product.getTypeDrink().toLowerCase().indexOf(lowercaseValue) != -1) return true;
                else if (product.getName().toLowerCase().indexOf(lowercaseValue) != -1) return true;
                else if (String.valueOf(product.getPrice()).indexOf(lowercaseValue) != -1) return true;
                else return false;
            });
        }));
        tableView.setItems(searchList);
    }
    public boolean checkNull() {
        boolean idNull = idText.getText() == null;
        boolean nameNull = nameText.getText() == null;
        boolean priceNull = priceText.getText() == null;
        boolean typeNull = typeText.getValue() == null;
        if (idNull && nameNull && priceNull && typeNull) {
            return true;
        } else return false;
    }
}

