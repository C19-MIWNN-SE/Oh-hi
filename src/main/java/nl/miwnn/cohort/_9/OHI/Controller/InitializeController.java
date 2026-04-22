package nl.miwnn.cohort._9.OHI.Controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.OHIUserRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * @author INT Developers
 * Initialize databade when the application is started empty
 */

@Controller
public class InitializeController {
    private final Logger log = LoggerFactory.getLogger(InitializeController.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private OHIUserRepository ohiUserRepository;

    public InitializeController(OHIUserRepository ohiUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.ohiUserRepository = ohiUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

   @EventListener(ContextRefreshedEvent.class)
    public void seed(){
        if(ohiUserRepository.count() == 0) {
            seedUsers();
        }
    }

    //todo
    private void seedUsers() {
        if (ohiUserRepository.count() == 0) {
            OHIUser docent = new OHIUser("docent", passwordEncoder.encode("docent"), "DOCENT");
            OHIUser student = new OHIUser("student", passwordEncoder.encode("student"), "STUDENT");
            ohiUserRepository.save(docent);
            ohiUserRepository.save(student);
        }
    }
}
