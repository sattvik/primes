(ns primes.clojure.hinted-seq
  "Generates primes using an infinite, lazy sieve.  This version uses primitive
  hints to make the numeric operations more streamlined.")

(defn divides?
  "Returns true if n is divisible by d."
  [^long n ^long d]
  (zero? (rem n d)))

(defn has-prime-factor?
  "Returns true if n has a factor in primes."
  [^long n primes]
  (some #(divides? n %) (take-while #(<= % (Math/sqrt n)) primes)))

(defn next-prime
  "Given n and a list of prime factors, return the smallest number greater than
  or equal to n that is relatively prime to all of the numbers in known-primes."
  [^long n known-primes]
  (if (has-prime-factor? n known-primes)
    (recur (inc n) known-primes)
    n))

(def prime-seq
  ((fn next [^long p primes]
     (lazy-seq
       (cons p
         (next (next-prime (inc p) primes)
           (conj primes p)))))
    2 []))

(deftype LazySeqPrimes [prime-set]
  primes.Primes
  (isPrime [this n]
    (contains? prime-set n)))

(defn get-primes
  "Returns a list of all the prime numbers less than n"
  [^long n]
  (LazySeqPrimes. (apply hash-set (take-while #(< % n) prime-seq))))
