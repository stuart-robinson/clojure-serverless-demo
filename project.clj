(defproject clojure-serverless-demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.amazonaws/aws-lambda-java-core "1.0.0"]
                 [clj-http "3.7.0"]
                 [compojure "1.6.0"]
                 [environ "1.1.0"]
                 [ring "1.6.3"]
                 [ring-cors "0.1.12"]
                 [ring/ring-mock "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [com.amazonaws/DynamoDBLocal "1.10.5.1"]
                 [com.taoensso/faraday "1.9.0"]]
  :repositories [["dynamodblocal"  {:url "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"}]]
  :profiles {:uberjar {:aot :all}}
  :uberjar-name "clojure-serverless-demo-standalone.jar"
  :main clojure-serverless-demo.core)
