package net.ncguy.serialui.factory.forms;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.DualWipeCommand;
import net.ncguy.serialui.cmd.TriWipeCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by nick on 07/05/17.
 */
public class TriWipeCommandForm {
    private JPanel setColourABG;
    private JButton setColourABtn;
    public JPanel root;
    private JSpinner wipeTime;
    private JButton sendWipe;
    private JPanel setColourBBG;
    private JButton SetColourBBtn;
    private JPanel setColourCBG;
    private JButton setColourCBtn;
    private RGBForm host;

    public TriWipeCommandForm(RGBForm host) {
        this.host = host;
        setColourABtn.addActionListener(actionEvent -> {
            Color col = setColourABG.getBackground();
            col = JColorChooser.showDialog(TriWipeCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourABG.setBackground(col);
            setColourABG.repaint();
        });
        sendWipe.addActionListener(actionEvent -> {
            int[] data = new int[10];
            Color a = setColourABG.getBackground();
            Color b = setColourBBG.getBackground();
            Color c = setColourCBG.getBackground();
            data[0] = Math.max(1, a.getRed());
            data[1] = Math.max(1, a.getGreen());
            data[2] = Math.max(1, a.getBlue());
            data[3] = Math.max(1, b.getRed());
            data[4] = Math.max(1, b.getGreen());
            data[5] = Math.max(1, b.getBlue());
            data[6] = Math.max(1, c.getRed());
            data[7] = Math.max(1, c.getGreen());
            data[8] = Math.max(1, c.getBlue());
            data[9] = Math.max(1, Math.min((Integer) wipeTime.getValue(), 255));
            try {
                host.SendCommandPayload(new TriWipeCommand().PreparePayload(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        SetColourBBtn.addActionListener(actionEvent -> {
            Color col = setColourBBG.getBackground();
            col = JColorChooser.showDialog(TriWipeCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourBBG.setBackground(col);
            setColourBBG.repaint();
        });
        setColourCBtn.addActionListener(actionEvent -> {
            Color col = setColourCBG.getBackground();
            col = JColorChooser.showDialog(TriWipeCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourCBG.setBackground(col);
            setColourCBG.repaint();
        });
    }
}
