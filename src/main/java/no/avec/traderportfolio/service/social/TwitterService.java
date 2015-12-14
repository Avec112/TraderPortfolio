package no.avec.traderportfolio.service.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by avec on 12/12/15.
 */
@Component
public class TwitterService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${twitter.recipientId}")
    private String twitterRecipientId;

    public void sendDirectMessage(String msg) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        DirectMessage message = twitter.sendDirectMessage(twitterRecipientId, msg);

        LOG.debug("Sent: {} to @{}", msg, message.getRecipientScreenName());
    }

}
