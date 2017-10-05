(ns smart-input.keyboard-layout
  (:import java.awt.event.KeyEvent))

;; Alphabet characters (how original and smart!)
(def vk-alpha {
               "a"        [[:down KeyEvent/VK_A]          [:up KeyEvent/VK_A]]
               "b"        [[:down KeyEvent/VK_B]          [:up KeyEvent/VK_B]]
               "c"        [[:down KeyEvent/VK_C]          [:up KeyEvent/VK_C]]
               "d"        [[:down KeyEvent/VK_D]          [:up KeyEvent/VK_D]]
               "e"        [[:down KeyEvent/VK_E]          [:up KeyEvent/VK_E]]
               "f"        [[:down KeyEvent/VK_F]          [:up KeyEvent/VK_F]]
               "g"        [[:down KeyEvent/VK_G]          [:up KeyEvent/VK_G]]
               "h"        [[:down KeyEvent/VK_H]          [:up KeyEvent/VK_H]]
               "i"        [[:down KeyEvent/VK_I]          [:up KeyEvent/VK_I]]
               "j"        [[:down KeyEvent/VK_J]          [:up KeyEvent/VK_J]]
               "k"        [[:down KeyEvent/VK_K]          [:up KeyEvent/VK_K]]
               "l"        [[:down KeyEvent/VK_L]          [:up KeyEvent/VK_L]]
               "m"        [[:down KeyEvent/VK_M]          [:up KeyEvent/VK_M]]
               "n"        [[:down KeyEvent/VK_N]          [:up KeyEvent/VK_N]]
               "o"        [[:down KeyEvent/VK_O]          [:up KeyEvent/VK_O]]
               "p"        [[:down KeyEvent/VK_P]          [:up KeyEvent/VK_P]]
               "q"        [[:down KeyEvent/VK_Q]          [:up KeyEvent/VK_Q]]
               "r"        [[:down KeyEvent/VK_R]          [:up KeyEvent/VK_R]]
               "s"        [[:down KeyEvent/VK_S]          [:up KeyEvent/VK_S]]
               "t"        [[:down KeyEvent/VK_T]          [:up KeyEvent/VK_T]]
               "u"        [[:down KeyEvent/VK_U]          [:up KeyEvent/VK_U]]
               "v"        [[:down KeyEvent/VK_V]          [:up KeyEvent/VK_V]]
               "w"        [[:down KeyEvent/VK_W]          [:up KeyEvent/VK_W]]
               "x"        [[:down KeyEvent/VK_X]          [:up KeyEvent/VK_X]]
               "y"        [[:down KeyEvent/VK_Y]          [:up KeyEvent/VK_Y]]
               "z"        [[:down KeyEvent/VK_Z]          [:up KeyEvent/VK_Z]]
               "<tab>"    [[:down KeyEvent/VK_TAB]        [:up KeyEvent/VK_TAB]]
               "<cr>"     [[:down KeyEvent/VK_ENTER]      [:up KeyEvent/VK_ENTER]]
               "<bs>"     [[:down KeyEvent/VK_BACK_SPACE] [:up KeyEvent/VK_BACK_SPACE]]
               "<ins>"    [[:down KeyEvent/VK_INSERT]     [:up KeyEvent/VK_INSERT]]
               "<del>"    [[:down KeyEvent/VK_DELETE]     [:up KeyEvent/VK_DELETE]]
               "<esc>"    [[:down KeyEvent/VK_ESCAPE]     [:up KeyEvent/VK_ESCAPE]]
               "<home>"   [[:down KeyEvent/VK_HOME]       [:up KeyEvent/VK_HOME]]
               "<end>"    [[:down KeyEvent/VK_END]        [:up KeyEvent/VK_END]]
               "<pgup>"   [[:down KeyEvent/VK_PAGE_UP]    [:up KeyEvent/VK_PAGE_UP]]
               "<pgdown>" [[:down KeyEvent/VK_PAGE_DOWN]  [:up KeyEvent/VK_PAGE_DOWN]]
               "<f1>"     [[:down KeyEvent/VK_F1]         [:up KeyEvent/VK_F1]]
               "<f2>"     [[:down KeyEvent/VK_F2]         [:up KeyEvent/VK_F2]]
               "<f3>"     [[:down KeyEvent/VK_F3]         [:up KeyEvent/VK_F3]]
               "<f4>"     [[:down KeyEvent/VK_F4]         [:up KeyEvent/VK_F4]]
               "<f5>"     [[:down KeyEvent/VK_F5]         [:up KeyEvent/VK_F5]]
               "<f6>"     [[:down KeyEvent/VK_F6]         [:up KeyEvent/VK_F6]]
               "<f7>"     [[:down KeyEvent/VK_F7]         [:up KeyEvent/VK_F7]]
               "<f8>"     [[:down KeyEvent/VK_F8]         [:up KeyEvent/VK_F8]]
               "<f9>"     [[:down KeyEvent/VK_F9]         [:up KeyEvent/VK_F9]]
               "<f10>"    [[:down KeyEvent/VK_F10]        [:up KeyEvent/VK_F10]]
               "<f11>"    [[:down KeyEvent/VK_F11]        [:up KeyEvent/VK_F11]]
               "<f12>"    [[:down KeyEvent/VK_F12]        [:up KeyEvent/VK_F12]]
               "<space>"  [[:down KeyEvent/VK_SPACE]      [:up KeyEvent/VK_SPACE]]
               "<up>"     [[:down KeyEvent/VK_UP]         [:up KeyEvent/VK_UP]]
               "<down>"   [[:down KeyEvent/VK_DOWN]       [:up KeyEvent/VK_DOWN]]
               "<left>"   [[:down KeyEvent/VK_LEFT]       [:up KeyEvent/VK_LEFT]]
               "<right>"  [[:down KeyEvent/VK_RIGHT]      [:up KeyEvent/VK_RIGHT]]
               })

;;; The above can be aligned in Emacs with C-u M-x
;;; align-regex
;;; \(\s-*\)\[?\[:
;;; 1
;;; 1
;;; y


;;; Each layout entry is a dictionnary of key sequence for each symbol
(def layouts
  {:ja_JP
   (conj vk-alpha
         ;; adding symbols
         {"!"
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_1] [:up KeyEvent/VK_1] [:up KeyEvent/VK_SHIFT]]
          "\""
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_2] [:up KeyEvent/VK_2] [:up KeyEvent/VK_SHIFT]]
          "#"
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_3] [:up KeyEvent/VK_3] [:up KeyEvent/VK_SHIFT]]
          "$"
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_4] [:up KeyEvent/VK_4] [:up KeyEvent/VK_SHIFT]] 
          "%"
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_5] [:up KeyEvent/VK_5] [:up KeyEvent/VK_SHIFT]]
          "&"
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_6] [:up KeyEvent/VK_6] [:up KeyEvent/VK_SHIFT]]
          "'"
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_7] [:up KeyEvent/VK_7] [:up KeyEvent/VK_SHIFT]]
          "("
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_8] [:up KeyEvent/VK_8] [:up KeyEvent/VK_SHIFT]]
          ")"
          [[:down KeyEvent/VK_SHIFT] [:down KeyEvent/VK_9] [:up KeyEvent/VK_9] [:up KeyEvent/VK_SHIFT]]
          ;; TODO next symbols
          })})



(defn press-on-layout [layout-name key-str]
  "Adapts key presses to different layouts.
@param layout-name is something like `:ja_JP`
@param key-str is one character like `\"$\"` to be pressed in that layout
If no sequence is found, returns nil"
  (get (get layouts layout-name) key-str)
  )
