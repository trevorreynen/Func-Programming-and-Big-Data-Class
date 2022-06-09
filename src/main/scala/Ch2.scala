// Ch2.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Tue. 06/07/2022
// Trevor Reynen

// Created by Wei Zhong on 06/28/2020.


// In this chapter, we will discuss control structure and functions.

// Expressions vs. Statements
// An expression produces a value.
// A statement carries out an action. Action can usually have side effects (mutation).

// In Scala, almost all constructs have values.
// This is major features of functional programming.
// This feature can make a program more concise and easier to read.
object Ch2 {
    def main(args: Array[String]): Unit = {
        // ========== 2.1 Conditional Expressions ==========
        // In Scala, if/else produces a value.

        val x = 0

        // Type of s is inferred. So you know it's a type during compiling time.
        // Scala is static typing like Java. Developers do not write
        // the type of variable out. Your code is more concise.
        val s = if (x > 0) 1.0 else -1.0
        println("s: " + s)
        // Output: s: -1.0


        // Type of a mixed expression is the common supertype of both branches.
        // Any is equivalent to Object. Common supertype of String and Int is Any.

        val res1 = if (x > 0) "positive" else -1



        // ========== 2.2 Statements ==========
        var n = 10  // n is mutable.
        var r = 1
        if (n > 0) {
            // This is called a statement. Statement usually has a side effect.
            // Side effect means that you mutate r.
            r = r * n
            n -= 1
        }



        // ========== 2.3 Block Expressions and Assignments ==========
        // In Scala, a block {} produces a value.
        // The value of the block is the value of the last expression.
        // In Scala, return expression is rarely used.

        val x0 = 1.0
        val y0 = 1.0
        val x1 = 4.0
        val y1 = 5.0

        import scala.math._

        val distance = {
            val dx = x1 - x0
            val dy = y1 - y0
            // The last expression is value of block.
            sqrt(dx * dx + dy * dy)
        }

        println("distance: " + distance)
        // Output: distance: 5.0



        // ========== 2.5 Loops ==========
        // r and n are mutable variables, which is declared previously.
        r = 1
        n = 10

        while (n > 0) {
            // This statement has side effects: you are mutating r.
            r = r * n
            // n is counter. Mutating n.
            n -= 1
        }

        println("r: " + r + " n: " + n)
        // Output: r: 3628800 n: 0


        // For any language features, you need to evaluate it's advantages and disadvantages.
        // As a developer, you need to have the critical mindset.

        // For Expressions, the construct:
        // for (i <- expr)
        // makes i traverse all values of expr on the right.

        r = 1
        n = 10
        // The type of local variable i is the element type of collection
        // Value of i can be 1, 2, ..., 9, 10
        for (i <- 1 to n)
            r = r * i

        println("r: " + r)
        // Output: r: 3628800


        // Other example of for expression
        val s1 = "Hello"
        var sum1 = 0

        // Until returns a range that does not include the upper-bound.
        for (i <- 0 until s1.length)
            sum1 += s1(i)

        println("sum1: " + sum1)
        // Output: sum1: 500


        // For expression is the feature of functional programming. i is not mutated.
        // For every iteration, i is recreated and reinitialized.

        // In this example, there is no need to use indexes.
        // You can directly loop over character.

        sum1 = 0
        for (ch <- "Hello") sum1 += ch



        // ========== 2.6 Advanced For Loops and For Comprehensions ==========
        // For comprehensions are an important feature of functional language.

        // You can have multiple generators of the form variable <- expression.
        // You can separate them by semicolons. There is no mutation in this loop.
        // i and j are never mutated. For each iteration, i and j are recreated.

        // Inner loop is j <- 1 to 4.
        // Inner loop moves faster than outer loop.
        for (i <- 1 to 3; j <- 1 to 4)
            print((10 * i + j) + " ")
            // Output: 11 12 13 14 21 22 23 24 31 32 33 34
        println()

        // You can have any number of definition, introducing variable that can be used
        // inside the loop. Under the hood, this code is translated into functional code
        // using map and flatmap function.
        for (i <- 1 to 3; from = 4 - i; j <- from to 3)
            print((10 * i + j) + " ")
            // Output: 13 22 23 31 32 33
        println()

        // Alternative syntax: enclose generators, guard, and definition inside curly braces.
        // Semicolon will be dropped.
        for {
            i <- 1 to 3
            from = 4 - i
            j <- from to 3
        } print((10 * i + j) + " ")
          // Output: 13 22 23 31 32 33
        println()

        // When the body of the for loop starts with yield, the loop constructs a collection
        // of values, one for each iteration.

        // res18 is IndexedSequence. Usually we use vector to represent IndexedSequence.
        // Vector is an immutable collection.

        val res18 = for (i <- 1 to 10) yield 1 % 3
        println("res18: " + res18)
        // Output: res18: Vector(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)


        // Generated collection is compatible with the first generator.
        val res19 = for (c <- "Hello"; i <- 0 to 1) yield (c + i).toChar
        println("res19: " + res19)
        // Output: res19: HIeflmlmop

        val res20 = for (i <- 0 to 1; c <- "Hello") yield (c + i).toChar
        println("res20: " + res20)
        // Output: res20: Vector(H, e, l, l, o, I, f, m, m, p)



        // ========== 2.7 Functions ==========
        // public static double abs (equivalent java syntax)

        // Meaning of return in Scala is different from Java.
        // Return in Scala actually will interrupt the thread.
        def abs(x: Double): Double = if (x >= 0) x else -x

        println(abs(3))
        // Output: 3.0
        println(abs(-3))
        // Output: 3.0

        // Imperative Style
        def fac(n: Int): Int = {
            var r = 1
            for (i <- 1 to n) r = r * i
            r
        }

        println(fac(4))
        // Output: 24


        // For factorial, the functional style needs to use recursion.
        // Recursive function has no mutation. However, it is costly.

        // The beauty of the recursive function is that you get rid of mutations
        // inside the function.

        def recursiveFac(n: Int): Int = {
            if (n <= 0) 1 else n * recursiveFac(n-1)
        }

        println(recursiveFac(4))
        // Output: 24



        // ========== 2.8 Default and Named Arguments ==========
        // Left and right has default values.
        def decorate(str: String, left: String="[", right: String="]"): String = {
            left + str + right
        }


        println(decorate("Hello"))
        // Output: [Hello]

        // right="]<<<" is called named argument. Named argument can make sure your code is more
        // readable if you have a large number of parameters.
        println(decorate(str="Hello", right="]<<<"))
        // Output: [Hello]<<<



        // ========== 2.9 Variable Arguments ==========
        // Sometimes it's convenient to implement a function that takes a variable
        // number of arguments. Args is argument sequence. Result is the local variable.
        // If mutation only happens within the local function, that mutation will not affect
        // other parts of the program. This type of mutation is allowed for semi-functional style.
        def sum(args: Int*): Int = {
            var result = 0
            for (arg <- args) result += arg
            result
        }

        val result1 = sum(1, 4, 9, 16, 25)
        println("result1: " + result1)
        // Output: result1: 55

        val result2 = sum(1, 4)
        println("result2: " + result2)
        // Output: result2: 5

        val result3 = sum()
        println("result3: " + result3)
        // Output: result3: 0

        // Tell the compiler that you want the parameter to be considered as argument sequence.
        val result4 = sum(1 to 5: _*)
        println("result4: " + result4)
        // Output: result4: 15



        // ========== 2.10 Procedure ==========
        // Procedure is the function with nothing to return.
        // Unit means that this function does not return anything.
        // Box will print the border for String.

        def box(s: String): Unit = {
            val border = "-" * s.length + "--\n"
            println("\n" + border + "|" + s + "|\n" + border)
        }

        box("Fred")
        // Output:
        // ------
        // |Fred|
        // ------
        box("USC Upstate")
        // Output:
        // -------------
        // |USC Upstate|
        // -------------



        // ========== 2.12 Exception ==========
        // Exception happens when you have some illegal operations.
        // Exception expression has the special data type called Nothing.

        def root(x: Double): Double = {
            if (x >= 0)
                sqrt(x)
            else
                throw new IllegalArgumentException("x should not be negative")
        }

        try {
            println(root(4))
            // Output: 2.0
            println(root(-4))
            // Output: java.lang.IllegalArgumentException: x should not be negative.
        } catch {
            case error: Exception => println(error)
        }

    }
}

