package controllers;

import play.mvc.*;

import views.html.*;

import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.*;
import models.users.*;

public class HomeController extends Controller {

    private Environment env;

    @Inject
    public HomeController(Environment env){
        this.env = env;
    }

    public Result index() {
        // List<Product> products = Product.findAll(); //to generate the product list dynamically
        if(Product.getLowQty().size() > 0){
            String lowQtyStr = "Restock needed! Check product list!";
            flash("warning", lowQtyStr);
        }
        return ok(index.render(User.getUserById(session().get("email")), env));
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
