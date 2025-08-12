package com.kako.onestep.explore;

import com.kako.onestep.explore.dto.FeishuMessage;
import com.kako.onestep.explore.service.FeishuBotService;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;

/*
 飞书机器人测试类
 这个类会在应用启动时自动运行，用于测试飞书机器人功能
 注意：需要先在application.properties中配置正确的webhook URL
*/

/**
 * springboot自动装配机制： 1. 实现了CommandLineRunner接口，springboot在启动完成后，会自动调用所有实现了这个接口的run方法 2.
 * component注解：@Component 注解告诉Spring Boot这是一个需要管理的组件 Spring Boot会自动扫描并创建这个类的实例 这个注解让Spring
 * Boot知道这个类需要被"自动装配"
 */
// @Component
public class FeishuBotTest implements CommandLineRunner {

    private final FeishuBotService feishuBotService;

    public FeishuBotTest(FeishuBotService feishuBotService) {
        this.feishuBotService = feishuBotService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 测试发送文本消息
        System.out.println("=== 测试飞书机器人功能 ===");

        // 发送简单文本消息
        boolean textResult = feishuBotService.sendTextMessage("Hello! 这是来自Spring Boot应用的测试消息！");
        System.out.println("文本消息发送结果: " + (textResult ? "成功" : "失败"));

        // 发送富文本消息
        FeishuMessage.Element element1 =
                FeishuMessage.Element.builder().tag("text").text("这是富文本消息的第一行\n").build();

        FeishuMessage.Element element2 =
                FeishuMessage.Element.builder()
                        .tag("a")
                        .text("点击这里")
                        .href("https://www.feishu.cn")
                        .build();

        boolean richTextResult =
                feishuBotService.sendRichTextMessage(Arrays.asList(element1, element2));
        System.out.println("富文本消息发送结果: " + (richTextResult ? "成功" : "失败"));

        // 发送卡片消息
        boolean cardResult = feishuBotService.sendCardMessage("测试卡片", "这是一个测试卡片消息的内容");
        System.out.println("卡片消息发送结果: " + (cardResult ? "成功" : "失败"));

        System.out.println("=== 测试完成 ===");
    }
}
