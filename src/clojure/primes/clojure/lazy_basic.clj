(ns primes.clojure.lazy-basic
  "Finds primes in an infinite, lazy sequence via trial by division."
  (:require [primes.util :as util]))

(defn divides?
  "Returns true if n is divisible by d."
  [n d]
  (zero? (rem n d)))

(defn has-prime-factor?
  "Returns true if n has a factor in primes."
  [n primes]
  (some #(divides? n %) primes))

(defn next-prime
  "Given n and a list of prime factors, return the smallest number greater than
  or equal to n that is relatively prime to all of the numbers in known-primes."
  [n known-primes]
  (if (has-prime-factor? n known-primes)
    (recur (inc n) known-primes)
    n))

(def prime-seq
  "A lazy sequence of prime numbers."
  ((fn inner [p primes]
     (lazy-seq
       (cons p
             (inner (next-prime (inc p) primes)
                    (conj primes p)))))
     2 []))

(defn get-primes
  "Gets all of the primes less than n."
  [n]
  (util/get-primes prime-seq n))

(defn realise
  "Realise the first n items in prime-seq."
  [^long n]
  (util/realise prime-seq n))
