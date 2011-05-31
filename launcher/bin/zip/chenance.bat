cd lib
java -Dlog4j.configuration=log4j.release.properties -Djava.library.path=. -classpath chenance-gui-${chenance.gui.version}.jar;chenance-data-${chenance.data.version}.jar cn.sh.fang.chenance.MainWindow
pause