package net.ncguy.serialui.cmd;

import net.ncguy.serialui.RGBForm;

import java.awt.*;

/**
 * Created by nick on 07/05/17.
 */
public class WipeCommand implements BaseCommand {
    @Override
    public String name() {
        return "Wipe";
    }

    @Override
    public RGBForm.CommandPayload PreparePayload(int[] data) {
        RGBForm.CommandPayload payload = new RGBForm.CommandPayload(CMD_WIPE);
        payload.colours[0] = new Color(data[0], data[1], data[2]);
        payload.wait = (short) data[3];
        return payload;
    }
}
