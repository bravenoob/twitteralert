package ch.bfh.bigdata.semarbeit.twitteralert.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.bfh.bigdata.semarbeit.twitteralert.domain.VersionWatcher;
import ch.bfh.bigdata.semarbeit.twitteralert.repository.VersionWatcherRepository;
import ch.bfh.bigdata.semarbeit.twitteralert.web.rest.errors.BadRequestAlertException;
import ch.bfh.bigdata.semarbeit.twitteralert.web.rest.util.HeaderUtil;
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
 * REST controller for managing VersionWatcher.
 */
@RestController
@RequestMapping("/api")
public class VersionWatcherResource {

    private final Logger log = LoggerFactory.getLogger(VersionWatcherResource.class);

    private static final String ENTITY_NAME = "versionWatcher";

    private final VersionWatcherRepository versionWatcherRepository;

    public VersionWatcherResource(VersionWatcherRepository versionWatcherRepository) {
        this.versionWatcherRepository = versionWatcherRepository;
    }

    /**
     * POST  /version-watchers : Create a new versionWatcher.
     *
     * @param versionWatcher the versionWatcher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new versionWatcher, or with status 400 (Bad Request) if the versionWatcher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/version-watchers")
    @Timed
    public ResponseEntity<VersionWatcher> createVersionWatcher(@Valid @RequestBody VersionWatcher versionWatcher) throws URISyntaxException {
        log.debug("REST request to save VersionWatcher : {}", versionWatcher);
        if (versionWatcher.getId() != null) {
            throw new BadRequestAlertException("A new versionWatcher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VersionWatcher result = versionWatcherRepository.save(versionWatcher);
        return ResponseEntity.created(new URI("/api/version-watchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /version-watchers : Updates an existing versionWatcher.
     *
     * @param versionWatcher the versionWatcher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated versionWatcher,
     * or with status 400 (Bad Request) if the versionWatcher is not valid,
     * or with status 500 (Internal Server Error) if the versionWatcher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/version-watchers")
    @Timed
    public ResponseEntity<VersionWatcher> updateVersionWatcher(@Valid @RequestBody VersionWatcher versionWatcher) throws URISyntaxException {
        log.debug("REST request to update VersionWatcher : {}", versionWatcher);
        if (versionWatcher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VersionWatcher result = versionWatcherRepository.save(versionWatcher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, versionWatcher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /version-watchers : get all the versionWatchers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of versionWatchers in body
     */
    @GetMapping("/version-watchers")
    @Timed
    public List<VersionWatcher> getAllVersionWatchers() {
        log.debug("REST request to get all VersionWatchers");
        return versionWatcherRepository.findAll();
    }

    /**
     * GET  /version-watchers/:id : get the "id" versionWatcher.
     *
     * @param id the id of the versionWatcher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the versionWatcher, or with status 404 (Not Found)
     */
    @GetMapping("/version-watchers/{id}")
    @Timed
    public ResponseEntity<VersionWatcher> getVersionWatcher(@PathVariable Long id) {
        log.debug("REST request to get VersionWatcher : {}", id);
        Optional<VersionWatcher> versionWatcher = versionWatcherRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(versionWatcher);
    }

    /**
     * DELETE  /version-watchers/:id : delete the "id" versionWatcher.
     *
     * @param id the id of the versionWatcher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/version-watchers/{id}")
    @Timed
    public ResponseEntity<Void> deleteVersionWatcher(@PathVariable Long id) {
        log.debug("REST request to delete VersionWatcher : {}", id);

        versionWatcherRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
