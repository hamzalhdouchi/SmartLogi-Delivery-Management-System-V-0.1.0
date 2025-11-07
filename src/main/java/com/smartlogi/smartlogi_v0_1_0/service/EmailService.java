package com.smartlogi.smartlogi_v0_1_0.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendTemplateEmail(String to, String subject,
                                  String template, Map<String, Object> variables)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Prepare Thymeleaf context with variables
        Context context = new Context();
        context.setVariables(variables);

        // Process the template with given context
        String htmlContent = templateEngine.process(template, context);

        // Set email properties
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML content

        // Send the email
        mailSender.send(message);
    }
}
