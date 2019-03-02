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

import java.util.*;

public class ProductController extends Controller{

    private FormFactory formFactory;
    @Inject
    public ProductController(FormFactory formFactory){
        this.formFactory = formFactory;
    }

    // public static Result uploadImage() {
    //     File file = request().body().asRaw().asFile();
    //     return ok("File uploaded");
    // }

    public Result addProduct() {
        Form<Product> productForm = formFactory.form(Product.class);
        
        return ok(addProduct.render(productForm, User.getUserById(session().get("email"))));
    }

    public Result addProductSubmit() {
        Form<Product> productForm = formFactory.form(Product.class).bindFromRequest();

        if(productForm.hasErrors()){

            return badRequest(addProduct.render(productForm, (User.getUserById(session().get("email")))));
        } else {
            Product newProduct = productForm.get();

            if(newProduct == null) {
                flash("error", "Product not added! Please, try again.");
                return badRequest(addProduct.render(productForm, (User.getUserById(session().get("email")))));
            }

            if (Product.getProductById(newProduct.getProductID()) == null) {
                newProduct.save();
                flash("success","Item " + newProduct.getProductName() + " was added.");
                return redirect(controllers.routes.ProductController.productList(0));
            } else {
                   newProduct.update();
                flash("success", "Item " + newProduct.getProductName() + " was updated.");
                return redirect(controllers.routes.ProductController.productList(0));
            }
    }
    }

    //returns all the products
    public Result productList(Long cat) {
        List<Product> itemList = null;
        List<Category> categoryList = Category.findAll();

        if(cat ==0){
            itemList = Product.findAll();
        }else {
            itemList = Category.find.ref(cat).getItems();

        }
        return ok(productList.render(itemList, categoryList,(User.getUserById(session().get("email")))));
    }
      

    public Result deleteItem(Long ProductID){
        Product.find.ref(ProductID).delete();
        flash("Success", "Product has been deleted");
        return redirect(controllers.routes.ProductController.productList(0));
    }

    public Result updateItem(Long id) {
    Product i;
    Form<Product> productForm;

    try {
        // Find the item by id
        i = Product.find.byId(id);

        // Populate the form object with data from the item found in the database
        productForm = formFactory.form(Product.class).fill(i);
    } catch (Exception ex) {
        return badRequest("error");
    }

    // Display the "add item" page, to allow the user to update the item
    return ok(addProduct.render(productForm,(User.getUserById(session().get("email")))));
}

}

