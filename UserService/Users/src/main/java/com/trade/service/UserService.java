package com.trade.service;

import com.trade.dto.AuthResponseDTO;
import com.trade.dto.LoginDTO;
import com.trade.dto.RegisterDTO;
import com.trade.dto.UserDTO;
import com.trade.mapper.UserMapper;
import com.trade.model.User;
import com.trade.model.UserRole;
import com.trade.repository.UserRepository;
import com.trade.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //register
    public AuthResponseDTO register(RegisterDTO userDTO) throws Exception {
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            throw new Exception("This username is already taken");
        }
        if(userRepository.existsByEmail(userDTO.getEmail())) {
            throw new Exception("This email is already in use");
        }

        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (user.getRole() == null) user.setRole(UserRole.TRADER);
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        userRepository.save(user);
        logger.info("User {} registered successfully", userDTO.getUsername());
        return new AuthResponseDTO(token, userMapper.toUserDTO(user));
    }

    //login
    public AuthResponseDTO login(LoginDTO userDTO) throws Exception {
        User user = userRepository.findByUsername(userDTO.getUsername());
        if(user == null) {
            throw new Exception("Invalid username");
        }
        if(!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password");
        }

        logger.info("User {} logged in successfully", userDTO.getUsername());
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponseDTO(token, userMapper.toUserDTO(user));
    }

    //get
    public UserDTO getUserById(Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        return userMapper.toUserDTO(user);
    }

    //getAll
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDTO).toList();
    }

}
