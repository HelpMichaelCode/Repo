package controllers;

import play.mvc.*;

import views.html.*;

import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.*;
import models.users.*;
import models.products.*;

public class HomeController extends Controller {

    private Environment env;

    @Inject
    public HomeController(Environment env){
        this.env = env;
    }

    public Result index() {
        ProductController.flashLowStock();

        List<ProductSkeleton> bestSellers = new ArrayList<>();
        List<ProductSkeleton> temp = ProductController.getSpecs(Long.valueOf(0), "");
        List<Product> all = Product.findAll();
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).getProduct().getProductName().contains("N/A")){ // this loop removes the 'Not Available' product object
                                            // that we have created to avoid having null pointers in trending pc
                temp.remove(i);
            }
        }
        Collections.sort(temp, ProductSkeleton.TotalSoldComparator);

        for(int i=0; i<6; i++){
            bestSellers.add(temp.get(i));
        }
        return ok(index.render(bestSellers, User.getUserById(session().get("email")), env));
    }

    public Result stats(){
        List<String> names = new ArrayList<>();
        List<Integer> sales = new ArrayList<>();
        
        for(Category c: Category.findAll()){
            names.add(c.getName());
            int sum = 0;
            for(Product p: Product.findAll()){
                if(p.getCategory().getName().equals(c.getName())){
                    sum += p.getTotalSold();
                }
            }
            sales.add(sum);
        }

        return ok(stats.render(names, sales, User.getUserById(session().get("email"))));
    }
}
