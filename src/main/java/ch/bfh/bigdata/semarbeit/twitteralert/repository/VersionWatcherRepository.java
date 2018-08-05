package ch.bfh.bigdata.semarbeit.twitteralert.repository;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.VersionWatcher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the VersionWatcher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersionWatcherRepository extends JpaRepository<VersionWatcher, Long> {

    @Query("select version_watcher from VersionWatcher version_watcher where version_watcher.user.login = ?#{principal.username}")
    List<VersionWatcher> findByUserIsCurrentUser();

}
