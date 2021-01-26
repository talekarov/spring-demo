package com.tkarov.demo.model.repository;

import com.tkarov.demo.model.persistance.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    Optional<User> findById(@Param("id") Long id);

    Optional<User> findByUsername(@Param("username") String username);

    List<User> findAll();

    Optional<User> findByEmail(@Param("email") String email);

    void deleteById(@Param("id") Long id);

    boolean existsByEmail (@Param("email") String email);

    boolean existsByUsername(@Param("username") String username);


}