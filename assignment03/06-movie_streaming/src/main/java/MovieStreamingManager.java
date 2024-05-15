import java.util.List;

public class MovieStreamingManager {
    private FileStreamService fileStreamService;
    private CacheService cacheService;

    // Constructor to inject the file stream and cache services
    public MovieStreamingManager(FileStreamService fileStreamService, CacheService cacheService) {
        this.fileStreamService = fileStreamService;
        this.cacheService = cacheService;
    }

    // Method to stream a movie by its ID
    public StreamingDetails streamMovie(String movieId) throws MovieStreamingManagerException {
        StreamingDetails details;

        try {
            details = cacheService.getDetails(movieId);
        } catch (CacheServiceException e) {
            throw new MovieStreamingManagerException("Error occured: " + e);

        }

        if (details == null) {
            MovieMetadata metadata;
            try {
                metadata = fileStreamService.retrieveMovie(movieId);
            } catch (FileStreamServiceException e) {
                throw new MovieStreamingManagerException("Error occured: " + e);
            }

            String streamToken;  // Assume there's a method to generate a streaming token
            try {
                streamToken = fileStreamService.generateToken(movieId);
            } catch (FileStreamServiceException e) {
                throw new MovieStreamingManagerException("Error occured: " + e);
            }

            details = new StreamingDetails(movieId, streamToken, metadata);
            try {
                cacheService.cacheDetails(movieId, details);
            } catch (CacheServiceException e) {
                throw new MovieStreamingManagerException("Error occured: " + e);
            }
        }
        return details;
    }

    // Additional methods can be added here for other functionalities
    public void updateMovieMetadata(String movieId, MovieMetadata metadata) throws MovieStreamingManagerException {
        try {
            fileStreamService.updateMetadata(movieId, metadata);
        } catch (FileStreamServiceException e) {
            throw new MovieStreamingManagerException("Error occured: " + e);
        }

        try {
            cacheService.refreshCache(movieId, metadata);
        } catch (CacheServiceException e) {
            throw new MovieStreamingManagerException("Error occured: " + e);
        }
    }

    public boolean validateStreamingToken(String token) throws MovieStreamingManagerException {
        if (token == null) {return false;}
        List<String> listOfTokens;

        try {
            listOfTokens = fileStreamService.retrieveListOfTokens();
        } catch (FileStreamServiceException e) {
            throw new MovieStreamingManagerException("Error occured: " + e);
        }

        if (listOfTokens == null) {return false;}
        return listOfTokens.contains(token);
    }
}
