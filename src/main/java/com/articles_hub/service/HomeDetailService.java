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
import java.util.List;
import java.util.logging.Logger;
//import javax.persistence.Query;
import javax.persistence.FlushModeType;
import org.hibernate.query.Query;
//import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
//import org.hibernate.resource.transaction.spi.TransactionStatus;
//import org.hibernate.query.Query;

/**
 *
 * @author Neel Patel
 */
public class HomeDetailService {

    private static final Logger LOG = Logger.getLogger(HomeDetailService.class.getName());
    
    private static HomeDetailService obj=new HomeDetailService();
    
    public static HomeDetailService getHomeDetail(){
        return obj;
    }
    
    private DataBase db;
    
    private HomeDetailService(){
        db=DataBase.getDataBase();
//        System.err.println("HouseDetail service initialized");
    }
    
    public HouseDetail getHouseDetail(long articleId){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            HouseDetail article=(HouseDetail) session.get(HouseDetail.class, articleId);
            if(article==null){
                LOG.warning("ArticleService, getArticleDetail :- "+
                      "article not found, articleId :- "+articleId);
                return null;
            }
            LOG.info("ArticleService, getArticleDetail :- "+
                      "articleId :- "+article.getHouseDetailId());
            return article;
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return null;
    }
    
    public long addHouseDetail(HouseDetail articleDetail){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            if(articleDetail==null){
                LOG.warning("ArticleService, addArticle :- "+
                          "Null reference articleDetail");
                return -1;
            }
            HouseDetail article=articleDetail;
            session.setFlushMode(FlushModeType.AUTO);
            Query q= session.getNamedQuery("UserProfile.byName");
            q.setParameter("name", articleDetail.getAuthor());
            List<UserProfile> list = q.list();
//            System.out.println("check 5 - "+articleDetail.getAuthor());
            if(list.size()!=1){
                LOG.warning("ArticleService, addArticle :- "+
                            "Invalid UserProfile, number of UserProfile found :- "
                            +list.size());
                return -1;
            }
            list.get(0).addHouseDetail(article);
            session.flush();
            t.commit();
            LOG.info("ArticleService, addArticle :- "+
                      "Article Created, articleId :- "+article.getHouseDetailId());
            return article.getHouseDetailId();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive())
                t.rollback();
//            if(session!=null)
//                session.flush();
        }
        return -1;
    }
    
    public boolean updateArticle(HouseDetail articleDetail){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            if(articleDetail==null){
                LOG.warning("ArticleService, updateArticle :- "+
                          "Null reference articleDetail");
                return false;
            }
//            System.out.println("check 1");
            session.setFlushMode(FlushModeType.AUTO);
            HouseDetail article=session.get(HouseDetail.class, articleDetail.getHouseDetailId());
            if(article==null){
                LOG.warning("ArticleService, updateArticle :- "+
                            "article not found, articleId :- "
                            +articleDetail.getHouseDetailId());
                return false;
            }
            article.setTitle(articleDetail.getTitle());
            article.getArticleContent().clear();
            article.getArticleContent().addAll(articleDetail.getArticleContent());
            session.flush();
            t.commit();
            LOG.info("ArticleService, updateArticle :- "+
                      "article updated, aricleId :- "+article.getHouseDetailId());
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
    
    public boolean removeHouseDetail(long articleId){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            session.setFlushMode(FlushModeType.AUTO);
            HouseDetail article=(HouseDetail) session.get(HouseDetail.class, articleId);
            if(article==null){
                LOG.warning("ArticleService, removeArticleDetail :- "+
                            "article not found, articleId :- "+articleId);
                return false;
            }
            session.delete(article);
            LOG.info("ArticleService, removeArticleDetail :- "+
                        "article removed, articleId :- "+article.getHouseDetailId());
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return false;
    }
    
    public HouseDetail[] getAllArticle(int start,int size){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            Query q= session.getNamedQuery("Article.allArticle");
            q.setFirstResult(start);
            q.setMaxResults(size);
            List<HouseDetail> list = q.list();
            if(list.size()>=1){
                LOG.info("ArticleService, getAllArticle :- "+
                          "start :- "+start+", size :- "+size);
                return list.toArray(new HouseDetail[0]);
            }else{
                LOG.warning("ArticleService, getAllArticle :- "+
                          "no record found, start :- "+start+", size:-"+size);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(t!=null&&t.isActive()&&!t.getRollbackOnly())
                t.commit();
        }
        return new HouseDetail[0];
    }
    
    public long getAllCount(){
        Session session=db.getSession();
        Transaction t=session.beginTransaction();
        try{
            Query q= session.getNamedQuery("Article.count");
            long count = (Long)q.uniqueResult();
            LOG.info("ArticleService, getAllCount :- "+
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
