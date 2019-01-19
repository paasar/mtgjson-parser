(ns mtgjson-parser.core
  (:require [cheshire.core :refer [parse-string]]
            [clojure.string :refer [join]]
            [medley.core :refer [distinct-by]])
  (:import [java.io File])
  (:gen-class))

(def input (parse-string (slurp "resources/AllSets.json") true))

(defn- ensure-empty-dir [output-dir]
  (.mkdir (File. output-dir))
    (doseq [f (rest (file-seq (File. output-dir)))]
      (.delete f)))

(defn create-card-names-per-block []
  (let [ordinals (map inc (range))
        sorted-blocks (sort-by :releaseDate (vals input))
        blocks-with-ordinal (map vector ordinals sorted-blocks)
        output-dir "./output"]
    (ensure-empty-dir output-dir)
    (doseq [[n {:keys [code name cards]}] blocks-with-ordinal]
      (let [content (->> cards
                         (map (juxt :name :multiverseId))
                         (remove #(nil? (second %)))
                         (distinct-by first)
                         (sort-by first)
                         (map #(join ";" %))
                         (join "\n"))]
        (when (seq content)
          (spit (format "%s/%04d__%s__%s" output-dir n code name) content))))))

(defn -main [& args]
  (println "Starting")
  (create-card-names-per-block)
  (println "Done"))

;{
; "LEA" : { /* set data */ },
; "LEB" : { /* set data */ },
; "2ED" : { /* set data */ },
; ...0
; }

;{
;  "name" : "Nemesis",            // The name of the set
;  "code" : "NMS",                // The set's abbreviated code
;  "gathererCode" : "NE",         // The code that Gatherer uses for the set. Only present if diferent than 'code'
;  "oldCode" : "NEM",             // An old style code used by some Magic software. Only present if different than 'gathererCode' and 'code'
;  "magicCardsInfoCode" : "ne",   // The code that magiccards.info uses for the set. Only present; if magiccards.info has this set
;  "releaseDate" : "2000-02-14"   // When the set was released (YYYY-MM-DD). For promo sets, the ;date the first card was released.
;  "border" : "black",            // The type of border on the cards, either "white", "black" or ;"silver"
;  "type" : "expansion",          // Type of set. One of: "core", "expansion", "reprint", "box", ;"un",
;                                 //                      "from the vault", "premium deck", "duel; deck",
;                                 //                      "starter", "commander", "planechase", ";archenemy",
;                                 //                      "promo", "vanguard", "masters", "conspi;racy", "masterpiece"
;  "block" : "Masques",           // The block this set is in,
;  "onlineOnly" : false,          // Present and set to true if the set was only released online
;  "booster" : [ "rare", ... ],   // Booster contents for this set, see below for details
;  "cards" : [ {}, {}, {}, ... ]  // The cards in the set
;}
