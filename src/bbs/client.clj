(ns bbs.client
  "A bbs client"
  (:use [co.paralleluniverse.pulsar core actors])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:refer-clojure :exclude [promise await])
  (:gen-class))

(defn- client-prn [name msg]
  (println (format "[%s's client] - %s" name msg)))

(defn- prn-msg [name from msg]
  (client-prn name (format "%s: %s" from msg)))

(defsfn client [name server]
  (receive
   [:new-msg from msg] (do (prn-msg name from msg)
                           (recur name server))
   [:info msg] (do
                 (client-prn name msg)
                 (recur name server))
   [:send msg] (do
                 (! server [:send @self msg])
                 (recur name server))
   :disconnect (client-prn name "Disconnected")
   [:exit _ _ _] (client-prn name "Lost connection. Shutting down...")))

(defn create-client [name server]
  (let [c (spawn :trap true client name server)]
    (! server [:join c name])
    c))

(def cli-options
  ;; An option with a required argument
  [["-n" "--name NAME" "Client name"
    :default (.getHostName (java.net.InetAddress/getLocalHost))]
   ["-s" "--server SERVER" "Server bulletin board name"
    :default "bbs"
    :parse-fn #(keyword %)]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])


(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (create-client (:name options) (:server options))))
