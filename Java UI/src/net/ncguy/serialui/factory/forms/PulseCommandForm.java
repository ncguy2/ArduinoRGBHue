package net.ncguy.serialui.factory.forms;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.PulseCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by nick on 07/05/17.
 */
public class PulseCommandForm {
    public JPanel root;
    private JButton setColourABtn;
    private JPanel setColourABG;
    private JSpinner pulseTime;
    private JButton sendPulse;
    private JPanel setColourBBG;
    private JButton setColourBBtn;
    private RGBForm host;

    public PulseCommandForm(RGBForm host) {
        this.host = host;
        setColourABtn.addActionListener(actionEvent -> {
            Color col = setColourABG.getBackground();
            col = JColorChooser.showDialog(PulseCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourABG.setBackground(col);
            setColourABG.repaint();
        });
        setColourBBtn.addActionListener(actionEvent -> {
            Color col = setColourBBG.getBackground();
            col = JColorChooser.showDialog(PulseCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourBBG.setBackground(col);
            setColourBBG.repaint();
        });
        sendPulse.addActionListener(actionEvent -> {
            int[] data = new int[7];
            Color a = setColourABG.getBackground();
            Color b = setColourBBG.getBackground();
            data[0] = Math.max(1, a.getRed());
            data[1] = Math.max(1, a.getGreen());
            data[2] = Math.max(1, a.getBlue());
            data[3] = Math.max(1, b.getRed());
            data[4] = Math.max(1, b.getGreen());
            data[5] = Math.max(1, b.getBlue());
            data[6] = Math.max(1, Math.min((Integer) pulseTime.getValue(), 255));
            try {
                host.SendCommandPayload(new PulseCommand().PreparePayload(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
