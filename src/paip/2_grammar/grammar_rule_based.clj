;;; Chapter 2: "A simple Lisp Program" - generative grammar - rule-based
;;; solution.
;;;
;;; Notes:
;;; - Using maps instead of a-lists for more idiomatic (and efficient) Clojure.
;;;   Hence we also don't need the special -> notation.
;;; - simple-grammar, not *simple-grammar*. In Clojure everything is assumed to
;;;   be constant unless stated otherwise, so earmuffs for constants are not
;;;   necessary.

(ns paip.2-grammar.grammar_rule_based)

;;; Maps category to a list of rewrites. When a rewrite is a list in itself,
;;; it's treated recursively.
(def simple-grammar
  {'sentence '((noun-phrase verb-phrase)),
   'noun-phrase '((Article Noun)),
   'verb-phrase '((Verb noun-phrase)),
   'Article '(the a),
   'Noun '(man ball woman table),
   'Verb '(hit took saw liked)})

(def grammar simple-grammar)

(defn rewrites
  "Return a list of the possible rewrites for this category."
  [category]
  (get grammar category))

(defn generate
  "Generate a random sentence or phrase."
  [phrase]
  (cond (list? phrase) (mapcat generate phrase)
        (rewrites phrase) (generate (rand-nth (rewrites phrase)))
        :else (list phrase)))

;;; Solution to exercise 2.1
(defn generate-2-1
  [phrase]
    ;; Uses if-let since a cond solution would require mean (rewrites phrase)
    ;; even when not necessary. In the book, setf is used on a let binding but
    ;; this goes against the Clojure way. We could define a cond-let macro
    ;; for Clojure too.
  (if-let [prewrites (rewrites phrase)]
    (generate (rand-nth prewrites))
    (if (list? phrase)
      (mapcat generate phrase)
      (list phrase))))

;;;---- To enable tracing, uncomment the following two lines
;(use 'clojure.tools.trace)
;(trace-vars generate rewrites)
;;;----

;(dotimes [_ 10]
  ;(println (generate 'sentence)))

(defn generate-tree
  "Generate a random sentence or phrase, with a complete parse tree."
  [phrase]
  (cond (list? phrase) (map generate-tree phrase)
        (rewrites phrase)
        (cons phrase (generate-tree (rand-nth (rewrites phrase))))
        :else (list phrase)))

;(generate-tree 'sentence)

(defn combine-all
  "Return a list of lists formed by appeding a y to an x.
  E.g. (combine-all '((a) (b)) '((1) (2)))
  -> ((a 1) (b 1) (a 2) (b 2))"
  [xlist ylist]
  (mapcat (fn [y]
            (map #(concat % y) xlist))
          ylist))

(defn generate-all
  "Generate a list of all possible expansions of this phrase."
  [phrase]
  (cond (nil? phrase) (list nil)
        (list? phrase) (combine-all (generate-all (first phrase))
                                    (generate-all (seq (rest phrase))))
        (rewrites phrase) (mapcat generate-all (rewrites phrase))
        :else (list (list phrase))))

(count (generate-all 'sentence))
