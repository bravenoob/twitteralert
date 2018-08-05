package ch.bfh.bigdata.semarbeit.twitteralert.repository;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.WatchReceiver;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WatchReceiver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WatchReceiverRepository extends JpaRepository<WatchReceiver, Long> {

}
