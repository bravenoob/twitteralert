package ch.bfh.bigdata.semarbeit.twitteralert.repository;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
