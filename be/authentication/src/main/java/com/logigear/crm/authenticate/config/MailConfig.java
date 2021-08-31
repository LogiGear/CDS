package com.logigear.crm.authenticate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String HOST_ADDRESS;
    @Value("${spring.mail.port}")
    private String HOST_PORT;
    @Value("${spring.mail.username}")
    private String USERNAME;
    @Value("${spring.mail.password}")
    private String PASSWORD;
    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String TRANSPORT_PROTOCOL;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String AUTH;
    @Value("${spring.mail.properties.mail.debug}")
    private String DEBUG;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String STARTTLS_ENABLE;
    @Value("${spring.mail.properties.mail.smtp.ssl.trust}")
    private String SSL_TRUST;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(HOST_ADDRESS);
        javaMailSender.setPort(Integer.parseInt(HOST_PORT));
        javaMailSender.setUsername(USERNAME);
        javaMailSender.setPassword(PASSWORD);
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", TRANSPORT_PROTOCOL);
        properties.setProperty("mail.smtp.ssl.trust", SSL_TRUST);
        properties.setProperty("mail.smtp.auth", AUTH);
        properties.setProperty("mail.smtp.starttls.enable", STARTTLS_ENABLE);
        properties.setProperty("mail.debug", DEBUG);
        return properties;
    }
}

