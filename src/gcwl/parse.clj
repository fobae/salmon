
;; Filename: parse.clj
;; Copyright (c) 2008-2017 Clement Trösa <iomonad@riseup.net>
;; 
;; Last-Updated: 04/22/2017 Saturday 00:04:10
;; Description: Parsing utils

(ns gcwl.parse)

(def ^:private admin (atom ["iomonad"])) ;; Registred users

(defn push-admin [nick]
  "Add user to admin atom"
  (swap! admin conj nick))

(defn admin? [nick]
  "Predicate to check if user is administrator"
  (if (.contains @admin)
    (true)
    (false)))

(defn handleerr [error]
  "Just avoiding formating usage"
  (str (format "Error: %s" error)))

(defn extract-command [message]
  "Extract command from raw message"
  (if-let [[_ cmd rest-of-text]
           (re-find #"^[.](\S+)\s*(.*)$" (:text message))]
    [cmd (assoc message :text rest-of-text
                :command cmd)]))

(defn extract-word [message]
  "Extract arguments from raw message"
  (if-let [[_ word rest-of-text]
           (re-find #"^(\S+)\s*(.*)$" (:text message))]
    [word (assoc message :text rest-of-text)]))
