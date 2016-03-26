(ns cl-in-clj.core
  (:gen-class))

; CL's 'append' is Clojure's 'concat'
; Also note that Clojure is case sensitive by default; CL is case insensitive.
; CL: (append '(Pat Kim) '(Robin Sandy))
(concat '(Pat Kim) '(Robin Sandy))

; CL's 'length' is Clojure's 'count'
(count '(Foo Bar Baz))
