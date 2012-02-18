(ns primes.core)

(def default-samples 5)
(def default-n 100000)

(def algorithms
  {:java-bitset      'primes.java.bitset
   :java-lazy        'primes.java.lazy
   :clojure-lazy     'primes.clojure.lazy-seq
   :clojure-hinted   'primes.clojure.hinted-seq})

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
  [algo-key]
  (let [ns-sym (algorithms algo-key)]
    (require :reload ns-sym)
    (ns-resolve ns-sym 'get-primes)))

(defn benchmark [samples max algorithm]
  (print (str (name algorithm) \,))
  (flush)
  (let [times (doall
                (for [i (range samples)]
                  (let [algo-fn (reload-algo-function algorithm)
                        time    (time-invoke #(algo-fn max))]
                    (if (= i (dec samples))
                      (println time)
                      (print (str time \,)))
                    (flush)
                    time)))]
    (let [[avg sd] (compute-stats times)]
      (println "  average:" avg)
      (println "  std dev:" sd))))

(defn benchmark-all [samples n algorithms]
  (dorun (map #(benchmark samples n %) algorithms)))

(defn to-num [n default-value]
  (cond
    (number? n) n
    (string? n) (Long/parseLong n)
    :default default-value))

(defn -main [samples n & args]
  (let [samples (to-num samples default-samples)
        n       (to-num n default-n)
        algos   (or (seq (map keyword args)) (keys algorithms))]
    (if-let [invalid-algos (seq (remove #(contains? algorithms %) algos))]
      (handle-bad-args (map name invalid-algos))
      (benchmark-all samples n algos))))
