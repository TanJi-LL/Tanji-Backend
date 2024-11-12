package com.tanji.mailapi.application;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.History;
import com.google.api.services.gmail.model.ListHistoryResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberCommandService;
import com.tanji.mailapi.exception.MailCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.tanji.mailapi.exception.MailErrorCode.INSUFFICIENT_PERMISSION;
import static com.tanji.mailapi.exception.MailErrorCode.MAIL_FETCH_FAILED;

@Slf4j
@RequiredArgsConstructor
@Service
public class GmailFetchService {

    private final GmailService gmailService;
    private final MemberCommandService memberCommandService;
//    private static final String TRASH_LABEL = "TRASH";

    public int getTrashHistoryCount(Member member) throws IOException, GeneralSecurityException, MailCustomException {
        Gmail gmail = gmailService.getGmailService(member);
        BigInteger lastHistoryId = member.getLastHistoryId();

        try {
            ListHistoryResponse messagesResponse = fetchHistoryResponse(gmail, lastHistoryId);
            List<History> histories = messagesResponse.getHistory();

            if (histories == null) { // 변경 사항이 없는 경우
                return 0;
            }

            // 최신 historyId 업데이트
            BigInteger latestHistoryId = messagesResponse.getHistoryId();
            memberCommandService.updateLastHistoryId(member, latestHistoryId);

            return histories.size();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 400 || e.getStatusCode() == 404) { // historyId가 유효하지 않거나 null인 경우
                BigInteger newHistoryId = getLatestHistoryId(gmail); // 가장 최근 historyId로 업데이트
                member.updateLastHistoryId(newHistoryId.add(BigInteger.ONE));
                return 0;
            } else if (e.getStatusCode() == 403) { // 지메일 접근 권한 허용하지 않은 멤버인 경우
                log.error("gmail 권한 에러: {}", e.getMessage());
                throw new MailCustomException(INSUFFICIENT_PERMISSION);
            }
            else {
                log.error("gmail 예상치 못한 에러: {}", e.getMessage());
                throw new MailCustomException(MAIL_FETCH_FAILED);
            }
        }
    }

    private static ListHistoryResponse fetchHistoryResponse(Gmail gmail, BigInteger lastHistoryId) throws IOException {
        return gmail.users()
                .history()
                .list("me")
                .setStartHistoryId(lastHistoryId) // 시작 아이디 설정
                .setHistoryTypes(List.of("messageDeleted"))
                .execute();
    }

//    /**
//     * trash label이 붙은 history 카운트
//     */
//    private static long countTrashLabelAdded(List<History> histories) {
//        return histories.stream()
//                .filter(history -> history.getLabelsAdded() != null &&
//                        history.getLabelsAdded().stream()
//                                .anyMatch(labelAdded -> labelAdded.getLabelIds().contains(TRASH_LABEL))
//                )
//                .count();
//    }

    /**
     * 가장 최근 메일의 historyId 조회
     */
    private BigInteger getLatestHistoryId(Gmail gmail) throws IOException {
        // 가장 최신 메일 조회
        ListMessagesResponse messagesResponse = gmail.users()
                .messages()
                .list("me")
                .setMaxResults(1L) // 가장 최신 메일 1건 조회
                .execute();

        List<Message> messages = messagesResponse.getMessages();

        if (messages == null || messages.isEmpty()) {
            return BigInteger.valueOf(0); // 메일이 없는 경우
        }

        // 최신 메일 ID를 기반으로 메일 정보 조회
        Message latestMessage = gmail.users().messages().get("me", messages.getFirst().getId()).execute();

        return fetchHistoryResponse(gmail, latestMessage.getHistoryId())
                .getHistoryId();
    }
}