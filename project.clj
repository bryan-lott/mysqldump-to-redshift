(defproject mysqldump-to-redshift "0.1.0-SNAPSHOT"
  :description "Convert a mysqldump file to a Redshift datafile"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot mysqldump-to-redshift.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
  :profiles {:uberjar {:aot :all}
             :user {:plugins [[com.jakemccrary/lein-test-refresh "0.12.0"]]}}
