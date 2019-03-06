package models.users;

import java.security.*;
import java.io.*;

public class Login{
    private String email;
    private String password;

    public String validate() {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = password.getBytes("UTF-8");
            b = md.digest(b);
            String passwordCheckSum = new String(b);
            password = passwordCheckSum;
            }
            catch (NoSuchAlgorithmException e) {
                //unfortunate sequence of events eh?
            } catch (UnsupportedEncodingException e) {
                //unfortunate sequence of events eh?
            }
        if(User.authenticate(email, password) == null){
            return "Invalid user / password";
        }
        return null;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
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
        this.password = password;
    }

}
