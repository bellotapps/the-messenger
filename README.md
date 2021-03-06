# The Messenger [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://travis-ci.org/bellotapps/the-messenger.svg?branch=master)](https://travis-ci.org/bellotapps/the-messenger) [![Maven Central](https://img.shields.io/maven-central/v/com.bellotapps.the-messenger/the-messenger-build.svg)](https://repo.maven.apache.org/maven2/com/bellotapps/the-messenger/)

A framework for sending messages.


## Description

This framework consist of several libraries that helps sending and receiving messages.



## How to use it?

### Get it!

#### Maven central

All the modules are hosted in Maven Central, and can be accessed by adding the corresponding dependency in your ```pom.xml``` file.

For example:

```xml
<dependency>
    <groupId>com.bellotapps.the-messenger</groupId>
    <artifactId>commons</artifactId>
    <version>${the-messenger.version}</version>
</dependency>
```

**Note:** A placeholder is used as ```version``` in the previous example to avoid changing this readme each time a new version is released. Replace the ```${the-messenger.version}``` placeholder with the actual version of the ```the-messenger``` project.


#### Build from source

You can also build your own versions of the libraries.
**Maven is required for this**.

```
$ git clone https://github.com/bellotapps/the-messenger.git
$ cd the-messenger
$ mvn clean install
```

**Note:** There are several profiles defined in the root's ```pom.xml``` file. The ```local-deploy``` profile will also install the sources and javadoc jars. You can use it like this:

```
$ mvn clean install -P local-deploy
```

**Note:** You can also download the source code from [https://github.com/bellotapps/the-messenger/archive/master.zip](https://github.com/bellotapps/the-messenger/archive/master.zip)

**Note:** If you just want to get the JAR files, you must use the following command which won't install the libraries in your local repository.

```
$ mvn clean package
```


### Bill of Materials

This project includes a Bill Of Materials in order to make it easier to import the libraries. Include the following in your ```pom.xml```.

```xml
<dependencyManagement>
    <dependencies>
        <!-- ... -->
        <dependency>
            <groupId>com.bellotapps.the-messenger</groupId>
            <artifactId>bill-of-materials</artifactId>
            <version>${the-messenger.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <!-- ... -->
    </dependencies>
</dependencyManagement>
```

After adding the ```bill-of-materials``` artifact as an imported managed dependency, you can start using the different libraries in your project.

**Note:** A placeholder is used as ```version``` in the previous example to avoid changing this readme each time a new version is released. Replace the ```${the-messenger.version}``` placeholder with the actual version of the ```the-messenger``` project.


## License

Copyright 2019 BellotApps

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
