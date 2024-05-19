# CatFactRetriever testing documentation by Markus Niemack

# 1. External Dependencies & Test Doubles
1. HTTP calls to the API: Http.get(String url) makes an http call to the API, which is obviously an external service. HTTP calls can be unreliable (or error prone during calls), therefore this one should be tested using a double (I specifically used a mock for this).
2. JSON parsing: This is part of a standard library, which are generally very reliable and well-tested. Therefore this one does not need to be tested using a double.
# 2. Refactoring Rationale
To make it easier to test, I refactored CatFactsRetriever to use an interface for the HTTP client, making mocking more possible.
 The only change needed in HttpUtil.java is the statement for implementing the interface: "implements HttpClient".

The changes in CatFactsRetriever.java are a declaration of an HTTP Client using the new Interface and a Constructor:

```java
private final HttpClient httpClient;

public CatFactsRetriever(HttpClient httpClient) {
        this.httpClient = httpClient;
        }
```

and in the lines which used HttpUtil.get(), I replaced them with HttpClient.get():

```java
String response = httpClient.get("https://catfact.ninja/fact");
```
and
```java
String response = httpClient.get("https://catfact.ninja/facts?limit=" + limit);
```

The rest of the code remains the same.

# 3. Disadvantage of Using Doubles

- Relying too much on the Implementation of CatFactsRetriever: In my test suite, I mock the HttpClient to return specific JSON strings,
If the implementation of CatFactsRetriever changes, such as changing the way it processes the JSON response, the tests might fail
even if the logic is sound or vice versa. Example test that might cause problems:
```java
@Test
    public void testRetrieveLongest() throws IOException {
        String fakeResponse = "{\"data\":[{\"fact\":\"Cats sleep a lot.\",\"length\":17},{\"fact\":\"Cats are very agile.\",\"length\":19}]}";
        when(mockHttpClient.get("https://catfact.ninja/facts?limit=2")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveLongest(2);
        assertEquals("Cats are very agile.", result);

        verify(mockHttpClient).get("https://catfact.ninja/facts?limit=2");
    }
```
- False Sense of Security: Since we control the responses from HttpClient by mocking it, the tests will pass even though the API might
behave a bit differently. An API provider might also change the format of the response etc.
Example where I design the response myself:
```java
@Test
    public void testRetrieveRandom() throws IOException {
        String fakeResponse = "{\"fact\":\"Cats are awesome!\"}";
        when(mockHttpClient.get("https://catfact.ninja/fact")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveRandom();
        assertEquals("Cats are awesome!", result);

        verify(mockHttpClient).get("https://catfact.ninja/fact");
    }
```
-Not testing real Integration: By using mocks, The actual interaction between the HttpClient Implementation HttpUtil and CatFactsRetriever is not 
actually tested. Network conditions, changes in 3rd party API etc. might therefore not be caught. Example of that:
```java
@Test
public void testRetrieveLongestWithEqualLengthFacts() throws IOException {
    String fakeResponse = "{\"data\":[{\"fact\":\"Cats are agile.\",\"length\":16},{\"fact\":\"Cats are swift.\",\"length\":16}]}";
    when(mockHttpClient.get("https://catfact.ninja/facts?limit=2")).thenReturn(fakeResponse);
    
    String result = catFactsRetriever.retrieveLongest(2);
    assertEquals("Cats are agile.", result);
}
```
# 4. Test Cases and Tests

- Empty case: An empty string should be returned.
- Boundary case: Test with minimal limit (or other edge cases for the limit, like MAX_INT etc.)

- Tests and their cases:
1. testRetrieveRandom: Ensures that retrieveRandom correctly parses the fact from the response.
2. testRetrieveLongest: Checks that the longest fact is correctly identified from the list.
3. testRetrieveLongestEmpty: Verifies that an empty array results in an empty string being returned.
4. testRetrieveLongestWithNonJsonObject: Creates a scenario where the JSON array contains a non-JSONObject element, ensuring the method handles it without errors.
5. testRetrieveLongestWithShorterFact: Ensures that if a new fact is shorter, it doesn't replace the current longest fact.
6. testRetrieveLongestWithEqualLengthFacts: Ensures that if facts have equal length, the first one is chosen.
7. testRetrieveLongestWithSingleFact: Verifies behavior with the minimum limit of 1.
