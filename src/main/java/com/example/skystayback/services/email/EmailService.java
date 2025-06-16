package com.example.skystayback.services.email;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Map;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, EmailTemplateType type, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        String htmlContent = templateEngine.process(type.getTemplateName(), context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendEmailWithAttachment(String to, String subject, String htmlContent, byte[] attachment, String attachmentName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            if (attachment != null && attachment.length > 0) {
                ByteArrayResource resource = new ByteArrayResource(attachment);
                helper.addAttachment(attachmentName, resource);
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando email con adjunto", e);
        }
    }
}

