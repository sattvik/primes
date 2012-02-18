(ns primes.clojure.lazy-seq
  "Generates primes using an infinite, lazy sieve.")

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
  (first (remove #(has-prime-factor? % known-primes) (iterate inc n))))

(def prime-seq
  ((fn next [n primes]
    (cons n (lazy-seq (next (next-prime (inc n) primes) (conj primes n)))))
    2 []))

(deftype LazySeqPrimes [prime-set]
  primes.Primes
  (isPrime [this n]
    (contains? prime-set n)))

(defn get-primes
  "Returns a Primes for all primes less than n."
  [n]
  (LazySeqPrimes. (apply hash-set (take-while #(< % n) prime-seq))))
