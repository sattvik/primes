(ns primes.clojure.lazy-inline
  "Finds primes as an infinite, lazy sequence via trial by division.  This
  version improves on the unchecked array-based method by inlining the divides?
  operation."
  (:require [primes.util :as util]))

(set! *unchecked-math* true)

(definline divides?
  "Returns true if n is divisible by d."
  [n d]
  `(zero? (rem ~n ~d)))

(definline has-prime-factor?
  "Returns true if n has a factor in primes."
  [n ^longs primes]
  `(let [c#     (aget ~primes 0)
         sqrtn# (long (Math/sqrt (double ~n)))]
     (loop [i# 1]
       (when-not (= i# c#)
         (let [p# (aget ~primes i#)]
           (cond
             (> p# sqrtn#)    false
             (divides? ~n p#) true
             :else            (recur (inc i#))))))))

(definline next-prime
  "Given n and a list of prime factors, return the smallest number greater than
  or equal to n that is relatively prime to all of the numbers in known-primes."
  [n known-primes]
  `(loop [n# ~n]
     (if (has-prime-factor? n# ~known-primes)
       (recur (+ 2 n#))
       n#)))

(defn- add-prime
  "Adds a prime to the array of primes, doubling the size of the vector, if
  necessary."
  [^longs primes ^long p]
  (let [next-loc (aget primes 0)
        size     (alength primes)]
    (if (= size next-loc)
      (let [new-primes (long-array (* 2 size))]
        (System/arraycopy primes     0
                          new-primes 0
                          size)
        (recur new-primes p))
      (do
        (aset primes next-loc p)
        (aset primes 0 (inc next-loc))
        primes))))

(def prime-seq
  "A lazy sequence of prime numbers."
  (cons 2
        ((fn inner [^long p ^longs primes]
           (lazy-seq
             (cons p
                   (inner (next-prime (+ 2 p) primes)
                          (add-prime primes p)))))
           3 (let [arr (long-array 32)]
               (aset arr 0 1)
               arr))))

(defn get-primes
  "Gets all of the primes less than n."
  [n]
  (util/get-primes prime-seq n))

(defn realise
  "Realise the first n items in prime-seq."
  [^long n]
  (util/realise prime-seq n))

(set! *unchecked-math* false)
