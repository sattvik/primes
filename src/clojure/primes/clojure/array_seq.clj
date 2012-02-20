(ns primes.clojure.array-seq
  "Generates primes using an infinite, lazy sieve.")

(defn divides?
  "Returns true if n is divisible by d."
  [^long n ^long d]
  (zero? (rem n d)))

(defn has-prime-factor?
  "Returns true if n has a factor in primes."
  [^long n ^longs primes]
  (let [count (aget primes 0)]
    (loop [i 1]
      (cond 
        (= i count) false
        (divides? n (aget primes i)) true
        :els (recur (inc i))))))

(defn next-prime
  "Given n and a list of prime factors, return the smallest number greater than
  or equal to n that is relatively prime to all of the numbers in known-primes."
  [^long n ^longs known-primes]
  (if (has-prime-factor? n known-primes)
    (recur (inc n) known-primes)
    n))

(defn- add-prime
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
  ((fn next [^long n ^longs primes]
    (cons n (lazy-seq (next (next-prime (inc n) primes) (add-prime primes n)))))
    2 (let [arr (long-array 2)]
        (aset arr 0 1)
        arr)))

(deftype LazySeqPrimes [prime-set]
  primes.Primes
  (isPrime [this n]
    (contains? prime-set n)))

(defn get-primes
  "Returns a list of all the prime numbers less than n"
  [^long n]
  (LazySeqPrimes. (apply hash-set (take-while #(< % n) prime-seq))))
