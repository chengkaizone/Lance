package org.lance.util;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * ÀÈ∆¨π§æﬂ
 * 
 * @author lance
 * 
 */
public class FragmentService {
	public static void commit(FragmentManager paramFragmentManager,
			FragmentTransaction paramFragmentTransaction, boolean paramBoolean) {
		commitAndExecInternal(paramFragmentManager, paramFragmentTransaction,
				paramBoolean, false);
	}

	public static void commitAndExec(FragmentManager paramFragmentManager,
			FragmentTransaction paramFragmentTransaction, boolean paramBoolean) {
		commitAndExecInternal(paramFragmentManager, paramFragmentTransaction,
				paramBoolean, true);
	}

	private static void commitAndExecInternal(
			FragmentManager paramFragmentManager,
			FragmentTransaction paramFragmentTransaction,
			boolean paramBoolean1, boolean paramBoolean2) {
		if (!paramBoolean1)
			paramFragmentTransaction.commitAllowingStateLoss();
		else
			paramFragmentTransaction.commit();
		if (!paramBoolean2)
			return;
		paramFragmentManager.executePendingTransactions();
	}

	public static void executePendingTransactions(
			FragmentManager paramFragmentManager) {
		paramFragmentManager.executePendingTransactions();
	}
}