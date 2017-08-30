[![License](https://img.shields.io/badge/license-LGPL%202.1-blue.svg)](http://www.gnu.org/licenses/lgpl-2.1.html) [![Language](http://img.shields.io/badge/language-java-brightgreen.svg)](https://www.java.com/)
# GML-Writer-for-yED
Extends the [Graph Modeling Language (GML)](https://en.wikipedia.org/wiki/Graph_Modelling_Language) export of JGraphT to better supports the import to yED.

* Released: January 17, 2017
* Based on: JgraphT (and yED)
* Written by [Hayato Hess](mailto:hayato.hess@gmail.com) and Contributors

## Getting Started ##
The package `com.github.systemdir.gml.examples` in the [Example](https://github.com/Systemdir/GML-Writer-for-yED/tree/master/Example/src/com/github/systemdir/gml/examples) directory contains two small demo applications to help you get started. 

To use the gml writer, ensure that your project has following maven dependencies:
```
<dependencies>
        <dependency>
            <groupId>com.github.systemdir.gml</groupId>
            <artifactId>GMLWriterForYed</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.jgrapht</groupId>
            <artifactId>jgrapht-core</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.jgrapht</groupId>
            <artifactId>jgrapht-ext</artifactId>
            <version>1.0.0</version>
        </dependency>
</dependencies>
```

### How to import to yED 
After exporting the graph to a .gml file, import it in [yED](https://www.yworks.com/products/yed) by using the open dialog. After importing, the graph will most likely look broken as every node is placed on top of each other. To resolve this, go to the `Layout` menu in yED and select one fitting layout. Further, the `Fit Node to Label` in the `Tools` menu is useful when the labels are longer than the node's widths.

## Manually Download Ressources ##
* [Binaries (1.0)](https://hayato-hess.de/files/GmlWriter/GMLWriterForYed-1.0.0.jar)
* [Source (1.0)](https://hayato-hess.de/files/GmlWriter/GMLWriterForYed-1.0.0-sources.jar)
* [Documentation (1.0)](https://hayato-hess.de/files/GmlWriter/GMLWriterForYed-1.0.0-javadoc.jar)

## Pictures ##
![yed2](http://hayato-hess.de/pictures/yED/YED2.jpg)
![yed](http://hayato-hess.de/pictures/yED/YED1cpy.jpg)
