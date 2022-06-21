// Ch12.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/09/2022
// Trevor Reynen

// Created by Wei Zhong on 06/06/2018.

// This chapter will introduce major concepts of functional programming.
// In functional programming, functions are first-class citizens that can be passed around and
// manipulated just like any other data type.

// In Java before 1.8: Data is first class citizen and function is second class citizen.
object Ch12 {
    def main(args: Array[String]): Unit = {

        // ========== 12.1 Functions as Values ==========
        // In Scala, a function is a first-class citizen just like a number.
        // You can store a function in a variable.
        import scala.math._
        val num = 3.14

        // _ behind the ceil function indicates that you really meant the function and you
        // did not just forget to supply the arguments.

        // fun: Double => Double
        // fun is a variable containing a function, not a fixed function.
        val fun = ceil _

        // What can you do with a function?
        // Call it

        val res0 = fun(num)
        println("res0: " + res0)
        // Output: res0: 4.0

        // In the second way, you can give it to a function as argument.

        // The map function accepts a function, applies this function to each value in array
        // and returns an array with the function value.

        // Map is similar to for loop in Java.
        // Declarative style means WHAT TO DO not how to do it.
        // Map each element to the new element based on fun (declarative style)
        val res1 = Array(3.14, 1.42, 2.0).map(fun)
        println("res1: " + res1.mkString(", "))
        // Output: res1: 4.0, 2.0, 2.0

        // Infix notation.
        val res2 = Array(3.14, 1.42, 2.0) map fun
        println("res2: " + res2.mkString(", "))
        // Output: res2: 4.0, 2.0, 2.0



        // ========== 12.2 Anonymous Functions ==========
        // In Scala, you don't have to give a name to each function, just like you don't have to
        // give a name to each number.

        val triple = (x: Double) => 3 * x

        // That is just as if you have used a def:
        // def triple(x: Double): Double = 3 * x

        val res3 = Array(3.14, 1.42, 2.0).map(triple)
        println("res3: " + res3.mkString(", "))
        // Output: res3: 9.42, 4.26, 6.0

        // But you don't have to name a function since this function may be used only once.
        // You can pass anonymous functions to another function.

        // We tell the map method: Multiply each element by 3.
        val res4 = Array(3.14, 1.42, 2.0).map((x: Double) => 3 * x)

        // How to use infix notation.
        val res5 = Array(3.14, 1.42, 2.0) map { (x: Double) => 3 * x }



        // ========== 12.3 Functions Take Another Function as Parameter ==========
        // Evaluate any function f whose input is 0.25.
        // f is variable holding a function.

        // Type of this function.
        // (parameterType) => resultType
        // ((Double) => Double) => Double
        def valueAtOneQuarter(f: Double => Double): Double = f(0.25)

        val res6 = valueAtOneQuarter(ceil _)
        println("res6: " + res6)
        // Output: res6: 1.0

        val res7 = valueAtOneQuarter(sqrt _)
        println("res7: " + res7)
        // Output: res7: 0.5

        // Since valueAtOneQuarter is a function that receives a function, it's
        // called a higher-order function.

        // Functional programming is powerful because you can translate mathematical formulas into
        // code very easily.

        // A higher-order function can produce a function.
        // The power of mulBy is that it can deliver functions that multiple by any amount.
        // What is type of this function?
        // Double => (Double => Double)
        def mulBy(factor: Double): Double => Double = {
            (x: Double) => factor * x // Anonymous functions.
        }

        // Scala allows local functions.
        def mulBy2(factor: Double): Double => Double = {
            // Local function.
            def myfun(x: Double): Double = {
                factor * x
            }

            myfun
        }

        def fun4: Double => Double = {
            sqrt _
        }

        val quintuple = mulBy(5)
        val res8 = quintuple(20)
        println("res8: " + res8)
        // Output: res8: 100.0

        // You can also do it in one step.
        val res9 = mulBy(5)(20)



        // ========== 12.4 Parameter Inference ==========
        // When you pass an anonymous function to another function, Scala helps you by deducing
        // types when possible.
        // def valueAtOneQuarter(f: Double => Double): Double = f(0.25)

        val res10 = valueAtOneQuarter((x: Double) => x * 3)

        // Since the function knows that you will pas in Double => Double.
        val res11 = valueAtOneQuarter(x => x * 3)

        // As a special bonus, for a function that has just one parameter, you can omit the ()
        // around the parameter. If you have 2 parameters, you cannot drop the parenthesis.
        val res12 = valueAtOneQuarter(x => x * 3)

        // If a parameter occurs only once on the right-hand side of => you can replace it
        // with an underscore.

        // A function that multiplies something by 3.
        val res13 = valueAtOneQuarter(3 * _)

        // Keep in mind that these shortcuts only work when the parameter types are known.



        // ========== 12.5 Useful Higher-order Functions ==========
        // Many methods in the Scala collection library uses higher-order functions.
        // For the big data application, they also use lots of higher-order functions.

        // Map each value of this collection to 0.1 * value.
        // This is very quick way to produce a collection containing 0.1, 0.2, ..., 0.9
        val res14 = (1 to 9).map(0.1 * _)
        println("res14: " + res14)
        // Output: res14: Vector(0.1, 0.2, 0.30000000000000004, 0.4, 0.5, 0.6000000000000001, 0.7000000000000001, 0.8, 0.9)

        // <ore concise and more expressive, the better your code.
        (1 to 9).map("*" * _).foreach(println)
        // Output:
        // *
        // **
        // ***
        // ****
        // *****
        // ******
        // *******
        // ********
        // *********

        // Filter method.
        // How to get only even number in a sequence.
        val res15 = (1 to 9).filter(_ % 2 == 0)
        println("res15: " + res15)
        // Output: res15: Vector(2, 4, 6, 8)

        // reduceLeft
        // Produce something by combining elements from left to right.
        val res16 = (1 to 5).reduceLeft((x, y) => x * y)
        println("res16: " + res16)
        // Output: res16: 120

        val res17 = (1 to 5).reduceLeft(_ * _)

        val res18 = (1 to 5).reduceLeft((x: Int, y: Int) => x * y)

        // Split the string into array of strings based on whitespace.
        val res19 = "Mary had a little lamb".split(" ")
        println("res19: " + res19.mkString(", "))
        // Output: res19: Mary, had, a, little, lamb

        // Sort the sequence of strings based on length of string.
        val res20a = res19.sortWith((x, y) => x.length < y.length)
        println("res20a: " + res20a.mkString(", "))
        // Output: res20a: a, had, Mary, lamb, little

        // Sort the sequence of strings based on value of first letter.
        val res20b = res19.sortWith((x, y) => x(0) < y(0))
        println("res20b: " + res20b.mkString(", "))
        // Output: res20b: Mary, a, had, little, lamb



        // ========== 12.6 Closure ==========
        // Closure consists of code together with the definition of any non-local variables
        // that code uses.

        def mulBy3(factor: Double): Double => Double = {
            (y: Double) => factor * y
        }

        val triple2 = mulBy3(3)
        val half2 = mulBy3(0.5)

        // Each of the returned functions has its own setting for factor.
        // Factor is non-local variable captured by function.



        // ========== 12.8 Currying ==========

        def mul(x: Int, y: Int): Int = x * y
        // This function takes one argument, yielding a function that takes another argument.
        // You are doing partial computation given x only.
        // In many cases, x and y are not available at the same time. If you only have x, you
        // can get y from other program.

        // In that situation, this function is very useful.
        def mulOneAtTime(x: Int): Int => Int = (y: Int) => x * y

        // Equivalent notation
        // You can put x and y into different parameter list.
        def mulOneAtTime2(x: Int)(y: Int): Int = x * y

        val x = 5

        // _ means I assign a function NOT function call to res21.
        val res21 = mulOneAtTime2(x) _
        val y = 10
        val res22 = res21(y) // When y is available, you can do final computation.
        println("res22: " + res22)
        // Output: res22: 50


        // Why curry is useful?
        // Sometimes, only value of x is available, you can call mulOneAtTime2 function to
        // produce partial computation. Then, in other context, when y is available, you can
        // perform complete computation.

        // Furthermore, Curry is important for type inference for compiler.



        // ========== 12.9 Control Abstractions ==========
        // Call-by-value parameter: parameter is always evaluated before the function call.
        // Call-by-name parameter: (important concept in functional programming) parameter will not
        // evaluate when you call the function. Parameter is evaluated on demand.

        // This is called LAZY EVALUATION.

        // Call-by-value sometimes is challenging since it's difficult to figure out the actual
        // memory usage of your program. However, lazy can be efficient.

        // :=> means call by name.
        // : means call by value.

        // Block is call-by-name parameter.
        // If the block is call-by-value, this piece of code will be run immediately before the
        // function is called.
        // Here, we want to execute this code in another thread.

        def runInThread(block: => Unit): Unit = {
            println("I am in the function")
            // We will run the block in another thread.
            // Another thread means this computation will happen in second thread.
            new Thread {
                override def run(): Unit = { block }
            }.start()
        }

        runInThread { println("Hi"); Thread.sleep(5000); println("Bye") }
        // Output:
        // I am in function
        //
        // Hi
        // 9
        // 8
        // 7
        // 6
        // 5
        // 4
        // 3
        // 2
        // 1
        // 0
        // Bye

        // Let us define an until statement that works like while, but with an inverted condition.
        // Inverted condition means: if condition is false, we continue.
        // If condition is true, we stop.

        // We have to define condition as call-by-nae because we want to evaluate this condition
        // multiple times.
        // Call-by-name is widely used for big data application.
        def until(condition: => Boolean)(block: => Unit): Unit = {
            if (!condition) {
                block
                until(condition)(block)
            }
        }

        println()
        var z = 10

        // Until is language keyword created by yourself.
        // This is a very powerful feature which makes Scala extend its functionality very easily.
        // if z == 0 is false, continue the loop.
        until(z == 0) {
            z -= 1
            println(z)
        }

    }
}

