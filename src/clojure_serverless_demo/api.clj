(ns clojure-serverless-demo.api
  (:require [compojure.core :refer [GET routes]]
            [ring.util.response :as r]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]))

(defn builder [db-config]
  (defroutes api
    (GET "/ping" []
         (r/response {:result "pong"}))))

(defn handler [api]
  (-> api
      wrap-keyword-params
      wrap-params
      wrap-json-params
      wrap-json-response))

(defn gw-handler [api-handler])
