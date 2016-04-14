package github.jdrost1818.model;

import javax.persistence.*;

/**
 * Created by JAD0911 on 4/14/2016.
 */
@Entity
@Table(name = "TAG")
public class Tag {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String title;

    public Tag() {
        // Needed for hibernate
    }

    public Tag(String title) {
        setTitle(title);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
