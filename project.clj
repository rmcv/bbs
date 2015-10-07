(def quasar-version "0.7.3")

(defproject bbs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [co.paralleluniverse/pulsar ~quasar-version]
                 [org.clojure/tools.cli "0.3.3"]]
  :java-agents [[co.paralleluniverse/quasar-core ~quasar-version]]
  :profiles {;; ----------- dev --------------------------------------
             :dev
             {:plugins [[lein-midje "3.1.3"]]
              :dependencies [[midje "1.7.0" :exclusions [org.clojure/tools.namespace]]]
              :jvm-opts [;; Debugging
                         "-ea"
                         ;"-Dco.paralleluniverse.fibers.verifyInstrumentation=true"
                         "-Dco.paralleluniverse.fibers.detectRunawayFibers=false"
                         ;"-Dco.paralleluniverse.fibers.traceInterrupt=true"
                         ;; Recording
                         ;"-Dco.paralleluniverse.debugMode=true"
                         ;"-Dco.paralleluniverse.globalFlightRecorder=true"
                         ;"-Dco.paralleluniverse.monitoring.flightRecorderLevel=1"
                         ;"-Dco.paralleluniverse.flightRecorderDumpFile=pulsar.log"
                         ;"-Xdebug"
                         ;"-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
                         ;; Logging
                         "-Dlog4j.configurationFile=log4j.xml"
                         "-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"
                         ]
              :global-vars {*warn-on-reflection* true}}

             ;; ----------- server --------------------------------------
             :server
             {:repositories {"oracle" "http://download.oracle.com/maven/"}
              :dependencies [[co.paralleluniverse/quasar-galaxy ~quasar-version]]
              :jvm-opts [;; Debugging
                         "-ea"
                         ;; Galaxy
                         "-Djgroups.bind_addr=127.0.0.1"
                         ; "-Dgalaxy.nodeId=1"
                         ; "-Dgalaxy.port=7051"
                         ; "-Dgalaxy.slave_port=8051"
                         "-Dgalaxy.multicast.address=225.0.0.1"
                         "-Dgalaxy.multicast.port=7050"
                         "-Dco.paralleluniverse.galaxy.configFile=src/test/clojure/co/paralleluniverse/pulsar/examples/cluster/config/peer.xml"
                         "-Dco.paralleluniverse.galaxy.autoGoOnline=true"
                         ;; Logging
                         "-Dlog4j.configurationFile=log4j.xml"
                         ;"-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"
                         ]}

             })
