(ns primes.clojure.lazy-seq
  "Generates primes using an infinite, lazy sieve.")

(defn divides?
  "Returns true if n is divisible by d."
  [n d]
  (zero? (rem n d)))

(defn has-prime-factor?
  "Returns true if n has a factor in primes."
  [n primes]
  (some #(divides? n %) (take-while #(<= % (Math/sqrt n)) primes)))

(defn next-prime
  "Given n and a list of prime factors, return the smallest number greater than
  or equal to n that is relatively prime to all of the numbers in known-primes."
  [n known-primes]
  (if (has-prime-factor? n known-primes)
    (recur (inc n) known-primes)
    n))

(def prime-seq
  ((fn next [p primes]
     (lazy-seq
       (cons p
         (next (next-prime (inc p) primes)
           (conj primes p)))))
    2 []))

(defn get-primes [n]
  "Returns a sequence of all primes less than n."
  (doall (take-while #(< % n) prime-seq)))
