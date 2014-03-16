(defproject pisets "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :main pisets.core
  :aot [pisets.core]
  :uberjar-name "pisets.jar"
  :plugins [[lein-cljsbuild "1.0.2"]]
  :cljsbuild {
              :builds [{
                        :source-paths ["src-cljs"]
                        :compiler {
                                   :output-to "/tmp/main.js"
                                   :optimiztions :whitespace
                                   :pretty-print true}}]}
  :dependencies [
                 [tailrecursion/javelin "3.1.1"]
                 [http-kit "2.1.16"]
                 [com.novemberain/langohr "2.7.1"]
                 [cheshire "5.3.1"]
                 [ring "1.2.1"]
                 [compojure "1.1.6"]
                 [org.clojure/clojure "1.5.1"]])
