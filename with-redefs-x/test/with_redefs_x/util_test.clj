(ns with-redefs-x.util-test
  (:require [clojure.test :refer :all]
            [with-redefs-x.util :refer :all]))

(deftest test-mapmerge
  (let [foo (fn [x] {(- x) (inc x)})]
    (is (= {-4 5 -8 9 3 -2}
           (mapmerge foo [4 8 -3])))
    (is (= {}
           (mapmerge foo [])))))

(deftest test-mapcatv
  (let [foo (fn [x] [(* 2 x) (inc x)])]
    (is (= [8 5 -10 -4 0 1]
           (mapcatv foo [4 -5 0])))
    (is (vector? (mapcatv foo [4 -5 0])))
    (is (= [] (mapcatv foo [])))
    (is (vector? (mapcatv foo [])))))

(deftest test-update-vals
  (is (= {:a 6 :b 1 :c -9}
         (update-vals {:a 5 :b 0 :c -10} inc)))
  (is (= {}
         (update-vals {} inc)))
  (is (= {:a -5 :b -10 :c -20}
         (update-vals {:a 5 :b 0 :c -10} - 10))))

(deftest test-walk+replace
  (is (= {:c [:c :b :c :d]
          :b {:c :b :b :c}
          :e :c
          :d {:c {:c {:c {:c [:c :c]}}}}}
         (walk+replace {:a [:a :b :c :d]
                        :b {:a :b :b :a}
                        :e :a
                        :d {:a {:a {:a {:a [:a :a]}}}}}
                       :a :c))))

(deftest test-walk+replace-all-vals
  ;;Note that the keys are not updated.
  (is (= {:a {:hello! :b
              :b :hello!}
          :b [:hello! :b :c :goodbye!]
          :c :hello!
          :d :goodbye!}
         (walk+replace-all-vals {:a {:a :b :b :a}
                                 :b [:a :b :c :d]
                                 :c :a
                                 :d :d}
                                {:a :hello! :d :goodbye!}))))

