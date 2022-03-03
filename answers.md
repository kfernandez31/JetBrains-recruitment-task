<div style="text-align: right"> Kacper Kramarz-Fernandez, 03.03.2022 </div>

# 1.What does the solve() method compute?
- For the sake of readability, I renamed `solve()`'s argument `k` to `r`.
- When referring to `help()`'s complexity, I'll use:
  -  $h(k,n)$ for the provided implementation,
  -  $h'(k,n)$ for my own implementation (discussed in paragraph 4),
  
  where $n:=$`vals.size()`.

The `solve()` method iterates over all $r-l+1$ values of `i`, calling `help()` with every `i` as its 2nd argument, outputting a list in every iteration. The result is thus a list of lists and solve's time and memory complexities are respectively:
$$O(solve) = (r-l+1)\cdot O(h(k,n))$$
$$O_{mem}(solve)=(r-l+1)\cdot O_{mem}(h(k,n))$$

In order to illustrate what `help()` does, let's introduce the following notation:
- $L=[a_0,\ldots,a_{n-1}]$ : a list of length $n$,
- () : an empty tuple, $(j_1,\ldots,j_{k-1})$ : a tuple of length $k-1$  satisfying $\forall_{i=1,...,k-2} j_i<j_{i+1}$
-  $L[i,...,n-1]$ : a suffix of list $L$ from index $i$ to $n-1$,
-  $@$ : symbol of concatenation of two lists, $\sum^{@}$ : sum in the sense of concatenation,
-  $\odot$ : operator that when applied as:
   $$(j_1, \ldots, j_{k-1})\odot L[j_{k-1}+1,...,n-1]$$
    outputs the given suffix with every element multiplied by $\prod_{i=1,\ldots,k-1}L[j_i]$. If the left operand is $()$, it results in identity. If the right operand is $[]$ (an empty list), the result is also $[]$.


In the example provided, we have $n=3$, $L=[2,5,1]$ (meaning `vals`) and $k=1,...,5$. We obtain:

- for $k=1$: 
  $$()\odot L[0,...,N-1] = [2,5,1]$$
- for $k=2$: 
$$((0)\odot L[1,2]) @ ((1)\odot L[2]) @ ((2)\odot []) = $$
$$=((0)\odot L[1,2]) @ ((1)\odot L[2])= [5,10,2]$$
We can see that any tuple with $i_{k-1}=n-1$ will yield an empty list in the $\odot$ operation. With conciseness in mind, I will omit empty lists further below.
- for $k=3$:
$$((0,1)\odot L[2]) = [10]$$
- $k=4,5$ both result in empty lists 

The result for the example is thus:
$$[[2,5,1], [5,10,2], [10], [], []]$$
For given $k$, `help()`'s result is:
$$\sum^{@}_{j_1<\ldots<j_{k-1}}(j_1,\ldots,j_{k-1})\odot L[j_{k-1}+1,\ldots,n-1]$$

# 2. What computational complexity does this method have? Why?
What is suboptimal in `help()` in help and will be adressed later on:
1. $O(n)$ space for `newVals`.
2. $O(n)$ space due to using streams.
3. $O(n)$ time for the loop that calls `List.remove()` on `newVals`.
4. $O(n)$ time due to using `map()`.

<h3><b> Time complexity </b></h3>
<hr>

We can bound the length of the input list in each of `help()`'s calls to the first input list's length - $n$. With each call, the input list's length is cut by $1$ and $k$ is decremented. Each call also performs a loop of at most $n$ iterations. Points 3. and 4. also take place. We obtain:
$$O(h(k,n)) = n\cdot n\cdot O(h(k-1,n-1))$$
We also know that:
$$\forall_{n'\leq n}O(h(1,n'))= n'\ \leq n^2$$
$$\forall_{k}O(h(k,0))= 1$$
Knowing that the recursion ends whenever either $k$ reaches $1$ or $n$ reaches $0$, we obtain:
$$O(h(k,n)) = n^{2min(k-1,n)}$$
However, to capture the time complexity more precisely, we can write:
$$O(h(k,n)) = n^2\cdot(n-1)^2\cdot\ldots\cdot(n-min(k-1,n)+1)^2 \leq \prod_{i=0}^{min(k-1, n)} (n-i+1)^2$$
(sidenote: such a product is sometimes called a <a href="https://en.wikipedia.org/wiki/Falling_and_rising_factorials">"falling factorial" </a>)

