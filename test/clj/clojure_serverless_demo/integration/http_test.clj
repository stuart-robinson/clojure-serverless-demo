(ns clojure-serverless-demo.integration.http-test
  (:require [clojure-serverless-demo.fixtures :as f]
            [clojure-serverless-demo.api :as api]
            [clj-http.client :as http]
            [clojure.test :refer :all]))

(def db-config {})

x(def api (api/handler (api/builder db-config)))

(use-fixtures :once (f/with-local-http api))

(deftest api-routes-test
  (is (= (:body (http/get "http://localhost:8888/ping" {:as :auto}))
         {:result "pong"})))
