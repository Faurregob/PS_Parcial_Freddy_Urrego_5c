package co.edu.ucompensar.parcial;

import co.edu.ucompensar.parcial.model.User;
import co.edu.ucompensar.parcial.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Carga un usuario de prueba al arrancar la aplicación
 * (solo si no existe en la base de datos).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final IUserRepository userRepository;

    public DataInitializer(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new User("admin", "admin123"));
            System.out.println("✅ Usuario de prueba creado → usuario: admin | contraseña: admin123");
        }
    }
}
