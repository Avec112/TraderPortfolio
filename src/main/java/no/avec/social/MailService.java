package no.avec.social;

import org.apache.commons.mail.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * Created by ronny.ness on 11/12/15.
 */
@Component
public class MailService {

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${from}")
    private String from;

    @Value("${to}")
    private String[] to;

    @Value("${subject}")
    private String subject;


    public void send(String msg) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setAuthentication(username, password);
        email.setStartTLSEnabled(true);
        email.setFrom(from);
//        email.setCharset("ISO-8859-1");
        email.setSubject(subject);
        email.setHtmlMsg(msg);
//        email.setTextMsg(msg);
        email.addTo(to);
        email.send();
    }

}
