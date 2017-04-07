;;; TODO: add tests in a separate file? same as for EOPL utils

(ns paip.6-tools.pat-match)

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

;;; TODO: define match-* functions from the table here (page 184)

(def single-matcher-table
  "Table mapping single matcher names to matching functions."
  {'?is match-is
   '?or match-or
   '?and match-and
   '?not match-not})

(defn single-pattern?
  "Is this a single-matching pattern?"
  [pattern]
  (and (seq pattern) (get single-matcher-table (first pattern))))

(defn single-matcher
  "Call the right single-pattern matching function."
  [pattern input bindings]
  ((get single-matcher-table (first pattern)) (rest pattern) input bindings))

(defn pat-match
  ([pattern input] (pat-match pattern input no-bindings))
  ([pattern input bindings]
   (cond (= bindings fail) fail
         (variable? pattern) (match-variable pattern input bindings)
         (= pattern input) bindings
         (single-pattern? pattern) (single-matcher pattern input bindings)
         :else fail)))
