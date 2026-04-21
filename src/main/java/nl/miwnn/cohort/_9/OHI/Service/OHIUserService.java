package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.OHIUserRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author INT Development
 * Service class for finding a user
 */

@Service
public class OHIUserService implements UserDetailsService {
    private final OHIUserRepository ohiUserRepository;
    private final PersonRepository personRepository;

    public OHIUser createAccount (Person person, String role) {
        return new OHIUser(usernameCreation(person),"password", role);
        // Onderstaande methode genereert een random wachtwoord, maar voor nu is hetzelfde wachtwoord gebruiken handig
        // UUID.randomUUID().toString()
    }

    public String usernameCreation(Person person) {
        int newCountUsername = personRepository.countPeopleByFirstName(person.getFirstName()) + 1;
        return person.getFirstName() + newCountUsername;
    }

    public OHIUserService(OHIUserRepository ohiUserRepository, PersonRepository personRepository) {
        this.ohiUserRepository = ohiUserRepository;
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return ohiUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Gebruiker niet gevonden: " + username));
    }
}
