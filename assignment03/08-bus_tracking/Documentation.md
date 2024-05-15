## A: Number of invocations

There is no mention to change any code for certain edge case scenarios, which means that I excluded certain test 
cases like invalid Location coordinates or receiving the same Location. I also assumed that any change in Location, 
no matter how small, results in a call to updateBusLocation(). The tests that I wrote for this task are tests 1 - 3.

## B: Content of invocations using ArgumentCaptor

We add 2 ArgumentCaptors to the test suite. Since no business rules for the Services are presented I assumed the 
following to be handled correctly by the system (and tests for this would not make sense to write without the logic):
- Passing waypoints in rapid succession (spamming limit)
- Precision threshold of Locations to key waypoints
- Time intervals for buses to re-trigger notifications if they are idling

The tests for this task are tests 4 - 6.

## C: Content of invocations using observability

The following assumptions about the correct handling of business logic are made for not covered topics in tests:
- Time related issues like
  - losing the signal right after reaching a key waypoint
  - losing and regaining the signal in frequent intervals (spamming)
  - losing the signal for longer periods
- Location related issues
  - missed key locations between loss location and regain location
  - losing the signal close to a key location

The tests for this task are tests 7 + 8.

## D: Comparison of direct method calls and event-driven updates

Direct method calls are straightforward, easy to understand and give immediate feedback. However, they also increase 
the coupling of the Services and bottleneck the flow by being the single point of failure. It is thus more reasonable 
to use for small scale applications.

Event-driven updates achieve loose coupling, scalability and, due to their (usual) asynchronous nature, can be 
responsive. The downside of this is an increased complexity with architectural setup, difficulties in debugging and 
managing the event resolve order. It is thus more approachable for larger-scale projects.

For this class I would recommend using an event-driven approach.

