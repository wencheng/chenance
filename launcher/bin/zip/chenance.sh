#!/bin/sh
exec java -Djava.library.path=./lib/ -DXstartOnFirstThread \
    -classpath chenance-gui-${chenance.gui.short}.jar:chenance-data-${chenance.data.version}.jar:antlr-2.7.6.jar:asm-1.5.3.jar:asm-attrs-1.5.3.jar:beans-1.1.1.jar:beansbinding-1.2.1-swt.jar:cglib-2.1_3.jar:commands-3.4.0.jar:common-3.4.0.jar:commons-beanutils-1.8.0.jar:commons-collections-3.2.1.jar:commons-io-1.4.jar:commons-logging-1.1.1.jar:databinding-1.1.1.jar:databinding-1.2.1.jar:dom4j-1.6.1.jar:ehcache-1.2.3.jar:h2-1.0.71.jar:hibernate-3.2.1.ga.jar:hibernate-annotations-3.2.1.ga.jar:hibernate-entitymanager-3.2.1.ga.jar:javabuilder.clazz.0.1.DEV.2008.09.03.jar:javassist-3.3.ga.jar:jboss-archive-browsing-5.0.0alpha-200607201-119.jar:jface-3.4.2.jar:jta-1.0.1B.jar:log4j-1.2.13.jar:nestedvm-1.0.jar:osgi-3.4.3.jar:persistence-api-1.0.jar:runtime-3.4.0.jar:sqlite-jdbc-3.6.16.jar:swt.jar:aspectjrt-1.6.5.jar \
    cn.sh.fang.chenance.MainWindow
