antlr.transcriptor
==============
This tool is based on [ANTLR 4](http://antlr.org/) and [Java grammar](https://github.com/antlr/grammars-v4) provided by author of ANTLR. It will save lexical information in another file for each java file.

Binary file: [Transcriptor.jar](https://dl.dropboxusercontent.com/u/15553400/Transcriptor.jar)

# Usage

`java -jar Transcriptor.jar [src path]`

Once program finished, you will find a file with suffix name `.java.token` for each `.java` file.


# How to modify

If you don't like default token names, you could modify `Java.g4` by yourself. There are three kinds of rules in grammar. 

* Rules start with lower case are **parser rules**, which will generate AST node in parser.
* Rules start with upper case are **lexer rules**, which will emit tokens to be composited parse trees. 
* Rules start with fragment are helper rules, which could be used in lexer rules but won't generate anything.

#### So if you want to custimize token names, change those rules start with upper case.

Once you finished, assume you already installed ANTLR 4 and [Maven](http://maven.apache.org/). Type following commands in shell:

```
cd src/
antlr4 Java.g4 -no-listener        // only need lexer actually….
cd ..
mvn package
```
Then you will find the `jar` file in `target/` folder.
