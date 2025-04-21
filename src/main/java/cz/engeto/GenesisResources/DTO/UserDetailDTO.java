package cz.engeto.GenesisResources.DTO;

import lombok.Data;

@Data
public class UserDetailDTO {
    private Long id;
    private String name;
    private String surname;
    private String personID;
    private String uuid;
}
