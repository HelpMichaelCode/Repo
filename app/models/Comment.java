package models;

import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.*;
import models.users.*;

@Entity
public class Comment extends Model {
    @Id
    private Long id;
    @ManyToOne
    private User user; //ser who posted the comment
    @ManyToOne
    private Forum forum;
    @Constraints.Required
    private String body;

    public Comment(){
    }

    public Comment(Long id, String body, Forum forum, User user){
        this.id = id;
        this.body = body;
        this.forum = forum;
        this.user = user;
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
    public User getUser(){
        return user;
    }

    public void setUser(User u){
        this.user = u;
    }
    public Forum getForum(){
        return forum;
    }

    public void setForum(Forum f){
        this.forum = f;
    }
    
    public static Finder<Long, Comment> find = new Finder<>(Comment.class);

    public static final List<Comment> findAll() {
        return Comment.find.all();
    }

    // public Comment getCommentbyId(Long id){
    //     if(id == null){
    //         return null;
    //     } else {
    //         return find.query().where().eq("id", id).findUnique();
    //     }
    }

