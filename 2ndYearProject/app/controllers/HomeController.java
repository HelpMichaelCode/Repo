package controllers;

import play.mvc.*;

import views.html.*;

import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.*;
import models.users.*;

public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render(User.getUserById(session().get("email"))));
    }

    // public Result userList(){
    //     List<User> userList = null;
    //     userList = User.findAll();

    //     return ok(userList.render(User.getUserById(session().get("email"))));
    // }
    // public Result updateUser(){

    // }
    
}
