(ns clojure-serverless-demo.aws.lambda
  (:require [clojure-serverless-demo.api :as api]
            [cheshire.core :refer [generate-stream parse-stream generate-string]]
            [clojure.java.io :as io])
  (:import [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:gen-class
   :name clojure_serverless_demo.ApiHandler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))

(def config {})

(defn api-gateway-request->ring-request
  "coerce an API gateway request to a valid Ring request"
  [request]
  {:server-port (-> (:headers request) :X-Forwarded-Port Integer/parseInt)
   :server-name (:Host (:headers request))
   :remote-addr (-> (:headers request)
                    :X-Forwarded-For
                    (clojure.string/split #", ")
                    first)
   :uri (:path request)
   :scheme (-> (:headers request) :X-Forwarded-Proto keyword)
   :protocol (:protocol (:requestContext request))
   :headers (into {} (for [[k v] (:headers request)]
                       [(clojure.string/lower-case (name k)) v]))
   :request-method (-> (:httpMethod request)
                       (clojure.string/lower-case)
                       (keyword))
   :body (when-let [body (:body request)] (io/input-stream (.getBytes body)))
   :query-string (:queryStringParameters request)})

(defn ring-response->api-gateway-response
  "coerce a Ring response to an API gateway response"
  [response]
  {:statusCode (str (:status response))
   :body (:body response)
   :headers (:headers response)})

(defn wrap-api-gateway-request [f]
  (fn [request]
    (-> (api-gateway-request->ring-request request)
        (f)
        (ring-response->api-gateway-response))))


(defn wrap-api-gateway-request [f]
  (fn [request]
    (-> (api-gateway-request->ring-request request)
        (f)
        (ring-response->api-gateway-response))))


(def api-gateway-handler (-> (api/api)
                             (api/handler)
                             (wrap-api-gateway-request)))

(defn -handleRequest
  [_ input-stream output-stream context]
  (with-open [writer (io/writer output-stream)]
    (let [request (parse-stream (io/reader input-stream) true)
          response (api-gateway-handler request)
          logger (.getLogger context)]
      (.log logger (str request))
      (.log logger (str response))
      (generate-stream response writer))))
