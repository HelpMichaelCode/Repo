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
        return ok(login.render(loginForm));
       }

    public Result loginSubmit(){
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()){
            return badRequest(login.render(loginForm));
        } else {
            session().clear();

            session("email", loginForm.get().getEmail());

            return redirect(controllers.routes.HomeController.index());
        }
    }
       
}

