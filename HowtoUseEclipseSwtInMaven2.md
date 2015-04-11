# Introduction #

Supports SWT 3.4.2 and 3.5.0

  * **TESTED**: Windows and Mac OS X
  * **NON TESTED**: Linux


# Details #

```
<project>
        ...
	<repositories>
                ...
		<repository>
			<id>googlecode</id>
			<name>
				on googlecode
			</name>
			<url>http://chenance.googlecode.com/svn/maven2
			</url>
			<layout>default</layout>
		</repository>
	</repositories>
        <dependencies>
                ...
		<!-- swt from chenance.googlecode.com -->
		<dependency>
			<groupId>org.eclipse</groupId>
			<artifactId>swt</artifactId>
			<version>${swt.version}</version>
			<classifier>${swt.os-specific-classifier}</classifier>
		</dependency>
        </dependencies>
	<profiles>
		<profile>
			<id>unix</id>
			<activation>
				<os>
					<family>unix</family>
					<name>linux</name>
				</os>
			</activation>
			<properties>
				<swt.os-specific-classifier>gtk-linux</swt.os-specific-classifier>
				<swt.version>3.5.0</swt.version>
			</properties>
		</profile>
		<profile>
			<id>windows</id>
			<activation>
				<os>
					<family>windows</family>
					<arch>x86</arch>
				</os>
			</activation>
			<properties>
				<swt.os-specific-classifier>win32</swt.os-specific-classifier>
				<swt.version>3.5.0</swt.version>
			</properties>
		</profile>
		<profile>
			<id>mac</id>
			<activation>
				<os>
					<family>mac os x</family>
				</os>
			</activation>
			<properties>
				<swt.os-specific-classifier>macosx</swt.os-specific-classifier>
				<swt.version>3.5.0</swt.version>
			</properties>
		</profile>
	</profiles>
</project>
```


# Upgrade/Update repository #

  1. Download the latest binary from http://www.eclipse.org/swt/
  1. Checkout old maven-metadata.xml and copy it to .m2 as maven-metadata-local.xml
  1. Use "mvn install-file" to install .jar to local repo. (see http://maven.apache.org/plugins/maven-install-plugin/usage.html)
  1. Move maven-metadata-local.xml back to local svn
  1. Commit both maven-metadata.xml and binaries in .m2