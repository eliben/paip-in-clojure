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

(defn print-and-return
  "Prints and returns its parameter. Useful for interjection inside
  expressions."
  ([id param]
   (prn id param)
   param)
  ([param]
   (prn param)
   param))
