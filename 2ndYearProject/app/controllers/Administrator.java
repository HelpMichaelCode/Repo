package controllers;

import play.mvc.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

import models.users.*;
////////////////////////////// See step 10 from Session Management notes //////////////////////////////////////
public class Administrator extends Action.Simple {

    public CompletionStage<Result> call(Http.Context context) {

        String id = context.session().get("email");
        if(id != null) {
            User user = User.getUserById(id);
            if("admin".equals(user.getRole())) {
                return delegate.call(context); //this is returned if user is an Administrator
            } //end of if statement
        } //end of if statement
        context.flash().put("error", "Administrator login required");

        return CompletableFuture.completedFuture(redirect(controllers.routes.LoginController.login()));
    }

}