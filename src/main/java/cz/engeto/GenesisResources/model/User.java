package cz.engeto.GenesisResources.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @Column(length = 12, unique = true)
    private String personID;
    @Column(unique = true)
    private String uuid;
}
