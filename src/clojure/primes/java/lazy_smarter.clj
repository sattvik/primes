(ns primes.java.lazy-smarter
  "Finds primes up to n using a lazy trial by division implemented in Java.
  This method improves on the basic prime finder by limiting the number of
  divisions that takes place."
  (:require [primes.util :as util])
  (:import primes.java.SmarterLazyPrimeFinder))

(def prime-seq
  "A lazy sequence of primes, implemented as a Java iterator."
  (iterator-seq (SmarterLazyPrimeFinder/primeIterator)))

(defn get-primes
  "Gets all of the primes less than n."
  [n]
  (util/get-primes prime-seq n))
