package com.seohaeng.backend.global.test;

import com.seohaeng.backend.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class testContoller {

    @GetMapping("/api")
    public ApiResponse<String> ApiTest(){
        return ApiResponse.onSuccess("응답통일 Test");
    }
}
