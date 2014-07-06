package org.lance.service;

import java.util.List;
import org.lance.service.Person;

interface AndroidService{
	String getName();
	double getSalary();
	List<Person> getPersons();
	String print(in Person person);
}