package models.shopping;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import play.mvc.*;
import play.data.*;
import javax.inject.Inject;

import views.html.*;
import play.db.ebean.Transactional;
import play.api.Environment;

import models.*;
import models.users.*;

@Entity
public class Basket extends Model {

    @Id
    private Long id;

    public Basket(){
    }


    @OneToMany(mappedBy = "basket", cascade = CascadeType.PERSIST)
    private List<OrderItem> basketItems;
    
    @OneToOne
    private User user;
    
    public static Finder<Long, Basket> find = new Finder<Long, Basket>(Basket.class);

    public static List<Basket> findAll(){
        return Basket.find.all();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }



    public List<OrderItem> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<OrderItem> basketItems) {
        this.basketItems = basketItems;
    }

    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Add item for sale to basket
    // Either update existing order item or ad a new one.
    public void addProductOnSale(Product product) {
        
        boolean productFound = false;
        // Check if product already in this basket
        // Check if item in basket
        // Find orderitem with this product
        // if found increment quantity
        for (OrderItem oi : basketItems) {
            if (oi.getProduct().getProductID() == product.getProductID()) {
                oi.increaseQty();
                productFound = true;
                break;
            }
        }
        if (productFound == false) {
            // Add orderItem to list
            OrderItem newItem = new OrderItem(product);
            // Add to items
            basketItems.add(newItem);
        }
    }

    public double getBasketTotal(){
        double total = 0;

        for(OrderItem i: basketItems){
            total += i.getItemTotal();
        }
        return total;
    }

   

    public void removeItem(OrderItem product) {

        // Using an iterator ensures 'safe' removal of list objects
        // Removal of list items is unreliable as index can change if an item is added or removed elsewhere
        // iterator works with an object reference which does not change
        for (Iterator<OrderItem> iter = basketItems.iterator(); iter.hasNext();) {
            OrderItem i = iter.next();
            if (i.getId().equals(product.getId()))
            {
                // If more than one of these items in the basket then decrement
                if (i.getQuantity() > 1 ) {
                    i.decreaseQty();
                }
                // If only one left, remove this item from the basket (via the iterator)
                else {
                    // delete object from db
                    i.delete();
                    // remove object from list
                    iter.remove();
                    break;
                }             
            }
       }
    }

    public void removeAllProducts(){
        for(OrderItem i: this.basketItems){
            i.delete();
        }
        this.basketItems = null;
    }
}