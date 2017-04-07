;;; Chapter 4: "GPS: The general problem solver" - the basic/naive solver.
;;;
;;; Notes:
;;; - This solution tries to be as faithful as possible to the original CL code,
;;;   including a mutable state. Not using mutable state here would result in a
;;;   completely different design, which is presented in the rest of the chapter
;;;   anyway.
;;; - One difference is using add-set and del-set instead of add-list and
;;;   del-list, because in Clojure set-like ops are efficiently defined on sets,
;;;   not lists.

(ns paip.4-gps.gps-basic)

(defrecord Op [action preconds add-set del-set])

;;; *ops* and *state* are dynamic variables that will be set by GPS when
;;; executing a solution to some particular problem. The funuctions defined here
;;; refer to these variables directly.
(def ^:dynamic *ops* nil)
(def ^:dynamic *state* nil)

;;; Pre-declare symbols used in the functions below.
(declare achieve)

(defn appropriate?
  "Is it appropriate to perform an operation to achieve a goal?"
  [goal op]
  (contains? (:add-set op) goal))

(defn apply-op
  "Print a message and update *state* if op is applicable and return true."
  [op]
  (when (every? achieve (:preconds op))
    (println "Executing " (:action op))
    ;; Do the update in a single swap!
    (swap! *state* #(clojure.set/union
                      (clojure.set/difference % (:del-set op))
                      (:add-set op)))
    true))

(defn achieve
  "Try to achieve a goal and return true if it's achieved, false otherwise."
  [goal]
  (or (contains? @*state* goal)
      (some apply-op (filter #(appropriate? goal %) *ops*))))
    
(defn GPS
  "GPS: achieve all goals using ops, from the starting state 'state'."
  [state goals ops]
    ;; Set a dynamic binding for *ops* and *state* while the solution is
    ;; executing.
    (binding [*ops* ops
              *state* state]
      (when (every? achieve goals)
        'solved)))

;;; Data for the 'drive child to school' problem. State entities and actions are
;;; symbols.
(def school-ops
  "A list of available operators"
  (list (map->Op {:action 'drive-son-to-school
                  :preconds '(son-at-home car-works)
                  :add-set #{'son-at-school}
                  :del-set #{'son-at-home}})
        (map->Op {:action 'shop-installs-battery
                  :preconds '(car-needs-battery shop-knows-problem shop-has-money)
                  :add-set #{'car-works}})
        (map->Op {:action 'tell-shop-problem
                  :preconds '(in-communication-with-shop)
                  :add-set #{'shop-knows-problem}})
        (map->Op {:action 'telephone-shop
                  :preconds '(know-phone-number)
                  :add-set #{'in-communication-with-shop}})
        (map->Op {:action 'look-up-number
                  :preconds '(have-phone-book)
                  :add-set #{'know-phone-number}})
        (map->Op {:action 'give-shop-money
                  :preconds '(have-money)
                  :add-set #{'shop-has-money}
                  :del-set #{'have-money}})))

(GPS
  (atom #{'son-at-home 'car-needs-battery 'have-money 'have-phone-book})
  '(son-at-school) school-ops)

(GPS
  (atom #{'son-at-home 'car-needs-battery 'have-money})
  '(son-at-school) school-ops)

(GPS
  (atom #{'son-at-home 'car-works})
  '(son-at-school) school-ops)

;(clojure.pprint/pprint school-ops)
