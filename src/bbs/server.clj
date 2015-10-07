(ns bbs.server
  "A bbs server"
  (:use [co.paralleluniverse.pulsar core actors])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:refer-clojure :exclude [promise await]))

(defn- get-name [ref clients]
  (-> (first (filter #(= (:ref %) ref) clients))
      :name))

(defn- broadcast [msg clients]
  (doseq [c clients]
    (! (:ref c) msg)))

(defsfn server [clients]
  (receive
   [:join ref name] (do
                      (link! ref)
                      (broadcast
                       [:info (str name " joined the chat")]
                       clients)
                      (recur (conj clients {:name name :ref ref})))
   [:send ref msg]  (do
                      (broadcast
                       [:new-msg (get-name ref clients) msg]
                       clients)
                      (recur clients))
   [:exit _ ref _]  (do
                      (broadcast
                       [:info (str (get-name ref clients) " left the chat")]
                       clients)
                      (recur clients))
   :shutdown        (println "Shutting down")))

(defn create-server []
  (spawn :trap true server '()))

(def cli-options
  [["-n" "--name NAME" "Server name"
    :default "bbs"]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        s (create-server)]
    (register! (keyword (:name options)) s))
  :ok)
