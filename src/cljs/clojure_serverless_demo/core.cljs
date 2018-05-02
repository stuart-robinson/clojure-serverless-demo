(ns clojure-serverless-demo.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [clojure-serverless-demo.events :as events]
            [clojure-serverless-demo.views :as views]
            [clojure-serverless-demo.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
