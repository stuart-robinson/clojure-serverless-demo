(ns clojure-serverless-demo.core
  (:require [clojure-serverless-demo.aws.dynamodb-local :as local-db]
            [ring.adapter.jetty :refer [run-jetty]]
            [taoensso.faraday :as far]
            [clojure-serverless-demo.api :as api]
            [clojure-serverless-demo.storage :as storage]))

(def default  {:http-server nil
               :db-server nil})

(def server (atom default))

(defn run-local-server []
  (let [db-config {:access-key "ACCESSKEY"
                   :secret-key "TOPSECRET"
                   :endpoint "http://localhost:8000"}
        api (api/handler (api/builder db-config))
        table-config storage/table-config
        db-server (local-db/build-server)]
    (swap! server assoc :db-server db-server
           :http-server (run-jetty api {:port 8888 :join? false}))
    (.start db-server)
    (far/ensure-table db-config
                      (:name table-config)
                      (:primary-key table-config)
                      {:throughput (:throughput table-config)
                       :block? true})))

(defn stop-local-server []
  (.stop (:http-server @server))
  (.stop (:db-server @server))
  (reset! server default))

(defn -main [& args])
