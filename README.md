# Spreadsheet evaluation

A CLI tools that reads a  Spreadsheet file and prints out
the result of evaluating each cell.


# Requirements

- Java 8


# Tasks

## Run using Gradle

`$ ./gradlew run --args="-f sample.tsv"`


## Run using fat JAR

- Generate fat JAR: `./gradlew shadowJar`
- Evaluate sample file
`$ java -jar build/libs/cflox-0.0.1-SNAPSHOT-all.jar -f sample.tsv`


# Future work

- Extending the expression parser. Look into [ANTLR](https://theantlrguy.atlassian.net/wiki/spaces/ANTLR3/pages/2687299/Expression+evaluator)