(ns primes.util
  "Provides some generic utilities for the primes suite")

(defn get-primes
  "Given a lazy sequence of primes, return a collection of all the primes up to
  n."
  [prime-seq n]
  (loop [primes prime-seq
         out    (transient [])]
    (let [p (first primes)]
      (if (< p n)
        (recur (next primes) (conj! out p))
        (persistent! out)))))

(definline realise
  "Given a lazy sequence, realise the first n items."
  [seq n]
  `(loop [^clojure.lang.ISeq seq# ~seq
          i# (long ~n)]
     (when-not (zero? i#)
       (recur (.next seq#) (dec i#)))))

(defn bitset->vector
  "Extracts all of the primes from a BitSet."
  [^java.util.BitSet bitset]
  (loop [i      3
         primes (transient [2])]
    (cond
      (>= i (.size bitset)) (persistent! primes)
      (.get bitset i)       (recur (+ i 2) primes)
      :else                 (recur (+ i 2) (conj! primes i)))))
