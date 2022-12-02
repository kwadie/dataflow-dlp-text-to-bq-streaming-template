package com.google.cloud.pso;


/** Category that can be used on Dataflow Templates. */
public enum TemplateCategory {
    GET_STARTED("get_started", "Get Started"),

    STREAMING("STREAMING", "Process Data Continuously (stream)"),

    BATCH("BATCH", "Process Data in Bulk (batch)"),

    UTILITIES("utilities", "Utilities");

    final String name;
    final String displayName;

    TemplateCategory(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }
}
