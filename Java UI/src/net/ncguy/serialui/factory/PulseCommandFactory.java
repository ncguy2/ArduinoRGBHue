package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.PulseCommand;
import net.ncguy.serialui.factory.forms.PulseCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class PulseCommandFactory extends BaseCommandFactory<PulseCommand> {

    public PulseCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Pulse";
    }

    @Override
    public JPanel BuildUI() {
        return new PulseCommandForm(host).root;
    }
}
