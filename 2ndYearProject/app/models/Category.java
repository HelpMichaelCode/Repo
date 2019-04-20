package models;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Category extends Model {

   // Fields
   // Annotate id as primary key
   @Id
   private Long id;

   @Constraints.Required
   private String name;

   // Category contains many products
   @OneToMany
   private List<Product> items;

   // Default constructor
   public  Category() {
   }
			    
   public  Category(Long id, String name, List<Product> items) {
      this.id = id;
      this.name = name;
      this.items = items;
   }
   public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public List<Product> getItems() {
    return items;
}

public void setItems (List<Product> items) {
    this.items = items;
}
   //Generic query helper for entity Computer with id Long
public static Finder<Long,Category> find = new Finder<Long,Category>(Category.class);

//Find all Products in the database
public static List<Category> findAll() {
    List<Category> categoryList = Category.find.query().where().orderBy("name asc").findList();
    categoryList.remove(Category.getCategoryById(Long.valueOf(1000)));
    return categoryList;
}

public static Category getCategoryById(Long id){
    if(id <= 0){
        return null;
    } else {
        return find.query().where().eq("id", id).findUnique();
    }
}

public static Map<String,String> options() {
    LinkedHashMap<String,String> options = new LinkedHashMap<>();
    // Get all the categories from the database and add them to the options hash map
    for (Category c: Category.findAll()) {
       options.put(c.getId().toString(), c.getName());
    }
    return options;
 }
}