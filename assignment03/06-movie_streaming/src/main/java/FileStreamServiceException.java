public class FileStreamServiceException extends RuntimeException {

    public FileStreamServiceException() {
        super("File streaming service is not available.");
    }
}
