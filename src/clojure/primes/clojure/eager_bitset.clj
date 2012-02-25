(ns primes.clojure.eager-bitset
  "Implements the Sieve of Eratosthenes in Clojure backed by a
  java.util.Bitset."
  (:import java.util.BitSet))

(set! *unchecked-math* true)

(defn get-primes
  "Returns a list of all the prime numbers less than n"
  [^long n]
  (let [n      (quot (+ n 63) 64)
        bitset (doto (BitSet/valueOf (long-array n 0x5555555555555555))
                 (.set 1)
                 (.clear 2))
        n      (.size bitset)
        sqrtn  (long (Math/sqrt n))]
    (loop [i 3
           j 9]
      (cond
        (>= i sqrtn) :done
        (zero? j)
          (if (.get bitset i)
            (recur (+ i 2) 0)
            (recur i (* i i)))
        (>= j n) 
          (recur (+ i 2) 0)
        :else
          (do
            (.set bitset j)
            (recur i (+ i j)))))
    bitset))

(set! *unchecked-math* false)
