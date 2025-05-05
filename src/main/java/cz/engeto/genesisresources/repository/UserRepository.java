package cz.engeto.genesisresources.repository;

import cz.engeto.genesisresources.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPersonID(String personID);
}
