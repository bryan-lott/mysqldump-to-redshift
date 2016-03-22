(ns mysqldump-to-redshift.core
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [progress.file :as progress])
  (:gen-class))

(defn file-size
  "Given a filename, determine the file size."
  [filename]
  (.length (io/file filename)))

(defn stream-lines-out!
  "Lazy, streams the lines to the provided filename."
  [^java.io.BufferedWriter w out-lines]
  (doseq [line out-lines] (.write w (str line "\n"))))

(defn fix-format
  "Remove nulls outside quotes and remove single quotes.
  Change commas outside double quotes to tabs."
  [s]
  (-> s
      (s/replace #"NULL(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)" "")
      (s/replace #",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", (str \tab))
      (s/replace "'", "")
      (s/replace "\\\"" "\"")))

(defn extract-values
  "Extract anything between parentheses (non-greedy)."
  [s]
  (map second (re-seq #"\((.*?)\)[,;]" s)))


(defn -main
  "Given an in-file and out-file, change mysqldump style inserts to redshift-compatible data."
  [& args]
  (let [in (first args)
        out (second args)
        size (file-size in)]
    (if (not= in out)
      (progress/with-file-progress out :filesize size
        (with-open [r (clojure.java.io/reader in)
                    w (clojure.java.io/writer out)]
          (->> (line-seq r)
               (filter #(s/starts-with? % "INSERT INTO "))
               (pmap extract-values)
               (flatten)
               (pmap fix-format)
               (stream-lines-out! w))))
      (println "Infile must not match outfile."))))
