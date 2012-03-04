(ns primes.java.lazy-array-cached
  "Finds primes up to n using a lazy trial by division implemented in Java.
  This method differs from the smarter prime finder by using arrays instead of
  ArrayLists to keep track of primes."
  (:require [primes.util :as util])
  (:import primes.java.ArrayLazyPrimeFinder))

(def prime-seq
  "A lazy sequence of primes, implemented as a Java iterator."
  ((fn inner [^java.util.Iterator iter]
     (lazy-seq
       (cons (.next iter) (inner iter))))
     (ArrayLazyPrimeFinder/primeIterator)))

(defn get-primes
  "Gets all of the primes less than n."
  [n]
  (util/get-primes prime-seq n))

(defn realise
  "Realise the first n items in prime-seq."
  [^long n]
  (util/realise prime-seq n))
