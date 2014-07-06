package org.lance.util;

/**
 * 
 * @author lance
 * 
 */
public class WhereStringBuilder {
	private StringBuilder mSelectWhere = new StringBuilder(" ");

	public WhereStringBuilder and() {
		this.mSelectWhere.append(" AND ");
		return this;
	}

	public WhereStringBuilder append(String paramString) {
		this.mSelectWhere.append(paramString).append("=").append("?");
		return this;
	}

	public WhereStringBuilder append(String paramString, int paramInt) {
		this.mSelectWhere.append(paramString).append("=").append(paramInt);
		return this;
	}

	public WhereStringBuilder append(String paramString, long paramLong) {
		this.mSelectWhere.append(paramString).append("=").append(paramLong);
		return this;
	}

	public WhereStringBuilder lP() {
		this.mSelectWhere.append("(");
		return this;
	}

	public WhereStringBuilder or() {
		this.mSelectWhere.append(" OR ");
		return this;
	}

	public WhereStringBuilder rP() {
		this.mSelectWhere.append(")");
		return this;
	}

	public String toString() {
		return this.mSelectWhere.toString();
	}
}