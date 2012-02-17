(ns primes.java.bitset
  (:import  primes.java.bitset.BitSetPrimeGenerator))

(defn get-primes [n]
  (BitSetPrimeGenerator/getPrimes n))
