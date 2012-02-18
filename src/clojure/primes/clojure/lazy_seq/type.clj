(ns primes.clojure.lazy-seq.type
  "Wraps the lazy sequence implementation with a type."
  (:require [primes.clojure.lazy-seq :as lazy-seq]))

(deftype LazySeqPrimes [prime-set]
  primes.Primes
  (isPrime [this n]
    (contains? prime-set n)))

(defn get-primes
  "Returns a Primes for all primes less than n."
  [n]
  (LazySeqPrimes. (apply hash-set (lazy-seq/get-primes n))))
