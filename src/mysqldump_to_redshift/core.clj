(ns mysqldump-to-redshift.core
  (:require [clojure.string :as s])
  (:gen-class))

(defn stream-lines-out!
  "Lazy, streams the lines to the provided filename."
  [^java.io.BufferedWriter w out-lines]
  (doseq [line out-lines] (.write w (str line "\n"))))

(defn fix-format [s]
  "Replace nulls w/ \000 and remove single quotes."
  (s/replace (s/replace s ",NULL," ",\\000,")
             "'", ""))

(def data-re (re-pattern "\\((.*?)\\)"))

(defn extract-values [s]
  "Extract anything between parentheses (non-greedy)."
  (map second (re-seq data-re s)))


(defn -main
  "Given a in-file and out-file, change mysqldump style inserts to redshift-compatible data."
  [& args]
  (with-open [r (clojure.java.io/reader (first args))
              w (clojure.java.io/writer (second args))]
    (->> (line-seq r)
         (filter #(s/starts-with? % "INSERT INTO `file` VALUES "))
         (pmap extract-values)
         (flatten)
         (pmap fix-format)
         (stream-lines-out! w))))
