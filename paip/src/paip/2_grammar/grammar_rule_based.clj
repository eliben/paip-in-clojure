; Chapter 2: "A simple Lisp Program" - generative grammar - rule-based solution.
;
; Notes:
; - Using maps instead of a-lists for more idiomatic (and efficient) Clojure.
;   Hence we also don't need the special -> notation.
; - simple-grammar, not *simple-grammar*. In Clojure everything is assumed to
;   be constant unless stated otherwise, so earmuffs for constants are not
;   necessary. On the other hand, *grammar* could be re-bound; therefore it has
;   earmuffs.

(ns paip.2-grammar.grammar_rule_based)

(def simple-grammar
  {'sentence '(noun-phrase verb-phrase),
   'noun-phrase '(Article Noun),
   'verb-phrase '(Verb noun-phrase),
   'Article '(the a),
   'Noun '(man ball woman table),
   'Verb '(hit took saw liked)})

(def *grammar* simple-grammar)

(defn rewrites
  "Return a list of the possible rewrites for this category"
  [category]
    (get *grammar* category))
