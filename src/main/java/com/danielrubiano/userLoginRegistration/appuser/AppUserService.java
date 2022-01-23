package com.danielrubiano.userLoginRegistration.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "User With Email %s Not Found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appuser) {
        boolean userExists = appUserRepository
                .findByEmail(appuser.getEmail())
                .isPresent();

        if(userExists) throw  new IllegalStateException("Email already taken");

        String encodedPassword = bCryptPasswordEncoder
                .encode(appuser.getPassword());

        appuser.setPasword(encodedPassword);

        appUserRepository.save(appuser);

        // TODO: Send confirmation token
        return "It Works";
    }
}
