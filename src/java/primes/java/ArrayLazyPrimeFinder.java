package primes.java;

import java.util.*;

public class ArrayLazyPrimeFinder {
    static boolean divides(long n, long d) {
        return 0 == (n % d);
    }

    static boolean hasPrimeFactor(long n, long[] primes) {
        final long limit = (long) Math.sqrt(n);
	final int maxOff = (int) primes[0];
        for (int i = 1; i < maxOff; ++i) {
	    final long p = primes[i];
            if (p > limit) {
                return false;
            }
            if (divides(n, p)) {
                return true;
            }
        }
        return false;
    }

    static long nextPrime(long n, long[] primes) {
        for (;;n += 2) {
            if (!hasPrimeFactor(n, primes)) {
                return n;
            }
        }
    }

    public static Iterator<Long> primeIterator() {
        return new Iterator<Long>() {
            private long thePrime = 2;
            private long[] knownPrimes = new long[31];

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Long next() {
                long returnMe = thePrime;
                if (thePrime == 2) {
                    ++thePrime;
		    knownPrimes[0] = 1;
                } else {
		    addPrime(thePrime);
                    thePrime = nextPrime(thePrime+=2, knownPrimes);
                }
                return returnMe;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Ye canna' change " +
                        "the laws of mathematics!");
            }

	    private void addPrime(long aPrime) {
	        int nextPrimeLoc = (int) knownPrimes[0];
	        if (nextPrimeLoc == knownPrimes.length) {
		    long[] newPrimes = new long[nextPrimeLoc << 1];
		    System.arraycopy(knownPrimes, 0, newPrimes, 0, nextPrimeLoc);
		    knownPrimes = newPrimes;
		}
		knownPrimes[nextPrimeLoc] = aPrime;
		++knownPrimes[0];
	    }
        };
    }
}
