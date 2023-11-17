package com.stit.controller;

import com.stit.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping()
    public ApiResponse doTest() throws Exception {
			double r = Math.random() * 100;
      return ApiResponse.ok("Hello-" + r);
    }

} // end class
