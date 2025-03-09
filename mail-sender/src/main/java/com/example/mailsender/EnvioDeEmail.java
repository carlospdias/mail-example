package com.example.mailsender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Locale;

@Service
public class EnvioDeEmail {

    private TemplateEngine templateEngine;
    private JavaMailSender javaMailSender;
    private byte[] applicationLogo;

    public EnvioDeEmail(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    private byte[] extractImageextractImage() throws IOException {
        if (this.applicationLogo == null) {
            Resource img = new ClassPathResource("static/img/logo-wifi-300x300.png");
            this.applicationLogo = img.getContentAsByteArray();
        }
        return this.applicationLogo;
    }


    private MimeMessage createMimeType(String htmlContent) throws MessagingException, IOException {
        final MimeMessage mimeMessage   = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

        message.setSubject("Example HTML email with inline image");
        message.setFrom("thymeleaf@example.com");
        message.setTo("carlos@thymeleaf.com");
        message.setText(htmlContent, true); // true = isHtml

        byte[] image = extractImageextractImage();
        InputStreamSource imageSource = new ByteArrayResource(image);
        message.addInline("logo", imageSource, "image/png");
        return mimeMessage;

    }

    private Context prepareContext(){
        final Context ctx = new Context(new Locale("pt","br"));

        ctx.setVariable("colaborador", "Carlos Pereira Dias");
        ctx.setVariable("empresa", "Jogos online S/A");
        ctx.setVariable("contrato", "152");
        ctx.setVariable("senha", "xadaefccasd21212c$sdasd");
        ctx.setVariable("imageResourceName", "logo");


        return ctx;
    }
    @Async
    public void sendEmailNow()  {
        MimeMessage message = null;
        try {

            Context ctx = prepareContext();
            final String htmlContent = templateEngine.process("mail/email-template.html", ctx);

            message = createMimeType(htmlContent);
            this.javaMailSender.send(message);
           // return true;
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
