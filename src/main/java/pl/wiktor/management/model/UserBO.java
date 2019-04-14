package pl.wiktor.management.model;

import lombok.Builder;
import lombok.Data;

@Data
public class UserBO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
