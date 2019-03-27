package models.products;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class TrendingPC extends Model {

    @Id
    @OneToOne
    private Long productId;
    @Constraints.Required
    private String shortName;
    @Constraints.Required
    private Processor cpu;
    @Constraints.Required
    private GraphicsCard gpu;
    @Constraints.Required
    private Motherboard motherboard;
    @Constraints.Required
    private Storage hddSize;

    public TrendingPC(){
    }

    public TrendingPC(Long productId, String shortName, String shortDescription,
     Processor cpu, GraphicsCard gpu, Motherboard motherboard, Storage hddSize){
        this.productId = productId;
        this.shortName = shortName;
        this.cpu = cpu;
        this.gpu = gpu;
        this.motherboard = motherboard;
        this.hddSize = hddSize;
    }

    public Long getProductId(){
        return productId;
    }
    public void setProductId(Long productId){
        this.productId = productId;
    }

    public String getShortName(){
        return shortName;
    }
    public void setShortName(String shortName){
        this.shortName = shortName;
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

    public Storage getHddSize(){
        return hddSize;
    }
    public void setHddSize(Storage hddSize){
        this.hddSize = hddSize;
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