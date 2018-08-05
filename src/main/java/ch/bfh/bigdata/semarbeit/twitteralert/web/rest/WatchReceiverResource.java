package ch.bfh.bigdata.semarbeit.twitteralert.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.bfh.bigdata.semarbeit.twitteralert.domain.WatchReceiver;
import ch.bfh.bigdata.semarbeit.twitteralert.repository.WatchReceiverRepository;
import ch.bfh.bigdata.semarbeit.twitteralert.web.rest.errors.BadRequestAlertException;
import ch.bfh.bigdata.semarbeit.twitteralert.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WatchReceiver.
 */
@RestController
@RequestMapping("/api")
public class WatchReceiverResource {

    private final Logger log = LoggerFactory.getLogger(WatchReceiverResource.class);

    private static final String ENTITY_NAME = "watchReceiver";

    private final WatchReceiverRepository watchReceiverRepository;

    public WatchReceiverResource(WatchReceiverRepository watchReceiverRepository) {
        this.watchReceiverRepository = watchReceiverRepository;
    }

    /**
     * POST  /watch-receivers : Create a new watchReceiver.
     *
     * @param watchReceiver the watchReceiver to create
     * @return the ResponseEntity with status 201 (Created) and with body the new watchReceiver, or with status 400 (Bad Request) if the watchReceiver has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/watch-receivers")
    @Timed
    public ResponseEntity<WatchReceiver> createWatchReceiver(@RequestBody WatchReceiver watchReceiver) throws URISyntaxException {
        log.debug("REST request to save WatchReceiver : {}", watchReceiver);
        if (watchReceiver.getId() != null) {
            throw new BadRequestAlertException("A new watchReceiver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WatchReceiver result = watchReceiverRepository.save(watchReceiver);
        return ResponseEntity.created(new URI("/api/watch-receivers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /watch-receivers : Updates an existing watchReceiver.
     *
     * @param watchReceiver the watchReceiver to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated watchReceiver,
     * or with status 400 (Bad Request) if the watchReceiver is not valid,
     * or with status 500 (Internal Server Error) if the watchReceiver couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/watch-receivers")
    @Timed
    public ResponseEntity<WatchReceiver> updateWatchReceiver(@RequestBody WatchReceiver watchReceiver) throws URISyntaxException {
        log.debug("REST request to update WatchReceiver : {}", watchReceiver);
        if (watchReceiver.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WatchReceiver result = watchReceiverRepository.save(watchReceiver);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, watchReceiver.getId().toString()))
            .body(result);
    }

    /**
     * GET  /watch-receivers : get all the watchReceivers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of watchReceivers in body
     */
    @GetMapping("/watch-receivers")
    @Timed
    public List<WatchReceiver> getAllWatchReceivers() {
        log.debug("REST request to get all WatchReceivers");
        return watchReceiverRepository.findAll();
    }

    /**
     * GET  /watch-receivers/:id : get the "id" watchReceiver.
     *
     * @param id the id of the watchReceiver to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the watchReceiver, or with status 404 (Not Found)
     */
    @GetMapping("/watch-receivers/{id}")
    @Timed
    public ResponseEntity<WatchReceiver> getWatchReceiver(@PathVariable Long id) {
        log.debug("REST request to get WatchReceiver : {}", id);
        Optional<WatchReceiver> watchReceiver = watchReceiverRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(watchReceiver);
    }

    /**
     * DELETE  /watch-receivers/:id : delete the "id" watchReceiver.
     *
     * @param id the id of the watchReceiver to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/watch-receivers/{id}")
    @Timed
    public ResponseEntity<Void> deleteWatchReceiver(@PathVariable Long id) {
        log.debug("REST request to delete WatchReceiver : {}", id);

        watchReceiverRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
