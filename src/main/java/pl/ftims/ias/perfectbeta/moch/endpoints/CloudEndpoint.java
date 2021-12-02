package pl.ftims.ias.perfectbeta.moch.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.ftims.ias.perfectbeta.exceptions.UploadFileException;
import pl.ftims.ias.perfectbeta.moch.services.CloudService;
import pl.ftims.ias.perfectbeta.moch.services.CloudServiceLocal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("image")
public class CloudEndpoint {


    CloudServiceLocal service;

    @Autowired
    public CloudEndpoint(CloudService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam(value = "files") MultipartFile[] files) throws UploadFileException, IOException {

        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            fileNames.add(service.uploadFileToCloudinary(file));
        }

        return new ResponseEntity<List<String>>(fileNames, HttpStatus.OK);
    }
}
