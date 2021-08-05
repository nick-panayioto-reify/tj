(ns core
  (:require
   [cheshire.core :as ch]
   [cheshire.generate :refer [add-encoder encode-str remove-encoder]]
   [clojure.tools.cli :refer [parse-opts]]
   [clojure.pprint :refer [pprint]]
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
  (let [is (if tdata
             (new ByteArrayInputStream (.getBytes tdata))
             System/in)
        reader (transit/reader is enc {:handlers read-transit-handlers})]
    (transit/read reader)))

(defn transit->json [tjson enc]
  (-> (transit->clj tjson enc)
      (ch/generate-string {:pretty pretty-printer})))

(defn json->transit [json enc]
  (-> (ch/parse-string json)
      (clj->transit enc)))

(def cli-options
  [["-i" "--input-type TYPE" "Type of input data: json, transit, or edn"
    :parse-fn keyword
    :default :transit]
   ["-o" "--output-type TYPE" "Type of output data: json, transit, or edn"
    :parse-fn keyword
    :default :json]
   ["-e" "--encoding ENCODING" "Transit encoding"
    :default :json
    :parse-fn keyword]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        {:keys [input-type output-type encoding help]} options
        [data] arguments
        out (cond
              help summary
              (and (= input-type :transit) (= output-type :json)) (transit->json data encoding)
              (and (= input-type :transit) (= output-type :edn)) (transit->clj data encoding)
              (and (= input-type :json) (= output-type :transit)) (json->transit data encoding)
              (and (= input-type :json) (= output-type :edn)) (ch/parse-string data)
              (and (= input-type :edn) (= output-type :json))
              (ch/generate-string (read-string (or data (slurp *in*))) {:pretty pretty-printer})
              (and (= input-type :edn) (= output-type :transit))
              (clj->transit (read-string (or data (slurp *in*))) encoding)
              :else (transit->json (:transit options) (:encoding options)))]
    (if (= output-type :edn)
      (pprint out)
      (println out))))

(comment
  (-main "-i" "json" "-o" "transit" "{\"x\": 22}")
  (-main "-i" "json" "-o" "edn" "{\"x\": 22}")
  (-main "[\"^ \",\"x\",22]")
  (-main "-i" "transit" "-o" "edn" "[\"^ \",\"x\",23]")
  (-main "-i" "edn" "-o" "json" "{:y 99}")
  (-main "-i" "edn" "-o" "transit" "{:y 99}")) 
