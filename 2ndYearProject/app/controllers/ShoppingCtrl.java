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

public class ShoppingCtrl extends Controller {

    private FormFactory formFactory;
    private Environment env;

    @Inject
    public ShoppingCtrl(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }
    // View an individual order
    @Transactional
    public Result viewOrder(long id) {
        ShopOrder order = ShopOrder.find.byId(id);
        return ok(orderconfirmed.render((User)User.getUserById(session().get("email")), order));
    }

    @Transactional
    public Result addToBasket(Long id) {
        
        // Find the item on sale
        Product product = Product.find.byId(id);
        
        // Get basket for logged in user
        User user = (User)User.getUserById(session().get("email"));
        
        // Check if item in basket
        if (user.getBasket() == null) {
            // If no basket, create one
            user.setBasket(new Basket());
            user.getBasket().setUser(user);
            user.update();
        }
        // Add product to the basket and save
        user.getBasket().addProductOnSale(product);
        user.update();
        
        // Show the basket contents     
        return ok(basket.render(user));
    }

    @Transactional
    public Result placeOrder() {
        User u = (User)User.getUserById(session().get("email"));
        
        // Create an order instance
        ShopOrder order = new ShopOrder();
        
        // Associate order with customer
        order.setUser(u);
        
        // Copy basket to order
        order.setProducts(u.getBasket().getBasketItems());
        
        // Save the order now to generate a new id for this order
        order.save();
       
       // Move items from basket to order
        for (OrderItem i: order.getProducts()) {
            // Associate with order
            i.setOrder(order);
            // Remove from basket
            i.setBasket(null);
            // update item
            i.update();
        }
        
        // Update the order
        order.update();
        
        // Clear and update the shopping basket
        u.getBasket().setBasketItems(null);
        u.getBasket().update();
        
        // Show order confirmed view
        return ok(orderconfirmed.render(u, order));
    }

    @Transactional
    public Result showBasket() {
        return ok(basket.render((User)User.getUserById(session().get("email"))));
    }

    // Add an item to the basket
    @Transactional
    public Result addOne(Long productId) {
        
        // Get the order item
        OrderItem product = OrderItem.find.byId(productId);
        // Increment quantity
        product.increaseQty();
        // Save
        product.update();
        // Show updated basket
        return redirect(routes.ShoppingCtrl.showBasket());
    }

    @Transactional
    public Result emptyBasket(){
        User u = (User)User.getUserById(session().get("email"));
        u.getBasket().removeAllProducts();
        u.getBasket().update();
        
        return ok(basket.render(u));
    }

    @Transactional
    public Result removeOne(Long productId) {
        
        // Get the order item
        OrderItem product = OrderItem.find.byId(productId);
        // Get user
        User u = (User)User.getUserById(session().get("email"));
        // Call basket remove item method
        u.getBasket().removeItem(product);
        u.getBasket().update();
        // back to basket
        return ok(basket.render(u));
    }

  
    
}