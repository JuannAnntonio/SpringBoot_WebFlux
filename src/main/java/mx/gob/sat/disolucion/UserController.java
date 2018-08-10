package mx.gob.sat.disolucion;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import mx.gob.sat.disolucion.model.User;
import mx.gob.sat.disolucion.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/users")
	public Flux<User> getAllTweets() {
		return userRepo.findAll();
	}

	@PostMapping("/users")
	public Mono<User> createTweets(@Valid @RequestBody User user) {
		return userRepo.save(user);
	}

	@GetMapping("/users/{id}")
	public Mono<ResponseEntity<User>> getTweetById(@PathVariable(value = "id") String userId) {
		return userRepo.findById(userId).map(savedTweet -> ResponseEntity.ok(savedTweet))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/users/{id}")
	public Mono<ResponseEntity<User>> updateName(@PathVariable(value = "id") String userId,
			@Valid @RequestBody User user) {
		return userRepo.findById(userId).flatMap(existingUser -> {
			existingUser.setNombre(user.getNombre());
			return userRepo.save(existingUser);
		}).map(updatedTweet -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/users/{id}")
	public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String userId) {
		return userRepo.findById(userId).flatMap(
				existingUser -> userRepo.delete(existingUser).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/stream/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<User> streamAllTweets() {
		return userRepo.findAll();
	}

}
