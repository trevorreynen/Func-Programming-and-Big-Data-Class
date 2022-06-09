// Ch1.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/06/2022
// Trevor Reynen

// Created by Wei Zhong on 07/07/2020.
// Chapter 1 will introduce Scala and Functional Programming basics.
object Ch1 {
    // Main function is where you start your application.
    def main(args: Array[String]): Unit = {
        // Standard way to create Scala variable.
        // You do not need to specify type of variable.
        // Type information is inferred by compiler.
        val answer = 8 * 5 + 2

        // If you want to be more defensive, you can give the type information.
        // However, this is rarely used.
        val answer1: Int = 8 * 5 + 2

        // A value declared with 'val' is actually a constant. You can not change it's contents.
        // In other words, answer is immutable.

        // To declare a variable whose contents can vary, use 'var'.
        var counter = 0
        counter = 1 // Ok to change a var.

        // In other words, 'var' is a mutable variable. 'val' is a immutable variable.
        println("Counter: " + counter)
        // Output: Counter: 1


        // In Scala, you are encouraged to use 'val' unless you really need to change contents of a
        // variable. In other words, immutability is encouraged.


        // You can specify the type of the variable if necessary.
        val greeting: String = "Hello"



        // ========== 1.3 Commonly Used Type ==========
        // Unlike Java, all types of variables are classes.
        // In other words, Scala is purely an object-oriented language.
        val res0 = 1.toString
        println("res0: " + res0)
        // Output: res0: 1


        // Declarative programming involves stating what the task or desired outcome is.
        // Here, we want to get the numbers from 1 to 10.
        // Range is one type of collection.
        // Int 1 is implicitly converted to RichInt, which has more functionality than Int class.
        val res1 = 1.to(10)

        // Access each element of range and print it out.
        res1.foreach(println)
        // Output: 1 2 3 4 5 6 7 8 9 10    (each number on a new line)

        // Calculate intersection between two Strings.
        val res2 = "Hello".intersect("World")
        println("res2: " + res2)
        // Output: res2: lo



        // ========== 1.4 Arithmetic and Operator Overloading ==========
        // In general, (a method b) is a shorthand for (a.method(b))
        // For example, 1.to(10) is same as 1 to 10 (infix notation)

        val res3 = 1.to(10)

        // Equivalent syntax.
        val res4 = 1 to 10 // Infix notation makes your code more readable.


        // a + b is shorthand for a.+(b).
        // + in Scala is the method name.
        // In other words, Scala has no silly prejudice against non-alphanumeric
        // characters in method names.

        val res5 = "hello".intersect("World")

        val res6 = "hello" intersect "World" // Infix notation.

        var counter2 = 0
        counter2 += 1 // Increase counter - Scala has no ++ operator.


        // BigInt is used to store very large integer.
        // You can use the usual mathematical operator with BigInt objects.

        val x: BigInt = 1234567890
        val res7 = x * x * x
        println("res7: " + res7)
        // Output: res7: 1881676371789154860897069000


        // This is much better than Java, where you would have to call x.multiply(x).multiply(x)
        // because * is not a method in Java. This is very verbose.



        // ========== 1.5 Calling Functions and Methods ==========
        // In Scala, _ character is a wildcard like * in Java.
        // We are importing all classes in the scala.math package.
        import scala.math._

        val res8 = sqrt(2)
        println("res8: " + res8)
        // Output: res8: 1.4142135623730951

        val res9 = pow(2, 4)
        println("res9: " + res9)
        // Output: res9: 16.0

        // Scala method without parameters often do not use parenthesis.
        val res10 = "hello".distinct
        println("res10: " + res10)
        // Output: res10: helo


        // In Scala, semicolon is optional at the end of a statement
        // since we want to save some typing.


        // A class in Scala has companion object whose methods act just like static
        // methods do in Java.
        // BigInt companion object to the BigInt class has a method called probablePrime.

        // Produce random prime with 100 bit long.
        val res11 = BigInt.probablePrime(100, scala.util.Random)
        println("res11: " + res11)
        // Output: res11: 673508361218937640397377745251

        // It yields a new BigInt object, without having to use new.
        val res12 = BigInt("1234567890")

        // This is shortcut for:
        val res13 = BigInt.apply("1234567890")

        // Why without new is good?
        val res14 = BigInt("1234567890") * BigInt("11235811321")
        println("res14: " + res14)
        // Output: res14: 13871371875005082690

        // Using the apply method of a companion object is common Scala idiom
        // for constructing objects.

        // Thanks to apply method of Array companion object.
        val res15 = Array(1, 4, 9, 16)
        // mkString will convert collection to String.
        println("res15: " + res15.mkString(", "))
        // Output: res15: 1, 4, 9, 16

        // Recap
        // In this section, we introduce some of the basic concepts of Scala programming.

    }
}

