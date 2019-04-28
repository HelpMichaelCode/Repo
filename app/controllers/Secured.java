package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;


import models.*;
////////////////////////////// See step 8 from Session Management notes //////////////////////////////////////
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(controllers.routes.LoginController.login());
    }
}