package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Repository.OHIUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Banic
 * Service class for finding a user
 */

@Service
public class OHIUserService implements UserDetailsService {
    private final OHIUserRepository ohiUserRepository;

    public OHIUserService(OHIUserRepository ohiUserRepository) {
        this.ohiUserRepository = ohiUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return ohiUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Gebruiker niet gevonden: " + username));
    }
}
