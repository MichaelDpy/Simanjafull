package com.simanja.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * ApiClient — Utility class untuk HTTP REST API calls ke backend.
 * Integrasi HttpClient bawaan Java 11+ dengan Jackson JSON parser.
 */
public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // ObjectMapper singleton dikonfigurasi untuk mensupport tipe Date Java 8
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static HttpRequest.Builder buildRequest(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");

        String token = SessionManager.getInstance().getJwtToken();
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }
        return builder;
    }

    // =============================================
    // GET
    // =============================================

    public static <T> T get(String path, TypeReference<T> typeRef) {
        try {
            HttpRequest request = buildRequest(path).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (isSuccess(response.statusCode())) {
                return mapper.readValue(response.body(), typeRef);
            } else {
                throw new RuntimeException(parseErrorMessage(response.body()));
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghubungi server: " + e.getMessage(), e);
        }
    }

    public static <T> T get(String path, Class<T> clazz) {
        try {
            HttpRequest request = buildRequest(path).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (isSuccess(response.statusCode())) {
                return mapper.readValue(response.body(), clazz);
            } else {
                throw new RuntimeException(parseErrorMessage(response.body()));
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghubungi server: " + e.getMessage(), e);
        }
    }

    // =============================================
    // POST
    // =============================================

    public static <T> T post(String path, Object body, Class<T> clazz) {
        try {
            String jsonBody = mapper.writeValueAsString(body);
            HttpRequest request = buildRequest(path)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (isSuccess(response.statusCode())) {
                if (clazz == Void.class) return null;
                return mapper.readValue(response.body(), clazz);
            } else {
                throw new RuntimeException(parseErrorMessage(response.body()));
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghubungi server: " + e.getMessage(), e);
        }
    }

    // =============================================
    // PUT
    // =============================================

    public static <T> T put(String path, Object body, Class<T> clazz) {
        try {
            String jsonBody = mapper.writeValueAsString(body);
            HttpRequest request = buildRequest(path)
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (isSuccess(response.statusCode())) {
                if (clazz == Void.class) return null;
                return mapper.readValue(response.body(), clazz);
            } else {
                throw new RuntimeException(parseErrorMessage(response.body()));
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghubungi server: " + e.getMessage(), e);
        }
    }

    // =============================================
    // DELETE
    // =============================================

    public static void delete(String path) {
        try {
            HttpRequest request = buildRequest(path).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!isSuccess(response.statusCode())) {
                throw new RuntimeException(parseErrorMessage(response.body()));
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghubungi server: " + e.getMessage(), e);
        }
    }

    // =============================================
    // HELPERS
    // =============================================

    private static boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private static String parseErrorMessage(String body) {
        try {
            // Coba parsing struktur JSON dari exception handler backend
            JsonNode node = mapper.readTree(body);
            if (node.has("message")) return node.get("message").asText();
            if (node.has("error")) return node.get("error").asText();
            return body;
        } catch (Exception e) {
            return body; // Jika bukan JSON
        }
    }
}
