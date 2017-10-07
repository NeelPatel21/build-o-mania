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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @NamedQuery(name = "UserProfile.byName",
          query = "from UserProfile where userName = :name"),
    @NamedQuery(name = "UserProfile.count",
          query = "select count(user) from UserProfile user"),
    @NamedQuery(name = "UserProfile.allUser", query = "from UserProfile")
})
@Table(name = "user_profiles")
//@XmlRootElement
public class UserProfile{
    
//schema
    @Id
    @GenericGenerator (name = "userId_gen", strategy = "sequence")
    @GeneratedValue(generator = "userId_gen")
    private long userId;

    @Column(name = "user_name", length = 50, nullable = false, unique = true)
    private String userName;
    
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Column(name = "passwd", length = 50, nullable = false)
    private String pass;
    
    @Column(name = "user_email", length = 100, nullable = false, unique = true)
    private String emailId;
    
    @Column(name = "user_info", length = 5000)
    private String info;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<HouseDetail> houseDetails = new HashSet<>(); // articles written by User.
    
//methods & constructors

    /**
     * initialized object with specified id.
     * @deprecated object initialized with this constructor might not be
       able to used with database as the userId is auto-generated field.
     * @param id userId
     */
    public UserProfile(long id) {
        userId=id;
    }

    public UserProfile() {
    }

    public String getEmailId() {
        return emailId;
    }
    
    public String getInfo() {
        return info;
    }

    @XmlTransient
    public Set<HouseDetail> getHouseDetails() {
        return Collections.unmodifiableSet(houseDetails);
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    /**
     * this method add article to the user.
     * this method will add the article to the users record articles.
     * it also update the article author of the object.
     * if 'comment' parameter is {@code null} then this method do nothing.<br>
     * Note:- if the article is already exist in the record (i.e. article having
       same articleId) then this method will do nothing.
     * @param article HouseDetail object going to be added.
     */
    public void addHouseDetail(HouseDetail houseDetail){
        if(houseDetail == null)
            return;
        if(this.houseDetails.add(houseDetail))
            houseDetail.setAuthor(this);
    }
    
    @Override
    public String toString() {
        return getUserId()+", "+getUserName();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
}
