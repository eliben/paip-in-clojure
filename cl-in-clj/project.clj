(defproject cl-in-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Public Domain"
            :url "http://unlicense.org"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.trace "0.7.9"]]
  :main ^:skip-aot cl-in-clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
