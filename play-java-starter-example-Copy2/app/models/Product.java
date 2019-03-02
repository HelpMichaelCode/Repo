package models;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Product extends Model {
    
    @Id
    // @Constraints.Required
    private Long productID;

    @Constraints.Required
    private String productName;

    @Constraints.Required
    private String productDescription;

    @Constraints.Required
    private double productPrice;

    @Constraints.Required
    private int productQty;

    // @Constraints.Required
    private int totalSold;

    // @Constraints.Required
    private double overallRating;

    private static final int QTY_LOW = 10; //new
    //constant for the restock notification 
    // (i.e., what's the least amount of something that you can have before sending the notification)

    @ManyToMany(cascade=CascadeType.ALL, mappedBy = "products")
    private List<Category> categories;

    public Product() {
    }

    public Product(Long productID, String productName, String productDescription,
                    double productPrice, int productQty) {
        this.productID = productID;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.totalSold = 0;
        this.overallRating = 0;
    }

    //Accessors and mutators / getters and setters
    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public int getTotalSold() {
        return totalSold;
    }
    
    public double getOverallRating() {
        return overallRating;
    }

    public List<Category> getCategories() {
        return categories;
    }
    
    public void setCategories (List<Category> categories) {
        this.categories = categories;
    }

    //end of getters and setters

    //Finder and finder methods
    public static Finder<Long, Product> find = new Finder<>(Product.class);

    public static final List<Product> findAll() {
        return Product.find.all();
    }

    public static Product getProductById(Long id){
        if(id <= 0){
            return null;
        } else {
            return find.query().where().eq("email", id).findUnique();
        }
    }
    //End of finder methods


    public void purchase(int quantity) {
        if (productQty<quantity) {
            //Display error message
            // flash("error", "Oops! We don't have the selected quantity of this product in stock. We'll restock soon.");
        } else {
            productQty -= quantity;
            totalSold += quantity;
            checkQty();
            //Display message to inform the user that action is completed successful
            // flash("success", "Update was successful!");
        }
    }

    public void restock(int quantity){
        if(quantity>100){
            //Displaye error message
            // flash("error", "You cannot add more than 100 items of one type!");
        } else {
            productQty += quantity;
            //Display message to inform the user that action is completed successful
            // flash("success", "Update was successful!");
        }
    }

    public void checkQty(){
        if(productQty<=QTY_LOW){
            //Display error
            // flash("error", "This product's quantity is low! Restock as soon as possible!"); //what am i doing?
        }
    }

}