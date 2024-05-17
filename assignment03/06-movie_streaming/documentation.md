# MovieStreaming


## 1. Implementing missing methods

According to the description of the task, class MovieStreamingManager has to have 3 methods:
- streamMovie(String movieId);
- updateMovieMetadata(String movieId, MovieMetadata metadata);
- validateStreamingToken(String token).

The last two methods were missing, therefore, we have decided to implement them ourselves:
- method updateMovieMetadata updates movie information in the distributed file system and refreshes the cache, it's implementation is as follows:
```java
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
```
- method validateStreamingToken checks the validity of a token against file system records, it's implementation is as follows:

```java
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
```
According to our understanding of this method, it should check if a token passed as an argument is contained in the list of all valid tokens that is stored in the file system.  
Since existing interface FileStreamService didn't have any method to retrieve a list of valid tokens, it was decided to add it to the interface:

```java
public interface FileStreamService {
    ...
    List<String> retrieveListOfTokens() throws FileStreamServiceException;
}
```


## 2. Testing with mocks

Class MovieStreamingManager has two dependencies:
- interface FileStreamService;
- interface CacheService.

When an object of class MovieStreamingManager is created, it conveniently gets these dependencies through its constructor. Thus, when testing, these dependencies can be easily mocked.
All our tests for class MovieStreamingManager require these dependencies, thus, at the beginning of the test class, we created a fixture, containing declaration of three instances:
- fileStreamService;
- cacheService;
- movieStreamingManager;

Since our tests use those instances and may change there state, we decided to instantiate those objects in @BeforeEach setUp() method that guarantees that 
every time a new test needs a corresponding instance, it's supplied to it with a fresh state.

Our tests also require several other objects:
- movieMetadata, containing title and description;
- streamingDetails, containing movieId, streamToken and movieMetadata.

Out tests don't change state of these objects, thus, we added these objects (declared and initialized) to the fixture at the beginning of the test class. 

After analyzing the code, the following test cases were devised:
1) for method streamMovie(movieId):
- streamMovieDetailsNotNull(): this test checks that streamingDetails are returned to the user if they are contained in cacheService and can be retrieved from it. First, we 
mock that getDetails method called on cacheService returns streamingDetails. Then, we call method under test and compare results of this method with streamingDetails. Additionally,
we check that other methods inside our method under test are not called.
- streamMovieDetailsAreNull(): this test is a bit more involved, since we first have to mock returns of several other methods (cacheService.getDetails(movieId), 
fileStreamService.retrieveMovie(movieId) and fileStreamService.generateToken(movieId)) are called inside our method under test, and only then we can compare that our method returns a correct object.
Additional complication is that after getting streamingDetails, the method invokes method cacheDetails(movieId, streamingDetails) on cacheService. To check the correctness of this part of the method under test,
we verified that the method cacheDetails was indeed invoked one time and that the corresponding correct arguments (movieId, streamingDetails) were passed to it. The latter was done using argument captors for class String and class StreamingDetails.

2) for method updateMovieMetadata(movieId, movieMetadata):
- updateMovieMetadataTest(): the test checks the "happy" execution of the method under test. After calling our method, we check that both methods that should be invoked by our 
method were indeed called one time each (fileStreamService.updateMetadata(movieId, movieMetadata) and cacheService.refreshCache(movieId, movieMetadata)) and that the arguments passed to these methods were correct (by using argument captors for class String and class MovieMetadata).

3) for method validateStreamingToken(String token):
We could see 3 different cases worth testing: one "happy" case and three "unhappy":
- first "unhappy" case, validateNullStreamingToken(): this test checks that the method under test returns false if a null streamingToken is passed as an argument. Additionally, we verified that in this case out method doesn't invoke 
fileStreamService.retrieveListOfTokens(); 
- second "unhappy" case, validateStreamingTokenIfFileStreamServiceReturnsNullList(): this test checks that the method under test returns false if fileStreamService.retrieveListOfTokens() returns a null list.
To make this test work, we mocked the corresponding behaviour of fileStreamService.retrieveListOfTokens() method.
- third "unhappy" case, validateStreamingTokenThatDoesntExists(): the test verifies that if streamingToken is not in the list of valid tokens, our method inder test returns false. As in the previous case, we had to mock
behaviour of fileStreamService.retrieveListOfTokens() method;
- "happy" case, validateStreamingTokenThatExists(): the test check that if streamingToken is in the list of valid tokens, our method inder test returns true. As in the previous two cases, we also had to mock
behaviour of fileStreamService.retrieveListOfTokens() method;


## 3. Handling failures

