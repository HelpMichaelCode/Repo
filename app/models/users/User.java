package models.users;

import models.shopping.*;
import models.products.*;
import models.SendMailSSL;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import java.io.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.security.*;

@Entity
public class User extends Model{
    @Id 
    @Constraints.Required
    private String email;

    // @Constraints.Required
    private String role; //Administrator or regular user

    @Constraints.Required
    private String username;

    @Constraints.Required
    private String password;

    @Constraints.Required//new
    private String address;

    @Constraints.Required//new
    private String mobileNumber;

    @OneToOne(mappedBy="user", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<ShopOrder> orders;

    private SendMailSSL sendEmail;

    private User user;
    
    public User(){
    }

    public User(String email, String username, String password, String address, String mobileNumber, String role){ //insert new fields
        this.email = email;
        this.role = role;
        this.username = username;
        this.password = hash(password);
        this.address = address;
        this.mobileNumber = mobileNumber;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    
    public List<ShopOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ShopOrder> orders) {
        this.orders = orders;
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
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = hash(password);
    }

    public void setPasswordPlain(String password){
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    private static Finder<Long, User> find = new Finder<>(User.class);

    public static final List<User> findAll() {
        return User.find.all();
     }

    public static User authenticate(String email, String password) {
        return find.query().where().eq("email", email).eq("password", password).findUnique();
    }

    public static User getUserById(String id){ 
         if(id == null){
            return null;
         } else {
            return find.query().where().eq("email", id).findUnique();
        }
    }

    public static String hash(String password){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = password.getBytes("UTF-8");
            b = md.digest(b);
            String passwordCheckSum = new String(b);
            return passwordCheckSum; //returns the hash signature of the entered password if successful
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return " "; //should never get to this statement
    }

    public boolean emailCheck(){
        boolean result = false;
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        if(matcher.find()){
            result = true;
        }
        return result;
    }

    public void SendMailSSL(){
        //from, Password, To, Subject, Message
        sendEmail.send("bldpcproject@gmail.com","2nd_year_project", this.email ,"Welcome!","Hello "+ this.username +"! Welcome to BLDPC. We hope you enjoy surfing through our website and find what's best for you! \n Click the link down below and enter in your credentials \nhttp://localhost:9000/login");
    }

    public boolean numberCheck(){
        boolean result = false;
        try{
            double number = Double.parseDouble(mobileNumber);
            result = true;
        } catch(NumberFormatException e){
            result = false;
        }
        return result;
    }

    public boolean checkLengthOfStrings(){
        if(ProductSkeleton.checkStringLen(email) ||ProductSkeleton.checkStringLen(role) || 
        ProductSkeleton.checkStringLen(username) || ProductSkeleton.checkStringLen(mobileNumber) || 
        ProductSkeleton.checkStringLen(address) || ProductSkeleton.checkStringLen(password)){
            return true;
        }
        return false;
    }
}