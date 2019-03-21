package models;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Review extends Model {
    
    @Id
    private Long id;

    @Constraints.Required
    private String body;

    @Constraints.Required
    private double rating;

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
    
    
}