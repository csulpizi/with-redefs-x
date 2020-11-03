(ns with-redefs-x.core
  (:require [with-redefs-x.util :refer :all]
            [clojure.set :refer [map-invert]]))

(defmacro with-redefs-x [bindings & body]
  (let [kvs (apply hash-map bindings)
        var||sym (mapmerge (fn [var] {var (gensym)}) (keys kvs))]
    `(let ~(->> var||sym map-invert (mapcatv vec))
       (with-redefs ~(mapcatv vec (walk+replace-all-vals kvs var||sym))
         ~@body))))
