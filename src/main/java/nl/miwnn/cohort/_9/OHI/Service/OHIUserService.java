package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import nl.miwnn.cohort._9.OHI.Repository.OHIUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Banic
 * Service class for finding a user
 */


public class OHIUserService {
    private final OHIUserRepository ohiUserRepository;

    public OHIUserService(OHIUserRepository ohiUserRepository) {
        this.ohiUserRepository = ohiUserRepository;
    }

    public void createTestUser () {
        OHIUser testUser = new OHIUser(1L, "user", "test");
        ohiUserRepository.save(testUser);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return ohiUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
//                        "Gebruiker niet gevonden: " + username));
//    }
}
