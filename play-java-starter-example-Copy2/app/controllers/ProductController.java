package controllers;

//imports for image upload
import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Http.MiltipartFormData.FilePart;
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
                MultipartFormData<File> data = request().body().asMultipartFormData();
                FilePart<File> image = data.getFile("upload");

                newProduct.save();

                String saveImageMessage = saveFile(newProduct.getProductID(), image);
                
                flash("success","Item " + newProduct.getProductName() + " was added.");
                return redirect(controllers.routes.ProductController.productList(0));
            } else {
                flash("error","Product with ID " + newProduct.getProductID() + " already exists.");
                return badRequest(addProduct.render(productForm, (User.getUserById(session().get("email")))));
            }

        }
    }

    //returns all the products

    public Result productList(Long cat) {

        List<Product> list = null;
     
        List<Category> categoryList = Category.findAll();

        if(cat == 0) {
            list = Product.findAll();
        }else {
            list = Category.find.ref(cat).getProducts();

        }
        return ok(productList.render(list, categoryList,(User.getUserById(session().get("email")))));

    }
      

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
                File directory = new File("public/images/productImages");
                id(!directory.exists()){
                    directory.mkdirs();
                }
                //saving the image
                File newFile = new File("public/images/productImages/", productID + "." + extension);
                if(file.renameTo(newFile)){
                   
                    try{
                        BufferedImage image = ImageIO.read(newFile);
                        BufferedImage scaledImage = Scalr.resize(image, 120);
                        if(ImageIO.write(scaledImage, extention, new File("public/images/productImages/", productID + "thumbnail.jpg"))){
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
    }
}