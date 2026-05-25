package co.edu.ucompensar.parcial.service;

import co.edu.ucompensar.parcial.model.User;
import co.edu.ucompensar.parcial.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceUser {

    private final IUserRepository userRepository;

    public ServiceUser(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Valida credenciales. Retorna el usuario si son correctas, o vacío si no.
     */
    public Optional<User> login(String username, String password) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    /**
     * Registra un nuevo usuario. Lanza excepción si el username ya existe.
     */
    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El usuario '" + username + "' ya está registrado.");
        }
        return userRepository.save(new User(username, password));
    }
}
