#!/bin/sh
exec java \
	-Dlog4j.configuration=log4j.release.properties \ 
	-Djava.library.path=./lib/ -DXstartOnFirstThread \
    -classpath chenance-gui-${chenance.gui.version}.jar:chenance-data-${chenance.data.version}.jar \
    cn.sh.fang.chenance.MainWindow
