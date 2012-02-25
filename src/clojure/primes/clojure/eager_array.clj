(ns primes.clojure.eager-array
  "Implements the Sieve of Eratosthenes in Clojure backed by a
  arrays of long primitives."
  (:import [clojure.lang Numbers RT]))

(set! *unchecked-math* true)

(defn get-primes
  "Returns a list of all the prime numbers less than n"
  [^long n]
  (let [imax (long (Math/ceil (Math/sqrt n)))
        numlongs (inc (quot n 64))
        ^longs bitset (long-array numlongs 0x5555555555555555)]
    (aset bitset 0 0x5555555555555553)
    (loop [i 3
           j 9]
      (cond
        (>= i imax) :done
        (zero? j)
          (let [array-off (quot i 64)
                long-off  (rem i 64)]
            (if (Numbers/testBit (RT/aget bitset (int array-off)) long-off)
              (recur (+ i 2) 0)
              (recur i (* i i))))
        (>= j n) 
          (recur (+ i 2) 0)
        :else
          (let [array-off (quot j 64)
                long-off  (rem j 64) 
                old-long  (RT/aget bitset (int array-off))
                new-long  (Numbers/setBit old-long long-off)]
            (RT/aset bitset (int array-off) new-long)
            (recur i (+ i j)))))
    bitset))

(set! *unchecked-math* false)
