(ns clojure-serverless-demo.chat)

(defn say [{:keys [name message]}]
  {:id (str (java.util.UUID/randomUUID))
   :name name
   :message message
   :channel "default"
   :timestamp (System/currentTimeMillis)})

(defn join [{:keys [name]}]
  (say {:name "channel"
        :message (str name " joined the channel...")}))

(defn process-messages [messages]
  (take-last 10 (sort-by :timestamp messages)))
