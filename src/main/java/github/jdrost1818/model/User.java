package github.jdrost1818.model;

import github.jdrost1818.model.craigslist.CraigslistPost;

import javax.persistence.*;
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

    public boolean hasSeenPost(String postId) {
        return seenPosts.stream().anyMatch(p -> p.getId().equals(postId));
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
}
