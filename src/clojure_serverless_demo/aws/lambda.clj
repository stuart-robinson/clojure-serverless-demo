(ns clojure-serverless-demo.aws.lambda
  (:require [clojure-serverless-demo.core :as core]
            [cheshire.core :refer [generate-stream parse-stream]]
            [clojure.java.io :as io]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.apigw :refer [wrap-apigw-lambda-proxy]]
            [uswitch.lambada.core :refer [deflambdafn]]))

(def api-gateway-handler (-> (wrap-keyword-params (core/api-builder {}))
                              wrap-params
                              wrap-json-params
                              wrap-json-response
                              wrap-apigw-lambda-proxy))

(deflambdafn api_gateway_test.api.lambda [is os ctx]
  (with-open [writer (io/writer os)]
    (let [request (parse-stream (io/reader is :encoding "UTF-8") true)]
      (generate-stream (api-gateway-handler request) writer))))
