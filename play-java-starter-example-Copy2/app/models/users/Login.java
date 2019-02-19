package models.users;

public class Login{
    private String email;
    private String password;

    public String validate() {
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
        this.password = password;
    }

}
