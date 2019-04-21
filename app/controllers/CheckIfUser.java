package controllers;

import play.mvc.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

import models.users.*;

public class CheckIfUser extends Action.Simple {

    public CompletionStage<Result> call(Http.Context context){

        String id = context.session().get("email");
        if(id != null) {
            User user = User.getUserById(id);
            if("customer".equals(user.getRole())) {
                return delegate.call(context); //this is returned if user is an Administrator
            } //end of if statement
        } //end of if statement
        context.flash().put("error", "Login required");

        return CompletableFuture.completedFuture(redirect(controllers.routes.LoginController.login()));
    }
    
    }
