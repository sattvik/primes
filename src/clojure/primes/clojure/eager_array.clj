(ns primes.clojure.eager-array
  "Implements the Sieve of Eratosthenes in Clojure backed by a
  arrays of long primitives."
  (:import [clojure.lang Numbers RT]))

(set! *unchecked-math* true)

(def ^:const even-mask 0x5555555555555555)

(defn get-primes
  "Returns a list of all the prime numbers less than n"
  [^long n]
  (let [sqrtn         (long (Math/sqrt (double n)))
        numlongs      (quot (+ n 63) 64)
        ^longs bitset (long-array numlongs even-mask)
        n             (* numlongs 64)]
    (aset bitset 0 (bit-xor even-mask 0x06))
    (loop [i 3
           j 9]
      (cond
        (>= i sqrtn) :done
        (zero? j)
          (let [array-off (quot i 64)
                long-off  (rem i 64)]
            (if (Numbers/testBit (aget bitset array-off) long-off)
              (recur (+ i 2) 0)
              (recur i (* i i))))
        (>= j n) 
          (recur (+ i 2) 0)
        :else
          (let [array-off (quot j 64)
                long-off  (rem j 64) 
                old-long  (aget bitset array-off)
                new-long  (Numbers/setBit old-long long-off)]
            (aset bitset array-off new-long)
            (recur i (+ i j)))))
    bitset))

(set! *unchecked-math* false)
