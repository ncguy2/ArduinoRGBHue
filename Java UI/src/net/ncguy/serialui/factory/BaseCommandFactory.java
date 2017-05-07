package net.ncguy.serialui.factory;

import net.ncguy.serialui.RGBForm;
import net.ncguy.serialui.cmd.BaseCommand;

import javax.swing.*;

/**
 * Created by Guy on 06/05/2017.
 */
public abstract class BaseCommandFactory<T extends BaseCommand> {

    public RGBForm host;

    public BaseCommandFactory(RGBForm host) {
        this.host = host;
    }

    public abstract String Name();
    public abstract JPanel BuildUI();

    @Override
    public String toString() {
        return Name();
    }
}
