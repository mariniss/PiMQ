PiMQ
========

"PiMQ" is a very simple library to control your Raspberry Pi GPIO through a JMS queue.
It provides also a REST interface to send manager JMS queue ad a Grails application to manage GPIO by web site.

Compiling
-------

First is necessary create the binaries from the sources the required software are Git and Maven.

1. Download the sources by `` git clone https://github.com/mariniss/PiMQ.git `` (or download the zip)
2. `` mvn clean install `` from the sources main folder

And then will we have the executable jars in the target folders for each project.

Installation
-------

To install are necessary:
* Apache ActiveMQ
* Pi4J libraries
* (optional) Apache Tomcat (or similar), if you need the REST server
* (optional) MySql DB

## Raspberry client

1. Install Pi4j on Raspberry Pi, see [http://pi4j.com/](http://pi4j.com/)
2. Copy `` activemq-all-x.x.x.jar `` in a directory of your Raspberry Pi; you can download form [http://activemq.apache.org/download.html](http://activemq.apache.org/download.html)
3. Copy PiMQ client (/PiMQ/PiMQ-Client/target/org.fm.pimq.client-0.1-SNAPSHOT.jar) to a  RaspberryPi folder (es /opt/PiMQ)

## JMS service

1. Install Apache ActiveMQ on a remote server, your pc or your Raspberry Pi, see [http://activemq.apache.org/installation.html](http://activemq.apache.org/installation.html)

## REST server (optional)

1. Install Apache Tomcat (or similar) on a remote server or your pc
2. Copy PiMQ server (/PiMQ/PiMQ-Server/target/org.fm.pimq.server-0.1-SNAPSHOT.war) to webapps root directory. If you are using tomcat the directory is : `` apache-tomcat-x.x.x/webapps ``

## Web application (optional)

1. MySql RDBMS, or similar (Postgress)
1. Install Apache Tomcat (or similar) on a remote server or your pc
2. Copy PiMQ WebApp war (/PiMQ/PiMQ-Webapp/target/org.fm.pimq.webapp-0.1-SNAPSHOT.war) to webapps root directory. If you are using tomcat the directory is : `` apache-tomcat-x.x.x/webapps ``
