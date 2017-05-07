package net.ncguy.serialui.cmd;

import net.ncguy.serialui.RGBForm;

/**
 * Created by Guy on 06/05/2017.
 */
public interface BaseCommand {

    String name();
    RGBForm.CommandPayload PreparePayload(int[] data);

    public static final byte CMD_SET = 1;
    public static final byte CMD_PULSE = 2;
    public static final byte CMD_PULSE_MASK = 3;
    public static final byte CMD_SWEEP = 4;
    public static final byte CMD_WIPE = 5;
    public static final byte CMD_DUALWIPE = 6;
    public static final byte CMD_TRIWIPE = 7;
    public static final byte CMD_QUADWIPE = 8;

    public static final byte CMD_INSTRUCTION_WORM = 11;
    public static final byte CMD_INSTRUCTION_DUALWIPE = 12;
    public static final byte CMD_INSTRUCTION_TRIWIPE = 13;
    public static final byte CMD_INSTRUCTION_QUADWIPE = 14;

}
