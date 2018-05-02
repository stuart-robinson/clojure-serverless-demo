(ns clojure-serverless-demo.chat)

(defn say [{:keys [name message]}]
  (let [timenow-ms (System/currentTimeMillis)]
    {:id (str (java.util.UUID/randomUUID))
     :name name
     :message message
     :channel "default"
     :timestamp timenow-ms
     :order (- 2147483647 timenow-ms)}))

(defn join [{:keys [name]}]
  (say {:name "channel"
        :message (str name " joined the channel...")}))

(defn process-messages [messages]
  messages)
