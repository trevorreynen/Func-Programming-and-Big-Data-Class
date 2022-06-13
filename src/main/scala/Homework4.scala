// Homework4.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/20/2022
// Trevor Reynen

// ==========<  Homework 4  >==========

object Homework4 {
    def main(args: Array[String]): Unit = {

        // Question 1
        // Compute the total amount spent per customer in some e-commerce data.
        // Sort the results based on amount spent.

        // Text file name: customer-orders.csv.
        // Format for each record: customerId itemID moneySpent.

        // This is the step-by-step instructions.
        // Split each comma-separated line into fields.
        // Map each line to key/value pairs of customer ID and dollar amount.
        // Use reduceByKey to add up amount spent by customer ID.
        // Flip key (customer ID) value (amount spent).
        // Collect results and print them.


        // Sources, most helpful, so far:
        // https://alvintoh.gitbook.io/apache-2-0-spark-with-scala/section-3-spark-basics-and-simple-examples/13.-key-value-rdds-and-the-average-friends-by-age-example
        // https://stackoverflow.com/questions/40471719/scala-convert-liststring-to-tuple-listint-int
        // https://www.oreilly.com/library/view/learning-spark/9781449359034/ch04.html

        def parseLine(line: String) = {
            val element = line.split(",")
            val customerID = element(0).toInt
            val moneySpent = element(2).toDouble
            (customerID, moneySpent)
        }

        val file = scala.io.Source.fromFile("./assets/customer-orders.csv")

        val lines = file
            .getLines()
            .map(parseLine)
            .toSeq

        for (l <- lines) {
            //println(l)

            val (x, y) = l
            println("CustomerID: " + x + ", moneySpent: " + y)
        }


    }
}

