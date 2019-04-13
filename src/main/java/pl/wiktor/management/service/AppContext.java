package pl.wiktor.management.service;

import lombok.Data;
import org.springframework.stereotype.Component;
import pl.wiktor.management.model.UserBO;

@Data
@Component
public class AppContext {

    private UserBO authenticatedUser;
    private UserBO userToEdit;

}
