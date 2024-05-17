import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BookManager {
    //new
    private BookRatingsFetcher fetcher;

    public BookManager(BookRatingsFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public List<Book> highRatedBooks() {

        try {
            List<Book> allBooks = fetcher.all();
            //precondition
            if (allBooks == null) {
                throw new IllegalArgumentException("Book list cannot be null");
            }
            return allBooks.stream()
                    .filter(book -> book.getRating() >= 4)
                    .collect(toList());
        } finally {
            fetcher.close();
        }
    }

    public List<String> uniqueAuthors() {
        try {
            List<Book> allBooks = fetcher.all();
            if (allBooks == null) {
                throw new IllegalArgumentException("Book list cannot be null");
            }
            List<String> uniqueAuthors = new ArrayList<>();
            allBooks.forEach(book -> {
                if (!uniqueAuthors.contains(book.getAuthor())) {
                    uniqueAuthors.add(book.getAuthor());
                }
            });
            return uniqueAuthors;
        } finally {
            fetcher.close();
        }
    }
}
