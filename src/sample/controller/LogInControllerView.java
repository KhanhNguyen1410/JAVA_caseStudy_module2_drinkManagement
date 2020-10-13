package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.controller.login.MyLoginController;
import sample.controller.login.file.ReadFile;
import sample.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogInControllerView implements MyLoginController {
    @FXML
    private TextField userText;

    @FXML
    private PasswordField passText;

    private static List<User> userList;

    static {
        userList = new ArrayList<>();
        userList.add(new User("admin", "admin"));
        userList.add(new User("khanh", "1234"));
        for (User user : ReadFile.useReadName()) {
            userList.add(user);
        }
    }

    @Override
    public void signIn(ActionEvent event) throws IOException {
        if (checkAdmin() && checkPass()) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/View.fxml"));
            Parent viewParent = loader.load();
            Scene scene = new Scene(viewParent);
            stage.setScene(scene);
            stage.show();
        } else if (checkUserName() && checkPass()) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/UserView.fxml"));
            Parent viewParent = loader.load();
            Scene scene = new Scene(viewParent);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("pass or name sai");
            alert.show();
        }
    }

    @Override
    public void signUp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String name = userText.getText();
        String pass = passText.getText();
        if (name != null && pass != null) {
            User newUser = new User(name, pass);
            userList.add(newUser);
            try {
                FileOutputStream fos = new FileOutputStream("user.dat");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                if (!checkUserName()) {
                    for (User user : userList) {
                        oos.writeObject(user);
                    }
                    oos.close();
                    alert.setContentText("sign up ok, bam sign in de vao");
                } else {
                    alert.setContentText("user name is existed");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reset();
            }
        } else {
            alert.setContentText("enter name and pass");
        }
        alert.show();
    }

    @Override
    public void reset() {
        userText.clear();
        passText.clear();
    }

    @Override
    public boolean checkUserName() {
        String name = userText.getText();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkAdmin() {
        String admin = userText.getText();

        if (admin.equals("admin")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPass() {
        String pass = passText.getText();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPassWord().equals(pass)) {
                return true;
            }
        }
        return false;
    }
}


