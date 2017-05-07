package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.DualWipeCommand;
import net.ncguy.serialui.cmd.WipeCommand;
import net.ncguy.serialui.factory.forms.DualWipeCommandForm;
import net.ncguy.serialui.factory.forms.WipeCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class DualWipeCommandFactory extends BaseCommandFactory<DualWipeCommand> {

    public DualWipeCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Dual-Wipe";
    }

    @Override
    public JPanel BuildUI() {
        return new DualWipeCommandForm(host).root;
    }
}
