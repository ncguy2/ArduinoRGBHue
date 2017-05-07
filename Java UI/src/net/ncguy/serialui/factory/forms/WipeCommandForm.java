package net.ncguy.serialui.factory.forms;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.PulseCommand;
import net.ncguy.serialui.cmd.WipeCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by nick on 07/05/17.
 */
public class WipeCommandForm {
    private JPanel setColourABG;
    private JButton setColourABtn;
    public JPanel root;
    private JSpinner wipeTime;
    private JButton sendWipe;
    private RGBForm host;

    public WipeCommandForm(RGBForm host) {
        this.host = host;
        setColourABtn.addActionListener(actionEvent -> {
            Color col = setColourABG.getBackground();
            col = JColorChooser.showDialog(WipeCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourABG.setBackground(col);
            setColourABG.repaint();
        });
        sendWipe.addActionListener(actionEvent -> {
            int[] data = new int[4];
            Color a = setColourABG.getBackground();
            data[0] = Math.max(1, a.getRed());
            data[1] = Math.max(1, a.getGreen());
            data[2] = Math.max(1, a.getBlue());
            data[3] = Math.max(1, Math.min((Integer) wipeTime.getValue(), 255));
            try {
                host.SendCommandPayload(new WipeCommand().PreparePayload(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
