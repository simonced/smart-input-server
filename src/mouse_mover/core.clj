(ns mouse-mover.core)

;; Ref: https://gist.github.com/stingh711/3760481
;; ==============================================

(import '(java.net DatagramSocket DatagramPacket))

;; socket
(def socket (DatagramSocket. 5200))

(def running (atom true))
(def buffer (make-array Byte/TYPE 1024))

;; handling packet data
(defn parse [packet]
  (println (String. (.getData packet) 0 (.getLength packet))))
;;; need to learn the meaning of that writing style...


(defn start-server []
  ;; Do something about how to stop that infinite loop!
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)]
      (do
        (println "running")
        (.receive socket packet)
        (future (parse packet))))))
