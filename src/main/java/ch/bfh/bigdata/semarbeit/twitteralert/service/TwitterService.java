package ch.bfh.bigdata.semarbeit.twitteralert.service;

import ch.bfh.bigdata.semarbeit.twitteralert.config.ApplicationProperties;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;


@Service
public class TwitterService {
    private final Logger log = LoggerFactory.getLogger(TwitterService.class);

    private final ApplicationProperties.Twitter twitter;
    private KafkaTemplate kafkaTemplate;

    private Client hosebirdClient;
    private BlockingQueue<String> msgQueue;


    public TwitterService(ApplicationProperties applicationProperties, KafkaTemplate kafkaTemplate) {
        this.twitter = applicationProperties.getTwitter();
        this.kafkaTemplate = kafkaTemplate;
        msgQueue = new LinkedBlockingQueue<>(10000);
    }

    private void init(List<Long> followings) {
        ClientBuilder builder = initTwitter(followings);
        if (hosebirdClient != null) {
            hosebirdClient.stop();
        }
        hosebirdClient = builder.build();
        hosebirdClient.connect();
    }

    private ClientBuilder initTwitter(List<Long> followings) {
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        hosebirdEndpoint.followings(followings);
        //hosebirdEndpoint.trackTerms(terms);
        Authentication hosebirdAuth = new OAuth1(twitter.getConsumerKey(), twitter.getConsumerSecret(), twitter.getToken(), twitter.getSecret());
        return new ClientBuilder()
            .name("Hosebird-Client-01")
            .hosts(hosebirdHosts)
            .authentication(hosebirdAuth)
            .endpoint(hosebirdEndpoint)
            .processor(new StringDelimitedProcessor(msgQueue));
    }

    public void setTwitterFollwings(List<String> profiles) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(twitter.getConsumerKey())
            .setOAuthConsumerSecret(twitter.getConsumerSecret())
            .setOAuthAccessToken(twitter.getToken())
            .setOAuthAccessTokenSecret(twitter.getSecret());
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        List<Long> userIds = profiles.stream().map(user -> {
            try {
                return twitter.showUser(user).getId();
            } catch (TwitterException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        init(userIds);
    }

    @Async
    public void executeAsynchronously() {
        while (!hosebirdClient.isDone()) {
            try {
                String tweet = msgQueue.take();
                log.debug("sending message='{}' to topic='{}'", tweet, kafkaTemplate.getDefaultTopic());
                kafkaTemplate.sendDefault(tweet);
            } catch (InterruptedException e) {
                log.error("Problem with twitter client", e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        hosebirdClient.stop();
    }
}
