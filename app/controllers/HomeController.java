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
import models.shopping.*;

public class HomeController extends Controller {
    private FormFactory formFactory;
    private Environment env;

    @Inject
    public HomeController(FormFactory formFactory, Environment env){
        this.formFactory = formFactory;
        this.env = env;
    }

    public Result index() {
        ProductController.flashLowStock();

        List<ProductSkeleton> bestSellers = new ArrayList<>();
        List<ProductSkeleton> recentlyAdded = new ArrayList<>();
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

        Collections.sort(temp, ProductSkeleton.IdComparator);
        for(int i=0; i<4; i++){
            recentlyAdded.add(temp.get(i));
        }
        return ok(index.render(bestSellers, recentlyAdded, User.getUserById(session().get("email")), env));
    }

    public Result contactUs(){
        return ok(contactUs.render(User.getUserById(session().get("email"))));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
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
        String[] prodNames= names.toArray(new String[names.size()]);
        Integer[] sold= sales.toArray(new Integer[sales.size()]);
        String jsonString = getJsonString(prodNames, sold);

        //Get best selling product for the last 30 days
        String[] bestSellerString = getBestSeller30Days().split(",", 2);
        Long maxSold = Long.valueOf(0);
        String bestSeller = "N/A";
        try{
            maxSold = Long.parseLong(bestSellerString[0], 10);
            bestSeller = bestSellerString[1];
        } catch(ArrayIndexOutOfBoundsException e){
        } catch(NumberFormatException e){
        }
        double revenue = totalRevenue();
        return ok(stats.render(jsonString, bestSeller, maxSold, revenue, User.getUserById(session().get("email")), "sales"));
        // return ok(test.render(jsonString, User.getUserById(session().get("email"))));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result salesStatsCategory(String cat){ 
        List<String> names = new ArrayList<>();
        List<Integer> sales = new ArrayList<>();
        List<Product> all = Product.findAll();
        for(Category c: Category.findAll()){
            if(c.getName().toLowerCase().equals(cat.toLowerCase())){
                for(Product p: all){
                    if(p.getCategory().getId() == c.getId()){
                        names.add(p.getProductName());
                        sales.add(p.getTotalSold());
                    }
                }
                String[] prodNames= names.toArray(new String[names.size()]);
                Integer[] sold= sales.toArray(new Integer[sales.size()]);
                String jsonString = getJsonString(prodNames, sold);

                //Get best selling product for the last 30 days 
                // --- we could have made this to get the best selling product of the specified category but there wasn't much time
                String[] bestSellerString = getBestSeller30Days().split(",", 2);
                Long maxSold = Long.parseLong(bestSellerString[0], 10);
                String bestSeller = bestSellerString[1];
                // try{
                //     maxSold = Long.parseLong(bestSellerString[0], 10);
                //     bestSeller = bestSellerString[1];
                // } catch(ArrayIndexOutOfBoundsException e){  
                // } catch(NumberFormatException e){  
                // }
                double revenue = revenueCategory(cat);
                return ok(stats.render(jsonString, bestSeller, maxSold, revenue, User.getUserById(session().get("email")), "sales"));
            }
        }
        return redirect(controllers.routes.HomeController.stats());
    } 

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result inStock(){
        List<String> names = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        for(Category c: Category.findAll()){
            names.add(c.getName());
            int sum = 0;
            for(Product p: Product.findAll()){
                if(p.getCategory().getName().equals(c.getName())){
                    sum += p.getProductQty();
                }
            }
            quantity.add(sum);
        }
        String[] prodNames= names.toArray(new String[names.size()]);
        Integer[] qty= quantity.toArray(new Integer[quantity.size()]);
        String jsonString = getJsonString(prodNames, qty);

        //Get best selling product for the last 30 days
        String[] bestSellerString = getBestSeller30Days().split(",", 2);
        Long maxSold = Long.valueOf(0);
        String bestSeller = "N/A";
        try{
            maxSold = Long.parseLong(bestSellerString[0], 10);
            bestSeller = bestSellerString[1];
        } catch(ArrayIndexOutOfBoundsException e){
        } catch(NumberFormatException e){
        }
        double revenue = totalRevenue();
        return ok(stats.render(jsonString, bestSeller, maxSold, revenue, User.getUserById(session().get("email")), "stock"));
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result inStockCategory(String cat){ 
        List<String> names = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        List<Product> all = Product.findAll();
        for(Category c: Category.findAll()){
            if(c.getName().toLowerCase().equals(cat.toLowerCase())){
                for(Product p: all){
                    if(p.getCategory().getId() == c.getId()){
                        names.add(p.getProductName());
                        quantity.add(p.getProductQty());
                    }
                }
                String[] prodNames= names.toArray(new String[names.size()]);
                Integer[] qty= quantity.toArray(new Integer[quantity.size()]);
                String jsonString = getJsonString(prodNames, qty);

                //Get best selling product for the last 30 days 
                // --- we could have made this to get the best selling product of the specified category but there wasn't much time
                String[] bestSellerString = getBestSeller30Days().split(",", 2);
                Long maxSold = Long.parseLong(bestSellerString[0], 10);
                String bestSeller = bestSellerString[1];
                double revenue = revenueCategory(cat);
                return ok(stats.render(jsonString, bestSeller, maxSold, revenue, User.getUserById(session().get("email")), "stock"));
            }
        }
        return redirect(controllers.routes.HomeController.stats());
    }   
    
    private String getJsonString(String[] names, Integer[] sold){
        // String exampleValues = "{'c':[{'v': 'Work'}, {'v': 11}]}, {'c':[{'v': 'Eat'}, {'v': 2}]}, {'c':[{'v': 'Commute'}, {'v': 2}]},{'c':[{'v': 'Watch TV'}, {'v':2}]}, {'c':[{'v': 'Sleep'}, {'v':7, 'f':'7'}]}";
        String values = getValues(names, sold);
        String jsonString = "{'cols': [{'id': 'name', 'label': 'Names', 'type': 'string'}, {'id': 'sold', 'label': 'Total sold', 'type': 'number'}],'rows': [" + values + "]}";
        return jsonString;
    }

    private String getValues(String[] names, Integer[] sold){
        String start = "{'c':[{'v': '";
        String middle = "'}, {'v': ";
        String end = "}]},";
        String finalElementMid = ", 'f':'";
        String finalElementEnd = "'}]}";
        String values = "";
        if(names.length == sold.length){
            for(int i = 0; i<names.length; i++){
                if(i==0){
                    values += start;
                } else {
                    values += (" " + start);
                }
                values += names[i];
                values += middle;
                values += sold[i];
                if((i+1) == names.length){
                    values += finalElementMid;
                    values += sold[i];
                    values += finalElementEnd;
                } else {
                    values += end;
                }
            }
            return values;
        }
        return "{'c':[{'v': 'Bad values'}, {'v': 0}]}";
    }

    private String getBestSeller30Days(){
        Long max = Long.valueOf(0);
        String prodName = "N/A";
        for(Product p: Product.findAll()){
            Long prodTotal = Long.valueOf(0);
            for(ShopOrder so: ShopOrder.findAll()){
                Calendar orderDate = Calendar.getInstance();
                Calendar currentDate = Calendar.getInstance();
                orderDate = so.getOrderDate();
                if(ShoppingController.orderedLessThan30DaysAgo(orderDate, currentDate)){
                    for(OrderLine ol: so.getProducts()){
                        if(ol.getProduct().getProductID() == p.getProductID()){
                            prodTotal += ol.getQuantity();
                        }
                    }
                }
                if(prodTotal > max){
                    max = prodTotal;
                    prodName = p.getProductName();
                }
            }   
        }
        return max + "," + prodName;
    }

    private double totalRevenue(){
        double sum = 0;
        for(Product p: Product.findAll()){
            sum += (p.getTotalSold() * p.getProductPrice());
        }
        return sum;
    }
    private double revenueCategory(String cat){
        double sum = 0;
        for(Category c: Category.findAll()){ 
            if(c.getName().toLowerCase().equals(
                cat.toLowerCase())){
                for(Product p: Product.findAll()){
                    if(p.getCategory().getId() == c.getId()){
                        sum += (p.getTotalSold() * p.getProductPrice());
                    } 
                }
            }
        }
        return sum;
    }

    // @Security.Authenticated(Secured.class)
    public Result forum(){
        Form<Forum> forumForm = formFactory.form(Forum.class);
        return ok(forum.render(Forum.findAll(), forumForm, User.getUserById(session().get("email"))));
    }

    @Security.Authenticated(Secured.class)
    public Result addThread(String userEmail){
        Form<Forum> form = formFactory.form(Forum.class).bindFromRequest();
        if(form.hasErrors()){
            flash("error", "Please fill in all the fields if you wish to start a new thread!");
            return redirect(controllers.routes.HomeController.forum());
         } else {
                Forum newPost = form.get();
                if (newPost.getId() == null) {
                    if(newPost.checkLengthOfStrings()){
                        flash("error", "Please try using less than 255 characters.");
                        return redirect(controllers.routes.HomeController.forum());
                    }
                    newPost.setUser(User.getUserById(userEmail)); //the email that is passed in the form
                    newPost.save();
                    flash("success", "You have successfully started a new thread");
                } else {
                    flash("error", "There was a problem processing your post, please try again.");
                }
                return redirect(controllers.routes.HomeController.forum());
            }
        }
    

    public Result displayThread(String title){
        Forum post = null;
        Form<Comment> commentForm = formFactory.form(Comment.class);
        for(Forum e: Forum.findAll()){
            if(e.getTitle().equals(title)){
                post = e;
            }
        }
        if(post == null){
            flash("warning", "Thread not found");
            return redirect(controllers.routes.HomeController.forum());
        } else {
            return ok(thread.render(post, commentForm, User.getUserById(session().get("email"))));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result deleteThread(Long threadId){
        Forum thread = Forum.getForumById(threadId);
        User currentUser = User.getUserById(session().get("email"));
        if(!currentUser.getRole().equals("admin")){ //if not admin - secondary check just to be sure
            flash("error", "You do not have the permissions to delete a thread!");
        } else {
            if(thread != null){
                for(Comment e: thread.getComments()){
                    e.delete();
                }
                thread.delete();
                flash("success", "Thread deleted successfully");
            } else {
                flash("error", "Thread not found");
            }
        }
        return redirect(controllers.routes.HomeController.forum());
    }

    @Security.Authenticated(Secured.class)
    public Result deleteComment(String threadTitle, Long commentId){
        Comment comment = Comment.getCommentById(commentId);
        User currentUser = User.getUserById(session().get("email"));
        if(comment != null){
            if(!currentUser.getRole().equals("admin") && !currentUser.getEmail().equals(comment.getUser().getEmail())){ //if not admin or user who posted the comment
                flash("error", "You do not have the permissions to delete this comment!");
            } else {
                comment.delete();
                flash("success", "Comment deleted successfully");
            }
        } else {
            flash("error", "Comment not found");
        }
        return redirect(controllers.routes.HomeController.displayThread(threadTitle));
    }

    @Security.Authenticated(Secured.class)
    public Result postComment(String userEmail, Long postId){
        Form<Comment> form = formFactory.form(Comment.class).bindFromRequest();
        if(form.hasErrors()){
            flash("error", "Please fill in all the fields if you wish to post a comment!");
            return redirect(controllers.routes.HomeController.displayThread(Forum.getForumById(postId).getTitle()));
        } else {
            Comment newComment = form.get();
            if (newComment.getId() == null) {
                if(newComment.checkLengthOfStrings()){
                    flash("error", "Please do no use more than 255 characters");
                    return redirect(controllers.routes.HomeController.displayThread(Forum.getForumById(postId).getTitle()));
                }
                newComment.setUser(User.getUserById(userEmail)); //the email that is passed in the form
                newComment.setForum(Forum.getForumById(postId));
                newComment.save();
                flash("success", "Your reply has been posted");
            } else {
                flash("error", "There was a problem processing your comment, please try again.");
            }
            return redirect(controllers.routes.HomeController.displayThread(Forum.getForumById(postId).getTitle()));
        }
    }
}