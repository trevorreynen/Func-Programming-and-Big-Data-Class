// WordCountSortedDataset.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Mon. 06/27/2022
// Trevor Reynen

// In this lab, we will count up how many of each word occurs in a book using regular expressions
// and sorting the final result. We also provide SQL query solution to count how many of each word
// occurs in the book.
// In our previous example, we used RDD. In this example, we use a combination of RDD and datasets.
// RDD is better suited to load the raw unstructured data. The dataset is used to perform SQL
// operations. As a result, we get the best of both worlds.


// Imports.
import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import Utilities._


object WordCountSortedDataset {
    def main(args: Array[String]): Unit = {

        // Set the log level to only print errors.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create a Spark Session using every core of the local machine.
        val spark = SparkSession
            .builder()
            .appName("WordCount")
            .master("local[*]")
            .getOrCreate()

        // Read each line of my book into a bookRDD.
        val bookRDD = spark.sparkContext.textFile("./assets/book.txt")

        // Split each line using a regular expression that extracts words.
        val wordsRDD = bookRDD.flatMap(x => x.split("\\W+"))

        import spark.implicits._

        // Convert RDD to Dataset. Our Dataset has one column named "Value".
        // RDD is good at loading raw unstructured data. Dataset is better for SQL analysis because
        // of SQL operations and optimizations.
        val wordsDS = wordsRDD.toDS()

        println("Show wordsDataset")
        wordsDS.show(4)

        // Normalize everything to lowercase using lowercase function.
        // Rename column name from value to word.
        val lowercaseWordsDS = wordsDS.select(lower($"value").alias("word"))

        // Count up the occurrences of each word sing groupBy and count.
        // groupBy will group all unique words together.
        val wordCountDS = lowercaseWordsDS.groupBy("word").count()

        println("Schema for wordCountDS")
        wordCountDS.printSchema()
        wordCountDS.show(4)

        // Sort by counts.
        val wordCountSortedDS = wordCountDS.sort("count")

        // Sho complete sorted results.
        wordCountSortedDS.show(wordCountSortedDS.count.toInt)

    }
}

