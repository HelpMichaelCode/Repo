package controllers;

import play.mvc.*;
import play.data.*;
import javax.inject.Inject;

import views.html.*;
import play.db.ebean.Transactional;
import play.api.Environment;

import java.util.List;

// Import models
import models.*;
import models.users.*;
import models.shopping.*;
import java.util.Calendar;
import models.products.*;

@Security.Authenticated(Secured.class)
// Authorise user (check if user is a customer)
@With(CheckIfUser.class)

public class ShoppingController extends Controller {

    private FormFactory formFactory;
    private Environment env;

    @Inject
    public ShoppingController(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }

    @With(CheckIfUser.class)
    @Transactional
    public Result addToCart(Long id, String page) {
        
        // Find the item on sale
        Product product = Product.find.byId(id);
        
        // Get cart for logged in user
        User user = User.getUserById(session().get("email"));
        
        // Check if item in cart
        if (user.getShoppingCart() == null) {
            // If no cart, create one -- Users should have their carts created whenever they register
            user.setShoppingCart(new ShoppingCart());
            user.getShoppingCart().setUser(user);
            user.update();
        }
        // Add product to the cart and save
        if(user.getShoppingCart().addProductToCart(product)){
            user.update();
            // notify user that item was added to their cart
            flash("success", "Product " + product.getProductName() + " was added to cart.");
        } else {
            flash("error", product.getProductName() + " is out of stock.");
        }
        
        
        String[] category = page.split(" ", 2);
        if(category.length > 1){
            if(category[0].equalsIgnoreCase("category")){
                Long catId = Long.parseLong(category[1]);
                return redirect(routes.ProductController.productList(catId, ""));
            }
        } else if(page.equals("home")){
            return redirect(controllers.routes.HomeController.index());
        }
        return redirect(routes.ProductController.productList(0, ""));
    }

    @Transactional
    public Result placeOrder() {
        User u = User.getUserById(session().get("email"));
        
        // Create an order instance
        ShopOrder order = new ShopOrder();
        
        List<OrderLine> orderLines = u.getShoppingCart().getCartItems();
        boolean flag = false;
        for(OrderLine i: orderLines){
            
            if(i.getProduct().getProductQty() < i.getQuantity()){
                i.setQuantity(i.getProduct().getProductQty());
                i.update();
                flag = true;
                // i.getProduct().update();
            }
        }
        if(flag){
            flash("error", "Sorry, we don't have that many of those. We have set the quantity to the amount we have in stock."); 
            // executes if item quantity is updated on line 81
            return ok(basket.render(User.getUserById(session().get("email"))));
        }
        // Associate order with customer
        order.setUser(u);
        
        // Copy cart to order
        order.setProducts(u.getShoppingCart().getCartItems());
        
        // Save the order now to generate a new id for this order
        order.save();
       
       // Move items from cart to order
        for (OrderLine orderLine: order.getProducts()) {
            // Associate with order
            orderLine.setOrder(order);
            // Remove from cart
            orderLine.getProduct().purchase(orderLine.getQuantity());
            orderLine.getProduct().update();
            orderLine.setCart(null);
            // update item
            orderLine.update();
        }
        
        // Update the order
        order.update();
        
        // Clear and update the shopping basket
        u.getShoppingCart().setCartItems(null);
        u.getShoppingCart().update();
        
        // Show order confirmed view
        u.sendMailOrder(order);
        flash("success", "You order has been confirmed!");
        return redirect(routes.ProductController.productList(0, ""));
    }

    @Transactional
    public Result showCart() {
        return ok(basket.render(User.getUserById(session().get("email"))));
    }

    // Add an item to the basket
    @Transactional
    public Result addOne(Long orderLineId) {
        
        // Get the order line
        OrderLine orderLine = OrderLine.find.byId(orderLineId);
        Product p = Product.getProductById(orderLine.getProduct().getProductID());
        
        if(p.getProductQty() <= orderLine.getQuantity()){
            orderLine.setQuantity(p.getProductQty());
            orderLine.update();
            flash("error", "Sorry, we don't have that many of those. We have set the quantity to the amount we have.");
            
        } else if(p.getProductQty() > 0){
            // Increment quantity
            orderLine.increaseQty();

            // Update table
            orderLine.update();
            // p.decrementStock();
            // p.update();
        } else {
            flash("error","It seems we do not have any more of those in stock.");
        } 
        // Show updated basket
        return redirect(routes.ShoppingController.showCart());
    }

    @Security.Authenticated(Secured.class)
    @With(CheckIfUser.class)
    @Transactional
    public Result removeOne(Long orderLineId) {
        
        // Get the order item
        OrderLine orderLine = OrderLine.find.byId(orderLineId);
        Product p = Product.getProductById(orderLine.getProduct().getProductID());
        // Get user
        User u = User.getUserById(session().get("email"));
        
        // Call basket remove item method
        u.getShoppingCart().removeItem(orderLine);
        u.getShoppingCart().update();
        // p.incrementStock();
        // p.update();
        return redirect(routes.ShoppingController.showCart());
    }

