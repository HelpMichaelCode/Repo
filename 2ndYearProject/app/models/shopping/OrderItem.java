package models.shopping;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;
import models.users.*;

@Entity
public class OrderItem extends Model {

    @Id
    private Long id;
    private int quantity;
    private double price;

    @ManyToOne
    private ShopOrder order;
    
    @ManyToOne
    private  Basket basket;
    
    // Unidirection mapping - Many order items can have one product
    // Product not interested in this
    @ManyToOne
    private Product products;


    public OrderItem(){
    }

    public OrderItem(Product p){
        products = p;
        quantity = 1;
        price = p.getProductPrice();
    }

    public void increaseQty(){
        quantity++;
    }

    public void decreaseQty(){
        quantity--;
    }

    public double getItemTotal(){
        return this.price * this.quantity;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public static Finder<Long, OrderItem> find = new Finder<Long, OrderItem>(OrderItem.class);

    public static List<OrderItem> findAll(){
        return OrderItem.find.all();
    }


    public ShopOrder getOrder() {
        return order;
    }

    public void setOrder(ShopOrder order) {
        this.order = order;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Product getProduct() {
        return products;
    }

    public void setProduct(Product products) {
        this.products = products;
    }
}