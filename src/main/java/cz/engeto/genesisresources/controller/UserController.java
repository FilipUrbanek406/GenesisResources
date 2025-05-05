package cz.engeto.genesisresources.controller;

import cz.engeto.genesisresources.DTO.UserBasicDTO;
import cz.engeto.genesisresources.DTO.UserDetailDTO;
import cz.engeto.genesisresources.model.User;
import cz.engeto.genesisresources.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (userService.isPersonIDAlreadyUsed(user.getPersonID())) {
            return new ResponseEntity<>("PersonID již použito. Uživatel nelze vytvořit", HttpStatus.BAD_REQUEST);
        }

        if (!userService.isPersonIDValid(user.getPersonID())) {
            return new ResponseEntity<>("Zadané personID není platné. Uživatel nelze vytvořit", HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) Boolean detail) {
        List<User> users = userService.getAllUsers();

        if (Boolean.TRUE.equals(detail)) {
            List<UserDetailDTO> detailDTOs = new ArrayList<>();

            for (User user : users) {
                UserDetailDTO detailDTO = userService.convertToDetailDTO(user);
                detailDTOs.add(detailDTO);
            }
            return new ResponseEntity<>(detailDTOs, HttpStatus.OK);
        } else {
            List<UserBasicDTO> basicDTOs = new ArrayList<>();

            for (User user : users) {
                UserBasicDTO basicDTO = userService.convertToBasicDTO(user);
                basicDTOs.add(basicDTO);
            }
            return new ResponseEntity<>(basicDTOs, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id,
                                         @RequestParam(required = false) Boolean detail) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Uživatel s tímto ID neexistuje", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        if (Boolean.TRUE.equals(detail)) {
            UserDetailDTO detailDTO = userService.convertToDetailDTO(user);
            return new ResponseEntity<>(detailDTO, HttpStatus.OK);
        } else {
            UserBasicDTO basicDTO = userService.convertToBasicDTO(user);
            return new ResponseEntity<>(basicDTO, HttpStatus.OK);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserBasicDTO userDTO) {
        Optional<User> existingUserOptional = userService.getUserById(userDTO.getId());

        if (existingUserOptional.isEmpty()) {
            return new ResponseEntity<>("Uživatel s tímto ID neexistuje", HttpStatus.NOT_FOUND);
        }

        User existingUser = existingUserOptional.get();
        existingUser.setName(userDTO.getName());
        existingUser.setSurname(userDTO.getSurname());

        User updatedUser = userService.updateUser(existingUser);

        UserBasicDTO responseDTO = new UserBasicDTO();
        responseDTO.setId(updatedUser.getId());
        responseDTO.setName(updatedUser.getName());
        responseDTO.setSurname(updatedUser.getSurname());

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        Optional<User> user = userService.deleteUserById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>("Uživatel byl smazán", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Uživatel s tímto ID neexistuje", HttpStatus.NOT_FOUND);
        }
    }
}
