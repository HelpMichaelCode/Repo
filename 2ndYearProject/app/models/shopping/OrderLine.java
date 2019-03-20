package models.shopping;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;
import models.users.*;

@Entity
public class OrderLine extends Model {

    @Id
    private Long id;
    private int quantity;
    private double price;

    @ManyToOne
    private ShopOrder order;
    
    @ManyToOne
    private  ShoppingCart cart;
    
    // Unidirection mapping - Many order items can have one product
    // Product not interested in this
    @ManyToOne
    private Product products;


    public OrderLine(){
    }

    public OrderLine(Product p){
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

    public double getLineTotal(){
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

    public static Finder<Long, OrderLine> find = new Finder<Long, OrderLine>(OrderLine.class);

    public static List<OrderLine> findAll(){
        return OrderLine.find.all();
    }


    public ShopOrder getOrder() {
        return order;
    }

    public void setOrder(ShopOrder order) {
        this.order = order;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return products;
    }

    public void setProduct(Product products) {
        this.products = products;
    }
}