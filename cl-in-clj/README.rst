cl-in-clj
---------

Clojure project with code samples and walkthroughs for translating Common Lisp
("CL") code directly to Clojure. These samples are meant to be independent of
any particular content (like the PAIP book) and stand on their own for quickly
checking how some Common Lisp idiom translates to Clojure.

Versions of Common Lisp and Clojure environments
================================================

For Common Lisp: CLISP 2.49 (as installed by ``apt-get clisp`` on a Ubuntu
machine). For some code samples I also double-checked with SBCL.

For Clojure: 1.8, installed by Leiningen.

How it's structured
===================

At this time all code sits in ``core.clj``.  CL snippets usually appear in
comments, with the equivalent Clojure code following. The Clojure code can be
copy pasted into a REPL or run from the editor if you have the appropriate
integration enabled.

The idea is to enable easy "grepability" over the code to quickly figure out how
to map some CL idiom to Clojure.
