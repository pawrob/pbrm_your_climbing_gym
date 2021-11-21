package pl.ftims.ias.your_climbing_gym.exceptions;

public class UploadFileException extends AbstractAppException {

    public static final String FILE_UPLOAD_ERROR = "Error while uploading %s";

    private UploadFileException(String message) {
        super(message);
    }

    public static UploadFileException createUploadFileException(String filename) {
        return new UploadFileException(String.format(FILE_UPLOAD_ERROR, filename));
    }
}
