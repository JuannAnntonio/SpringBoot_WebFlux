package com.jnpr.ms.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jnpr.ms.model.User;
import com.jnpr.ms.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@Api(value = "Users microservice")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/all")
	@ApiOperation(value = "Find all user", notes = "Return all users")
	public Flux<User> getAllUsers() {
		return userRepo.findAll();
	}

	@PostMapping("/new")
	public Mono<User> createUser(@Valid @RequestBody User user) {
		return userRepo.save(user);
	}

	@GetMapping("/search/{id}")
	public Mono<ResponseEntity<User>> getUserById(@PathVariable(value = "id") String userId) {
		return userRepo.findById(userId).map(savedTweet -> ResponseEntity.ok(savedTweet))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/update/{id}")
	public Mono<ResponseEntity<User>> updateName(@PathVariable(value = "id") String userId,
			@Valid @RequestBody User user) {
		return userRepo.findById(userId).flatMap(existingUser -> {
			existingUser.setNombre(user.getNombre());
			return userRepo.save(existingUser);
		}).map(updatedTweet -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/delete/{id}")
	public Mono<ResponseEntity<Void>> deleteUser(@PathVariable(value = "id") String userId) {
		return userRepo.findById(userId).flatMap(
				existingUser -> userRepo.delete(existingUser).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/stream/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<User> streamAllUsers() {
		return userRepo.findAll();
	}

}
