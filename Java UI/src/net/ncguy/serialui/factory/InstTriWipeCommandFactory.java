package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.InstWormCommand;
import net.ncguy.serialui.factory.forms.instructions.InstTriWipeCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class InstTriWipeCommandFactory extends BaseCommandFactory<InstWormCommand> {

    public InstTriWipeCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Worm tri wipe";
    }

    @Override
    public JPanel BuildUI() {
        return new InstTriWipeCommandForm(host).root;
    }
}
