package net.ncguy.serialui.cmd;

import net.ncguy.serialui.RGBForm;

import java.awt.*;

/**
 * Created by nick on 07/05/17.
 */
@Command
public class InstBreathingCommand implements BaseCommand {
    @Override
    public String name() {
        return "Breathing";
    }

    @Override
    public RGBForm.CommandPayload PreparePayload(int[] data) {
        RGBForm.CommandPayload payload = new RGBForm.CommandPayload(CMD_INSTRUCTION_BREATHING);
        payload.colours[0] = new Color(data[0], data[1], data[2]);
        payload.colours[1] = new Color(data[3], 1, 1);
        payload.wait = (short) data[4];
        return payload;
    }
}
