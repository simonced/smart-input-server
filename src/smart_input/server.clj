(ns smart-input.server)

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


(defn parse-mouse-data [data]
  "Parsing mouse signal data and act accordingly.
Data format: X+n,Y+m where + can also be - and n and m are numbers.
What to do if n and m are not parseable? Actually an exception is raised. 
Return true if data are correctly formated for the operation.
False otherwise."
  (println "move mouse!" data)
  (let [[_ x-offset y-offset] (re-matches #"X([+-]\d+),Y([+-]\d+)" data)
        mouse-pos (get-mouse-position)]
    (set-mouse-position (+ (:x mouse-pos) (Integer. x-offset))
                        (+ (:y mouse-pos) (Integer. y-offset)))
    true))


;; handling packet data
(defn parse [packet]
  "WIP parse received messages and make action.
- MOUSE :: moving the mouse
- EXIT :: stoping the server from the client"
  (let [message (String. (.getData packet) 0 (.getLength packet))
        [_ signal data] (re-matches #"^([A-Z]*):(.*)" message)]
    (println "received message >" message "<")

    ;; what messages do we handle?
    (case signal
      "MOUSE" (parse-mouse-data data)
      false) 
    ;; TODO add other signals
    ))


(defn -main []
  "Starting the udp server!"
  (reset! running true)
  (println "running")
  
  ;; waiting loop
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)]
      (do
        (.receive socket packet)
        (reset! running  @(future (parse packet)))
        ;; @ of a future will deference it and return the value of the computation!
        (println "new running state >" @running "<")
        )))
  (System/exit 0))
