(ns smart-input.server
  (:gen-class))

;; Ref: https://gist.github.com/stingh711/3760481
;; ==============================================

;;; UDP socket class
(import '(java.net DatagramSocket DatagramPacket))

;;; Rotot class
(import java.awt.Robot)
(import java.awt.MouseInfo)
(import java.awt.event.InputEvent)
(import java.awt.Toolkit)
(import java.awt.event.KeyEvent)
(import java.awt.im.InputContext)

;;; utilities
(require '[clojure.string :as str])

;;; my keyboard layout lib
(use '[smart-input.keyboard-layout :as kbl])
;;; my network utilities lib
(use '[smart-input.network :as netwk])

;;; screen settings
(defn get-screen-size []
  "Return the screen dimensions as a typle {:width :height}"
  (let [dimensions (.getScreenSize (Toolkit/getDefaultToolkit))]
    {:width (.getWidth dimensions) :height (.getHeight dimensions)}))


;;; ==================== automation ====================


(def robot (new Robot))
(def autodelayKey 50)


;;; ==================== mouse related ====================


;;; current mouse position
(defn get-mouse-position []
  "Returns the mouse position on current screen as a tuple {:x :y}"
  (let [mouse-location (.getLocation (MouseInfo/getPointerInfo))]
    {:x (.x mouse-location) :y (.y mouse-location)}))


;;; moving the mouse on the screen
(defn set-mouse-position [x y]
  "Sets mouse cursor on screen"
  (.mouseMove robot x y))


;;; ==================== Keyboard related ====================

(defn key-detect-layout []
  "Trying to detect keyboard layout."
  (.toString (.getLocale (InputContext/getInstance))))

(def key-layout (keyword (key-detect-layout)))


;;; pressing key sequences for a symbol requiring modifiers
;;; like some punctuation
(defn key-press-symbol [seq_]
  "Pressing keys in sequence"
  (let [[action keycode] (first seq_) next (rest seq_)]
    (case action
      :down (.keyPress robot keycode)
      :up   (.keyRelease robot keycode)
      nil
      )
    
    ;; calling again with the rest of the sequence
    ;; (println next)
    (cond (not (empty? next))
          ;; (println "next call")
          (key-press-symbol next)))
  )


(defn key-press [symbol]
  "Simply simulates key presses.
Layouts needs to be handled differently for non alphanumeric characters!"
  (let [key-seq (kbl/press-on-layout key-layout symbol)]
    (if key-seq
      (key-press-symbol key-seq)
      (println "No key seq found!")))
  )


;;; reading about Keys: https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
(defn key-press-chord [chord]
  "Press chords of keys like <Ctrl> + <a>.
chord parameter syntax: C-a"
  (let [start (first chord) next (rest chord)]
    (case start
      "C" (do (.keyPress robot KeyEvent/VK_CONTROL) (key-press-chord next) (.keyRelease robot KeyEvent/VK_CONTROL))
      "A" (do (.keyPress robot KeyEvent/VK_ALT) (key-press-chord next) (.keyRelease robot KeyEvent/VK_ALT))
      "S" (do (.keyPress robot KeyEvent/VK_SHIFT) (key-press-chord next) (.keyRelease robot KeyEvent/VK_SHIFT))
      (key-press start)
      )))


;;; ==================== server related ===================

;; socket
(def socket-port 5200)


(defn parse-mouse-data-move [data]
  "Parsing mouse signal data and act accordingly.
Data format: X+n,Y+m where + can also be - and n and m are numbers.
What to do if n and m are not parseable? Actually an exception is raised. 
Return true if data are correctly formated for the operation.
False otherwise."
  ;; (println "move mouse!" data)
  (let [[_ x-offset y-offset] (re-matches #"X([+-]\d+),Y([+-]\d+)" data)
        mouse-pos (get-mouse-position)]
    (.setAutoDelay robot 0)                ; delay between key presses in ms
    (set-mouse-position (+ (:x mouse-pos) (Integer. x-offset))
                        (+ (:y mouse-pos) (Integer. y-offset)))
    true))


(defn parse-key-chord [chord-data]
  "Utility function that parses one chord at a time.
Data format: 
- a will press the letter a (or any other single symbol)
- C-a will press Ctrl + a
- S-s will press Shift + a
- A-a will press Alt + a
and combinaisons will be possible like:
- C-A-b wil press Ctrl + Alt + b"
  (let [chord (str/split chord-data #"-")]
    ;; (println "parsing chord:" chord)
    (key-press-chord chord))
  )


(defn parse-key-data [data]
  "Parsing key press data and act accordingly.
Mutliple chords can be sent if separated by a space.
ie: a b c C-a
will push in sequence a,b,c then Ctrl+a"
  (let [chords (str/split data #" ")]
    ;; (println "keys received: " chords)
    (.setAutoDelay robot autodelayKey)                ; delay between key presses in ms
    (doseq [c chords] (parse-key-chord c))
    true)
  )


(defn parse-mouse-data-click [data]
  "Makes the mouse click
TODO deal with different buttons, for now only left click (=> data ignored)"
  ;; (println "mouse click!" data)
  (.mousePress robot InputEvent/BUTTON1_MASK)
  (.mouseRelease robot InputEvent/BUTTON1_MASK)
  true)


(defn parse-mouse-data-scroll [data]
  "Makes the mouse scroll
@parameter data is the ammount of vertical scroll
can be positive of negative"
  ;; (println "mouse scrool!" data)
  (.setAutoDelay robot 0)
  (.mouseWheel robot (Integer.  data))
  true)


;; handling packet data
(defn parse [packet]
  "WIP parse received messages and make action.
- MOUSE_MOVE :: moving the mouse
- MOUSE_CLICK :: click the mouse button (1:left)
- MOUSE_SCROLL :: scroll wheel
- EXIT :: stoping the server from the client"
  (let [message (String. (.getData packet) 0 (.getLength packet))
        [_ signal data] (re-matches #"^([A-Z_]+):(.*)" message)]
    ;; (println "received message: " message)

    ;; what messages do we handle?
    (case signal
      "MOUSE_MOVE" (parse-mouse-data-move data)
      "MOUSE_CLICK" (parse-mouse-data-click data)
      "MOUSE_SCROLL" (parse-mouse-data-scroll data)
      "KEY_PRESS" (parse-key-data data)
      false) 
    ;; TODO add other signals
    ))


(defn -main []
  "Starting the udp server!"

  (def socket (DatagramSocket. socket-port))     ; need to learn the meaning of that writing style...
  (def running (atom true))               ;can we change that @running at runtime?
  (def buffer (make-array Byte/TYPE 1024))

  ;;(reset! running true)

  ;; listing of network interfaces
  (println (map (fn [i]
                  (let [[name ip] i]
                    (format "%s\n\t%s\n" name ip)))
                (netwk/listNetworkInterfaces)))

  ;; display of listening port and detected keyboard layout
  (println "running.\nPort:" socket-port "\nKeyboard layout:" key-layout)
  
  ;; waiting loop
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)]
      (do
        (.receive socket packet)
        (reset! running  @(future (parse packet)))
        ;; @ of a future will deference it and return the value of the computation!
        ;; (println "new running state >" @running "<")
        )))
  (System/exit 0))
