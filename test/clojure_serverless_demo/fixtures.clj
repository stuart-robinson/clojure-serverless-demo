(ns clojure-serverless-demo.fixtures
  (:require  [clojure.test :as t]
             [taoensso.faraday :as far]
             [ring.adapter.jetty :refer [run-jetty]]
             [clojure-serverless-demo.aws.dynamodb-local :as dynamodb-local]))

(defn with-local-http [handler config]
  (fn [f]
    (let [server (run-jetty handler {:port (:port config)
                                     :join? false})]
      (try
        (f)
        (finally
          (.stop server))))))

(def client-opts {:access-key "ACCESSKEY"
                  :secret-key "TOPSECRET"
                  :endpoint "http://localhost:8000"})

(defn with-local-db [db-config]
  (fn [f]
    (let [s (dynamodb-local/build-server)]
      (try
        (.start s)
        (f)
        (finally
          (.stop s))))))

(defn with-table [table-config db-config]
  (fn [f]
    (far/ensure-table db-config
                      (:name table-config)
                      (:primary-key table-config)
                      {:throughput (:throughput table-config)
                       :block? true})
    (f)))
