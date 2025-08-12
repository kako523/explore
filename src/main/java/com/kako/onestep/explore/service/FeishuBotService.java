package com.kako.onestep.explore.service;

import com.kako.onestep.explore.dto.FeishuMessage;
import java.util.List;

public interface FeishuBotService {

    boolean sendMessageViaWebhook(FeishuMessage message);

    boolean sendTextMessage(String text);

    boolean sendRichTextMessage(List<FeishuMessage.Element> elements);

    boolean sendCardMessage(String title, String content);
}
