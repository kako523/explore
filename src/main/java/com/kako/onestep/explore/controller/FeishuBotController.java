package com.kako.onestep.explore.controller;

import com.kako.onestep.explore.dto.FeishuMessage;
import com.kako.onestep.explore.service.FeishuBotService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/feishu/bot")
@RequiredArgsConstructor
public class FeishuBotController {

    private final FeishuBotService feishuBotService;

    /** 发送文本消息 */
    @PostMapping("/send/text")
    public ResponseEntity<Map<String, Object>> sendTextMessage(
            @RequestBody Map<String, String> request) {
        String text = request.get("text");
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "文本内容不能为空"));
        }

        boolean success = feishuBotService.sendTextMessage(text);
        return ResponseEntity.ok(
                Map.of("success", success, "message", success ? "消息发送成功" : "消息发送失败"));
    }

    /** 发送富文本消息 */
    @PostMapping("/send/richtext")
    public ResponseEntity<Map<String, Object>> sendRichTextMessage(
            @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> elementsData =
                    (List<Map<String, String>>) request.get("elements");

            if (elementsData == null || elementsData.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "富文本元素不能为空"));
            }

            List<FeishuMessage.Element> elements =
                    elementsData.stream()
                            .map(
                                    elementData ->
                                            FeishuMessage.Element.builder()
                                                    .tag(elementData.get("tag"))
                                                    .text(elementData.get("text"))
                                                    .href(elementData.get("href"))
                                                    .build())
                            .collect(Collectors.toList());

            boolean success = feishuBotService.sendRichTextMessage(elements);
            return ResponseEntity.ok(
                    Map.of("success", success, "message", success ? "富文本消息发送成功" : "富文本消息发送失败"));
        } catch (Exception e) {
            log.error("发送富文本消息时发生异常", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "请求格式错误: " + e.getMessage()));
        }
    }

    /** 发送卡片消息 */
    @PostMapping("/send/card")
    public ResponseEntity<Map<String, Object>> sendCardMessage(
            @RequestBody Map<String, String> request) {
        String title = request.get("title");
        String content = request.get("content");

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "卡片标题不能为空"));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "卡片内容不能为空"));
        }

        boolean success = feishuBotService.sendCardMessage(title, content);
        return ResponseEntity.ok(
                Map.of("success", success, "message", success ? "卡片消息发送成功" : "卡片消息发送失败"));
    }

    /** 发送自定义消息 */
    @PostMapping("/send/custom")
    public ResponseEntity<Map<String, Object>> sendCustomMessage(
            @RequestBody FeishuMessage message) {
        if (message == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "消息内容不能为空"));
        }

        boolean success = feishuBotService.sendMessageViaWebhook(message);
        return ResponseEntity.ok(
                Map.of("success", success, "message", success ? "自定义消息发送成功" : "自定义消息发送失败"));
    }

    /** 健康检查接口 */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(
                Map.of(
                        "status", "UP",
                        "service", "Feishu Bot Service",
                        "timestamp", System.currentTimeMillis()));
    }
}
