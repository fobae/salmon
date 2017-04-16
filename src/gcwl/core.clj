;; Filename: core.clj
;; Copyright (c) 2008-2017 Clement Trösa <iomonad@riseup.net>
;; 
;; Last-Updated: 04/16/2017 Sunday 23:57:00
;; Description: Main namespace of the program

(ns gcwl.core
  "Main namespace of the program"
  (:require [gcwl.config    :as config]
            [gcwl.bot       :as bot]
            [clojure.string :as string]
            [cheshire.core  :as json]))

(defn run []
  (println "[*] Starting bot")
  (bot/start-bot []))
