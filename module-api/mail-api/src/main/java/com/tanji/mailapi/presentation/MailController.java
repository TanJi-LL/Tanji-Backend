package com.tanji.mailapi.presentation;

import com.tanji.authapi.utils.MemberUtil;
import com.tanji.mailapi.application.GmailFetchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;

@Tag(name = "메일 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mails")
public class MailController {

    private final GmailFetchService gmailFetchService;
//    private final MemberQueryService memberQueryService;

    @Operation(summary = "삭제한 메일 수 조회1 (테스트용)")
    @GetMapping("/test/trash")
    public long getTrashCount(Principal principal) throws GeneralSecurityException, IOException {
        Long memberId = MemberUtil.getMemberId(principal);
        return gmailFetchService.getTrashHistoryCount(memberId);
    }
}