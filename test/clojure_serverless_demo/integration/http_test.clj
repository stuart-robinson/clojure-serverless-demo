(ns clojure-serverless-demo.integration.http-test
  (:require [clojure-serverless-demo.fixtures :as f]
            [clojure-serverless-demo.api :as api]
            [clojure-serverless-demo.storage :as s]
            [clj-http.client :as http]
            [clojure.test :refer :all]))

(def table-config
  {:name :messages
   :primary-key [:id :s]
   :throughput {:read 5 :write 1}})

(def db-config {:access-key "ACCESSKEY"
                :secret-key "TOPSECRET"
                :endpoint "http://localhost:8000"})

(def api (api/handler (api/builder db-config)))

(use-fixtures :once
  (f/with-local-http api)
  (f/with-local-db db-config)
  (f/with-table table-config db-config))

(deftest api-routes-test
  (is (= (:body (http/get "http://localhost:8888/ping" {:as :auto}))
         {:result "pong"})))

(deftest api-join-request-test
  (http/post "http://localhost:8888/join"
             {:form-params {:name "test-name"}
              :content-type :json})
  (http/post "http://localhost:8888/say"
             {:form-params {:name "test-name"
                           :message "hello world"}
              :content-type :json})
  (let [results (:body (http/get
                        "http://localhost:8888/fetch-messages"
                        {:as :auto}))]
    (is (= ["channel" "test-name"] (map :name results)))))
