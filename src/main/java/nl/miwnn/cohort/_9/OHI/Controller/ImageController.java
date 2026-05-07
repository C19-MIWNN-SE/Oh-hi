package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Image;
import nl.miwnn.cohort._9.OHI.Repository.ImageRepository;
import nl.miwnn.cohort._9.OHI.Service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author INT Developers
 * Controller for adding profile images
 */
@Controller
public class ImageController {
    private final ImageService imageService;


    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> showImage(@PathVariable Long id) {
        Image image = imageService.findById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getData());
    }
}
