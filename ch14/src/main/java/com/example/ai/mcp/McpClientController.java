package com.example.ai.mcp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.client.autoconfigure.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MCP Client controller — available only when the "mcp-client" profile is active.
 *
 * Prerequisites:
 *   1. Uncomment spring-ai-starter-mcp-client in pom.xml.
 *   2. Uncomment an MCP client connection in application.properties (STDIO or Streamable-HTTP).
 *   3. If using STDIO: ensure Node.js is installed (node --version).
 *   4. Run with: ./mvnw spring-boot:run -Dspring-boot.run.profiles=mcp-client
 *
 * SyncMcpToolCallbackProvider is auto-configured by Spring AI when the MCP client
 * starter is on the classpath and spring.ai.mcp.client.enabled=true. It queries
 * all configured MCP servers for their tool listings at startup.
 */
@RestController
@RequestMapping("/api/ai")
@Profile("mcp-client")
public class McpClientController {

    private final ChatClient chatClient;
    private final SyncMcpToolCallbackProvider toolCallbackProvider;

    public McpClientController(ChatClient chatClient,
                                SyncMcpToolCallbackProvider toolCallbackProvider) {
        this.chatClient           = chatClient;
        this.toolCallbackProvider = toolCallbackProvider;
    }

    record McpAnswer(String reply) {}

    // Passes all tools discovered from connected MCP servers to the ChatClient.
    // The LLM decides which tools to invoke based on the user's question.
    @GetMapping("/mcp-agent")
    public McpAnswer mcpAgent(@RequestParam String question) {
        String reply = chatClient.prompt()
                .user(question)
                .tools(toolCallbackProvider.getToolCallbacks())
                .call()
                .content();
        return new McpAnswer(reply);
    }
}
