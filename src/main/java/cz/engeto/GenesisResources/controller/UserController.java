package cz.engeto.GenesisResources.controller;

import cz.engeto.GenesisResources.DTO.UserBasicDTO;
import cz.engeto.GenesisResources.DTO.UserDetailDTO;
import cz.engeto.GenesisResources.model.User;
import cz.engeto.GenesisResources.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            User savedUser = userService.addUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("personID již použito, uživatel nelze vytvořit",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id,
                                         @RequestParam(required = false) Boolean detail) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();

        if (Boolean.TRUE.equals(detail)) {
            UserDetailDTO detailDTO = convertToDetailDTO(user);
            return ResponseEntity.ok(detailDTO);
        } else {
            UserBasicDTO basicDTO = convertToBasicDTO(user);
            return ResponseEntity.ok(basicDTO);
        }
    }

    private UserBasicDTO convertToBasicDTO(User user) {
        UserBasicDTO dto = new UserBasicDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        return dto;
    }

    private UserDetailDTO convertToDetailDTO(User user) {
        UserDetailDTO dto = new UserDetailDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setPersonID(user.getPersonID());
        dto.setUuid(user.getUuid());
        return dto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        Optional<User> user = userService.deleteUserById(id);

        if (user.isPresent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
