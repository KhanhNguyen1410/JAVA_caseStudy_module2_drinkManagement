package sample.controller.account;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface MyAdminControl {
    void add();
    void reset();
    void edit();
    void update();
    void delete(ActionEvent event);
    boolean checkId(int id);
    void logOut(ActionEvent event) throws IOException;
}
