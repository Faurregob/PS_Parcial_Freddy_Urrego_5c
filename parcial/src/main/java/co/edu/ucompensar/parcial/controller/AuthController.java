package co.edu.ucompensar.parcial.controller;

import co.edu.ucompensar.parcial.model.User;
import co.edu.ucompensar.parcial.service.ServiceUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final ServiceUser serviceUser;

    public AuthController(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    // ──────────────────────────────────────────────
    //  GET /  →  redirige al login
    // ──────────────────────────────────────────────
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // ──────────────────────────────────────────────
    //  GET /login  →  muestra el formulario
    // ──────────────────────────────────────────────
    @GetMapping("/login")
    public String loginForm(HttpSession session, Model model) {
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    // ──────────────────────────────────────────────
    //  POST /login  →  valida credenciales
    // ──────────────────────────────────────────────
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        // Validaciones básicas de campos vacíos
        if (username == null || username.isBlank()) {
            model.addAttribute("error", "El campo usuario es obligatorio.");
            return "login";
        }
        if (password == null || password.isBlank()) {
            model.addAttribute("error", "El campo contraseña es obligatorio.");
            return "login";
        }

        Optional<User> user = serviceUser.login(username.trim(), password);

        if (user.isPresent()) {
            session.setAttribute("loggedUser", user.get().getUsername());
            return "redirect:/dashboard";
        } else {
            // Mensaje diferenciado: usuario existe o no
            model.addAttribute("error", "Usuario o contraseña incorrectos. Verifica tus datos e intenta de nuevo.");
            model.addAttribute("username", username); // mantiene el campo usuario lleno
            return "login";
        }
    }

    // ──────────────────────────────────────────────
    //  GET /dashboard  →  página protegida
    // ──────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String loggedUser = (String) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", loggedUser);
        return "dashboard";
    }

    // ──────────────────────────────────────────────
    //  GET /logout  →  cierra sesión
    // ──────────────────────────────────────────────
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
