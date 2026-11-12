package com.smartparking;

import com.smartparking.service.ParkingManager;
import com.smartparking.ui.LoginFrame;
import com.smartparking.util.CsvFileStore;

import javax.swing.*;
import java.nio.file.Path;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception e) { System.err.println("Using default Swing appearance: " + e.getMessage()); }
            CsvFileStore store = new CsvFileStore(Path.of("data"));
            new LoginFrame(store).setVisible(true);
        });
    }
}
