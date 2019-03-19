package models.users;

import models.shopping.*;
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
    private Basket basket;

@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<ShopOrder> orders;



    public User(){
    }

    public User(String email, String username, String password, String address, String mobileNumber, String role){ //insert new fields
        this.email = email;
        this.role = role;
        this.username = username;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = password.getBytes("UTF-8");
            b = md.digest(b);
            String passwordCheckSum = new String(b);
            this.password = passwordCheckSum;
            }
            catch (NoSuchAlgorithmException e) {
                //unfortunate sequence of events eh?
            } catch (UnsupportedEncodingException e) {
                //unfortunate sequence of events eh?
            }
        this.address = address;
        this.mobileNumber = mobileNumber;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
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
        try{
            MessageDigest md = MessageDigest.getInstance("MD5"); // gets instance of Message Digest-gets you a hash code (MD5, SHA-1)
            byte[] b = password.getBytes(); //turns the passed into the method string into a byte array
            b = md.digest(b); //gets the checksum of the byte array/encrytpts
            String passwordCheckSum = new String(b); //encrypted byte array is passed into a string object
            this.password = passwordCheckSum; //assign the attribute the encrypted string
            }
            catch (NoSuchAlgorithmException e) {
                //unfortunate sequence of events eh?
            // } catch (UnsupportedEncodingException e) {
            //     //unfortunate sequence of events eh?
            }
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

    public boolean emailCheck(){
        boolean result = false;
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        if(matcher.find()){
            result = true;
        }
        return result;
    }
}