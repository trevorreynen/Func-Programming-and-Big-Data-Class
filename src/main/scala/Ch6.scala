// Ch6.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/08/2022
// Trevor Reynen

// Created by Wei Zhong on 07/04/2020.

// Chapter 6
// In this section we will learn when to use the Object construct in Scala.
// You use Object construct when you need a class with a single instance or when you want to find
// a home for various values or functions, which is very similar to a static method.


// ========== 6.1 Singletons ==========
// Scala has no static methods or static fields. Instead, you use the Object construct.
// An Object defines a single instance of a class with features that you want.

// In our example the Account constructor is executed when the first call to
// Account.newUniqueNumber() is made. If an Object is never used, its constructors are not
// executed at all.
object Account {

    // Similar to static variable (field) in Java.
    private var lastNumber = 0

    def newUniqueNumber(): Int = {
        lastNumber += 1
        lastNumber
    }

} // End of Object Account.



// ========== 6.2 Companion Object ==========
// In Java or C++, you often have a class with both instance methods and static methods.

// In Scala, you achieve this by having a class and companion Object of the same name. In other
// words, the companion Objects store static members and static methods like Java.

// Primary constructor is private. Nobody outside can call this constructor.
// 'val' means field. Without 'val', it is class parameter.

// You have two fields, first one (id) is immutable and second one (balance) is mutable.

class Account1 private(val id: Int, initialBalance: Double) {

    // Declare second field.
    private var balance = initialBalance

    def deposit(amount: Double): Unit = { balance += amount }

    def description: String = "Account " + id + " with balance " + balance

}


// Companion Object.
// This will contain static method and variable for Account1 class.
object Account1 {

    // This is the field for Object Account1, which is similar to static member of Java.
    private var lastNumber = 0

    private def newUniqueNumber(): Int = { lastNumber += 1; lastNumber }

    // It's common to have Object with apply method. Typically, such apply method returns an Object
    // of the companion class.

    // Object Account1 can only have one instance.
    // Object of companion class Account1 can have multiple instances.

    // Since primary constructor of companion class is private, only way to create these Objects
    // is by calling apply method.

    def apply(initialBalance: Double): Account1 = {
        new Account1(newUniqueNumber(), initialBalance)
    }

} // End of Companion Object Account1.



// ========== 6.3 Objects Extending a Class or Trait (interface) ==========
// Trait is very similar to interface in Java, but more powerful.

// One useful application is to specify default Objects that can be shared.

// Abstract class means that one or several method's implementation is empty. This is useful
// for inheritance.

abstract class UndoableAction(val description: String) {

    def undo(): Unit
    def redo(): Unit

}


// A useful default is the "do nothing" action. Of course, we only need one of them. Extends means
// that DoNothingAction is the subclass of UndoableAction.
object DoNothingAction extends UndoableAction("Do Nothing") {

    def undo() {} // We define empty action
    def redo() {}

}

// DoNothingAction Object can be shared across all places that need this default.


object Ch6 {
    def main(args: Array[String]): Unit = {

        // When newUniqueNumber is called for the first time, the Account Object is
        // created (which is single instance).
        val res0 = Account.newUniqueNumber()
        println("res0: " + res0)
        // Output: res0: 1

        // You refer to same Account Object.
        val res1 = Account.newUniqueNumber()
        // You refer to same Account Object every time you call newUniqueNumber. The value
        // of its counter increases.
        // You can't have second Account Object in your program.
        val resla = Account.newUniqueNumber()

        // This is equivalent to calling Account1.apply(1000.0)
        val acct1 = Account1(1000.0)
        println(acct1.description)
        // Output: Account 1 with balance 1000.0

        val acct2 = Account1(2300.0)
        println(acct2.description)
        // Output: Account 2 with balance 2300.0

        val actions = Map("open" -> DoNothingAction, "save" -> DoNothingAction)

    }
}

