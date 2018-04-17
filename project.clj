(defproject clojure-serverless-demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [uswitch/lambada "0.1.2"]
                 [clj-http "3.7.0"]
                 [compojure "1.6.0"]
                 [ring "1.6.3"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-mock "0.3.2"]
                 [ring-apigw-lambda-proxy "0.3.0"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [com.amazonaws/DynamoDBLocal "1.10.5.1"]
                 [com.taoensso/faraday "1.9.0"]]
  :repositories [["dynamodblocal"  {:url "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"}]]
  :aot [clojure-serverless-demo.aws.lambda]
  :uberjar {:aot :all}
  :uberjar-name "clojure-serverless-demo-standalone.jar")
