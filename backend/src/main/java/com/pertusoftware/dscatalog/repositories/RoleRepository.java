
package com.pertusoftware.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pertusoftware.dscatalog.entities.Role;

/*Camada de acesso a dados*/
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
