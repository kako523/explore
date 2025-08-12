package com.kako.onestep.explore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kako.onestep.explore.dto.FeishuMessage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeishuBotService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${feishu.bot.webhook.url:}")
    private String webhookUrl;

    @Value("${feishu.bot.app.id:}")
    private String appId;

    @Value("${feishu.bot.app.secret:}")
    private String appSecret;

    public FeishuBotService() {
        this.httpClient =
                new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 通过Webhook发送消息
     *
     * @param message 消息内容
     * @return 是否发送成功
     */
    public boolean sendMessageViaWebhook(FeishuMessage message) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.error("飞书机器人Webhook URL未配置");
            return false;
        }

        try {
            String jsonBody = objectMapper.writeValueAsString(message);

            RequestBody body =
                    RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder().url(webhookUrl).post(body).build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    log.info("飞书消息发送成功");
                    return true;
                } else {
                    log.error(
                            "飞书消息发送失败，状态码: {}, 响应: {}",
                            response.code(),
                            response.body() != null ? response.body().string() : "");
                    return false;
                }
            }
        } catch (IOException e) {
            log.error("发送飞书消息时发生异常", e);
            return false;
        }
    }

    /**
     * 发送简单文本消息
     *
     * @param text 文本内容
     * @return 是否发送成功
     */
    public boolean sendTextMessage(String text) {
        FeishuMessage message = FeishuMessage.createTextMessage(text);
        return sendMessageViaWebhook(message);
    }

    /**
     * 发送富文本消息
     *
     * @param elements 富文本元素列表
     * @return 是否发送成功
     */
    public boolean sendRichTextMessage(java.util.List<FeishuMessage.Element> elements) {
        FeishuMessage message = FeishuMessage.createRichTextMessage(elements);
        return sendMessageViaWebhook(message);
    }

    /**
     * 发送卡片消息（简单实现）
     *
     * @param title 卡片标题
     * @param content 卡片内容
     * @return 是否发送成功
     */
    public boolean sendCardMessage(String title, String content) {
        // 这里可以实现卡片消息的发送逻辑
        // 由于卡片消息比较复杂，这里先用文本消息代替
        String cardText = String.format("【%s】\n%s", title, content);
        return sendTextMessage(cardText);
    }
}
