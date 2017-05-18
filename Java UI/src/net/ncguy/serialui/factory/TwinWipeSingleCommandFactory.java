package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.TwinWipeSingleCommand;
import net.ncguy.serialui.factory.forms.TwinWipeSingleCommandForm;

import javax.swing.*;

/**
 * Created by nick on 07/05/17.
 */
public class TwinWipeSingleCommandFactory extends BaseCommandFactory<TwinWipeSingleCommand> {

    public TwinWipeSingleCommandFactory(RGBForm host) {
        super(host);
    }

    @Override
    public String Name() {
        return "Twin-Wipe Single";
    }

    @Override
    public JPanel BuildUI() {
        return new TwinWipeSingleCommandForm(host).root;
    }
}
