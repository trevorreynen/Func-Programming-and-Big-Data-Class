// Homework3.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/16/2022
// Trevor Reynen

// ==========<  Homework 3  >==========

object Homework3 {
    def main(args: Array[String]): Unit = {

        // Question 1
        // How do you get the largest element of an array with reduceLeft? In order to test the
        // code, you need first randomly generated array using functional programming and print out
        // the content of the array.
        val randArr = Seq.fill(5)(scala.util.Random.nextInt(100))

        println("Q1 Array: " + randArr)
        val largest = randArr.reduceLeft((x, y) => if (x > y) x else y)
        println("Q1 Largest: " + largest)


        // Question 2
        // Write the factorial function using to and reduceLeft, without a loop or recursion. You
        // need to add a special case when n < 1.
        def reduceFac(value: Int) = {
            if (value > 0) {
                (1 to value).reduceLeft(_ * _)
            } else {
                -1 * (1 to math.abs(value)).reduceLeft(_ * _)
            }
        }

        println("\nQ2 reduceFac(3): " + reduceFac(3))
        println("Q2 reduceFac(-5): " + reduceFac(-5))
        println("Q2 reduceFac(1): " + reduceFac(1))


        // Question 3
        // Write a program that reads words from a text file. Use a mutable map to count how often
        // each word appears. To read the words, simply use a java.util.Scanner:
        // val in = new java.util.Scanner(new java.io.File("myfile.txt")
        // while (in.hasNext()) process in.next()
        // Or look at the documentations for a Scala way. At the end, print out all words and their
        // counts.
        def count(process: String => Unit): Unit = {
            val in = new java.util.Scanner(new java.io.File("./assets/HW3text.txt"))

            try {
                while (in.hasNext) {
                    process(in.next())
                }
            } finally {
                in.close()
            }
        }

        def countWordsMutableMap(): scala.collection.mutable.HashMap[String, Int] = {
            val words = new scala.collection.mutable.HashMap[String, Int]
            count(w => words(w) = words.getOrElse(w, 0) + 1)

            println("\nQuestion 3:")
            words.foreach{ case (word, count) => printf("%2d - %s\n", count, word) }
            println()
            words
        }

        countWordsMutableMap()


        // Question 4
        // Repeat the preceding exercise with an immutable map.
        def countWordsImmutableMap(): Map[String, Int] = {
            var words = Map[String, Int]()
            count(w => words += w -> (words.getOrElse(w, 0) + 1))

            println("\nQuestion 4:")
            words.foreach{ case (word, count) => printf("%2d - %s\n", count, word) }

            words
        }

        countWordsImmutableMap()

    }
}

