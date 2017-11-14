(ns smart-input.network)

(import (java.net NetworkInterface))

(defn listNetworkInterfaces []
  "List network interfaces
Returns a list of interfaces with name+ip like so:
[[interfacename1 192.168.1.1] [etc ...]]"
  (let [intf (NetworkInterface/getNetworkInterfaces)
        intfs (enumeration-seq intf)]
    (println "Network interfaces:")
    (map (fn [n]
           [(.getDisplayName n)
            (.getHostAddress (first (enumeration-seq (.getInetAddresses n))))]
           )
         (filter (fn [n] (and (.isUp n) (not (.isVirtual n))))
                 intfs)
         )
    )
  )
