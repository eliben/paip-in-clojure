;;; TODO: add tests in a separate file? same as for EOPL utils

(ns paip.6-tools.pat-match)

;;; TODO: Implement segment matching too

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

(defn match-is
  "Suceed and bind var if the input satisfied pred.
  var-and-pred is the list (var pred)."
  [var-and-pred input bindings]
  (let [[v pred] var-and-pred
        new-bindings (pat-match v input bindings)]
    (if (or (= new-bindings fail)
            (not ((resolve pred) input)))
      fail
      new-bindings)))

(defn match-and
  "Succeed if all the patterns match the input."
  [patterns input bindings]
  (cond (= bindings fail) fail
        (empty? patterns) bindings
        :else (match-and
                (rest patterns)
                input
                (pat-match (first patterns) input bindings))))
  
(defn match-or
  "Succeed if any of the patterns match the input."
  [patterns input bindings]
  (if (empty? patterns)
    fail
    (let [new-bindings (pat-match (first patterns) input bindings)]
      (if (= new-bindings fail)
        (match-or (rest patterns) input bindings)
        new-bindings))))
  
(defn match-not
  "Succeed if none of the patterns match the input.
   This will never bind variables."
  [patterns input bindings]
  (if (match-or patterns input bindings)
    fail
    bindings))

(def single-matcher-table
  "Table mapping single matcher names to matching functions."
  {'?is match-is
   '?or match-or
   '?and match-and
   '?not match-not})

(defn single-pattern?
  "Is this a single-matching pattern?"
  [pattern]
  (and (list? pattern) (get single-matcher-table (first pattern))))

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
         (and (list? pattern) (list? input)) (pat-match
                                              (rest pattern)
                                              (rest input)
                                              (pat-match
                                               (first pattern)
                                               (first input)
                                               bindings))
         :else fail)))

;(pat-match '(a ?v b) '(a c d))
(pat-match '(a = (?is ?v number?)) '(a = 8))

(pat-match '(a (?and (?is ?v number?) (?is ?v odd?))) '(a 7))
