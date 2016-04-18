package github.jdrost1818.model;

import javax.persistence.*;

/**
 * Table in charge of relating regular terms to actual tags.
 * For example:
 *
 *      TAG_DESCRIPTION_JOIN
 *
 *
 * Created by JAD0911 on 4/14/2016.
 */
@Entity
@Table(name = "TAG_DESCRIPTION_JOIN")
public class TagRelation {

    @Id
    private String id;

    @ManyToOne
    private Tag tag;

    public TagRelation() {
        // Need default constructor for hibernate
    }

    public TagRelation(String words, Tag tag) {
        setId(words);
        setTag(tag);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
