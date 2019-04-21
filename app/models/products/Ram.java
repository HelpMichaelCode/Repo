package models.products;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;


@Entity
public class Ram extends ProductSkeleton {

    // @Id
    // private Long productId;
    @Constraints.Required
    private String capacity;

    public Ram(){
    }

    public Ram(Long productId, String manufacturer, String name,
     Product product, String capacity){
        // this.productId = productId;
        super(productId, manufacturer, name, product);
        this.capacity = capacity;
    }

    public String getCapacity(){
        return capacity;
    }
    public void setCapacity(String capacity){
        this.capacity = capacity;
    }

    public static Finder<Long, Ram> find = new Finder<>(Ram.class);

    public static final List<Ram> findAll() {
        return Ram.find.all();
    }

    public static Ram getRamById(Long id){
        if(id <= 0){
            return null;
        } else {
            return find.query().where().eq("product_id", id).findUnique();
        }
    }
}