import net.jqwik.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BookTest {
    private BookRatingsFetcher bookRatingsFetcherMock;
    private BookManager bookManager;
//    test for point A
//    @BeforeEach
//    public void setup() {
//        bookRatingsFetcherMock = mock(BookRatingsFetcher.class);
//        bookManager = new BookManager(bookRatingsFetcherMock);
//    }
//
//    @Test
//    void checkHighRatedBooks_AboveBoundary() {
//        Book book1 = new Book("Very Good book", 5);
//        when(bookRatingsFetcherMock.all()).thenReturn(List.of(book1));
//        List<Book> result = bookManager.highRatedBooks();
//        assertEquals(1, result.size());
//        assertEquals(book1, result.get(0), "The book is the HighRatedBook Very Good Book with rating 5");
//    }
//
//    @Test
//    void checkHighRatedBooks_AtBoundary() {
//        Book book1 = new Book("Boundary Book", 4);
//        when(bookRatingsFetcherMock.all()).thenReturn(List.of(book1));
//        List<Book> result = bookManager.highRatedBooks();
//        assertEquals(1, result.size());
//        assertEquals(book1, result.get(0), "The book is the HighRatedBook Boundary Book with rating 5");
//    }
//
//    @Test
//    void checkNotHighRatedBooks_BelowBoundary() {
//        Book book2 = new Book("Below book", 3);
//        when(bookRatingsFetcherMock.all()).thenReturn(List.of(book2));
//        List<Book> result = bookManager.highRatedBooks();
//        assertEquals(0, result.size(), "The book is below the threshold, no HighRatedBook retrieved");
//    }
//
//    @Test
//    void checkHighRatedBooks_WithMixedRatings() {
//        Book book1 = new Book("Highly Rated Book", 5);
//        Book book2 = new Book("Boundary Book", 4);
//        Book book3 = new Book("Below Book", 3);
//        Book book4 = new Book("Average Book", 2);
//        when(bookRatingsFetcherMock.all()).thenReturn(List.of(book1, book2, book3, book4));
//        List<Book> result = bookManager.highRatedBooks();
//        assertEquals(2, result.size(), "Should only return the 2 books with ratings 4 or above");
//        assertEquals(List.of(book1, book2), result, "The list should only include Highly Rated Book and Boundary Book");
//    }
//
//    @Test
//    void checkEmptyRatingBook() {
//        when(bookRatingsFetcherMock.all()).thenReturn(List.of());
//        List<Book> result = bookManager.highRatedBooks();
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void checkThrowExceptionHighRatedBooks_WhenBookListIsNull() {
//        when(bookRatingsFetcherMock.all()).thenReturn(null);
//        bookManager = new BookManager(bookRatingsFetcherMock);
//        assertThrows(IllegalArgumentException.class, () -> {
//            bookManager.highRatedBooks();
//        }, "highRatedBooks must throw exception when book list is null");
//    }
//
//    @Property
//    boolean randomReturnedBooks_HaveHighRating(@ForAll("highRatingBook") List<Book> books) {
//        BookRatingsFetcher fetcher = mock(BookRatingsFetcher.class);
//        when(fetcher.all()).thenReturn(books);
//        BookManager bookManager = new BookManager(fetcher);
//
//        List<Book> highRatedBooks = bookManager.highRatedBooks();
//        return highRatedBooks.stream().allMatch(book -> book.getRating() >= 4);
//    }
//
//    @Property
//    boolean randomReturnedBooks_HaveNotHighRating(@ForAll("lowRatingBook") List<Book> books) {
//        BookRatingsFetcher fetcher = mock(BookRatingsFetcher.class);
//        when(fetcher.all()).thenReturn(books);
//        BookManager bookManager = new BookManager(fetcher);
//
//        List<Book> highRatedBooks = bookManager.highRatedBooks();
//        return highRatedBooks.isEmpty();
//    }
//
//    @Provide
//    Arbitrary<List<Book>> highRatingBook() {
//        return Arbitraries.integers().between(4, 5).flatMap(rating ->
//                Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(20).flatMap(title ->
//                        Arbitraries.just(new Book(title, rating))
//                )
//        ).list().ofMinSize(1).ofMaxSize(20);
//    }
//
//    @Provide
//    Arbitrary<List<Book>> lowRatingBook() {
//        return Arbitraries.integers().between(1, 3).flatMap(rating ->
//                Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(20).flatMap(title ->
//                        Arbitraries.just(new Book(title, rating))
//                )
//        ).list().ofMinSize(1).ofMaxSize(20);
//    }

    //test for point B

        @BeforeEach
        public void setup() {
            bookRatingsFetcherMock = mock(BookRatingsFetcher.class);
            bookManager = new BookManager(bookRatingsFetcherMock);
        }

        @Test
        void checkUniqueAuthors_WithDuplicateAuthors() {
            List<Book> books = Arrays.asList(
                    new Book("Book One", "Author same", 5),
                    new Book("Book Two", "Author same", 4),
                    new Book("Book Three", "Author different", 3)
            );
            when(bookRatingsFetcherMock.all()).thenReturn(books);
            List<String> authors = bookManager.uniqueAuthors();
            assertEquals(2, authors.size(), "Two authors are the same");
            assertTrue(authors.contains("Author same") && authors.contains("Author different"), "Correct authors should be included");
        }

        @Test
        void checkUniqueAuthors_WithNoDuplicates() {
            List<Book> books = Arrays.asList(
                    new Book("Book1", "Author1", 5),
                    new Book("Book2", "Author2", 4),
                    new Book("Book3", "Author3", 1)
            );
            when(bookRatingsFetcherMock.all()).thenReturn(books);
            List<String> authors = bookManager.uniqueAuthors();
            assertEquals(3, authors.size(), "Should return three unique authors");
            assertTrue(authors.containsAll(Arrays.asList("Author1", "Author2", "Author3")), "All authors must be included");
        }

        @Test
        void checkUniqueAuthors_WhenNoBooks() {
            when(bookRatingsFetcherMock.all()).thenReturn(Arrays.asList());
            List<String> authors = bookManager.uniqueAuthors();
            assertTrue(authors.isEmpty(), "Empty list should be returned when no books are available");
        }

        @Test
        void checkUniqueAuthors_ThrowsExceptionWhenBooksNull() {
            when(bookRatingsFetcherMock.all()).thenReturn(null);
            assertThrows(IllegalArgumentException.class, () -> bookManager.uniqueAuthors(), "IllegalArgumentException returned book list is null");
        }
    }


