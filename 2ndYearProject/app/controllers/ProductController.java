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

        flashLowStock();


        Product p;
        Form<Product> productForm;
            // Find the item by id
            p = Product.getProductById(id);

            // Populate the form object with data from the item found in the database
            if(p != null) {
                productForm = formFactory.form(Product.class).fill(p);
            } else {
                List<ProductSkeleton> specs = getSpecs(Long.valueOf(0), "");
                return badRequest(productList.render(specs, Category.findAll(), User.getUserById(session().get("email")), env));
                //redirect to add/update item with user's session
            }

        // Display the "add product" page, to allow the user to update the item
        return ok(addProduct.render(productForm,(User.getUserById(session().get("email"))), "Update product "+p.getProductName()));
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
        return ok(productList.render(specs, categoryList, User.getUserById(session().get("email")), env));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    @Transactional
    public Result deleteItem(Long productID){
        flashLowStock();
        
        String deleted = "";
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
                for(Review r: Review.findAll()){
                    if(r.getProduct().getProductID() == e.getProductID()){
                        filtered.add(r);
                    }
                }
                return ok(product.render(e, filtered, reviewForm, User.getUserById(session().get("email")), env));
            }
        }

        return redirect(controllers.routes.ProductController.productList(0, ""));
    }

    // @Transactional
    @Security.Authenticated(Secured.class)
    public Result addReviewSubmit(Long pid){
        Form<Review> reviewForm = formFactory.form(Review.class).bindFromRequest();
        Product productObj = Product.getProductById(pid);
        List<Review> filtered = new ArrayList<>();
        for(Review r: Review.findAll()){
            if(r.getProduct().getProductID() == productObj.getProductID()){
                filtered.add(r);
            }
        }
        if(reviewForm.hasErrors()){
            flash("error", "Please fill in all the fields!");
            return badRequest(product.render(productObj, filtered, reviewForm, User.getUserById(session().get("email")), env));
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
                    if(!alreadyReviewed){
                        productObj.calculateRating(newReview.getRating());
                        productObj.update();
                    } else {
                        flash("error", "You have reviewed this product already. If you wish to update the comment or rating of the review please contact a moderator/admin");
                        return redirect(controllers.routes.ProductController.displayProduct(newReview.getProduct().getProductName()));
                    }
                    if(newReview.getBody().equals("")){
                        newReview.setBody("No comment");
                    }
                    newReview.save();
                    flash("success", "Thank you for you feedback!");

                        // Calculate the overall rating of the product
                    return redirect(controllers.routes.ProductController.displayProduct(newReview.getProduct().getProductName()));
                } else {
                    flash("error", "We ran into a problem processing your review, please try again later.");
                    return badRequest(product.render(newReview.getProduct(), filtered, reviewForm, User.getUserById(session().get("email")), env));
                }
            } else {
                flash("error", "You need to purchase the product before reviewing it!");
                return badRequest(product.render(newReview.getProduct(), filtered, reviewForm, User.getUserById(session().get("email")), env));
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
        List<Review> reviews = new ArrayList<>();
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
            if(e.getProduct().getProductID() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(TrendingPC.class).fill(tp);
            return ok(addTrendingPC.render(form, pid, User.getUserById(session().get("email")), "Update PC info"));
        } else {
            flash("error", "PC not found");
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
        }
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
            if(e.getProductId() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Ram.class).fill(tp);
        } else {
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
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
            if(e.getProductId() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Storage.class).fill(tp);
        } else {
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
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
            if(e.getProductId() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Processor.class).fill(tp);
        } else {
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
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
            if(e.getProductId() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(GraphicsCard.class).fill(tp);
        } else {
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
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
            if(e.getProductId() == pid){
                tp = e;
            }
        }
        if(tp != null){
            form = formFactory.form(Motherboard.class).fill(tp);
        } else {
            return badRequest(productList.render(getSpecs(Long.valueOf(0), ""), Category.findAll(), User.getUserById(session().get("email")), env));
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
                if(p.getProductID() == low.getProductID()){
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
