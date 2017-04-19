(ns paip.11-logic.unification
  (:use clojure.tools.trace)
  (:use paip.utils))

(def fail
  "Denotes a failure in matching"
  nil)

(def no-bindings
  "Denotes successful match with no variable bindings"
  {})

(defn variable?
  [x]
  (and (symbol? x) (= \? (get (str x) 0))))

(defn get-binding
  "Find a variable->value binding in the given binding."
  [v bindings]
  (get bindings v))

(defn extend-bindings
  "Add a v -> value mappping to bindings."
  [v value bindings]
  (assoc bindings v value))

(defn match-variable
  "Does v match input? Uses (or updates) and returns bindings."
  [v input bindings]
  (let [b (get-binding v bindings)]
    (cond (nil? b) (extend-bindings v input bindings)
          (= input b) bindings
          :else fail)))

(declare unify-variable occurs-check)

(defn unify
  "Unify x and y with the given bindings."
  ([x y] (unify x y no-bindings))
  ([x y bindings]
   (cond (= bindings fail) fail
         (= x y) bindings
         (variable? x) (unify-variable x y bindings)
         (variable? y) (unify-variable y x bindings)
         (and (list? x) (list y)) (unify
                                   (rest x)
                                   (rest y)
                                   (unify (first x) (first y) bindings))
         :else fail)))

(defn unify-variable
  "Unify v with x, using and maybe extending bindings."
  [v x bindings]
  (cond (get-binding v bindings) (unify (get-binding v bindings) x bindings)
        (and (variable? x) (get-binding x bindings)) (unify
                                                      v
                                                      (get-binding x bindings)
                                                      bindings)
        (occurs-check v x bindings) fail
        :else (extend-bindings v x bindings)))

(defn occurs-check
  "Does var occur anywhere inside v?"
  [v x bindings]
  (cond (= v x) true
        (and (variable? x) (get-binding x bindings)) (occurs-check
                                                      v
                                                      (get-binding x bindings)
                                                      bindings)
        (cons? x) (or (occurs-check v (first x) bindings)
                      (occurs-check v (rest x) bindings))
        :else false))

;(trace-vars occurs-check)

(unify '(?x + 1) '(2 + ?y))
