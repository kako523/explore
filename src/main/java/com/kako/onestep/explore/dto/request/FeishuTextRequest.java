package com.kako.onestep.explore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "FeishuTextRequest", description = "发送飞书文本消息的请求体")
public class FeishuTextRequest {

    @Schema(description = "文本内容", example = "Hello Feishu!")
    @NotBlank(message = "text不能为空")
    private String text;
}
