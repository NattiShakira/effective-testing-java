# A. Get high-rated books
The class `BookManager` communicates with an SQL database when in production, which contains book metadata (title, author, and rating).
The `highRatedBooks` method of `BookManager` returns only the books with high rating (high is defined as >= 4/5).

## 1. External Dependencies
The system consists of the following components:

- Book: A simple data class representing book metadata.
- BookRatingsFetcher: Responsible for fetching books from a database.
- DatabaseConnection: Manages the database connection.
- BookManager: Business logic class that filters high-rated books.

`BookManager` depends on `BookRatingsFetcher` and indirectly on `DatabaseConnection`. The strategy used has been the one of 
using mocks for both dependencies. `BookRatingsFetcher` has been mocked to control the returned books and test the filtering logic in `BookManager`.
`DatabaseConnection` has been mocked to ensure `BookRatingsFetcher` can be instantiated without an actual database connection.
The mocks isolate the unit tests from the external systems like the database, enabling for a controlled and decoupled interaction. Moreover, the simulation are faster since there is no need to wait for the database connection and the classes interactions are correctly reflected.

## 2. Refractoring and Unit Testing
`BookManager` class was refactored to allow for better unit testing via dependency injection. This involved  making 
the class depend on abstractions rather than concrete implementations, allowing to inject dependencies during testing.
Originally, the `BookManager` instantiates `DatabaseConnection` and `BookRatingsFetcher` directly within its `highRatedBooks` method. 
This tightly bounded the class to specific implementations of these dependencies, making unit testing challenging without involving actual database connections.
Refactoring steps:
- Modified `BookManager` to accept `BookRatingsFetcher` through its constructor, separating concerns and making the class more testable.
- Adjusted `BookRatingsFetcher` to ensure it was fully mocked or stubbed if needed by also using dependency injection for `DatabaseConnection`.
This approach aligns with the  Hexagonal Architecture by separating the application core from its external concerns. `BookRatingsFetcher` and
  `DatabaseConnection` interfaces were defined by ports abstracting the details of data fatching and database management.


In the unit tests for the `BookManager` class's `highRatedBooks` method, several best practices and principles of software testing are used in combination of the doubles:

- Specification-Based Testing: tests are designed to verify the functionality according to the specifications, which state that only books with a rating of 4 or higher should be returned. Tests such as `checkHighRatedBooks_AboveBoundary`, `checkHighRatedBooks_AtBoundary`, and `checkNotHighRatedBooks_BelowBoundary` directly validate these conditions.

- Boundary Testing: by explicitly testing the condition where books have a rating exactly at the boundary value of 4, we ensure that edge cases are correctly handled, preventing off-by-one errors and similar issues.

- Structural Testing: further verification of the efficiency of the method under test is verified by `checkHighRatedBooks_WithMixedRatings`. The test includes books both above and below the rating threshold to ensure that the filter works correctly in mixed conditions. Moreover, the behaviors of an empty string is checked by `checkEmptyRatingBook`. Line coverage reaches 100%.

- Design contracts: to further enhance unit testing and testing to guide development a precondition has been added that throws an Exception when the book-list is null. Moreover, its functionality is checked by `checkThrowExceptionHighRatedBooks_WhenBookListIsNull`.
  
- Property-based testing: property-based testing has been employed to verify that a function behaves correctly across a much broader set of inputs. The `randomReturnedBooks_HaveHighRating` property test aims to ensure that no matter what collection of books is returned with high ratings, the `highRatedBooks` method correctly identifies and returns all of them.
  Similarly, the `randomReturnedBooks_HaveNotHighRating` test confirms that books with ratings below 4 are appropriately filtered out by the highRatedBooks method. 

The test are cohesive, independent and isolated, following the practises of code quality. 
## 3. Disadvantages of test doubles

Several has to be taken into consideration when using test doubles. In this implementation:

- The `BookManager` class relies on the `BookRatingsFetcher` to fetch book ratings from a database. In testing, if `BookRatingsFetcher` is mocked to return predetermined responses, the tests will not cover scenarios where the actual database queries might fail, or return unexpected results. This could mean potential issues in production where database responses vary under different conditions.
- If the schema of the database or the behavior of the `BookRatingsFetcher` changes, the mocks used in the tests for `BookManager` need to be updated to reflect these changes. Failing to do so can lead to a scenario where the tests pass, but the application fails in production because the tests were interacting with an outdated version of the mocked dependency.
- Mocking the `BookRatingsFetcher` and `DatabaseConnection` might lead developers to overlook the integration between `BookManager` and these components. If `BookRatingsFetcher` starts using a new method to establish database connections that requires additional configuration, the tests might not catch this requirement since the database connection is mocked and assumed to work flawlessly. This neglect can lead to integration issues.

# A. Get unique authors

In order to extend the functionality of `BookManager` to retrieve a list of unique authors, proper refactoring to existing code to implement this new feature has been done.
First the `Book` class has been modified to include the `author` field and update `BookRatingsFetcher` to handle author data, than the class `BookManager` has been implemented with the new `uniqueAuthors` method.


1. External Dependencies:
The `uniqueAuthors` method in the `BookManager` class relies on several external components to function properly. These include:
- Book: A data class adjusted to receive authors as parameters, including the author's name.   
- BookRatingsFetcher: fetches the list of books from a database. It serves as a dependency for accessing book data.  
- DatabaseConnection: Manages the connection to the SQL database.
- BookManager: Business logic class that filters high-rated books and unique authors.

`BookManager` depends on `BookRatingsFetcher` and indirectly on `DatabaseConnection`. The strategy used has been the one of
using mocks for both dependencies. `BookRatingsFetcher` has been mocked to control the returned books and test the filtering logic in `BookManager`, such as unique authors.
`DatabaseConnection` has been mocked to ensure `BookRatingsFetcher` can be instantiated without an actual database connection.
The mocks isolate the unit tests from the external systems like the database, enabling for a controlled and decoupled interaction. Moreover, the simulation are faster since there is no need to wait for the database connection and the classes interactions are correctly reflected.

2. - Refactoring:The code has already been refactored to inject `BookRatingsFetcher` via the constructor in the exercise part A, allowing easy replacement with a mock during testing. The method `uniqueAuthors` was added to `BookManager` to retrieve a list of unique authors from the books provided by `BookRatingsFetcher`. The design is adherent to code quality test design principles by decoupling the class from its dependencies.
   - Unit tests: Tests are created to ensure that `uniqueAuthors` correctly handles and returns unique author names under different conditions such as duplicates in the list, no books, and when the book list is null. Edge cases like empty lists and null values are tested to ensure robust error handling.
     a precondition has been added that throws an Exception when the book-list is null. The previous test has been commented to avoid interference. 
3. Disadvantages of Using Test Doubles:
- By using a mock for `BookRatingsFetcher`, the tests might not catch issues that occur with real database queries.
- If the database schema changes (e.g., the author's field changes in type or name), the mocks will need to be updated to reflect these changes.
- Mocking might cause developers to miss integration issues, such as the correct configuration of database connections and handling of database-specific exceptions.