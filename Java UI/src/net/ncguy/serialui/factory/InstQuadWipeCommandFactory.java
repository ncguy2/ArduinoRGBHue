package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.InstWormCommand;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class InstQuadWipeCommandFactory extends BaseCommandFactory<InstWormCommand> {

    public InstQuadWipeCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Worm quad wipe";
    }

    @Override
    public JPanel BuildUI() {
        return new InstTriWipeCommandForm(host).root;
    }
}
