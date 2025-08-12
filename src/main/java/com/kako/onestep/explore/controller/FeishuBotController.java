package com.kako.onestep.explore.controller;

import com.kako.onestep.explore.dto.FeishuMessage;
import com.kako.onestep.explore.dto.request.FeishuCardRequest;
import com.kako.onestep.explore.dto.request.FeishuRichTextRequest;
import com.kako.onestep.explore.dto.request.FeishuTextRequest;
import com.kako.onestep.explore.service.FeishuBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/feishu/bot")
@RequiredArgsConstructor
@Tag(name = "FeishuBot", description = "飞书机器人发送消息接口")
public class FeishuBotController {

    private final FeishuBotService feishuBotService;

    /** 发送文本消息 */
    @Operation(summary = "发送文本消息")
    @ApiResponse(
            responseCode = "200",
            description = "执行结果",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @PostMapping("/send/text")
    public ResponseEntity<Map<String, Object>> sendTextMessage(
            @Valid @RequestBody FeishuTextRequest request) {
        boolean success = feishuBotService.sendTextMessage(request.getText());
        return ResponseEntity.ok(
                Map.of("success", success, "message", success ? "消息发送成功" : "消息发送失败"));
    }

    /** 发送富文本消息 */
    @Operation(summary = "发送富文本消息")
    @PostMapping("/send/richtext")
    public ResponseEntity<Map<String, Object>> sendRichTextMessage(
            @Valid @RequestBody FeishuRichTextRequest request) {
        // 构造符合飞书post规范的富文本消息
        List<List<FeishuMessage.Element>> content =
                request.getContent().stream()
                        .map(
                                row ->
                                        row.stream()
                                                .map(
                                                        element ->
                                                                FeishuMessage.Element.builder()
                                                                        .tag(element.getTag())
                                                                        .text(element.getText())
                                                                        .href(element.getHref())
                                                                        .build())
                                                .collect(Collectors.toList()))
                        .collect(Collectors.toList());

        FeishuMessage message =
                FeishuMessage.builder()
                        .msgType("post")
                        .content(
                                FeishuMessage.Content.builder()
                                        .post(
                                                FeishuMessage.Post.builder()
                                                        .zhCn(
                                                                FeishuMessage.Post.ZhCn.builder()
                                                                        .title(request.getTitle())
                                                                        .content(content)
                                                                        .build())
                                                        .build())
                                        .build())
                        .build();

        boolean success = feishuBotService.sendMessageViaWebhook(message);
        return ResponseEntity.ok(
                Map.of("success", success, "message", success ? "富文本消息发送成功" : "富文本消息发送失败"));
    }

    /** 发送卡片消息 */
    @Operation(summary = "发送卡片消息")
    @PostMapping("/send/card")
    public ResponseEntity<Map<String, Object>> sendCardMessage(
            @Valid @RequestBody FeishuCardRequest request) {
        boolean success =
                feishuBotService.sendCardMessage(request.getTitle(), request.getContent());
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
