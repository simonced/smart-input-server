(ns mouse-mover.core)

;; Ref: https://gist.github.com/stingh711/3760481
;; ==============================================

;;; UDP socket class
(import '(java.net DatagramSocket DatagramPacket))

;;; Rotot class
(import java.awt.Robot)
(import java.awt.MouseInfo)
(import java.awt.Toolkit)


;;; screen settings
(defn get-screen-size []
  "Return the screen dimensions as a typle {:width :height}"
  (let [dimensions (.getScreenSize (Toolkit/getDefaultToolkit))]
    {:width (.getWidth dimensions) :height (.getHeight dimensions)}
    ))


;;; ==================== mouse related ====================

(def robot (new Robot))

;;; current mouse position
(defn get-mouse-position []
  "Returns the mouse position on current screen as a tuple {:x :y}"
  (let [mouse-location (.getLocation (MouseInfo/getPointerInfo))]
    {:x (.x mouse-location) :y (.y mouse-location)}))


;;; moving the mouse on the screen
(defn set-mouse-position [x y]
  "Sets mouse cursor on screen"
  (.mouseMove robot x y))


;;; ==================== server related ===================

;; socket
(def socket (DatagramSocket. 5200))     ; need to learn the meaning of that writing style...
(def running (atom true))               ;can we change that @running at runtime?
(def buffer (make-array Byte/TYPE 1024))


;; handling packet data
(defn parse [packet]
  "TODO parse received messages and make action. First to come:
- moving the mouse"
  (let [message (String. (.getData packet) 0 (.getLength packet))]
    (println message)))



(defn start-server []
  ;; Do something about how to stop that infinite loop!
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)]
      (do
        (println "running")
        (.receive socket packet)
        (future (parse packet))))))
