import scala.io.StdIn.readInt
import scala.util.Random

object Other {

}

object Main {

  // TODO upper/lowecase?
  val consonants = List('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z')
  val vowels = List('a', 'e', 'i', 'o', 'u')
  val random = new Random

  def returnConsonants(arv: Int): Unit = { // Random consonants and vowels

  }

  def returnVowels(arv: Int): Unit = {

  }

  def askCharNumbers(): (Int, Int) = {
    Console.println("Enter no of consonants: ")
    val consonant = readInt() // Assumes that user inserts integers
    Console.println("Enter no of vowels: ")
    val vowel = readInt()
    if (consonant < 4 || vowel < 3) {
      Console.println("There needs to be at least 4 consonants, you entered: " + consonant)
      Console.println("There needs to be at least 3 vowels, you entered: " + vowel)
      askCharNumbers()
    }
    if (consonant + vowel < 9) {
      val chars = consonant + vowel
      Console.println("There has to be at least 9 characters! You have: " + chars)
      askCharNumbers()
    }
    (consonant, vowel)
  }

  def showChars(characters: (Int, Int)): Unit = {
    returnConsonants(characters._1)
    returnVowels(characters._2)
  }

  def main(args: Array[String]): Unit = {
    Console.println("Welcome to Countdown! Choose your 9 characters: ")
    val chars = askCharNumbers()
    Console.println("\nYou chose " + chars._1 + " consonants and " + chars._2 + " vowels")
    showChars(chars)

  }

}

