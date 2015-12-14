package no.avec.traderportfolio.service.social;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by avec on 11/12/15.
 */
@Component
public class MailService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${gmail.username}")
    private String username;

    @Value("${gmail.password}")
    private String password;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.to}")
    private String[] to;

    @Value("${mail.subject}")
    private String subject;


    public void send(String msg) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setAuthentication(username, password);
        email.setStartTLSEnabled(true);
        email.setFrom(from);
        email.setSubject(subject);
        email.setHtmlMsg(msg);
        email.addTo(to);
        email.send();

        LOG.debug("Mail sendt");
    }

}
