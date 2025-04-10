package cz.engeto.GenesisResources.service;

import cz.engeto.GenesisResources.model.User;
import cz.engeto.GenesisResources.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {

        return userRepository.findById(id);
    }
}
