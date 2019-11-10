(ns electron.core)

(def electron       (js/require "electron"))
(def app            (.-app electron))
(def globalShortcut (.-globalShortcut electron))
(def browser-window (.-BrowserWindow electron))
(def crash-reporter (.-crashReporter electron))
(def ipcMain        (.-ipcMain       electron))

(def main-window (atom nil))

(defn init-browser []
  (reset! main-window (browser-window.
                        (clj->js {:width 800
                                  :height 600
                                  :webPreferences {:nodeIntegration true}})))
  ; Path is relative to the compiled js file (main.js in our case)
  (.loadURL ^js/electron.BrowserWindow @main-window (str "file://" js/__dirname "/public/index.html"))
  (.on ^js/electron.BrowserWindow @main-window "closed" #(reset! main-window nil)
  (globalShortcutReady)))

(defn callbackRegLog []
  (.log js/console "regist?"))

(defn callbackUnregLog []
  (.log js/console "unregist?"))

(defn globalShortcutReady []
  (.register globalShortcut "CommandOrControl+Z" callbackRegLog))

(defn setDiv [event arg1 arg2]
  (.log js/console "setDiv")
  (.log js/console arg1)
  (.log js/console arg2))

(.on ipcMain "setDiv" setDiv)


; CrashReporter can just be omitted
(.start crash-reporter
        (clj->js
          {:companyName "MyAwesomeCompany"
           :productName "MyAwesomeApp"
           :submitURL "https://example.com/submit-url"
           :autoSubmit false}))

(.on app "window-all-closed" #(when-not (= js/process.platform "darwin")
                                (.quit app)))
(.on app "ready" init-browser)

(.on app "will-quit" callbackUnregLog)
