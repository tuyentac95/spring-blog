package com.codegym.model;

import javax.persistence.*;

@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category_id;

    private String description;
    private String content_id;
    private String status;

    public Blog() {
    }

    public Blog(String title, Category category_id, String description, String content_id, String status) {
        this.title = title;
        this.category_id = category_id;
        this.description = description;
        this.content_id = content_id;
        this.status = status;
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

    public Category getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Category category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category_id=" + category_id +
                ", description='" + description + '\'' +
                ", content_id='" + content_id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
