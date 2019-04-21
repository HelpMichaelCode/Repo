package models.products;

import java.util.*;
import java.util.Comparator;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;

// @Entity
@MappedSuperclass
public abstract class ProductSkeleton extends Model {
    
    @Id
    // @Constraints.Required
    private Long productId;

    @Constraints.Required
    private String manufacturer;

    @Constraints.Required
    private String name;

    @ManyToOne(cascade=CascadeType.ALL)
    private Product product;

    public ProductSkeleton(){
    }

    public ProductSkeleton(Long productId, String manufacturer, String name, Product product){
        this.productId = productId;
        this.manufacturer = manufacturer;
        this.name = name;
        this.product = product;
    }

    public Long getProductId(){
        return productId;
    }
    public void setProductId(Long id){
        this.productId = id;
    }

    public String getManufacturer(){
        return manufacturer;
    }
    public void setManufacturer(String manufacturer){
        this.manufacturer = manufacturer;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Product getProduct(){
        return product;
    }
    public void setProduct(Product product){
        this.product = product;
    }

    public static Comparator<ProductSkeleton> TotalSoldComparator = new Comparator<ProductSkeleton>() {

	public int compare(ProductSkeleton p1, ProductSkeleton p2) {

	   int first = p1.getProduct().getTotalSold();
	   int second = p2.getProduct().getTotalSold();

	   /*For ascending order*/
	//    return rollno1-rollno2;

	   /*For descending order*/
	   return second-first;
   }};
}