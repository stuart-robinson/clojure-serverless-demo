(ns clojure-serverless-demo.storage
  (:require [taoensso.faraday :as far]))

(def table-config
  {:name :messages
   :primary-key [:timestamp :n]
   :throughput {:read 5 :write 1}})

;;require secondary index inorder to be able to sort correctly
;; scan should use filter expression to discard messsage older than 10 minutes
(defn fetch-messages [db-config]
  (far/scan db-config
            (:name table-config)
            {:limit 10
             :index "channel-timestamp-index"}))

(defn save-message [message db-config]
  (far/put-item db-config
                (:name table-config)
                message)
  {:result "success"})
