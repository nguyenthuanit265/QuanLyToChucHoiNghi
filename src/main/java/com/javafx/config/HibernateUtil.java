package com.javafx.config;

import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.List;
import java.util.Properties;

public class HibernateUtil {
    public static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties properties = new Properties();
                properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                properties.put(Environment.URL, "jdbc:mysql://localhost:3306/quanlyhoinghi_javafx");
                properties.put(Environment.USER, "root");
                properties.put(Environment.PASS, "minhthuan123456");

                properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
                properties.put("hibernate.show_sql", false);
                properties.put("hibernate.format_sql", true);
                properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                properties.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(properties);

                List<Class<?>> classes = EntityScanner
                        .scanPackages("com.javafx.entity").result();

                MetadataSources metadataSources = new MetadataSources();
                for (Class<?> annotatedClass : classes) {
                    metadataSources.addAnnotatedClass(annotatedClass);
                }


                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                EntityScanner.scanPackages("com.javafx.entity")
                        .addTo(configuration);
                sessionFactory = configuration.buildSessionFactory();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
