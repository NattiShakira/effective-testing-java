# 1. Specification testing

I applied the seven step principle from chapter 2 in the book. Meaning first I read the README.md, then read the code to

understand what's going on. Then I identified some partitions. In order of tests: empty list of candidates, 

single elements for candidates, candidates greater than target, target being 0, target smaller than smallest candidate, 

having multiple combinations be valid, having a negative target, having negative candidates

the possible inputs for candidates are: an empty list, a list with 1 entry, a list with multiple entries, positive integers, negative integers, zero

the possible inputs for targets are: zero, a positive int., or a negative int. 

Therefore the partitions are:

- candidates contains no entries in list.

- candidates contain multiple entries in list.

- candidates contain multiple valid combinations in list.

- candidates contain only negative integers.

- candidates contain only positive integers.

- candidates contain only 0.

- candidates contain a list of mixed positive and negative integers, as well as 0.

- target is 0

- target is positive

- target is negative

- target is smaller than smallest candidate

- target is greater than all candidates

the boundaries I identified for this problem are:

- empty candidate list

- target smaller than smallest candidate

- target larger than all candidates

- negative candidates

I then implemented test cases based on these assumptions.

The tests, testNegativeCandidates and testMixedCandidates both provoke an out of bounds error, 

as there is no input sanitation for negative numbers. line 20 infinitely recurs. 

The reason for this is, that the condition target >= candidates[i] will always be true

if the target is positive, and some of the candidates negative.

The fix for this is to skip negative candidates, if that is the intended behaviour.

This could be done by adding:

(this fix will be fixed in part 3, mutation testing)
```
if (candidates[i] < 0)
    {
        continue;
    }
```

at the start of the for loop (line 19)


The test negativeCandidatesAndTarget fails too, but it's unclear to me 

whether this is a bug or not, as negative integers are not mentioned in the requirements.

I'm assuming negative integers are unwanted inputs, therefore no fix is needed, as it returns

an empty list.

# 2. Structural testing

This test suite already gives a very good coverage, only missing line 7, which is the 

Class initiation. All the structural testing I did, was just one test, which tests

said Initiation by testing if an instance of the class is created. With this

I have a suite that has 100% line and branch coverage.

# 3. Mutation testing

The mutation tests uncovered an issue with the bugfix I provided above. It missed

cases where the candidate is 0.

```
if (candidates[i] <= 0)
    {
        continue;
    }
```

This is the fixed fix.

It also uncovered another problem, removing line 2 didn't break any tests, 

as all my tests had a pre-sorted candidate list. I fixed this by adding a 

test case with an unsorted list.

With these fixes, the mutation tests let no mutants survive.







