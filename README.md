GIFTy
=====

A simple application for generating question files in the [GIFT microformat](http://docs.moodle.org/23/en/GIFT_format) which is used by the [Moodle](https://moodle.org/)  Course Mangagement Software.

![GIFTy Example Usage](http://img641.imageshack.us/img641/8568/mulitplechoiceexamplesc.png)

Main Features
--------

GIFTy allows you to generate numerous types of GIFT formatted questions without having to understand the GIFT syntax.
These questions can be saved to a text file which can be uploaded to and read by Moodle or any other software that uses the GIFT format.
There is also the option to add questions to an existing file containing GIFT formated questions.

Currently the following question types are supported.

- True / False
- Essay
- Math Range
- Math Range with specified interval end Points
- Numerical with multiple partially correct answers and optional feedback
- Short Answer ( Multiple possible answers)
- Matching
- Multiple Choice with optional feedback
- Multiple Choice with multiple correct / partially correct answers and optional feedback

Dependencies 
------------

GIFTy is written in Java and hence requires the [Java Runtime Enviroment](http://www.oracle.com/technetwork/java/javase/downloads/jre7u9-downloads-1859586.html) (Version 7 or later).

Install
-----------

1. Simply copy the build directory to a direcotory of your choice e.g "Program Files".
2. Create a shortcut to the gifty.jar file which can be found in the build directory.
4. You should now be able to start GIFTy by double clicking the shortcut that you created.

Usage
------------
### General GIFTy Workflow

#### Creating a new GIFT Question file

1. Open GIFTy
2. Select the correct the tab for the question type you wish to generate
3. Complete all neccessary fields to create the question
4. ![Add button](http://img534.imageshack.us/img534/2277/addbutton.png) Use the  Add button to add the question - **The add button does not save the questions to a file**
5. ![Clear Button](http://img29.imageshack.us/img29/1723/clearbutton.png) Clear the fields with the Clear button if you wish...
6. Repeat steps 3 to 5 to add as many questions you like
7. Use the save button to periodically save added questions to a file -  **Unsaved questions will be lost without warning when you close GIFTy**

### GIFTy Controls

![](http://img248.imageshack.us/img248/1900/giftytut.png)


Build
------

If for whatever reason you wish to create a fresh build of GIFTy, you can do so with the ant build file build.xml which can be found in the root directory for the repository.

To build a runnable jar use the **create\_run\_jar** target.

```
$ ant create_run_jar
```

Alternatively in eclipse simply right click on the build.xml file and run as **Ant Build**

