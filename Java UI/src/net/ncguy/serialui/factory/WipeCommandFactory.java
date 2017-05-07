package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.BaseCommand;
import net.ncguy.serialui.cmd.WipeCommand;
import net.ncguy.serialui.factory.forms.WipeCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class WipeCommandFactory extends BaseCommandFactory<WipeCommand> {

    public WipeCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Wipe";
    }

    @Override
    public JPanel BuildUI() {
        return new WipeCommandForm(host).root;
    }
}
