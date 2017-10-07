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
package com.articles_hub.database;

import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Neel Patel
 */
public class DataBase {
    static{//this block should be removed at deployment time.
//        System.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/Articles_Hub");
    }
    
    private static DataBase db;
    public static DataBase getDataBase(){
        if(db==null)
            synchronized(DataBase.class){
//            try {
//                Class.forName("org.hibernate.cfg.Configuration");
//            } catch(ClassNotFoundException ex) {
//                System.err.println("hibernate config not found");
//            }
                if(db==null)
                    db=new DataBase();
            }
        return db;
    }
    
    private SessionFactory sf=new Configuration().configure().buildSessionFactory();
    private DataBase(){
//        Configuration config=new Configuration();
//        config.configure();
//        sf=config.buildSessionFactory(new StandardServiceRegistryBuilder()
//                  .applySettings(config.getProperties()).build());
    }
    
    public Session getSession(){
        try{
            return sf.openSession();
        }catch(Throwable ex){
            ex.printStackTrace();
        }
        return null;
    }
}
//    private Session session;
//    
//    public DataBase(){
//        session = sf.openSession();
//        session.setHibernateFlushMode(FlushMode.ALWAYS);
//        Runtime.getRuntime().addShutdownHook(new Thread(()->{
//            try{
//                this.finalize();
//            }catch(Throwable t){
//                t.printStackTrace();
//            }
//        }));
//        session.beginTransaction();
//    }
//
//    /**
//     * this method return object of type UserProfile if user having userName
//       specified exist in database.
//     * object return by this method is persistent i.e. if any changes made in
//       future on this object will be reflected to the database.
//     * this method return {@code null} if there is no record of specified
//       username or more then one record of specified username.
//     * @param userName user name
//     * @return object of UserProfile if available, null otherwise. 
//     */
//    public UserProfile getUserProfile(String userName){
//        try{
//            synchronized(this){
////                Query q= session.createNamedQuery("UserProfile.byName");
//                sf.getCurrentSession().beginTransaction();
//                Query q= sf.getCurrentSession().createNamedQuery("UserProfile.byName");
//                q.setParameter("name", userName);
//                List<UserProfile> list = q.list();
//                if(list.size()==1)
//                    return list.get(0);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return null;
//    }
//    
//    /**
//     * this method return object of type UserProfile if user having
//       {@code userId} specified exist in database.
//     * object return by this method is persistent i.e. if any changes made in
//       future on this object will be reflected to the database.
//     * this method return {@code null} if there is no record of specified
//       userId or more then one record of specified userId.
//     * @param userId user id
//     * @return object of UserProfile if available, null otherwise. 
//     */
//    public UserProfile getUserProfile(long userId){
//        try{
//            synchronized(this){
////                return session.get(UserProfile.class, userId);
//                
//                sf.getCurrentSession().beginTransaction();
//                return sf.getCurrentSession().get(UserProfile.class, userId);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return null;
//    }
//    
//    /**
//     * this method store object of UserProfile in database.
//     * this method make any transient object persistent.
//     * this method should be called only once for object otherwise
//       there may be chance of duplicate record.
//     * this method returns {@code userid} of the object.
//     * this method returns -1 if there is any error occur while storing
//       object in database.
//     * @param user object to be stored.
//     * @return id of the user.
//     */
//    public long storeUserProfile(UserProfile user){
//        try{
//            synchronized(this){
////                return (long)session.save(user);
//                sf.getCurrentSession().beginTransaction();
//                return (long)sf.getCurrentSession().save(user);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return -1;
//    }
//    
//    /**
//     * this method return object of type Article if article having
//       {@code articleId} specified exist in database.
//     * object return by this method is persistent i.e. if any changes made in
//       future on this object will be reflected to the database.
//     * this method return {@code null} if there is no record of specified
//       articleId or more then one record of specified articleId.
//     * @param articleId article id
//     * @return object of Article if available, null otherwise. 
//     */
//    public Article getArticle(long articleId){
//        try{
//            synchronized(this){
////                return session.get(Article.class, articleId);
//                return sf.getCurrentSession().get(Article.class, articleId);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return null;
//    }
//    
//    /**
//     * this method return List of type articles which have any
//       of specified tags.
//     * objects in the list return by this method is persistent i.e. if any
//       changes made in future on this objects will be reflected to the database.
//     * this method return empty list if there is no such article exist or error
//       occur while processing.
//     * @param tags article tags
//     * @return list of Article. 
//     */
//    public List<Article> getArticles(Tag... tags){
//        try{
//            synchronized(this){
////                Query q= session.createNamedQuery("Article.byTag");
//                Query q= sf.getCurrentSession().createNamedQuery("Article.byTag");
//                q.setParameterList("tags", Stream.of(tags).map(t->t.getTagName()).toArray());
//                List<Article> list = q.list();
//                return list;
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return Collections.EMPTY_LIST;
//    }
//    
//    /**
//     * this method store object of Article in database.
//     * this method make any transient object persistent.
//     * this method should be called only once for object otherwise
//       there may be chance of duplicate record.
//     * this method returns {@code articleid} of the object.
//     * this method returns -1 if there is any error occur while storing
//       object in database.
//     * @param article object to be stored.
//     * @return id of the article.
//     */
//    public long storeArticle(Article article){
//        try{
//            synchronized(this){
////                return (long)session.save(article);
//                return (long)sf.getCurrentSession().save(article);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return -1;
//    }
//    
//    /**
//     * this method return object of type Tag if Tag having tagName
//       specified exist in database.
//     * object return by this method is persistent i.e. if any changes made in
//       future on this object will be reflected to the database.
//     * this method return {@code null} if there is no record of specified
//       tagName or more then one record of specified tagName.
//     * @param tagName tag name
//     * @return object of Tag if available, null otherwise. 
//     */
//    public Tag getTag(String tagName){
//        try{
//            synchronized(this){
////                Query q= session.createNamedQuery("Tag.byName");
//                Query q= sf.getCurrentSession().createNamedQuery("Tag.byName");
//                q.setParameter("name", tagName);
//                List<Tag> list = q.list();
//                if(list.size()==1)
//                    return list.get(0);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return null;
//    }
//    
//    /**
//     * this method return object of type Tag if tag having
//       {@code tagId} specified exist in database.
//     * object return by this method is persistent i.e. if any changes made in
//       future on this object will be reflected to the database.
//     * this method return {@code null} if there is no record of specified
//       tagName or more then one record of specified tagName.
//     * @param tagId user id
//     * @return object of Tag if available, null otherwise. 
//     */
//    public Tag getTag(long tagId){
//        try{
//            synchronized(this){
////                return session.get(Tag.class, tagId);
//                sf.getCurrentSession().beginTransaction();
//                return sf.getCurrentSession().get(Tag.class, tagId);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return null;
//    }
//    
//    /**
//     * this method store object of Tag in database.
//     * this method make any transient object persistent.
//     * this method should be called only once for object otherwise
//       there may be chance of duplicate record.
//     * this method returns {@code tagId} of the object.
//     * this method returns -1 if there is any error occur while storing
//       object in database.
//     * @param tag object to be stored.
//     * @return id of the tag.
//     */
//    public long storeTag(Tag tag){
//        try{
//            synchronized(this){
////                return (long)session.save(tag);
//                return (long)sf.getCurrentSession().save(tag);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return -1;
//    }
//    
//    public void flush(){
//        session.flush();
//    }
//    
//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        session.flush();
//        session.close();
//        sf.close();
//    }
//    
