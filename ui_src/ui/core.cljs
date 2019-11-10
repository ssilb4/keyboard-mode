(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines]]))

(def electron       (js/require "electron"))
(def ipcRenderer    (.-ipcRenderer electron))

(def join-lines (partial string/join "\n"))

(enable-console-print!)

(defonce divX        (atom 0))
(defonce divY        (atom 0))

(defonce proc (js/require "child_process"))

(defn setDivSize []
  (.send ipcRenderer "setDiv" @divX @divY)
  (.log js/console "setDivSize"))

(defn root-component
 []
  [:div
   [:p
    [:form
     {:on-submit (fn [^js/Event e]
                   (.preventDefault e)
                   (setDivSize))}
     [:input#divX
      {:type "text"
       :placeholder "type in divX"
       :on-change (fn [^js/Event e]
                    (reset! divX ^js/String (.-value (.-target e))))}]
     [:input#divY
      {:type "text"
       :placeholder "type in y div"
       :on-change (fn [^js/Event e]
                    (reset! divY ^js/String (.-value (.-target e))))}]
     [:input
      {:type :submit}]]]])

(defn root-test
  []
  [:div
   [:button]])

(reagent/render
  [root-component]
  (.-body js/document))








