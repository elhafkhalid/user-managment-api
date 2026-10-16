package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserCreateRequest;
import com.example.usermanagement.dto.UserResponse;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.EmailAlreadyExistsException;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // ═══════════════════════════════════════════════
    //  FLUX 1 — Créer un utilisateur (POST /api/users)
    // ═══════════════════════════════════════════════

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {

        // 1. Vérifier unicité de l'email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email déjà utilisé : " + request.getEmail());
        }

        // 2. DTO → Entity
        User user = userMapper.toEntity(request);

        // 3. Hacher le mot de passe avec BCrypt
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 4. Sauvegarder
        User saved = userRepository.save(user);

        // 5. Entity → DTO (sans password)
        return userMapper.toResponse(saved);
    }


}