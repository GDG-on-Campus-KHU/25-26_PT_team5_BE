package com.gdg.team5.scrap.controller;

import com.gdg.team5.common.response.BaseResponse;
import com.gdg.team5.scrap.domain.ScrapType;
import com.gdg.team5.scrap.dto.ScrapResponseDto;
import com.gdg.team5.scrap.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    @GetMapping("/save")
    public ResponseEntity<String> saveScrap(@RequestParam Long userId,
                                            @RequestParam ScrapType type,
                                            @RequestParam Long contentId) {
        scrapService.saveScrap(userId, type, contentId);
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
        return ResponseEntity.ok()
            .header("Content-Type", "text/html; charset=UTF-8")
            .body(html);
    }

    @GetMapping("")
    public BaseResponse<List<ScrapResponseDto>> getScraps() {
        return new BaseResponse<>(scrapService.getScraps());
    }
}
