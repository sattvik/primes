(defproject primes "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :main primes.core
  :source-path "src/clojure"
  :java-source-path "src/java"
  :test-path "test/clojure" :warn-on-reflection true
  :dependencies [[org.clojure/clojure "1.3.0"]]
  :multi-deps {"1.1" [[org.clojure/clojure "1.1.0"]]
               "1.2" [[org.clojure/clojure "1.2.1"]]
               "1.4" [[org.clojure/clojure "1.4.0-beta1"]]}
  :dev-dependencies [[vimclojure/server "2.3.1"]
                     [midje "1.3.1"]]
  :plugins [[lein-multi "1.1.0"]])
