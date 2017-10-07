/*
 * The MIT License
 *
 * Copyright 2017 Neel Patel.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.articles_hub.database.beans;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
//import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 *
 * @author Neel Patel
 */
@Entity
@NamedQueries({
//    @NamedQuery(name = "Article.byTag",
//          query = "select distinct a from Article a inner join "
//                    +"a.tags t where t.tagName in (:tags) order by a.articleId desc"),
    @NamedQuery(name = "Article.count",
          query = "select count(a) from HouseDetail a"),
    @NamedQuery(name = "Article.allArticle", query = "from HouseDetail")
})
//@XmlRootElement
@XmlRootElement(name = "houseDetail")
@Table(name = "house_details")
public class HouseDetail {

//schema    
    @Id
    @GenericGenerator(name = "houseDetailId_gen", strategy = "sequence")
    @GeneratedValue(generator = "houseDetailId_gen")
    private long houseDetailId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserProfile author; //foreign key :- UserProfile(userId)
    
    @Column(name = "detail_title", nullable = false, length = 1000)
    private String title;
    
    @Lob
    @Column(name = "article_data",nullable = false)
    private Description description = new Description();
    
    @Column(name = "category", length = 10, nullable = false)
    private String category;
    
//methods & constructors

    /**
     * initialized object with specified id.
     * @deprecated object initialized with this constructor might not be
       able to used with database as the userId is auto-generated field.
     * @param id articleId
     */
    public HouseDetail(long id){
        this.houseDetailId=id;
    }

    public HouseDetail() {
    }
    
    public long getHouseDetailId() {
        return houseDetailId;
    }

    @XmlTransient
    public UserProfile getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getArticleContent() {
        return description.getContent();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * update author with 'user'
     * @param user author of the article
     */
    void setAuthor(UserProfile user){
        this.author=user;
    }
    
    @Override
    public String toString() {
        return houseDetailId+", "+title;
    }

    /**
     * this method returns true only if 'obj' is instance of HouseDetail and it have
       same 'articleId'.
     * @param obj object
     * @return true if object is equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof HouseDetail))
            return false;
        HouseDetail up=(HouseDetail)obj;
        return this.houseDetailId==up.houseDetailId;
    }
 
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.houseDetailId ^ (this.houseDetailId >>> 32));
        return hash;
    }
    
}
