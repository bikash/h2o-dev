# H2O

H2O makes Hadoop do math! H2O scales statistics, machine learning and math over BigData. H2O is extensible and users can build blocks using simple math legos in the core. H2O keeps familiar interfaces like R, Excel & JSON so that BigData enthusiasts & experts can explore, munge, model and score datasets using a range of simple to advanced algorithms. Data collection is easy. Decision making is hard. H2O makes it fast and easy to derive insights from your data through faster and better predictive modeling. H2O has a vision of online scoring and modeling in a single platform.

## 1. Product Vision for first cut

H2O product, the Analytics Engine will scale Classification and Regression.
- RandomForest, Gradient Boosted Methods (GBM), Generalized Linear Modeling (GLM), Deep Learning and K-Means available over R / REST / JSON-API
- Basic Linear Algebra as building blocks for custom algorithms
- High predictive power of the models
- High speed and scale for modeling and scoring over BigData

Data Sources
- We read and write from/to HDFS, S3, NoSQL, SQL
- We ingest data in CSV format from local and distributed filesystems (nfs)
- A JDBC driver for SQL and DataAdapters for NoSQL datasources is in the roadmap. (v2)

Console provides Adhoc Data Analytics at scale via R-like Parser on BigData
 - Able to pass and evaluate R-like expressions, slicing and filters make this the most powerful web calculator on BigData

### Users

Primary users are Data Analysts looking to wield a powerful tool for Data Modeling in the Real-Time. Microsoft Excel, R, SAS wielding Data Analysts and Statisticians.
Hadoop users with data in HDFS will have a first class citizen for doing Math in Hadoop ecosystem.
Java and Math engineers can extend core functionality by using and extending legos in a simple java that reads like math. See package hex.
Extensibility can also come from writing R expressions that capture your domain.

### Design

We use the best execution framework for the algorithm at hand. For first cut parallel algorithms: Map Reduce over distributed fork/join framework brings fine grain parallelism to distributed algorithms.
Our algorithms are cache oblivious and fit into the heterogeneous datacenter and laptops to bring best performance.
Distributed Arraylets & Data Partitioning to preserve locality.
Move code, not data, not people.

### Extensions

One of our first powerful extension will be a small tool belt of stats and math legos for Fraud Detection. Dealing with Unbalanced Datasets is a key focus for this.
Users will use JSON/REST-api via H2O.R through connects the Analytics Engine into R-IDE/RStudio.

-----

## 2. Downloading H2O Dev

While most of this README is oriented towards developers who do their own builds, most H2O users just download and use a pre-built version.  If that's you, just follow these steps:

1.  Point to <http://h2o.ai>
1.  Click on Download
1.  Scroll down to find the section for H2O Dev
1.  Click on the version you want (generally the latest numbered release)

## 3. Using H2O Dev Artifacts

Every nightly build publishes R, python, java and scala artifacts to a per-build repository.  In particular, you can find java artifacts in the maven/repo directory.

Here is an example snippet of a gradle build file using h2o-dev as a dependency.  Replace x, y, z, and nnnn with valid numbers.

```
// h2o-dev dependency information
def h2oBranch = 'master'
def h2oBuildNumber = 'nnnn'
def h2oProjectVersion = "x.y.z.${h2oBuildNumber}"

repositories {
  // h2o-dev dependencies
  maven {
    url "https://s3.amazonaws.com/h2o-release/h2o-dev/${h2oBranch}/${h2oBuildNumber}/maven/repo/"
  }
}

dependencies {
  compile "ai.h2o:h2o-core:${h2oProjectVersion}"
  compile "ai.h2o:h2o-algos:${h2oProjectVersion}"
  compile "ai.h2o:h2o-web:${h2oProjectVersion}"
  compile "ai.h2o:h2o-app:${h2oProjectVersion}"
}
```

