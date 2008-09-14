package cn.sh.fang.chenance;

import org.javabuilders.clazz.ClassBuilder;

public class Application {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		
		ClassBuilder bld = new ClassBuilder();
		
		//re-redirect the app to the secondary main() using the new custom classloader
		bld.run(MainWindow.class.getName(), args);

	}
	
}
