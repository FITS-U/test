package com.example.model.client;

import com.example.model.dto.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "category-service", url = "http://localhost:8085")
public interface CategoryClient {
    @GetMapping("api/v1/interest-category")
    List<CategoryResponse> getCategoryByUserId(@RequestHeader("Authorization") String authorization);
}