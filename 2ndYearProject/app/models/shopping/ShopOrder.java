package models.shopping;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import java.util.Date;
import models.*;
import models.users.*;

// ShopOrder entity managed by Ebean
@Entity
public class ShopOrder extends Model {

    @Id
    private Long id;    

    private Date orderDate;
    
    
	@OneToMany(mappedBy="order", cascade = CascadeType.ALL)
    private List<OrderLine> products;
    
    @ManyToOne
    private User user;


    // Default constructor
    public  ShopOrder() {
        orderDate = new Date();
    }
    	
    public static Finder<Long,ShopOrder> find = new Finder<Long,ShopOrder>(ShopOrder.class);

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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        orderDate = orderDate;
    }

    public double getOrderTotal() {
        
        double total = 0;
        
        for (OrderLine i: products) {
            total += i.getLineTotal();
        }
        return total;
    }
}