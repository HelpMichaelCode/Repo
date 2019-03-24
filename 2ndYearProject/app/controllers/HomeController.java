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

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        List<Product> products = Product.findAll(); //to generate the product list dynamically
        if(Product.getLowQty().size() > 0){
            String lowQtyStr = "Restock needed! Check product list!";
            flash("warning", lowQtyStr);
        }
        return ok(index.render(products, User.getUserById(session().get("email")), env));
    }

    // public Result userList(){
    //     List<User> userList = null;
    //     userList = User.findAll();

    //     return ok(userList.render(User.getUserById(session().get("email"))));
    // }
    // public Result updateUser(){

    // }
    
}
