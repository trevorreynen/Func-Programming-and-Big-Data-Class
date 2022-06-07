// Rational.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Due: Wed. 06/08/2022
// Trevor Reynen

// Created by Wei Zhong on 07/04/2020.

// Define an immutable object using functional style programming.

// Since Scala allows operator as the method name, this can potentially make your code
// more understandable.

// x and y is called class parameter, which is the parameter of primary constructor.
// In front of x and y, we never put the val/var. If you put val/var in front of x or y, x
// and y become fields of the Object.
class Rational(x: Int, y: Int) {

    // Figure out what is greated common divisor so that we can reduce Rational to simplest form.
    // This is recursive function without mutation.
    private def gcd(a: Int, b: Int): Int = {
        if (b == 0) a // Base case.
        else gcd(b, a % b) // Recursive case.
    }

    // We define the private instance variable (field) holding gcd. Follwing 2 statements are
    // executed when you build the Object. They are part of your primary constructor.

    private val g = gcd(x, y)
    //println("g: " + g)
    // Above will print "g: #" every time Rational(#, #) is called in testRational.


    // Auxiliary constructor.
    // x is numerator, denominator is 1.
    def this(x: Int) = this(x, 1)

    // You can omit parenthesis if parameter list is empty.
    def numer: Int = x / g
    def denom: Int = y / g

    // Print Rational.
    override def toString: String = numer + "/" + denom

    // Add operation
    // Left operand of add is Rational number itself. Operator in Scala can be used as the
    // method name. We return a new copy of Rational. Original copy of Rational is untouched.
    def + (that: Rational): Rational = {
        new Rational(
            numer * that.denom + that.numer * denom,
            denom * that.denom
        )
    }

    // Compare two Rational values.
    def < (that: Rational): Boolean = {
        numer * that.denom < that.numer * denom
    }


    // Take the maximum of two Rational values.
    def max(that: Rational): Rational = {
        if (this < that) that else this
    }

    // Define negative of Ratinal number.
    def unary_- : Rational = new Rational(-numer, denom)

    def - (that: Rational): Rational = this + -that

}


object testRational {
    def main(args: Array[String]): Unit = {
        val rat1 = new Rational(6, 8)
        println("rat1: " + rat1)
        // Output: rat1: 3/4

        val y = new Rational(6)
        println("y: " + y)
        // Output: y: 6/1

        val res1 = new Rational(3, 4)
        val res2 = new Rational(5, 7)
        val res3 = res1 + res2
        // res3 = res1.add(res2) Java
        // res4 = res1 + res2 + res3
        // res4 = res1.add(res2).add(res3) Java
        println("res3: " + res3)
        // Output: res3: 41/28

        val res5 = new Rational(1, 3)
        val res6 = new Rational(3, 2)
        val res7 = res5 < res6
        println("res7: " + res7)
        // Output: res7: true

        val res8 = res5.max(res6)
        println("res8: " + res8)
        // Output: res8: 3/2

        val res9 = res5 - res6
        println("res9: " + res9)
        // Output: res9: 7/-6

    }
}

