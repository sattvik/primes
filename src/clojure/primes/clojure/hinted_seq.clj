(ns primes.clojure.hinted-seq
  "Generates primes using an infinite, lazy sieve.")

(defn divides?
  "Returns true if n is divisible by d."
  [^long n ^long d]
  (zero? (rem n d)))

(defn has-prime-factor?
  "Returns true if n has a factor in primes."
  [^long n primes]
  (some #(divides? n %) primes))

(defn next-prime
  "Given n and a list of prime factors, return the smallest number greater than
  or equal to n that is relatively prime to all of the numbers in known-primes."
  [^long n known-primes]
  (first (remove #(has-prime-factor? % known-primes) (iterate inc n))))

(def prime-seq
  ((fn next [^long n primes]
    (cons n (lazy-seq (next (next-prime (inc n) primes) (conj primes n)))))
    2 []))

(deftype LazySeqPrimes [prime-set]
  primes.Primes
  (isPrime [this n]
    (contains? prime-set n)))

(defn get-primes
  "Returns a list of all the prime numbers less than n"
  [^long n]
  (LazySeqPrimes. (apply hash-set (take-while #(< % n) prime-seq))))
