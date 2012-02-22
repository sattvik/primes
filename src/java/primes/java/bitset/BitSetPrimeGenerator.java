package primes.java.bitset;

import java.util.BitSet;
import primes.Primes;

public class BitSetPrimeGenerator {
    public static Primes getPrimes(long n) {
        if (n >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The BitSetPrimeGenerator only" +
                    " supports a maximum of " + Integer.MAX_VALUE);
        }
        return new BitSetPrimes((int) n);
    }

    /** Primes backed by a BitSet instance. */
    private static class BitSetPrimes implements Primes {
        private final BitSet sieve;

        public BitSetPrimes(int n) {
            sieve = new BitSet(n);
            // zero and one are not prime
            sieve.set(0);
            sieve.set(1);

            // find the primes
            int sqrtn = (int) Math.floor(Math.sqrt(n));
            for (int i = 2; i <= sqrtn; i = sieve.nextClearBit(++i)) {
                // mark all the multiples of i as primes
                for (int j = i * i; j < n; j += i) {
                    sieve.set(j);
                }
            }
        }

        @Override
        public boolean isPrime(long n) {
            return !sieve.get((int) n);
        }
        
    }
}
