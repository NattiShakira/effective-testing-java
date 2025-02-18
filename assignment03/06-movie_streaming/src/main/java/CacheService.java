public interface CacheService {
    StreamingDetails getDetails(String movieId) throws CacheServiceException;
    boolean cacheDetails(String movieId, StreamingDetails details) throws CacheServiceException;
    //boolean cacheDetails(String movieId, String streamToken, MovieMetadata metadata) throws CacheServiceException;
    boolean refreshCache(String movieId, MovieMetadata metadata) throws CacheServiceException;
}
