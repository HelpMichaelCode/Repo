package controllers;

//imports for image upload
import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.File;
//imports for image scaling
import java.io.IOException;
import java.awt.image.*;
import javax.imageio.*;

import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.*;
import models.users.*;
import views.html.*;



public class ProductController extends Controller{

    private FormFactory formFactory;
    private Environment env;

    @Inject
    public ProductController(FormFactory formFactory, Environment env){
        this.formFactory = formFactory;
        this.env = env;
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addProduct() {
        Form<Product> productForm = formFactory.form(Product.class);
        
        return ok(addProduct.render(productForm, User.getUserById(session().get("email")), "Add product to BLDPC"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addProductSubmit(){
        Form<Product> newProductForm = formFactory.form(Product.class).bindFromRequest();
        MultipartFormData<File> data = request().body().asMultipartFormData();
        FilePart<File> image = data.getFile("upload");

        if(newProductForm.hasErrors()){
            flash("error", "Fill in all the fields.");
            return badRequest(addProduct.render(newProductForm, User.getUserById(session().get("email")), "Add product to BLDPC"));
        } else {
            Product newProduct = newProductForm.get();
            if (newProduct.getProductID() == null) {
                if(newProduct.getCategory().getId() == null){
                    flash("error", "Please select a category first.");
                    return badRequest(addProduct.render(newProductForm, User.getUserById(session().get("email")), "Add product to BLDPC"));
                } else {
                    newProduct.save();
                    flash("success", "Item " + newProduct.getProductName() + " has been added successfuly!");
                }
            } else {
                newProduct.update();
                flash("success", "Item " + newProduct.getProductName() + " has been updated successfuly!");
            }
            String saveImageMessage = saveFile(newProduct.getProductID(), image);
            return redirect(controllers.routes.ProductController.productList(0, ""));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateItem(Long id) {
        Product p;
        Form<Product> productForm;

        try {
            // Find the item by id
            p = Product.find.byId(id);

            // Populate the form object with data from the item found in the database
            productForm = formFactory.form(Product.class).fill(p);
        } catch (Exception ex) {
            return badRequest(productList.render(Product.findAll(), Category.findAll(), User.getUserById(session().get("email")), env, ""));
            //redirect to add/update item with user's session
        }

        // Display the "add product" page, to allow the user to update the item
        return ok(addProduct.render(productForm,(User.getUserById(session().get("email"))), "Update product "+p.getProductName()));
    }

    // this method gets all the products from the database and passes them into the productList view, which displays them

    public Result productList(Long cat, String keyword) {
        List<Product> itemList = null;
        List<Category> categoryList = Category.findAll();
        if(keyword == null){
            keyword = "";
        }

        if(cat == 0){
            itemList = Product.findAll();
        }else {
            itemList = Category.find.ref(cat).getItems();

        }
        return ok(productList.render(itemList, categoryList,User.getUserById(session().get("email")), env, keyword));
    }
      
    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    @Transactional
    public Result deleteItem(Long productID){
        Product.find.ref(productID).delete();
        flash("Success", "Product has been deleted");
        return redirect(controllers.routes.ProductController.productList(0, ""));
    }

    public String saveFile(Long productID, FilePart<File> uploaded){
        if(uploaded != null){ //making sure the file doesn't exist
            String mimeType = uploaded.getContentType(); //making sure its an image
            if(mimeType.startsWith("image/")){
                String fileName = uploaded.getFilename();
                //exctracting the extension of the image
                // String extension = "";
                // we'll go with jpgs only ;)
                // int index = fileName.lastIndexOf('.');
                // if(index >= 0){
                //     extension = fileName.substring(index + 1);
                // }

                File file = uploaded.getFile();
                //checking if the directories in the specified path exist, if they do not they are created
                File directory = new File("public/images/productImages");
                if(!directory.exists()){
                    directory.mkdirs();
                }
                //saving the image
                File newFile = new File("public/images/productImages/", productID + "."
                //  + extension);
                + "jpg");
                if(file.renameTo(newFile)){
                   
                    try{
                        BufferedImage image = ImageIO.read(newFile);
                        
                        return "/ file uploaded";
                        
                    } catch (IOException e) {
                        return "/ file uploaded.";
                    }

                } else {
                    return "/ file upload failed.";
                }
            }
        }
       	return "/ no image file.";
    }


    /********************************* THIS IS NOW DONE IN JAVASCRIPT *********************************/
    // public Result search(String keyword) {
    //     List<Product> itemList = null;
    //     List<Category> categoryList = Category.findAll();
    //     // List<String> allItems = new List<>();
    //     List<Product> filteredItems = null;

    //         itemList = Product.findAll();

    //     for(Product e: itemList){
    //         if(e.getProductName().contains(keyword) || e.getProductDescription().contains(keyword)){
    //             filteredItems.add(e);
    //         }
    //     }
    //     return ok(productList.render(filteredItems, categoryList, User.getUserById(session().get("email")), env, keyword));
    // }

    public Result displayProduct(String productName){
        Form<Review> reviewForm = formFactory.form(Review.class);
        Form<Product> prodForm = formFactory.form(Product.class);
        List<Review> filtered = new ArrayList<>();
        

        for(Product e: Product.findAll()){
            if(e.getProductName().equals(productName)){
                return ok(product.render(e, Review.findAll(), filtered, reviewForm, prodForm, User.getUserById(session().get("email")), env));       
            }
        }
        
        return redirect(controllers.routes.ProductController.productList(0, ""));    
    }

    // @Transactional
    @Security.Authenticated(Secured.class)
    public Result addReviewSubmit(){
        Form<Review> reviewForm = formFactory.form(Review.class).bindFromRequest();
        Form<Product> productForm = formFactory.form(Product.class).bindFromRequest();
        List<Review> filtered = new ArrayList<>();

        if(reviewForm.hasErrors()){
            flash("error", "Please fill in all the fields!");
            return badRequest(product.render(reviewForm.get().getProduct(), Review.findAll(), filtered, reviewForm, productForm, User.getUserById(session().get("email")), env));
        } else {
            Review newReview = reviewForm.get();
            Product temp = productForm.get();
            Product productObj = Product.getProductById(temp.getProductID());
            newReview.setUser(User.getUserById(session().get("email")));
            newReview.setProduct(productObj);
            productObj.calculateRating(newReview.getRating());
            
            if(newReview.getReviewbyId(newReview.getId()) == null){
                newReview.save();
                productObj.update();
                flash("success", "Thank you for you feedback!");

                    // Calculate the overall rating of the product
                return redirect(controllers.routes.ProductController.displayProduct(newReview.getProduct().getProductName()));
            } else {
                flash("error", "We ran into a problem processing your review, please try again later.");
                return badRequest(product.render(newReview.getProduct(), Review.findAll(), filtered, reviewForm, productForm, User.getUserById(session().get("email")), env));
            }
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    @Transactional
    public Result deleteReview(Long id){
        try{
            Review.find.ref(id).delete();
            flash("success", "Review has been deleted.");
        } catch (Exception ex) {
            flash("error", "Could not delete review.");
        }
        
        return redirect(controllers.routes.ProductController.userReviews(Review.find.ref(id).getUser().getEmail()));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result userReviews(String email){
        List<Review> reviews = Review.findAll();
        return ok(userReviews.render(reviews, email, User.getUserById(session().get("email"))));
    }

}




