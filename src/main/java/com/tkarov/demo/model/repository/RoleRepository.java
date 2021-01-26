package com.tkarov.demo.model.repository;

import com.tkarov.demo.model.persistance.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {
}
