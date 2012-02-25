package primes.java;

import java.util.*;

public class SmarterLazyPrimeFinder {
    static boolean divides(long n, long d) {
        return 0 == (n % d);
    }

    static boolean hasPrimeFactor(long n, List<Long> primes) {
        final long limit = (long) Math.sqrt(n);
        for (long p : primes) {
            if (p > limit) {
                return false;
            }
            if (divides(n, p)) {
                return true;
            }
        }
        return false;
    }

    static long nextPrime(long n, List<Long> primes) {
        for (;;n += 2) {
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
                if (thePrime == 2) {
                    ++thePrime;
                } else {
                    knownPrimes.add(thePrime);
                    thePrime = nextPrime(thePrime+=2, knownPrimes);
                }
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
