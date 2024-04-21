package com.stopwatch;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageUtil;

import java.awt.event.KeyEvent;

@Slf4j
@PluginDescriptor(
        name = "Stopwatch"
)
public class StopwatchPlugin extends Plugin {
    private Stopwatch stopwatch;
    private StopwatchPanel panel;
    private NavigationButton navButton;

    @Inject
    private Client client;
    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ItemManager itemManager;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Inject
    private KeyManager keyManager;
    @Inject
    private StopwatchConfig config;

    private final HotkeyListener startStopKeyListener = new HotkeyListener(() -> config.startStopHotkey()) {
        @Override
        public void keyPressed(KeyEvent e) {
            if (config.startStopHotkey().matches(e)) {
                next();
            }
        }
    };

    private final HotkeyListener resetKeyListener = new HotkeyListener(() -> config.resetHotkey()) {
        @Override
        public void keyPressed(KeyEvent e) {
            if (config.resetHotkey().matches(e)) {
                resetTimer();
            }
        }
    };

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getKey().equals("infobox")) {
            infoBoxManager.removeIf(Stopwatch.class::isInstance);

            if (config.infobox() && stopwatch.getState() != StopwatchState.IDLE) {
                infoBoxManager.addInfoBox(stopwatch);
            }
        }
    }

    @Override
    protected void startUp() {
        stopwatch = new Stopwatch(itemManager.getImage(ItemID.WATCH), this);

        panel = injector.getInstance(StopwatchPanel.class);
        navButton = NavigationButton.builder()
                .tooltip("Stopwatch")
                .icon(ImageUtil.loadImageResource(getClass(), "/stopwatch.png"))
                .priority(6)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);
        keyManager.registerKeyListener(startStopKeyListener);
        keyManager.registerKeyListener(resetKeyListener);
    }

    @Override
    protected void shutDown() {
        keyManager.unregisterKeyListener(startStopKeyListener);
        keyManager.unregisterKeyListener(resetKeyListener);
        clientToolbar.removeNavigation(navButton);
        infoBoxManager.removeIf(Stopwatch.class::isInstance);

        panel.reset();
    }

    public void next() {
        switch (stopwatch.getState()) {
            case IDLE:
                stopwatch.start();
                stopwatch.setState(StopwatchState.RUNNING);
                panel.setButtonText("Stop");

                if (config.infobox()) {
                    infoBoxManager.addInfoBox(stopwatch);
                }
                break;
            case RUNNING:
                stopwatch.stop();
                stopwatch.setState(StopwatchState.PAUSED);
                panel.setButtonText("Start");
                break;
            case PAUSED:
                stopwatch.start();
                stopwatch.setState(StopwatchState.RUNNING);
                panel.setButtonText("Stop");
                break;
            default:
                break;
        }
        update();
    }

    public void resetTimer() {
        stopwatch.reset();
        stopwatch.setState(StopwatchState.IDLE);
        panel.setButtonText("Start");
        infoBoxManager.removeIf(Stopwatch.class::isInstance);
        update();
    }

    public void update() {
        panel.setTimerText(stopwatch.getText());
    }

    @Provides
    StopwatchConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(StopwatchConfig.class);
    }
}
