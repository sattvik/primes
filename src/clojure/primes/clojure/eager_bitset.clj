(ns primes.clojure.eager-bitset
  "Implements the Sieve of Eratosthenes in Clojure backed by a
  java.util.Bitset."
  (:import java.util.BitSet))

(set! *unchecked-math* true)

(def ^:const even-mask 0x5555555555555555)

(defn get-primes
  "Returns a list of all the prime numbers less than n"
  [^long n]
  (let [sqrtn    (long (Math/sqrt (double n)))
        numlongs (quot (+ n 63) 64)
        bitset   (doto (BitSet/valueOf (long-array numlongs even-mask))
                   (.set 1)
                   (.clear 2))
        n        (.size bitset)]
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
