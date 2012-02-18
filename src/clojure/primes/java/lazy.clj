(ns primes.java.lazy
  (:import  primes.java.lazy.LazyPrimeGenerator))

(defn get-primes [n]
  (LazyPrimeGenerator/getPrimes n))
