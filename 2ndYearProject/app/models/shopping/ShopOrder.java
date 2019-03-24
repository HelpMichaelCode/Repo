package models.shopping;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import java.util.Date;
import models.*;
import models.users.*;
import java.text.SimpleDateFormat;

// ShopOrder entity managed by Ebean
@Entity
public class ShopOrder extends Model {

    @Id
    private Long id;    
    private Calendar orderDate;
    
    
	@OneToMany(mappedBy="order", cascade = CascadeType.ALL)
    private List<OrderLine> products;
    
    @ManyToOne
    private User user;


    // Default constructor
    public  ShopOrder() {
        orderDate = Calendar.getInstance();
    }
    	
    public static Finder<Long,ShopOrder> find = new Finder<Long,ShopOrder>(ShopOrder.class);

    public static List<ShopOrder> findAll() {
        return ShopOrder.find.all();
    }
    //Find all Products in the database
    public List<OrderLine> getProducts() {
        return products;
    }

    public void setProducts(List<OrderLine> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getOrderDate() {
        return orderDate;
    }
    //date
    public String getOrderDateString() {
        if(orderDate == null) {
            return "No Date Availible";
        }
        String s = new SimpleDateFormat("dd-MMM-yyyy").format(orderDate.getTime());
        return s;
    }

    public void setOrderDate(Calendar orderDate) {
        orderDate = orderDate;
    }

    public double getOrderTotal() {
        
        double total = 0;
        
        for (OrderLine i: products) {
            total += i.getLineTotal();
        }
        return total;
    }


    public void adjustStock(){
     
        for (OrderLine i : products) {
           Product p = Product.find.byId(i.getProduct().getProductID());
            if (i.getProduct().getProductID() == p.getProductID()) {
                int quantity = i.getQuantity();
                p.restock(quantity);
                p.update();
            }
        }
    }

}