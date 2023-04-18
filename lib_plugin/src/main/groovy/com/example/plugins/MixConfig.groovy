package com.example.plugins

/**
 * 为TraceMan自定义的配置项extension
 */
class MixConfig {
    String output
    boolean open
    String traceConfigFile
    boolean logTraceInfo

    MixConfig() {
        open = true
        output = ""
        logTraceInfo = true
    }
}