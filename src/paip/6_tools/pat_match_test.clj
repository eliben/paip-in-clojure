(ns paip.6-tools.pat-match-test
  (:use clojure.test)
  (:use paip.6-tools.pat-match))

(deftest pat-match-basic-var-test
  (is (= {'?v 'a} (pat-match '(?v) '(a))))
  (is nil? (pat-match '(?v) '()))

  (is (= {'?v 'c} (pat-match '(a ?v b) '(a c b))))
  (is (nil? (pat-match '(a ?v b) '(a c d))))

  (is (= {'?v 'b} (pat-match '(a t ?v) '(a t b))))

  (is (= {'?v 'b, '?u 'a} (pat-match '(?u t ?v) '(a t b))))

  (is (= {'?v 'b} (pat-match '(?v t ?v) '(b t b))))
  (is nil? (pat-match '(?v t ?v) '(a t b)))
  )

(deftest pat-match-is-test
  (is (= {'?v 8} (pat-match '(a = (?is ?v number?)) '(a = 8))))
  (is nil? (pat-match '(a = (?is ?v number?)) '(a = jk)))
  )

(run-tests)
