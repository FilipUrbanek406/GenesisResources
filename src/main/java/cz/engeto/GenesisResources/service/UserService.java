package cz.engeto.GenesisResources.service;

import cz.engeto.GenesisResources.exception.FileException;
import cz.engeto.GenesisResources.model.User;
import cz.engeto.GenesisResources.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private List<String> availablePersonIDs = new ArrayList<>();

    public UserService() {
        try {
            readPersonID("resources/dataPersonId.txt");
        } catch (FileException e) {
            System.err.println("Nepodařilo se načíst personID: " + e.getMessage());
        }
    }

    public User addUser(User user) throws IllegalArgumentException {
        if (isPersonIDAlreadyUsed(user.getPersonID())) {
            throw new IllegalArgumentException("PersonID již existuje v databázi!");
        }

        if (!availablePersonIDs.contains(user.getPersonID())) {
            throw new IllegalArgumentException("Zadané personID není platné!");
        }

        String uuid = UUID.randomUUID().toString();
        user.setUuid(uuid);

        return userRepository.save(user);
    }

    public void readPersonID(String fileName) throws FileException {
        int lineNumber = 0;
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    availablePersonIDs.add(line);
                }
            }

        } catch (FileNotFoundException e) {
            throw new FileException("Nepodařilo se nalézt soubor: " + fileName + "!");
        }
    }

    public boolean isPersonIDAlreadyUsed(String personID) {
        return userRepository.findByPersonID(personID).isPresent();
    }

    public boolean isPersonIDValid(String personID) {
        return availablePersonIDs.contains(personID);
    }

    public void saveUser(User user) {
        String uuid = UUID.randomUUID().toString();
        user.setUuid(uuid);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {

        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }
        return user;
    }
}
