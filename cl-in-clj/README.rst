cl-in-clj
---------

Clojure project with code samples and walkthroughs for translating Common Lisp
code directly to Clojure. These samples are meant to be independent of any
particular content (like the PAIP book) and stand on their own for quickly
checking how some Common Lisp idiom translates to Clojure.

Versions of Common Lisp and Clojure environments
================================================

For Common Lisp: sbcl 1.1.14 (as installed by ``apt-get sbcl`` on a Ubuntu
machine).

For Clojure: 1.8, installed by Leiningen.

Conventions used
================

Within the code I frequently use "CL" to stand for "Common Lisp". CL snippets
usually typically in comments, with the equivalent Clojure code following. The
Clojure code can be copy pasted into a REPL or run from the editor if you have
the appropriate integration enabled.
