import java.util.List;

public interface FileStreamService {
    MovieMetadata retrieveMovie(String movieId) throws FileStreamServiceException;
    boolean updateMetadata(String movieId, MovieMetadata metadata) throws FileStreamServiceException;
    String generateToken(String movieId) throws FileStreamServiceException;
    List<String> retrieveListOfTokens() throws FileStreamServiceException;
}
