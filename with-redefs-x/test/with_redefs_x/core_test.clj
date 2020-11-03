(ns with-redefs-x.core-test
  (:require [clojure.test :refer :all]
            [with-redefs-x.core :refer :all]))

(defn foo [x] (* x (inc x)))

(deftest test-redef-recursion
  (is (= 90 (foo 9))
      "foo returns expected result when not redef'd")
  (is (= 110 (foo (inc 9)))
      "manually overwriting function call returns expected result.")
  (with-redefs [foo (fn [x] (foo (inc x)))]
    (is (thrown? java.lang.StackOverflowError (foo 9))
        "redef'ing foo using a call to foo causes a stack overflow"))
  (with-redefs-x [foo (fn [x] (foo (inc x)))]
    (is (= 110 (foo 9))
        "redef-x'ing foo using a call to foo returns expected result")))

(deftest test-basic-behavior
  (with-redefs [foo (constantly 60)]
    (is (= 60 (foo 9))
        "check base case"))
  (with-redefs-x [foo (constantly 60)]
    (is (= 60 (foo 9))
        "check with-redefs-x performs identically to base case")))

(deftest test-nested-calls
  (let [bar (fn [x] (- 15 (foo x)))]
    (with-redefs [foo (constantly 60)]
      (is (= -45 (bar 9))
          "check base case"))
    (with-redefs-x [foo (constantly 60)]
      (is (= -45 (bar 9))
          "redef-x'ing performs identically to base case"))))
