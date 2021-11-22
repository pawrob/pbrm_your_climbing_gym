package pl.ftims.ias.perfectbeta.moch.services;

import org.springframework.web.multipart.MultipartFile;
import pl.ftims.ias.perfectbeta.exceptions.UploadFileException;

import java.io.IOException;

public interface CloudServiceLocal {

    String uploadFileToCloudinary(MultipartFile file) throws UploadFileException, IOException;
}
