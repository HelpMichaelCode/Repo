package models;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;
import models.users.*;

@Entity
public class Review extends Model {
    
    @Id
    private Long id;

    // @Constraints.Required
    private String body;

    @Constraints.Required
    private double rating;

    @ManyToOne
    private User user; //username of the user who posted the review

    @ManyToOne
    private Product product;

    public Review(){
    }

    public Review(Long id, String body, double rating){
        this.id = id;
        this.body = body;
        this.rating = rating;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public double getRating(){
        return rating;
    }

    public void setRating(double rating){
        this.rating = rating;
    }

    public Product getProduct(){
        return product;
    }

    public void setProduct(Product p){
        this.product = p;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User u){
        this.user = u;
    }
    
    public static Finder<Long, Review> find = new Finder<>(Review.class);

    public static final List<Review> findAll() {
        return Review.find.all();
    }

    public  Review getReviewbyId(Long id){
        if(id == null){
            return null;
        } else {
            return find.query().where().eq("id", id).findUnique();
        }
    }
}