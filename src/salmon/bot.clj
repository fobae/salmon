;; Filename: bot.clj
;; Copyright (c) 2008-2017 Clement Trösa <iomonad@riseup.net>
;; 
;; Last-Updated: 05/06/2017 Saturday 22:05:24
;; Description: Bot related function

(ns salmon.bot
  (:require [irclj.core     :as irc]
            [irclj.events   :as events]
            [salmon.parse     :refer [extract-command]]
            [clojure.string :as string])
  (:gen-class))

(defn choose-handler [plugins name]
  "Small function for handler selection"
  (->> plugins (map #(get-in % [:commands name])) (remove nil?) first))

(defn respond-with [irc message responses]
  "Parse response using with procedure"
  (when-not (nil? responses)
    (def vec-responses (if (coll? responses) responses [responses]))
    (doseq [r vec-responses]
      (irc/reply irc message r))))

(defn server-callback [plugins]
  "Server callback to analyse and parse raw data"
  (fn [irc msg]
    (try
      (when-let [[command updated-message] (extract-command msg)]
        (println (format "CMD: %s %s" command (str (:text msg))))
        (if-let [handler (choose-handler plugins command)]
          (when-let [responses (handler irc updated-message)]
            (respond-with irc updated-message responses))
          (respond-with irc updated-message
                        (str (format "Command %s not found." (get updated-message :command))))))
      (catch Throwable e
        (irc/reply irc msg (str "Error" e)) ; Throw error to channel
        (println (.getMessage e))
        (.printStackTrace e)))))

(defn run-bot [plugins & {:keys [host port nick password channels server-password]}]
  "Bot entry point"
  ;; Create local bot instance
  (def bot (irc/connect host port nick
                        :pass server-password
                        :callbacks {:privmsg (server-callback plugins)
                                    :raw-log irclj.events/stdout-callback}))
                                        ;  (list-plugins)
  (println (format "[*] Connecting as %s@%s" nick host))
  (when password (irc/identify bot password)) ; Auth if defined
  (dosync
   (alter bot assoc ; Add other options to bot connection
          :prefixes {}
          :ssl? true
          :plugins plugins)
   (doseq [c channels]
     (irc/join bot c)) ; Join each channels
   ))


(defn start-bot [plugins]
  "Start the bot instance"
  (let [nick "salmon"
        host "irc.freenode.net"
        port 6667
        channels (-> (str "#bottest,#testing")
                     (.split ",") ; Pass each chan to vec
                     vec)]
    (run-bot plugins
             :host host
             :port port
             :nick nick
             :channels channels)))