package models.products;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;
@Entity
public class TrendingPC extends ProductSkeleton {

    // @Id
    // @OneToOne
    // private Long productId;
    // @Constraints.Required
    // private String shortName;
    
    @ManyToOne
    @Constraints.Required
    private Processor cpu;

    @ManyToOne
    @Constraints.Required
    private GraphicsCard gpu;

    @ManyToOne
    @Constraints.Required
    private Motherboard motherboard;

    @Constraints.Required
    private int ramQty;

    @ManyToOne
    @Constraints.Required
    private Ram ram;
    
    @ManyToOne
    @Constraints.Required
    private Storage storage;

    public TrendingPC(){
    }

    public TrendingPC(Long productId, String name, String manufacturer,
     Processor cpu, GraphicsCard gpu, Motherboard motherboard, Storage storage, Product product){
        super(productId, manufacturer, name, product);
        this.cpu = cpu;
        this.gpu = gpu;
        this.motherboard = motherboard;
        this.storage = storage;
    }

    public Processor getCpu(){
        return cpu;
    }
    public void setCpu(Processor cpu){
        this.cpu = cpu;
    }

    public GraphicsCard getGpu(){
        return gpu;
    }
    public void setGpu(GraphicsCard gpu){
        this.gpu = gpu;
    }

    public Motherboard getMotherboard(){
        return motherboard;
    }
    public void setMotherboard(Motherboard motherboard){
        this.motherboard = motherboard;
    }

    public Storage getStorage(){
        return storage;
    }
    public void setStorage(Storage storage){
        this.storage = storage;
    }

    public int getRamQty(){
        return ramQty;
    }

    public void setRamQty(int ramQty){
        this.ramQty = ramQty;
    }

    public Ram getRam(){
        return ram;
    }

    public void setRam(Ram ram){
        this.ram = ram;
    }

    public static Finder<Long, TrendingPC> find = new Finder<>(TrendingPC.class);

    public static final List<TrendingPC> findAll() {
        return TrendingPC.find.all();
    }

    public static TrendingPC getTrendingPCById(Long id){
        if(id <= 0){
            return null;
        } else {
            return find.query().where().eq("product_id", id).findUnique();
        }
    }
}