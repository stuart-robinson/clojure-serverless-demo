(ns clojure-serverless-demo.events
  (:require [re-frame.core :as re-frame]
            [clojure-serverless-demo.db :as db]
            [clojure-serverless-demo.config :as config]
            [ajax.core :refer [GET POST]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::user-submitted-message
 (fn [db [_ message]]
   (POST
    (str config/host "/say")
    {:params {:name (:name db)
              :message message}
     :format :json
     :response-format :json
     :keywords? true
     :handler       #(re-frame/dispatch [::process-say-response %])
     :error-handler #(re-frame/dispatch [::bad-response %])})
   db))

(re-frame/reg-event-db
 ::user-submitted-username
 (fn [db [_ name]]
   (POST
    (str config/host "/join")
    {:params {:name name}
     :format :json
     :response-format :json
     :keywords? true
     :handler       #(re-frame/dispatch [::process-join-response % name])
     :error-handler #(re-frame/dispatch [::bad-response %])})
   db))

(re-frame/reg-event-db
 ::process-join-response
 (fn [db [_ response name]]
   (re-frame/dispatch [::fetch-messages "foo"])
   (assoc db :name name)))

(re-frame/reg-event-db
 ::fetch-messages
 (fn [db [_ _]]
   (GET
    (str config/host "/fetch-messages")
    {:response-format :json
     :keywords? true
     :handler       #(re-frame/dispatch [::process-fetch-messages-response %])
     :error-handler #(re-frame/dispatch [::bad-response %])})
   db))

(re-frame/reg-event-db
 ::process-say-response
 (fn [db [_ response]]
   (re-frame/dispatch [::fetch-messages "foo"])
   db))

(re-frame/reg-event-db
 ::process-fetch-messages-response
 (fn [db [_ response]]
   (assoc db :messages response)))


(re-frame/reg-event-db
 ::bad-response
 (fn [db [_ response]]
   (.debug js/console response)
   db))
