package ch.bfh.bigdata.semarbeit.twitteralert.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Twitteralert.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Twitter twitter = new Twitter();

    public Twitter getTwitter() {
        return twitter;
    }

    public static class Twitter {

        private String consumerKey, consumerSecret, token,secret;

        public String getConsumerKey() {
            return consumerKey;
        }

        public void setConsumerKey(String consumerKey) {
            this.consumerKey = consumerKey;
        }

        public String getConsumerSecret() {
            return consumerSecret;
        }

        public void setConsumerSecret(String consumerSecret) {
            this.consumerSecret = consumerSecret;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }

}
