<h1 style="text-align: center;">PDF Mantis</h1><p align="center">
  Simplified PDF Data Extraction
</p>


[![Generic badge](https://img.shields.io/badge/Release-0.0.1-blue.svg)](https://shields.io/)
[![Generic badge](https://img.shields.io/badge/Language-Java-red.svg)](https://shields.io/)
[![Generic badge](https://img.shields.io/badge/Platforms-Windows|Linux|Mac-black.svg)](https://shields.io/)

## Table of Contents

- [What is PDF Mantis](#What-is-PDF-Mantis)
- [Why was PDF Mantis created and who is it for](#Why-was-PDF-Mantis-created-and-who-is-it-for)
- [Requirements](#Requirements)
- [Installation](#Installation)
- [Usage](#Usage)
  - [Loading a PDF](#Loading-a-PDF)
  - [Closing a PDF](#Closing-a-PDF)
  - [Text](#Text)
  - [TextIndex](#TextIndex)
  - [TextIndex Expanded](#TextIndex-Expanded)
  - [Images](#Images)
  - [ImageIndex](#ImageIndex)
  - [Metadata](#Metadata)
  - [Tables](#Tables)
  - [OCR](#OCR)
- [FAQ](#FAQ)
- [I have a question](#I-have-a-question)
- [I want to contribute](#I-want-to-contribute)
- [Acknowledgments](#acknowledgments)

## What is PDF Mantis

PDF Mantis is a Java based high level API which aims to simplify PDF data extraction. 

It provides a unified set of extraction features:

* Simple text extraction
* Identification of text coordinates, font and colour
* Detection and extraction of images by page or page coordinates
* Extract standard metadata fields as well as exposing hidden ones
* Detect and extract simple data tables
* Straightforward optical character recognition for scanned PDF's

Available for use on Windows, Linux and macOS.

## Why was PDF Mantis created and who is it for
Despite [not being particularly well regarded](https://www.google.com/search?q=why+pdf+is+a+terrible+format) by the tech community, the PDF is one of the world's most popular document formats - As a result of this, it is not uncommon to find PDF generation nestled somewhere within a typical businesses internal systems.

The idea for PDF Mantis came about while performing some work at one such client - The end output of their system was a PDF which was sent directly to their customers. We were tasked with writing a test automation framework which validated these outputs.

After scouring Github for suitable OSS candidates, we managed to find a [collection of different libraries](#acknowledgments) which would help us complete the task.

The problem was that it took quite a bit of work to become familiar with the nuances of the libraries in conjunction with the idiosyncrasies of PDFs. After a lot of trial and error, we ended up with a solid bunch of helper classes which performed common extraction tasks, such as analysing text etc.

We couldn't help thinking at the time that this should be easier - much easier. We only wanted to extract from PDFs, not build them!.

And that's what PDF Mantis aims to be, a simple way to extract data from PDFs without necessarily understanding the complexities of PDFs.

## Requirements
PDF Mantis requires at least [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 8 and then either [Maven](http://maven.apache.org) or [Gradle](https://gradle.org).

## Installation
### Maven

For [Maven](https://maven.apache.org/), add the below dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.graysonnorland.pdfmantis</groupId>
    <artifactId>pdf-mantis</artifactId>
    <version>0.0.1</version>
    <scope>test</scope>
</dependency>
```
### Gradle
Alternatively for [Gradle](https://gradle.org), add the following to your `build.gradle` file:

```yml
testCompile 'com.graysonnorland.pdfmantis:pdf-mantis:0.0.1'
```


## Usage

### Loading a PDF

First things first, you need to create a `PdfMantis` object - This effectively represents the PDF. It is from this object that you will be able to access all extraction features.

There are several ways you can load a PDF:

```java
// Load by String path
PdfMantis pdf = new PdfMantis("/home/example.pdf");

// Load by File object
PdfMantis pdf = new PdfMantis(new File("/home/example.pdf"));

// Load by URL
PdfMantis pdf = new PdfMantis(new URL("https://www.somewebpage.com/example.pdf"));

// Load by Input Stream
PdfMantis pdf = new PdfMantis(this.getClass().getResourceAsStream("example.pdf"));
```

If your PDF is encrypted, just provide the password as a secondary input parameter like so:

```java
// Load encrypted PDF by String path
PdfMantis pdf = new PdfMantis("/home/example.pdf", "SuperSecret00!");

// Load encrypted PDF by URL
PdfMantis pdf = new PdfMantis(new URL("https://www.somewebpage.com/example.pdf", "abc123"));
```

### Closing a PDF

It is best practise to close a PDF once your work on it is complete - You can do this like so:

```java
// Load a PDF
PdfMantis pdf = new PdfMantis("/home/example.pdf");

// Then close it
pdf.closePdf();
```

### Text

The `getText()` method handles text extraction.

You can get all the text from the PDF as a String:
```java
String allText = pdf.getText().getAll();
```
Or get text from a certain page:
```java
String textFromPage2 = pdf.getText().getAllFromPage(2);
```
Or even get the text from a page range:
```java
String textFromPages2To3 = pdf.getText().getAllFromPageRange(2, 3);
```

You can also extract text from an area on a page via coordinates:
```java
int page = 2;
double x = 72.02;
double y = 717.45;
double height = 4.98;
double width = 95.64;
        
String textFromArea = pdf.getText().getFromArea(page, x, y, height, width);
```

But how does one determine these coordinates? 

For that we can utilise `TextIndex`, which not only provides text coordinates but also exposes font and colour information as well.

### TextIndex
The simplest way to utilise `TextIndex` is to use the `getForString()`method:

```java
List<String> textIndexForPhrase = pdf.getTextIndex().getForString("test string");
```

This method combs the entire PDF and returns a prettified String representation of the `TextIndex` object for any occurrences of the provided phrase, with each occurrence looking something like this:

```
Page Number=1,
Word=test string,
Font=ABCDEE+Calibri,
Font Size=9.0,
Colour=FILL:GRAY 0.0;,
X=72.02,
Y=717.45,
Height=4.98,
Width=95.64
```

One thing to note for the `getForString()` method is that if the phrase you're searching for breaks to a new line it will capture the surrounding text identified in the rectangle. For example, consider the below text:
![Alt text](readme-images/multi-line-1.png?raw=true)

If you searched for the String *Apollo 11* it would capture the coordinates for the highlighted area:
![Alt text](readme-images/multi-line-2.png?raw=true)

However, if you searched for the String *Kennedy Space Center* it would capture the coordinates for the highlighted area:
![Alt text](readme-images/multi-line-3.png?raw=true)

This is because coordinates are based on the `Rectangle` class so in order to capture the requested String it had to draw a rectangle which caught all the required keywords.

While the `getForString()` method is the simplest way to use `TextIndex`, you can of course drill down into it in much further detail.

### TextIndex Expanded

There are two ways to build the `TextIndex`.

One way is to utilise the `buildUnicodeIndex()` method - This extracts coordinates, font and colour information for every single unicode character in the PDF document:

```java
List <TextIndex> unicodeIndex = pdf.getTextIndex().buildUnicodeIndex();
```

The other way is to utilise the `buildWordIndex()` method. This extracts coordinates, font and colour information for every single word in the PDF document:

```java
List <TextIndex> wordIndex = pdf.getTextIndex().buildWordIndex();
```

Once we have the `TextIndex`, there are a number of methods available to expose key information for each entry: 

```java
// Get the index
List <TextIndex> wordIndex = pdf.getTextIndex().buildWordIndex();

// Iterate over each entry in the index
for (TextIndex word : wordIndex) {

    // And then we can expose information like so...
    word.getWord();
    word.getPageNumber();
    word.getFont();
    word.getFontSize();
    word.getColour();
    word.getX();
    word.getY();
    word.getHeight();
    word.getWidth();
}
```

If desired, we can convert `TextIndex` into a String List for easy viewing:

```java
// Get your index
List <TextIndex> wordIndex = pdf.getTextIndex().buildWordIndex();
// Then prettify it!
List <String> prettyWordIndex = pdf.getTextIndex().prettifyIndex(wordIndex);
```

### Images

The `getImage()` method handles image extraction.

It returns a Map; the key is a String containing the unique image name, and the value is a `BufferedImage`.

You can get all the images from the PDF like so:
```java
Map<String, BufferedImage> allImages = pdf.getImage().getAllImages();
```
Or get all images from a certain page:
```java
Map<String, BufferedImage> imagesFromPage3 = pdf.getImage().getImagesFromPage(3);
```

You can also extract an image from an area on a page via coordinates:
```java
int page = 2;
double x = 48.699;
double y = 688.5;
double height = 120.0;
double width = 650.0;

BufferedImage actualImage = pdf.getImage().getFromArea((page, x, y, height, width);
```
To obtain image coordinates, we can utilise `ImageIndex`.

### ImageIndex

You can build the `ImageIndex` by utilising the `getImageIndex()` method:

```java
List <ImageIndex> imageIndex = pdf.getImageIndex().buildIndex();
```

Once we have the `ImageIndex`, there are a number of methods available to expose key information for each entry:

```java
// Get the index
List <ImageIndex> imageIndex = pdf.getImageIndex().buildIndex();

// Iterate over each entry in the index
for (ImageIndex image : imageIndex) {

    // And then we can expose information like so...
    image.getPageNumber();
    image.getImageName();
    image.getImage();
    image.getX();
    image.getY();
    image.getHeight();
    image.getWidth();
}
```
If desired, we can convert `ImageIndex` into a String List for easy viewing:

```java
// Get your index
List <ImageIndex> imageIndex = pdf.getImageIndex().buildIndex();
// Then prettify it!
List <String> prettyImageIndex = pdf.getImageIndex().prettifyIndex(imageIndex);
```

### Metadata

The `getMeta()` method handles metadata extraction.

You can pull several standard metadata fields like so:

```java
String creationDate = pdf.getMeta().getCreationDate();
String modifiedDate = pdf.getMeta().getModifiedDate();
String producer = pdf.getMeta().getProducer();
String keywords = pdf.getMeta().getKeywords();
String creator = pdf.getMeta().getCreator();
String subject = pdf.getMeta().getSubject();
String author = pdf.getMeta().getAuthor();
String title = pdf.getMeta().getTitle();
```

We can also go a bit deeper and attempt to expose hidden metadata fields via the `getAll()` method - This method takes advantage of [Apache Tika's](https://tika.apache.org/) auto-parsing capabilities to achieve this.

It returns a String Map, with the key being the metadata field name, and the value being the metadata value extracted:

```java
Map<String, String> allMetadata = pdf.getMeta().getAll();
```

### Tables

The `getTable()` method handles table extraction.

This method utilises [Tabulas's](https://github.com/tabulapdf/tabula-java) table detection algorithms to find and extract simple table data from PDFs.

You can either extract tables from a single page:

```java
List<Table> tablesFromPage2 = pdf.getTable().extractFromPage(2);
```

Or extract tables from the entire PDF document:

```java
List<Table> allTables = pdf.getTable().extractAll();
```

Once you have your table, you can navigate and query it using [Tabulas's Table Class](https://javadoc.io/doc/technology.tabula/tabula/latest/technology/tabula/Table.html) like so:

```java
// Get the first table from page 1
Table firstTableFromPage1 = pdf.getTable().extractFromPage(1).get(0);

// Get the cell value from the second column on the third row
String secondColumnThirdRowValue = table.getCell(2, 1).getText();

// Get the total number of rows in the table
int totalRows = table.getRowCount();
```

If desired, you can prettify the table into a String, so you can easily view what has been extracted:

```java
// Get the first table from page 1
Table firstTableFromPage1 = pdf.getTable().extractFromPage(1).get(0);

// Prettify it!
String prettyTable = pdf.getTable().prettifyTables(firstTableFromPage1);
```
If you print the String then it will look something like this:

```java
╔═════════════════╤══════════════════════╗
║ Number of Coils │ Number of Paperclips ║
╠═════════════════╪══════════════════════╣
║ 5               │ 3, 5, 4              ║
╟─────────────────┼──────────────────────╢
║ 10              │ 7, 8, 6              ║
╟─────────────────┼──────────────────────╢
║ 15              │ 11, 10, 12           ║
╟─────────────────┼──────────────────────╢
║ 20              │ 15, 13, 14           ║
╚═════════════════╧══════════════════════╝

```
Please note, the more complex the table, [the harder it is to detect](#Why-am-I-struggling-to-extract-certain-tables).

### OCR
*Please note, if you're using Windows then the OCR features will work out of the box - However, if you're using Linux or macOS then some [initial set-up](#How-can-I-get-OCR-working-on-Linux-and-macOS) is required.* 

The `getOCRText()` method handles OCR.

This method utilises [Tesseract's](https://github.com/tesseract-ocr/tesseract) deep learning neural networks to perform optical character recognition against scanned PDFs.

You can perform OCR against the whole PDF:
```java
String allOCRText = pdf.getOCRText().getAll();
```
Or against a certain page:
```java
String ocrTextFromPage3 = pdf.getOCRText().getAllFromPage(3);
```
Or even against a page range:
```java
String ocrTextFromPages1To3 = pdf.getOCRText().getAllFromPageRange(1, 3);
```

OCR is defaulted to 300 DPI as this is the [resolution that works best](https://tesseract-ocr.github.io/tessdoc/ImproveQuality.html#:~:text=Tesseract+works+best+on+images,of+capital+letter+in+pixels.) with Tesseract, but you can override this if desired by providing it as a secondary input parameter:
```java
// Lower resolution (150 DPI)
String allOCRTextLowerResolution = pdf.getOCRText().getAll(150);

// Higher resolution (500 DPI)
String ocrTextFromPage3HigherResolution = pdf.getOCRText().getAllFromPage(3, 500);
```

## FAQ
### Why does PDF Mantis work perfectly on some PDFs but not others
The PDF format was created so that a document could be displayed exactly the same on any machine, regardless of what operating system it was created with - However, it's important to point out that **not all PDFs are created equal**.

It's an incredibly versatile and highly configurable format - Generally speaking, no two PDFs are the same and therein lies the problem.

As a result of this, if you are facing difficulties which are not covered within this README, it is recommended that you raise an issue and share the offending PDF.

### Why am I struggling to extract certain tables
When Tabula's table detection algorithms work, it feels a bit like magic. Alas, it's not actually magic but just some very well-designed code.
However, there are some occasions where the algorithms cannot correctly identify a table due to its complexity.

There are two options in this case:
- You can use `Text Index` to determine the co-ordinates of the columns and extract accordingly.
- You can switch to [Tabula](https://github.com/tabulapdf/tabula-java) itself, which offers a host of different tuning options to identify trickier tables.

### How can I get OCR working on Linux and macOS
While the [Tesseract](https://github.com/tesseract-ocr/tesseract) binaries for Windows are included in [Tess4j](http://tess4j.sourceforge.net/), they are not for Linux & macOS. This means you will to have to build/install the necessary libraries before you can use the OCR methods on these platforms. 

The current version of PDF Mantis uses Tess4j version `4.5.4`, which requires Tesseract version `4.1.1`. 

You can generally just use a package manager to install the necessary components, such as `apt install tesseract-ocr` if your on Linux or `brew install tesseract` if your on macOS.

If you face issues installing via package managers, you can [install directly from git](https://tesseract-ocr.github.io/tessdoc/Compiling-%E2%80%93-GitInstallation.html) instead.

## I have a question

Before you raise an issue, it is best to search for existing issues that might help you. If you have found a suitable issue but still need clarification, you can write your question in this issue.

Also, consider that PDF Mantis is effectively an abstraction layer which sits over a bunch of well established projects. As such, you may very well find answer to your problems on Stack Overflow and the like, so it's worth having a scan of the internet first.

If you then still feel the need to ask a question and need clarification, we recommend the following:

- Open an Issue
- Provide as much context as you can about what problem you're running into.
- Attach the PDF.

## I want to contribute

Create a pull request, make your changes, add your tests and then submit for review.

Please note, if your making changes to [OCR](#OCR) and you're working on Linux or macOS, then due to [this](#How-can-I-get-OCR-working-on-Linux-and-macOS), the associated tests are skipped by default.

You can force it to run by passing the system property of `-DforceOCRTests=true` like so:

```shell
mvn clean test -DforceOCRTests=true
```

## Acknowledgments

PDF Mantis makes use of the following open source projects:

- [Apache PDFBox](https://pdfbox.apache.org/)
- [Apache Tika](https://tika.apache.org/)
- [Apache Maven](https://maven.apache.org/)
- [Tabula](https://tabula.technology/)
- [Tess4j](http://tess4j.sourceforge.net/)
- [JUnit](https://junit.org/junit5/)
- [Flip Tables](https://github.com/JakeWharton/flip-tables)