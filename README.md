# CSCI-509 - Functional Programming & Big Data | Summer 2022 | USC Upstate

This is a collection of all labs and homework done in my CSCI-509 - Functional Programming & Big Data class.

---

## Downloading Apache Spark
Downloading Apache Spark to work properly in a Windows environment.

1. For the most part, except for the notes below, I followed this guide: [How to Install Apache Spark on Windows 10](https://phoenixnap.com/kb/install-spark-on-windows-10). I am on Windows 11 but that shouldn't matter for the most part.
2. NOTE: Before installing Apache Spark, I already had downloaded:
   - Java versions: 1.8.0_251 (which should only be working with this Functional Programming environment but IDK for sure) and 1.8.0_333 (main by default). I also have (what I think is) JDK 18.0.1.1. I have no idea if that's being used. It's not listed in my PATH variables but I don't remember when I removed it. Could mean something which connected to JDK before I removed it from the environment might be using it, but IDK.
   - Python 3.10.4 (and Anaconda3 but that's irrelevant to this, at least for now.)
3. Since I was told by my teacher through his environment setup guide to use Library Dependency Spark 3.0.0, I had to retrieve my link from here: [Apache Archive Spark 3.0.0](https://archive.apache.org/dist/spark/spark-3.0.0/). I have no idea what the differences are in the listed files so I am first trying "spark-3.0.0-bin-hadoop3.2.tgz".
4. I install Apache Spark to C:\Programs\Spark\ instead of the suggested root folder C:\Spark\
5. I made the new file for Hadoop and bin at C:\Programs\Hadoop\bin\
6.1. New USER environment variables added:
   - (Clicked "New..." button) Variable name: SPARK_HOME , Variable value: C:\Programs\Spark\spark-3.0.0-bin-hadoop3.2
   - (Clicked "New..." button) Variable name: HADOOP_HOME , Variable value: C:\Programs\Hadoop
6.2. New USER PATH environment variables added:
   - Added new variable: "%SPARK_HOME%\bin". Moved near the top under the javapath variable for less problems as higher variables take priority.
   - Added new variable: "%HADOOP_HOME%\bin". Moved near the top under the previous SPARK_HOME variable.

---

## Downloading Netcat/ncat (nc command on Linux)

1. On Windows, Ncat is part of Nmap. Not sure if that's also the same situation for Linux. So, I downloaded that from here: [Nmap.org Microsoft Windows binaries](https://nmap.org/download#windows).
   - The command "nc ..." on Linux is "ncat ..." for Windows (after downloading Nmap, otherwise that command doesn't exist.).

