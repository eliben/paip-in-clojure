; Chapter 2: "A simple Lisp Program" - generative grammar
;
; Notes:
; - Using lists, similar to the original PAIP code.
; - Clojure code gets compiled, so functions need to be known before they are
;   used. We can either modify the definition order or use 'declare'.

(ns paip.2-grammar.grammar)

(defn one-of
  "Pick one element of s and make a list of it"
  [s]
  (list (rand-nth s)))

(defn Article [] (one-of '(the a)))
(defn Noun [] (one-of '(man ball woman table)))
(defn Verb [] (one-of '(hit took saw liked)))
(defn noun-phrase [] (concat (Article) (Noun)))
(defn verb-phrase [] (concat (Verb) (noun-phrase)))
(defn sentence [] (concat (noun-phrase) (verb-phrase)))

; Generate some sentences
(dotimes [_ 10]
  (println (sentence)))
