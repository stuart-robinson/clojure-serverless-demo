(ns clojure-serverless-demo.config)

(def debug?
  ^boolean goog.DEBUG)

(def host
  (if debug?
    "http://localhost:8888"
    "https://odkas8ut1b.execute-api.eu-west-2.amazonaws.com/prod"))
