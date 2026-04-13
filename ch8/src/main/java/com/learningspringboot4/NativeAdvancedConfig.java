package com.learningspringboot4;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(VideoRuntimeHints.class)
public class NativeAdvancedConfig {
}
