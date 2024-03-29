; To run these tests from the lein REPL, run:
; => (require '[paip.6-tools.search-test] :reload-all)
;
; and the tests will run because of the (run-tests) invocation at the bottom.
(ns paip.6-tools.search-test
  (:use clojure.test)
  (:use paip.6-tools.search))

(deftest depth-first-search-test
  (is (= 12 (depth-first-search 1 #(= % 12) (finite-binary-tree 15))))
  (is (nil? (depth-first-search 1 #(= % 22) (finite-binary-tree 15)))))

(deftest breadth-first-search-test
  (is (= 12 (breadth-first-search 1 #(= % 12) (finite-binary-tree 15))))
  (is (nil? (breadth-first-search 1 #(= % 22) (finite-binary-tree 15)))))

(deftest best-first-search-test
  (is (= 12 (best-first-search 1 #(= % 12) (finite-binary-tree 15) (diff 12))))
  (is (nil? (best-first-search 1 #(= % 22) (finite-binary-tree 15) (diff 12)))))

(deftest sorter-test
  (is (= '(8 5 10 20 124 9991)
         ((sorter #(count (str %))) '(10 8 5) '(124 20 9991)))))

(deftest beam-search-test
  (is (nil? (beam-search 1 #(= % 12) (finite-binary-tree 15) (diff 12) 2)))
  (is (= 12 (beam-search 1 #(= % 12) (finite-binary-tree 15) (diff 12) 3))))

(run-tests)
