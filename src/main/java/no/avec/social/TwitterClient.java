package no.avec.social;

import twitter4j.*;

/**
 * Created by ronny.ness on 12/12/15.
 */
public class TwitterClient {
    public void sendMsg(String recipientId, String msg) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        //Status status = twitter.updateStatus(msg);
        //System.out.println("Successfully updated the status to [" + status.getText() + "].");
        DirectMessage message = twitter.sendDirectMessage(recipientId, msg);
        System.out.println("Sent: " + msg + " to @" + message.getRecipientScreenName());
    }

    public static void main(String[] args) throws TwitterException {
        TwitterClient client = new TwitterClient();
        client.sendMsg("RonnyNaess", "Det virker! Hilsen Twitter4j");
    }

}
