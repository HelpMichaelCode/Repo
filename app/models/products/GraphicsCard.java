package models.products;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;

@Entity
public class GraphicsCard extends ProductSkeleton {

    // @Id
    // private Long productId;

    @Constraints.Required
    private String bus;
    @Constraints.Required
    private String memory;
    @Constraints.Required
    private String gpuClock;
    @Constraints.Required
    private String memoryClock;

    public GraphicsCard(){
    }

    public GraphicsCard(Long productId, String manufacturer, String name,
     Product product, String bus, String memory,
     String gpuClock, String memoryClock){
        // this.productId = productId;
        super(productId, manufacturer, name, product);
        this.bus = bus;
        this.memory = memory;
        this.gpuClock = gpuClock;
        this.memoryClock = memoryClock;
    }

    public String getBus(){
        return bus;
    }
    public void setBus(String bus){
        this.bus = bus;
    }

    public String getGpu(){
        return memory;
    }

    public String getMemory(){
        return memory;
    }
    public void setMemory(String memory){
        this.memory = memory;
    }

    public String getGpuClock(){
        return gpuClock;
    }
    public void setGpuClock(String gpuClock){
        this.gpuClock = gpuClock;
    }

    public String getMemoryClock(){
        return memoryClock;
    }
    public void setMemoryClock(String memoryClock){
        this.memoryClock = memoryClock;
    }

    public static Finder<Long, GraphicsCard> find = new Finder<>(GraphicsCard.class);

    public static final List<GraphicsCard> findAll() {
        return GraphicsCard.find.all();
    }

    public static GraphicsCard getGraphicsCardById(Long id){
        if(id <= 0){
            return null;
        } else {
            return find.query().where().eq("product_id", id).findUnique();
        }
    }

}