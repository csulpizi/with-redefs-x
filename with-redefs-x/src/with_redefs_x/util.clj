(ns with-redefs-x.util)

(defn mapmerge [f coll]
  (->> coll (map f) (into {})))

(defn mapcatv [f coll]
  (->> coll (mapcat f) vec))

(defn update-vals [kvs f & args]
  (mapmerge (fn [[k v]] {k (apply f v args)}) kvs))

(defn walk+replace [coll find replace]
  (clojure.walk/postwalk #(if (= find %) replace %) coll))

(defn walk+replace-all-vals
  "Given maps `kvs` and `find||replace`
  (where `find||replace` is of the form {<find> <replace>, ...}),
  replace all vals of `kvs` for each find/replace pair."
  [kvs find||replace]
  (loop [result kvs
         replacements-left find||replace]
    (if-let [kv (first replacements-left)]
      (recur (update-vals result walk+replace (key kv) (val kv))
             (dissoc replacements-left (key kv)))
      result)))