    public Result deleteFromCart(Long orderLineId){
        OrderLine orderLine = OrderLine.find.byId(orderLineId);
        if(orderLine != null){
            User.getUserById(session().get("email")).getShoppingCart().deleteItem(orderLine);
        }
        return redirect(routes.ShoppingController.showCart());
    }

    @Transactional
    public Result emptyCart(){
        User u = User.getUserById(session().get("email"));
        u.getShoppingCart().removeAllProducts();
        u.getShoppingCart().update();
        
        return ok(basket.render(u));
    }

    @Transactional
    public Result viewOrders() { 
               
        return ok(viewOrders.render(User.getUserById(session().get("email"))));
    }

    @Transactional
    public Result cancelOrder(Long orderId){
        ShopOrder order = ShopOrder.find.byId(orderId);
        if(order != null){
            if(User.getUserById(session().get("email")).getEmail().equals(order.getUser().getEmail())){
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                
                c1=order.getOrderDate();
                if(compareDates(c1,c2)){
                // order.removeAllItems(orderId);
                order.adjustStock();
                order.delete();
                
                    flash("success", "Your order has been cancelled");
                }else {
                    flash("error", "Sorry, it is too late to cancel this order");
                }
                return ok(viewOrders.render(User.getUserById(session().get("email"))));
            }
            flash("error", "You do not have permissions to cancel that order!");
            return redirect(controllers.routes.HomeController.index());   
        } else {
            flash("error", "Order does not exist!");
            return redirect(controllers.routes.HomeController.index());
        }
    }

    public boolean compareDates(Calendar c1, Calendar c2){
        boolean allowed = true;
        long miliSecondForDate1 = c1.getTimeInMillis();
        long miliSecondForDate2 = c2.getTimeInMillis();
        // Calculate the difference in millisecond between two dates
        long diffInMilis = miliSecondForDate2 - miliSecondForDate1;

        long diffInMinutes = diffInMilis / (60 * 1000);
        if(diffInMinutes > 5){ //for demostration purposes cancel order is only available in the first 5 minutes after placing it
            allowed=false;
        }
        return allowed;
    }

    public static boolean orderedLessThan30DaysAgo(Calendar c1, Calendar c2){
        boolean result = true;
        long miliSecondForDate1 = c1.getTimeInMillis();
        long miliSecondForDate2 = c2.getTimeInMillis();
        // Calculate the difference in millisecond between two dates
        long diffInMilis = miliSecondForDate2 - miliSecondForDate1;

        long diffInMinutes = diffInMilis / (60 * 1000);
        if(diffInMinutes>43200){
            result=false;
        }
        return result;
    }
    
    @Security.Authenticated(Secured.class)
    public Result addBuild(){
        Form<TrendingPC> productForm = formFactory.form(TrendingPC.class);
        
        return ok(buildPC.render(productForm, User.getUserById(session().get("email")), "Build Your PC!"));
    }

    @Security.Authenticated(Secured.class)
    public Result addBuildSubmit(){
        Form<TrendingPC> newForm = formFactory.form(TrendingPC.class).bindFromRequest();
        User user = User.getUserById(session().get("email"));
        if(newForm.hasErrors()){
            flash("error", "Fill in all fields!");
            return badRequest(buildPC.render(newForm, User.getUserById(session().get("email")), "Build Your PC!"));
        } else {
            TrendingPC pc = newForm.get();
            if(pc.getGpu().getProductId() != null){
                if(GraphicsCard.findAll() != null){
                    for(GraphicsCard g: GraphicsCard.findAll()){
                        if(pc.getGpu().getProductId().equals(g.getProductId())){
                            user.getShoppingCart().addProductToCart(g.getProduct());
                            // flash("error", "GPU added!");
                        }
                    }
                } 
            }
            if(pc.getMotherboard().getProductId() != null){
                if(Motherboard.findAll() != null){
                    for(Motherboard g: Motherboard.findAll()){
                        if(pc.getMotherboard().getProductId().equals(g.getProductId())){
                            user.getShoppingCart().addProductToCart(g.getProduct());
                        }
                    }
                }
            }
            if(pc.getCpu().getProductId() != null){
                if(Processor.findAll() != null){ 
                    for(Processor g: Processor.findAll()){
                        if(pc.getCpu().getProductId().equals(g.getProductId())){
                            user.getShoppingCart().addProductToCart(g.getProduct());
                        } 
                    }
                }
            }
            if(pc.getRam().getProductId() != null){
                if(Ram.findAll() != null){  
                    for(Ram g: Ram.findAll()){
                        if(pc.getRam().getProductId().equals(g.getProductId())){
                            user.getShoppingCart().addProductToCart(g.getProduct());
                        }  
                    }
                }
            }
            if(pc.getStorage().getProductId() != null){
                if(Storage.findAll() != null){
                    for(Storage g: Storage.findAll()){
                        if(pc.getStorage().getProductId().equals(g.getProductId())){
                            user.getShoppingCart().addProductToCart(g.getProduct());
                        }  
                    }
                }
            } 
            user.update();
        flash("success", "Components added to cart");
        return redirect(controllers.routes.ShoppingController.showCart());
        }
    }

    
}