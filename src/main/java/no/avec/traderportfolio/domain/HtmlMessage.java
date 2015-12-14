package no.avec.traderportfolio.domain;

import j2html.TagCreator;
import j2html.tags.ContainerTag;


/**
 * Created by avec on 13/12/15.
 */
public class HtmlMessage {

    private final ContainerTag ct;

    private HtmlMessage(HtmlMessageBuilder builder) {
        this.ct = builder.ct;
    }

    public String render() {
        return this.ct.render();
    }

    public static class HtmlMessageBuilder {

        private final ContainerTag ct;

        public HtmlMessageBuilder() {
            ct = TagCreator.html();
        }

        public HtmlMessageBuilder h1(String text) {
            ct.with(TagCreator.h1(text));
            return this;
        }

        public HtmlMessageBuilder pre(String text) {
            ct.with(TagCreator.pre().withText(text));
            return this;
        }

        public HtmlMessageBuilder p(String text) {
            ct.with(TagCreator.p(text));
            return this;
        }

        public HtmlMessageBuilder b(String text) {
            ct.with(TagCreator.b(text));
            return this;
        }

        public HtmlMessageBuilder br() {
            ct.with(TagCreator.br());
            return this;
        }

        public HtmlMessage build() {
            return new HtmlMessage(this);
        }
    }
}
