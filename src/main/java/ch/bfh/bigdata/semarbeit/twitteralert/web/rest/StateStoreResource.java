package ch.bfh.bigdata.semarbeit.twitteralert.web.rest;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * StateStoreResource controller
 */
@RestController
@RequestMapping("/api/state-store")
public class StateStoreResource {

    private final Logger log = LoggerFactory.getLogger(StateStoreResource.class);
    private KafkaStreams kafkaStreams;

    public StateStoreResource(KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
    }

    /**
     * GET getWordCount
     */
    @GetMapping("/get-word-count")
    public String getWordCount() {
        ReadOnlyKeyValueStore<String, Long> keyValueStore =
            kafkaStreams.store("counts-store", QueryableStoreTypes.keyValueStore());

        return keyValueStore.approximateNumEntries() + "";
    }

}
