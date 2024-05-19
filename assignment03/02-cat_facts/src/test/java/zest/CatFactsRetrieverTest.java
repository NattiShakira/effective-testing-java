package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CatFactsRetrieverTest {
    private HttpClient mockHttpClient;
    private CatFactsRetriever catFactsRetriever;

    @BeforeEach
    public void setUp() {
        mockHttpClient = Mockito.mock(HttpClient.class);
        catFactsRetriever = new CatFactsRetriever(mockHttpClient);
    }

    @Test
    public void testRetrieveRandom() throws IOException {
        String fakeResponse = "{\"fact\":\"Cats are awesome!\"}";
        when(mockHttpClient.get("https://catfact.ninja/fact")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveRandom();
        assertEquals("Cats are awesome!", result);

        verify(mockHttpClient).get("https://catfact.ninja/fact");
    }

    @Test
    public void testRetrieveLongest() throws IOException {
        String fakeResponse = "{\"data\":[{\"fact\":\"Cats sleep a lot.\",\"length\":17},{\"fact\":\"Cats are very agile.\",\"length\":19}]}";
        when(mockHttpClient.get("https://catfact.ninja/facts?limit=2")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveLongest(2);
        assertEquals("Cats are very agile.", result);

        verify(mockHttpClient).get("https://catfact.ninja/facts?limit=2");
    }

    @Test
    public void testRetrieveLongestEmpty() throws IOException {
        String fakeResponse = "{\"data\":[]}";
        when(mockHttpClient.get("https://catfact.ninja/facts?limit=0")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveLongest(0);
        assertEquals("", result);

        verify(mockHttpClient).get("https://catfact.ninja/facts?limit=0");
    }

    @Test
    public void testRetrieveLongestWithNonJsonObject() throws IOException {
        String fakeResponse = "{\"data\":[\"Non-JSON Object\", {\"fact\":\"Cats sleep a lot.\",\"length\":17}]}";
        when(mockHttpClient.get("https://catfact.ninja/facts?limit=2")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveLongest(2);
        assertEquals("Cats sleep a lot.", result);

        verify(mockHttpClient).get("https://catfact.ninja/facts?limit=2");
    }

    @Test
    public void testRetrieveLongestWithShorterFact() throws IOException {
        String fakeResponse = "{\"data\":[{\"fact\":\"Cats are agile.\",\"length\":16},{\"fact\":\"Cats are very agile.\",\"length\":19}]}";
        when(mockHttpClient.get("https://catfact.ninja/facts?limit=2")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveLongest(2);
        assertEquals("Cats are very agile.", result);

        verify(mockHttpClient).get("https://catfact.ninja/facts?limit=2");
    }

    @Test
    public void testRetrieveLongestWithEqualLengthFacts() throws IOException {
        String fakeResponse = "{\"data\":[{\"fact\":\"Cats are agile.\",\"length\":16},{\"fact\":\"Cats are swift.\",\"length\":16}]}";
        when(mockHttpClient.get("https://catfact.ninja/facts?limit=2")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveLongest(2);
        assertEquals("Cats are agile.", result);

        verify(mockHttpClient).get("https://catfact.ninja/facts?limit=2");
    }

    @Test
    public void testRetrieveLongestWithSingleFact() throws IOException {
        String fakeResponse = "{\"data\":[{\"fact\":\"Cats are agile.\",\"length\":16}]}";
        when(mockHttpClient.get("https://catfact.ninja/facts?limit=1")).thenReturn(fakeResponse);

        String result = catFactsRetriever.retrieveLongest(1);
        assertEquals("Cats are agile.", result);

        verify(mockHttpClient).get("https://catfact.ninja/facts?limit=1");
    }
}
