(ns pisets.core
  (:use 
    compojure.core
    [compojure.handler :only [site]]
    [ring.util.response :only [status response]]
    org.httpkit.server)
  (:require 
    [langohr.core :as rmq]
    [clojure.edn :as edn]
    [compojure.route :as route]
    [ring.middleware.multipart-params :as mp]
    [clojure.java.io :as io])
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

(defn handle "doc-string" [bookname body]
  (let [file (io/file (str (@config :upload-dir) bookname))]
    (io/copy (slurp body) file)
    {:status 201}))

(defroutes books-api
  (POST "/:name" {{bookname :name} :params body :body} 
          (handle bookname body)))

(defroutes all-routes
  (context "/books" [] books-api))

(defn -main [filename] 
  (deliver config (edn/read-string (slurp filename)))
  (run-server (site #'all-routes) 
              {:port (Integer. (@config :port))})
  (println (str "Server started with config '" filename "':\n " (str @config))))
