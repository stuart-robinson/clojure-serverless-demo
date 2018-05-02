(ns clojure-serverless-demo.aws.dynamodb-client
  (:import [com.amazonaws.auth BasicAWSCredentials]
           [com.amazonaws.services.dynamodbv2  AmazonDynamoDBClientBuilder.]
           ))

(defn db-client-builder
  [config])

(def db-config {:access-key "ACCESSKEY"
                :secret-key "TOPSECRET"
                :endpoint "http://localhost:8000"})


(defn db-client [config]
  (memoize (db-client-builder config)))

(defn list-tables [config]
  (-> (db-client config)
      .listTables
      .getTableNames
      seq))
