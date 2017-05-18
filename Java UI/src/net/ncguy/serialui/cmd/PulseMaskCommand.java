package net.ncguy.serialui.cmd;

import net.ncguy.serialui.RGBForm;

/**
 * Created by Guy on 07/05/2017.
 */
public class PulseMaskCommand implements BaseCommand {

    @Override
    public String name() {
        return "Masked Pulse";
    }

    @Override
    public RGBForm.CommandPayload PreparePayload(int[] data) {
        RGBForm.CommandPayload payload = new RGBForm.CommandPayload(CMD_PULSE_MASK);
        payload.SetColour(0, data[0], data[1], data[2]);
        payload.SetColour(1, data[3], data[4], data[5]);

        payload.SetWait((short) data[data.length-1]);
        return payload;
    }
}
