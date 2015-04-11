# Introduction #

Add your content here.


# Package #

JFace and Databinding package includes:
(_All package start with org.eclipse._)

  * JFace
    * jface
    * core.runtime
    * core.commands
    * equinox.common

  * Databinding
    * core.databinding
    * core.databinding.beans
    * jface.databinding

# Version #

SWT 3.5.0
  * JFace 3.5.0
  * Databinding 1.2.1

SWT 3.4.2
  * JFace 3.4.2
  * Databinding 1.3.0

# pom.xml #
```
	<repositories>
		<repository>
			<id>googlecode</id>
			<url>http://chenance.googlecode.com/svn/maven2</url>
		</repository>
	</repositories>
	<dependencies>
		<dependency>
			<groupId>org.eclipse</groupId>
			<artifactId>jface</artifactId>
			<version>3.4.2</version>
		</dependency>
		<dependency>
			<groupId>org.eclipse.jface</groupId>
			<artifactId>databinding</artifactId>
			<version>1.2.1</version>
		</dependency>
	</dependencies>
```