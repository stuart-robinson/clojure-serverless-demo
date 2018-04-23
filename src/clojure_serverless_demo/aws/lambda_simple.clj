;;simple lambda function in clojure
(ns clojure-serverless-demo.aws.lambda-simple
  (:require [cheshire.core :refer [generate-stream parse-stream generate-string]]
            [clojure.java.io :as io])
  (:import [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:gen-class
   :name clojure_serverless_demo.SimpleHandler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))

(defn -handleRequest
  [_ input-stream output-stream context]
  (with-open [writer (io/writer output-stream)]
    (generate-stream {:hello "world"} writer)))
