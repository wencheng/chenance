/**
 * (c) Copyright Mirasol Op'nWorks Inc. 2002, 2003. 
 * http://www.opnworks.com
 * Created on Apr 2, 2003 by lgauthier@opnworks.com
 * 
 */

package cn.sh.fang.chenance.provider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import cn.sh.fang.chinance.data.entity.Transaction;

/**
 * Sorter for the TableViewerExample that displays items of type 
 * <code>ExampleTask</code>.
 * The sorter supports three sort criteria:
 * <p>
 * <code>DESCRIPTION</code>: Task description (String)
 * </p>
 * <p>
 * <code>OWNER</code>: Task Owner (String)
 * </p>
 * <p>
 * <code>PERCENT_COMPLETE</code>: Task percent completed (int).
 * </p>
 */
public class BalanceSheetSorter extends ViewerSorter {

	/**
	 * Constructor argument values that indicate to sort items by 
	 * description, owner or percent complete.
	 */
	public final static int DESCRIPTION 		= 1;
	public final static int OWNER 				= 2;
	public final static int PERCENT_COMPLETE 	= 3;

	// Criteria that the instance uses 
	private int criteria;

	/**
	 * Creates a resource sorter that will use the given sort criteria.
	 *
	 * @param criteria the sort criterion to use: one of <code>NAME</code> or 
	 *   <code>TYPE</code>
	 */
	public BalanceSheetSorter(int criteria) {
		super();
		this.criteria = criteria;
	}

	/* (non-Javadoc)
	 * Method declared on ViewerSorter.
	 */
	public int compare(Viewer viewer, Object o1, Object o2) {
/*
		DebitCreditItem task1 = (DebitCreditItem) o1;
		DebitCreditItem task2 = (DebitCreditItem) o2;

		switch (criteria) {
			case DESCRIPTION :
				return compareDescriptions(task1, task2);
			case OWNER :
				return compareOwners(task1, task2);
			case PERCENT_COMPLETE :
				return comparePercentComplete(task1, task2);
			default:
				return 0;
		}
		*/
		return 0;
	}

	/**
	 * Returns the sort criteria of this this sorter.
	 *
	 * @return the sort criterion
	 */
	public int getCriteria() {
		return criteria;
	}
}
