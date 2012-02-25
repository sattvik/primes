(ns primes.java.eager-bitset
  "Implements the Sieve of Eratosthenes in Java backed by a java.util.Bitset."
  (:import  primes.java.EagerBitSetPrimeFinder))

(defn get-primes [n]
  "Returns a bitset that contains all of the primes up to n."
  (EagerBitSetPrimeFinder/getPrimes n))
