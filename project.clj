(defproject smart_input "0.1.0-SNAPSHOT"
  :description "UDP server for remote controlling mouse and keyboard from a smart phone"
  :url "https://github.com/simonced/smart-input-server/"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :aot  [smart-input.server]
  :main smart-input.server)