Before we could simulate failures and see how our system can handle them, we had to make the following changes to our code:
- first, we allowed our cacheService, fileStinreamService and movieStreamingManager to throw custom runtime exceptions: CacheServiceException, FileStreamServiceException and MovieStreamingManagerException accordingly;
- then, we decided to wrap up any method invocation on cacheService and fileStreamService into a try/catch block; 
- finally, in the catch block, a caught exception (CacheServiceException/FileStreamServiceException) would lead to a MovieStreamingManagerException. The latter exception should be caught by clients 
of movieStreamingManager. For the purpose of testing MovieStreamingManager class, assuring that the class throws a MovieStreamingManagerException when it has to, is considered enough.

To check failure handling by our code, the following test cases were devised:
1) for method streamMovie(movieId):
- streamMovieCacheServiceCannotGetDetails: if cacheService is down (throws a CacheServiceException) and, thus, streamingDetails cannot be received from it, the method under test
throws an exception (MovieStreamingManagerException) as well without invoking any other methods; to simulate this test case, we mocked behavior of cacheService.getDetails(movieId) and also checked that other methods
(fileStreamService.retrieveMovie(movieId), fileStreamService.generateToken(movieId) and cacheService.cacheDetails(movieId, streamingDetails)) were never called;

- streamMovieFileStreamServiceCannotRetrieveMovie(): if cacheService.getDetails(movieId) returns null, but then file streaming service is unavailable (throws a FileStreamServiceException) and cannot retrieve a movie by movieId,
the method under test throws an exception (MovieStreamingManagerException) as well without invoking remaining methods; in this test case, we mocked behavior of cacheService.getDetails(movieId) and fileStreamService.retrieveMovie(movieId) and checked that other methods
(fileStreamService.generateToken(movieId) and cacheService.cacheDetails(movieId, streamingDetails)) were never called;

- streamMovieFileStreamServiceCannotGenerateToken(): if cacheService.getDetails(movieId) returns null but file streaming service crashes between retrieving a movie 
(fileStreamService.retrieveMovie(movieId) works fine) and generating a streamingToken (fileStreamService.generateToken(movieId) throws a FileStreamServiceException),
the method under test throws an exception (MovieStreamingManagerException) without invoking the last remaining method; in this test case, we mocked behavior of cacheService.getDetails(movieId), fileStreamService.retrieveMovie(movieId)
and fileStreamService.generateToken(movieId) and checked that last method (cacheService.cacheDetails(movieId, streamingDetails)) was never invoked;
- 
- streamMovieCacheServiceCannotCacheDetails(): TODO: there's something wrong with this test

2) for method updateMovieMetadata(movieId, movieMetadata):
- updateMovieDataFileStreamServiceUnavailable(): this test simulates a situation when a file streaming system is unavailable and cannot update metadata (fileStreamService.updateMetadata(movieId, movieMetadata) throws a FileStreamServiceException). In this case,
the method under test also throws an exception (MovieStreamingManagerException) without calling the second method cacheService.cacheDetails(movieId, streamingDetails);

- updateMovieDataCacheServiceUnavailable(): this test checks that, even if the file streaming system was able to update metadata, the crash of the cache service resulted in a failure to refresh metadata in cache will lead to
an exception thrown by the method under test. In the test case, we check that the method under test throws an exception (MovieStreamingManagerException), although both methods (fileStreamService.updateMetadata(movieId, movieMetadata) and cacheService.cacheDetails(movieId, streamingDetails))
were called;

3) for method validateStreamingToken(String token):
- validateStreamingTokenFileStreamServiceUnavailable(): the only external system that this method interacts with, is the file system. When the latter is unavailable (throwns a fileStreamServiceException),
our method under test throws an exception as well. In the test case, we assert that the method under test throws a MovieStreamingManagerException and calls once the method
fileStreamService.retrieveListOfTokens().


To facilitate mocking behaviour of methods that are invoked internally by our methods under test, we had to make this corresponding "internal" methods to return something (if case they were void).
For simplicity, we decided that they can return a boolean (probably, a true value in all the cases), except cases when they throw an exception. The changed methods are the following:
- in CacheService interface - two methods:
```java
public interface CacheService {
   ...
   boolean cacheDetails(String movieId, StreamingDetails details) throws CacheServiceException;
   boolean refreshCache(String movieId, MovieMetadata metadata) throws CacheServiceException;
}
```
- in FileStreamService interface - one method:
```java
public interface FileStreamService {
   ...
   boolean updateMetadata(String movieId, MovieMetadata metadata) throws FileStreamServiceException;
   ...
}
```


## 4. Use of principles of maintainable test code

Code coverage reports and results of run tests can be found in the asset folder.