See the latest H2O Dev [nightly build page](http://s3.amazonaws.com/h2o-release/h2o-dev/master/latest.html) for information about installing nightly build artifacts.

See the [h2o-droplets github repository](https://github.com/h2oai/h2o-droplets) for a working example of how to use java artifacts with gradle.

> Note: Stable H2O Dev artifacts are periodically published to Maven Central ([click here to search](http://search.maven.org/#search%7Cga%7C1%7Cai.h2o)) but may lag substantially behind H2O Dev Bleeding Edge nightly builds.

-----

## 4. Building H2O Dev

Getting started with H2O development requires JDK 1.7, Node.js, and Gradle.  We use the Gradle wrapper (called `gradlew`) to ensure an up-to-date local version of Gradle and other dependencies are installed in your development directory.

### 4.1. Building from the command line (Quick Start)

The assumption is the setup steps described in section 4.2 and beyond have been followed.

#### Recipe 1:  Fresh clone and build, skipping tests, and run h2o

```
# Build H2O
git clone https://github.com/h2oai/h2o-dev.git
cd h2o-dev
./gradlew build -x test

# Start H2O
java -jar build/h2o.jar

# Point browser to http://localhost:54321

```

#### Recipe 2:  Fresh clone and build, running tests

```
git clone https://github.com/h2oai/h2o-dev.git
cd h2o-dev
./gradlew syncSmalldata
./gradlew build
```

Note: Running tests starts 5 test JVMs that form an H2O cluster, and requires at least 8GB of RAM.  Preferably 16GB of RAM.

#### Recipe 3:  Pull, clean and build, running tests

```
git pull
./gradlew syncSmalldata
./gradlew clean
./gradlew build
```

#### Notes

A 'clean' is recommended after each git pull.

Skip tests by putting '-x test' at the end the gradle build command line.  Tests typically run for 7-10 minutes on a Macbook Pro laptop with 4 CPUs (8 hyperthreads) and 16 GB of RAM.

Syncing smalldata is not strictly required after each pull, but if tests fail due to missing data files then this is the first troubleshooting step to try.  Syncing smalldata grabs data files from AWS S3 to the smalldata directory in your workspace.  The sync is incremental.  Do not check these files in.  The smalldata directory is in .gitignore.  If you do not run any tests, you do not need the smalldata directory.

### 4.2. Setup on all Platforms

##### Install required python packages (using `sudo` if necessary)

    `pip install grip`
    `pip install tabulate`
    `pip install wheel`

### 4.3. Setup on Windows

##### Step 1: Download and install [Python](https://www.python.org/ftp/python/2.7.9/python-2.7.9.amd64.msi) for Windows. 
  From the command line, validate `python` is using the newly-installed package. [Update the Environment variable](https://docs.python.org/2/using/windows.html#excursus-setting-environment-variables) with the Python path.
  
  `C:\Python27\;C:\Python27\Scripts\`

###### Step 2: Install required Python packages:

    `pip install grip`
    `pip install tabulate`
    `pip install wheel`

##### Step 3: Install JDK

Install [Java 1.7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) and add the appropriate directory `C:\Program Files\Java\jdk1.7.0_65\bin` with java.exe to PATH in Environment Variables. To make sure the command prompt is detecting the correct Java version, run:

    javac -version

The CLASSPATH variable also needs to be set to the lib subfolder of the JDK: 

    CLASSPATH=/<path>/<to>/<jdk>/lib

##### Step 4. Install Node.js

Install [Node.js](http://nodejs.org/download/) and add the installed directory `C:\Program Files\nodejs`, which must include node.exe and npm.cmd to PATH if not already prepended. 

##### Step 5. Install R, the required packages, and Rtools:

To install these packages from within an R session, enter:

    R> install.packages("RCurl")
    R> install.packages("rjson")
    R> install.packages("statmod")
    R> install.packages(c("devtools", "roxygen2", "testthat"))

Install [R](http://www.r-project.org/) and add the preferred bin\i386 or bin\x64 directory to your PATH.

Note: Acceptable versions of R are >= 2.13 && <= 3.0.0 && >= 3.1.1.

To manually install packages, download the releases of the following R packages: [bitops](http://cran.r-project.org/package=bitops), [devtools](http://cran.r-project.org/package=devtools), [digest](http://cran.r-project.org/package=digest), [Rcpp](http://cran.r-project.org/package=Rcpp), [RCurl](http://cran.r-project.org/package=RCurl), [rjson](http://cran.r-project.org/package=rjson), [roxygen2](http://cran.r-project.org/package=roxygen2), [statmod](http://cran.r-project.org/package=statmod), [stringr](http://cran.r-project.org/package=stringr), and [testthat](http://cran.r-project.org/package=testthat).

    cd Downloads
    R CMD INSTALL bitops_x.x-x.zip
    R CMD INSTALL RCurl_x.xx-x.x.zip
    R CMD INSTALL rjson_x.x.xx.zip
    R CMD INSTALL statmod_x.x.xx.zip
    R CMD INSTALL Rcpp_x.xx.x.zip
    R CMD INSTALL digest_x.x.x.zip
    R CMD INSTALL testthat_x.x.x.zip
    R CMD INSTALL stringr_x.x.x.zip
    R CMD INSTALL roxygen2_x.x.x.zip
    R CMD INSTALL devtools_x.x.x.zip


Finally, install [Rtools](http://cran.r-project.org/bin/windows/Rtools/), which is a collection of command line tools to facilitate R development on Windows.
**NOTE**: During Rtools installation, do **not** install Cygwin.dll.

##### Step 6. Install [Cygwin](https://cygwin.com/setup-x86_64.exe)
**NOTE**: During installation of Cygwin, deselect the Python packages to avoid a conflict with the Python.org package. 

###### Step 6b. Validate Cygwin
If Cygwin is already installed, remove the Python packages or ensure that Native Python is before Cygwin in the PATH variable. 

##### Step 7. Update or validate the Windows PATH variable to include R, Java JDK, Cygwin. 

##### Step 8. Git Clone [h2o-dev](https://github.com/h2oai/h2o-dev.git)

If you don't already have a Git client, please install one.  The default one can be found here http://git-scm.com/downloads .  Make sure that during the install command prompt support is turned on.

Download and update h2o-dev source codes:

    git clone https://github.com/h2oai/h2o-dev

##### Step 9. Run the top-level gradle build:

    cd h2o-dev
    gradlew build

> If you encounter errors run again with `--stacktrace` for more instructions on missing dependencies.


### 4.4. Setup on OS X

If you don't have [Homebrew](http://brew.sh/), we recommend installing it.  It makes package management for OS X easy.

##### Step 1. Install JDK

Install [Java 1.7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html). To make sure the command prompt is detecting the correct Java version, run:

    javac -version

##### Step 2. Install Node.js:

Using Homebrew:

    brew install node

Otherwise, install from the [NodeJS website](http://nodejs.org/download/). 

##### Step 3. Install R and the required packages:

Install [R](http://www.r-project.org/) and add the bin directory to your PATH if not already included.

<a name="InstallRPackagesInUnix"></a>

Install the following R packages: [RCurl](http://cran.r-project.org/package=RCurl), [rjson](http://cran.r-project.org/package=rjson), [statmod](http://cran.r-project.org/package=statmod), [devtools](http://cran.r-project.org/package=devtools), [roxygen2](http://cran.r-project.org/package=roxygen2) and [testthat](http://cran.r-project.org/package=testthat).

    cd Downloads
    R CMD INSTALL bitops_x.x-x.tgz
    R CMD INSTALL RCurl_x.xx-x.x.tgz
    R CMD INSTALL rjson_x.x.xx.tgz
    R CMD INSTALL statmod_x.x.xx.tgz
    R CMD INSTALL Rcpp_x.xx.x.tgz
    R CMD INSTALL digest_x.x.x.tgz
    R CMD INSTALL testthat_x.x.x.tgz
    R CMD INSTALL stringr_x.x.x.tgz
    R CMD INSTALL roxygen2_x.x.x.tgz
    R CMD INSTALL devtools_x.x.x.tgz

To install these packages from within an R session:

    R> install.packages("RCurl")
    R> install.packages("rjson")
    R> install.packages("statmod")
    R> install.packages(c("devtools", "roxygen2", "testthat"))

##### Step 4. Git Clone [h2o-dev](https://github.com/h2oai/h2o-dev.git)

OS X should have with Git installed. To download and update h2o-dev source codes:

    git clone https://github.com/h2oai/h2o-dev

##### Step 5. Run the top-level gradle build:

    cd h2o-dev
    ./gradlew build

> If you encounter errors run again with `--stacktrace` for more instructions on missing dependencies.

### 4.5. Setup on Ubuntu 14.04

##### Step 1. Install Node.js, npm, and bower:

    sudo apt-get install npm
    sudo ln -s /usr/bin/nodejs /usr/bin/node
    npm install -g bower

##### Step 2. Install JDK:

Install [Java 1.7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html). Installation instructions can be found here [JDK installation](http://askubuntu.com/questions/56104/how-can-i-install-sun-oracles-proprietary-java-jdk-6-7-8-or-jre). To make sure the command prompt is detecting the correct Java version, run:

    javac -version

##### Step 3. Install R and the required packages:

Installation instructions can be found here [R installation](http://cran.r-project.org).  Click “Download R for Linux”.  Click “ubuntu”.  Follow the given instructions.

To install the required packages, follow the [same instructions as for OS X above](#InstallRPackagesInUnix).

##### Step 4. Git Clone [h2o-dev](https://github.com/h2oai/h2o-dev.git)

If you don't already have a Git client:

    sudo apt-get install git

Download and update h2o-dev source codes:

    git clone https://github.com/h2oai/h2o-dev

##### Step 5. Run the top-level gradle build:

    cd h2o-dev
    ./gradlew build

> If you encounter errors run again with `--stacktrace` for more instructions on missing dependencies.

> Make sure that you are not running as root, since `bower` will reject such a run.

### 4.6. Setup on Ubuntu 13.10

##### Step 1. Install Node.js, npm, and bower

On Ubuntu 13.10, the default Node.js (v0.10.15) is sufficient, but the default npm (v1.2.18) is too old, so use a fresh install from the npm website:

    sudo apt-get install node
    sudo ln -s /usr/bin/nodejs /usr/bin/node
    wget http://npmjs.org/install.sh
    sudo apt-get install curl
    sudo sh install.sh
   

##### Steps 2-4. Follow steps 2-4 for Ubuntu 14.04

### 4.7. Setting up your preferred IDE environment

For users of Intellij's IDEA, generate project files with:

    ./gradlew idea

For users of Eclipse, generate project files with:

    ./gradlew eclipse


## 5. Launching H2O after building

    java -jar build/h2o.jar


## 6. Building H2O on Hadoop

Pre-built H2O-on-Hadoop zip files are available on the [download page](http://h2o.ai/download).  Each Hadoop distribution version has a separate zip file in h2o-dev.

To build H2O with Hadoop support yourself, do the following from the top-level h2o-dev directory:

    (export BUILD_HADOOP=1; ./gradlew build -x test)
    ./gradlew dist

This will create a directory called 'target' and generate zip files there.  Note that BUILD_HADOOP is the default behavior when the username is 'jenkins' (see settings.gradle); otherwise you have to ask for it, as shown above.


### Adding support for a new version of Hadoop

In the h2o-hadoop directory each hadoop version has a build directory for the driver and an assembly directory for the fatjar.

You need to:

1.  Add a new driver directory and assembly directory (each with a build.gradle file) in h2o-hadoop
2.  Add these new projects to h2o-dev/settings.gradle
3.  Add the new hadoop version to HADOOP_VERSIONS in make-dist.sh
4.  Add the new hadoop version to wget list in h2o-dist/index.html

-----

## 7. Sparkling Water

Sparkling Water combines two open source technologies: Apache Spark and H2O, our machine learning engine.  It makes H2O’s library of Advanced Algorithms, including Deep Learning, GLM, GBM, KMeans, PCA, and Random Forest, accessible from Spark workflows. Spark users are provided with options to select the best features from either platforms to meet their Machine Learning needs.  Users can combine Spark's RDD API and Spark MLLib with H2O’s machine learning algorithms, or use H2O independent of Spark in the model building process and post-process the results in Spark. 

Here are links to resources for Sparkling Water:

* [Download page for pre-built packages](http://h2o.ai/download/) (Scroll down for Sparkling Water)
* [Sparkling Water github repository](https://github.com/h2oai/sparkling-water)
* [README](https://github.com/h2oai/sparkling-water/blob/master/README.md)
* [Developer documentaton](https://github.com/h2oai/sparkling-water/blob/master/DEVEL.md)

## 8. Documentation

### Generate REST API documentation 

To generate the REST API documentation, use the following commands: 

    cd ~/h2o-dev
    cd py
    python ./generate_rest_api_docs.py  # to generate Markdown only
    python ./generate_rest_api_docs.py --generate_html  --github_user GITHUB_USER --github_password GITHUB_PASSWORD # to generate Markdown and HTML

The default location for the generated documentation is `build/docs/REST`. 

-----
-----

## Community

We will breathe & sustain a vibrant community with the focus of taking software engineering approach to data science and empower everyone interested in data to be able to hack data using math and algorithms.
Join us on google groups [h2ostream](https://groups.google.com/forum/#!forum/h2ostream).

Team & Committers

```
SriSatish Ambati
Cliff Click
Tom Kraljevic
Tomas Nykodym
Michal Malohlava
Kevin Normoyle
Spencer Aiello
Anqi Fu
Nidhi Mehta
Arno Candel
Josephine Wang
Amy Wang
Max Schloemer
Ray Peck
Prithvi Prabhu
Brandon Hill
Jeff Gambera
Ariel Rao
Viraj Parmar
Kendall Harris
Anand Avati
Jessica Lanford
Yan Zou
Alex Tellez
Allison Washburn
Amy Wang
Erik Eckstrand
James Dean
Neeraja Madabhushi
Sebastian Vidrio
Ben Sabrin
Matt Dowle

```

## Advisors

Scientific Advisory Council

```
Stephen Boyd
Rob Tibshirani
Trevor Hastie
```

Systems, Data, FileSystems and Hadoop

```
Doug Lea
Chris Pouliot
Dhruba Borthakur
```

## Investors

```
Jishnu Bhattacharjee, Nexus Venture Partners
Anand Babu Periasamy
Anand Rajaraman
Ash Bhardwaj
Rakesh Mathur
Michael Marks
Egbert Bierman
Rajesh Ambati
```
