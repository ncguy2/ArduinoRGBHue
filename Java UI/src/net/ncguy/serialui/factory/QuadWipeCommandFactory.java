package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.QuadWipeCommand;
import net.ncguy.serialui.factory.forms.QuadWipeCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class QuadWipeCommandFactory extends BaseCommandFactory<QuadWipeCommand> {

    public QuadWipeCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Quad-Wipe";
    }

    @Override
    public JPanel BuildUI() {
        return new QuadWipeCommandForm(host).root;
    }
}
