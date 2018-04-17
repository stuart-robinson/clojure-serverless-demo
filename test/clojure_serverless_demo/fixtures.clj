(ns clojure-serverless-demo.fixtures
  (:require  [clojure.test :as t]
             [taoensso.faraday :as far]
             [ring.adapter.jetty :refer [run-jetty]]
             [clojure-serverless-demo.aws.dynamodb-local :as dynamodb-local]))

(def server-port 8888)

(def ^:dynamic *server-address* (str "http://localhost:" server-port))

(defn with-local-http [handler]
  (fn [f]
    (let [server (run-jetty handler {:port server-port
                                     :join? false})]
      (f)
      (.stop server))))

(def client-opts {:access-key "ACCESSKEY"
                  :secret-key "TOPSECRET"
                  :endpoint "http://localhost:8000"})

(defn with-local-db [db-config]
  (fn [f]
    (let [s (dynamodb-local/build-server)]
      (try
        (.start s)
        (f)
        (.stop s)
        (catch Exception e
          (.stop s)
          (throw e))))))

(defn with-table [table-config db-config]
  (fn [f]
    (far/ensure-table db-config
                      (:name table-config)
                      (:primary-key table-config)
                      {:throughput (:throughput table-config)
                       :block? true})
    (f)))
