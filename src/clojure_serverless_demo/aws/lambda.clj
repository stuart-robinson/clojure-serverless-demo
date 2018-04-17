(ns clojure-serverless-demo.aws.lambda
  (:require [clojure-serverless-demo.api :as api]
            [cheshire.core :refer [generate-stream parse-stream]]
            [clojure.java.io :as io]
            [ring.middleware.apigw :refer [wrap-apigw-lambda-proxy]]
            [uswitch.lambada.core :refer [deflambdafn]]))


(def api-gateway-handler (-> (api/builder db-config)
                             (api/handler)
                             (wrap-apigw-lambda-proxy)))

(defn -handleRequest
  [input-stream output-stream context]
  (with-open [writer (io/writer output-stream)]
    (let [request (parse-stream (io/reader input-stream) true)
          logger (.getLogger ctx)]
      (.log logger (str request))
      (generate-stream (api-gateway-handler request) writer))))
