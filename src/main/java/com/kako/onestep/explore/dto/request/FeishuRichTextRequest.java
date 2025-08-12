package com.kako.onestep.explore.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "FeishuRichTextRequest", description = "发送飞书富文本消息的请求体（符合飞书post规范）")
public class FeishuRichTextRequest {

    @Schema(description = "富文本标题", example = "系统通知")
    private String title;

    @ArraySchema(arraySchema = @Schema(description = "富文本内容行（二维数组：每行包含多个元素）"))
    @NotEmpty(message = "content不能为空")
    @Valid
    private List<List<Element>> content;

    @Data
    @Schema(name = "Element", description = "富文本元素")
    public static class Element {
        @Schema(description = "元素类型：text(文本)、a(链接)", example = "text")
        @NotEmpty(message = "tag不能为空")
        private String tag;

        @Schema(description = "文本内容", example = "这是文本内容")
        private String text;

        @Schema(description = "链接地址（当tag为a时必填）", example = "https://www.feishu.cn")
        private String href;
    }
}
