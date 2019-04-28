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
import models.shopping.*;
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
        flashLowStock();

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
                    // if(checkStringLen(newProduct.getProductName()) || checkStringLen(newProduct.getProductDescription())){
                    if(newProduct.checkLengthOfStrings()){
                        flash("error", "Text is too long please try using less than 255 characters.");
                        return badRequest(addProduct.render(newProductForm, User.getUserById(session().get("email")), "Add product to BLDPC"));
                    }
                    newProduct.save();
                    String saveImageMessage = saveFile(newProduct.getProductID(), image);
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
        flashLowStock();
        Product p;
        Form<Product> productForm;
            // Find the item by id
            p = Product.getProductById(id);

            // Populate the form object with data from the item found in the database
            if(p != null) {
                productForm = formFactory.form(Product.class).fill(p);
            } else {
                prodNotFound();
                List<ProductSkeleton> specs = getSpecs(Long.valueOf(0), "");
                return badRequest(productList.render(specs, Category.findAll(), User.getUserById(session().get("email")), env, "" + 0 ));
            }

        // Display the "add product" page, to allow the user to update the item
        return ok(addProduct.render(productForm,(User.getUserById(session().get("email"))), "Update product "+p.getProductName()));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result restock(Long id){
        Restock p;
        Form<Restock> productForm;
            // Find the item by id
        p = new Restock(Product.getProductById(id));
        // Populate the form object with data from the item found in the database
        if(p != null) {
            productForm = formFactory.form(Restock.class).fill(p);
        } else {
            prodNotFound();
            return redirect(controllers.routes.ProductController.lowStockProducts(Long.valueOf(0)));
        }
        return ok(restock.render(id, productForm,(User.getUserById(session().get("email"))), "Restock product " + p.getProductName()));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result restockSubmit(Long id){
       Form<Restock> form = formFactory.form(Restock.class).bindFromRequest();
       if(form.hasErrors()){
            flash("error", "Fill in all the fields.");
            return badRequest(restock.render(id, form, User.getUserById(session().get("email")), "Restock product"));
        } else {
            Restock r = form.get();
            Product p = Product.getProductById(id);
            if(p.getProductID() != null){
                if(r.getProductID() == p.getProductID()){
                    if(r.getRestock() > 100){
                        flash("error", "You cannot add more than 100 items of one type!");
                        return badRequest(restock.render(id, form,(User.getUserById(session().get("email"))), "Restock product " + p.getProductName()));
                    } else if(r.getRestock() <= 0){
                        flash("error", "Negative value detected! Please try again!");
                        return badRequest(restock.render(id, form,(User.getUserById(session().get("email"))), "Restock product " + p.getProductName()));
                    } else {
                        p.restock(r.getRestock());
                        p.update();
                        flash("success", "Restocking successful");
                    }
                }
                return redirect(controllers.routes.ProductController.lowStockProducts(Long.valueOf(0)));
            } else {
                prodNotFound();
                return badRequest(restock.render(id, form, User.getUserById(session().get("email")), "Restock product"));
            }
        }
    }

    // this method gets all the products from the database and passes them into the productList view, which displays them
    public Result productList(Long cat, String keyword) {
        flashLowStock();

        List<Category> categoryList = Category.findAll();
        // categoryList.remove(Category.getCategoryById(Long.valueOf(1000)));
        List<ProductSkeleton> specs = null;

        if(keyword == null){
            keyword = "";
        }
        specs=getSpecs(cat, keyword);
        return ok(productList.render(specs, categoryList, User.getUserById(session().get("email")), env, "category " + cat));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    @Transactional
    public Result deleteItem(Long productID){
        
        String deleted = "";
        if(Product.getProductById(productID) == null){
            prodNotFound();
            return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
        } else {
            List<ProductSkeleton> all = getSpecs(Product.getProductById(productID).getCategory().getId(), "");
            for(ProductSkeleton e: all){
                switch(e.getProduct().getCategory().getName().toLowerCase()){
                    case "gaming pcs":
                    case "gaming laptops":
                    case "home pcs":
                    case "home laptops":
                    case "top spec pcs":
                    case "workstations":
                        if(e.getProductId().toString().equals(productID.toString())){
                            deleted = TrendingPC.getTrendingPCById(productID).getName();
                            TrendingPC.find.ref(productID).delete();
                        }
                        break;
                    case "cpus":
                        if(e.getProductId().toString().equals(productID.toString())){
                            for(TrendingPC temp: TrendingPC.findAll()){
                                if(temp.getCpu().getProductId().toString().equals(productID.toString())){
                                    temp.setCpu(Processor.getProcessorById(Long.valueOf(1)));
                                    temp.update();

                                }
                            }
                        deleted = Processor.getProcessorById(productID).getName();
                        Processor.find.ref(productID).delete();
                        }
                        break;
                    case "graphics cards":
                        if(e.getProductId().toString().equals(productID.toString())){
                            for(TrendingPC temp: TrendingPC.findAll()){
                                if(temp.getGpu().getProductId().toString().equals(productID.toString())){
                                    temp.setGpu(GraphicsCard.getGraphicsCardById(Long.valueOf(1)));
                                    temp.update();

                                }
                            }
                        deleted = GraphicsCard.getGraphicsCardById(productID).getName();
                        GraphicsCard.find.ref(productID).delete();
                        }
                        break;
                    case "motherboards":
                    if(e.getProductId().toString().equals(productID.toString())){
                            for(TrendingPC temp: TrendingPC.findAll()){
                                if(temp.getMotherboard().getProductId().toString().equals(productID.toString())){
                                    temp.setMotherboard(Motherboard.getMotherboardById(Long.valueOf(1)));
                                    temp.update();

                                }
                            }
                        deleted = Motherboard.getMotherboardById(productID).getName();
                        Motherboard.find.ref(productID).delete();
                        }
                        break;
                    case "ram":
                        if(e.getProductId().toString().equals(productID.toString())){
                            for(TrendingPC temp: TrendingPC.findAll()){
                                if(temp.getRam().getProductId().toString().equals(productID.toString())){
                                    temp.setRam(Ram.getRamById(Long.valueOf(1)));
                                    temp.update();

                                }
                            }
                        deleted = Ram.getRamById(productID).getName();
                        Ram.find.ref(productID).delete();
                        }
                        break;
                    case "storage":
                        if(e.getProductId().toString().equals(productID.toString())){
                            for(TrendingPC temp: TrendingPC.findAll()){
                                if(temp.getStorage().getProductId().toString().equals(productID.toString())){
                                    temp.setStorage(Storage.getStorageById(Long.valueOf(1)));
                                    temp.update();

                                }
                            }
                        deleted = Storage.getStorageById(productID).getName();
                        Storage.find.ref(productID).delete();
                        }
                        break;
                }
            }
            flash("success", deleted + " has been deleted");
            return redirect(controllers.routes.ProductController.productList(0, ""));
        }
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
                if(!directory.exists()){
                    directory.mkdirs();
                }
                //saving the image
                File newFile = new File("public/images/productImages/", productID + "." + extension);
                if(file.renameTo(newFile)){
                   
                    try{
                        BufferedImage image = ImageIO.read(newFile);
                        return "/ file uploaded";
                    } catch (IOException e) {
                        return "/ file uploaded";
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

    public Result displayProduct(String productName){
        Form<Review> reviewForm = formFactory.form(Review.class);
        List<Review> filtered = new ArrayList<>();

        for(Product e: Product.findAll()){
            if(e.getProductName().equals(productName)){
                for(Review r: Review.findAll()){
                    if(r.getProduct().getProductID() == e.getProductID()){
                        filtered.add(r);
                    }
                }
                ProductSkeleton ps;
                switch(e.getCategory().getId().intValue()){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        ps = TrendingPC.getTrendingPCById(e.getProductID());
                        break;
                    case 7:
                        ps = Processor.getProcessorById(e.getProductID());
                        break;
                    case 8:
                        ps = Motherboard.getMotherboardById(e.getProductID());
                        break;
                    case 9:
                        ps = Ram.getRamById(e.getProductID());
                        break;
                    case 11:
                        ps = Storage.getStorageById(e.getProductID());
                        break;
                    case 10:
                        ps = GraphicsCard.getGraphicsCardById(e.getProductID());
                        break;
                    default:
                        ps = TrendingPC.getTrendingPCById(Long.valueOf(1000));
                }
                return ok(product.render(e, ps, filtered, reviewForm, User.getUserById(session().get("email")), env));
            }
        }
        prodNotFound();
        return redirect(controllers.routes.ProductController.productList(0, ""));
    }


    // @Transactional
    @Security.Authenticated(Secured.class)
    public Result addReviewSubmit(Long pid){
        Form<Review> reviewForm = formFactory.form(Review.class).bindFromRequest();
        Product productObj = Product.getProductById(pid);
        List<Review> filtered = new ArrayList<>();
        if(productObj == null){
            prodNotFound();
            return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
        } else {
            for(Review r: Review.findAll()){
                if(r.getProduct().getProductID() == productObj.getProductID()){
                    filtered.add(r);
                }
            }
            if(reviewForm.hasErrors()){
                flash("error", "Please fill in all the fields!");
                return redirect(controllers.routes.ProductController.displayProduct(productObj.getProductName()));
            } else {
                Review newReview = reviewForm.get();
                newReview.setUser(User.getUserById(session().get("email")));
                newReview.setProduct(productObj);
                boolean alreadyReviewed = false;
                boolean userHasPurchasedProduct = false;

                for(ShopOrder s: User.getUserById(session().get("email")).getOrders()){
                    for(OrderLine o: s.getProducts()){
                        if(o.getProduct().getProductID() == newReview.getProduct().getProductID()){
                            userHasPurchasedProduct = true;
                        }
                    }
                }
                for(Review r: productObj.getReviews()){
                    if(r.getUser().getEmail().equals(newReview.getUser().getEmail())){
                        alreadyReviewed = true;
                    }
                }

                if(userHasPurchasedProduct){
                    if(newReview.getId() == null){
                        if(newReview.checkLengthOfStrings()){
                        // if(checkStringLen(newReview.getBody())){
                            flash("error", "Text is too long please try using less than 255 characters.");
                            return redirect(controllers.routes.ProductController.displayProduct(newReview.getProduct().getProductName()));
                        }
                        if(!alreadyReviewed){
                            productObj.calculateRating(newReview.getRating());
                            productObj.update();
                        } else {
                            flash("error", "You have reviewed this product already. If you wish to update the comment or rating of the review please contact an admin");
                            return redirect(controllers.routes.ProductController.displayProduct(newReview.getProduct().getProductName()));
                        }
                        if(newReview.getBody().equals("")){
                            newReview.setBody("-No comment-");
                        }
                        newReview.save();
                        flash("success", "Thank you for you feedback!");

                            // Calculate the overall rating of the product
                        return redirect(controllers.routes.ProductController.displayProduct(newReview.getProduct().getProductName()));
                    } else {
                        flash("error", "We ran into a problem processing your review, please try again later.");
                        return redirect(controllers.routes.ProductController.displayProduct(newReview.getProduct().getProductName()));
                    }
                } else {
                    flash("error", "You need to purchase the product before reviewing it!");
                    return redirect(controllers.routes.ProductController.displayProduct(newReview.getProduct().getProductName()));
                }
            }
        }
    }
    

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    @Transactional
    public Result deleteReview(Long id, String email, String page){
        Review delete = Review.getReviewById(id);
        if(delete == null){
            flash("error", "Review not found");
        } else {
            delete.delete();
            flash("Success", "Review has been deleted");
        }
        if(page.equals("all")){
            return redirect(controllers.routes.ProductController.allReviews());
        } else {
            return redirect(controllers.routes.ProductController.userReviews(email));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result userReviews(String email){
        List<Review> reviews = new ArrayList<>();
        try{
            String testingEmail = User.getUserById(email).getEmail();
        } catch ( NullPointerException ex ) {
            flash("error", "User not found");
            return redirect(controllers.routes.ProductController.allReviews());
        }

        for(Review r: Review.findAll()){
            if(r.getUser().getEmail().equals(email)){
                reviews.add(r);
            }
        }
        return ok(userReviews.render(reviews, email, User.getUserById(session().get("email"))));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result allReviews(){
        return ok(allReviews.render(Review.findAll(), User.getUserById(session().get("email"))));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addTrendingPC(Long p){
        try{
            Long prodIdTest = Product.getProductById(p).getProductID();
            if(Product.getProductById(p).getCategory().getId() > 6){
                flash("error", "Product is in different category");
                return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
            }
        } catch (NullPointerException ex) {
            prodNotFound();
            return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
        }
        try{
            String testExists = TrendingPC.getTrendingPCById(p).getName();
            return redirect(controllers.routes.ProductController.updateTrendingPC(p));
        } catch (NullPointerException ex) {
            Form<TrendingPC> productForm = formFactory.form(TrendingPC.class);
            return ok(addTrendingPC.render(productForm, p, User.getUserById(session().get("email")), "Add PC info to BLDPC"));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addTrendingPCSubmit(Long pid){
        String addPcString = "Add PC info to BLDPC";
        Form<TrendingPC> newForm = formFactory.form(TrendingPC.class).bindFromRequest();
        
        if(newForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addTrendingPC.render(newForm, pid, User.getUserById(session().get("email")), addPcString));
        } else {
            TrendingPC pc = newForm.get();
            // if(checkStringLen(pc.getName()) || checkStringLen(pc.getManufacturer())){
            if(pc.checkLengthOfStrings()){
                flash("error", "Text is too long please try using less than 255 characters.");
                return badRequest(addTrendingPC.render(newForm, pid, User.getUserById(session().get("email")), addPcString));
            }
            pc.setProduct(Product.getProductById(pid));
            if(pc.getCpu().getProductId() == null){
                pc.setCpu(Processor.getProcessorById(Long.valueOf(1)));
            }
            if(pc.getGpu().getProductId() == null){
                pc.setGpu(GraphicsCard.getGraphicsCardById(Long.valueOf(1)));
            }
            if(pc.getMotherboard().getProductId() == null){
                pc.setMotherboard(Motherboard.getMotherboardById(Long.valueOf(1)));
            }
            if(pc.getRam().getProductId() == null){
                pc.setRam(Ram.getRamById(Long.valueOf(1)));
            }
            if(pc.getStorage().getProductId() == null){
                pc.setStorage(Storage.getStorageById(Long.valueOf(1)));
            }
            if(pc.getProductId() == null){
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
            if(e.getProductId().toString().equals(pid.toString())){
                tp = e;
                break;
            }
        }
        if(tp != null){
            form = formFactory.form(TrendingPC.class).fill(tp);
            return ok(addTrendingPC.render(form, pid, User.getUserById(session().get("email")), "Update PC info"));
        } else {
            prodNotFound();
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env, "" + 0));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addRam(Long p){
        try{
            Long prodIdTest = Product.getProductById(p).getProductID();
            if(Product.getProductById(p).getCategory().getId() != 9){
                flash("error", "Product is in different category");
                return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
            }
        } catch (NullPointerException ex) {
            prodNotFound();
            return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
        }
        try{
            String testExists = Ram.getRamById(p).getName();
            return redirect(controllers.routes.ProductController.updateRam(p));
        } catch (NullPointerException ex) {
            Form<Ram> productForm = formFactory.form(Ram.class);
            return ok(addRam.render(productForm, p, User.getUserById(session().get("email")), "Add RAM info to BLDPC"));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addRamSubmit(Long pid){
        String addRamString = "Add RAM info to BLDPC";
        Form<Ram> newForm = formFactory.form(Ram.class).bindFromRequest();
        if(newForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addRam.render(newForm, pid, User.getUserById(session().get("email")), addRamString));
        } else {
            Ram r = newForm.get();
            if(r.checkLengthOfStrings()){
            // if(checkStringLen(r.getCapacity()) || checkStringLen(r.getName()) || checkStringLen(r.getManufacturer())){
                flash("error", "Text is too long please try using less than 255 characters.");
                return badRequest(addRam.render(newForm, pid, User.getUserById(session().get("email")), addRamString));
            }
            r.setProduct(Product.getProductById(pid));            
            if(r.getProductId() == null){
                r.setProductId(pid);
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
            if(e.getProductId().toString().equals(pid.toString())){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Ram.class).fill(tp);
        } else {
            prodNotFound();
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env, "" + 0));
        }
        return ok(addRam.render(form, pid, User.getUserById(session().get("email")), "Update RAM info"));
    }

        @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addStorage(Long p){
        try{
            Long prodIdTest = Product.getProductById(p).getProductID();
            if(Product.getProductById(p).getCategory().getId() != 11){
                flash("error", "Product is in different category");
                return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
            }
        } catch (NullPointerException ex) {
            prodNotFound();
            return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
        }
        try{
            String testExists = Storage.getStorageById(p).getName();
            return redirect(controllers.routes.ProductController.updateStorage(p));
        } catch (NullPointerException ex) {
            Form<Storage> productForm = formFactory.form(Storage.class);
            return ok(addStorage.render(productForm, p, User.getUserById(session().get("email")), "Add storage memory info to BLDPC"));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addStorageSubmit(Long pid){
        String addStorageString = "Add storage memory info to BLDPC";
        Form<Storage> newForm = formFactory.form(Storage.class).bindFromRequest();
        if(newForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addStorage.render(newForm, pid, User.getUserById(session().get("email")), addStorageString));
        } else {
            Storage s = newForm.get();
            if(s.checkLengthOfStrings()){
            // if(checkStringLen(s.getCapacity()) || checkStringLen(s.getName()) || checkStringLen(s.getManufacturer())){
                flash("error", "Text is too long please try using less than 255 characters.");
                return badRequest(addStorage.render(newForm, pid, User.getUserById(session().get("email")), addStorageString));
            }
            s.setProduct(Product.getProductById(pid));
            if(s.getProductId() == null){
                s.setProductId(pid);
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
            if(e.getProductId().toString().equals(pid.toString())){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Storage.class).fill(tp);
        } else {
            prodNotFound();
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env, "" + 0));
        }
        return ok(addStorage.render(form, pid, User.getUserById(session().get("email")), "Update storage memory info"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addProcessor(Long p){
        try{
            Long prodIdTest = Product.getProductById(p).getProductID();
            if(Product.getProductById(p).getCategory().getId() != 7){
                flash("error", "Product is in different category");
                return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
            }
        } catch (NullPointerException ex) {
            prodNotFound();
            return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
        }
        try{
            String testExists = Processor.getProcessorById(p).getName();
            return redirect(controllers.routes.ProductController.updateProcessor(p));
        } catch (NullPointerException ex) {
            Form<Processor> productForm = formFactory.form(Processor.class);
            return ok(addProcessor.render(productForm, p, User.getUserById(session().get("email")), "Add processor info to BLDPC"));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addProcessorSubmit(Long pid){
        String addCpuString = "Add processor info to BLDPC";
        Form<Processor> newProcessorForm = formFactory.form(Processor.class).bindFromRequest();
        if(newProcessorForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addProcessor.render(newProcessorForm, pid, User.getUserById(session().get("email")), addCpuString));
        } else {
            Processor newCpu = newProcessorForm.get();
            if(newCpu.checkLengthOfStrings()){
            // if(checkStringLen(newCpu.getCores()) || checkStringLen(newCpu.getClock()) ||
            // checkStringLen(newCpu.getCache()) || checkStringLen(newCpu.getName()) 
            // || checkStringLen(newCpu.getManufacturer())){
                flash("error", "Text is too long please try using less than 255 characters.");
                return badRequest(addProcessor.render(newProcessorForm, pid, User.getUserById(session().get("email")), addCpuString));
            }
            newCpu.setProduct(Product.getProductById(pid));
            if(newCpu.getProductId() == null){
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
            if(e.getProductId().toString().equals(pid.toString())){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Processor.class).fill(tp);
        } else {
            prodNotFound();
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env, "" + 0));
        }
        return ok(addProcessor.render(form, pid, User.getUserById(session().get("email")), "Update processor info"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addGraphicsCard(Long pid){
        try{
            Long prodIdTest = Product.getProductById(pid).getProductID();
            if(Product.getProductById(pid).getCategory().getId() != 10){
                flash("error", "Product is in different category");
                return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
            }
        } catch (NullPointerException ex) {
            prodNotFound();
            return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
        }
        try{
            String testExists = GraphicsCard.getGraphicsCardById(pid).getName();
            return redirect(controllers.routes.ProductController.updateGraphicsCard(pid));
        } catch (NullPointerException ex) {
            Form<GraphicsCard> productForm = formFactory.form(GraphicsCard.class);
            return ok(addGraphicsCard.render(productForm, pid, User.getUserById(session().get("email")), "Add GPU info to BLDPC"));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addGraphicsCardSubmit(Long pid){
        String addGpuString = "Add GPU info to BLDPC";
        Form<GraphicsCard> gpuForm = formFactory.form(GraphicsCard.class).bindFromRequest();
        if(gpuForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addGraphicsCard.render(gpuForm, pid, User.getUserById(session().get("email")), addGpuString));
        } else {
            GraphicsCard gpu = gpuForm.get();
            if(gpu.checkLengthOfStrings()){
            // if(checkStringLen(gpu.getBus()) || checkStringLen(gpu.getMemory()) ||
            // checkStringLen(gpu.getGpuClock()) || checkStringLen(gpu.getMemoryClock()) ||
            // checkStringLen(gpu.getName()) || checkStringLen(gpu.getManufacturer())){
                flash("error", "Text is too long please try using less than 255 characters.");
                return badRequest(addGraphicsCard.render(gpuForm, pid, User.getUserById(session().get("email")), addGpuString));
            }
            gpu.setProduct(Product.getProductById(pid));
            if(gpu.getProductId() == null){
                gpu.setProductId(pid);
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
            if(e.getProductId().toString().equals(pid.toString())){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(GraphicsCard.class).fill(tp);
        } else {
            prodNotFound();
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env, "" + 0));
        }
        return ok(addGraphicsCard.render(form, pid, User.getUserById(session().get("email")), "Update GPU info"));

    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addMotherboard(Long pid){
        try{
            Long prodIdTest = Product.getProductById(pid).getProductID();
            if(Product.getProductById(pid).getCategory().getId() != 8){
                flash("error", "Product is in different category");
                return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
            }
        } catch (NullPointerException ex) {
            prodNotFound();
            return redirect(controllers.routes.ProductController.productList(Long.valueOf(0), ""));
        }
        try{
            String testExists = Motherboard.getMotherboardById(pid).getName();
            return redirect(controllers.routes.ProductController.updateMotherboard(pid));
        } catch (NullPointerException ex) {
            Form<Motherboard> productForm = formFactory.form(Motherboard.class);
            return ok(addMotherboard.render(productForm, pid, User.getUserById(session().get("email")), "Add motherboard info to BLDPC"));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result addMotherboardSubmit(Long pid){
        String addMbString = "Add motherboard info to BLDPC";
        Form<Motherboard> mbForm = formFactory.form(Motherboard.class).bindFromRequest();
        if(mbForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(addMotherboard.render(mbForm, pid, User.getUserById(session().get("email")), addMbString));
        } else {
            Motherboard mb = mbForm.get();
            if(mb.checkLengthOfStrings()){
            // if(checkStringLen(mb.getRamSlots()) || checkStringLen(mb.getMaxRam()) ||
            // checkStringLen(mb.getName()) || checkStringLen(mb.getManufacturer())){
                flash("error", "Text is too long please try using less than 255 characters.");
                return badRequest(addMotherboard.render(mbForm, pid, User.getUserById(session().get("email")), addMbString));
            }
            mb.setProduct(Product.getProductById(pid));
            if(mb.getProductId() == null){
                mb.setProductId(pid);
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
            if(e.getProductId().toString().equals(pid.toString())){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Motherboard.class).fill(tp);
        } else {
            prodNotFound();
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env, "" + 0));
        }
        return ok(addMotherboard.render(form, pid, User.getUserById(session().get("email")), "Add motherboard info to BLDPC"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result lowStockProducts(Long cat){
        List<Product> temp = new ArrayList<>();
        List<Product> itemList = null;
        if(cat==0){
            itemList = Product.findAll();
        } else {
            itemList = Category.find.ref(cat).getItems();
        }
        for(Product p: itemList){
            for(Product low: Product.getLowQty()){
                
                if(p.getProductName().contains("N/A")){
                    break;
                }
                if(p.getProductID().toString().equals(low.getProductID().toString())){
                    temp.add(low);
                }
            }
        }
        return ok(lowProducts.render(temp, Category.findAll(), User.getUserById(session().get("email"))));
    }

    private static boolean checkLowStock(){
        if(Product.getLowQty().size() > 0){
            return true;
        } else {
            return false;
        }
    }

    protected static void flashLowStock(){
        if(checkLowStock()){
            String lowQtyStr = "Restock needed! Check low stock list!";
            flash("warning", lowQtyStr);
        }
    }

    private void prodNotFound(){
        flash("error", "Product not found");
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
                            if(temp.getProductId().toString().equals(p.getProductID().toString()))
                                specs.add(temp);
                        }
                        break;
                    case "graphics cards":
                        for(GraphicsCard temp: GraphicsCard.findAll()){
                            if(temp.getProductId().toString().equals(p.getProductID().toString()))
                            specs.add(temp);
                        }
                        break;
                    case "motherboards":
                        for(Motherboard temp: Motherboard.findAll()){
                            if(temp.getProductId().toString().equals(p.getProductID().toString()))
                            specs.add(temp);
                        }
                        break;
                    case "ram":
                        for(Ram temp: Ram.findAll()){
                            if(temp.getProductId().toString().equals(p.getProductID().toString()))
                            specs.add(temp);
                        }
                        break;
                    case "storage":
                        for(Storage temp: Storage.findAll()){
                            if(temp.getProductId().toString().equals(p.getProductID().toString()))
                            specs.add(temp);
                        }
                        break;
                    }
                }
            }
            return specs;
    }
}
