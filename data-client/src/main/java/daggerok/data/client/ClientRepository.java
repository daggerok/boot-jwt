package daggerok.data.client;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, Long> {
    Optional<Client> findByClientId(@Param("clientId") final String clientId);
}
