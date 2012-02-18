package primes.java.lazy;

import java.util.*;
import primes.Primes;
public class LazyPrimeGenerator {
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

    static Iterator<Long> primeIterator() {
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

    public static Primes getPrimes(long n) {
        final Set<Long> primes = new HashSet<Long>();
        Iterator<Long> primeSeq = primeIterator();
        for (Long prime = primeSeq.next(); prime < n; prime = primeSeq.next()) {
            primes.add(prime);
        }
        return new Primes() {
            @Override
            public boolean isPrime(long n) {
                return primes.contains(n);
            }
        };
    }
}
