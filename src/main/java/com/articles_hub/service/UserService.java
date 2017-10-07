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
package com.articles_hub.service;

import com.articles_hub.database.DataBase;
import com.articles_hub.database.beans.HouseDetail;
import com.articles_hub.database.beans.UserProfile;
import com.articles_hub.api.model.UserDetail;
import com.articles_hub.api.model.Util;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.FlushModeType;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
//import org.hibernate.resource.transaction.spi.TransactionStatus;
//import org.hibernate.query.Query;

/**
 *
 * @author Neel Patel
 */
public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());
    
    private static UserService obj;
    
    public static UserService getUserService(){
        if(obj==null)
        synchronized(UserService.class){
            if(obj==null)
                obj=new UserService();
        }    
        return obj;
    }
    
    private DataBase db;
    
    private UserService(){
        db=DataBase.getDataBase();
//        System.err.println("user service initialized");
    }
    
    public UserDetail getUserDetail(String userName){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            Query q= session.getNamedQuery("UserProfile.byName");
            q.setParameter("name", userName);
            List<UserProfile> list = q.list();
            if(list.size()==1){
                LOG.info("UserService, getUserDetail :- "+
                          "userName :- "+userName);
                return Util.makeUserDetail(list.get(0));
            }else if(list.size()>1){
                LOG.warning("UserService, getUserDetail :- "+
                          "multiple UserProfile found, userName :- "+userName);
            }else{
                LOG.warning("UserService, getUserDetail :- "+
                          "UserProfile not found, userName :- "+userName);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return null;
    }
    
    public boolean addUser(UserDetail user){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            if(user==null){
                LOG.warning("UserService, addUser :- "+
                          "null reference user");
                return false;
            }
//            System.out.println("check 1");
            session.setFlushMode(FlushModeType.AUTO);
            session.save(Util.makeUserProfile(user));
            session.flush();
            t.commit();
            LOG.info("UserService, addUser :- "+
                      "userName :- "+user.getUserName());
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null && t.isActive())
                t.rollback();
//            if(session!=null)
//                session.flush();
        }
        return false;
    }
    
    public boolean updateUser(UserDetail user){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            if(user==null){
                LOG.warning("UserService, updateUser :- "+
                          "null reference user");
                return false;
            }
            session.setFlushMode(FlushModeType.AUTO);
            Query q= session.getNamedQuery("UserProfile.byName");
            q.setParameter("name", user.getUserName());
            List<UserProfile> list = q.list();
            if(list.size()!=1){
                LOG.warning("UserService, updateUser :- "+
                            "multiple UserProfile found, userName :- "
                            +user.getUserName());
                return false;
            }
            UserProfile userProfile=list.get(0);
            userProfile.setFirstName(user.getFirstName());
            userProfile.setLastName(user.getLastName());
            userProfile.setPass(user.getPass());
            userProfile.setInfo(user.getInfo());
            userProfile.setEmailId(user.getEmailId());
            session.flush();
            t.commit();
            LOG.info("UserService, updateUser :- "+
                        "UserProfile updated, userName :- "
                        +user.getUserName());
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null && t.isActive())
                t.rollback();
//            if(session!=null)
//                session.flush();
        }
        return false;
    }
    
    public HouseDetail[] getAllHouseDetails(String userName){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            Query q= session.getNamedQuery("UserProfile.byName");
            q.setParameter("name", userName);
            List<UserProfile> list = q.list();
            if(list.size()<1){
                LOG.warning("UserService, getAllArticles :- "+
                          "UserProfile not found, userName :- "+userName);
                return null;
            }else if(list.size()>1){
                LOG.warning("UserService, getAllArticles :- "+
                          "multiple UserProfile found, userName :- "+userName);
                return null;
            }
            UserProfile user = list.get(0);
            LOG.info("UserService, getAllArticles :- "+
                        "userName :- "+userName+", number of articles :- "+
                        user.getHouseDetails().size());
            return user.getHouseDetails().toArray(new HouseDetail[0]);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return null;
    }
    
    public UserProfile[] getAllUserProfiles(int start,int size){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            Query q= session.getNamedQuery("UserProfile.allUser");
            q.setFirstResult(start);
            q.setMaxResults(size);
            List<UserProfile> list = q.list();
            if(list.size()>=1){
                LOG.info("UserService, getAllUserProfiles :- "+
                          "start :- "+start+", size :- "+size);
                return list.toArray(new UserProfile[0]);
            }else{
                LOG.warning("UserService, getAllUserProfiles :- "+
                          "no record found, start :- "+start+", size:-"+size);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return new UserProfile[0];
    }
    
    public long getAllCount(){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            Query q= session.getNamedQuery("UserProfile.count");
            long count = (Long)q.uniqueResult();
            LOG.info("UserService, getAllCount :- "+
                      "count :- "+count);
            return count;
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return 0;
    }
}
