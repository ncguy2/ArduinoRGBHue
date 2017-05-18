package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.PulseCommand;
import net.ncguy.serialui.factory.forms.PulseMaskCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class PulseMaskCommandFactory extends BaseCommandFactory<PulseCommand> {

    public PulseMaskCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Masked Pulse";
    }

    @Override
    public JPanel BuildUI() {
        return new PulseMaskCommandForm(host).root;
    }
}
