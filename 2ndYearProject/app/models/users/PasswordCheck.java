package models.users;

import java.security.*;
import java.io.*;

public class PasswordCheck extends User {
    private String password2;


    public PasswordCheck(){

    }

    public PasswordCheck(String email, String username, String password1,
     String address, String mobileNumber, String role, String password2) {
        super(email, username, password1, address, mobileNumber, role);
        this.password2 = hash(password2);
    }

    public PasswordCheck(User u){
        super(u.getEmail(), u.getUsername(), u.getPassword(), u.getAddress(),
        u.getMobileNumber(), u.getRole());
    }

    public String getPassword2(){
        return password2;
    }

    public void setPassword2(String password2){
        this.password2 = hash(password2);
    }

}