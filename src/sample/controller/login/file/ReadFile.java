package sample.controller.login.file;

import sample.model.Product;
import sample.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    public static List<Product> productMenu() {
        List<Product> products = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream("drink.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                products.add((Product) ois.readObject());
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
        return products;
    }
    public static List<User> useReadName() {
        List<User> users = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream("user.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                users.add((User) ois.readObject());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.getMessage();
        } catch (EOFException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }
}
