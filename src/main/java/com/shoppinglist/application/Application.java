package com.shoppinglist.application;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "shoppinglist")
@PWA(name = "Shopping List", shortName = "Shopping List", iconPath = "images/shoppinglist.png", offlineResources = {"images/logo.png"})
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application implements AppShellConfigurator {
    private static Connection conn;

    private static int user_id;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static Connection getConnection() {
        try {
            if (conn != null && !conn.isClosed())
                return  conn;
            else {
                String url = "jdbc:postgresql://192.168.0.236:5432/postgres?user=postgres&password=qwe";
                conn = DriverManager.getConnection(url);
                conn.setSchema("ShoppingList");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static int getUser_id() {
        user_id = 1;
        return user_id;
    }
}
