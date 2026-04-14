package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Mees Drenth
 * Uitleg
 */

@Service
public class PersonService {

    public void testList(){
        List<Person> people = new ArrayList<>();
        people.add(new Person(1L, "Mark", "", "Satero", null, null));
    }
}
