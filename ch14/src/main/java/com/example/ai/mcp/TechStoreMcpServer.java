package com.example.ai.mcp;

/**
 * MCP Server — Tool Discovery for the chapter-14-demo application.
 *
 * How it works:
 *   The spring-ai-starter-mcp-server-webmvc starter scans all @Component beans
 *   for @Tool-annotated methods and registers them in the MCP protocol layer.
 *   No additional annotation or explicit registration is needed.
 *
 * Tools exposed via MCP from this application:
 *   - DateTimeTools.getCurrentDateTime()  → "Returns the current date and time in ISO-8601 format."
 *   - ProductTools.getProductPrice(sku)   → "Returns the current price of a product given its SKU identifier."
 *
 * MCP endpoint (Streamable-HTTP transport):
 *   POST http://localhost:8080/mcp
 *
 * Test with the MCP Inspector (requires Node.js):
 *   npx @modelcontextprotocol/inspector@latest http://localhost:8080/mcp
 *
 * The Inspector opens at http://localhost:6274 and lets you browse and
 * invoke the exposed tools from a browser UI.
 */
public final class TechStoreMcpServer {
    // Documentation class only — no instantiation needed.
    private TechStoreMcpServer() {}
}
