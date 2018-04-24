(ns clojure-serverless-demo.integration.dynamodb-local-test
  (:require [clojure.test :refer :all]
            [taoensso.faraday :as far]
            [clojure-serverless-demo.fixtures :refer [with-local-db]]))

(use-fixtures :once with-local-db)

(def storage-config
  {:dynamodb-config {:access-key "ACCESSKEY"
                     :secret-key "TOPSECRET"
                     :endpoint "http://localhost:8000"}
   :table-config {:name :test-table
                  :}})

(deftest dynamodb-local-test

  (let [client-opts {:access-key "ACCESSKEY"
                     :secret-key "TOPSECRET"
                     :endpoint "http://localhost:8000"}]

    (far/ensure-table client-opts
                      :test-table
                      [:id :n]
                      {:throughput {:read 1 :write 1}
                       :block? true})

    (is (= (far/list-tables client-opts)
           [:test-table]))))
