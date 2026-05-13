package com.wedding.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String driver = sce.getServletContext().getInitParameter("jdbc.driver");
        String url = sce.getServletContext().getInitParameter("jdbc.url");
        String username = sce.getServletContext().getInitParameter("jdbc.username");
        String password = sce.getServletContext().getInitParameter("jdbc.password");
        DBConnection.initialize(driver, url, username, password);
    }
}
