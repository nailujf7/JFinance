# J-Finance
A desktop application written in java, which is used to manage finances. 

## Motivation
As I have to manage different bank accounts not only from myself but also for my grandparents, I faced the issue of losing overview of the finances. I always used a simple document to keep track of my finances but that was cumbersome. Since I always wanted to develop my own application I came across the idea to build an application in which I could easily manage my finances and more. 

![](https://github.com/nailujf7/JFinance/blob/master/IMG/jfinance.gif)
![](https://github.com/nailujf7/JFinance/blob/master/IMG/jfinance_themes.gif)
![](https://github.com/nailujf7/JFinance/blob/master/IMG/jfinance_login.gif)




## Getting Started

To use the desktop application there are some requirements that need to be configured before executing it.
Please follow these steps:


### Prerequisites

Java

```
Install Java 8 to run the JFinance desktop application
```

SQL host

```
Install and configure an SQL host. 
This application used MySQL Workbench's local host to run the database. 
```

### Installing

After installing Java 8 and configuring an SQL Host, simply go to .. and run the .exe or .jar file.

Step 1) Creating the database.

```
Run the CreateDB.sql file in your sql host.
```

Step 2) Configuring the connection.

```
Go to the MainApp.java (in /app/..) and edit line: 50

Keep the first  parameter the same. 
Change the second parameter to your SQL userName.
Change the third  parameter to your SQL userPassword.
```


Step 3) Run the application.

```
For getting the user passwords see: /doc/manual.pdf 
```


## Built With

* [MySQL](https://www.mysql.com/) - For the database of the application
* [Java](https://www.java.com/) - The langauge that we build our application in.
* [IntelliJ](https://www.jetbrains.com/de-de/idea/) - In what we developed the application.
* [Scenebuilder](http://gluonhq.com/products/scene-builder/) - For building the application scene's.
* [Maven](https://maven.apache.org/) - Dependency Management
* [Log4j2](https://logging.apache.org/log4j/) - Logger
* [Hibernate](https://hibernate.org/) - Database Management

### Dependencies/ Libraries 
* [JFoenix](http://www.jfoenix.com/) - For the material design in the application
* [iText](https://itextpdf.com/) - PDF Creator
* [OpenCSV](http://opencsv.sourceforge.net/) - CSV Import
* [SQL Connector](http://www.jfoenix.com/) - For the connection to the database (MYJbdc)



## Built By

* **Julian Flieter** 
