// Doc_LogReg.scala

// CSCI-509 - Functional Programming & Big Data | USC Upstate
// Thu. 06/30/2022
// Trevor Reynen

// In this lab, we will discuss classification. Classification is the process of putting something
// into a category. Classification has a big impact on our economy. There are many classification
// examples in the real.

// Given an image, you want to know whether the picture contains a cat or not.
// Given customer profiles, you want to know whether the customer will buy the produce or not.
// Given patients test results, you want to know whether the patient has cancer.

// Logistic regression is one type of classification algorithm.

// Before we use logistic regression algorithm, we need to build or train logistic regression model
// using the training set.

// The process of building logistic regression is very similar to how we build linear regression in
// the previous lecture.

// Official documentation for logistic regression.


// Imports.
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.classification.LogisticRegression


object Doc_LogReg {
    def main(args: Array[String]): Unit = {

        Logger.getLogger("org").setLevel(Level.ERROR)

        // Build Spark Session.
        val spark = SparkSession
            .builder()
            .master("local[*]")
            .getOrCreate()

        // Load training data.
        val training = spark.read
            .format("libsvm")
            .load("./assets/sample_libsvm_classification.txt")

        // Define logistic regression model with a few parameters.
        // In this lab, this parameter is hardcoded.
        val lr_class = new LogisticRegression()
            .setMaxIter(10)
            .setRegParam(0.3)
            .setElasticNetParam(0.8)

        // Build the logistic regression model using training set.
        val lrModel_class = lr_class.fit(training)

        // Print the coefficients and intercept for logistic regression.
        println(s"Coefficients: ${lrModel_class.coefficients}" + "\n" +
                s"Intercept: ${lrModel_class.intercept}")

        spark.stop()

        /* Output:

        Coefficients: (692,[244,263,272,300,301,328,350,351,378,379,405,406,407,428,433,434,455,456,461,462,483,484,489,490,496,511,512,517,539,540,568],[-7.353983524188197E-5,-9.102738505589466E-5,-1.9467430546904298E-4,-2.0300642473486668E-4,-3.1476183314863995E-5,-6.842977602660743E-5,1.5883626898239883E-5,1.4023497091372047E-5,3.5432047524968605E-4,1.1443272898171087E-4,1.0016712383666666E-4,6.014109303795481E-4,2.840248179122762E-4,-1.1541084736508837E-4,3.85996886312906E-4,6.35019557424107E-4,-1.1506412384575676E-4,-1.5271865864986808E-4,2.804933808994214E-4,6.070117471191634E-4,-2.008459663247437E-4,-1.421075579290126E-4,2.739010341160883E-4,2.7730456244968115E-4,-9.838027027269332E-5,-3.808522443517704E-4,-2.5315198008555033E-4,2.7747714770754307E-4,-2.443619763919199E-4,-0.0015394744687597765,-2.3073328411331293E-4])
        Intercept: 0.22456315961250325

        */

    }
}

