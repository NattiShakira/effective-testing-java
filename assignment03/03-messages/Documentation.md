## A: Number of invocations

To make testing easier I changed the MessageProcessor class by using dependency injection of the MessageService class.
I tested for various amounts and variations of Messages (tests 1 - 5, see Assets).

## B: Content of invocations using ArgumentCaptor

Using the ArgumentCaptor I checked the usual problems for String related content tests (tests 6 - 8).

## C: Content of invocations using observability

To track the state I added an array into the MessageProcessor that logs the processed messages 
and a method to view them. I used the same test criteria as for the ArgumentCaptor (tests 9 - 11).

## D: Comparison of B and C

The advantage of using the ArgumentCaptor is that you do not need to change your code, it directly captures the 
argument for precise assertions, and it has no unwanted side effects since it does not alter state.
However, that also means that it only interacts with the mocked objects and does not capture other interactions.
The setup for the mocking framework is usually also pretty extensive.

The advantage of using observability is that it shows the real behavior of the state involved, which can produce 
"stronger/more real" assertions. It also requires no additional setup. On the other hand, this method may force you 
to cluster your code with additional functionality to make the test work in the first place. Which means increased 
maintenance of code and possible additional vulnerabilities.

Both have their place for their respective uses.
