(ns primes.java.lazy-basic
  "Finds primes up to n using a lazy trial by division implemented in Java."
  (:require [primes.util :as util])
  (:import primes.java.BasicLazyPrimeFinder))

(def prime-seq
  "A lazy sequence of primes, implemented as a Java iterator."
  (iterator-seq (BasicLazyPrimeFinder/primeIterator)))

(defn get-primes
  "Gets all of the primes less than n."
  [n]
  (util/get-primes prime-seq n))

(defn realise
  "Realise the first n items in prime-seq."
  [^long n]
  (util/realise prime-seq n))
