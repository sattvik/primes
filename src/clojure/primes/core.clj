(ns primes.core
  (:require [primes.java.bitset :as java-bitset]))

(def ranges [10000000])

(def all-generators
  {:java-bitset java-bitset/get-primes
   })

(defn handle-bad-args [bad-args]
  (apply println "Bad argumets:" bad-args)
  (apply println "Valid arguments are:" (map name (keys all-generators))))

(defn time-invoke [test-fn]
  (let [start (System/nanoTime)]
    (test-fn)
    (/ (- (System/nanoTime) start) 1000000.0)))

(defn benchmark [generator-key]
  (println "About to benchmark:" (name generator-key))
  (let [generator (all-generators generator-key)]
    (doseq [max ranges]
      (print "Getting" max "primes: ")
      (let [millis (time-invoke #(generator max))]
        (println millis)))))

(defn benchmark-all [generators]
  (map benchmark generators))

(defn -main [& args]
  (let [generators (or (seq (map keyword args)) (keys all-generators))]
    (if-let [invalid-generators (seq (remove #(contains? all-generators %) generators))]
      (handle-bad-args (map name invalid-generators))
      (benchmark-all generators))))
