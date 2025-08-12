package com.kako.onestep.explore.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kako.onestep.explore.dto.FeishuMessage;
import com.kako.onestep.explore.service.FeishuBotService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeishuBotServiceImpl implements FeishuBotService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${feishu.bot.webhook.url:}")
    private String webhookUrl;

    @Value("${feishu.bot.app.id:}")
    private String appId;

    @Value("${feishu.bot.app.secret:}")
    private String appSecret;

    public FeishuBotServiceImpl() {
        this.httpClient =
                new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
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

    @Override
    public boolean sendTextMessage(String text) {
        FeishuMessage message = FeishuMessage.createTextMessage(text);
        return sendMessageViaWebhook(message);
    }

    @Override
    public boolean sendRichTextMessage(List<FeishuMessage.Element> elements) {
        FeishuMessage message = FeishuMessage.createRichTextMessage(elements);
        return sendMessageViaWebhook(message);
    }

    @Override
    public boolean sendCardMessage(String title, String content) {
        String cardText = String.format("【%s】\n%s", title, content);
        return sendTextMessage(cardText);
    }
}
