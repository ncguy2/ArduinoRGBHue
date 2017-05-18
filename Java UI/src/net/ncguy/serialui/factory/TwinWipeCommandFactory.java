package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.TwinWipeCommand;
import net.ncguy.serialui.factory.forms.TwinWipeCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class TwinWipeCommandFactory extends BaseCommandFactory<TwinWipeCommand> {

    public TwinWipeCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Twin-Wipe";
    }

    @Override
    public JPanel BuildUI() {
        return new TwinWipeCommandForm(host).root;
    }
}
