(ns clojure-serverless-demo.aws.lambda-test
  (:require [clojure-serverless-demo.aws.lambda :as sut]
            [clojure.test :refer :all]
            [ring.core.spec :as ring-spec]
            [clojure.spec.alpha :as s]))

(deftest api-gateway-request->ring-request-test
  (let [api-gateway-request

        {:path "/ping"
         :queryStringParameters nil
         :pathParameters {:proxy "ping"}
         :headers {:X-Forwarded-Proto "https"
                   :X-Forwarded-Port "443"
                   :X-Forwarded-For "81.106.81.0, 54.182.244.13"
                   :Host "kofu3jdkgj.execute-api.us-east-1.amazonaws.com"
                   :User-Agent "curl/7.55.1"}
         :resource "/{proxy+}"
         :httpMethod "GET"
         :requestContext {:path "/dev/ping"
                          :stage "dev"
                          :protocol "HTTP/1.1"
                          :resourceId "suovu9"
                          :requestTime "17/Apr/2018:21:17:59 +0000"
                          :requestId "ce486fc6-4284-11e8-951d-b96ffe0b5d52"
                          :httpMethod "GET"}
         :body "test"
         :query-string "test"}


        result (sut/api-gateway-request->ring-request api-gateway-request)]

    (is (s/valid? :ring/request result))))
