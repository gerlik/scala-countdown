import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color.{CadetBlue, DARKBLUE}
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.text.{Font, Text}

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Random

object Main extends JFXApp {

  val consonants = List('B', 'C', 'D', 'F', 'G', 'H', 'F', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Z')
  val vowels = List('A', 'E', 'I', 'O', 'U')

  var lastTime = 0
  var userPoints = 0
  var lettersRoundBoolean = true // Switch rounds number-letter
  var charachtersChosen = false // 9
  var consonantAmount = 0
  var vowelAmount = 0

  var target = 0
  var numberAmount = 0 // max 6
  val random = new Random
  var bigNumbers = ListBuffer(25, 50, 75, 100)

  val score = new Label {
    text = "Score: " + userPoints
    font = new Font("Ariel", 20)
  }
  val timer = new Label {
    text = ""
    font = new Font("Ariel", 35)
  }
  val targetNumber = new Label {
    visible = false
    font = new Font("Ariel", 35)
  }
  val characters = new Label {
    font = new Font("Ariel", 25)
  }
  val numbers = new Label {
    font = new Font("Ariel", 25)
  }
  val info = new Label {
    font = new Font("Ariel", 12)
  }

  stage = new PrimaryStage {
    title = "Countdown"
    width = 600
    height = 600
    resizable = false

    scene = new Scene {
      fill = Color.rgb(179, 229, 255)

      val countdown = new Text {
        text = "Countdown"
        font = new Font("Ariel", 40)
        style = "-fx-font-weight: bold"
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(CadetBlue, DARKBLUE))
      }

      val userAnswer = new TextField {
        margin = Insets(0, 30, 0, 0)
      }

      val lettersRoundButton = new Button {
        text = "Letters"
        style = "-fx-background-color: #68c0f2;" + "-fx-background-radius: 0em;"
        onAction = _ => {
          lettersRoundBoolean = true
          roundSwitch()
        }
      }

      val numbersRoundButton = new Button {
        text = "Numbers"
        style = "-fx-background-color: #FFF;" + "-fx-background-radius: 0em;"
        onAction = _ => {
          lettersRoundBoolean = false
          roundSwitch()
        }
      }

      val vowelButton = new Button {
        text = "Vowel"
        style = "-fx-background-color: #FFF;" + "-fx-background-radius: 0em;"
        onAction = _ => {
          returnVowels()
          characterCheck()
        }
      }

      val consonantButton = new Button {
        text = "Consonant"
        style = "-fx-background-color: #FFF;" + "-fx-background-radius: 0em;"
        onAction = _ => {
          returnConsonants()
          characterCheck()
        }
      }

      val generateBigNumberButton = new Button {
        text = "Big Number"
        style = "-fx-background-color: #FFF;" + "-fx-background-radius: 0em;"
        visible = false
        onAction = _ => {
          generateBigNumber()
        }
      }

      val generateSmallNumberButton = new Button {
        text = "Small Number"
        style = "-fx-background-color: #FFF;" + "-fx-background-radius: 0em;"
        visible = false
        onAction = _ => {
          generateSmallNumber()
        }
      }

      val startButton = new Button {
        text = "Start"
        style = "-fx-background-color: #99c47c;" + "-fx-background-radius: 0em;"
        onMousePressed = _ => {
          if (!lettersRoundBoolean && numberAmount == 6) {
            info.setText("Surround operators (+ - * /) with spaces! You have 30 seconds.")
            generateTargetNumber()
          } else {
            info.setText("Start thinking your word! You have 30 seconds.")
          }
//          startTimer(3)
          new Runnable {
            override def run(): Unit = startTimer(3)
          }

          // TODO update seconds in UI!

          //          thread({
          //            timer.setText("1")
          //            Timer(1000) {
          //              tick(30)
          //            }
          //
          //            def tick(nTime: Int) { // this method is called every 100 milliseconds
          //              //    var lastTime = Time.current
          //              val newTime = lastTime + 1
          //              Console.println("newTime: " + newTime)
          //              Console.println("lastTime: " + lastTime)
          //              if (newTime != lastTime && newTime <= nTime) {
          //                timer.setText(newTime.toString)
          //                Console.println("sek " + newTime)
          //                lastTime = newTime
          //              }
          //            }
          //          })

        }

      }

      //      def thread(task: => Unit) = new Runnable() {
      //        def run(): Unit = {
      //          //              def startTimer(time: Int): Unit = {
      ////          Console.println("seconds")
      //        }
      //      }.run()

      //      def startTimer(time: Int): Unit = {
      //        Timer(1000) {
      //          tick(time)
      //        }
      //      }
      //      def tick(nTime: Int) { // Called every 1000 milliseconds
      //        //    var lastTime = Time.current
      //        val newTime = lastTime + 1
      //        Console.println("newTime: " + newTime)
      //        Console.println("lastTime: " + lastTime)
      //        if (newTime != lastTime && newTime <= nTime) {
      //          timer.setText(newTime.toString)
      //          Console.println("sek " + newTime)
      //          lastTime = newTime
      //        }
      //      }

      val checkButton = new Button() {
        text = "Check answer"
        style = "-fx-background-color: #68c0f2;" + "-fx-background-radius: 0em;"
        onAction = _ => {
          if (lettersRoundBoolean && checkUpTheWord(userAnswer.getText) && checkGeneratedCharactersUsed()) {
            info.setText("Good word. Well done!")
            userPoints = userPoints + (userAnswer.getText().length * 1000)
            score.setText("Score: " + userPoints)
          } else if (lettersRoundBoolean && !checkUpTheWord(userAnswer.getText)) {
            info.setText("No such word.")
          } else if (!lettersRoundBoolean && calculationCheck() && checkGeneratedNumbersUsed()) { // todo uses generated numbers ONCE!
            info.setText("Good expression.")
            userPoints = userPoints + 5000
            score.setText("Score: " + userPoints)
          } else if (!lettersRoundBoolean && !calculationCheck()) {
            info.setText("Something does not add up.")
          }
        }
      }

      val resetButton = new Button {
        text = "Reset"
        style = "-fx-background-color: #ffd84d;" + "-fx-background-radius: 0em;"
        onAction = _ => {
          resetGame()
        }
      }

      // ########################################################

      def characterCheck(): Unit = {
        if (lettersRoundBoolean && characters.getText.length >= 9) {
          charachtersChosen = true
        }
      }

      def checkGeneratedCharactersUsed(): Boolean = {
        val userChars = userAnswer.getText().toUpperCase().toList.sorted
        val givenChars = characters.getText.toUpperCase().toList.sorted
        if (!givenChars.exists(a => userChars.contains(a))) {
          info.setText("You did not use given characters!")
          false
        } else {
          true
        }
      }


      def evaluate(expression: List[String]): String = expression match { // TODO operator precedence
        case l :: "*" :: r :: rest => evaluate((l.toInt * r.toInt).toString :: rest)
        case l :: "x" :: r :: rest => evaluate((l.toInt * r.toInt).toString :: rest)
        case l :: "+" :: r :: rest => evaluate((l.toInt + r.toInt).toString :: rest)
        case l :: "-" :: r :: rest => evaluate((l.toInt - r.toInt).toString :: rest)
        case l :: "/" :: r :: rest => evaluate((l.toInt / r.toInt).toString :: rest)
        case l :: ":" :: r :: rest => evaluate((l.toInt / r.toInt).toString :: rest)
//        case value :: Nil => if (value.toInt == target) {println(value); true} // TODO check if expression is correct
        case value :: Nil =>  value
//        case value => value
      }

      def calculationCheck(): Boolean = {
        if (!lettersRoundBoolean) {
          val expression = userAnswer.getText().split(" ").toList
          val result = evaluate(expression)
          println(result)
          if (result.toInt == target) { // TODO check how close is exp to target
            return true
          } else {
            return false
          }
        }
        false
      }

      def checkGeneratedNumbersUsed(): Boolean = { // TODO duplicates
        val userNumbers = userAnswer.getText().toList.sorted
        val givenNumbers = characters.getText.toList.sorted
        if (!givenNumbers.exists(a => userNumbers.contains(a))) {
          info.setText("You did not use given numbers!")
          false
        } else {
          true
        }
      }

      def returnConsonants(): Unit = { // Random consonants
        if (!charachtersChosen && consonantAmount < 6) {
          val chars = characters.getText
          characters.setText(chars + consonants(random.nextInt(consonants.length)))
          consonantAmount = consonantAmount + 1
        }
      }

      def returnVowels(): Unit = { // Random vowels
        if (!charachtersChosen && vowelAmount < 5) {
          val chars = characters.getText
          characters.setText(chars + vowels(random.nextInt(vowels.length)))
          vowelAmount = vowelAmount + 1
        }
      }

      def checkUpTheWord(word: String): Boolean = {
        val source = Source.fromFile("src/main/scala/resources/words_eng.txt")
        for (line <- source.getLines()) {
          if (line.equals(word)) {
            checkButton.setDisable(true)
            return true
          }
        }
        source.close()
        false;
      }


      def generateBigNumber(): Unit = {
        if (numberAmount < 6 && bigNumbers.nonEmpty) {
          val nmbrs = numbers.getText
          val el = random.nextInt(bigNumbers.length)
          numbers.setText(nmbrs + " " + bigNumbers(el))
          bigNumbers.remove(el)
          numberAmount = numberAmount + 1
        }
      }

      def generateSmallNumber(): Unit = {
        if (numberAmount < 6) {
          val nmbrs = numbers.getText
          numbers.setText(nmbrs + " " + random.between(1, 10))
          numberAmount = numberAmount + 1
        }
      }

      def generateTargetNumber(): Unit = {
        target = random.between(100, 999)
        targetNumber.setText(target.toString)
//        startTimer(3)
      }

      def roundSwitch(): Unit = {
        if (lettersRoundBoolean) {
          // Show letters round
          consonantButton.setVisible(true)
          vowelButton.setVisible(true)
          lettersRoundButton.setStyle("-fx-background-color: #68c0f2;" + "-fx-background-radius: 0em;")
          numbersRoundButton.setStyle("-fx-background-color: #FFF;" + "-fx-background-radius: 0em;")
          generateBigNumberButton.setVisible(false)
          generateSmallNumberButton.setVisible(false)
          targetNumber.setVisible(false)
          characters.setVisible(true)
          numbers.setVisible(false)
          userAnswer.setText("")
          timer.setText("")
          checkButton.setDisable(false)
        } else if (!lettersRoundBoolean) {
          // Show numbers round
          consonantButton.setVisible(false)
          vowelButton.setVisible(false)
          lettersRoundButton.setStyle("-fx-background-color: #FFF;" + "-fx-background-radius: 0em;")
          numbersRoundButton.setStyle("-fx-background-color: #68c0f2;" + "-fx-background-radius: 0em;")
          generateBigNumberButton.setVisible(true)
          generateSmallNumberButton.setVisible(true)
          targetNumber.setVisible(true)
          characters.setVisible(false)
          numbers.setVisible(true)
          userAnswer.setText("")
          timer.setText("")
          checkButton.setDisable(false)
        }
      }

      def resetGame(): Unit = {
        characters.setText("")
        consonantAmount = 0
        vowelAmount = 0
        charachtersChosen = false

        numbers.setText("")
        numberAmount = 0
        targetNumber.setText("")
        bigNumbers = ListBuffer(25, 50, 75, 100)

        checkButton.setDisable(false)
        timer.setText("")
        userAnswer.setText("")
        info.setText("")
      }


      def startTimer(time: Int): Unit = { // Solution for now
        for (seconds <- 1 to time) {
          Console.println(seconds)
          Thread.sleep(1000L)
        }
        timer.setText("Time is up!")
      }

      content = new VBox(
        new HBox(score) {
          padding = Insets(10)
        }, countdown, timer,
        new HBox(targetNumber) {
          alignment = Pos.Center
          padding = Insets(10)
        },
        new HBox(characters) {
          alignment = Pos.Center
          padding = Insets(10)
        },
        new HBox(lettersRoundButton, numbersRoundButton) {
          alignment = Pos.Center
          padding = Insets(10)
        },
        new HBox(characters, numbers) {
          alignment = Pos.Center
          padding = Insets(10)
        },
        new HBox(vowelButton, consonantButton, generateBigNumberButton, generateSmallNumberButton) {
          alignment = Pos.Center
          padding = Insets(10)
        },
        new HBox(userAnswer, checkButton, startButton, resetButton), info) {
        padding = Insets(50, 130, 50, 130)
        alignment = Pos.Center
      }


      //      root = new BorderPane {
      //      content = Seq(new HBox {
      //        padding = Insets(50, 120, 200, 120)
      //        val start = new Button{
      //          text = "start"
      //        }
      //          new Text {
      //            text = "Countdown"
      //            style = "-fx-font: normal bold 20pt sans-serif"
      //            fill = new LinearGradient(
      //              endX = 0,
      //              stops = Stops(CadetBlue, DARKBLUE))
      //          }
      //
      //          new Text {
      //            text = "Timer"
      //            style = "-fx-font: normal bold 20pt sans-serif"
      //            fill = new LinearGradient(
      //              endX = 0,
      //              stops = Stops(CadetBlue, DARKBLUE))
      //          }
      //      })
    }
  }
}

//object Time {
//  val form = new java.text.SimpleDateFormat("mm:ss")
//  def current = form.format(java.util.Calendar.getInstance().getTime)
//}
//
//object Timer {
//  def apply(interval: Int, repeats: Boolean = true)(op: => Unit) {
//    val timeOut = new javax.swing.AbstractAction() {
//      def actionPerformed(e: java.awt.event.ActionEvent) = op
//    }
//    val t = new javax.swing.Timer(interval, timeOut)
//    t.setRepeats(repeats)
//    t.start()
//  }
//}




