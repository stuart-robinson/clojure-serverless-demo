(defproject clojure-serverless-demo "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [cljs-ajax "0.5.1"]
                 [com.amazonaws/aws-lambda-java-core "1.0.0"]
                 [clj-http "3.7.0"]
                 [compojure "1.6.0"]
                 [environ "1.1.0"]
                 [ring "1.6.3"]
                 [ring-cors "0.1.12"]
                 [ring/ring-mock "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [com.amazonaws/DynamoDBLocal "1.10.5.1"]
                 [com.amazonaws/aws-java-sdk-dynamodb "1.11.320"]
                 [com.taoensso/faraday "1.9.0"]]

  :repositories [["dynamodblocal"  {:url "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"}]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "test/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler clojure-serverless-demo.handler/dev-handler}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :profiles
  {:uberjar {:aot :all
             :uberjar-name "clojure-serverless-demo-standalone.jar"}
   :dev
   {:dependencies [[binaryage/devtools "0.9.4"]
                   [day8.re-frame/re-frame-10x "0.3.0"]
                   [day8.re-frame/tracing "0.5.0"]
                   [figwheel-sidecar "0.5.13"]
                   [com.cemerick/piggieback "0.2.2"]]

    :plugins      [[lein-figwheel "0.5.13"]]}
   :prod { :dependencies [[day8.re-frame/tracing-stubs "0.5.0"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "clojure-serverless-demo.core/mount-root"}
     :compiler     {:main                 clojure-serverless-demo.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true
                                           "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :jar true
     :compiler     {:main            clojure-serverless-demo.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}


    ]}

  :main clojure-serverless-demo.core


;;  :prep-tasks [["cljsbuild" "once" "min"] "compile"]
  )