<h3><b> Memory complexity </b></h3>
<hr>

To calculate the memory complexity, we have to know how many tuples correspond to each possible suffix length.
We can express the complexity as the following sum:

$$O_{mem}(h(k,n)) = \sum_{i=0}^{n-k+1}1\cdot i\cdot\binom{n-i-1}{k-2}+ \sum_{i=0}^{n-1}1\cdot\binom{i}{k-2}(n-i+1),$$
where in the first sum (that corresponds to the total number of elements of the result):
- $1$ - choice for the tuple's last element,
- $i$ - length of suffix,
- $\binom{n-i-1}{k-2}$ - allocation of $n-i-1$ possible indices on the remaining $k-2$ positions in the tuple,
- $n\cdot\binom{n}{k-1}$ - for each possible tuples we have suffixes of at most length $n$,
  
and in the second sum (that's due to points 1-2 of this paragraph):
- $1$ - choice for the tuple's last element,
- $n-i+1$ - length of suffix,
- $\binom{i}{k-2}$ - allocating all $i$ indices lesser than $i$ on $k-2$ remaining positions in the tuple,

# 3. Can this algorithm be optimized?
Yes, refer to the following paragraph.


# 4. If yes, suggest your Java implementation of the optimized solve() method, then state and prove its time complexity.
What can be optimized is to get rid of the complexity that comes from points 1-4 of paragraph 2. as follows:
1. Using a "list view" (through the `List.subList()` method).
2. Not doing unnecessary copying by abandoning streams.
3. Same as 1.
4. Multiplicating a list's elements only when `k==1`.

We can also get rid of additional iterations that would call `help()` on an empty list by adding some conditions in `help()`'s below else-case and in its loop. By leaving all list creation to `solve()`, `help()` ends up as a void method that appends to its accumulator `List<Long> res`.

The code is as follows:

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(solve(new ArrayList<>(List.of(2L, 5L, 1L)), 1, 5));
    }

    private static List<List<Long>> solve(List<Long> vals, Integer l, Integer r) {
        ArrayList<List<Long>> res = new ArrayList<>();
        for (Integer i = l; i <= r; i++) {
            res.add(new ArrayList<>());
            help(res.get(i-l), vals, 1L, i);
        }
        return res;
    }

    private static void help(List<Long> res, List<Long> vals, Long factor, Integer k) {
        if (k == 1) {
            for (int i = 0; i < vals.size(); i++) {
                res.add(i, factor * vals.get(i));
            }
        } else if (k-1 < vals.size() && !vals.isEmpty()) {
            for (int i = 0; i < vals.size() - 1 && k-1 <= vals.size() - (i + 1); i++) {
                List<Long> suffix = vals.subList(i+1, vals.size());
                help(res, suffix, factor * vals.get(i), k - 1);
            }
        }
    }
}
```
<h3><b> Time complexity </b></h3>
<hr>

$$O(h'(k,n)) = n\cdot(n-1)\cdot\ldots\cdot(n-min(k-1,n)+1) \leq \prod_{i=0}^{min(k-1, n)} (n-i+1) = \sqrt{O(h(k,n))}$$
The lack of the exponent $2$ comes from getting rid of $n$-cost operations at each recursion level. 

<h3><b> Memory complexity </b></h3>
<hr>

$$O_{mem}(h'(k,n)) = \sum_{i=0}^{n-k+1}i\cdot\binom{n-i-1}{k-2}$$
Here we omit any redundant copying.
<hr>

`solve()`'s complexities when using my implementation for `help()` are then as follows:
$$O(solve') = (r-l+1)\cdot O(h'(k,n))$$
$$O_{mem}(solve')=(r-l+1)\cdot O_{mem}(h'(k,n))$$