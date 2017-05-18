package net.ncguy.serialui.factory.forms.instructions;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.InstBreathingCommand;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by nick on 07/05/17.
 */
public class InstBreathingCommandForm {
    private JPanel setColourABG;
    private JButton setColourABtn;
    public JPanel root;
    private JSpinner wipeTime;
    private JButton sendWorm;
    private JSpinner speedScale;
    private RGBForm host;

    public InstBreathingCommandForm(RGBForm host) {
        this.host = host;
        setColourABtn.addActionListener(actionEvent -> {
            Color col = setColourABG.getBackground();
            col = JColorChooser.showDialog(InstBreathingCommandForm.this.root, "Choose a colour", col);
            if(col == null) return;
            setColourABG.setBackground(col);
            setColourABG.repaint();
        });
        sendWorm.addActionListener(actionEvent -> {
            int[] data = new int[5];
            Color a = setColourABG.getBackground();
            data[0] = Math.max(1, a.getRed());
            data[1] = Math.max(1, a.getGreen());
            data[2] = Math.max(1, a.getBlue());
            data[3] = Math.max(1, Math.min(((Integer) speedScale.getValue() * 10), 255));
            data[4] = Math.max(1, Math.min((Integer) wipeTime.getValue(), 255));
            try {
                host.SendCommandPayload(new InstBreathingCommand().PreparePayload(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
