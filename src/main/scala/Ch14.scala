// Ch14.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Tue. 06/14/2022
// Trevor Reynen

// Created by Wei Zhong on 06/14/2018.

// Imports.
import scala.collection.mutable.ArrayBuffer

// Chapter 14
// This chapter introduces pattern matching.
// Functional programming offers very powerful pattern matching mechanism.

object Ch14 {
    def main(args: Array[String]): Unit = {

        // ========== 14.1 Better Switch ==========
        // C-style switch statement in Scala.

        for (ch <- "+-!") {
            var sign = 0

            // ch is target.
            // '+' is the pattern.
            // Pattern matching is an expression, which produces a value.
            // Functional languages prefer expressions over statements since expressions have no
            // side effect.
            sign = ch match {
                case '+' => 1
                case '-' => -1
                // _ represents default pattern.
                case _ => 0
            }

            print(sign + " ")
        }


        // You can use the match statement with any types, not just numbers or characters.
        import java.awt._

        val color = SystemColor.textText

        // Pattern matching for color.
        val res0 = color match {
            // In this example, your pattern is constant.
            case Color.RED => "Text is red."
            case Color.BLACK => "Text is black."
            case _ => "Not red or black."
        }

        println("\n\nres0: " + res0 + "\n")



        // ========== 14.3 Variables in Patterns ==========
        // If the case keyboard is followed by a variable name, then the match expressions is
        // assigned to that variable.

        val str = "+-3!"

        //for (i <- 0 until str.length) {
        for (i <- str.indices) {
            var sign = 0
            var digit = 0

            str(i) match {
                case '+' => sign = 1
                case '-' => sign = -1
                //case ch => digit = 10
                case ch if Character.isDigit(ch) => digit = Character.digit(ch, 10)
                case _ =>
            }

            println(str(i) + " " + sign + " " + digit)
        }

        println()

        // Output w/ Zhong's Code   | Output w/ Book's Code
        // + 1 0                    | + 1 0
        // - -1 0                   | - -1 0
        // 3 0 10                   | 3 0 3
        // ! 0 10                   | ! 0 0



        // ========== 14.4 Type Patterns ==========
        // You can match on the type of an expression.

        // BigInt(42) produces the instance of class.
        // BigInt is the object (class with single instance).

        for (obj <- Array(42, "42", BigInt(42), BigInt, 42.0)) {
            val result = obj match {
                // In the first pattern, the target is bound to x as Int.
                case x: Int => x
                case s: String => Integer.parseInt(s)
                // _ means I don't care about name and value of this variable.
                case _: BigInt => Int.MaxValue
                case BigInt => -1
                case _ => 0
            }

            println(result)
        }

        println()


        // This example demonstrates pattern matching is more powerful and flexible than simple
        // isInstanceOf (check type) and asInstanceOf mechanism (type casting).

        // When you match against a type, you must supply a variable's name.
        // Otherwise, you match against the object (single instance of the class).

        // In Scala, this pattern matching form is preferred over using isInstanceOf operator.
        // Always arrange your pattern from specific to general.

        for (obj <- Array(Map("Fred" -> 42), Map(42 -> "Fred"), Array(42), (Array("Fred")))) {
            val result = obj match {

                case m: Map[_, _] => "It's a Map!"
                case a: Array[Int] => "It's an Array[Int]!"
                case b: Array[_] => "It's an Array of something other than Int..."
            }

            println(result)
        }

        println()



        // ========== 14.5 Matching Arrays, Lists, and Tuples ==========
        // To match an array against it's contents, use Array expression in pattern.

        for (arr <- Array(Array(0), Array(1, 0), Array(0, 1, 0), Array(1, 1, 0))) {
            val result = arr match {
                case Array(0, _*) => "0.." // Match array starts with 0.
                case Array(0) => "0" // Match array contains only 0.
                case Array(x, y) => x + " " + y // Match array with two elements.
                // Third pattern matches any array starting with zero.
                case _ => "something else"
            }

            println(result)
        }

        println()

        // You can match lists in the same way, with list expression. Array is mutable data
        // structure. Here, List is immutable.
        import scala.collection.immutable.List

        for (lst <- Array(List(0), List(1, 0), List(2, 0, 0), List(1, 0, 0))) {
            val result = lst match {
                // List with only zero.
                case 0 :: Nil => "0"
                // 2 element lists.
                case x :: y :: Nil => x + " " + y
                // x is the head.
                case x :: tail => x + " ..."
                case _ => "something else" // Default case.
            }

            println(result)
        }

        println()

        // Matching tuples. Tuples are immutable. Tuples are basically several items grouped
        // together. Tuples are denoted by ( ).
        for (pair <- Array((0, 1), (1, 0), (1, 1))) {
            // Matching is the expression, which produces a value.
            val result = pair match {
                case (0, _) => "0." // First item is zero.
                case (y, 0) => y + " 0" // Second item is zero.
                case _ => "Neither is 0"
            }

            println(result + "\n")
        }


        // Pattern matching with regular expression.
        // A bunch of digits, one white space, and a bunch of letters.

        val pattern = "([0-9]+) ([a-z]+)".r

        val res1 = "99 bottles" match {
            case pattern(num, item) => (num.toInt, item)
        }

        println("res1: " + res1 + "\n")



        // ========== 14.7 Patterns in Variable Declaration ==========
        // You can use these patterns inside variable declaration.

        val (x, y) = (1, 2)
        println("x: " + x)
        println("y: " + y + "\n")

        // Tuple is very useful for functions that return a pair.
        val (q, r) = BigInt(10) /% 3
        println("q: " + q)
        println("r: " + r + "\n")


        // Same syntax works for any patterns with variable names.
        val arr1 = Array(1, 7, 2, 9, 10)
        // _* represents the rest of array.
        val Array(first, second, _*) = arr1
        println("first: " + first)
        println("second: " + second + "\n")



        // ========== 14.8 Patterns in For Expression ==========
        // I want to convert Java properties objects as Scala Map.
        import scala.collection.JavaConversions.propertiesAsScalaMap

        // k is key and v is value for each item in the map.
        for ((k, v) <- System.getProperties()) {
            //println(k + " -> " + v) // Commented out since it fills debug menu 10 fold.
        }

        //println() // Commented out since it's related to println above..

        // Print all keys with empty value, skipping over all others.
        for ((k, "") <- System.getProperties()) {
            //println(k) // Commented out since it fills debug menu 10 fold.
        }

        //println() // Commented out since it's related to println above..



        // ========== 14.9 Case Classes ==========
        // Case class is special kind of classes that are optimized for use in pattern matching.
        // Why we use case class? Case class is useful for functional decomposition with pattern
        // matching. Given you an object of Amount (superclass), Amount has 3 subclasses: Dollar,
        // Currency, and Nothing.
        // You need to answer 2 questions:
        // 1) Which subclass was used (what kind of constructor we used to build subclass? Whether
        // it's Dollar, Currency, or Nothing.
        // 2) What were the arguments of constructors or what is value of it's instance variable.
        // This situation is so common that many functional language automate it. Technical term
        // for this situation is called pattern matching.

        // Abstract class does not contain anything. It's ideally case for superclass.
        abstract class Amount

        // Dollar has one immutable field called value.
        case class Dollar(value: Double) extends Amount

        // Currency has two immutable fields: value, unit.
        case class Currency(value: Double, unit: String) extends Amount

        case object Nothing extends Amount

        for (amt <- Array(Dollar(1000.0), Currency(1000.0, "EUR"), Nothing)) {
            val result = amt match {
                // v is value to build the object.
                case Dollar(v) => "$" + v
                case Currency(_, u) => "Oh no, I got " + u
                case Nothing => ""
            }

            println(amt + ": " + result)
        }

        // Example of immutable list operation.

        // How to reverse the list?
        // T is type parameter just like Java's generic type.
        // Type parameter makes functional code more flexible.
        def reverse[T](xs: List[T]): List[T] =
            xs match {
                case Nil => xs // Empty list.
                // y is head and ys is tail.
                case y :: ys => reverse(ys) ++ List(y)
            }

        val res3 = List(1, 2, 3)
        val res4 = reverse(res3)
        println("res4: " + res4)

        // Remove n'th element of list.
        def removeAt[T](xs: List[T], n: Int): List[T] = {
            // We take first n and drop first n+1 element.
            (xs take n) ++ (xs drop n + 1)
        }

        val res5a = List(4, 10, 35, 21, 11, 34, 45)

        val res5b = res5a.take(2)
        println("res5b: " + res5b)

        val res5c = res5a.drop(3)
        println("res5c: " + res5c)

        val res5d = res5b ++ res5c
        println("res5d: " + res5d)

        val res5e = removeAt(res5a, 2)
        println("res5e: " + res5e)

        // Higher order list function.

        // Multiply each element of list by same factor.
        def scaleList(xs: List[Double], factor: Double): List[Double] = {
            xs map (x => x * factor)
        }

        // You want to square each element of list.
        def squareList(xs: List[Int]): List[Int] = {
            xs map (x => x * x)
        }

        // Select all elements of list which are positive.
        // Each tool is specialized for each task.
        def posElements(xs: List[Int]): List[Int] = {
            xs filter (x => x > 0)
        }

        val nums = List(7, 3, -2, -4, 5, 7, 1)

        // Same as (xs filter p, xs filterNot p)
        // Separate list into positive list and negative list.
        val res6 = nums partition(x => x > 0)
        println("res6: " + res6)

        // Same as (xs takeWhile p, xs dropWhile p)
        // All my prefix has to satisfy this condition.
        val res7 = nums span (x => x > 0)
        println("res7: " + res7)

        // Write a functional that packs consecutive duplicates of list elements into sub-lists.
        def pack[T](xs: List[T]): List[List[T]] = {
            xs match {
                case Nil => Nil
                case x :: xs1 =>
                    // First is the consecutive letters at front.
                    val (first, rest) = xs span (y => y == x)
                    // Recursive call to find duplicates in the rest of list.
                    first :: pack(rest)
            }
        }

        val res8 = List("a", "a", "a", "b", "c", "c", "a")
        val res9 = pack(res8)
        println("res9: " + res9)

        // Using pack, write a function encode that produces the run-length encoding of list.
        // Run-length encoding is used to compression of image and other files.
        // I get the letter and it's length.
        def encode[T](xs: List[T]): List[(T, Int)] = {
            pack(xs) map (ys => (ys.head, ys.length))
        }

        // Array and Strings
        // Array and String can implicitly be converted to sequence where needed. As a result,
        // Arrays and Strings can support the same operations as Seq. They cannot be subclasses of
        // Seq because they come from Java World.

        val xs2 = Array(1, 2, 3, 44)

        // Double each element of array.
        val res13 = xs2 map (x => x * 2)

        val s2 = "Hello World"

        // Give us all upper case characters in String.
        val res14 = s2 filter (c => c.isUpper)
        println("res14: " + res14)

        val res15 = s2 exists(c => c.isUpper)
        println("res15: " + res15)

        // Whether all letters are upper case.
        val res16a = s2 forall (c => c.isUpper)
        println("res16a: " + res16a)

        // Zip list and string.
        val pairs1 = List(1, 2, 3) zip s2
        println("pairs1: " + pairs1)

        val res16b = List("Hello", "World")
        val pairs2 = List(1, 2, 3) zip res16b
        println("pairs2: " + pairs2)

        val res17 = pairs2.unzip
        println("res17: " + res17)

        // xs flatMap f   f is a collection-valued function.
        // Applied f to each element of xs and concatenates the results.
        val res18 = s2 flatMap (c => List('.', c))
        println("res18: " + res18)

        // Compute scaler product of vector.
        def scalerProduct(xs: Vector[Double], ys: Vector[Double]): Double = {
            // After zip, each element is tuple.
            (xs zip ys).map(xy => xy._1 * xy._2).sum
        }

        // Better notation.
        def scalerProduct_2(xs: Vector[Double], ys: Vector[Double]): Double = {
            (xs zip ys).map { case (x, y) => x * y }.sum
        }

        // List all combination of numbers x and y where x is drawn from 1 .. M and y is drawn
        // from 1 .. N

        val M = 8
        val N = 2

        val res19 =
            for {
                x <- 1 to M
                y <- 1 to N // Second generator moves faster than first generator.
            } yield (x, y)

        println("res19: " + res19)

        // A number n is prime if the only divisors of n are 1 and n itself.
        // Functional programing can faithfully translate your math idea into code literally.
        def isPrime(n: Int): Boolean = {
            // Check the number from 2 to n-1 one by one to see if they are divisible by d.
            (2 until n) forall (d => n % d != 0)
        }

    }
}

