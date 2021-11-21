package pl.ftims.ias.your_climbing_gym.moch.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.ftims.ias.your_climbing_gym.exceptions.UploadFileException;
import pl.ftims.ias.your_climbing_gym.moch.services.CloudService;

@RestController
@RequestMapping("image")
public class CloudEndpoint {

    @Autowired
    private CloudService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) throws UploadFileException {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }
}
