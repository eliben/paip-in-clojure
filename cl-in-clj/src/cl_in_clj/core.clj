(ns cl-in-clj.core
  (:gen-class))

; true/false literals are just 'true' and 'false'
(pr true)
(pr false)

; CL's 'append' is Clojure's 'concat'
; Also note that Clojure is case sensitive by default; CL is case insensitive.
; CL> (append '(Pat Kim) '(Robin Sandy))
(concat '(Pat Kim) '(Robin Sandy))

; CL's 'length' is Clojure's 'count'
(count '(Foo Bar Baz))

; Defining variables is a tricky one, since Clojure is explicitly against state.
; In CL, we'd do:
;
;   CL> (setf x 10)
;   CL> x
;   10
;
; Note that in some CL implementations (like SBCL) the above wouldn't work
; without a preceding (defvar x).
;
; In Clojure, we'd need to use one of the explicit thread-safe state management
; utilities. The simplest is probably 'atom', which we can update with reset!,
; swap!, etc.
; In general, we'd like to avoid using these constructs in Clojure unless really
; necessary.
(def x (atom 10))
(deref x)           ; or @x
(reset! x 20)
(deref x)

; Accessing list elements. IN CL we have 'first', 'rest', 'second', 'third',
; 'fourth...' In clojure, 'first' and 'rest' work. 'second' works too. For
; others, just use 'nth'
(def letters '(a b c d e f g))
(first letters)
(second letters)
(nth letters 2)     ; 0-based indexing
(rest letters)
(last letters)

; Subtle difference between CL and Clojure is how 'rest' behaves at the end of a
; list. In CL, (rest '()) is nil. In Clojure it's the empty list:
(rest '())
; One workaround for this is to use 'seq', which returns nil for an empty
; collection. If it's important for (rest '())) to return nil, do:
(seq (rest '()))

; CL also uses 'elt' for generic sequence element access. In Clojure, both
; 'nth' and 'get' work for vectors.
(def v ['a 'b 'c 'd 'e])
(get v 3)
(nth v 3)

; In CL 'last' returns the last element wrapped in a list, so we'd need
; (first (last lst)) to access the element itself. In Clojure 'last' just
; returns the last element.
(last letters)

; Simple function definition: CL's 'defun' is Clojure's 'defn'. The syntax for
; arguments is different too (Clojure uses [...]).
;
;   CL> (defun twice (x) (* x 2))
;   CL> (twice 12)
;   24
(defn twice [x] (* x 2))
(twice 12)

; In Clojure it's possible to define variadic functions, functions with multiple
; arities and functions with keyword arguments. Here are some examples.

; Variadic function with a parameter name following a '&' capturing all
; "unnamed" parameters into a list.
(defn foo [& args] (map list args))
(foo 1 2 3 4 5)

; Some arguments may be left as required.
(defn bar [a b & args] (list a b args))
(bar 10 20 30 40)

; Multi-arity function that can be used to implement "optional" positional
; arguments, or just provide slightly different variants of the function for
; different arities.
(defn multiarity
  ([x] (multiarity x 1 2))
  ([x y] (multiarity x y 2))
  ;; Actual implementation: the lower-arity variants provide default values.
  ([x y z] (list x y z)))

(multiarity 10)
(multiarity 10 20)
(multiarity 10 20 30)

; We can also use multi-arity definitions to create recursive definitions on
; the argument list.
(defn multiarity-rec
  ([x] x)
  ([x & args] (+ x (apply multiarity-rec args))))

(multiarity-rec 10)
(multiarity-rec 10 20)
(multiarity-rec 10 20 30)

; Keyword arguments with a map following a '&' and :keys
(defn kwargs [& {:keys [name age weight]}]
  (list name age weight))

(kwargs :name "Tom" :age 20 :weight 145)

; Default values for some keys with :or (otherwise the default is nil)
(defn kwargsdefault [& {:keys [name age weight]
                        :or {age 18, weight 150}}]
  (list name age weight))

(kwargsdefault :name "Tim")

; Multiple return values: in Clojure, just return a vector. Then, destructuring
; can be used to give each value a name. This replaces CL's multiple-value-bind.
(defn xanddouble [x] [x (* 2 x)])
(let [[x doublex] (xanddouble 4)]
  (list x doublex))

; We can do more complex destructuring too, with '&' capturing a list of
; remaining values, and :as to capture the complete list.
(let [[x & others] [1 2 3 4 5]]
  (list x others))

(let [[x & others :as all] [1 2 3 4 5]]
  (list x others all))

; Clojure allows using earlier bound vars later in the same vector, like let*
; in CL.
(let [x 2
      y (* 2 x)]
  (+ x y))

; 'map' variants
; CL's 'mapcar' is just 'map' in Clojure.
;
;   CL> (mapcar #'list '(a b c) '(1 2 3))
;
; An important difference this example highlights is the namespace for
; functions. CL is a Lisp-2 where functions have their own symbol table, so we
; need to use special syntax to tell CL we want the actual function when a
; symbol appears in a non-head position in a form. Clojure is Lisp-1, so 'list'
; is a function wherever it's used.
;
; For a version that flattens lists in the sequence, see mapcat below.
(map list '(a b c) '(1 2 3))

; CL's 'defparameter' is just translated to 'def' in Clojure, where variables
; are not mutable unless special tools are used (see the 'atom' examples above).
;
;   CL> (defparameter *titles* '(Mr Ms Miss Sir))
(def titles '(Mr Ms Miss Sir))

; CL's 'member' performs a linear search in a list. Clojure has a wealth of
; first-order data structures that are much better than lists for storing data
; that has to be searched. For example, we have sets with 'contains?'
; In general, functions like 'contains?' and 'get' will refuce to work on lists,
; since lists really aren't meant for these operations. It's possible to
; simulate 'member' with the 'some' function if *really* needed.
(contains? #{1 2 3 4} 3)      ; efficient search in a set

(some #(= 3 %) '(1 2 3 4))    ; using 'some' to simulate CL's 'member'
(some #{3} '(1 2 3 4))        ; 'some' can also take a set to search for

; CL has the built-in 'trace'. Clojure has tools.trace
; (https://github.com/clojure/tools.trace). After we add it as a project
; dependency, it's easy to use. It also has all kinds of fancy options beyond
; basic tracing.
(defn sillymul
  [x n]
  (if (= n 0)
    0
    (+ x (sillymul x (dec n)))))

; Uncomment these lines to trace the execution of 'sillymul'
; (use 'clojure.tools.trace)
; (trace-vars sillymul)
; (sillymul 4 9)

; Similarly to CL, Clojure has 'time' to time the execution of expressions.
(time (* 2 2))
(time (Thread/sleep 100))

; PAIP defines the 'mappend' function in section 1.7, which applies a function
; to each element of a list and appends all the results together. In Clojure
; this is just 'mapcat'
(mapcat #(list % (+ % %)) '(1 10 300))

; CL has 'funcall' to call a function by name, but Clojure as a Lisp-1 doesn't
; need this.
(+ 1 2 3 4)
(defn addemup [& nums] (apply + nums))
(addemup 1 2 3 4)
; Also as the addemup function above shows, 'apply' is same as in CL, just that
; we simpy say + instead of #'+.

; CL's 'lambda' is 'fn'. It's a shame, lambda is so much cooler ;-)
((fn [x] (+ x 2)) 4)

; CL's random with an integer argument is Clojure's rand-int
(rand-int 5)

; Clojure also has rand-nth for selecting a random element from a collection
(rand-nth [:k :t :p])

; CL's 'assoc' works on a-lists; in Clojure it's idiomatic to use maps instead
; (much more efficient). Moreover, in Clojure 'assoc' adds a value to a map,
; so it's a different function.
; To grab keys from a Clojure map use 'get' or just the key as the function:
(def simplemap {:a 2, :b 5, :c 20, :k 32})
(get simplemap :k)
(:k simplemap)

; CL has defstruct. Clojure has structs too, but records are more versatile.
; ->TypeName is a generated constructor, and afterwards map-like keyword access
; applies.
(defrecord Person [name weight position])
(def joe (->Person "Joe" 190 :manager))

; Keyword access to record's fields.
(+ 10 (:weight joe))

; map->TypeName is also generated, to create instances with keyword-name
; arguments.
(def fred (map->Person {:name "Fred", :position :janitor}))

; This can be used to emulate defaults
(defn ->PersonWithDefaults
  [fieldmap]
  (map->Person (merge {:position :employee} fieldmap)))

(def tim (->PersonWithDefaults {:name "Tim", :weight 152})) 

; CL's progn is do 
(do
  (prn ":" 1)
  (prn ":" 2)
  4)

; CL's dotimes is Clojure's dotimes
(dotimes [n 4] (println n))

; CL's dolist is Clojure's doseq
(doseq [x '(a b c d)] (println x))

; doseq can be combined with destructuring
(doseq [[x & others] '((a b c) (1 2 3))]
  (println x others))

; dolist does an outer product loop when passed multiple sequences
(doseq [x '(a b c d)
        y '(1 2 3 4)]
  (println (list x y)))

; CL has a zoo of functions for finding elements in collections: find, find-if
; find-if-not, remove, remove-if, etc.
; In Clojure it can all be done with filter/remove and/or 'complement'. Here
; are some recipes:

; Keep only the odd numbers in the sequence.
(filter odd? '(1 2 3 4 5))

; Keep only the non-odd numbers in the sequence (remove all odd ones).
(remove odd? '(1 2 3 4 5))
; Same effect (in fact this is exactly how 'remove' is implemented).
(filter (complement odd?) '(1 2 3 4 5))

; Find the first odd number in the sequence. This is efficient because 'filter'
; returns a lazy sequence.
(first (filter odd? '(2 3 4 5)))

; Find the first non-odd number in the sequence.
(first (remove odd? '(2 3 4 5)))

; CL has a bunch of keyword variants for 'find' and 'remove', for setting the
; test function, etc. In Clojure this isn't necessary given the concise syntax
; for anonymous functions. For example, here's another way to keep only odd
; numbers in a sequence - but here the function is truly arbitrary.
(filter #(odd? %) '(1 2 3 4 5))
