package com.example.ai.mcp;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * MCP Server component — exposes TechStore tools via the MCP protocol.
 *
 * @McpTool registers methods exclusively in the MCP protocol layer
 * (spring-ai-starter-mcp-server-webmvc). These tools are discoverable by any
 * MCP-compatible client (Claude Desktop, other Spring AI apps, etc.) but are
 * NOT registered as in-process Tool Calling beans.
 *
 * Contrast with @Tool (DateTimeTools, ProductTools) which registers for BOTH
 * in-process Tool Calling AND MCP auto-discovery.
 *
 * MCP endpoint (Streamable-HTTP): POST http://localhost:8080/mcp
 *
 * Test with the MCP Inspector (requires Node.js):
 *   npx @modelcontextprotocol/inspector@latest http://localhost:8080/mcp
 */
@Component
public class TechStoreMcpServer {

    private static final Map<String, Double> PRICES = Map.of(
            "spring-book",   49.99,
            "java-guide",    39.99,
            "docker-manual", 29.99
    );

    @McpTool(description = "Returns the current price of a TechStore product by SKU.")
    public String getProductPrice(String sku) {
        Double price = PRICES.get(sku.toLowerCase());
        return price == null ? "Product not found" : "Price: $" + price;
    }

    @McpTool(description = "Returns the current date and time in ISO-8601 format.")
    public String getCurrentDateTime() {
        return LocalDateTime.now().toString();
    }

    @McpTool(description = "Returns the TechStore return policy.")
    public String getReturnPolicy() {
        return "Items may be returned within 30 days in original condition. "
             + "Digital downloads are non-refundable.";
    }
}
