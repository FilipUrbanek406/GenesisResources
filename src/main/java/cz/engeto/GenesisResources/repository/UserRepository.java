package cz.engeto.GenesisResources.repository;

import cz.engeto.GenesisResources.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPersonID(String personID);
}
