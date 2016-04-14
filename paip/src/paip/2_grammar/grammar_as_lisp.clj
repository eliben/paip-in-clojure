; Chapter 2: "A simple Lisp Program" - generative grammar as Lisp functions.
;
; Notes:
; - Using lists, similar to the original PAIP code.
; - Clojure code gets compiled, so functions need to be known before they are
;   used. We can either modify the definition order or use 'declare'.

(ns paip.2-grammar.grammar_as_lisp)

(defn one-of
  "Pick one element of s and make a list of it"
  [s]
  (list (rand-nth s)))

(declare noun-phrase)

(defn Prep [] (one-of '(to in by with on)))
(defn PP [] (concat (Prep) (noun-phrase)))
(defn Adj [] (one-of '(bit little blue green adiabatic)))

(defn Adj* []
  ; One approach to selection with 50% probability
  (if (= (rand-int 2) 0)
    nil
    (concat (Adj) (Adj*))))

(defn PP* []
  ; Another approach to selection with 50% probability
  (if (rand-nth '(true false))
    (concat (PP) (PP*))
    nil))

(defn Article [] (one-of '(the a)))
(defn Noun [] (one-of '(man ball woman table)))
(defn Verb [] (one-of '(hit took saw liked)))
(defn noun-phrase [] (concat (Article) (Adj*) (Noun) (PP*)))
(defn verb-phrase [] (concat (Verb) (noun-phrase)))
(defn sentence [] (concat (noun-phrase) (verb-phrase)))

; Generate some sentences
(dotimes [_ 10]
  (println (sentence)))
