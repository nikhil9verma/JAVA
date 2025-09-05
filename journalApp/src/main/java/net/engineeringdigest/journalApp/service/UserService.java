package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.Repository.JournalEntryRepo;
import net.engineeringdigest.journalApp.Repository.UserRepo;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired  // Inject as Spring bean instead of static field
    private PasswordEncoder passwordEncoder;

    // Create new user with encoded password and default role
    public void saveEntry(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ensure roles are always set
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole(Arrays.asList("USER"));
        }

        userRepo.save(user);
    }

    // Alternative method for user creation (consistent with saveEntry)
    public void saveNewUser(User user) {
        saveEntry(user); // Reuse the main save logic
    }

    // Update existing user with proper password handling
    public User updateUser(String username, User updatedUser) {
        User existingUser = findUserByUsername(username);

        if (existingUser == null) {
            throw new RuntimeException("User not found: " + username);
        }

        // Update username if provided
        if (updatedUser.getUserName() != null && !updatedUser.getUserName().isEmpty()) {
            existingUser.setUserName(updatedUser.getUserName());
        }

        // Update password if provided (encode it)
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        // Update roles if provided
        if (updatedUser.getRole() != null && !updatedUser.getRole().isEmpty()) {
            existingUser.setRole(updatedUser.getRole());
        }

        return userRepo.save(existingUser);
    }

    // Save user without encoding password (for internal use)
    public void saveUserWithoutEncoding(User user) {
        userRepo.save(user);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepo.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepo.deleteById(id);
    }

    public User findUserByUsername(String username) {
        return userRepo.findByUserName(username);
    }

    // Check if user exists
    public boolean existsByUsername(String username) {
        return findUserByUsername(username) != null;
    }

    // Validate user data
    private void validateUser(User user) {
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        if (user.getPassword().length() < 3) {
            throw new RuntimeException("Password must be at least 3 characters long");
        }
    }

    // Create user with validation
    public void createUser(User user) {
        validateUser(user);

        if (existsByUsername(user.getUserName())) {
            throw new RuntimeException("Username already exists: " + user.getUserName());
        }

        saveEntry(user);
    }
}
