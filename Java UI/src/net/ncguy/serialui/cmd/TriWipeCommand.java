package net.ncguy.serialui.cmd;

import net.ncguy.serialui.RGBForm;

import java.awt.*;

/**
 * Created by nick on 07/05/17.
 */
public class TriWipeCommand implements BaseCommand {
    @Override
    public String name() {
        return "TriWipe";
    }

    @Override
    public RGBForm.CommandPayload PreparePayload(int[] data) {
        RGBForm.CommandPayload payload = new RGBForm.CommandPayload((byte) 7);
        payload.colours[0] = new Color(data[0], data[1], data[2]);
        payload.colours[1] = new Color(data[3], data[4], data[5]);
        payload.colours[2] = new Color(data[6], data[7], data[8]);
        payload.wait = (short) data[9];
        return payload;
    }
}
