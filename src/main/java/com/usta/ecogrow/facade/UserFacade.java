package com.usta.ecogrow.facade;

import com.usta.ecogrow.dao.UserDao;
import com.usta.ecogrow.model.Role;
import com.usta.ecogrow.model.User;
import com.usta.ecogrow.security.PasswordHasher;
import com.usta.ecogrow.util.ValidationException;

import java.util.regex.Pattern;

/**
 * Patrón Facade: la interfaz solo conoce estas operaciones (autenticar, registrar, administrar usuarios)
 * y no los detalles de DAO, validación ni hash de contraseñas.
 */
public class UserFacade {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final UserDao users = new UserDao();

    public User login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            throw new ValidationException("Ingresa tu correo y tu contraseña.");
        }
        User user = users.findByEmail(email.trim().toLowerCase()).orElse(null);
        if (user == null) {
            PasswordHasher.burn(password);
            throw new ValidationException("Correo o contraseña incorrectos.");
        }
        if (!PasswordHasher.verify(password, user.passwordHash())) {
            throw new ValidationException("Correo o contraseña incorrectos.");
        }
        return user;
    }

    /** Registro público: solo se permiten los roles AUTHORITY y OWNER (el ADMIN lo crea otro admin). */
    public User register(String name, String email, String password, Role role, String document, String phone) {
        if (role == null || role == Role.ADMIN) {
            throw new ValidationException("Selecciona un tipo de usuario válido.");
        }
        return create(name, email, password, role, document, phone);
    }

    /** Alta o edición hecha por un administrador. rawPassword vacío en edición = no cambiarla. */
    public User saveAsAdmin(User user, String rawPassword) {
        boolean isNew = user.id() == 0;
        if (isNew) {
            return create(user.name(), user.email(), rawPassword, user.role(), user.document(), user.phone());
        }
        validateProfile(user.name(), user.email());
        String hash = "";
        if (rawPassword != null && !rawPassword.isEmpty()) {
            validatePassword(rawPassword);
            hash = PasswordHasher.hash(rawPassword);
        }
        return users.save(new User(user.id(), user.name().trim(), user.email().trim().toLowerCase(), hash,
                user.role(), user.document(), user.phone()));
    }

    public void deleteAsAdmin(User target, User actor) {
        if (target.id() == actor.id()) {
            throw new ValidationException("No puedes eliminar tu propia cuenta.");
        }
        if (target.role() == Role.ADMIN && users.countByRole(Role.ADMIN) <= 1) {
            throw new ValidationException("Debe existir al menos un administrador.");
        }
        users.delete(target.id());
    }

    public UserDao dao() {
        return users;
    }

    private User create(String name, String email, String password, Role role, String document, String phone) {
        validateProfile(name, email);
        validatePassword(password);
        String normalized = email.trim().toLowerCase();
        if (users.findByEmail(normalized).isPresent()) {
            throw new ValidationException("Ya existe una cuenta con ese correo.");
        }
        User user = new User(0, name.trim(), normalized, PasswordHasher.hash(password), role,
                document == null ? "" : document.trim(), phone == null ? "" : phone.trim());
        return users.save(user);
    }

    private static void validateProfile(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("El nombre es obligatorio.");
        }
        if (email == null || !EMAIL.matcher(email.trim()).matches()) {
            throw new ValidationException("Ingresa un correo electrónico válido.");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("La contraseña debe tener al menos 8 caracteres.");
        }
        boolean letter = password.chars().anyMatch(Character::isLetter);
        boolean digit = password.chars().anyMatch(Character::isDigit);
        if (!letter || !digit) {
            throw new ValidationException("La contraseña debe combinar letras y números.");
        }
    }
}
