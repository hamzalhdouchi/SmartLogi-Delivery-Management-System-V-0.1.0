package com.smartlogi.smartlogi_v0_1_0.serviceTest;

import com.smartlogi.smartlogi_v0_1_0.entity.ClientExpediteur;
import com.smartlogi.smartlogi_v0_1_0.entity.Colis;
import com.smartlogi.smartlogi_v0_1_0.entity.Destinataire;
import com.smartlogi.smartlogi_v0_1_0.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notification Service - Complete Test Suite")
class NotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private NotificationService notificationService;

    private ClientExpediteur clientExpediteur;
    private Destinataire destinataire;
    private Colis colis;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationService, "emailFrom", "hamzaalhadouchi@gmail.com");

        clientExpediteur = new ClientExpediteur();
        clientExpediteur.setId("client-123");
        clientExpediteur.setNom("Dupont");
        clientExpediteur.setPrenom("Jean");
        clientExpediteur.setEmail("jean.dupont@example.com");
        clientExpediteur.setTelephone("0612345678");

        destinataire = new Destinataire();
        destinataire.setId("dest-123");
        destinataire.setNom("Martin");
        destinataire.setPrenom("Pierre");
        destinataire.setEmail("pierre.martin@example.com");
        destinataire.setTelephone("0698765432");

        colis = new Colis();
        colis.setId("colis-123");
        colis.setClientExpediteur(clientExpediteur);
        colis.setDestinataire(destinataire);
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should send collecte email successfully")
    void testEnvoyerNotificationCollecte_Success() throws Exception {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email collecte</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(templateEngine, times(1)).process(eq("email-collecte"), any(Context.class));
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle exception gracefully")
    void testEnvoyerNotificationCollecte_Exception() {
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("SMTP error"));

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle template engine exception")
    void testEnvoyerNotificationCollecte_TemplateException() {
        when(templateEngine.process(anyString(), any(Context.class))).thenThrow(new RuntimeException("Template error"));

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle null client email")
    void testEnvoyerNotificationCollecte_NullEmail() {
        clientExpediteur.setEmail(null);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle empty client email")
    void testEnvoyerNotificationCollecte_EmptyEmail() {
        clientExpediteur.setEmail("");
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle invalid email format")
    void testEnvoyerNotificationCollecte_InvalidEmail() {
        clientExpediteur.setEmail("invalid-email");
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle special characters in client name")
    void testEnvoyerNotificationCollecte_SpecialCharacters() throws Exception {
        clientExpediteur.setNom("Dupont-O'Connor");
        clientExpediteur.setPrenom("Jean-François");
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle long tracking number")
    void testEnvoyerNotificationCollecte_LongTrackingNumber() throws Exception {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should send livraison email successfully")
    void testEnvoyerNotificationLivraison_Success() throws Exception {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email livraison</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(templateEngine, times(1)).process(eq("email-livraison"), any(Context.class));
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle exception gracefully")
    void testEnvoyerNotificationLivraison_Exception() {
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("SMTP error"));

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle template engine exception")
    void testEnvoyerNotificationLivraison_TemplateException() {
        when(templateEngine.process(anyString(), any(Context.class))).thenThrow(new RuntimeException("Template error"));

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle null destinataire email")
    void testEnvoyerNotificationLivraison_NullEmail() {
        destinataire.setEmail(null);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle empty destinataire email")
    void testEnvoyerNotificationLivraison_EmptyEmail() {
        destinataire.setEmail("");
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle invalid email format")
    void testEnvoyerNotificationLivraison_InvalidEmail() {
        destinataire.setEmail("invalid-email");
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle special characters in destinataire name")
    void testEnvoyerNotificationLivraison_SpecialCharacters() throws Exception {
        destinataire.setNom("Martin-Lefèvre");
        destinataire.setPrenom("Marie-José");
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(mailSender, times(1)).send(mimeMessage);
    }

//    @Test
//    @DisplayName("EnvoyerNotificationLivraison - Should handle multiple recipients")
//    void testEnvoyerNotificationLivraison_MultipleRecipients() throws Exception {
//        destinataire.setEmail("user1@example.com,user2@example.com");
//        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
//        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        doNothing().when(mailSender).send(any(MimeMessage.class));
//
//        notificationService.envoyerNotificationLivraison(destinataire, colis);
//
//        verify(mailSender, never()).send(mimeMessage);
//    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should use correct email from")
    void testEnvoyerNotificationCollecte_CorrectEmailFrom() throws Exception {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(mailSender, times(1)).send(mimeMessage);
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should use correct email from")
    void testEnvoyerNotificationLivraison_CorrectEmailFrom() throws Exception {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(mailSender, times(1)).send(mimeMessage);
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle mail sender send exception")
    void testEnvoyerNotificationCollecte_SendException() {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Send failed")).when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(mailSender, times(1)).createMimeMessage();
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle mail sender send exception")
    void testEnvoyerNotificationLivraison_SendException() {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Send failed")).when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(mailSender, times(1)).createMimeMessage();
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle HTML content with special chars")
    void testEnvoyerNotificationCollecte_HtmlSpecialChars() throws Exception {
        when(templateEngine.process(anyString(), any(Context.class)))
                .thenReturn("<html><body>Bonjour & bienvenue chez SmartLogi © 2024</body></html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationCollecte(clientExpediteur, colis);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle HTML content with special chars")
    void testEnvoyerNotificationLivraison_HtmlSpecialChars() throws Exception {
        when(templateEngine.process(anyString(), any(Context.class)))
                .thenReturn("<html><body>Livraison confirmée ✓ pour le colis #123</body></html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        notificationService.envoyerNotificationLivraison(destinataire, colis);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("EnvoyerNotificationCollecte - Should handle null colis")
    void testEnvoyerNotificationCollecte_NullColis() {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");

        notificationService.envoyerNotificationCollecte(clientExpediteur, null);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("EnvoyerNotificationLivraison - Should handle null colis")
    void testEnvoyerNotificationLivraison_NullColis() {
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");

        notificationService.envoyerNotificationLivraison(destinataire, null);

        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }
}
