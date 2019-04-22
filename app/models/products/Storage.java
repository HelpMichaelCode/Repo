package models.products;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;


@Entity
public class Storage extends ProductSkeleton {

    // @Id
    // private Long productId;
    @Constraints.Required
    private String capacity;

    public Storage(){
    }

    public Storage(Long productId, String manufacturer, String name,
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

    public static Finder<Long, Storage> find = new Finder<>(Storage.class);

    public static final List<Storage> findAll() {
        return Storage.find.all();
    }

    public static Storage getStorageById(Long id){
        if(id <= 0){
            return null;
        } else {
            return find.query().where().eq("product_id", id).findUnique();
        }
    }

    public boolean checkLengthOfStrings(){
        if(checkStringLen(getManufacturer()) || checkStringLen(getName()) || checkStringLen(capacity)){
            return true;
        }
        return false;
    }
}