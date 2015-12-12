package no.avec.social;

import org.apache.commons.mail.*;

import java.nio.charset.Charset;

/**
 * Created by ronny.ness on 11/12/15.
 */
public class MailClient {

    public void sendMail(String msg) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("ronny.naess", "****"));
        email.setSSLOnConnect(true);
        email.setFrom("ronny.naess@gmail.com");
        email.setSubject("Traderportefølje");
        //email.setMsg(msg);
        email.setHtmlMsg("<html><pre>" + msg + "</pre></html>");
        email.setTextMsg(msg);
        email.addTo("ronny.naess@gmail.com");
        //email.addTo("tkjelsrud@gmail.com");
        email.send();
    }

}
