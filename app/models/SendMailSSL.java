package models;

import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;
import models.users.User;

import play.mvc.*;
import play.data.*;
import javax.inject.Inject;

import views.html.*;
import play.db.ebean.Transactional;
import play.api.Environment;
 
public class SendMailSSL{    
 public static void main(String[] args) {    
     //from,password,to,subject,message    
     //User user = User.getUserById("email");

    // Mailer.send("santoniom338@gmail.com","Michaelsantos1234", user.getEmail(),"Welcome!","Hello new user! Welcome to BLDPC. We hope you enjoy surfing through our website and find what's best for you!");  
     //change from, password and to  
 }
 public static void send(String from,String password,String to,String sub,String msg){  

    //Get properties object    
    Properties props = new Properties();    
    props.put("mail.smtp.host", "smtp.gmail.com");    
    props.put("mail.smtp.socketFactory.port", "465");    
    props.put("mail.smtp.socketFactory.class",    
              "javax.net.ssl.SSLSocketFactory");    
    props.put("mail.smtp.auth", "true");    
    props.put("mail.smtp.port", "465");   

    //get Session   
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {    
     protected PasswordAuthentication getPasswordAuthentication() {    
     return new PasswordAuthentication(from,password);  
     }    
    });    

    //compose message    
    try {    
     MimeMessage message = new MimeMessage(session);    
     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
     message.setSubject(sub);    
     message.setText(msg);    

     //send message  
     Transport.send(message);    
     System.out.println("message sent successfully");    
    } catch (MessagingException e) {throw new RuntimeException(e);}    
       
}     
}    