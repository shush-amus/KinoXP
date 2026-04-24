package com.example.kinoxp.dto;

import java.util.List;

public record AuthResponse(
        boolean authenticated,
        String username,
        List<String> roles
) {
}
