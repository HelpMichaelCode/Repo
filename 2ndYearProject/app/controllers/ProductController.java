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
import models.products.*;
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
        if(Product.getLowQty().size() > 0){
            String lowQtyStr = "Restock needed! Check product list!";
            flash("warning", lowQtyStr);
        }

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
                String saveImageMessage = saveFile(newProduct.getProductID(), image);

                if(newProduct.getCategory().getId() == null){
                    flash("error", "Please select a category first.");
                    return badRequest(addProduct.render(newProductForm, User.getUserById(session().get("email")), "Add product to BLDPC"));
                } else {
                    newProduct.save();
                    flash("success", "Item " + newProduct.getProductName() + " has been added successfuly!");
                    switch(newProduct.getCategory().getId().intValue()){
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            return redirect(controllers.routes.ProductController.addTrendingPC(newProduct.getProductID()));
                        case 7:
                            return redirect(controllers.routes.ProductController.addProcessor(newProduct.getProductID()));
                        case 8:
                            return redirect(controllers.routes.ProductController.addMotherboard(newProduct.getProductID()));
                        case 9:
                            return redirect(controllers.routes.ProductController.addRam(newProduct.getProductID()));
                        case 11:
                            return redirect(controllers.routes.ProductController.addStorage(newProduct.getProductID()));
                        case 10:
                            return redirect(controllers.routes.ProductController.addGraphicsCard(newProduct.getProductID()));
                        default:
                            return redirect(controllers.routes.ProductController.productList(0, ""));
                    }
                }
            } else {
                newProduct.update();
                flash("success", "Item " + newProduct.getProductName() + " has been updated successfuly!");
                switch(newProduct.getCategory().getId().intValue()){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        return redirect(controllers.routes.ProductController.updateTrendingPC(newProduct.getProductID()));
                    case 7:
                        return redirect(controllers.routes.ProductController.updateProcessor(newProduct.getProductID()));
                    case 8:
                        return redirect(controllers.routes.ProductController.updateMotherboard(newProduct.getProductID()));
                    case 9:
                        return redirect(controllers.routes.ProductController.updateRam(newProduct.getProductID()));
                    case 11:
                        return redirect(controllers.routes.ProductController.updateStorage(newProduct.getProductID()));
                    case 10:
                        return redirect(controllers.routes.ProductController.updateGraphicsCard(newProduct.getProductID()));
                    default:
                        return redirect(controllers.routes.ProductController.productList(0, ""));
                }
            }
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateItem(Long id) {

        if(Product.getLowQty().size() > 0){
            String lowQtyStr = "Restock needed! Check product list!";
            flash("warning", lowQtyStr);
        }


        Product p;
        Form<Product> productForm;
            // Find the item by id
            p = Product.getProductById(id);

            // Populate the form object with data from the item found in the database
            if(p != null) {
                productForm = formFactory.form(Product.class).fill(p);
            } else {
                List<ProductSkeleton> specs = getSpecs(Long.valueOf(0), "");
                return badRequest(productList.render(Product.findAll(), specs, Category.findAll(), User.getUserById(session().get("email")), env));
                //redirect to add/update item with user's session
            }

        // Display the "add product" page, to allow the user to update the item
        return ok(addProduct.render(productForm,(User.getUserById(session().get("email"))), "Update product "+p.getProductName()));
    }

    // this method gets all the products from the database and passes them into the productList view, which displays them

    public Result productList(Long cat, String keyword) {
        if(Product.getLowQty().size() > 0){
            String lowQtyStr = "Restock needed! Check product list!";
            flash("warning", lowQtyStr);
        }

        List<Category> categoryList = Category.findAll();
        List<ProductSkeleton> specs = null;
        
        if(keyword == null){
            keyword = "";
        } 
        specs=getSpecs(cat, keyword);
        return ok(productList.render(Product.findAll(), specs, categoryList, User.getUserById(session().get("email")), env));
    }
      
    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    @Transactional
    public Result deleteItem(Long productID){

        if(Product.getLowQty().size() > 0){
            String lowQtyStr = "Restock needed! Check product list!";
            flash("warning", lowQtyStr);
        }

        Product.find.ref(productID).delete();
        flash("success", "Product has been deleted");
        return redirect(controllers.routes.ProductController.productList(0, ""));
    }

    public String saveFile(Long productID, FilePart<File> uploaded){
        if(uploaded != null){ //making sure the file doesn't exist
            String mimeType = uploaded.getContentType(); //making sure its an image
            if(mimeType.startsWith("image/")){
                String fileName = uploaded.getFilename();
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
            return badRequest(product.render(productForm.get(), Review.findAll(), filtered, reviewForm, productForm, User.getUserById(session().get("email")), env));
        } else {
            Review newReview = reviewForm.get();
            Product temp = productForm.get();
            Product productObj = Product.getProductById(temp.getProductID());
            newReview.setUser(User.getUserById(session().get("email")));
            newReview.setProduct(productObj);
            boolean flag = false;
            for(Review r: productObj.getReviews()){
                if(r.getUser().getEmail().equals(newReview.getUser().getEmail())){
                    flag = true;
                }
            }

            if(!flag){
                productObj.calculateRating(newReview.getRating());
                productObj.update();
            }
                
            
            if(newReview.getId() == null){
                if(newReview.getBody().equals("")){
                    newReview.setBody("No comment");
                }
                newReview.save();
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
    public Result deleteReview(Long id, String email){
        Review.find.ref(id).delete();
        flash("Success", "Review has been deleted");
        return redirect(controllers.routes.ProductController.userReviews(email)); 
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result userReviews(String email){
        List<Review> reviews = Review.findAll();
        return ok(userReviews.render(reviews, email, User.getUserById(session().get("email"))));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addTrendingPC(Long p){
        Form<TrendingPC> productForm = formFactory.form(TrendingPC.class);
        
        return ok(addTrendingPC.render(productForm, p, User.getUserById(session().get("email")), "Add PC info to BLDPC"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addTrendingPCSubmit(Long pid){
        Form<TrendingPC> newForm = formFactory.form(TrendingPC.class).bindFromRequest();
        if(newForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addTrendingPC.render(newForm, pid, User.getUserById(session().get("email")), "Add PC info to BLDPC"));
        } else {
            TrendingPC pc = newForm.get();
            pc.setProduct(Product.getProductById(pid));
            if(pc.getProductId() == null){
                for(Processor cpu: Processor.findAll()){
                    if(pc.getCpu().getProductId() == cpu.getProductId()){
                        pc.setCpu(cpu);
                         flash("success", "CPU id --" + cpu.getProductId()); //never gets to this one
                    }
                    
                }
                pc.setProductId(pid);
                pc.save();
                flash("success", "PC " + pc.getName() + " was added");
                return redirect(controllers.routes.HomeController.index());
            } else {
                pc.update();
                flash("success", "PC " + pc.getName() + " was updated");
                return redirect(controllers.routes.HomeController.index());
            }
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateTrendingPC(Long pid){
        List<TrendingPC> all = TrendingPC.findAll();
        Form<TrendingPC> form;
        TrendingPC tp = null;
        for(TrendingPC e: all){
            if(e.getProduct().getProductID() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(TrendingPC.class).fill(tp);
        } else {
            return badRequest(productList.render(Product.findAll(), getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
        }
        return ok(addTrendingPC.render(form, pid, User.getUserById(session().get("email")), "Update PC info"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addRam(Long p){
        Product temp = Product.getProductById(p);
        Form<Ram> productForm = formFactory.form(Ram.class);
        return ok(addRam.render(productForm, p, User.getUserById(session().get("email")), "Add RAM info to BLDPC"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addRamSubmit(Long pid){
        Form<Ram> newForm = formFactory.form(Ram.class).bindFromRequest();
        if(newForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addRam.render(newForm, pid, User.getUserById(session().get("email")), "Add RAM info to BLDPC"));
        } else {
                Ram r = newForm.get();
                r.setProduct(Product.getProductById(pid));
            if(r.getProduct().getProductID() == null){
                r.save();
                flash("success", r.getName() + " was added");
            } else {
                r.update();
                flash("success", r.getName() + " was updated");
            }
            return redirect(controllers.routes.HomeController.index());
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateRam(Long pid){
        List<Ram> all = Ram.findAll();
        Form<Ram> form;
        Ram tp = null;
        for(Ram e: all){
            if(e.getProduct().getProductID() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Ram.class).fill(tp);
        } else {
            return badRequest(productList.render(Product.findAll(), getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
        }
        return ok(addRam.render(form, pid, User.getUserById(session().get("email")), "Update RAM info"));
    }

        @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addStorage(Long p){
        Product temp = Product.getProductById(p);
        Form<Storage> productForm = formFactory.form(Storage.class);
        return ok(addStorage.render(productForm, p, User.getUserById(session().get("email")), "Add storage memory info to BLDPC"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addStorageSubmit(Long pid){
        Form<Storage> newForm = formFactory.form(Storage.class).bindFromRequest();
        if(newForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addStorage.render(newForm, pid, User.getUserById(session().get("email")), "Add storage memory info to BLDPC"));
        } else {
                Storage s = newForm.get();
                s.setProduct(Product.getProductById(pid));
            if(s.getProduct().getProductID() == null){
                s.save();
                flash("success", s.getName() + " was added");
            } else {
                s.update();
                flash("success", s.getName() + " was updated");
            }
            return redirect(controllers.routes.HomeController.index());
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateStorage(Long pid){
        List<Storage> all = Storage.findAll();
        Form<Storage> form;
        Storage tp = null;
        for(Storage e: all){
            if(e.getProduct().getProductID() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Storage.class).fill(tp);
        } else {
            return badRequest(productList.render(Product.findAll(), getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
        }
        return ok(addStorage.render(form, pid, User.getUserById(session().get("email")), "Update storage memory info"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addProcessor(Long p){
        Form<Processor> productForm = formFactory.form(Processor.class);
        
        return ok(addProcessor.render(productForm, p, User.getUserById(session().get("email")), "Add processor info to BLDPC"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addProcessorSubmit(Long pid){
        Form<Processor> newProcessorForm = formFactory.form(Processor.class).bindFromRequest();
        if(newProcessorForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addProcessor.render(newProcessorForm, pid, User.getUserById(session().get("email")), "Add processor info to BLDPC"));
        } else {
            Processor newCpu = newProcessorForm.get();
            newCpu.setProduct(Product.getProductById(pid));
            if(newCpu.getProduct().getProductID() == null){
                newCpu.save();
                flash("success", "Processor " + newCpu.getName() + " was added");
            } else {
                newCpu.update();
                flash("success", "Processor " + newCpu.getName() + " was updated");
            }
            return redirect(controllers.routes.HomeController.index());
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateProcessor(Long pid){
        List<Processor> all = Processor.findAll();
        Form<Processor> form;
        Processor tp = null;
        for(Processor e: all){
            if(e.getProduct().getProductID() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Processor.class).fill(tp);
        } else {
            return badRequest(productList.render(Product.findAll(), getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
        }
        return ok(addProcessor.render(form, pid, User.getUserById(session().get("email")), "Update processor info"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addGraphicsCard(Long pid){
        Form<GraphicsCard> gpuForm = formFactory.form(GraphicsCard.class);
        return ok(addGraphicsCard.render(gpuForm, pid, User.getUserById(session().get("email")), "Add GPU info to BLDPC"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addGraphicsCardSubmit(Long pid){
        Form<GraphicsCard> gpuForm = formFactory.form(GraphicsCard.class).bindFromRequest();
        if(gpuForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addGraphicsCard.render(gpuForm, pid, User.getUserById(session().get("email")), "Add GPU info to BLDPC"));
        } else {
            GraphicsCard gpu = gpuForm.get();
            gpu.setProduct(Product.getProductById(pid));
            if(gpu.getProduct().getProductID() == null){
                gpu.save();
                flash("success", "GPU " + gpu.getName() + " was added");
            } else {
                gpu.update();
                flash("success", "GPU " + gpu.getName() + " was updated");
            }
            return redirect(controllers.routes.HomeController.index());
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateGraphicsCard(Long pid){
        List<GraphicsCard> all = GraphicsCard.findAll();
        Form<GraphicsCard> form;
        GraphicsCard tp = null;
        for(GraphicsCard e: all){
            if(e.getProduct().getProductID() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(GraphicsCard.class).fill(tp);
        } else {
            return badRequest(productList.render(Product.findAll(), getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
        }
        return ok(addGraphicsCard.render(form, pid, User.getUserById(session().get("email")), "Update GPU info"));

    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addMotherboard(Long pid){
        Form<Motherboard> mbForm = formFactory.form(Motherboard.class);
        return ok(addMotherboard.render(mbForm, pid, User.getUserById(session().get("email")), "Add motherboard info to BLDPC"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addMotherboardSubmit(Long pid){
        Form<Motherboard> mbForm = formFactory.form(Motherboard.class).bindFromRequest();
        if(mbForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addMotherboard.render(mbForm, pid, User.getUserById(session().get("email")), "Add motherboard info to BLDPC"));
        } else {
            Motherboard mb = mbForm.get();
            mb.setProduct(Product.getProductById(pid));
            if(mb.getProduct().getProductID() == null){
                mb.save();
                flash("success", "Motherboard " + mb.getName() + " was added");
            } else {
                mb.update();
                flash("success", "Motherboard " + mb.getName() + " was updated");
            }
            return redirect(controllers.routes.HomeController.index());
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateMotherboard(Long pid){
        List<Motherboard> all = Motherboard.findAll();
        Form<Motherboard> form;
        Motherboard tp = null;
        for(Motherboard e: all){
            if(e.getProduct().getProductID() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Motherboard.class).fill(tp);
        } else {
            return badRequest(productList.render(Product.findAll(), getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
        }
        return ok(addMotherboard.render(form, pid, User.getUserById(session().get("email")), "Add motherboard info to BLDPC"));
    }


    public static List<ProductSkeleton> getSpecs(Long cat, String keyword){
        keyword = keyword.toLowerCase();
        List<ProductSkeleton> specs = new ArrayList<>();
        List<Product> itemList = null;
        if(cat==0){
            itemList = Product.findAll();
        } else {
            itemList = Category.find.ref(cat).getItems();
        }
        for(Product p: itemList) {
            if(p.getProductName().toLowerCase().contains(keyword) ||
            p.getProductDescription().toLowerCase().contains(keyword)){
                String lcName = p.getCategory().getName().toLowerCase();
                switch(lcName){
                    case "gaming pcs":
                    case "gaming laptops":
                    case "home pcs":
                    case "home laptops":
                    case "top spec pcs":
                    case "workstations":
                        for(TrendingPC temp: TrendingPC.findAll()){
                            if(temp.getProductId().toString().equals(p.getProductID().toString()))
                                specs.add(temp);
                        }
                        break;
                    case "cpus":
                        for(Processor temp: Processor.findAll()){
                            if(temp.getProduct().getProductID() == p.getProductID())
                                specs.add(temp);
                        }
                        break;
                    case "graphics cards":
                        for(GraphicsCard temp: GraphicsCard.findAll()){
                            if(temp.getProduct().getProductID() == p.getProductID())
                            specs.add(temp);
                        }
                        break;
                    case "motherboards":
                        for(Motherboard temp: Motherboard.findAll()){
                            if(temp.getProduct().getProductID() == p.getProductID())
                            specs.add(temp);
                        }
                        break;
                    case "ram":
                        for(Ram temp: Ram.findAll()){
                            if(temp.getProduct().getProductID() == p.getProductID())
                            specs.add(temp);
                        }
                        break;
                    case "storage":
                        for(Storage temp: Storage.findAll()){
                            if(temp.getProduct().getProductID() == p.getProductID())
                            specs.add(temp);
                        }
                        break;
                    }
                }
            }
            return specs;
    }
}



