package com.nexign.dmf.rss.rssvw.config;



import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpSessionConfig {
    @Value("${app.session.maxInactiveInterval}")
    private Integer MAXINACTIVEINTERVAL;
    @Bean                           // bean for http session listener
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {               // This method will be called when session created
                se.getSession().setMaxInactiveInterval(MAXINACTIVEINTERVAL);
//                System.out.println("Session Created with session id+" + se.getSession().getId());
//                System.out.println("MAXINACTIVEINTERVAL="+MAXINACTIVEINTERVAL);
            }
            @Override
            public void sessionDestroyed(HttpSessionEvent se) {         // This method will be automatically called when session destroyed
                UserDBSource uds = (UserDBSource) se.getSession().getAttribute("datasource");
                uds.closeDataSource();
//                System.out.println("Session Destroyed, Session id:" + se.getSession().getId());
            }
        };
    }
    @Bean                   // bean for http session attribute listener
    public HttpSessionAttributeListener httpSessionAttributeListener() {
        return new HttpSessionAttributeListener() {
            @Override
            public void attributeAdded(HttpSessionBindingEvent se) {            // This method will be automatically called when session attribute added
//                System.out.println("Attribute Added following information");
//                System.out.println("Attribute Name:" + se.getName());
//                System.out.println("Attribute Value:" + se.getValue());
            }
            @Override
            public void attributeRemoved(HttpSessionBindingEvent se) {      // This method will be automatically called when session attribute removed
                System.out.println("attributeRemoved");
            }
            @Override
            public void attributeReplaced(HttpSessionBindingEvent se) {     // This method will be automatically called when session attribute replace
//                System.out.println("Attribute Replaced following information");
//                System.out.println("Attribute Name:" + se.getName());
//                System.out.println("Attribute Old Value:" + se.getValue());
            }
        };
    }
}
