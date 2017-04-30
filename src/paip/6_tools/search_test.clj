(ns paip.6-tools.search-test
  (:use clojure.test)
  (:use paip.6-tools.search))

(deftest depth-first-search-test
  (is (= 12 (depth-first-search 1 #(= % 12) (finite-binary-tree 15))))
  (is (nil? (depth-first-search 1 #(= % 22) (finite-binary-tree 15))))
  )

(deftest breadth-first-search-test
  (is (= 12 (breadth-first-search 1 #(= % 12) (finite-binary-tree 15))))
  (is (nil? (breadth-first-search 1 #(= % 22) (finite-binary-tree 15))))
  )

(run-tests)
