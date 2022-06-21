// Ch13.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/13/2022
// Trevor Reynen

// Created by Wei Zhong on 07/05/2020.

// Imports.
import scala.collection.mutable.ArrayBuffer

// Chapter 13

// In Chapter 13, we will introduce the powerful collection library and discuss how to apply
// functional style programming to the collection.

// The root of the collection library is Iterable, meaning that you can iterate through
// your collection.

// For iterable, there are three subclasses: Seq, Set, and Map.

// Seq is an ordered sequence of values such as array, list, or vector. In other words, for
// sequence, the order matters. IndexedSeq is the subclass of Seq. IndexedSeq allows fast random
// access through an integer index.

// Set is an unordered collection of values. Order does not matter. Each element of set has to
// be unique. Sequence allows duplicates.

// Map is a set of (key, value) pairs. Key of the map has to be unique.

object Ch13 {
    def main(args: Array[String]): Unit = {

        val coll1 = Array(1, 7, 2, 9)
        // Iterable actually roots from imperative style of programming.
        val iter = coll1.iterator

        // This is the loop to go through each element one by one, which is imperative style.
        while (iter.hasNext)
            print(iter.next() + " ")
        println()
        // Output: 1 7 2 9


        // Universal Creating Principle for collection library.
        // Each Scala collection class has a companion object with an apply method for constructing
        // an instance of the collection.

        import java.awt.Color

        val res0 = Set(Color.RED, Color.GREEN, Color.BLACK)
        val res1 = Vector(Color.RED, Color.GREEN, Color.BLACK)

        // ArrayBuffer is array whose size can be changed.
        val res2 = ArrayBuffer(Color.RED, Color.GREEN, Color.BLACK)



        // ========== 13.2 Mutable and Immutable Collections ==========
        // Scala supports both mutable and immutable collections.
        // An immutable collection can never change or be updated. The advantage of an
        // immutable collection is that you can safely share a reference to it, even in a
        // multi-threaded program.
        // Scala gives a preference to immutable collection.

        // Immutable map.
        val res3 = Map("Hello" -> 42)

        // This is equivalent syntax.
        // Here, Map is the companion object of Map class. Apply is usually used to create
        // instances of class.
        val res4 = Map.apply("Hello" -> 42)

        // You want to create new key value pairs. Immutable data structure cannot update.
        // Mutable structure is convenient to program. However, it's a nightmare for multi-threads.
        //res4.put("Fred", 29)

        // Mutable Map Import.
        import scala.collection.mutable

        // Empty mutable Map.
        val res5a = new mutable.HashMap[String, Int]

        // Create mutable Map with one key value pair.
        val res5b = mutable.HashMap("Fred" -> 29)

        // For mutable Map, you can do the update.
        res5a.put("Fred", 29)
        res5a.put("Jacky", 21)
        println("res5: " + res5a)
        // Output: res5: Map(Fred -> 29, Jacky -> 21)


        // If you had no prior experience with immutable collections, you may wonder how you can do
        // useful work with them.
        // The key is that you can create new collections out of old ones.
        // This is particularly natural in recursive computations. For example, here we compute
        // the set of all digits of an integer.

        // Set here is immutable version.
        // For each recursive call, I never touch old set. I only create new set for update.

        def digits(n: Int): Set[Int] = {
            if (n < 0) digits(-n)
            // This is the base case where n is a single digit.
            else if (n < 10) Set(n)
            else digits(n / 10) + (n % 10)
        }

        val res5c = digits(553322211)
        println("res5c: " + res5c)
        // Output: res5c: Set(5, 3, 2, 1)

        // Vector is immutable indexed sequence. List is immutable non-indexed sequence.
        // ArrayBuffer is mutable indexed sequence.

        val vec1 = (1 to 10000000) map (_ % 100)

        // Convert vector into list.
        val lst1 = vec1.toList

        // Run block, measure the running time and return the results.
        // T is called type parameter, which is similar to Java's generic type.
        // Type parameter makes your code more flexible since T can be any objects.

        // Block is call-by-name parameter since you want to delay evaluation on demand.
        def time[T](block: => T): T = {
            val start = System.nanoTime()
            // Running the block.
            val result = block

            // Running time to execute block.
            val elapsed = System.nanoTime() - start
            println(elapsed + " nanoseconds ")
            result
        }

        // Comparing running time for IndexedSeq and non-IndexedSeq.
        // Access the element of vector at the position of 800,000.
        // Since list is recursive structure, you have to traverse one by one to get to the end.
        // Vector is the shallow tree. You just need a few hops to access any element. That is why
        // vector is more efficient than list.
        println("Output for r5 = time(vec1(800000))")
        val r5 = time(vec1(800000))
        println("Output for r6 = time(lst1(800000))")
        val r6 = time(lst1(800000))
        // Output:
        // Output for r5 = time(vec1(800000))
        // 23000 nanoseconds
        // Output for r6 = time(lst1(800000))
        // 9008500 nanoseconds



        // ========== 13.4 Lists, Which is Immutable Data Structure ==========
        val r7a = List(4, 2)
        // Equivalent representation. Nil means the end of the list.
        val r7b = 4 :: 2 :: Nil
        val r8 = 9 :: r7a // Add an element at the front of the list.
        // List is the fundamental functional data structure.
        println("r8: " + r8)
        // Output: r8: List(9, 4, 2)

        // In Java or C++, one uses an iterator to traverse a linked list. But it's often more
        // natural to use recursion since list is recursive data structure.

        // For example, the following function computes the sum of all elements in a list of integers.

        def sum1(lst: List[Int]): Int = {
            if (lst == Nil) 0
            else lst.head + sum1(lst.tail)
        }

        val r11 = sum1(List(9, 4, 2))
        println("r11: " + r11)
        // Output: r11: 15

        // Better way to process the list is to use pattern matching. Pattern matching is a very
        // powerful mechanism in functional programming.

        // :: operator in the second pattern is used to destructure the list into head and tail.
        // lst is the target, h :: t is the pattern.

        def sum2(lst: List[Int]): Int = {
            lst match {
                case Nil => 0
                case h :: t => h + sum2(t)
            }
        }

        val r12 = sum2(List(9, 4, 2))
        println("r12: " + r12)
        // Output: r12: 15



        // ========== 13.6 Set ==========
        // A set is a collection of distinct elements. Set by default is immutable.
        // You can create new set and original Set(2, 0, 1) is untouched.

        val r15 = Set(2, 0, 1) + 4

        // Unlike lists, Sets do not retain the order in which elements are inserted.
        // By default, sets are implemented as hash sets in which elements are organized by the
        // value of the hashcode method.

        val r16 = Set(1, 2, 3, 4, 5, 6)

        for (i <- r16) print(i + " ")
        println()
        // Output: 5 1 6 2 3 4

        // Linked hash sets remember the order in which elements were inserted.
        // It keeps a linked list for this purpose.

        val weekdays = scala.collection.mutable.LinkedHashSet("Mo", "Tu", "We", "Th", "Fr")

        for (i <- weekdays) print(i + " ")
        println()
        // Output: Mo Tu We Th Fr

        // Several useful set operations.
        val digit3 = Set(1, 7, 2, 9)

        // Check whether digit3 set has 0.
        val r17 = digit3 contains 0
        println("r17: " + r17)
        // Output: r17: false

        // Create second set.
        val primes = Set(2, 3, 5, 7)

        // Union operation.
        val r18 = digit3 ++ primes
        println("r18: " + r18)
        // Output: r18: Set(5, 1, 9, 2, 7, 3)

        // Intersect operation.
        val r19 = digit3 & primes
        println("r19: " + r19)
        // Output: r19: Set(7, 2)

        // Difference operation.
        val r20 = digit3 -- primes
        println("r20: " + r20)
        // Output: r20: Set(1, 9)



        // ========== 13.7  Operator for Adding or Removing Elements ==========
        // Vector is immutable
        // +: will add an element to the beginning of an ordered collection.

        val r21 = 1 +: Vector(1, 2, 3)
        println("r21: " + r21)
        // Output: r21: Vector(1, 1, 2, 3)

        // +: add an element to the end of ordered collection.
        val r22 = Vector(1, 2, 3) :+ 5
        println("r22: " + r22)
        // Output: r22: Vector(1, 2, 3, 5)


        // The beauty of immutable structure is that they can share anything without concerns.
        // Immutable structure does not mean they are not efficient.

        // Mutable collection have += operator that mutates the left-hand side.

        import scala.collection.mutable.ArrayBuffer
        // Numbers is the immutable reference.
        // ArrayBuffer is mutable object.
        val numbers = ArrayBuffer(1, 2, 3)
        numbers += 5
        println("numbers: " + numbers)
        // Output: numbers: ArrayBuffer(1, 2, 3, 5)

        // For immutable collection, you can use += like this.
        // r23 is mutable, Set object is immutable.
        var r23 = Set(1, 2, 3)
        r23 += 5

        // Similarly, -- operator removes multiple elements.

        val r25 = numbers -- Vector(1, 2, 7, 9)
        println("r25: " + r25)
        // Output: r25: ArrayBuffer(3, 5)
        println("numbers: " + numbers)
        // Output: numbers: ArrayBuffer(1, 2, 3, 5)



        // ========== 13.8 Common Methods for Iterable Trait ==========
        // You can think about trait like interface.

        val coll = Range(1, 10)
        println("coll: " + coll)
        // Output: coll: Range 1 until 10

        // Head: Get first element.
        val r26 = coll.head
        println("r26: " + r26)
        // Output: r26: 1

        // Last: Get the last element.
        val r27 = coll.last
        println("r27: " + r27)
        // Output: r27: 9


        // headOption is used in case the collection is empty.
        // This way, you can avoid nasty NullPointerException.
        // Option has 2 subclasses: None, Some(result). None means computation fails. Some(result)
        // will wrap the result into Some object.

        // If you have Nil for return value, you will pay big price.
        val r28 = coll.headOption

        // If r28 contains no results, default value (-1) will be returned.

        // In order to get the computation results back, the user is forced to deal with error.
        // User has to consider how to recover if the program fails. This provides very clean and
        // universal solution for Nil. This is functional style answer to Nil.
        val r28r = r28.getOrElse(-1)
        println("r28r: " + r28r)
        // Output: r28r: 1

        // Count how many even numbers you have.
        // Declarative API: What I want to do vs. Java: How to do it.
        // Declarative API is concise, expressive, and functional.
        val r29a = coll.count(_ % 2 == 0)
        // Count how many negative numbers are in the collection.
        val r29b = coll.count(_ < 0)

        // Whether all elements are even.
        val r30 = coll.forall(_ % 2 == 0)
        println("r30: " + r30)
        // Output: r30: false

        // Whether there is one even number in the collection.
        val r31 = coll.exists(_ % 2 == 0)

        // This is the advantage of functional style programming since it brings so many convenient
        // little toolboxes to you. Versus Java, you only have one big hammer called FOR loop.

        // Return all even numbers.
        val r32a = coll.filter(_ % 2 == 0)
        println("r32a: " + r32a)
        // Output: r32a: Vector(2, 4, 6, 8)

        // Return all odd numbers.
        val r32b = coll.filterNot(_ % 2 == 0)
        println("r32b: " + r32b)
        // Output: r32b: Vector(1, 3, 5, 7, 9)

        // Partition = (filter, filterNot)
        // Return the vector with even numbers and the vector with odd numbers.
        val r33 = coll.partition(_ % 2 == 0)
        println("r33: " + r33)
        // Output: r33: (Vector(2, 4, 6, 8),Vector(1, 3, 5, 7, 9))

        // takeWhile returns the prefix satisfying predicate.
        // Return the number less than 3 in the prefix.
        val r34 = coll.takeWhile(_ < 3)
        println("r34: " + r34)
        // Output: r34: Range 1 to 2

        // Drop prefix which is less than 3.
        val r35a = coll.dropWhile(_ < 3)
        println("r35a: " + r35a)
        // Output: r35a: Range 3 to 9

        def fun1(x: Int): Boolean = {
            (x < 3) && (x > 5)
        }

        // Take the prefix of numbers satisfying the condition defined by fun1.
        val r35b = coll.takeWhile(x => fun1(x))
        println("r35b: " + r35b)
        // Output: r35b: empty Range 1 until 1

        // Span produce the results based on takeWhile and dropWhile.
        // Span = (takeWhile, dropWhile)
        val r36 = coll.span(_ < 3)
        println("r36: " + r36)
        // Output: r36: (Range 1 to 2,Range 3 to 9)

        // Take first 5 numbers.
        val r37 = coll.take(5)
        println("r37: " + r37)
        // Output: r37: Range 1 to 5

        // This API works for all iterable, including Vector, String, Array, List, ArrayBuffer.

        // Drop the first 4 numbers.
        val r38a = coll.drop(4)
        println("r38a: " + r38a)
        // Output: r38a: Range 5 until 10

        // Take the last 4 numbers.
        val r38b = coll.takeRight(4)
        println("r38b: " + r38b)
        // Output: r38b: Range 6 until 10

        // SplitAt = (take, drop).
        // splitAt combines take and drop.
        val r39 = coll.splitAt(4)
        println("r39: " + r39)
        // Output: r39: (Range 1 to 4,Range 5 until 10)

        // Select the interval of numbers.
        val r40a = coll.slice(2, 8)
        println("r40a: " + r40a)
        // Output: r40a: Range 3 to 8

        val b = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val r40b = b.slice(2, 8)
        println("r40b: " + r40b)
        // Output: r40b: Vector(3, 4, 5, 6, 7, 8)

        val c = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val r40c = c.slice(2, 8)
        println("r40c: " + r40c.mkString(", "))
        // Output: r40c: 3, 4, 5, 6, 7, 8

        // Partition the collection in fixed size.
        val r41 = coll.grouped(3).toArray
        println("r41:")
        for (i <- r41)
            println(i.mkString(" "))
        println()
        // Output:
        // r41:
        // 1 2 3
        // 4 5 6
        // 7 8 9

        // Group the element in fixed size by passing the sliding window.
        val r42 = coll.sliding(3).toArray
        println("r42:")
        for (i <- r42)
            println(i.mkString(" "))
        println()
        // Output:
        // r42:
        // 1 2 3
        // 2 3 4
        // 3 4 5
        // 4 5 6
        // 5 6 7
        // 6 7 8
        // 7 8 9

        // Split the string based on spaces.
        val words = "Mary had a little lamb".split(" ")
        // Reverse the array.
        val r43 = words.reverse
        println("r43: " + r43.mkString(" "))
        // Output: r43: lamb little a had Mary

        // Sort the array based on length of array.
        val r44 = words.sortBy(_.length)
        println("r44: " + r44.mkString(" "))
        // Output: r44: a had Mary lamb little

        // Sort the array in increasing order of string length.
        val r45 = words.sortWith(_.length > _.length)
        println("r45: " + r45.mkString(" "))
        // Output: r45: little Mary lamb had a



        // ========== 13.9 Mapping Function (Higher Order Function) ==========
        // Higher order function means that you can take the function as parameter or
        // return the function. In other words, function is the first class citizen.

        // By default, Scala prefers immutable data structure.
        val names = List("Peter", "Paul", "Mary")

        // Convert each element to the uppercase.
        // You may want to transform all elements of a collection without looping.
        val r46 = names.map(_.toUpperCase)
        println("r46: " + r46.mkString(" "))
        // Output: r46: PETER PAUL MARY


        def ulcases(s: String) = Vector(s.toUpperCase, s.toLowerCase)

        // Map each element to vector of uppercase and lowercase.
        val r47 = names.map(ulcases)
        println("r47: " + r47)
        // Output: r47: List(Vector(PETER, peter), Vector(PAUL, paul), Vector(MARY, mary))

        // If the function yields a collection such as ulcases, you may want to concatenate
        // all results. After concatenation, results are easier to manipulate.

        // Ulcases is called a collection-value function.

        val r48 = names.flatMap(ulcases)
        println("r48: " + r48)
        // Output: r48: List(PETER, peter, PAUL, paul, MARY, mary)



        // ========== 13.10 Reducing and Folding ==========
        // Reducing and folding want to combine elements with a binary function.
        // Binary function is the function taking 2 parameters.

        // Reduce does not have initial value. Fold does start with initial value.
        val lst = List(1, 7, 2, 9)

        // Applies a binary operator to all elements of a collection, going left to right.
        val r49 = lst.reduceLeft(_ - _)
        println("r49: " + r49)
        // Output: r49: -17

        // Applies a binary operator to all elements of collection, going right to left.
        val r50 = lst.reduceRight(_ - _)
        println("r50: " + r50)
        // Output: r50: -13

        //val r51a = lst.foldLeft(0)(_ - _)
        //println("r51a: " + r51a)
        // Output: r51a: -19

        //val r51b = lst.foldRight(0)(_ - _)
        //println("r51b: " + r51b)
        // Output: r51b: -13



        // ========== 13.11 Zipping ==========
        // Zipping means that you want to combine corresponding elements.
        val prices = List(5.0, 20.0, 9.95)
        val quantities = List(10, 2, 1)

        // Zip produces list of tuples.
        val r52 = prices zip quantities
        println("r52: " + r52)
        // Output: r52: List((5.0,10), (20.0,2), (9.95,1))

        // Each element is total value of that product.
        val r53 = (r52 map { p => p._1 * p._2 })
        println("r53: " + r53)
        // Output: r53: List(50.0, 40.0, 9.95)

        val r54a = r53.sum

        // You can combine previous two steps.
        val r54b = (r52 map { p => p._1 * p._2 }).sum

        // Equivalent and better notation.
        // Total value of your products.
        val r54c = (r52 map { case (price, amount) => price * amount }).sum

    }
}

