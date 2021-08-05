(ns core
  (:require
   [cheshire.core :as ch]
   [cheshire.generate :refer [add-encoder encode-str remove-encoder]]
   [clojure.tools.cli :refer [parse-opts]]
   [cognitect.transit :as transit])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]
           (java.time LocalDate Instant)
           (java.util.regex Pattern))
  (:gen-class))

(def pretty-printer (ch/create-pretty-printer
                     (assoc ch/default-pretty-print-options
                            :indent-arrays? true)))

(add-encoder Object encode-str)

(def write-transit-handlers
  {Pattern (transit/write-handler "java.util.regex.Pattern"
                                  #(.toString %))
   LocalDate (transit/write-handler "java.time.LocalDate"
                                    #(hash-map :day (.getDayOfMonth %) :month (.getMonthValue %) :year (.getYear %)))
   Instant (transit/write-handler "java.time.Instant"
                                  #(.getEpochSecond %))})

(def read-transit-handlers
  {"java.time.LocalDate" (transit/read-handler #(LocalDate/of (:year %) (:month %) (:day %)))
   "java.time.Instant" (transit/read-handler #(Instant/ofEpochSecond %))})

(defn clj->transit [data enc]
  (let [os (new ByteArrayOutputStream 4096)
        writer (transit/writer os enc {:handlers write-transit-handlers})]
    (transit/write writer data)
    (.toString os)))

(defn transit->clj [tdata enc]
  (let [is (new ByteArrayInputStream (.getBytes tdata))
        reader (transit/reader is enc {:handlers read-transit-handlers})]
    (transit/read reader)))

(defn transit->json [tjson enc]
  (-> (transit->clj tjson enc)
      (ch/generate-string {:pretty pretty-printer})))

(defn json->transit [json enc]
  (-> (ch/parse-string json)
      (clj->transit enc)))

(def cli-options
  [["-t" "--transit TRANSIT" "Transit to convert to json"
    :parse-fn identity]
   ["-j" "--json JSON" "JSON to convert to transit"
    :parse-fn identity]
   ["-e" "--encoding ENCODING" "Transit encoding"
    :default :json
    :parse-fn keyword]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (println summary)
      (:transit options) (println (transit->json (:transit options) (:encoding options)))
      (:json options) (println (json->transit (:json options) (:encoding options)))
      :else (println summary))))

(comment 
  (-main "-j" "{\"x\": 22}")
  (-main "-t" "[\"^ \",\"x\",22]")) 
