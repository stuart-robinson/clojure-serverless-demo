(ns clojure-serverless-demo.config
  (:require [environ.core :refer [env]]))

(def db-config {:access-key (env :x-aws-access-key-id)
                :secret-key (env :x-aws-secret-access-key)
                :endpoint (env :endpoint)})

(def table-config
  {:name (or (keyword (env :dynamodb-table-name)) :messages-dev)
   :primary-key [:id :s]
   :options {:throughput {:read 5 :write 1}
             :block? true}})
