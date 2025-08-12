package com.kako.onestep.explore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "FeishuCardRequest", description = "发送飞书卡片消息的请求体（简化）")
public class FeishuCardRequest {

    @Schema(description = "卡片标题", example = "系统通知")
    @NotBlank(message = "title不能为空")
    private String title;

    @Schema(description = "卡片内容", example = "服务已恢复")
    @NotBlank(message = "content不能为空")
    private String content;
}
