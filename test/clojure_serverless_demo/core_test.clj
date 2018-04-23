(ns clojure-serverless-demo.core-test
  (:require [clojure.test :refer :all]
            [clojure-serverless-demo.api :as api]
            [ring.mock.request :as mock]))

(deftest api-ping-test
  (is (= (api/api (mock/request :get "/ping"))
         {:status  200
          :headers {}
          :body {:result "pong"}})))


(deftest api-echo-test
  (is (= (api/api (-> (mock/request :post "/echo")
                      (mock/json-body {:foo "bar"})))
         {:status  200
          :headers {}
          :body {:foo "bar"}})))
