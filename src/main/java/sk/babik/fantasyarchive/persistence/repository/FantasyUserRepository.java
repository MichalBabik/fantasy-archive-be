package sk.babik.fantasyarchive.persistence.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.babik.fantasyarchive.persistence.model.FantasyUser;

import java.util.Optional;

@Repository
public interface FantasyUserRepository extends JpaRepository<FantasyUser, Long> {

    @Override
    void deleteById(Long aLong);

    @Override
    boolean existsById(Long aLong);

    @Override
    Optional<FantasyUser> findById(Long aLong);
}
