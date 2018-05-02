(ns clojure-serverless-demo.aws.dynamodb-local
  (:import [com.amazonaws.services.dynamodbv2.local.main ServerRunner]))

(System/setProperty "sqlite4java.library.path"
                    (.getPath (clojure.java.io/resource "sqlite4java-libs")));

(def args (into-array String ["-inMemory"]))

(defn build-server []
  (ServerRunner/createServerFromCommandLineArgs args))
