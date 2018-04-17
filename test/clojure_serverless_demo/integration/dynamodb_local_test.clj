(ns clojure-serverless-demo.integration.dynamodb-local-test
  (:require [clojure.test :refer :all]
            [taoensso.faraday :as far]
            [clojure-serverless-demo.fixtures :refer [with-local-db with-table]]))

(def db-config {:access-key "ACCESSKEY"
                :secret-key "TOPSECRET"
                :endpoint "http://localhost:8000"})

(def table-config
  {:name :test-table
   :primary-key [:id :n]
   :throughput {:read 5 :write 1}})

(use-fixtures :once (with-local-db db-config) (with-table table-config db-config))

(deftest dynamodb-local-test
  (is (= (far/list-tables db-config)
         [:test-table])))
