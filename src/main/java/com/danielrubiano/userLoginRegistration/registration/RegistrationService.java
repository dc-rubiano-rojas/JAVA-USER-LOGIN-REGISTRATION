package com.danielrubiano.userLoginRegistration.registration;

import com.danielrubiano.userLoginRegistration.appuser.AppUser;
import com.danielrubiano.userLoginRegistration.appuser.AppUserRole;
import com.danielrubiano.userLoginRegistration.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isValid = emailValidator.
                test(request.getEmail());

        if(!isValid) throw new IllegalStateException("Email Not Valid");

        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }
}
