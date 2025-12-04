package com.gdg.team5.scrap.controller;

import com.gdg.team5.auth.domain.CustomUserDetails;
import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.scrap.domain.ScrapType;
import com.gdg.team5.scrap.dto.ScrapResponseDto;
import com.gdg.team5.scrap.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    // 스크랩 저장
    // 이메일에선 GET만 가능 -> 링크 삽입 방식
    @GetMapping("/save")
    public ResponseEntity<String> saveScrap(@RequestParam Long userId,
                                            @RequestParam ScrapType type,
                                            @RequestParam Long contentId) {
        // 저장
        scrapService.saveScrap(userId, type, contentId);
        // 정상 저장 확인 문구
        // 확인 누르면 원래 페이지로 돌아감
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>저장 완료</title>
                </head>
                <body onload="closeWindow()">
                    <div style="text-align: center; margin-top: 100px;">
                        <h3>✅ 스크랩이 완료되었습니다.</h3>
                        <p>창이 자동으로 닫히지 않으면<br>아래 버튼을 눌러주세요.</p>
                        <button onclick="closeWindow()" style="padding: 10px 20px; font-size: 16px; cursor: pointer;">창 닫기</button>
                    </div>

                    <script>
                        function closeWindow() {
                            window.opener = null;
                            window.open('', '_self');
                            window.close();

                            history.go(-1);
                        }

                        alert('✅ 스크랩이 완료되었습니다!');
                        closeWindow();
                    </script>
                </body>
                </html>
                """;
        // HTML을 띄우려면 BaseResponse가 아닌 ResponseEntity 사용이 필수라서 예외적으로 사용
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }

    // 스크랩 목록 조회
    @GetMapping("")
    public BaseResponse<List<ScrapResponseDto>> getScraps(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(scrapService.getScraps(userDetails.getId()));
    }

    // 스크랩 삭제
    @DeleteMapping("/{scrapId}")
    public BaseResponse<String> deleteScrap(@PathVariable Long scrapId) {
        scrapService.deleteScrap(scrapId);
        return new BaseResponse<>("스크랩 삭제 성공");
    }
}
