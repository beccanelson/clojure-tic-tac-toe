(ns tic-tac-toe.ui-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.ui :as ui]
            [tic-tac-toe.player :as player]))

(def initial-board [0 1 2 3 4 5 6 7 8])

(describe "UI"
  (around [it]
    (with-out-str (it)))

  (context "#display-board"
    (it "prints an empty board"
      (should-be-a String (with-out-str (ui/display-board initial-board)))))

  (context "is-an-available-spot?"
    (it "returns truthy if a number string is in the set of available spots"
      (should (ui/is-an-available-spot? initial-board "0"))
      (should (ui/is-an-available-spot? [0] "0"))
      (should-not (ui/is-an-available-spot? [0 4] "8"))))

  (context "is-not-a-number?"
    (it "returns falsey if an input string is a number"
      (should-not (ui/is-not-a-number? "4"))
      (should-not (ui/is-not-a-number? "0")))

    (it "returns truthy if an input string is a letter or symbol"
      (should (ui/is-not-a-number? "#"))
      (should (ui/is-not-a-number? "A"))))

  (context "is-a-valid-marker?"
    (it "returns false if a marker is a number"
      (should-not (ui/is-a-valid-marker? "4")))

    (it "returns false if a marker is not a single character"
      (should-not (ui/is-a-valid-marker? "123")))

    (it "returns true if a marker is a letter or a symbol"
      (should (ui/is-a-valid-marker? "@"))
      (should (ui/is-a-valid-marker? "A"))))

  (context "is-a-valid-computer-marker?"
    (it "returns false if a marker is the same as a human marker"
      (should-not (ui/is-a-valid-computer-marker? (player/human "$") "$"))))

  (context "#get-spot"
    (around [it]
      (with-out-str (it)))

    (it "returns user input as an integer"
      (should= 4
        (with-in-str "4"
          (ui/get-spot [0 1 2 3 4 5 6 7 8]))))

    (it "prints an error message if invalid input is given."
      (should-be-a String
        (with-in-str "i\n1"
          (with-out-str (ui/get-spot [0 1 2 3 4 5 6 7 8])))))

    (it "prints an error message if a spot is not available on the board"
      (should-contain "Sorry, that's not an available spot. Try again!"
        (with-in-str "0\n1"
          (with-out-str (ui/get-spot ["O" 1 "X" 3 4 5 6 7 8]))))))

  (context "#get-user-input"
    (it "returns user input if it meets a given condition"
      (should= "user input"
        (with-in-str "user input"
          (ui/get-user-input string? "error"))))

    (it "prints an error message if the input doesn't meet a condition"
      (should-contain "error"
        (with-in-str "2\n1"
          (with-out-str (ui/get-user-input #(= "1" %) "error")))))

    (it "takes an optional list of available spots"
      (should= "4"
        (with-in-str "4"
          (ui/get-user-input ui/is-an-available-spot? "error" [4])))))

  (context "#display-welcome-message"
    (it "prints a welcome message to the console"
      (should-be-a String (with-out-str (ui/display-welcome-message)))))

  (context "#display-winner"
    (it "displays a different message based on the player type"
      (should-not=
        (with-out-str
          (ui/display-winner (player/computer "O")))
        (with-out-str
          (ui/display-winner (player/human "X"))))))

  (context "#get-marker-choice"
    (around [it]
      (with-out-str (it)))

    (it "prints a separate message for a human and a computer"
      (should-not=
        (with-out-str
          (with-in-str "X" (ui/get-marker-choice (player/human nil))))
        (with-out-str
          (with-in-str "O" (ui/get-marker-choice (player/computer nil))))))

    (it "returns a marker that is a single character"
      (should= "$"
        (with-in-str "$"
          (ui/get-marker-choice (player/human nil)))))

    (it "prints an error if a marker is less than or greater than one character"
      (should-contain "single character"
        (with-out-str
          (with-in-str "\nX" (ui/get-marker-choice (player/human nil)))))
      (should-contain "single character"
        (with-out-str
          (with-in-str "123\nX" (ui/get-marker-choice (player/human nil))))))

    (it "prints an error if a marker is a number"
      (should-contain "number"
        (with-out-str
          (with-in-str "3\nA" (ui/get-marker-choice (player/human nil))))))

    (it "prints an error message if a computer marker is the same as a human marker"
      (should-contain "Markers cannot be the same"
        (with-out-str
          (with-in-str "#\n@" (ui/get-marker-choice (player/computer nil) (player/human "#")))))))

  (context "#y-or-n"
    (it "returns truty if y or n"
      (should (ui/y-or-n "y"))
      (should (ui/y-or-n "n")))

    (it "returns falsy if anything else"
      (should-not (ui/y-or-n "cat"))))

  (context "#player-would-like-to-continue?"
    (around [it]
      (with-out-str (it)))

    (it "outputs to the console"
      (should-be-a String
        (with-out-str
          (with-in-str "n"
            (ui/player-would-like-to-continue?)))))

    (it "returns true if input is 'y'"
      (should= true
        (with-in-str "y"
          (ui/player-would-like-to-continue?))))

    (it "returns false if input is 'n'"
      (should= false
        (with-in-str "n"
          (ui/player-would-like-to-continue?))))

    (it "prints an error if input is anything else"
      (should-contain "please choose y or n"
        (with-out-str
          (with-in-str "cat\nn"
            (ui/player-would-like-to-continue?))))))

  (context "#goodbye"
    (it "prints goodbye"
      (should= "Goodbye!\n"
        (with-out-str
          (ui/goodbye))))))
