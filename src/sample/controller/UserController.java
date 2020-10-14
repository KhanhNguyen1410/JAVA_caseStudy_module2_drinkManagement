package sample.controller;

import javafx.collections.FXCollections;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.model.BagUser;
import sample.model.Product;

import java.io.*;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    private TableView<Product> tableView;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> proColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private TableColumn<Product, String> typeColumn;

    @FXML
    private TextField searchText;

    @FXML
    private Label totalText;

    @FXML
    private Button addButton;

    @FXML
    private Button subButton;

    @FXML
    private TableView<BagUser> bagView;

    @FXML
    private TableColumn<BagUser,String> nameColumn;
    @FXML
    private TableColumn<BagUser,String> priceUserColumn;

    @FXML
    private TableColumn<BagUser,Integer> quantity;

    private int total = 0;

    private ObservableList<Product> productList;
    private List<String> tempProducts = new ArrayList<>();
    private ObservableList<BagUser> bagUsers;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productList = FXCollections.observableArrayList(
                readProduct()
        );
        bagUsers = FXCollections.observableArrayList();

        priceUserColumn.setCellValueFactory(new PropertyValueFactory<BagUser,String>("price"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<BagUser,String>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<BagUser,Integer>("quantity"));

        idColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        proColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("typeDrink"));
        tableView.setItems(productList);
        bagView.setItems(bagUsers);

        this.search();
    }

    public List<Product> readProduct(){
        List<Product> products2= new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream("drink.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                products2.add((Product) ois.readObject());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return products2;
    }

    @FXML
    void add(ActionEvent event) {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        try{
            BagUser bagUser = new BagUser();
            bagUser.setName(selectedProduct.getName());
            bagUser.setPrice(selectedProduct.getPrice());
            bagUser.setQuantity(1);
            if (checkTempProduct(selectedProduct.getName())){
                for (int i = 0; i < bagUsers.size(); i++) {
                    if (bagUsers.get(i).getName().equals(selectedProduct.getName())){
                        bagUsers.get(i).setQuantity(bagUsers.get(i).getQuantity()+1);
                        bagUsers.set(i, bagUsers.get(i));
                    }
                }
            }
            else {
                bagUsers.add(bagUser);
            }
            NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
            NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));
            Number price = nf.parse(selectedProduct.getPrice());

            total +=(long) price;
            String result = nf2.format(total);
            totalText.setText(String.valueOf(result));
            tempProducts.add(selectedProduct.getName());

        } catch (NullPointerException | ParseException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("plz choose a product");
            alert.show();
        }
        if (total >= 0){
            subButton.setDisable(false);
        }
    }

    @FXML
    void sub(ActionEvent event) {
        Product selected = tableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (total >= 0) {
            try {
                if (checkTempProduct(selected.getName())) {
                    NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
                    Number price = nf.parse(selected.getPrice());
                    total -=(long) price;
                    for (int i = 0; i < bagUsers.size()  ; i++) {
                        if (bagUsers.get(i).getName().equals(selected.getName()) && bagUsers.get(i).getQuantity() > 0){
                            bagUsers.get(i).setQuantity(bagUsers.get(i).getQuantity()-1);
                            bagUsers.set(i, bagUsers.get(i));
                        }
                    }
                    totalText.setText(String.valueOf(total));
                } else {
                    alert.setContentText("plz add product to cart");
                    alert.show();
                }
            } catch (NullPointerException e) {
                alert.setContentText("plz choose a product");
                alert.show();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } else {

            total = 0;
            totalText.setText(String.valueOf(total));
            subButton.setDisable(true);
            tempProducts.remove(selected);
        }
    }
    public boolean checkTempProduct(String name){
        for (int i = 0; i < tempProducts.size(); i++) {
            if (tempProducts.get(i).equals(name)){
                return true;
            }
        }
        return false;
    }

    @FXML
    void buy(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("total: " + totalText.getText() + "VND");
        alert.show();
        tempProducts.remove(tempProducts);
        bagView.refresh();
        totalText.setText("0");
    }



    public void logOut(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/LoginView.fxml"));
        Parent viewParent = loader.load();
        Scene scene = new Scene(viewParent);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
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

}
