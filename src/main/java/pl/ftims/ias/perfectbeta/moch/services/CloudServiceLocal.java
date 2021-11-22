package pl.ftims.ias.perfectbeta.moch.services;

import org.springframework.web.multipart.MultipartFile;
import pl.ftims.ias.perfectbeta.exceptions.UploadFileException;

public interface CloudServiceLocal {
    String uploadFile(MultipartFile file) throws UploadFileException;
}
