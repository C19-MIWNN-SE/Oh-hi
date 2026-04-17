package nl.miwnn.cohort._9.OHI.Controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Image;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.ImageRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author INT Developers
 * Controller for Person - how people will be displayed
 */
@Controller
@RequestMapping("profiles")
public class PersonController {
    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonRepository personRepository;
    private final PersonService personService;
    private final CohortRepository cohortRepository;
    private final ImageRepository imageRepository;

    public PersonController(PersonRepository personRepository, PersonService personService, CohortRepository cohortRepository, ImageRepository imageRepository) {
        this.personRepository = personRepository;
        this.personService = personService;
        this.cohortRepository = cohortRepository;
        this.imageRepository = imageRepository;
    }

@GetMapping("")
public String showPeople(Model model) {
    List<Person> people = personRepository.findAll();
    List<Cohort> cohorts = cohortRepository.findAll();

    log.debug("person overview requested");
    model.addAttribute("people", people);
    model.addAttribute("allCohorts", cohorts);

    return "PersonenOverview";
}


/* Oh hi Mees */
@GetMapping("/add")
public String addPersonToCohort(Model model){
    model.addAttribute("person", new Person());

    return "add-edit-form";
}

@PostMapping("/save")
public String saveMemberToCohort(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult, RedirectAttributes redirectAttributes){
    if (personService.personAlreadyExists(person)){
            bindingResult.rejectValue("firstName", "alreadyExists", "Dit persoon bestaat al");
    }

    if (bindingResult.hasErrors()) {
        return "add-edit-form";
    }

    try {
        personService.saveMemberToCohort(person);
    } catch (Exception exception){
        redirectAttributes.addFlashAttribute("Dit persoon kon niet worden opgeslagen");
    }

    redirectAttributes.addFlashAttribute("successMessage", "Het persoon is succesvol opgeslagen!");
    return "redirect:/profiles";
}

@GetMapping("/remove/{id}")
public String deleteMemberFromCohort(@PathVariable Long id, RedirectAttributes redirectAttributes){

    try {
        personService.deleteMemberFromCohort(id);
        redirectAttributes.addFlashAttribute("successMessage",  "Het persoon is succesvol verwijderd");
    } catch (Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", "Het persoon kon niet verwijderd worden.");
    }

    return "redirect:/profiles";
}


@GetMapping("/{id}")
public String showProfile(@PathVariable Long id ,Model model, RedirectAttributes redirectAttributes){

    Person person = personRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Persoon bestaat niet " + id));

    log.info("De pagina wordt geladen");

    model.addAttribute("name", String.format("Oh hi %s!", person.getFullName()));
    model.addAttribute("aboutMe", person.getAboutMe());
    model.addAttribute("userRole", person.getEnumToLowerCase(person.getUserRole()));
    model.addAttribute("person", person);
    return "PersonProfile";
}

    @GetMapping("/editAbout/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persoon bestaat niet " + id));
        log.info("Bewerkformulier geopend voor: {}", id);
        model.addAttribute("person", personService.findById(id));
        return "add-edit-aboutme";
}

//    @PostMapping("/save")
//    public String saveFilm
//            (@Valid @ModelAttribute
//             Film film, BindingResult bindingResult, Model model, @RequestParam("leadActor")
//             Long leadActorId,RedirectAttributes redirectAttributes,
//             @RequestParam("imageFile") MultipartFile imageFile) throws IOException  {
//        if (film.getId() != null) {
//            Film existingFilm = filmService.getFilmById(film.getId());

//Save results of add/edit to about me information
@PostMapping("/saveAboutMe")
public String saveAboutMe(
        @ModelAttribute("person") Person aboutPerson,
        @RequestParam("imageFile") MultipartFile imageFile
//        @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage
) throws IOException {

    // Load existing person
    Person profilePerson = personService.findById(aboutPerson.getId());

    if (profilePerson == null) {
        throw new IllegalStateException("No person found with ID " + aboutPerson.getId());
    }

    // Update fields
    profilePerson.setAboutMe(aboutPerson.getAboutMe());

//    if (deleteImage) {
//        profilePerson.setImage(null);
//    }
    // Replace image if a new one was uploaded
    if (!imageFile.isEmpty()) {
        Image image = new Image();
        image.setData(imageFile.getBytes());
        image.setContentType(imageFile.getContentType());
        imageRepository.save(image);
        profilePerson.setImage(image);
    }

    // Save updated person
    personService.savePerson(profilePerson);

    return "redirect:/profiles/" + profilePerson.getId();
}


//    @PostMapping("/saveAboutMe")
//    public String saveAboutMe(@Valid @ModelAttribute("person") Person aboutPerson,
//                              BindingResult bindingResult,
//                              RedirectAttributes redirectAttributes,
//                              @RequestParam("imageFile") MultipartFile imageFile,
//                              @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage) throws IOException {
//    log.info("AboutMe opslaan voor {}", aboutPerson.getFullName());
//
//    if (bindingResult.hasErrors()){
//        log.warn("Error bij opslaan voor {}", bindingResult.getErrorCount());
//        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.aboutPerson", bindingResult);
//        redirectAttributes.addFlashAttribute("aboutPerson", aboutPerson);
//        return "redirect:/profiles/" + aboutPerson.getId();
//    }
//    if(aboutPerson.getId() != null){
//        Person profilePerson = personService.findById(aboutPerson.getId());
//
//        // Copy updated fields
//        profilePerson.setAboutMe(aboutPerson.getAboutMe());
//
//        if(imageFile.isEmpty()){
//            Image image = new Image();
//            image.setData(imageFile.getBytes());
//            image.setContentType(imageFile.getContentType());
//            imageRepository.save(image);
//            profilePerson.setImage(image);
//        }
//        else if (deleteImage){
//            aboutPerson.setImage(null);
//        }
//
//        personService.savePerson(profilePerson);
//        log.info("Aboutme van {} bijgewerkt", aboutPerson.getFullName());
//        return "redirect:/profiles/" + aboutPerson.getId();
//    }
//        if(!imageFile.isEmpty()){
//            Image image = new Image();
//            image.setData(imageFile.getBytes());
//            image.setContentType(imageFile.getContentType());
//            imageRepository.save(image);
//        }
//
//        personService.savePerson(aboutPerson);
//        log.info("Aboutme van {} bijgewerkt", aboutPerson.getFullName());
//        return "redirect:/profiles/" + aboutPerson.getId();
//
//    }

//consider renaming for program longevity
    @PostMapping("/saveProfile")
    public String saveProfile(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        try {
            personService.saveMemberToCohort(person);
        } catch (Exception exception){
            redirectAttributes.addFlashAttribute("Dit persoon kon niet worden opgeslagen");
        }

        redirectAttributes.addFlashAttribute("successMessage", "Het persoon is succesvol opgeslagen!");
        return "redirect:/profiles";
    }

}
