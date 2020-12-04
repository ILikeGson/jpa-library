package com.example.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
   T save(T t);
   Optional<T> findById(ID id);
   List<T> findByName(String name);
   List<T> findAll();
   void updateById(T t, ID id);
   void deleteById(ID id);
}
