package com.kronen.buddy.backend.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kronen.buddy.backend.persistence.domain.backend.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

}
