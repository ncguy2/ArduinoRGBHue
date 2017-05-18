package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.InstWormCommand;
import net.ncguy.serialui.factory.forms.instructions.InstDualWipeCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class InstDualWipeCommandFactory extends BaseCommandFactory<InstWormCommand> {

    public InstDualWipeCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Worm dual wipe";
    }

    @Override
    public JPanel BuildUI() {
        return new InstDualWipeCommandForm(host).root;
    }
}
