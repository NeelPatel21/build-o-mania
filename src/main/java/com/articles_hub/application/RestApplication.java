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
package com.articles_hub.application;

//import com.sun.jersey.api.core.PackagesResourceConfig;
import com.articles_hub.api.providers.AuthenticationFilter;
import com.articles_hub.api.resource.HouseDetailResource;
import com.articles_hub.api.resource.AuthenticationResource;
import com.articles_hub.api.resource.UserResource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/")
public class RestApplication extends Application {

    private Set<Class<?>> resource=new HashSet<>();
    private Map<String,Object> pro=new HashMap<>();
    public RestApplication() {
        if(!(System.getProperty("hibernate.connection.url")!=null&&!System.getProperty("hibernate.connection.url").trim().equals("")))
        	System.setProperty("hibernate.connection.url", System.getenv("hibernate.connection.url"));
        
//        System.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/Articles_Hub");     
    }

//    @Override
//    public Map<String, Object> getProperties() {
//        pro.putAll(super.getProperties());
//        return pro; //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public Set<Class<?>> getClasses() {
        resource.add(UserResource.class);
        resource.add(HouseDetailResource.class);
//        resource.add(CommentResource.class);
//        resource.add(TagResource.class);
        resource.add(AuthenticationResource.class);
        resource.add(AuthenticationFilter.class);
        return resource;
    }
    
    
}
