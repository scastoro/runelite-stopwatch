package com.stopwatch;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class StopwatchPluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(StopwatchPlugin.class);
        RuneLite.main(args);
    }
}