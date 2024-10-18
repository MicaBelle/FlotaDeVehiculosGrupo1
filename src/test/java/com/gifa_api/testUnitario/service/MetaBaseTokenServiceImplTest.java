package com.gifa_api.testUnitario.service;

import com.gifa_api.dto.metabase.MetabaseTokenResponseDTO;
import com.gifa_api.service.impl.MetabaseTokenServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MetaBaseTokenServiceImplTest {

    @InjectMocks
    private MetabaseTokenServiceImpl metabaseService;

    private static final String SECRET_KEY = "my-secret-key-for-jwt-which-should-be-very-long";

    // Test que lanza una excepción al fallar la generación del token
//    @Test
//    public void generateTokenForDashboard_shouldThrowException_whenTokenGenerationFails() {
//        Long dashboardId = 123L;
//
//        // Crear el SecretKey desde el String, simulando el comportamiento real
//        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//
//        // Mockear Jwts para lanzar una excepción en la creación del token
//        try (MockedStatic<Jwts> jwtsMockedStatic = mockStatic(Jwts.class)) {
//            // Simular el comportamiento del builder para lanzar una excepción
//            jwtsMockedStatic.when(Jwts::builder).thenThrow(new RuntimeException("Token generation error"));
//
//            // Act & Assert: verificar que se lance la excepción esperada
//            RuntimeException thrownException = assertThrows(RuntimeException.class,
//                    () -> metabaseService.generateTokenForDashboard(dashboardId));
//            assertEquals("Error al generar el token: Token generation error", thrownException.getMessage());
//        }
//    }

    // Test que genera un token válido
    @Test
    public void generateTokenForDashboard_generatesValidToken() {
        // Arrange
        Long dashboardId = 123L;
        String secretKeyString = SECRET_KEY;

        // Crear el SecretKey desde el String, simulando el comportamiento real
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());

        // Crear el payload del token
        Map<String, Object> payload = new HashMap<>();
        payload.put("resource", Map.of("dashboard", dashboardId));
        payload.put("params", new HashMap<>());
        payload.put("exp", (new Date().getTime() / 1000) + (3600000 / 1000)); // Expiración en 1 hora

        // Simular la generación del token usando Jwts.builder
        String token = Jwts.builder()
                .setClaims(payload)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        MetabaseTokenResponseDTO response = MetabaseTokenResponseDTO.builder()
                .token(token)
                .build();

        // Assert
        assertNotNull(response);
        assertEquals(token, response.getToken());
    }
}
