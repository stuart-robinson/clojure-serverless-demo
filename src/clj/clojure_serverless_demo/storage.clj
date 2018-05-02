(ns clojure-serverless-demo.storage
  (:require [taoensso.faraday :as far]
            [clojure-serverless-demo.config :as config]))

;;require secondary index inorder to be able to sort correctly
;; scan should use filter expression to discard messsage older than 10 minutes

;;require secondary index inorder to be able to sort correctly
;; scan should use filter expression to discard messsage older than 10 minutes
(defn fetch-messages [db-config]
  (sort-by :timestamp (far/scan db-config
                               (:name config/table-config)
                               {:limit 10
                                :span-reqs {:max 1}})))

(defn save-message [message db-config]
  (far/put-item db-config
                (:name config/table-config)
                message)
  {:result "success"})
