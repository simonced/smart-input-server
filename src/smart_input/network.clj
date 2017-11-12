(ns smart-input.network)

(import (java.net NetworkInterface))

(defn listNetworkInterfaces []
  "List network interfaces
Returns only the first Address for each interface (which I hope is IPv4 always...)"
  (let [intf (NetworkInterface/getNetworkInterfaces)
        intfs (enumeration-seq intf)]
    (println "Network interfaces:")
    (map (fn [n]
           {(.getDisplayName n)
            (.getHostAddress (first (enumeration-seq (.getInetAddresses n))))}
           )
         (filter (fn [n] (and (.isUp n) (not (.isVirtual n))))
                 intfs)
         )
    )
  )
