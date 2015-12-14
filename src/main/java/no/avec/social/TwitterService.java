package no.avec.social;

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

    @Value("${twitter.recipientId}")
    private String twitterRecipientId;

    public void sendDirectMessage(String recipientId, String msg) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        DirectMessage message = twitter.sendDirectMessage(recipientId, msg);
        // TODO log
//        System.out.println("Sent: " + msg + " to @" + message.getRecipientScreenName());
    }

}
