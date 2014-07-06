package org.lance.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用于测试android跨进程调用的java bean
 * 
 * @author chengkai
 * 
 */
public class Person implements Parcelable {
	private String name;
	private int age;
	private String sex;

	public Person() {
	}

	public Person(String name, int age, String sex) {
		this.name = name;
		this.age = age;
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (!(obj instanceof Person)) {
			return false;
		} else {
			Person p = (Person) obj;
			return this.name == p.name && this.age == p.age
					&& this.sex == p.sex;
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode = hashCode * 17 + age;
		hashCode = hashCode * 17 + name == null ? 0 : name.hashCode();
		hashCode = hashCode * 17 + sex == null ? 0 : sex.hashCode();
		return hashCode;
	}

	@Override
	public String toString() {
		return "姓名:" + name + "\t年龄:" + age + "\t性别:" + sex;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// 将属性参数写入Parcel中供远程调用
		dest.writeString(name);
		dest.writeInt(age);
		dest.writeString(sex);
	}

	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		// 从Parcelable中读取数据返回Person对象
		public Person createFromParcel(Parcel source) {
			return new Person(source.readString(), source.readInt(),
					source.readString());
		}

		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

	public Person(Parcel parcel) {
		this.name = parcel.readString();
		this.age = parcel.readInt();
		this.sex = parcel.readString();
	}
}
