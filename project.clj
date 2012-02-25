(defproject primes "0.1.0-SNAPSHOT"
  :description "Code for the ‘Crunching numbers in Clojure’ presentation for Clojure/West"
  :main primes.core
  :source-path "src/clojure"
  :java-source-path "src/java"
  :test-path "test/clojure"
  :warn-on-reflection true
  :dependencies [[org.clojure/clojure "1.4.0-beta1"]]
  :dev-dependencies [[vimclojure/server "2.3.1"]])
