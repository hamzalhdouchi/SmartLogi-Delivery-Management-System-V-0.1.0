package com.smartlogi.smartlogiv010.service;

import com.smartlogi.smartlogiv010.entity.ClientExpediteur;
import com.smartlogi.smartlogiv010.entity.Colis;
import com.smartlogi.smartlogiv010.entity.Destinataire;
import com.smartlogi.smartlogiv010.entity.User;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${smartlogi.email.from:hamzaalhadouchi@gmail.com}")
    private String emailFrom;


    public void envoyerNotificationCollecte(User client, Colis colis) {
        try {
            Context context = new Context();
            context.setVariable("client", client);
            context.setVariable("colis", colis);
            context.setVariable("dateCollecte", LocalDateTime.now());

            String htmlContent = templateEngine.process("email-collecte", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(client.getEmail());
            helper.setSubject("Votre colis a été collecté - SmartLogi");
            helper.setText(htmlContent, true);
            helper.setFrom(emailFrom);

            mailSender.send(message);
            log.info("Email de collecte envoyé à: {}", client.getEmail());

        } catch (Exception e) {
            log.error("Erreur envoi email collecte: {}", e.getMessage());
        }
    }

    public void envoyerNotificationLivraison(Destinataire destinataire, Colis colis) {
        try {
            Context context = new Context();
            context.setVariable("destinataire", destinataire);
            context.setVariable("colis", colis);
            context.setVariable("dateLivraison", LocalDateTime.now());

            String htmlContent = templateEngine.process("email-livraison", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinataire.getEmail());
            helper.setSubject("Votre colis a été livré - SmartLogi");
            helper.setText(htmlContent, true);
            helper.setFrom(emailFrom);
            mailSender.send(message);
            log.info("Email de livraison envoyé à: {}", destinataire.getEmail());

        } catch (Exception e) {
            log.error("Erreur envoi email livraison: {}", e.getMessage());
        }
    }

}


//tasklist | findstr java
//taskkill /F /IM java.exe
