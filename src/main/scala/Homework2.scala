// Homework2.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Tue. 06/14/2022
// Trevor Reynen

// ==========<  Homework 2  >==========

object Homework2 {
    def main(args: Array[String]): Unit = {

        // Question 1
        // You saw the corresponds method used with two arrays of strings. Make a call to
        // corresponds that checks whether the elements in an array of strings have the lengths
        // given in an array of integers.
        val strArr1 = Array("Hello", "Gaming")
        val lenArr1 = Array(5, 8)
        println("Question 1, Part 1: " + strArr1.corresponds(lenArr1)((str, len) => str.length == len))

        val strArr2 = Array("Functional", "Recursive")
        val lenArr2 = Array(10, 9)
        println("Question 1, Part 2: " + strArr2.corresponds(lenArr2)((str, len) => str.length == len))


        // Question 2
        // Write a function largest(fun: (Int) => Int, inputs: Seq[Int]) that yields the largest
        // value of a function within a given sequence of inputs. For example, largest(x =>
        // 10 * x - x * x, 1 to 10) should return 25. Don't use a loop or recursion.
        def largest(fun: (Int) => Int, inputs: Seq[Int]): Int = {
            fun(inputs.reduceLeft((x,y) => if (fun(x) >= fun(y)) x else y))
        }

        println("\nQuestion 2: " + largest((x => 10 * x - x * x), (1.to(10))))


        // Question 3
        // Modify the previous function to return the input at which the output is largest. For
        // example, largestAt(fun: (Int) => Int, inputs: Seq[Int]) should return 5. Don't use a
        // loop or recursion.
        def largestAt(fun: (Int) => Int, inputs: Seq[Int]) = {
            inputs.reduceLeft((x, y) => if (fun(x) >= fun(y)) x else y)
        }

        println("\nQuestion 3: " + largestAt((x => 10 * x - x * x), (1.to(10))))


        // Question 4
        // Implement an unless control abstraction that works just like if, but with an inverted
        // condition.
        // Does the first parameter need to be a call-by-name parameter? NO
        // Do you need currying? YES
        def unless(cond: Boolean)(block: => Unit): Unit = {
            if (!cond) {
                block
            }
        }

        print("\nQuestion 4, (1 == 2): ")
        unless(1 == 2) { print("Returned!\n") }

        print("Question 4, (2 == 2): ")
        unless(2 == 2) { print("Returned!\n") }
        println()

        print("Question 4, (3 == 2): ")
        unless(3 == 2) { print("Returned!\n") }
        println()


        // Question 5
        // Write a function values(fun: (Int) => Int, low: Int, high: Int) that yields a collection
        // of function inputs and outputs in a given range. For example, values(x => x * x, -5, 5)
        // should produce a collection of pairs (-5, 25), (-4, 16), (-3, 9), ..., (5, 25).
        def values(fun: (Int) => Int, low: Int, high: Int): Seq[(Int, Int)] = {
            for (i <- low to high) yield (i, fun(i))
        }

        println("\nQuestion 5: " + values((x => x * x), -5, 5))

    }
}

