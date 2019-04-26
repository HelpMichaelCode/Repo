package models;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;
import models.users.*;

@Entity
public class Forum extends Model {

    @Id
    private Long id;
    @ManyToOne
    private User user; //user who posted the topic
    @Constraints.Required
    private String title;
    @OneToMany
    private List<Comment> comments;
    @Constraints.Required
    private String body;

    public Forum(){
    }

    public Forum(Long id, String body, String title, User user, List<Comment> comments){
        this.id = id;
        this.body = body;
        this.title = title;
        this.user = user;
        this.comments = comments;
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

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public User getUser(){
        return user;
    }

    public void setUser(User u){
        this.user = u;
    }

    public List<Comment> getComments(){
        return comments;
    }

    public void setComments(List<Comment> c){
        this.comments = c;
    }
    
    public static Finder<Long, Forum> find = new Finder<>(Forum.class);

    public static final List<Forum> findAll() {
        return Forum.find.all();
    }

    public static Forum getForumById(Long id){
        if(id == null){
            return null;
        } else {
            return find.query().where().eq("id", id).findUnique();
        }
    }

}
