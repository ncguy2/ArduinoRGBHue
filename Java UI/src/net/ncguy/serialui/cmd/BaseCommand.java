package net.ncguy.serialui.cmd;

import net.ncguy.serialui.RGBForm;

/**
 * Created by Guy on 06/05/2017.
 */
public interface BaseCommand {

    String name();
    RGBForm.CommandPayload PreparePayload();

}
