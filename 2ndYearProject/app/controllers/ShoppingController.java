package controllers;

import play.mvc.*;
import play.data.*;
import javax.inject.Inject;

import views.html.*;
import play.db.ebean.Transactional;
import play.api.Environment;

// Import models
import models.*;
import models.users.*;
import models.shopping.*;

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
    // View an individual order
    @Transactional
    public Result viewOrder() {
        ShopOrder order = ShopOrder.find.byId(id);
        return ok(orderconfirmed.render((User)User.getUserById(session().get("email")), order));
    }

    @Transactional
    public Result addToCart(Long id) {
        
        // Find the item on sale
        Product product = Product.find.byId(id);
        
        // Get cart for logged in user
        User user = User.getUserById(session().get("email"));
        
        // Check if item in cart
        if (user.getShoppingCart() == null) {
            // If no cart, create one
            user.setShoppingCart(new ShoppingCart());
            user.getShoppingCart().setUser(user);
            user.update();
        }
        // Add product to the cart and save
        user.getShoppingCart().addProductToCart(product);
        user.update();

        //update stock
        product.decrementStock();
        product.update();
        
        // Show the cart contents     
        return ok(basket.render(user));
    }

    @Transactional
    public Result placeOrder() {
        User u = User.getUserById(session().get("email"));
        
        // Create an order instance
        ShopOrder order = new ShopOrder();
        
        // Associate order with customer
        order.setUser(u);
        
        // Copy cart to order
        order.setProducts(u.getShoppingCart().getCartItems());
        
        // Save the order now to generate a new id for this order
        order.save();
       
       // Move items from cart to order
        for (OrderLine i: order.getProducts()) {
            // Associate with order
            i.setOrder(order);
            // Remove from cart
            i.setCart(null);
            // update item
            i.update();
        }
        
        // Update the order
        order.update();
        
        // Clear and update the shopping basket
        u.getShoppingCart().setCartItems(null);
        u.getShoppingCart().update();
        
        // Show order confirmed view
        return ok(orderconfirmed.render(u, order));
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
        Product p = Product.find.byId(orderLine.getProduct().getProductID());
        
        if(p.getProductQty() > 0){

            // Increment quantity
            orderLine.increaseQty();

            // Update table
            orderLine.update();
            p.decrementStock();
            p.update();
        } else {
            flash("error","Oops, it seems we do not have any more of those in stock.");
        } 
        // Show updated basket
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
    public Result removeOne(Long orderLineId) {
        
        // Get the order item
        OrderLine orderLine = OrderLine.find.byId(orderLineId);
        Product ios = Product.find.byId(orderLine.getProduct().getProductID());
        // Get user
        User u = User.getUserById(session().get("email"));
        
        // Call basket remove item method
        u.getShoppingCart().removeItem(orderLine);
        u.getShoppingCart().update();
        // back to basket
        return ok(basket.render(u));
    }

  
    
}