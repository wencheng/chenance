#!/bin/sh
exec java -Djava.library.path=./lib/ -DXstartOnFirstThread \
    -classpath chenance-gui-${chenance.gui.version}.jar:chenance-data-${chenance.data.version}.jar \
    cn.sh.fang.chenance.MainWindow
