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
import com.articles_hub.database.beans.UserProfile;
import com.articles_hub.database.beans.UserToken;
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
public class AuthenticationService {

    private static final Logger LOG = Logger.getLogger(AuthenticationService.class.getName());
    
    private static AuthenticationService obj;
    
    public static AuthenticationService getAuthenticationService(){
        if(obj==null)
        synchronized(AuthenticationService.class){
            if(obj==null)
                obj=new AuthenticationService();
        }    
        return obj;
    }
    
    private DataBase db;
    
    private AuthenticationService(){
        db=DataBase.getDataBase();
//        System.err.println("authentication service initialized");
    }
    
    public String userLogin(String userName,String pass){
//        System.out.println("check 3 "+token);
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            Query q= session.getNamedQuery("UserProfile.byName");
            q.setParameter("name", userName);
            List<UserProfile> list = q.list();
            if(list.size()<1){
                LOG.warning("AuthenticationService, userLogin :- "+
                            "user not found, userName :- "+userName);
                return null;
            }else if(list.size()>1){
                LOG.warning("AuthenticationService, userLogin :- "+
                            "multiple user found, users :- "+list);
                return null;
            }
//            System.out.println("check 4 "+userName+" "+pass);
            UserProfile user=list.get(0);
            if(!pass.equals(user.getPass())){
                LOG.info("AuthenticationService, userLogin :- "+
                            "Login fail, userName :- "+userName);
                return null;
            }
            String token = getToken(userName);
            if(token!=null && !token.trim().equals("")){
                LOG.info("AuthenticationService, userLogin :- "+
                            "Login successfull, userName :- "+userName);
                return token;
            }
//            System.out.println("check 5 "+user.getPass()+" "+pass);
            UserToken tokenOb=new UserToken(user);
//            System.out.println("check 6 "+tokenOb.getToken()+" "+tokenOb.getUser());
            session.save(tokenOb);
            session.flush();
            t.commit();
            LOG.info("AuthenticationService, userLogin :- "+
                        "Login successfull, userName :- "+userName);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive())
                t.rollback();
        }
//        System.out.println("token "+getToken(userName));
        return getToken(userName);
    }
    
    public boolean userLogout(String token){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            session.setFlushMode(FlushModeType.AUTO);
            UserToken tokenObj=session.get(UserToken.class, token);
            if(tokenObj==null){
                LOG.warning("AuthenticationService, userLogout :- "+
                            "invalid request, token :- "+token);
                return false;
            }
            session.delete(tokenObj);
            LOG.info("AuthenticationService, userLogout :- "+
                        "Logout successfull, token :- "+token);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return false;
    }
    
    String getToken(String userName){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            Query q= session.getNamedQuery("UserTokens.byName");
            q.setParameter("name", userName);
            List<UserToken> list = q.list();
            if(list.size()!=1){
                LOG.warning("AuthenticationService, getToken :- "+
                            "multiple user found, users :- "+list);
                return null;
            }
//            System.out.println("check 7 "+list.get(0).getToken());
            return list.get(0).getToken();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive())
                t.rollback();
        }
        return null;
    }
    
    public String getUserName(String token){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            session.setFlushMode(FlushModeType.AUTO);
            UserToken tokenObj=session.get(UserToken.class, token);
            if(tokenObj==null){
                LOG.warning("AuthenticationService, getUserName :- "+
                            "user not found, token :- "+token);
                return null;
            }
            return tokenObj.getUser().getUserName();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return null;
    }
    
}
