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
package com.articles_hub.api.resource;


import com.articles_hub.api.model.UserDetail;
import com.articles_hub.service.AuthenticationService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Neel Patel
 */
@Path("/authentication")
//@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
public class AuthenticationResource {
    
    private AuthenticationService service;
    
    public AuthenticationResource(){
        try{
            service=AuthenticationService.getAuthenticationService();
//            System.out.println(" dccewcahcajhcabcjabcajcbac "+service);
        }catch(Exception ex){
            ex.printStackTrace();
        }
//        System.out.println("user service request");
    }
//    @GET
//    public String getUserDetail(){
//        return "service :- ";
//    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{userName}")
    public String login(@PathParam("userName") String userName, UserDetail user){
        if(!user.getUserName().equals(userName))
            return null;
//        System.out.println("check 1");
        String s= service.userLogin(user.getUserName(), user.getPass());
//        System.out.println("check 2 "+s);
        return s;
    }
    
    @DELETE
    @Path("/{userName}")
    public Response logout(@HeaderParam("token") String token, @PathParam("userName") String userName){
        if(!service.getUserName(token).equals(userName))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        if(service.userLogout(token))
            return Response.status(Response.Status.OK).build();
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    
}
