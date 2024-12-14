package com.example.ztp2;

import com.example.ztp2.model.ProductModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProductService {
    private static final String API_URL = "http://localhost:8080/api/products";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ProductService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<ProductModel> getAllProducts() throws Exception {
        final var request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<ProductModel> products = objectMapper.readValue(response.body(), new TypeReference<>() {});
        return products;
    }

    public ProductModel getProductById(Long productId) throws Exception {
        URI uri = new URI(API_URL + "/" + productId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to fetch product with ID: " + productId);
        }

        return objectMapper.readValue(response.body(), ProductModel.class);
    }

    public void addProduct(ProductRequest productRequest) throws Exception {
        URI uri = new URI(API_URL);

        String productJson = objectMapper.writeValueAsString(productRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(productJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            throw new Exception("Failed to add product");
        }
    }

    public void deleteProduct(Long productId) throws Exception {
        URI uri = new URI(API_URL + "/" + productId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204) {
            throw new Exception("Failed to delete product");
        }
    }
}
