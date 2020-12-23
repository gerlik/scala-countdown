import scala.io.StdIn.readInt

object Other {

}

object Main {

  // TODO upper/lowecase?
  val consonants = List('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z')
  val vowels = List('a', 'e', 'i', 'o', 'u')

  def askCharNumbers(): (Int, Int) = {
    Console.println("Insert no of consonants: ")
    val cons = readInt() // Assumes that user inserts integers
    Console.println("Insert no of vowels: ")
    val vowels = readInt()
    if (cons < 4 || vowels < 3) {
      Console.println("There has to be at least 4 consonants, you entered: " + cons)
      Console.println("There has to be at least 3 vowels, you entered: " + vowels)
      askCharNumbers()
    }
    if (cons + vowels < 9) {
      val characters = cons + vowels
      Console.println("There has to be at least 9 chatacters! You have: " + characters)
      askCharNumbers()
    }
    (cons, vowels)
  }


  def main(args: Array[String]): Unit = {
    Console.println("Welcome to Countdown! Choose 9 characters")
    val chars = askCharNumbers()
    Console.println("\nYou chose " + chars._1 + " consonants and " + chars._2 + " vowels")

  }

}

