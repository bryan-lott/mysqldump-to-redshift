(ns mysqldump-to-redshift.core-test
  (:require [clojure.test :refer :all]
            [mysqldump-to-redshift.core :refer :all]))

(deftest fix-format-test
  (testing "fix-format easy case"
    (is (= (fix-format "1784973,66,NULL,4289,NULL,'2014-01-15 18:02:23'")
           "1784973\t66\t\t4289\t\t2014-01-15 18:02:23")))
  (testing "fix-format dual nulls"
    (is (= (fix-format "1234,NULL,NULL,5678")
           "1234\t\t\t5678")))
  (testing "fix-format tabs outside quotes"
    (is (= (fix-format "1234,\"testing, commas in quotes\",5678")
           "1234\t\"testing, commas in quotes\"\t5678"))
    (is (= (fix-format "1234,5678,9012")
           "1234\t5678\t9012")))
  (testing "fix-format all cases together"
    (is (= (fix-format "1234,NULL,NULL,\", in quotes\",5678,NULL,NULL,\"another , in quotes\",9012")
           "1234\t\t\t\", in quotes\"\t5678\t\t\t\"another , in quotes\"\t9012"))))

(deftest extract-values-test
  (testing "extract-values parens in quotes"
    (is (= (extract-values "(\"(a)\"),(b, c, d);")
           ["\"(a)\"" "b, c, d"]))))
