package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.InstWormCommand;
import net.ncguy.serialui.factory.forms.instructions.InstWormCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class InstWormCommandFactory extends BaseCommandFactory<InstWormCommand> {

    public InstWormCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Worm";
    }

    @Override
    public JPanel BuildUI() {
        return new InstWormCommandForm(host).root;
    }
}
