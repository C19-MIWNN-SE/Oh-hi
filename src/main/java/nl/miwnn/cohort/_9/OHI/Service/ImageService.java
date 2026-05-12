package nl.miwnn.cohort._9.OHI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort._9.OHI.Model.Image;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.ImageRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

        Image img = new Image();
        img.setFilename(file.getOriginalFilename());
        img.setContentType(file.getContentType());
        img.setAlttxt(file.getOriginalFilename());
        img.setData(file.getBytes());

        return imageRepository.save(img);
    }

    public ResponseEntity<byte[]> serveImage(Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId).orElse(null);

        if (image != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getContentType()))
                    .body(image.getData());
        }

        return serveFallback("noImage.jpg");
    }

    public ResponseEntity<byte[]> serveProfileImage(Long personId) throws IOException {
        Person person = personRepository.findById(personId).orElse(null);

        //for when the user uploads their own image
        if (person != null && person.getProfileImage() != null) {
            Image img = person.getProfileImage();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(img.getContentType()))
                    .body(img.getData());
        }

        //based on the generated images paired to numbers for demo
        String idBased = personId + ".jpg";
        if (resourceExists(idBased)) {
            return serveFallback(idBased);
        }
        //if number outside that of amount of generated defaults then return black profile default
        return serveFallback("noImage.jpg");
    }

    private boolean resourceExists(String filename) {
        return new ClassPathResource("static/static-images/defaultPics/" + filename).exists();
    }

    private ResponseEntity<byte[]> serveFallback(String filename) throws IOException {
        ClassPathResource resource =
                new ClassPathResource("static/static-images/defaultPics/" + filename);

        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
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
        return imageRepository.findById(id).orElse(null);
    }


}
