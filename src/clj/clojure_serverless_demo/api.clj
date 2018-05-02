(ns clojure-serverless-demo.api
  (:require [compojure.core :refer [GET POST defroutes]]
            [ring.middleware.cors :refer [wrap-cors]]
            [clojure-serverless-demo.chat :as chat]
            [clojure-serverless-demo.storage :as storage]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]))

(defn response [body status max-age]
  {:status status
   :body body
   :headers {"Cache-Control" (str "max-age=" max-age)}})

(defn builder [db-config]
  (defroutes api
    (GET "/ping" []
         (response {:result "pong"} 200 0))

    (POST "/echo" {:keys [body] :as request}
          (response body 200 0))

    (GET "/fetch-messages" []
         (-> (storage/fetch-messages db-config)
             (chat/process-messages)
             (response 200 0)))

    (POST "/join" {:keys [body] :as request}
          (-> (chat/join body)
              (storage/save-message db-config)
              (response 200 0)))

    (POST "/say" {:keys [body] :as request}
          (-> (chat/say body)
              (storage/save-message db-config)
              (response 200 0)))))

(defn handler [api]
  (-> (wrap-cors api :access-control-allow-origin [#".*"]
                     :access-control-allow-methods [:get :put :post :delete])
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
