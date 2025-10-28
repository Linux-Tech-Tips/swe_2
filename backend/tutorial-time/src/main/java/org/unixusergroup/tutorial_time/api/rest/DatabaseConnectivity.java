package org.unixusergroup.tutorial_time.api.rest;


import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.users.DataSourceUserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/api/db")
public class DatabaseConnectivity {

    // Need to have a function that just performs a get request from the db.
    // Could just be a data source connectivity check tbh

    @GetMapping(path="/get")
    public ResponseEntity<?> checkDatabaseConnection() {
        try {
            HikariDataSource datasource = new HikariDataSource();
            datasource.setUsername("guest");
            datasource.setPassword("guest");
            datasource.setJdbcUrl("jdbc:mariadb://db:3306/springbootdb");

            Connection connection = datasource.getConnection();
            return ResponseEntity.status(HttpStatus.FOUND).body(connection);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
