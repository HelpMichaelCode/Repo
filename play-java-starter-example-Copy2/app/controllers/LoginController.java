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

    public Result loginSubmit() {
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm, User.getUserById(session().get("email"))));
        } else {
            session().clear();
            session("email", loginForm.get().getEmail());

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
            return badRequest(register.render(userForm, User.getUserById(session().get("email"))));
        } else {
            User newUser = userForm.get();
            if(User.getUserById(newUser.getEmail()) == null) {
                newUser.save(); //Add user to DB if email is not already in use.
                flash("success", "Thank you for registering!");
                return redirect(controllers.routes.LoginController.login());
            } else {
                return badRequest(register.render(userForm, User.getUserById(session().get("email"))));
            }
        }
    }
       
}

