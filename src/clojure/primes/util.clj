(ns primes.util
  "Provides some generic utilities for the primes suite")

(defn get-primes
  "Given a lazy sequence of primes, return a collection of all the primes up to
  n."
  [prime-seq n]
  (loop [primes prime-seq
         out    (transient [])]
    (if (< (first primes) n) 
      (recur (next primes) (conj! out n))
      (persistent! out))))
