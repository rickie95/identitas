package it.riccardomalavolti.identitas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import it.riccardomalavolti.identitas.model.UserCredentials;

/** Manages UserCredentials */
public interface UserCredentialRepository extends JpaRepository<UserCredentials, String> {
    
}
