package com.stopwatch;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@ConfigGroup("stopwatch")
public interface StopwatchConfig extends Config {
    @ConfigItem(
            keyName = "infobox",
            name = "Show infobox when stopwatch is active",
            description = "Display infobox when stopwatch is active"
    )
    default boolean infobox() {
        return true;
    }

    @ConfigItem(
            keyName = "start/stop hotkey",
            name = "Start/Stop Hotkey",
            description = "Pressing this key combination will start and stop the timer",
            position = 1
    )
    default Keybind startStopHotkey() {
        return new Keybind(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
    }

    @ConfigItem(
            keyName = "reset hotkey",
            name = "Reset Hotkey",
            description = "Pressing this key combination will reset the timer to 0",
            position = 2
    )
    default Keybind resetHotkey() {
        return new Keybind(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
    }
}
