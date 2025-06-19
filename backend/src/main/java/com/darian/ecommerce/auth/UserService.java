package com.darian.ecommerce.auth;

import com.darian.ecommerce.auth.dto.LoginDTO;
import com.darian.ecommerce.auth.dto.UserDTO;
import com.darian.ecommerce.auth.entity.User;

public interface UserService {
    // Register a new user
    UserDTO register(UserDTO userDTO);

    // Login user
    UserDTO login(LoginDTO loginDTO);

    // Check if username exists
    Boolean existedByUsername(String username);

    // Check if email exists
    Boolean existedByEmail(String email);

    // Get user by ID
    User getUserById(Integer userId);

}
