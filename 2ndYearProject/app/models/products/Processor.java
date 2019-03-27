package models.products;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;

@Entity
public class Processor extends ProductSkeleton {

    // @Id
    // private Long productId;
    @Constraints.Required
    private String cores;
    @Constraints.Required
    private String clock;
    @Constraints.Required
    private String cache;

    public Processor(){
    }

    public Processor(Long productId, String manufacturer, String name,
     Product product, String cores, String clock, String cache){
        // this.productId = productId;
        super(productId, manufacturer, name, product);
        this.cores = cores;
        this.clock = clock;
        this.cache = cache;
    }

    public String getCores(){
        return cores;
    }
    public void setCores(String cores){
        this.cores = cores;
    }

    public String getClock(){
        return clock;
    }
    public void setClock(String clock){
        this.clock = clock;
    }

    public String getCache(){
        return cache;
    }
    public void setCache(String cache){
        this.cache = cache;
    }

    public static Finder<Long, Processor> find = new Finder<>(Processor.class);

    public static final List<Processor> findAll() {
        return Processor.find.all();
    }

    public static Processor getProcessorById(Long id){
        if(id <= 0){
            return null;
        } else {
            return find.query().where().eq("product_id", id).findUnique();
        }
    }
}