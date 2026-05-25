package co.edu.ucompensar.parcial.service;

import co.edu.ucompensar.parcial.model.User;
import co.edu.ucompensar.parcial.repository.IUserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceUser - Pruebas Unitarias")


public class ServiceUserTest {
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private ServiceUser serviceUser;

    // --- login() -------------------------------------------

    @Test
    @DisplayName("Login exitoso con credenciales correctas")
    void login_credencialesCorrectas_retornaUsuario() {
        User user = new User("admin", "admin123");
        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        Optional<User> result = serviceUser.login("admin", "admin123");

        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUsername());
    }

    @Test
    @DisplayName("Login fallido - contrasena incorrecta")
    void login_contrasenaIncorrecta_retornaVacio() {
        User user = new User("admin", "admin123");
        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        Optional<User> result = serviceUser.login("admin", "wrongPass");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Login fallido - usuario no existe")
    void login_usuarioNoExiste_retornaVacio() {
        when(userRepository.findByUsername("ghost"))
                .thenReturn(Optional.empty());

        Optional<User> result = serviceUser.login("ghost", "pass");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Login fallido - username nulo")
    void login_usernameNulo_retornaVacio() {
        Optional<User> result = serviceUser.login(null, "pass");
        assertTrue(result.isEmpty());
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Login fallido - username vacio")
    void login_usernameVacio_retornaVacio() {
        Optional<User> result = serviceUser.login("  ", "pass");
        assertTrue(result.isEmpty());
    }

    // --- register() ----------------------------------------

    @Test
    @DisplayName("Registro exitoso - usuario nuevo")
    void register_usuarioNuevo_guardaYRetorna() {
        when(userRepository.existsByUsername("nuevo")).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> i.getArgument(0));

        User result = serviceUser.register("nuevo", "pass1234");

        assertEquals("nuevo", result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Registro fallido - usuario duplicado")
    void register_usuarioDuplicado_lanzaExcepcion() {
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                serviceUser.register("admin", "pass"));

        verify(userRepository, never()).save(any());
    }



}
