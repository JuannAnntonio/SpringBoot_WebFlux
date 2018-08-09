package mx.gob.sat.disolucion.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import mx.gob.sat.disolucion.model.User;

@Repository("userRepository")
public interface UserRepository extends ReactiveMongoRepository<User, String> {

	@Query("{ 'edad' : ?0 }")
	List<User> busquedaXEdad(Integer edad);

}
