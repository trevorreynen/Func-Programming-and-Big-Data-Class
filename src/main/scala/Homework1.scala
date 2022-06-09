// Homework1.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/09/2022
// Trevor Reynen

// Homework 1

object Homework1 {
    def main(args: Array[String]): Unit = {

        // Question 1
        // Write a function, product(s: String), that computes the product of the Unicode codes
        // of all letters in a string. For example, the product of the characters in "Hello"
        // is 9415087488.
        def product(s: String) = s.foldLeft(1: BigInt)((a, b) => a * b)
        println("Question 1: \"Hello\" = " + product("Hello"))
        println("Question 1: \"Goodbye\" = " + product("Goodbye"))


        // Question 2
        // Write the Scala equivalent of the Java loop:
        // for (int i = 10; i >= 0; i--)
        //     System.out.println(i);
        println("\nQuestion 2:")
        for (i <- 10 to (0, -1)) {
            println(i)
        }


        // Question 3
        // Using BigInt, compute 2^1024.
        val q3 = BigInt(2).pow(1024)
        println("\nQuestion 3: " + q3)


        // Question 4
        // Write a function countdown(n: Int) that prints the numbers from n to 0.
        def countdown(n: Int): Unit = {
            for (i <- n to (0, -1)) {
                println(i)
            }
        }
        println("\nQuestion 4: countdown(7)")
        countdown(7)
        println("Question 4: countdown(3)")
        countdown(3)


        // Question 5
        // One way to create random file or directory names is to produce a random BigInt and
        // convert it to base 36, yielding a string such as "qsnvbevtomcj38o06kul". Poke around
        // Scaladoc to find a way of doing this in Scala.
        import scala.math._
        import BigInt.probablePrime
        import scala.util.Random
        println("\nQuestion 5: Example 1 = " + probablePrime(100, Random).toString(36))
        println("Question 5: Example 2 = " + probablePrime(100, Random).toString(36))


        // Question 6
        // The signum of a number is 1 if the number is positive, –1 if it's negative, and 0 if
        // it's zero. Write a function that computes this value.
        def signum(n: Int) = {
            if (n > 0) {
                1
            } else if (n < 0) {
                -1
            } else {
                0
            }
        }
        println("\nQuestion 6: signum(4) = " + signum(4))
        println("Question 6: signum(-32) = " + signum(-32))
        println("Question 6: signum(0) = " + signum(0))

    }
}

