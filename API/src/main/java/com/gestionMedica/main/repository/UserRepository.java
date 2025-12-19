package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * Método para encontrar un usuario en base a un email.
     * @param String userEmail -> correo
     * @return Objeto de tipo User
     * Optional ofrece un mejor manejo para el NullPointerException
     * */
    Optional<User> findByUserEmail(String userEmail);

    /**
     * Método Optional para encontrar un usuario por el username
     * @Param String username -> Nombre del usuario
     * @return Object User -> Entidad usuario
     * */
    Optional<User> findByUserName(String userName);

    /**
     * Metodo para verificar si un usario existe en base a un email
     * @param userEmail  -> email del usuario
     * @return Boolean true/false
     * */
    Boolean existsByUserEmail(String userEmail);

    /*
     * Metodo para verificar la existencia de un usuario basándonos en el username
     * @param String userName
     * @return Boolean true/false
     * */
    Boolean existsByUserName(String userName);

    /*
     * Metodo personalizado para encontrar usuario activos basandonos en el email
     * @param String email
     * @return Object User
     * */
    @Query("SELECT u from User u WHERE u.userEmail = :email AND u.userStatus = true")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

}
