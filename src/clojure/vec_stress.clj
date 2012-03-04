(ns vec-stress)


(defn get-and-add [v]
  (conj v (reduce + (repeatedly 100 #(rand-nth v)))))

(defn append [v]
  (into v (repeatedly 50 #(rand-int 100))))

(defn pop-off
  ([v ^long n]
   (if (or (zero? n) (empty? v))
     v
     (recur (pop v) (dec n))))
  ([v] (pop-off v (rand-int (/ (count v) 2)))))

(defn chop-up [v]
  (let [start (rand-int (count v))
        end   (+ start (rand-int (- (count v) start)))]
    (subvec v start end)))


(defn do-something [v]
  (if (empty? v)
    (append v)
    (let [x (rand-int 10)]
      (cond
        (< x 3) (get-and-add v)
        (< x 6) (append v)
        (< x 9) (pop-off v)
        :else   (chop-up v)))))

(defn test-it [v]
  (let [v (into v (vec (repeatedly 100 #(rand-int 100))))]
    (time 
      (first (drop 1000000 (iterate do-something v))))))
