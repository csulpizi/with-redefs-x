# with-redefs-x
Clojure extension of clojure.core/with-redefs, allowing self-referencing functions

# Rationale

When redef'ing a function (particularly useful in testing in clojure) you may want to modify an existing function without completely changing the underlying functionality. Imagine the following function:

`(defn triple [x] (* 3 x))`

Consider the three following situations:
1. Shimming `triple` S.T. it increases the input prior to performing the function
1. Shimming `triple` S.T. it increases the output after performing the action
1. Shimming `triple` S.T. it performs a side effect

It would be intuitive to assume that you could use `clojure.core/with-redefs` to perform this shimming. However, the following code snippet would result in a stackoverflow, since it recursively calls the function `triple`:

`(with-redefs [triple (comp triple inc)] (triple 7))`

`with-redefs-x` was built to allow you to easily do the above.

# Use

For the enumerated shims listed above, the following snippets could be used.

*Case 1*: Shimming `triple` S.T. it increases the input prior to performing the function

`(with-redefs-x [triple (comp triple inc)] (triple 7)) => 24`

*Case 2*: Shimming `triple` S.T. it increases the output after performing the action

`(with-redefs-x [triple (comp inc triple)] (triple 7)) => 22`

*Case 3*: Shimming `triple` S.T. it performs a side effect

`(let [A (atom nil)] (with-redefs-x [triple (comp (partial reset! A) triple)] (triple 7))) => 21, @A == 21`
