package com.danielrubiano.userLoginRegistration.registration;

import com.danielrubiano.userLoginRegistration.appuser.AppUser;
import com.danielrubiano.userLoginRegistration.appuser.AppUserRole;
import com.danielrubiano.userLoginRegistration.appuser.AppUserService;
import com.danielrubiano.userLoginRegistration.registration.token.ConfirmationToken;
import com.danielrubiano.userLoginRegistration.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

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

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token Not Found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email Already Confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token Expired");
        }

        confirmationTokenService.setConfirmedAt(token);
//        appUserService.enableAppUser(
//                confirmationToken.getAppUser()
//        );
        return "Confirmed";
    }
}
