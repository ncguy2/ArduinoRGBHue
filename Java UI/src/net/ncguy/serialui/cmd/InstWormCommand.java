package net.ncguy.serialui.cmd;

import net.ncguy.serialui.RGBForm;

import java.awt.*;

/**
 * Created by nick on 07/05/17.
 */
public class InstWormCommand implements BaseCommand {
    @Override
    public String name() {
        return "Worm";
    }

    @Override
    public RGBForm.CommandPayload PreparePayload(int[] data) {
        RGBForm.CommandPayload payload = new RGBForm.CommandPayload(CMD_INSTRUCTION_WORM);
        payload.colours[0] = new Color(data[0], data[1], data[2]);
        payload.colours[1] = new Color(data[3], data[4], data[5]);
        payload.colours[2] = new Color(data[6], 255, 255);
        payload.wait = (short) data[7];
        return payload;
    }
}
