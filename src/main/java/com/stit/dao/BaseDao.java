package com.stit.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseDao<T, ID> {
	void insert(T t);

	void update(T t);

	void deleteById(ID id);
	void delete(T t);

	List<T> query(String sql);

	boolean existsById(ID id);
	boolean exists(T t);

	long count();

	List<T> findAll();
	Optional<T> findById(ID id);





	/*
	void insert(T t);
	void insert(T...  entities);
	void insert(Collection<T>  entities);

	void update(T t);
	void update(T...  entities);
	void update(Collection<T>  entities);

	void delete(T t);
	void delete(T...  entities);
	void delete(Collection<T>  entities);
	void deleteById(ID id);
	void deleteById(ID... ids);
	void deleteById(Collection<ID>  ids);

	boolean exists(T t);
	boolean existsById(ID id);

	long count();

	List<T> findAll();
	Optional<T> findById(ID id);
	Optional<T> findOptional(ID id);
	List<T> findAllById(ID... ids);
	List<T> findAllById(Collection<ID> ids);
 */

}
