(defproject mysqldump-to-redshift "0.3.0"
  :description "Convert a mysqldump file to a Redshift datafile"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [progress "1.0.2"]]
  :main ^:skip-aot mysqldump-to-redshift.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :user {:plugins [[com.jakemccrary/lein-test-refresh "0.12.0"]]}}
  :jvm-opts ["-Xms4g" "-Xmx4g"])
