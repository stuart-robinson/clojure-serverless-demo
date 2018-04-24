(ns clojure-serverless-demo.core-test
  (:require [clojure.test :refer :all]
            [clojure-serverless-demo.core :as core]
            [ring.mock.request :as mock]))

(deftest api-ping-test
  (is (= (core/api (mock/request :get "/ping"))
         {:status  200
          :headers {}
          :body {:result "pong"}})))
