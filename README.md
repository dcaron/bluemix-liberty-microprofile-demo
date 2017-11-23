## A WebSphere Liberty Profile Microprofile-1.2 based application prepared for deployment in IBM Cloud

**WebSphere Liberty** is a fast, dynamic, and easy-to-use Java application server, built on the open source 
[Open Liberty][openliberty] project. Ideal for developers but also ready for production, on-premise or [in the cloud][bluemix].

**IBM Bluemix** (is now **IBM Cloud**) is [the latest cloud offering][bluemix] from IBM. It enables organizations and developers 
to quickly and easily create, deploy, and manage applications on the cloud. Bluemix is an implementation of IBM's Open Cloud 
Architecture based on Cloud Foundry, an open source Platform as a Service (PaaS). IBM Cloud Foundry includes runtimes for Java, 
Node.js, PHP, Python, Ruby, Swift and Go; Cloud Foundry community build packs are also available.

Alhough IBM Cloud has already provided a runtime engine for WebSphere Liberty, sometimes this isn't enough and developers may need
their own version of the platform, i.e. a lightweight version based on Liberty Kernel, or an old version to ensure backward 
compartibility, or the version of WebSphere Liberty armed with a set of features specific for the developed application.

The project provides a demonstration of how to deploy a your own installation of WebSphere Liberty to IBM Cloud as a usual Java
application. The deployed installation is armed with the latest version of **MicroProfile**, an open forum to collaborate on 
Enterprise Java Microservices, issued on October 3, 2017.

[Eclipse MicroProfile 1.2][microprofile] is built on the 1.1 version and updates the config API and adds the health check, fault
tolerance, metrics, and JWT propagation APIs. As stated on the official page of the project, the goal of MicroProfile is to iterate 
and innovate in short cycles, get community approval, release, and repeat. Eventually, the output of this project could be submitted 
to the JCP for possible future inclusion in a Java JSR (or some other standards body). The WebSphere Liberty application server 
implements Microprofile 1.2, just the corresponding feature - `microprofile-1.2` - must be included into the *server.xml* 
configuration file.

### The application structure

The demo application is divided into two parts:
 1. *liberty-microprofile-app* - a web-application, which leverages MicroProfile 1.2. The application also contains a Java servlet
that puts into the output stream the information about on which JVM he works. This information will be important when we try to 
publish the application on different JVMs within the cloud platform.

 1. *liberty-microprofile-wlp* - a Maven project with the liberty-assembly Maven packaging type that is used to create a packaged 
 WebSphere Liberty server. The project is critically dependent on *[liberty-maven-plugin]* and leverages the plugin to provide 
 the following actions:
 * install the WebSphere Liberty runtime
 * create an application server
 * install the `microprofile-1.2` feature to the WebSphere Liberty runtime
 * copy applications specified as Maven compile dependencies to WebSphere Liberty server's apps directory
 * package the WebSphere Liberty runtime

The *liberty-maven-plugin* configuration:

```xml
<plugin>
    <groupId>net.wasdev.wlp.maven.plugins</groupId>
    <artifactId>liberty-maven-plugin</artifactId>
    <extensions>true</extensions>
    <configuration>
        <configFile>src/main/wlp/server.xml</configFile>
        <serverName>apiServer</serverName>
        <looseApplication>true</looseApplication>
        <installAppPackages>dependencies</installAppPackages>        
        <stripVersion>true</stripVersion>
        <features>
            <acceptLicense>true</acceptLicense>
	    </features>
    </configuration>
</plugin>
```

The `configFile` and `serverName` parameters let the plugin know the server's name and where the server's configuration is. 
The `looseApplication` and `installAppPackages` say to the plugin to install the applications described in the `dependencies` section 
into the server's apps directory. **Note:** the `location` attribute of the `application`/`webApplication` elements inside the 
*server.xml* configuration file, if the elements present there, must match the artifact's name (without the version number since 
the `stripVersion` plugin parameter is set to `true`). Otherwise the additional application configuration file will be created and 
the application configuration including the `context-root` will be lost.

Because no features are specified within the `features` element of the plugin's configuration, all missing features declared in the 
*server.xml* file will be installed.

IBM Cloud translates requests to the application on port **8080**, so the `httpEndpoint` configuration element of the server must
up the server on the **0.0.0.0** host and the **8080** port. The server's full configuration can be found here: 
[server.xml](liberty-microprofile-wlp/src/main/wlp/server.xml).

### Build the application

To build the application do:

```
# mvn clean package
```

### Deploy the application in IBM Cloud

By default IBM Cloud uses the *Java Buildpack Version: v3.19* buildpack to make a deployment of the application in the cloud. 
Since there are a number of executable files in the *wlp/bin* inside the WebSphere Liberty server archive, the buildpack isn't able 
to find the right one (*wlp/bin/server*). The following command must be specified for the Cloud Foundry Command Line Interface tool 
(as a parameter or in the *manifest.yml* file): `./wlp/bin/server run apiServer`. 

**Note:** `server run` is essential! If the `server start` is specified, the cloud infrastructure will seem the application has 
just been crashed after the start command exit. Once the command is specified, the path to the *java* command also must be set 
for the application. 

The *server* command reads the path from the `JAVA_HOME` environment variable. If the Java Buildpack is in use, the path to an 
OpenJDK installation must be assigned as the value of the variable: `/home/vcap/app/.java-buildpack/open_jdk_jre`. If the 
*Liberty Buildpack Version: v3.13* is taken into account, the following path to an *IBM JVM* installation must be in use: 
`/home/vcap/app/.java/jre`. If any out-of-memory problems are faced, the `JVM_ARGS` environment variable should be set.

The above parameters are collected into the *manifest.yml* file. To deploy the application in the cloud just do:

```
# cf push
```

When the command is completed, the application will be available by the configured URL:
![WLP is installed on Bluemix with IBM JVM](images/wlp-install-bluemix-ibm.png)

If you prefer *OpenJDK* vs *IBM JVM*, please use the following command:

```
# cf push -f ./manifest-oracle.yml
```

When the command is completed, the application will be available by the configured URL:
![WLP is installed on Bluemix with OpenJDK](images/wlp-install-bluemix-oracle.png)

[openliberty]: http://openliberty.io/
[bluemix]: https://console.bluemix.net/
[microprofile]: https://projects.eclipse.org/projects/technology.microprofile
[liberty-maven-plugin]: https://github.com/WASdev/ci.maven#packaging-types
