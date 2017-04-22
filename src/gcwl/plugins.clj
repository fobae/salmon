;; Filename: plugins.clj
;; Copyright (c) 2008-2017 Clement Trösa <iomonad@riseup.net>
;; 
;; Last-Updated: 04/22/2017 Saturday 22:00:40
;; Description: Plugins dsl to build commands

(ns gcwl.plugins
  (:require [gcwl.plugins.ping :as ping]
            [gcwl.plugins.date :as date]
            [gcwl.plugins.echo :as echo]
            [gcwl.plugins.auth :as auth]
            [gcwl.plugins.about :as about]
            [gcwl.plugins.testauth :as testauth]
            [gcwl.plugins.kernel :as kernel]))

(defmacro defplugin [cmd & fn]
  "Macro to create a plugin"
  '(def plugin {:command cmd
                :commands {cmd (fn [irc message] ~fn)}}))
;; Pass 
(def plugins-enabled (atom [ping/plugin
                            date/plugin
                            echo/plugin
                            auth/plugin
                            about/plugin
                            testauth/plugin
                            kernel/plugin]))

(defn list-plugins []
  "List enabled plugins to *out*"
  (doseq [p @plugins-enabled]
    (println (format "[*] Enabled plugin: %s" p))))
