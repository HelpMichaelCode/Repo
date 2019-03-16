package controllers;

import play.mvc.*;

import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.users.*;

import views.html.*;

public class LoginController extends Controller{
    private FormFactory formFactory;
    @Inject
    public LoginController(FormFactory formFactory){
        this.formFactory = formFactory;
    }

    public Result login() {
        Form<Login> loginForm = formFactory.form(Login.class);
        return ok(login.render(loginForm, User.getUserById(session().get("email"))));
       }

    public Result loginSubmit() { //THE PASSWORD IS NOT BEING ENCRYPTED BEFORE IT IS SENT THROUGH THE FORM
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

        return redirect(routes.HomeController.index());
    }

    public Result register() {
        Form<User> userForm = formFactory.form(User.class);
        
        return ok(register.render(userForm, User.getUserById(session().get("email"))));
    }

    public Result registerSubmit() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if(userForm.hasErrors()) {
            flash("error", "Please fill in all the fields!");
            //this message is sent only if the user has not filled in all the fields in the registration form
            return badRequest(register.render(userForm, User.getUserById(session().get("email"))));
        } else {
            User newUser = userForm.get();
            if(User.getUserById(newUser.getEmail()) == null) {
                if(newUser.emailCheck()){ //user is registered only if email is in the right format
                    newUser.save(); //Add user to DB if email is in the right format and is not already in use.
                    flash("success", "Thank you for registering!");
                    return redirect(controllers.routes.LoginController.login());
                } else {
                    flash("error", "Wrong email format! Please try again!");
                    return badRequest(register.render(userForm, User.getUserById(session().get("email")))); //bad format
                }
            } else {
                flash("error", "Email already in use! Please try again!");
                //if the email the user has entered is already in the database
                return badRequest(register.render(userForm, User.getUserById(session().get("email"))));
            }
        }
    }
       
}

