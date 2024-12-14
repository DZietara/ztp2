package com.example.ztp2.model;

import com.example.ztp2.common.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProductService {
    private static final String API_URL = "http://localhost:8080/api/products";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final CacheService cacheService;

    public ProductService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.cacheService = CacheService.getInstance(15, TimeUnit.SECONDS);
    }

    public List<ProductModel> getAllProducts() throws Exception {
        String cacheKey = "allProducts";
        List<ProductModel> cachedProducts = cacheService.get(cacheKey, new TypeReference<List<ProductModel>>() {});
        if (cachedProducts != null) {
            return cachedProducts;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<ProductModel> products = objectMapper.readValue(response.body(), new TypeReference<>() {});

        cacheService.put(cacheKey, products);

        return products;
    }

    public ProductModel getProductById(Long productId) throws Exception {
        String cacheKey = "product_" + productId;
        ProductModel cachedProduct = cacheService.get(cacheKey, new TypeReference<ProductModel>() {});
        if (cachedProduct != null) {
            return cachedProduct;
        }

        URI uri = new URI(API_URL + "/" + productId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to fetch product with ID: " + productId);
        }

        ProductModel product = objectMapper.readValue(response.body(), ProductModel.class);

        cacheService.put(cacheKey, product);

        return product;
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

        cacheService.clear("allProducts");
    }

    public void updateProduct(Long productId, ProductRequest productRequest) throws Exception {
        URI uri = new URI(API_URL + "/" + productId);
        String productJson = objectMapper.writeValueAsString(productRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(productJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Failed to update product");
        }

        cacheService.clear("product_" + productId);
        cacheService.clear("allProducts");
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

        cacheService.clear("product_" + productId);
        cacheService.clear("allProducts");
    }
}
