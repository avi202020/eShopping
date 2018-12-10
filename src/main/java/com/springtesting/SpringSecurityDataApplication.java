package com.springtesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
public class SpringSecurityDataApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpringSecurityDataApplication.class, args);
    }
}

