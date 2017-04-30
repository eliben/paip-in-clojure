;;; Chapter 6, section 6.4: a set of searching tools

(ns paip.6-tools.search
  (:use paip.utils))

(def fail
  "Denotes a failure in searching."
  nil)

(defn tree-search
  "Finds a state that satisfies goal?-fn; Starts with states, and searches
  according to successors and combiner."
  [states goal?-fn successors combiner]
  (printfv ";; Search: %s%n" (pr-str states))
  (cond (empty? states) fail
        (goal?-fn (first states)) (first states)
        :else (tree-search (combiner (successors (first states))
                                     (rest states))
                           goal?-fn
                           successors
                           combiner)))

(defn depth-first-search
  "Search new states first until goal is reached."
  [start goal?-fn successors]
  (tree-search (list start) goal?-fn successors concat))

(defn prepend
  [x y]
  (concat y x))

(defn breadth-first-search
  "Search old states first until goal is reached."
  [start goal?-fn successors]
  (tree-search (list start) goal?-fn successors prepend))

(defn binary-tree
  "A successors function representing a binary tree."
  [x]
  (list (* 2 x) (+ 1 (* 2 x))))

(defn finite-binary-tree
  "Returns a successor function that generates a binary tree with n nodes."
  [n]
  (fn [x]
    (filter #(<= % n) (binary-tree x))))

;(with-verbose
  ;(depth-first-search 1 #(= % 32) binary-tree))

(with-verbose
  (depth-first-search 1 #(= % 22) (finite-binary-tree 15)))

(with-verbose
  (breadth-first-search 1 #(= % 32) binary-tree))
