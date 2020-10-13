package sample.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private int price;
    private String typeDrink;

    public Product() {
    }

    public Product(int id, String name, int price, String typeDrink) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.typeDrink = typeDrink;
    }

    public String getTypeDrink() {
        return typeDrink;
    }

    public void setTypeDrink(String typeDrink) {
        this.typeDrink = typeDrink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", typeDrink='" + typeDrink + '\'' +
                '}';
    }
}
