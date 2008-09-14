package cn.sh.fang.chenance.data.entity;

public aspect ABinder {

	pointcut bindFieldSet() : set(public * b_*);
	
	pointcut bindSetters() : execution(* BaseEntity+.set*(..));
	
	after() : bindSetters() {
		String name = thisJoinPoint.getSignature().getName();
		name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
		((BaseEntity)thisJoinPoint.getThis()).changeSupport.firePropertyChange(name, null, null);

		// find if the variable is registered
//		Entry e = new Entry( thisJoinPoint.getThis(), thisJoinPoint. );
//		List<Entry> list = Binder.map.get( 

		// if true, sync it
    }
	
	pointcut startMethod() : 
		execution(* cn.sh.fang.chenance.util.swt.SwtTextAdapterProvider.*(..)) ||
		execution(* cn.sh.fang.chenance.util.swt.SwtTextAdapterProvider.Adapter.*(..));
	
	before() : startMethod() {
        System.err.println( thisJoinPoint.toLongString() );
	}

}
