(ns paip.utils-test
  (:use clojure.test)
  (:use paip.utils))

(deftest index-in-seq-test
  (is (= 0 (index-in-seq '(10 20 30) 10)))
  (is (= 1 (index-in-seq '(10 20 30) 20)))
  (is (= 2 (index-in-seq '(10 20 30) 30)))
  (is (= -1 (index-in-seq '(10 20 30) 50)))

  (is (= 1 (index-in-seq '(10 20 30 20) 20)))
  (is (= 3 (index-in-seq '(10 20 30 20) 20 2)))
  (is (= -1 (index-in-seq '(10 20 30 20 50) 20 4)))

  (is (= -1 (index-in-seq [10 20 30 20] 22)))
  (is (= 1 (index-in-seq [10 20 30 20] 20)))
  (is (= 3 (index-in-seq [10 20 30 20] 20 2)))

  (is (= -1 (index-in-seq '() 10)))
  )

(deftest cons?-test
  (is (cons? '(1)))
  (is (cons? '(1 2)))
  (is (cons? '(1 2 3)))

  (is (not (cons? [1 2 3])))
  (is (not (cons? '())))
  (is (not (cons? nil)))
  (is (not (cons? 'sdf)))
  )

(run-tests)
