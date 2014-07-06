package org.lance.entity;

import java.io.Serializable;

/**
 * 城市模型
 * 
 * @author Administrator
 * 
 */
public class City implements Serializable {
	private String name;
	private String province;
	private String spell;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

}
