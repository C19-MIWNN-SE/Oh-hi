package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Interest;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.InterestRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: INT Developers
 * Service to control the interests of either the student or teacher
 */

@Service
public class InterestService {

    private final InterestRepository interestRepository;
    private final PersonRepository personRepository;

    public InterestService(InterestRepository interestRepository, PersonRepository personRepository) {
        this.interestRepository = interestRepository;
        this.personRepository = personRepository;
    }
    public List<Interest> getAllInterests(){
        return interestRepository.findAll();
    }

    public void saveAllInterests(List<Interest> interests) {
        interestRepository.saveAll(interests);
    }


}
