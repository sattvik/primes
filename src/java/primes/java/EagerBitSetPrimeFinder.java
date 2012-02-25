package primes.java;

import java.util.Arrays;
import java.util.BitSet;

public class EagerBitSetPrimeFinder {
    public static BitSet getPrimes(int n) {
        // create a bit set with all even numbers marked as composite
        final long[] startState = new long[(n + 63) / 64];
        Arrays.fill(startState, 0x5555555555555555L);
        BitSet sieve = BitSet.valueOf(startState);
        n = sieve.size();

        // zero and one are not prime
        sieve.set(1);

        // two is prime
        sieve.clear(2);

        // find the primes
        int sqrtn = (int) Math.floor(Math.sqrt(n));
        for (int i = 3; i <= sqrtn; i += 2) {
          // skip over known composites
          if (sieve.get(i)) {
            continue;
          }
          // mark all the multiples of i as primes
          for (int j = i * i; j < n; j += i) {
            sieve.set(j);
          }
        }
        return sieve;
    }
}
