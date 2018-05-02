(ns clojure-serverless-demo.integration.http-test
  (:require [clojure-serverless-demo.fixtures :as f]
            [clojure-serverless-demo.api :as api]
            [clojure-serverless-demo.config :as config]
            [clj-http.client :as http]
            [clojure.test :refer :all]))

(def http-config
  {:port 8888})

(def db-config {:access-key "ACCESSKEY"
                :secret-key "TOPSECRET"
                :endpoint "http://localhost:8000"})

(def api (api/handler (api/builder db-config)))

(def http-addr (str "http://localhost:" (:port http-config)))

(use-fixtures :once
  (f/with-local-http api http-config)
  (f/with-local-db db-config)
  (f/with-table config/table-config db-config))

(deftest api-ping-test
  (is (= (:body (http/get (str http-addr "/ping") {:as :auto}))
         {:result "pong"})))

(deftest api-join-request-test
  (http/post (str http-addr "/join")
             {:form-params {:name "test-name"}
              :content-type :json})
  (http/post (str http-addr "/say")
             {:form-params {:name "test-name"
                           :message "hello world"}
              :content-type :json})
  (let [results (:body (http/get
                        (str http-addr "/fetch-messages")
                        {:as :auto}))]
    (is (= ["channel" "test-name"] (map :name results)))))
