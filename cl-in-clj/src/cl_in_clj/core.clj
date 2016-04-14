(ns cl-in-clj.core
  (:gen-class))

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
