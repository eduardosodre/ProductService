package br.com.school.product.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/test")
public class RestTest {

    @GetMapping
    public String test(){
        return "returning test";
    }
}
