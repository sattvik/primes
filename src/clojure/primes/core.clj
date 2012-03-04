(ns primes.core)

(def default-warmup-cycles 5)
(def default-samples 5)
(def default-n 100000)

(def algorithms
  {:java-lazy-basic              'primes.java.lazy-basic
   :clojure-lazy-basic           'primes.clojure.lazy-basic
   :clojure-lazy-hinted          'primes.clojure.lazy-hinted
   :java-lazy-smarter            'primes.java.lazy-smarter
   :clojure-lazy-smarter         'primes.clojure.lazy-smarter
   :clojure-lazy-sminted         'primes.clojure.lazy-sminted
   :clojure-lazy-exploitive      'primes.clojure.lazy-exploitive
   :clojure-lazy-array           'primes.clojure.lazy-array
   :clojure-lazy-array-unchecked 'primes.clojure.lazy-array-unchecked
   :clojure-lazy-inline          'primes.clojure.lazy-inline
   :java-lazy-array              'primes.java.lazy-array
   :java-eager-bitset            'primes.java.eager-bitset
   :clojure-eager-bitset         'primes.clojure.eager-bitset
   :clojure-eager-array          'primes.clojure.eager-array})

(defn handle-bad-args
  "Handles the case of bad arguments."
  [bad-args]
  (apply println "Bad argumets:" bad-args)
  (apply println "Valid arguments are:" (map name (keys algorithms))))

(defn time-invoke [f]
  "Returns the amount of time it takes to invoke the given function,
  in milliseconds."
  (let [start (System/nanoTime)]
    (f)
    (/ (- (System/nanoTime) start) 1000000.0)))

(defn compute-stats [samples]
  "Computes the mean and standard deviation of a list of samples."
  (let [sum          #(reduce + %)
        mean         #(/ (sum %) (count %))
        square       #(* % %)
        samples-mean (mean samples)
        samples-sd   (Math/sqrt (mean (map #(square (- % samples-mean)) samples)))]
    [samples-mean samples-sd]))

(defn reload-algo-function
  "Returns the function that implements the given algorithm,
  forcing recompilation of the code."
  [algo-key fn-sym]
  (let [ns-sym (algorithms algo-key)]
    (require :reload ns-sym)
    (ns-resolve ns-sym fn-sym)))

(defn benchmark [warmup-cycles samples max algorithm]
  (print (name algorithm))
  (flush)
  (let [runs    (+ warmup-cycles samples)
        result  ((reload-algo-function algorithm 'get-primes) max)
        fn-sym  (if (vector? result) 'realise 'get-primes)
        cnt     (if (vector? result) (count result) max)
        times   (doall
                  (for [i (range runs)]
                    (let [algo-fn (reload-algo-function algorithm fn-sym)
                          time    (time-invoke #(algo-fn cnt))]
                      (cond
                        (< i warmup-cycles) (print \,)
                        (= i (dec runs)) (println "done!")
                        :else      (print \.))
                      (flush)
                      time)))]
    (drop warmup-cycles times)))

(defn benchmark-all [warmup-cycles samples max algorithms]
  (zipmap algorithms
          (map #(benchmark warmup-cycles samples max %) algorithms)))

(defn to-num [n default-value]
  (cond
    (number? n) n
    (string? n) (Long/parseLong n)
    :default default-value))

(defn analyse [data]
  (println)
  (doseq [[algorithm times] data]
    (println (name algorithm))
    (let [[mean sd] (compute-stats times)]
      (println "  mean:" mean)
      (println "    sd:" sd))))

(defn write-stats [data samples n]
  (doseq [[algorithm times] data]
    (let [algo-name  (name algorithm)
          file       (str "stats-" algo-name \- samples \- n ".clj")
          [mean sd]  (compute-stats times)
          out-map    {:algorithm algo-name
                      :n         n
                      :samples   samples
                      :times     (apply vector times)
                      :mean      mean
                      :sd        sd}]
      (spit file (pr-str out-map)))))

(defn -main [warmup-cycles samples n & args]
  (let [warmup-cycles (to-num warmup-cycles default-warmup-cycles)
        samples (to-num samples default-samples)
        n       (to-num n default-n)
        algos   (or (seq (map keyword args)) (keys algorithms))]
    (if-let [invalid-algos (seq (remove #(contains? algorithms %) algos))]
      (handle-bad-args (map name invalid-algos))
      (let [data (benchmark-all warmup-cycles samples n algos)]
        (analyse data)
        (write-stats data samples n)))))
