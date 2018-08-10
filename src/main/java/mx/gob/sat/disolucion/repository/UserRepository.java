package mx.gob.sat.disolucion.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import mx.gob.sat.disolucion.model.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

	@Query("{ 'edad' : ?0 }")
	List<User> busquedaXEdad(Integer edad);

}
