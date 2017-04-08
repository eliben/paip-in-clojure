(ns paip.6-tools.pat-match-test
  (:use clojure.test)
  (:use paip.6-tools.pat-match))

(deftest pat-match-test
  (is (= {'?v 'c} (pat-match '(a ?v b) '(a c b))))
  (is (nil? (pat-match '(a ?v b) '(a c d)))))

(run-tests)
