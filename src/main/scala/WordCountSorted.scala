// WordCountSorted.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Wed. 06/15/2022
// Trevor Reynen


// We want to count up how many times each word occurs in a book. Then sort the final results.
// Based on final results, we want to know which is the most popular and the least popular words.


// Imports.
import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._


object WordCountSorted {
    def main(args: Array[String]): Unit = {

        // Set log level to only print errors. Otherwise, it will print too much information.
        Logger.getLogger("org").setLevel(Level.ERROR)

        // Create SparkContext using every core of the local machine.
        val sc = new SparkContext("local[*]", "WordCountSorted")

        // Load each line of the book into an RDD.
        val inputRDD = sc.textFile("./assets/book.txt")

        // Split each line using a regular expression that extracts words.
        // We use same functional API of Scala.
        // "\\W+" means multiple spaces.
        val wordsRDD = inputRDD.flatMap(x => x.split("\\W+"))

        // Normalize everything to lowercase.
        val lowercaseWordsRDD = wordsRDD.map(x => x.toLowerCase())

        // Let us do countByValue the hard way.
        // First map each word to word count tuple.
        // reduceByKey will combine information based on each key.
        // v1 and v2 is the value for the same key.
        // Each element of wordsCountRDD is the tuple containing words and frequency.
        val wordsCountRDD = lowercaseWordsRDD
            .map(x => (x, 1))
            .reduceByKey((v1, v2) => v1 + v2)

        // At first, switch key value pairs. Sort by key.
        // Collect results to local machine. Use collection carefully.
        val wordCountSorted = wordsCountRDD
            .map { case(word, count) => (count, word) }
            .sortByKey()
            .collect()

        // Going through wordCountSorted array to figure out each word and its count.
        for (result <- wordCountSorted) {
            val (count, word) = (result._1, result._2)
            println(s"$word: $count")
        }

        // Output:
        // transitions: 1
        // intimately: 1
        // 312: 1
        // ...
        // ...
        // your: 1420
        // to: 1825
        // you: 1878

    }
}

