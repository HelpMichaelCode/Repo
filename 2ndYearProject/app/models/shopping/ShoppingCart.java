package models.shopping;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;
import models.users.*;

@Entity
public class ShoppingCart extends Model {

    @Id
    private Long id;

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
}