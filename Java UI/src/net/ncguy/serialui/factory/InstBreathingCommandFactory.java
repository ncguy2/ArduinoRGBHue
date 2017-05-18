package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.InstBreathingCommand;
import net.ncguy.serialui.factory.forms.instructions.InstBreathingCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class InstBreathingCommandFactory extends BaseCommandFactory<InstBreathingCommand> {

    public InstBreathingCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Breathing";
    }

    @Override
    public JPanel BuildUI() {
        return new InstBreathingCommandForm(host).root;
    }
}
