package com.tanji.testapi.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test")
    public String test(Principal principal) {
        String name = principal.getName();
        System.out.println(name);

//        memberCommandService.createMember(member); // MemberService를 통해 회원 생성
        return "hi";
    }
}
