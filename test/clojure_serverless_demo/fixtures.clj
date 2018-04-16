(ns clojure-serverless-demo.fixtures
  (:require  [clojure.test :as t]
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

(defn with-local-db [f]
  (let [s (dynamodb-local/build-server)]
    (.start s)
    (f)
    (.stop s)))
