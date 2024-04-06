package com.example.calendrier_ceri;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ThemeManager {
    private static BooleanProperty darkModeActive = new SimpleBooleanProperty(false);

    public static BooleanProperty darkModeActiveProperty() {
        return darkModeActive;
    }

    public static void toggleDarkMode() {
        darkModeActive.set(!darkModeActive.get());
    }
}
