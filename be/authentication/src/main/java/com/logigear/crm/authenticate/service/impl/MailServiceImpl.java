package com.logigear.crm.authenticate.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.logigear.crm.authenticate.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(MailService.class);
    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email,String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("sprintOne@localhost.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Override
    public String buildEmail(String email,String name, String link) {
        String img = "https://i.ibb.co/F3CzCQc/mowede-logo.png";
        return
                "<div style=\"text-align: center;\" align=\"center\"><img src=\""+img+"\" \n" +
                        "alt=\"Mowede\" width=\"auto\" height=\"30\" /></div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Hi <strong>"+name+"</strong>,</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Thanks for registering an account for LogiGear CDS!</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">We need a little more information to complete your registration, including a</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">confirmation of your email address</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Click the button bellow to confirm your email address: <strong>"+ email +"</strong></div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: center;align: center\"><a href=\""+link+"\" style=\" background-color:#fdd803;border: none;border-radius:5px; color: black; padding: 8px 30px;text-align: center; " +
                        "text-decoration: none;cursor: pointer;font-size: 16px; font-weight: bold;\">Click here to confirm</a></div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">If you have any problems, please contact the adminstrator.</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Thank you,</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Mowede Team</div>\n";
    }

    @Override
    public String buildEmailForgotPassword(String email, String name, String link) {
    	String img = "https://i.ibb.co/F3CzCQc/mowede-logo.png";
        return
                "<div style=\"text-align: center;\" align=\"center\"><img src=\""+img+"\" \n" +
                        "alt=\"Mowede\" width=\"auto\" height=\"30\" /></div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Hi <strong>"+name+"</strong>,</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\"We have received a request to reset your password for your account</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">To reset your password, click the button below: <strong>"+
                "<div style=\"text-align: center;align: center\"><a href=\""+link+"\" style=\" background-color:#fdd803;border: none;border-radius:5px; color: black; padding: 8px 30px;text-align: center; " +
                        "text-decoration: none;cursor: pointer;font-size: 16px; font-weight: bold;\">Reset password</a></div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">If you didn't request a password reset, you can ignore this email.</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Please note: this link will be valid for the next 24 hours.\n"+
                "<div style=\"text-align: left;\" align=\"center\">&nbsp;</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Thank you,</div>\n"+
                "<div style=\"text-align: left;\" align=\"center\">Mowede Team</div>\n";
    }
}
