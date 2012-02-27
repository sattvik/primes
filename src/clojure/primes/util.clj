(ns primes.util
  "Provides some generic utilities for the primes suite")

(defn get-primes
  "Given a lazy sequence of primes, return a collection of all the primes up to
  n."
  [^clojure.lang.ISeq prime-seq
   ^long n]
  (loop [primes prime-seq
         out    ^clojure.lang.ITransientCollection (transient [])]
    (let [p (.longValue ^Long (.first primes))]
      (if (< p n)
        (recur (.next primes) (.conj out (Long. p)))
        (persistent! out)))))

(defn bitset->vector
  "Extracts all of the primes from a BitSet."
  [^java.util.BitSet bitset]
  (loop [i      3
         primes (transient [2])]
    (cond
      (>= i (.size bitset)) (persistent! primes)
      (.get bitset i)       (recur (+ i 2) primes)
      :else                 (recur (+ i 2) (conj! primes i)))))
