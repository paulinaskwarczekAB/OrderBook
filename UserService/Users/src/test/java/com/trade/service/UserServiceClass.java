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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        user = new User("trader01", "trader@test.com", "hashed", "John", "Smith", UserRole.TRADER);

        userDTO = new UserDTO(1L, "trader01", "trader@test.com",
                "John", "Smith", UserRole.TRADER, LocalDateTime.now());

        registerDTO = new RegisterDTO();
        loginDTO = new LoginDTO();
    }

    @Test
    @DisplayName("register — success")
    void register_success() throws Exception {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userMapper.toUser(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(jwtService.generateToken(any(), any())).thenReturn("jwt-token");
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        AuthResponseDTO result = userService.register(registerDTO);

        assertThat(result.getToken()).isEqualTo("jwt-token");
        assertThat(result.getTokenType()).isEqualTo("Bearer");
        assertThat(result.getUser()).isEqualTo(userDTO);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("register — throws when username taken")
    void register_throws_whenUsernameTaken() {
        when(userRepository.existsByUsername(any())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(registerDTO))
                .isInstanceOf(Exception.class)
                .hasMessage("This username is already taken");
    }

    @Test
    @DisplayName("register — throws when email taken")
    void register_throws_whenEmailTaken() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(registerDTO))
                .isInstanceOf(Exception.class)
                .hasMessage("This email is already in use");
    }

    @Test
    @DisplayName("login — success")
    void login_success() throws Exception {
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any(), any())).thenReturn("jwt-token");
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        AuthResponseDTO result = userService.login(loginDTO);

        assertThat(result.getToken()).isEqualTo("jwt-token");
        verify(jwtService).generateToken(user.getUsername(), user.getRole().name());
    }

    @Test
    @DisplayName("login — throws when user not found")
    void login_throws_whenUserNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(() -> userService.login(loginDTO))
                .isInstanceOf(Exception.class)
                .hasMessage("Invalid username");
    }

    @Test
    @DisplayName("login — throws when password wrong")
    void login_throws_whenPasswordWrong() {
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> userService.login(loginDTO))
                .isInstanceOf(Exception.class)
                .hasMessage("Invalid password");
    }

    @Test
    @DisplayName("getUserById — success")
    void getUserById_success() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertThat(result).isEqualTo(userDTO);
    }

    @Test
    @DisplayName("getUserById — throws when not found")
    void getUserById_throws_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(Exception.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("getAllUsers — returns mapped list")
    void getAllUsers_returnsMappedList() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(userDTO);
    }
}