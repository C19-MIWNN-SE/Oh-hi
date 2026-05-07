package nl.miwnn.cohort._9.OHI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort._9.OHI.Model.Image;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.ImageRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
@Service
public class ImageService {

    @Autowired private ImageRepository imageRepository;
    @Autowired private PersonRepository personRepository;

    public Image storeImage(MultipartFile file) throws IOException {

        Path uploadDir = Paths.get("static-images");
        Files.createDirectories(uploadDir);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = uploadDir.resolve(filename);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        Image img = new Image();
        img.setFilename(filename);
        img.setUrl("/images/" + filename);
        img.setContentType(file.getContentType());
        img.setAlttxt(filename);

        img.setData(file.getBytes());

        return imageRepository.save(img);
    }

    public void singleImageUpload(MultipartFile file, Long personId) throws IOException{
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("person not found"));
        Image img = storeImage(file);

        person.getImages().add(img);
        personRepository.save(person);
    }

    public void groupImageUpload(MultipartFile file, List<Long> personIds) throws IOException{
        Image img = storeImage(file);

        List<Person> people = personRepository.findAllById(personIds);
        for (Person person : people){
            person.getImages().add(img);
        }
        personRepository.saveAll(people);
    }

    public Image findById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Foto %d niet gevonden", id)));
    }
}
