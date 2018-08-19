package ch.bfh.bigdata.semarbeit.twitteralert.web.rest;

import ch.bfh.bigdata.semarbeit.twitteralert.TwitteralertApp;
import org.apache.kafka.streams.KafkaStreams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the StateStoreResource REST controller.
 *
 * @see StateStoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TwitteralertApp.class)
public class StateStoreResourceIntTest {

    private MockMvc restMockMvc;

    @Autowired
    private KafkaStreams kafkaStreams;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        StateStoreResource stateStoreResource = new StateStoreResource(kafkaStreams);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(stateStoreResource)
            .build();
    }

    /**
    * Test getWordCount
    */
    @Test
    public void testGetWordCount() throws Exception {
        restMockMvc.perform(get("/api/state-store/get-word-count"))
            .andExpect(status().isOk());
    }

}
