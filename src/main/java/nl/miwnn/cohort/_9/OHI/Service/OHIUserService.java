package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.AccountToken;
import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.AccountTokenRespository;
import nl.miwnn.cohort._9.OHI.Repository.OHIUserRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author INT Development
 * Service class for finding a user
 */

@Service
public class OHIUserService implements UserDetailsService {
    private final OHIUserRepository ohiUserRepository;
    private final PersonRepository personRepository;
    private final AccountTokenRespository accountTokenRespository;
    private final PasswordEncoder encoder;

//    Person testUser = new Person("Hans", "Hans");
//            personRepository.save(testUser);
//    OHIUser docent = new OHIUser("docent", passwordEncoder.encode("docent"), "DOCENT");
//    OHIUser student = new OHIUser("student", passwordEncoder.encode("student"), "STUDENT");
//    OHIUser hans = new OHIUser("hans", passwordEncoder.encode("hans"), "STUDENT", testUser);

    public String createAccount (Person person, String role) {
        OHIUser user = new OHIUser(usernameCreation(person),"password", role, person);
        ohiUserRepository.save(user);

        String token = UUID.randomUUID().toString();
        AccountToken accountToken = new AccountToken(token,user, LocalDateTime.now().plusDays(7));
        accountTokenRespository.save(accountToken);

        return "http://localhost:8080/person/account/setup?token=" + token;
    }

    public String usernameCreation(Person person) {
        int newCountUsername = personRepository.countPeopleByFirstName(person.getFirstName()) + 1;
        return person.getFirstName() + newCountUsername;
    }

    public OHIUserService(OHIUserRepository ohiUserRepository, PersonRepository personRepository, AccountTokenRespository accountTokenRespository, PasswordEncoder encoder) {
        this.ohiUserRepository = ohiUserRepository;
        this.personRepository = personRepository;
        this.accountTokenRespository = accountTokenRespository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return ohiUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Gebruiker niet gevonden: " + username));
    }

    public void updateCredentials(OHIUser user, String username, String password){
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        ohiUserRepository.save(user);
    }

    public void saveUser (OHIUser user) {
        ohiUserRepository.save(user);
    }

    public String getNameOfPerson(OHIUser loggedInUser) {
        if (loggedInUser.getPerson() != null) {
            return loggedInUser.getPerson().getFullName();
        } else if (loggedInUser.getRole().equals("ADMIN")) {
            return "godmode";
        } else return "hacker";
    }
}
