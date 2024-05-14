import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieStreamingManagerTest {

    private FileStreamService fileStreamService;
    private CacheService cacheService;
    private MovieStreamingManager movieStreamingManager;

    private final String title = "Dune";
    private final String description = "The movie tells the story of young Paul Atreides, whose family accepts "
                       + "the stewardship of the planet Arrakis.";
    private final MovieMetadata movieMetadata = new MovieMetadata(title, description);

    private final String movieId = "123456";
    private final String streamToken = "ksjkdg3861jynx273y72cbt53t";
    private final StreamingDetails streamingDetails = new StreamingDetails(movieId, streamToken, movieMetadata);

    private FileStreamServiceException fileStreamServiceException;
    private CacheServiceException cacheServiceException;

    @BeforeEach
    void setUp() {
        fileStreamService = mock(FileStreamService.class);
        cacheService = mock(CacheService.class);
        movieStreamingManager = new MovieStreamingManager(fileStreamService, cacheService);

        fileStreamServiceException = new FileStreamServiceException();
        cacheServiceException = new CacheServiceException();
    }

    @Test
    void streamMovieDetailsNotNull() {
        when(cacheService.getDetails(movieId)).thenReturn(streamingDetails);

        StreamingDetails actualStreamingDetails = movieStreamingManager.streamMovie(movieId);
        assertEquals(streamingDetails, actualStreamingDetails);

        verify(fileStreamService, never()).retrieveMovie(movieId);
        verify(fileStreamService, never()).generateToken(movieId);
        verify(cacheService, never()).cacheDetails(movieId, streamingDetails);
    }

    @Test
    void streamMovieDetailsAreNull() {
        when(cacheService.getDetails(movieId)).thenReturn(null);
        when(fileStreamService.retrieveMovie(movieId)).thenReturn(movieMetadata);
        when(fileStreamService.generateToken(movieId)).thenReturn(streamToken);

        StreamingDetails actualStreamingDetails = movieStreamingManager.streamMovie(movieId);

        assertEquals(movieId, actualStreamingDetails.getMovieId());
        assertEquals(streamToken, actualStreamingDetails.getStreamToken());
        assertEquals(movieMetadata, actualStreamingDetails.getMetadata());

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<StreamingDetails> streamingDetailsCaptor = ArgumentCaptor.forClass(StreamingDetails.class);

        verify(cacheService, times(1)).cacheDetails(stringCaptor.capture(), streamingDetailsCaptor.capture());

        String capturedMovieId = stringCaptor.getValue();
        assertEquals(movieId, capturedMovieId);

        StreamingDetails capturedStreamingDetails = streamingDetailsCaptor.getValue();
        assertEquals(movieId, capturedStreamingDetails.getMovieId());
        assertEquals(streamToken, capturedStreamingDetails.getStreamToken());
        assertEquals(movieMetadata, capturedStreamingDetails.getMetadata());
    }

    @Test
    void updateMovieMetadataTest() {
        movieStreamingManager.updateMovieMetadata(movieId, movieMetadata);

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MovieMetadata> movieMetadataCaptor = ArgumentCaptor.forClass(MovieMetadata.class);
        verify(fileStreamService, times(1)).updateMetadata(stringCaptor.capture(), movieMetadataCaptor.capture());

        String capturedMovieId = stringCaptor.getValue();
        assertEquals(movieId, capturedMovieId);

        MovieMetadata capturedMovieMetadata = movieMetadataCaptor.getValue();
        assertEquals(title, capturedMovieMetadata.getTitle());
        assertEquals(description, capturedMovieMetadata.getDescription());

        ArgumentCaptor<String> stringCaptorCS = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MovieMetadata> movieMetadataCaptorCS = ArgumentCaptor.forClass(MovieMetadata.class);
        verify(cacheService, times(1)).refreshCache(stringCaptorCS.capture(), movieMetadataCaptorCS.capture());

        String capturedMovieIdCS = stringCaptorCS.getValue();
        assertEquals(movieId, capturedMovieIdCS);

        MovieMetadata capturedMovieMetadataCS = movieMetadataCaptorCS.getValue();
        assertEquals(title, capturedMovieMetadataCS.getTitle());
        assertEquals(description, capturedMovieMetadataCS.getDescription());
    }

    @Test
    void validateNullStreamingToken() {
        String streamToken = null;
        boolean tokenIsValid = movieStreamingManager.validateStreamingToken(streamToken);
        assertFalse(tokenIsValid);
        verify(fileStreamService, never()).retrieveListOfTokens();
    }

    @Test
    void validateStreamingTokenIfFileStreamServiceReturnsNullList() {
        List<String> listOfTokens = null;
        when(fileStreamService.retrieveListOfTokens()).thenReturn(listOfTokens);
        boolean tokenIsValid = movieStreamingManager.validateStreamingToken(streamToken);
        assertFalse(tokenIsValid);
    }

    @Test
    void validateStreamingTokenThatExists() {
        List<String> listOfTokens = new ArrayList<>();
        listOfTokens.add(streamToken);
        listOfTokens.add("kujhefibcy3498ucn934");
        when(fileStreamService.retrieveListOfTokens()).thenReturn(listOfTokens);
        boolean tokenIsValid = movieStreamingManager.validateStreamingToken(streamToken);
        assertTrue(tokenIsValid);
    }

    @Test
    void validateStreamingTokenThatDoesntExists() {
        List<String> listOfTokens = new ArrayList<>();
        listOfTokens.add("Ksjkdg3861jynx273y72cbt53t");
        listOfTokens.add("kujhefibcy3498ucn934");
        when(fileStreamService.retrieveListOfTokens()).thenReturn(listOfTokens);
        boolean tokenIsValid = movieStreamingManager.validateStreamingToken(streamToken);
        assertFalse(tokenIsValid);
    }

    @Test
    void streamMovieCacheServiceCannotGetDetails() {
        when(cacheService.getDetails(movieId)).thenThrow(cacheServiceException);

        MovieStreamingManagerException exception = assertThrows(MovieStreamingManagerException.class, () -> movieStreamingManager.streamMovie(movieId));

        assertEquals("Error occured: CacheServiceException: Cache service is not available.", exception.getMessage());
        verify(fileStreamService, never()).retrieveMovie(movieId);
        verify(fileStreamService, never()).generateToken(movieId);
        verify(cacheService, never()).cacheDetails(movieId, streamingDetails);
    }

    @Test
    void streamMovieFileStreamServiceCannotRetrieveMovie() {
        when(cacheService.getDetails(movieId)).thenReturn(null);
        when(fileStreamService.retrieveMovie(movieId)).thenThrow(fileStreamServiceException);

        MovieStreamingManagerException exception = assertThrows(MovieStreamingManagerException.class, () -> movieStreamingManager.streamMovie(movieId));

        assertEquals("Error occured: FileStreamServiceException: File streaming service is not available.", exception.getMessage());
        verify(fileStreamService, never()).generateToken(movieId);
        verify(cacheService, never()).cacheDetails(movieId, streamingDetails);
    }

    @Test
    void streamMovieFileStreamServiceCannotGenerateToken() {
        when(cacheService.getDetails(movieId)).thenReturn(null);
        when(fileStreamService.retrieveMovie(movieId)).thenReturn(movieMetadata);
        when(fileStreamService.generateToken(movieId)).thenThrow(fileStreamServiceException);

        MovieStreamingManagerException exception = assertThrows(MovieStreamingManagerException.class, () -> movieStreamingManager.streamMovie(movieId));

        assertEquals("Error occured: FileStreamServiceException: File streaming service is not available.", exception.getMessage());
        verify(cacheService, never()).cacheDetails(movieId, streamingDetails);
    }

    /***@Test
    void streamMovieCacheServiceCannotCacheDetails() {
        when(cacheService.getDetails(movieId)).thenReturn(null);
        when(fileStreamService.retrieveMovie(movieId)).thenReturn(movieMetadata);
        when(fileStreamService.generateToken(movieId)).thenReturn(streamToken);
        when(cacheService.cacheDetails(movieId, streamingDetails)).thenThrow(cacheServiceException);

        MovieStreamingManagerException exception = assertThrows(MovieStreamingManagerException.class, () -> {
            movieStreamingManager.streamMovie(movieId);
        });

        assertEquals("Error occured: FileStreamServiceException: File streaming service is not available.", exception.getMessage());
        verify(cacheService, times(1)).cacheDetails(movieId, streamingDetails);
    }
     ***/

    @Test
    void updateMovieDataFileStreamServiceUnavailable() {

        when(fileStreamService.updateMetadata(movieId, movieMetadata)).thenThrow(fileStreamServiceException);
        MovieStreamingManagerException exception = assertThrows(MovieStreamingManagerException.class, () -> movieStreamingManager.updateMovieMetadata(movieId, movieMetadata));

        assertEquals("Error occured: FileStreamServiceException: File streaming service is not available.", exception.getMessage());
        verify(cacheService, never()).cacheDetails(movieId, streamingDetails);
    }

    @Test
    void updateMovieDataCacheServiceUnavailable() {
        when(fileStreamService.updateMetadata(movieId, movieMetadata)).thenReturn(true);
        when(cacheService.refreshCache(movieId, movieMetadata)).thenThrow(cacheServiceException);
        MovieStreamingManagerException exception = assertThrows(MovieStreamingManagerException.class, () -> movieStreamingManager.updateMovieMetadata(movieId, movieMetadata));

        assertEquals("Error occured: CacheServiceException: Cache service is not available.", exception.getMessage());
        verify(fileStreamService, times(1)).updateMetadata(movieId, movieMetadata);
        verify(cacheService, times(1)).refreshCache(movieId, movieMetadata);
    }

    @Test
    void validateStreamingTokenFileStreamServiceUnavailable() {
        when(fileStreamService.retrieveListOfTokens()).thenThrow(fileStreamServiceException);
        MovieStreamingManagerException exception = assertThrows(MovieStreamingManagerException.class, () -> movieStreamingManager.validateStreamingToken(streamToken));

        assertEquals("Error occured: FileStreamServiceException: File streaming service is not available.", exception.getMessage());
        verify(fileStreamService, times(1)).retrieveListOfTokens();
    }

}