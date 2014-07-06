package org.lance.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import org.lance.service.AndroidService.Stub;

/**
 * 提供aidl---注意接口名和服务名不能同名
 * 
 * @author Administrator
 * 
 */
public class AidlService extends Service {

	private PersonBinder pBinder;
	private static List<Person> persons = new ArrayList<Person>();
	static {
		Person person = new Person();
		person.setAge(25);
		person.setSex("男");
		person.setName("甘成凯");
		persons.add(person);
		person.setAge(25);
		person.setSex("女");
		person.setName("陈文龙");
		persons.add(person);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return pBinder;
	}

	public class PersonBinder extends Stub {

		@Override
		public String getName() throws RemoteException {
			return persons.get(0).getName();
		}

		@Override
		public double getSalary() throws RemoteException {
			return 5000;
		}

		@Override
		public List<Person> getPersons() throws RemoteException {
			return persons;
		}

		@Override
		public String print(Person person) throws RemoteException {
			return person.toString();
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		pBinder = new PersonBinder();
	}

}
