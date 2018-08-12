package ch.bfh.bigdata.semarbeit.twitteralert.messaging;

import ch.bfh.bigdata.semarbeit.twitteralert.config.ApplicationProperties;
import com.google.common.collect.Lists;
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

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


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
        init();
    }

    private void init() {
        ClientBuilder builder = initTwitter();
        hosebirdClient = builder.build();
        hosebirdClient.connect();
        executeAsynchronously();
    }

    private ClientBuilder initTwitter() {
        msgQueue = new LinkedBlockingQueue<>(100000);
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList("twitter", "api");
        hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);
        Authentication hosebirdAuth = new OAuth1(twitter.getConsumerKey(), twitter.getConsumerSecret(), twitter.getToken(), twitter.getSecret());
        return new ClientBuilder()
            .name("Hosebird-Client-01")
            .hosts(hosebirdHosts)
            .authentication(hosebirdAuth)
            .endpoint(hosebirdEndpoint)
            .processor(new StringDelimitedProcessor(msgQueue));
    }


    @Async
    public void executeAsynchronously() {
        while (!hosebirdClient.isDone()) {
            try {
                String tweet = msgQueue.take();
                log.info("sending message='{}' to topic='{}'", tweet, kafkaTemplate.getDefaultTopic());
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
