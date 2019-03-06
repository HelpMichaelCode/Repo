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
import org.imgscalr.*;

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
        
        return ok(addProduct.render(productForm, User.getUserById(session().get("email"))));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addProductSubmit(){
        Form<Product> newProductForm = formFactory.form(Product.class).bindFromRequest();
        MultipartFormData<File> data = request().body().asMultipartFormData();
        FilePart<File> image = data.getFile("upload");

        if(newProductForm.hasErrors()){
            return badRequest(addProduct.render(newProductForm, User.getUserById(session().get("email"))));
        } else {
            Product newProduct = newProductForm.get();
            if (newProduct.getProductID() == null) {
                newProduct.save();
                flash("success", "Item " + newProduct.getProductName() + " has been added successfuly!");
            } else {
                newProduct.update();
                flash("success", "Item " + newProduct.getProductName() + " has been updated successfuly!");
            }
            String saveImageMessage = saveFile(newProduct.getProductID(), image);
            return redirect(controllers.routes.ProductController.productList(0));
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
            return badRequest("error"); //redirect to add/update item with user's session
        }

        // Display the "add item" page, to allow the user to update the item
        return ok(addProduct.render(productForm,(User.getUserById(session().get("email")))));
    }


/*******************************************OLD******************************************* */
    // public Result addProductSubmit() {
    //     Form<Product> productForm = formFactory.form(Product.class).bindFromRequest();

    //     if(productForm.hasErrors()){
    //         return badRequest(addProduct.render(productForm, User.getUserById(session().get("email"))));
    //     } else {
    //         Product newProduct = productForm.get();
    //         MultipartFormData<File> data = request().body().asMultipartFormData();
    //         // FilePart<File> image = data.getFile("upload");

    //         if(Product.getProductById(newProduct.getProductID()) == null) {
    //             newProduct.save();
    //             // flash("success", "Item " + newProduct.getProductName() + " has been added successfuly!");
    //             } else {
    //             newProduct.update();
    //             // flash("success", "Item " + newProduct.getProductName() + " has been updated successfuly!");
    //             }
    //             // String saveImageMessage = saveFile(newProduct.getProductID(), image);
    //         flash("success", "Item " + newProduct.getProductName() + " has been added/updated successfuly!");
    //         return redirect(controllers.routes.ProductController.productList(0));
    //     }
    // }     
            //THIS IS/WAS PART OF ADDITEMSUBMIT
            // if (Product.getProductById(newProduct.getProductID()) == null) {
            //     MultipartFormData<File> data = request().body().asMultipartFormData();
            //     FilePart<File> image = data.getFile("upload");

            //     newProduct.save();

            //     String saveImageMessage = saveFile(newProduct.getProductID(), image);
                
            //     flash("success","Item " + newProduct.getProductName() + " was added.");
            //     return redirect(controllers.routes.ProductController.productList(0));
            // } else {
            //        newProduct.update();
            //     flash("success", "Item " + newProduct.getProductName() + " was updated.");
            //     return redirect(controllers.routes.ProductController.productList(0));
            // }    
    /*******************************************OLD******************************************* */

    // this method gets all the products from the database and passes them into the productList view, which displays them

    public Result productList(Long cat) {
        List<Product> itemList = null;
        List<Category> categoryList = Category.findAll();

        if(cat == 0){
            itemList = Product.findAll();
        }else {
            itemList = Category.find.ref(cat).getItems();

        }
        return ok(productList.render(itemList, categoryList,User.getUserById(session().get("email")), env));
    }
      
    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    @Transactional
    public Result deleteItem(Long productID){
        Product.find.ref(productID).delete();
        flash("Success", "Product has been deleted");
        return redirect(controllers.routes.ProductController.productList(0));
    }

    public String saveFile(Long productID, FilePart<File> uploaded){
        if(uploaded != null){ //making sure the file doesn't exist
            String mimeType = uploaded.getContentType(); //making sure its an image
            if(mimeType.startsWith("image/")){
                String fileName = uploaded.getFilename();
                //exctracting the extension of the image
                String extension = "";
                int index = fileName.lastIndexOf('.');
                if(index >= 0){
                    extension = fileName.substring(index + 1);
                }

                File file = uploaded.getFile();
                //checking if the directories in the specified path exist, if they do not they are created
                File directory = new File("/assets/images/productImages");
                if(!directory.exists()){
                    directory.mkdirs();
                }
                //saving the image
                File newFile = new File("/assets/images/productImages/" + productID + "." + extension);
                if(file.renameTo(newFile)){
                   
                    try{
                        BufferedImage image = ImageIO.read(newFile);
                        BufferedImage scaledImage = Scalr.resize(image, 120);
                        if(ImageIO.write(scaledImage, extension, new File("/assets/images/productImages/" + productID + "thumbnail.jpg"))){
                            return "/ file uploaded and thumbnail created.";
                        } else {
                            return "/ file uploaded but thumbnail creation failed.";
                        }
                    } catch (IOException e) {
                        return "/ file uploaded but thumbnail creation failed.";
                    }

                } else {
                    return "/ file upload failed.";
                }
            }
        } else {
            return "/ no image file.";
        }
        return "/ this should not be returned";
    }

}




