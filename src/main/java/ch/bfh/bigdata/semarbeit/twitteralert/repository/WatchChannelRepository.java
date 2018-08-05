package ch.bfh.bigdata.semarbeit.twitteralert.repository;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.WatchChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WatchChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WatchChannelRepository extends JpaRepository<WatchChannel, Long> {

}
