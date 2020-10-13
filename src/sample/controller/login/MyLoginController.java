package sample.controller.login;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface MyLoginController {
    void signIn(ActionEvent event) throws IOException;
    void signUp();
    void reset();
    boolean checkUserName();
    boolean checkAdmin();
    boolean checkPass();

}
