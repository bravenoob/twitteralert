package ch.bfh.bigdata.semarbeit.twitteralert.web.rest;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.WatchChannel;
import ch.bfh.bigdata.semarbeit.twitteralert.service.WatchChannelService;
import ch.bfh.bigdata.semarbeit.twitteralert.web.rest.errors.BadRequestAlertException;
import ch.bfh.bigdata.semarbeit.twitteralert.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WatchChannel.
 */
@RestController
@RequestMapping("/api")
public class WatchChannelResource {

    private final Logger log = LoggerFactory.getLogger(WatchChannelResource.class);

    private static final String ENTITY_NAME = "watchChannel";

    private final WatchChannelService watchChannelService;

    public WatchChannelResource(WatchChannelService watchChannelService) {
        this.watchChannelService = watchChannelService;
    }

    /**
     * POST  /watch-channels : Create a new watchChannel.
     *
     * @param watchChannel the watchChannel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new watchChannel, or with status 400 (Bad Request) if the watchChannel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/watch-channels")
    @Timed
    public ResponseEntity<WatchChannel> createWatchChannel(@Valid @RequestBody WatchChannel watchChannel) throws URISyntaxException {
        log.debug("REST request to save WatchChannel : {}", watchChannel);
        if (watchChannel.getId() != null) {
            throw new BadRequestAlertException("A new watchChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WatchChannel result = watchChannelService.save(watchChannel);
        return ResponseEntity.created(new URI("/api/watch-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /watch-channels : Updates an existing watchChannel.
     *
     * @param watchChannel the watchChannel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated watchChannel,
     * or with status 400 (Bad Request) if the watchChannel is not valid,
     * or with status 500 (Internal Server Error) if the watchChannel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/watch-channels")
    @Timed
    public ResponseEntity<WatchChannel> updateWatchChannel(@Valid @RequestBody WatchChannel watchChannel) throws URISyntaxException {
        log.debug("REST request to update WatchChannel : {}", watchChannel);
        if (watchChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WatchChannel result = watchChannelService.save(watchChannel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, watchChannel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /watch-channels : get all the watchChannels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of watchChannels in body
     */
    @GetMapping("/watch-channels")
    @Timed
    public List<WatchChannel> getAllWatchChannels() {
        log.debug("REST request to get all WatchChannels");
        return watchChannelService.findAll();
    }

    /**
     * GET  /watch-channels/:id : get the "id" watchChannel.
     *
     * @param id the id of the watchChannel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the watchChannel, or with status 404 (Not Found)
     */
    @GetMapping("/watch-channels/{id}")
    @Timed
    public ResponseEntity<WatchChannel> getWatchChannel(@PathVariable Long id) {
        log.debug("REST request to get WatchChannel : {}", id);
        Optional<WatchChannel> watchChannel = watchChannelService.findById(id);
        return ResponseUtil.wrapOrNotFound(watchChannel);
    }

    /**
     * DELETE  /watch-channels/:id : delete the "id" watchChannel.
     *
     * @param id the id of the watchChannel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/watch-channels/{id}")
    @Timed
    public ResponseEntity<Void> deleteWatchChannel(@PathVariable Long id) {
        log.debug("REST request to delete WatchChannel : {}", id);
        watchChannelService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
