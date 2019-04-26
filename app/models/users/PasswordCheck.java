package models.users;

import java.security.*;
import java.io.*;

public class PasswordCheck extends User {
    private String password2;
    private String password3;

    public PasswordCheck(){

    }

    public PasswordCheck(String email, String username, String realName, String password1,
     String address, String mobileNumber, String role, String password2, String password3) {
        super(email, username, realName, password1, address, mobileNumber, role);
        this.password2 = password2;
        this.password3 = password3;
    }

    public PasswordCheck(User u){
        super(u.getEmail(), u.getUsername(), u.getRealName(), u.getPassword(), u.getAddress(),
        u.getMobileNumber(), u.getRole());
    }

    public String getPassword2(){
        return password2;
    }

    public void setPassword2(String password2){ //to compare
        this.password2 = hash(password2);
    }

    public void setPassword2Plain(String password2){ //to copy value
        this.password2 = password2;
    }

    public String getPassword3(){
        return password3;
    }

    public void setPassword3(String password3){ //to compare
        this.password3 = hash(password3);
    }

    public void setPassword3Plain(String password3){ //to copy value
        this.password3 = password3;
    }
}