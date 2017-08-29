/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tallaron.snips.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Administrator
 */
@Entity
public class Snippet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    @Column(length=16384)
    private String content;
    private User user;
    private Language language;
    private Category category;
    
    public boolean isSelectedId(Snippet s) {
        if(s != null && id != null) {
        return id.equals(s.getId());
        } return false;
    }
    
    public boolean isSelectedCategory(Category c) {
        if(c != null && category != null) {
        return category.equals(c);
        } return false;
    }
    
    public boolean isSelectedLanguage(Language l) {
        if(l != null && language != null) {
        return language.equals(l);
        } return false;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if(!user.getSnippets().contains(this)) {
            user.addSnippet(this);
        }
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
    
    public void setLanguage(String language) {
        Language lang = new Language();
        lang.setName(language);
        this.language = lang;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public void setCategory(String category) {
        Category cat = new Category();
        cat.setName(category);
        this.category = cat;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Snippet)) {
            return false;
        }
        Snippet other = (Snippet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.gfn.snips.entities.Snippet[ id=" + id + " ]";
    }
    
}
