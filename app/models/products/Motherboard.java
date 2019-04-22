package models.products;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;


@Entity
public class Motherboard extends ProductSkeleton {

    // @Id
    // private Long productId;

    @Constraints.Required
    private String ramSlots;
    
    @Constraints.Required
    private String maxRam;

    public Motherboard(){
    }

    public Motherboard(Long productId, String manufacturer, String name,
     Product product, String ramSlots, String maxRam){
        // this.productId = productId;
        super(productId, manufacturer, name, product);
        this.ramSlots = ramSlots;
        this.maxRam = maxRam;
    }

    public String getRamSlots(){
        return ramSlots;
    }
    public void setRamSlots(String ramSlots){
        this.ramSlots = ramSlots;
    }

    public String getMaxRam(){
        return maxRam;
    }
    public void setMaxRam(String maxRam){
        this.maxRam = maxRam;
    }

    public static Finder<Long, Motherboard> find = new Finder<>(Motherboard.class);

    public static final List<Motherboard> findAll() {
        return Motherboard.find.all();
    }

    public static Motherboard getMotherboardById(Long id){
        if(id <= 0){
            return null;
        } else {
            return find.query().where().eq("product_id", id).findUnique();
        }
    }

    public boolean checkLengthOfStrings(){
        if(checkStringLen(getManufacturer()) || checkStringLen(getName()) || checkStringLen(ramSlots) || checkStringLen(maxRam)){
            return true;
        }
        return false;
    }
}