(ns primes.clojure.lazy-sminted
  "Finds primes in an infinite, lazy sequence via trial by division.  This
  implementation improves on the basic lazy prime finder by limiting the amout
  of trial divsion.  It also make use of primitive hints."
  (:require [primes.util :as util]))

(defn divides?
  "Returns true if n is divisible by d."
  [^long n ^long d]
  (zero? (rem n d)))

(defn has-prime-factor?
  "Returns true if n has a factor in primes."
  [^long n primes]
  (some #(divides? n %)
        (take-while #(<= % (Math/sqrt n))
                    primes)))

(defn next-prime
  "Given n and a list of prime factors, return the smallest number greater than
  or equal to n that is relatively prime to all of the numbers in known-primes."
  [^long n known-primes]
  (if (has-prime-factor? n known-primes)
    (recur (+ 2 n) known-primes)
    n))

(def prime-seq
  "A lazy sequence of prime numbers."
  (cons 2
        ((fn inner [^long p primes]
           (lazy-seq
             (cons p
                   (inner (next-prime (+ 2 p) primes)
                          (conj primes p)))))
           3 [])))

(defn get-primes
  "Gets all of the primes less than n."
  [n]
  (util/get-primes prime-seq n))
