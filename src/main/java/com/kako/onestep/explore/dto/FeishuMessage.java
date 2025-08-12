package com.kako.onestep.explore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeishuMessage {

    @JsonProperty("msg_type")
    private String msgType;

    private Content content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String text;
        private List<Element> elements;
        private Post post;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Element {
        private String tag;
        private String text;
        private String href;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @JsonProperty("zh_cn")
        private ZhCn zhCn;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ZhCn {
            private String title;
            private List<List<Element>> content;
        }
    }

    // 创建文本消息的便捷方法
    public static FeishuMessage createTextMessage(String text) {
        return FeishuMessage.builder()
                .msgType("text")
                .content(Content.builder().text(text).build())
                .build();
    }

    // 创建富文本消息的便捷方法
    public static FeishuMessage createRichTextMessage(List<Element> elements) {
        return FeishuMessage.builder()
                .msgType("post")
                .content(Content.builder().elements(elements).build())
                .build();
    }
}
