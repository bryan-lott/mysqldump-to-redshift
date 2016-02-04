(ns mysqldump-to-redshift.core
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [progress.file :as progress])
  (:gen-class))

(defn file-size [filename]
  (.length (io/file filename)))

(defn stream-lines-out!
  "Lazy, streams the lines to the provided filename."
  [^java.io.BufferedWriter w out-lines]
  (doseq [line out-lines] (.write w (str line "\n"))))

(defn fix-format [s]
  "Replace nulls w/ \000 and remove single quotes.
  Change commas outside double quotes to tabs."
  (-> s
      (s/replace ",NULL," ",,")
      (s/replace "'", "")
      (s/replace #",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", (str \tab))))

(def data-re (re-pattern "\\((.*?)\\)[,;]"))

(defn extract-values [s]
  "Extract anything between parentheses (non-greedy)."
  (map second (re-seq data-re s)))


(defn -main
  "Given a in-file and out-file, change mysqldump style inserts to redshift-compatible data."
  [& args]
  (let [in (first args)
        out (second args)
        size (file-size in)]
    (progress/with-file-progress out :filesize size
      (with-open [r (clojure.java.io/reader in)
                  w (clojure.java.io/writer out)]
        (->> (line-seq r)
             (filter #(s/starts-with? % "INSERT INTO `file` VALUES "))
             (pmap extract-values)
             (flatten)
             (pmap fix-format)
             (stream-lines-out! w))))))
