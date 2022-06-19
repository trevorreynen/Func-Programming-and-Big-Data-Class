// Homework4.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/20/2022
// Trevor Reynen

// ==========<  Homework 4  >==========


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext


object Homework4 {
    def parseLine(line: String): (Int, Double) = {
        val element = line.split(",")
        val customerID = element(0).toInt
        val moneySpent = element(2).toDouble

        (customerID, moneySpent)
    }

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

        Logger.getLogger("org").setLevel(Level.ERROR)

        val sc = new SparkContext("local[*]", "Homework4")

        val lines = sc.textFile("./assets/customer-orders.csv")

        val rdd = lines.map(x => parseLine(x))

        val totalSpentPerCustomer = rdd.reduceByKey((x, y) => x + y)

        // Collect and print the results.
        val results = totalSpentPerCustomer.collect()
        results.sorted.foreach(println)

        /* Output:

        (0,5524.949999999999)
        (1,4958.600000000001)
        (2,5994.59)
        ...
        ...
        (59,5642.889999999999)
        (60,5040.709999999999)
        (61,5497.4800000000005)

        */


        println("\nSorted Results:\n")

        // Get the sorted results of total spent per customer and print the sortedResults.
        val sortedTotalSpentPerCustomer = totalSpentPerCustomer
            .map(x => (x._2, x._1))
            .sortByKey()

        val sortedResults = sortedTotalSpentPerCustomer.collect()

        for (res <- sortedResults) {
            val amount = res._1
            val id = res._2
            println(s"$id: $amount")
        }

        /* Output:

        Sorted Results:

        45: 3309.3799999999997
        79: 3790.5699999999997
        96: 3924.2300000000005
        ...
        ...
        39: 6193.110000000001
        73: 6206.200000000001
        68: 6375.450000000001

        */

    }
}

