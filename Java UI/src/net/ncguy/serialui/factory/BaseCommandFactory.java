package net.ncguy.serialui.factory;

import net.ncguy.serialui.cmd.BaseCommand;

import javax.swing.*;

/**
 * Created by Guy on 06/05/2017.
 */
public abstract class BaseCommandFactory<T extends BaseCommand> {

    abstract JPanel BuildUI();

}
