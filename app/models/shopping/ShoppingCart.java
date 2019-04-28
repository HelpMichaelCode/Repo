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
public class ShoppingCart extends Model {

    @Id
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    private List<OrderLine> cartItems;
    
    @OneToOne
    private User user;
    

    public ShoppingCart(){
    }

    public static Finder<Long, ShoppingCart> find = new Finder<Long, ShoppingCart>(ShoppingCart.class);

    public static List<ShoppingCart> findAll(){
        return ShoppingCart.find.all();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }



    public List<OrderLine> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<OrderLine> cartItems) {
        this.cartItems = cartItems;
    }

    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Add item for sale to basket
    // Either update existing order item or ad a new one.
    public boolean addProductToCart(Product product) {
        
        if(product.getProductQty() > 1){
            boolean productFound = false;
            // Check if product already in this basket
            // Find OrderLine with this product
            // if found increment quantity
            for (OrderLine oi : cartItems) {
                if (oi.getProduct().getProductID() == product.getProductID()) {
                    productFound = true;
                    if(!((oi.getQuantity()+1) > product.getProductQty())){
                        oi.increaseQty();
                    } else {
                        return false;
                    }
                    break;
                }
            }
            if (productFound == false) {
                // Add OrderLine to list
                OrderLine newItem = new OrderLine(product);
                // Add to items
                cartItems.add(newItem);
            }
            return true; //if product quantity is greater than 0
        }
        return false; //if product qty is 0 or less?
    }

    public double getCartTotal(){
        double total = 0;

        for(OrderLine i: cartItems){
            total += i.getLineTotal();
        }
        return total;
    }

    public void removeItem(OrderLine orderLine) {

        // Removal of list items is unreliable as index can change if an item is added or removed elsewhere
        // iterator works with an object reference which does not change
        for (Iterator<OrderLine> iter = cartItems.iterator(); iter.hasNext();) {
            OrderLine i = iter.next();
            if (i.getId().equals(orderLine.getId()))
            {
                // If more than one of these items in the basket then decrement
                if (i.getQuantity() > 1 ) {
                    i.decreaseQty();
                    // i.getProduct().incrementStock();
                    // i.getProduct().update();
                }
                // If only one left, remove this item from the basket (via the iterator)
                else {
                    // delete object from db
                    i.delete();
                    // i.getProduct().incrementStock();
                    // i.getProduct().update();
                    // remove object from list
                    iter.remove();
                    break;
                }             
            }
       }
    }

    public void deleteItem(OrderLine orderLine){
        if(orderLine != null){
             orderLine.delete();
        }
    }

    public void removeAllProducts() {
        for(OrderLine i: this.cartItems) {
            Product p = Product.getProductById(i.getProduct().getProductID());
            if(p.getProductID() == i.getProduct().getProductID()){
                int quantity = i.getQuantity();
                // p.restock(quantity);
                // p.update();
            } 
            i.delete();
        }
        this.cartItems = null;
    }
}