package models.users;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class User extends Model{
    @Id 
    private String email;

    @Constraints.Required
    private String role; //Administrator or regular user

    @Constraints.Required
    private String name;

    @Constraints.Required
    private String password;

    public User(){
    }

    public User(String email, String role, String username, String password){
        this.email = email;
        this.role = role;
        this.name = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole(){
        return this.role;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getUsername(){
        return this.getUsername();
    }
    public void setUsername(String username){
        this.name = username;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    private static Finder<Long, User> find = new Finder<>(User.class);

    public static final List<User> findAll() {
        return User.find.all();
     }

     public static User authenticate(String email, String password) {
        return find.query().where().eq("email", email).eq("password", password).findUnique();
     }
}