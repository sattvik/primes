package primes.java;

import java.util.*;
import primes.Primes;

public class BasicLazyPrimeFinder {
    static boolean divides(long n, long d) {
        return 0 == (n % d);
    }

    static boolean hasPrimeFactor(long n, List<Long> primes) {
        for (long p : primes) {
            if (divides(n, p)) {
                return true;
            }
        }
        return false;
    }

    static long nextPrime(long n, List<Long> primes) {
        for (;;++n) {
            if (!hasPrimeFactor(n, primes)) {
                return n;
            }
        }
    }

    public static Iterator<Long> primeIterator() {
        return new Iterator<Long>() {
            private long thePrime = 2;
            private List<Long> knownPrimes = new ArrayList<Long>();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Long next() {
                long returnMe = thePrime;
                knownPrimes.add(thePrime);
                thePrime = nextPrime(++thePrime, knownPrimes);
                return returnMe;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Ye canna' change " +
                        "the laws of mathematics!");
            }
        };
    }
}
