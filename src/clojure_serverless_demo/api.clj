(ns clojure-serverless-demo.api
  (:require [compojure.core :refer [GET POST defroutes]]
            [ring.util.response :as r]
            [clojure-serverless-demo.core :as core]
            [clojure-serverless-demo.storage :as storage]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]))

(defn builder [db-config]
  (defroutes api
    (GET "/ping" []
         (prn "ping")
         (r/response {:result "pong"}))

    (POST "/echo" {:keys [body] :as request}
          (prn body)
          (r/response body))

    (GET "/fetch-messages" []
         (-> (storage/fetch-messages db-config)
             (core/process-messages)
             (r/response)))

    (POST "/join" {:keys [body] :as request}
          (-> (core/join body)
              (storage/save-message db-config)
              (r/response)))

    (POST "/say" {:keys [body] :as request}
          (-> (core/say body)
              (storage/save-message db-config)
              (r/response)))))

(defn handler [api]
  (-> (wrap-json-body api {:keywords? true})
      (wrap-json-response)))
