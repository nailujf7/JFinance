# J-Finance
A desktop application written in java that serves to manage finances. With this application, different users can operate their own account and manage their finances accordingly. One can manage different books and store payments in them. Furthermore, it is possible to import MT940 CSV files from the Sparkasse Bank and to export account statements of ledgers in PDF format. Finally, the dashboard provides an overview of the different ledgers and payments.

## Motivation
As I have to manage different bank accounts not only from myself but also for my grandparents, I faced the issue of losing overview of the finances and payments I had to make. I always used a simple document to keep track of my finances but that was cumbersome. Since I always wanted to develop my own application I came across the idea to build an application in which I could easily manage my finances and more. 

![](https://github.com/nailujf7/JFinance/blob/master/JFinance/jfinance.gif)
![](https://github.com/nailujf7/JFinance/blob/master/JFinance/jfinance_themes.gif)
![](https://github.com/nailujf7/JFinance/blob/master/JFinance/jfinance_login.gif)


Note: The JFinance folder contains an exemplary pdf of account statements and more pictures of this application.

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
Install and configure a SQL host. 
This application used MySQL Workbench's local host to run the database. 
```

### Installing

First it is necessary to install Java 8 and configuring an SQL Host.

Step 1) Creating the database.

```
Create a new database schema in your MySQL Host with the name jfinance.
```

Step 2) Configuring the connection.

```
Change the username and password to your MySQL credentials.
```


Step 3) Run the application.

```
Simply run from the JFinance folder the JFinance.jar.
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
