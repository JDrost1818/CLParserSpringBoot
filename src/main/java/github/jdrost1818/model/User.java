package github.jdrost1818.model;

import github.jdrost1818.model.craigslist.CraigslistPost;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String firstName;
    private String lastName;

    // TODO: I need to figure out encryption
    private String username;
    private String password;

    private Date lastParseTime = new Date(0);

    @Transient
    private List<CraigslistPost> seenPosts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<CraigslistPost> getSeenPosts() {
        return seenPosts;
    }

    public void setSeenPosts(List<CraigslistPost> seenPosts) {
        this.seenPosts = seenPosts;
    }

    public boolean hasSeenPost(String id, Date dateUpdated) {
        for (CraigslistPost seenPost : seenPosts) {
            if (seenPost.getId().equals(id)) {
                return dateUpdated.after(getLastParseTime());
            }
        }
        return false;
    }

    public void addSeenPost(CraigslistPost newPost) {
        if (seenPosts == null) {
            seenPosts = new ArrayList<>();
        }
        if (seenPosts.stream().noneMatch(post -> post.getId().equals(newPost.getId()))) {
            seenPosts.add(newPost);
        }
    }

    public void addSeenPosts(List<CraigslistPost> newPosts) {
        if (seenPosts == null) {
            seenPosts = newPosts;
        } else {
            seenPosts.addAll(newPosts);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastParseTime() {
        return lastParseTime;
    }

    public void setLastParseTime(Date lastParseTime) {
        this.lastParseTime = lastParseTime;
    }
}
