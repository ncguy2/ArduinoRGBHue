package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.TriWipeCommand;
import net.ncguy.serialui.cmd.WipeCommand;
import net.ncguy.serialui.factory.forms.DualWipeCommandForm;
import net.ncguy.serialui.factory.forms.TriWipeCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class TriWipeCommandFactory extends BaseCommandFactory<TriWipeCommand> {

    public TriWipeCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Tri-Wipe";
    }

    @Override
    public JPanel BuildUI() {
        return new TriWipeCommandForm(host).root;
    }
}
