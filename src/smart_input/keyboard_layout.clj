(ns smart-input.keyboard-layout)

;;; libs
(import java.awt.event.KeyEvent)


;;; Each layout entry is a dictionnary of key sequence for each symbol
(def layouts {:ja_JP
              {"$" [{:down KeyEvent/VK_SHIFT} {:down KeyEvent/VK_4} {:up KeyEvent/VK_4} {:up KeyEvent/VK_SHIFT}]}})


(defn press-on-layout [layout-name key-str]
  "Adapts key presses to different layouts.
@param layout-name is something like 'ja_JP'
@param key-str is one character like '$' to be pressed in that layout"
  ;; TODO
  (println (key-str (layout-name layouts)))
  )
