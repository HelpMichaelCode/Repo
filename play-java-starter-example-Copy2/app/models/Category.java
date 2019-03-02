package models;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

// Category entity managed by Ebean
@Entity
public class Category extends Model {

   // Fields
   // Annotate id as primary key
   @Id
   private Long categoryID;

   @Constraints.Required
   private String categoryName;

   // Category contains many products
   @OneToMany
   private List<Product> products;

   // Default constructor
   public  Category() {
   }
			    
   public  Category(Long id, String name, List<Product> products) {
      this.categoryID = id;
      this.categoryName = name;
      this.products = products;
   }
   public Long getCategoryID() {
    return categoryID;
    }

public void setCategoryID(Long id) {
    this.categoryID = id;
}

public String getCategoryName() {
    return categoryName;
}

public void setCategoryName(String name) {
    this.categoryName = name;
}

public List<Product> getProducts() {
    return products;
}

public void setProducts (List<Product> products) {
    this.products = products;
}
   //Generic query helper for entity Computer with id Long
public static Finder<Long,Category> find = new Finder<Long,Category>(Category.class);

//Find all Products in the database
public static List<Category> findAll() {
   return Category.find.query().where().orderBy("name asc").findList();
}

// public static LinkedHashMap<Long,String> options() {
//     LinkedHashMap<Long,String> options = new LinkedHashMap<>();
 
//     // Get all the categories from the database and add them to the options hash map
//     for (Category c: Category.findAll()) {
//        options.put(c.getCategoryID(), c.getCategoryName());
//     }
//     return options;
//  }
}