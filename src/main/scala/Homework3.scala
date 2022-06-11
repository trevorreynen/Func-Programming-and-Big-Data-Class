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
        // Example Output (bc it's random):
        // Q1 Array: List(82, 59, 88, 59, 74)
        // Q1 Largest: 88
        println()


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

        println("Q2 reduceFac(3): " + reduceFac(3))
        // Output: Q2 reduceFac(3): 6

        println("Q2 reduceFac(-5): " + reduceFac(-5))
        // Output: Q2 reduceFac(-5): -120

        println("Q2 reduceFac(1): " + reduceFac(1))
        // Output: Q2 reduceFac(1): 1
        println()


        // Question 3
        // Write a program that reads words from a text file. Use a mutable map to count how often
        // each word appears. To read the words, simply use a java.util.Scanner:
        // val in = new java.util.Scanner(new java.io.File("myfile.txt")
        // while (in.hasNext()) process in.next()
        // Or look at the documentations for a Scala way. At the end, print out all words and their
        // counts.
        val words1 = new scala.collection.mutable.HashMap[String, Int]
        val in1 = new java.util.Scanner(new java.io.File("./assets/HW3text.txt"))

        def count(word: String) = {
            if (words1.contains(word)) {
                words1(word) += 1
            } else {
                words1(word) = 1
            }
        }

        while (in1.hasNext()) {
            count(in1.next())
        }

        println("Question 3:")
        println("count - word")
        words1.foreach{ case (word, count) => printf("%2d - %s\n", count, word) }
        // Output:
        // Question 3:
        // count - word
        //  1 - is
        //  1 - dream
        //  1 - you
        //  1 - have
        //  2 - how
        //  1 - today
        //  1 - dreaam
        //  1 - I
        //  1 - and
        //  1 - work
        //  1 - hw2
        //  1 - a
        //  1 - my
        //  1 - well.
        //  1 - todya
        //  2 - are
        //  2 - very
        //  1 - make
        //  1 - your
        println()


        // Question 4
        // Repeat the preceding exercise with an immutable map.


    }
}

