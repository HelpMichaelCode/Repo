package controllers;

import play.mvc.*;

import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.*;
import models.shopping.*;
import models.users.*;

import views.html.*;

public class LoginController extends Controller{
    private FormFactory formFactory;
    private Environment env;
    
    @Inject
    public LoginController(FormFactory formFactory, Environment env){
        this.formFactory = formFactory;
        this.env = env;
    }

    @Transactional
    public Result login() {
        // if(session().)
        Form<Login> loginForm = formFactory.form(Login.class);
        return ok(login.render(loginForm, User.getUserById(session().get("email"))));
       }

    public Result loginSubmit() { //I believe the password is in plain text when the form is sent
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm, User.getUserById(session().get("email"))));
        } else {
            session().clear();
            session("email", loginForm.get().getEmail());
            flash("success", "Login successful! Welcome back!");
            return redirect(controllers.routes.HomeController.index());
        }
    }

    public Result logout() {
        session().clear();
        flash("success","You have been logged out.");

        return redirect(routes.LoginController.login());
    }

    public Result register() {
        Form<PasswordCheck> userForm = formFactory.form(PasswordCheck.class);
        
        return ok(register.render(userForm, User.getUserById(session().get("email")), "Register"));
    }

    public Result registerSubmit() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        Form<PasswordCheck> passwordForm = formFactory.form(PasswordCheck.class).bindFromRequest();

        if(userForm.hasErrors()) {
            flash("error", "Please fill in all the fields!");
            //this message is sent only if the user has not filled in all the fields in the registration form

            return badRequest(register.render(passwordForm, User.getUserById(session().get("email")), "Register"));

        } else {
            User newUser = userForm.get();
            PasswordCheck pc = passwordForm.get();
            
            if(!(pc.getPassword2().equals(newUser.getPassword()))){
                flash("error", "Passwords do not match.");

                return badRequest(register.render(passwordForm, User.getUserById(session().get("email")), "Register"));
            }
            if(User.getUserById(newUser.getEmail()) == null) {
                if(newUser.emailCheck()){ //user is registered only if email is in the right format
                    newUser.setShoppingCart(new ShoppingCart());
                    newUser.getShoppingCart().setUser(newUser);
                    newUser.save(); //Add user to DB if email is in the right format and is not already in use.
                    flash("success", "Thank you for registering!");

                    return redirect(controllers.routes.LoginController.login());
                } else {
                    flash("error", "Wrong email format! Please try again!");

                    return badRequest(register.render(passwordForm, User.getUserById(session().get("email")), "Register")); //bad format
                }
            } else {
                if(newUser.getUsername().equals(User.getUserById(newUser.getEmail()).getUsername())){ //if email and username are already in the DB for one user, then update
                    newUser.update();
                    flash("success", "User " + newUser.getUsername() + " was updated.");
                    return redirect(controllers.routes.LoginController.userList());
                } else {
                    flash("error", "Email already in use! Please try again!");
                    //if the email the user has entered is already in the database
                    return badRequest(register.render(passwordForm, User.getUserById(session().get("email")), "Register"));
                }
            }
        }
    } //end of registerSubmit

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result userList(){
        List<User> users = User.findAll();
        if(Product.getLowQty().size() > 0){
            String lowQtyStr = "Restock needed! Check product list!";
            flash("warning", lowQtyStr);
        }
        if(users.size()>0){
            return ok(userList.render(users,  User.getUserById(session().get("email"))));
        } else {
            flash("error", "No users found.");
            return badRequest(index.render(Product.findAll(), User.getUserById(session().get("email")), env));
        }
    }

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result updateUser(String email){
        User temp = null;
        PasswordCheck user;
        Form<PasswordCheck> userForm;

        if(Product.getLowQty().size() > 0){
            String lowQtyStr = "Restock needed! Check product list!";
            flash("warning", lowQtyStr);
        }

        try {
            temp = User.getUserById(email);
            user = new PasswordCheck(temp);

            userForm = formFactory.form(PasswordCheck.class).fill(user);
        } catch (Exception ex) {
            flash("error", "User not found.");
            return badRequest(userList.render(User.findAll(),  User.getUserById(session().get("email"))));
        }
        return ok(register.render(userForm, User.getUserById(session().get("email")), "Update user " + user.getUsername()));
    }
    

    @Security.Authenticated(Secured.class)
    @With(Administrator.class)
    public Result deleteUser(String email){
        User u = User.getUserById(email);
        for(ShopOrder s: u.getOrders()){
            s.delete(); //first deletes all records of previous orders
        }
        for(OrderLine e: u.getShoppingCart().getCartItems()){
            e.delete(); //then deletes all current order lines
        }
        u.getShoppingCart().delete(); //deletes user's shopping cart
        u.delete(); //deletes the user

        return redirect(controllers.routes.LoginController.userList());
    }
}      