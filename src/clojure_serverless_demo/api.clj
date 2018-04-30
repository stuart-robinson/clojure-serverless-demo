(ns clojure-serverless-demo.api
  (:require [compojure.core :refer [GET POST defroutes]]
            [ring.util.response :as r]
            [ring.middleware.cors :refer [wrap-cors]]
            [clojure-serverless-demo.chat :as chat]
            [clojure-serverless-demo.storage :as storage]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]))

(defn builder [db-config]
  (defroutes api
    (GET "/ping" []
         (r/response {:result "pong"}))

    (POST "/echo" {:keys [body] :as request}
          (r/response body))

    (GET "/fetch-messages" []
         (-> (storage/fetch-messages db-config)
             (chat/process-messages)
             (r/response)))

    (POST "/join" {:keys [body] :as request}
          (-> (chat/join body)
              (storage/save-message db-config)
              (r/response)))

    (POST "/say" {:keys [body] :as request}
          (-> (chat/say body)
              (storage/save-message db-config)
              (r/response)))))

(defn handler [api]
  (-> (wrap-cors api :access-control-allow-origin [#".*"]
                     :access-control-allow-methods [:get :put :post :delete])
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
