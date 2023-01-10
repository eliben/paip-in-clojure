(ns paip.11-logic.unification-test
  (:use clojure.test)
  (:use paip.11-logic.unification))

(deftest unify-simple
  (is (= {} (unify 'a 'a)))
  (is (= {} (unify '(?x ?y) '(?x ?y))))
  (is (nil? (unify 'a 'b)))

  (is (= {'?x 2} (unify '(?x + 1) '(2 + 1))))
  (is (= {'?y 1} (unify '(2 + 1) '(2 + ?y))))

  (is (= {'?x 2, '?y 1} (unify '(?x + 1) '(2 + ?y))))
  (is (= {'?x '?y} (unify '?x '?y)))
  (is (= {'?x '?y} (unify '(?x ?x) '(?y ?y))))
  (is (= {'?x '?y} (unify '(?x ?x ?x) '(?y ?y ?y))))
  (is (= {'?x '?y} (unify '(?x ?y) '(?y ?x))))
  (is (= {'?x '?y, '?a '?y} (unify '(?x ?y ?a) '(?y ?x ?x))))

  (is (nil? (unify '(?x aa) 'aa))))

(deftest unify-occurs-fail
  (is (nil? (unify '?x '(f ?x))))
  (is (nil? (unify '(?x ?y) '((f ?y) (f ?x))))))

(run-tests)
