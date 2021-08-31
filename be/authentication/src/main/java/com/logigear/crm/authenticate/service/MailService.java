package com.logigear.crm.authenticate.service;

public interface MailService {
    void send(String to, String email, String subject);
    String buildEmail(String email, String name, String link);
    String buildEmailForgotPassword(String email, String name, String link);
}
