(ns clojure-serverless-demo.api
  (:require [compojure.core :refer [GET POST defroutes]]
            [clojure-serverless-demo.core :as core]
            [clojure-serverless-demo.storage :as storage]
            [ring.util.response :as r]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-params
                                          wrap-json-response
                                          wrap-json-body]]))

(defn builder [db-config]
  (defroutes api
    (GET "/ping" []
         (r/response {:result "pong"}))

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
  (-> api
      (wrap-json-body {:keywords? true})
      wrap-json-response))
