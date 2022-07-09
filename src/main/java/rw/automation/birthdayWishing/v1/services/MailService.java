package rw.automation.birthdayWishing.v1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import rw.automation.birthdayWishing.v1.exceptions.BadRequestException;
import rw.automation.birthdayWishing.v1.models.User;
import rw.automation.birthdayWishing.v1.utils.Mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String appEmail;

    @Value("${swagger.app_name}")
    private String appName;

    @Autowired
    public MailService(SpringTemplateEngine templateEngine, JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmailVerifiedMail(User user) {
        Mail mail = new Mail(
                appName,
                "Successfully verified email",
                user.getFullName(), user.getEmail(),
                "verified-email",
                null);

        sendEmail(mail);
    }


    @Async
    public void sendAccountVerificationEmail(User user) {
        String message =user.getActivationCode();
        Mail mail = new Mail(
                appName,
                "Welcome to "+appName,
                user.getFullName(), user.getEmail(), "verify-email", message);

        sendEmail(mail);
    }

    @Async
    public void sendWelcomeEmailMail(User user) {
        Mail mail = new Mail(
                appName,
                "Welcome to "+appName+"",
                user.getFullName(), user.getEmail(), "welcome-email", user.getEmail());

        sendEmail(mail);
    }

    @Async
    public void sendResetPasswordMail(User user) {
        Mail mail = new Mail(
                appName,
                "Welcome to "+appName+", You requested to reset your password",
                user.getFullName(), user.getEmail(), "reset-password-email", user.getActivationCode());

        sendEmail(mail);
    }

    @Async
    public void sendBirthdayWishingToBirthdayHolder(User user){
        Mail mail = new Mail(
                appName,
                "Happy Birthday "+user.getFullName(),
                user.getFullName(),user.getEmail(),"wish-birthday-automation",user.getEmail()
        );
        sendEmail(mail);
    }

    @Async
    public void sendBirthdayWishingReminderToBirthdayHolderConnections(User user, User birthdayHolder){
        Mail mail = new Mail(
                appName,
                birthdayHolder.getFullName()+"'s birthday",
                user.getFullName(),user.getEmail(),"remind-birthday-automation",birthdayHolder.getFullName()
        );
        sendEmail(mail);
    }


    @Async
    public void sendEmail(Mail mail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("app_name",mail.getAppName());
            context.setVariable("data", mail.getData());
            context.setVariable("name", mail.getFullNames());
            context.setVariable("date",LocalDate.now());
            context.setVariable("otherData", mail.getOtherData());
            context.setVariable("birthdayHolder",mail.getData());

            String html = templateEngine.process(mail.getTemplate(), context);
            helper.setTo(mail.getToEmail());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(appEmail);
            mailSender.send(message);


        } catch (MessagingException exception) {
            throw new BadRequestException("Failed To Send An Email");
        }
    }
}