(ns clojure-serverless-demo.api-test
  (:require [clojure-serverless-demo.api :as api]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [ring.middleware.json :refer [wrap-json-body]]))

(def api (wrap-json-body (api/builder {}) {:keywords? true}))

(deftest api-ping-test
  (is (= (api (mock/request :get "/ping"))
         {:status  200
          :headers {"Cache-Control" "max-age=0"}
          :body {:result "pong"}})))

(deftest api-echo-test
  (is (= (api (-> (mock/request :post "/echo")
                  (mock/json-body {:foo "bar"})))
         {:status  200
          :headers {"Cache-Control" "max-age=0"}
          :body {:foo "bar"}})))
