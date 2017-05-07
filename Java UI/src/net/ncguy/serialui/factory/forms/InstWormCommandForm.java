package net.ncguy.serialui.factory.forms;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.DualWipeCommand;
import net.ncguy.serialui.cmd.InstWormCommand;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by nick on 07/05/17.
 */
public class InstWormCommandForm {
    private JPanel setColourABG;
    private JButton setColourABtn;
    public JPanel root;
    private JSpinner wipeTime;
    private JButton sendWorm;
    private JPanel setColourBBG;
    private JButton SetColourBBtn;
    private JSpinner tailLength;
    private RGBForm host;

    public InstWormCommandForm(RGBForm host) {
        this.host = host;
        setColourABtn.addActionListener(actionEvent -> {
            Color col = setColourABG.getBackground();
            col = JColorChooser.showDialog(InstWormCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourABG.setBackground(col);
            setColourABG.repaint();
        });
        sendWorm.addActionListener(actionEvent -> {
            int[] data = new int[8];
            Color a = setColourABG.getBackground();
            Color b = setColourBBG.getBackground();
            data[0] = Math.max(1, a.getRed());
            data[1] = Math.max(1, a.getGreen());
            data[2] = Math.max(1, a.getBlue());
            data[3] = Math.max(1, b.getRed());
            data[4] = Math.max(1, b.getGreen());
            data[5] = Math.max(1, b.getBlue());
            data[6] = Math.max(1, (Integer) tailLength.getValue());
            data[7] = Math.max(1, Math.min((Integer) wipeTime.getValue(), 255));
            try {
                host.SendCommandPayload(new InstWormCommand().PreparePayload(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        SetColourBBtn.addActionListener(actionEvent -> {
            Color col = setColourBBG.getBackground();
            col = JColorChooser.showDialog(InstWormCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourBBG.setBackground(col);
            setColourBBG.repaint();
        });
    }
}
