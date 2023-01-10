(ns paip.6-tools.pat-match-test
  (:use clojure.test)
  (:use paip.6-tools.pat-match))

(deftest pat-match-basic-var-test
  (is (= {'?v 'a} (pat-match '(?v) '(a))))

  (is (= {'?v 'c} (pat-match '(a ?v b) '(a c b))))
  (is (nil? (pat-match '(a ?v b) '(a c d))))

  (is (= {'?v 'b} (pat-match '(a t ?v) '(a t b))))

  (is (= {'?v 'b, '?u 'a} (pat-match '(?u t ?v) '(a t b))))

  (is (= {'?v 'b} (pat-match '(?v t ?v) '(b t b))))
  (is (nil? (pat-match '(?v t ?v) '(a t b))))

  (is (= {'?v '(t k)} (pat-match '(a ?v) '(a (t k))))))

(deftest pat-match-is-test
  (is (= {'?v 8} (pat-match '(a = (?is ?v number?)) '(a = 8))))
  (is (nil? (pat-match '(a = (?is ?v number?)) '(a = jk)))))

(deftest pat-match-and-test
  (is (nil? (pat-match '(a (?and (?is ?v number?) (?is ?v odd?))) '(a 8))))
  (is (= {'?v 7} (pat-match '(a (?and (?is ?v number?) (?is ?v odd?))) '(a 7)))))

(deftest pat-match-or-test
  (is (= {'?v 7} (pat-match '(a (?or (?is ?v odd?) (?is ?v zero?))) '(a 7))))
  (is (= {'?v 0} (pat-match '(a (?or (?is ?v odd?) (?is ?v zero?))) '(a 0))))
  (is (nil? (pat-match '(a (?or (?is ?v odd?) (?is ?v zero?))) '(a 2)))))

(deftest pat-match-not-test
  (is (= {'?v 7} (pat-match '(a ?v (?not ?v)) '(a 7 8))))
  (is (nil? (pat-match '(a ?v (?not ?v)) '(a 7 7)))))

(deftest pat-match-*-test
  (is (= {'?v '(b c)} (pat-match '(a (?* ?v) d) '(a b c d))))
  (is (= {'?v '(b)} (pat-match '(a (?* ?v) d) '(a b d))))
  (is (= {'?v '()} (pat-match '(a (?* ?v) d) '(a d))))

  (is (= {'?v '(b c)} (pat-match '(a (?* ?v) d ?v) '(a b c d (b c)))))
  (is (nil? (pat-match '(a (?* ?v) b c d ?v) '(a b c d (b k)))))
  (is (= {'?v '()} (pat-match '(a (?* ?v) b c d ?v) '(a b c d ()))))

  (is (= {'?x '(), '?y '(b c)} (pat-match '(a (?* ?x) (?* ?y) d) '(a b c d))))
  (is (= {'?x '(b c), '?y '(d)} (pat-match '(a (?* ?x) (?* ?y) ?x ?y)
                                           '(a b c d (b c) (d))))))

(deftest pat-match-+-test
  ;; ?+ can't match empty lists
  (is (nil? (pat-match '(a (?+ ?v) d) '(a d))))

  (is (= {'?v '(t)} (pat-match '(a (?+ ?v) d) '(a t d))))
  (is (= {'?v '(t), '?u '(p a)} (pat-match '(a (?+ ?v) (?+ ?u) d) '(a t p a d)))))

(deftest pat-match-?-test
  (is (= {'?v 'k} (pat-match '((?? ?v) d) '(k d) {})))
  (is (= {} (pat-match '((?? ?v) d) '(d) {})))

  (is (nil? (pat-match '(g (?? ?v) d) '(d) {})))
  (is (= {} (pat-match '(g (?? ?v) d) '(g d) {})))
  (is (= {'?v 'i} (pat-match '(g (?? ?v) d) '(g i d) {})))
  (is (nil? (pat-match '(g (?? ?v) d) '(g i x d) {}))))

(run-tests)
