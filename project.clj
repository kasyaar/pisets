(defproject pisets "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :main pisets.core
  :aot [pisets.core]
  :uberjar-name "pisets.jar"
  :dependencies [
                 [http-kit "2.1.16"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [compojure "1.1.6"]
                 [prismatic/dommy "0.1.2"]
                 [tailrecursion/javelin "3.1.1"]
                 [com.novemberain/langohr "2.5.0"]
                 [cheshire "5.3.1"]
                 [org.clojure/clojure "1.5.1"]])
