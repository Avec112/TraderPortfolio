package no.avec.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twitter4j.*;

/**
 * Created by ronny.ness on 12/12/15.
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
