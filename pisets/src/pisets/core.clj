(ns pisets.core
  (:use 
    compojure.core
    [compojure.handler :only [site]]
    [ring.util.response :only [status response]]
    [cheshire.core :only [generate-string]]
    org.httpkit.server)
  (:require 
    [clojure.edn :as edn]
    [compojure.route :as route]
    [clojure.java.io :as io]
    ; rabbit lib
    [langohr.core      :as rmq]
    [langohr.channel   :as lch]
    [langohr.queue     :as lq]
    [langohr.consumers :as lc]
    [langohr.basic     :as lb])
  (:gen-class))

; TODO:
; put task to rabbitmq direct queue
; develop python server to convert books using calibre
;   read tasks from rabbitmq direct queue
;   convert book and store to filesystem
;   sent message to topic based queue
; develop UI
; get new messages from topic queue and send it to related user

(def config (promise))

(defn create-message "creating a message for queue" [filepath]
  (generate-string {:path filepath}))

(defn store-book "doc-string" [bookname body]
  (let [filename (str (@config :upload-dir) bookname)
        message (create-message filename)
        file (io/file filename)]
    (io/copy (slurp body) file)
    (let [conn (rmq/connect)
          ch (lch/open conn)
          qname "pisets.tasks"]
      (lq/declare ch qname :exclusive false :auto-delete true)
      (lb/publish ch "" qname message :content-type "application/json" :type "pisets.task")
      (rmq/close ch)
      rmq/close conn)
    (println message)
    {:status 201}))

(defroutes books-api
  (POST "/:name" {{bookname :name} :params body :body} 
          (store-book bookname body)))

(defroutes all-routes
  (context "/books" [] books-api))

(defn -main [filename] 
  (deliver config (edn/read-string (slurp filename)))
  (run-server (site #'all-routes) 
              {:port (Integer. (@config :port))})
  (println (str "Server started with config '" filename "':\n " (str @config))))
