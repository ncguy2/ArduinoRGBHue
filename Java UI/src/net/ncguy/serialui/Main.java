package net.ncguy.serialui;

import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;
import java.net.URL;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        LoadLookAndFeel();
        RGBForm.Start(args);
    }

    public static void LoadLookAndFeel() {
        try {
            UIManager.setLookAndFeel(DarculaLaf.class.getCanonicalName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Optional<ImageIcon> GetImage(String path) {
        URL url = Main.class.getClassLoader().getResource(path);
        if(url == null) return Optional.empty();
        ImageIcon icon = new ImageIcon(url);
        return Optional.of(icon);
    }

}
