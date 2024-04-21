package com.stopwatch;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.runelite.client.ui.overlay.infobox.InfoBox;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

@Getter
@ToString
public class Stopwatch extends InfoBox {
    private static Timer timer;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private StopwatchState state;

    @Getter(AccessLevel.PACKAGE)
    private int currentTime;

    final StopwatchPlugin plugin;

    public Stopwatch(BufferedImage image, StopwatchPlugin plugin) {
        super(image, plugin);

        this.plugin = plugin;
        this.state = StopwatchState.IDLE;
    }

    @Override
    public String getText() {
        final int minutes = currentTime / 60;
        final int seconds = currentTime % 60;
        return String.format("%01d:%02d", minutes, seconds);
    }

    @Override
    public Color getTextColor() {
        return Color.WHITE;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTime++;
                plugin.update();
            }
        }, 1000, 1000);
    }

    public void stop() {
        timer.cancel();
    }

    public void reset() {
        timer.cancel();
        currentTime = 0;
    }
}
