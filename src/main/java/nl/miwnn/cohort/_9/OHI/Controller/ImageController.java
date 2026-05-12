package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Image;
import nl.miwnn.cohort._9.OHI.Repository.ImageRepository;
import nl.miwnn.cohort._9.OHI.Service.ImageService;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * @author INT Developers
 * Controller for adding profile images
 */
@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/profile-image/{personId}")
    public ResponseEntity<byte[]> profileImage(@PathVariable Long personId) throws IOException {
        return imageService.serveProfileImage(personId);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> image(@PathVariable Long imageId) throws IOException {
        return imageService.serveImage(imageId);
    }


}
