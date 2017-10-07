package com.articles_hub.heroku;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.SecuredRedirectHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * This class launches the web application in an embedded Jetty container. This is the entry point to your application. The Java
 * command that is used for launching should fire this main method.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        // The port that we should run on can be set into an environment variable
        // Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        if(!(System.getProperty("hibernate.connection.url")!=null&&!System.getProperty("hibernate.connection.url").trim().equals("")))
        	System.setProperty("hibernate.connection.url", System.getenv("hibernate.connection.url"));
        
        final Server server = new Server(Integer.valueOf(webPort));
//        final Server server = new Server();
        
        final WebAppContext root = new WebAppContext();

        root.setContextPath("/");
        // Parent loader priority is a class loader setting that Jetty accepts.
        // By default Jetty will behave like most web containers in that it will
        // allow your application to replace non-server libraries that are part of the
        // container. Setting parent loader priority to true changes this behavior.
        // Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
        root.setParentLoaderPriority(true);

        //jsp support
        org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server);
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");
        
        
        final String webappDirLocation = "src/main/webapp/";
//        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);
//        root.addServerClass("com.articles_hub.application.RestApplication");
        
//        HandlerCollection handlers = new HandlerCollection(
//                            root,
//                            new SecuredRedirectHandler(),
//                            new ContextHandlerCollection(),
//                            new DefaultHandler());
//        
//        HttpConfiguration httpConfig = new HttpConfiguration();
//        httpConfig.setSecureScheme("https");
//        httpConfig.setSecurePort(Integer.parseInt(webPort));
//        
//        HttpConfiguration httpsConfig = new HttpConfiguration();
//        httpsConfig.addCustomizer(new SecureRequestCustomizer());
//        
//        ServerConnector connector = new ServerConnector(server,
//                            new HttpConnectionFactory(httpConfig));
//        connector.setPort(8080);
//        
//        SslContextFactory sslContextFactory = new SslContextFactory(true);
//        sslContextFactory.setKeyStorePath("keystore/temp");
//        sslContextFactory.setKeyStorePassword("neel");
//        
//        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(
//                            sslContextFactory, "http/1.1");
//        
//        ServerConnector sslConnector = new ServerConnector(server,
//                            sslConnectionFactory,
//                            new HttpConnectionFactory(httpsConfig));
//        sslConnector.setPort(Integer.parseInt(webPort));
//        
//        server.setConnectors(new Connector[]{connector,sslConnector});
//        
        server.setHandler(root);
//        server.addBean(new com.articles_hub.application.RestApplication());
        server.start();
        server.join();
    }
}
