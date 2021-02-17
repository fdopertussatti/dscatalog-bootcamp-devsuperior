
package com.pertusoftware.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pertusoftware.dscatalog.entities.User;

/*Camada de acesso a dados*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
