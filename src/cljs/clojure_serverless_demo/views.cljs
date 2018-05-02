(ns clojure-serverless-demo.views
  (:require [re-frame.core :as re-frame]
            [clojure-serverless-demo.subs :as subs]
            [clojure-serverless-demo.events :as events]
            [reagent.core :as r]))

(defn user-message-input []
  (let [message (r/atom "")
        ok-click (fn [event]
                   (.preventDefault event)
                   (when-not (empty? @message)
                     (re-frame/dispatch [::events/user-submitted-message @message])
                     (reset! message "")))
        fetch-click (fn [_]
                      (re-frame/dispatch [::events/fetch-messages "foo"]))]
    (fn []
      [:form
       [:div.box.columns
        [:div.column.is-four-fifths
         [:input.input.is-primary {:type "text"
                                   :placeholder " something..."
                                   :value @message
                                   :on-change #(reset! message (-> % .-target .-value))}]]
        [:div.column.is-one-fifth
         [:button.button.is-primary.is-rounded
          {:type "input"
           :on-click #(ok-click %) }
          "Send"]]]])))

(defn user-join-input []
  (let [name (r/atom "")
        ok-click (fn [event]
                   (.preventDefault event)
                   (when-not (empty? @name)
                     (re-frame/dispatch [::events/user-submitted-username @name])
                     (reset! name "")))]
    (fn []
      [:form
       [:div.box.columns
        [:div.column.is-one-fifth
         [:input.input {:type "text"
                        :placeholder "enter username"
                        :value @name
                        :on-change #(reset! name (-> % .-target .-value))}]]
        [:div.column.is-one-fifth
         [:span
          [:button.button.is-primary {:type "input"
                                      :on-click #(ok-click %) }
           "Join"]
          ]]]])))


(defn message-line [message]
  [:li.box
   [:span.has-text-weight-bold (:name message)]
   [:span " "]
   [:span (:message message)]])

(defn message-panel []
  (let [messages (re-frame/subscribe [::subs/messages])]
    (fn []
      [:div
       (into [:ol.chat] (map message-line @messages))])))

(defn join-panel []
  (fn []
    [user-join-input]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    (fn []
      [:div#main.has-background-grey-lighter
       [:div.navbar.is-fixed-top.is-primary
        [:span.navbar-item.has-text-white.has-text-weight-bold "Chat"]]
       (if (nil? @name)
         [join-panel]
         [:div
          [:section.section.has-background-grey-lighter
           [message-panel]]
          [:section.section
           [user-message-input]]])])))
