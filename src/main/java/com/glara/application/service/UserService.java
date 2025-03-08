package com.glara.application.service;


import com.glara.application.dto.UserDTO;
import com.glara.domain.entity.User;
import com.glara.infrastructure.persistence.repository.UserRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public Uni<User> getUser(Long id) {
        return userRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Usuario no encontrado"));
    }

    public Uni<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Uni<List<UserDTO>> getAllUsers() {
        return userRepository.findAll();
    }

    public Uni<Void> createUser(User usuario) {
        return userRepository.persist(usuario);
    }

    public Uni<User> updateUser(User usuario, Long id) {
        return userRepository.update(usuario, id);
    }

    public Uni<Integer> deleteUserById(Long id) {
        return userRepository.deleteById(id);
    }
}
