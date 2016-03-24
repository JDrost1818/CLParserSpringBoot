package github.jdrost1818.model;

import github.jdrost1818.model.craigslist.CraigslistPost;

import java.util.List;

/**
 * Created by JAD0911 on 3/24/2016.
 */
public class User {

    private Long id;

    private String firstName;
    private String lastName;

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

    public boolean hasSeenPost(CraigslistPost post) {
        return this.seenPosts.contains(post);
    }
}
