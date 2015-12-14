package no.avec.domain;

import j2html.TagCreator;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by ronny.ness on 13/12/15.
 */
public class HtmlMessageTest extends TestCase {

    @Test
    public void testBuilder() {
        HtmlMessage htmlMessage = new HtmlMessage.HtmlMessageBuilder()
                .h1("H1 tekst her")
                .br()
                .p("Paragraf her")
                .pre("pretekst inni her")
                .build();
        String html = htmlMessage.render();
        assertEquals("<html><h1>H1 tekst her</h1><br><p>Paragraf her</p><pre>pretekst inni her</pre></html>", html);
    }

}