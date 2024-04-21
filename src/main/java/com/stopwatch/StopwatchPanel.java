package com.stopwatch;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Slf4j
@Singleton
public class StopwatchPanel extends PluginPanel {
    private final StopwatchPlugin plugin;
    private final StopwatchConfig config;

    private static JLabel timerLabel;
    private static JButton startStopButton;
    private static JButton resetButton;

    @Inject
    public StopwatchPanel(StopwatchPlugin plugin, StopwatchConfig config) {
        this.plugin = plugin;
        this.config = config;

        setLayout(new BorderLayout(0, 4));
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(8, 8, 8, 8));
        JPanel mainContent = new JPanel(new BorderLayout());

        startStopButton = new JButton("Start");
        startStopButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        startStopButton.setForeground(Color.WHITE);
        startStopButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        startStopButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, ColorScheme.DARK_GRAY_COLOR),
                new EmptyBorder(20, 4, 20, 4)
        ));

        timerLabel = new JLabel("0:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));

        startStopButton.addActionListener(e -> this.plugin.next());

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        resetButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, ColorScheme.DARK_GRAY_COLOR),
                new EmptyBorder(20, 4, 20, 4)
        ));

        resetButton.addActionListener(e -> this.plugin.resetTimer());

        mainContent.add(timerLabel, BorderLayout.NORTH);
        mainContent.add(startStopButton, BorderLayout.CENTER);
        mainContent.add(resetButton, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
    }

    public void setButtonText(String text) {
        startStopButton.setText(text);
    }

    public void setTimerText(String text) {
        timerLabel.setText(text);
    }

    public void reset() {
        setTimerText("0");
        setButtonText("Start");
    }
}
