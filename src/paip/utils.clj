;;; Foundational utility functions useful for translating for Common Lisp, but
;;; that don't have trivial translations in Clojure.
(ns paip.utils)

(defn index-in-seq
  "Finds the index of item in the given sequence; the optional start parameter
  specifies the starting index to start looking from. Returns -1 when not found.
  Note that this is using a generic approach unoptimized for vectors."
  ([seq item start]
   (let [idx (.indexOf (nthrest seq start) item)]
     (if (>= idx 0)
       (+ start idx)
       -1)))
  ([seq item] (.indexOf seq item)))

(defn cons?
  "Is x a 'cons cell'? Non empty lists return true, otherwise false.
  
  Doesn't raise an exception on non-sequence inputs.
  See http://eli.thegreenplace.net/2016/common-lisps-consp-and-listp-in-clojure/
  for more details."
  [x]
  (and (list? x) (not (empty? x))))

;;; printv variant that prints only when the dynamic *verbose* is true. A
;;; convenience wrapper macro with-verbose is provided.
(def ^:dynamic *verbose* false)

(defmacro printfv
  [fmt & args]
  `(when *verbose*
     (printf ~fmt ~@args)))

(defmacro with-verbose
  [& body]
  `(binding [*verbose* true] ~@body))
