# 飞书机器人使用指南

## 概述

这是一个基于Spring Boot的飞书机器人接口，支持发送文本消息、富文本消息和卡片消息。

## 功能特性

- ✅ 发送文本消息
- ✅ 发送富文本消息
- ✅ 发送卡片消息
- ✅ RESTful API接口
- ✅ 健康检查接口
- ✅ 自动测试功能

## 配置说明

### 1. 飞书机器人配置

在 `src/main/resources/application.properties` 中配置你的飞书机器人信息：

```properties
# 飞书机器人配置
feishu.bot.webhook.url=https://open.feishu.cn/open-apis/bot/v2/hook/your-webhook-url-here
feishu.bot.app.id=your-app-id-here
feishu.bot.app.secret=your-app-secret-here
```

### 2. 如何获取Webhook URL

1. 登录飞书开放平台：https://open.feishu.cn/
2. 创建应用或选择现有应用
3. 在应用功能中启用"机器人"功能
4. 获取Webhook URL

## API接口

### 1. 发送文本消息

**接口地址：** `POST /api/feishu/bot/send/text`

**请求示例：**
```json
{
    "text": "Hello, 这是一条测试消息！"
}
```

**响应示例：**
```json
{
    "success": true,
    "message": "消息发送成功"
}
```

### 2. 发送富文本消息

**接口地址：** `POST /api/feishu/bot/send/richtext`

**请求示例：**
```json
{
    "elements": [
        {
            "tag": "text",
            "text": "这是第一行文本\n"
        },
        {
            "tag": "a",
            "text": "点击这里",
            "href": "https://www.feishu.cn"
        }
    ]
}
```

### 3. 发送卡片消息

**接口地址：** `POST /api/feishu/bot/send/card`

**请求示例：**
```json
{
    "title": "通知标题",
    "content": "这是通知的详细内容"
}
```

### 4. 发送自定义消息

**接口地址：** `POST /api/feishu/bot/send/custom`

**请求示例：**
```json
{
    "msg_type": "text",
    "content": {
        "text": "自定义消息内容"
    }
}
```

### 5. 健康检查

**接口地址：** `GET /api/feishu/bot/health`

**响应示例：**
```json
{
    "status": "UP",
    "service": "Feishu Bot Service",
    "timestamp": 1703123456789
}
```

## 使用示例

### 使用curl测试

```bash
# 发送文本消息
curl -X POST http://localhost:8080/api/feishu/bot/send/text \
  -H "Content-Type: application/json" \
  -d '{"text": "Hello from curl!"}'

# 发送卡片消息
curl -X POST http://localhost:8080/api/feishu/bot/send/card \
  -H "Content-Type: application/json" \
  -d '{"title": "系统通知", "content": "服务器运行正常"}'

# 健康检查
curl http://localhost:8080/api/feishu/bot/health
```

### 使用Java代码

```java
@Autowired
private FeishuBotService feishuBotService;

// 发送文本消息
boolean success = feishuBotService.sendTextMessage("Hello World!");

// 发送富文本消息
List<FeishuMessage.Element> elements = Arrays.asList(
    FeishuMessage.Element.builder()
        .tag("text")
        .text("富文本内容\n")
        .build()
);
feishuBotService.sendRichTextMessage(elements);

// 发送卡片消息
feishuBotService.sendCardMessage("标题", "内容");
```

**包名说明：**
- Controller: `com.kako.onestep.explore.controller.FeishuBotController`
- Service: `com.kako.onestep.explore.service.FeishuBotService`
- DTO: `com.kako.onestep.explore.dto.FeishuMessage`

## 运行项目

### 环境要求
- Java 11 或更高版本
- Maven 3.6 或更高版本

### 启动步骤
1. 确保已配置正确的飞书机器人Webhook URL
2. 运行Spring Boot应用：
   ```bash
   mvn spring-boot:run
   ```
3. 应用启动后会自动运行测试，发送测试消息
4. 访问 `http://localhost:8080/api/feishu/bot/health` 检查服务状态

## 注意事项

1. **Webhook URL配置**：必须先在飞书开放平台获取正确的Webhook URL
2. **消息频率限制**：飞书对机器人消息发送有频率限制，请合理使用
3. **错误处理**：所有接口都有完善的错误处理，会返回详细的错误信息
4. **日志记录**：所有操作都会记录日志，便于调试和监控

## 故障排除

### 常见问题

1. **消息发送失败**
   - 检查Webhook URL是否正确
   - 确认飞书机器人是否已启用
   - 查看应用日志获取详细错误信息

2. **配置不生效**
   - 重启应用使配置生效
   - 检查配置文件格式是否正确

3. **网络连接问题**
   - 确认服务器能访问飞书API
   - 检查防火墙设置

## 扩展功能

如需添加更多功能，可以：

1. 实现更复杂的卡片消息格式
2. 添加消息模板功能
3. 实现消息群发功能
4. 添加消息历史记录
5. 实现消息撤回功能

## 技术支持

如有问题，请查看：
- 飞书开放平台文档：https://open.feishu.cn/document/
- 项目日志文件
- 应用健康检查接口
