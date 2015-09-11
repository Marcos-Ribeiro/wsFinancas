package br.com.tcc.crud;

import java.util.List;

public interface Crud <T> {
	
	public List<T> getAll();
	public List<T> getAllUser(Integer user);
	public T getObject(Integer id);
	public T save(T object);
	public void update(T object);
	public void delete(T object);
	
}
