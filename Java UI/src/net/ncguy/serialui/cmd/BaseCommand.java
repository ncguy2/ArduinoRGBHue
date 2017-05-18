package net.ncguy.serialui.cmd;

import net.ncguy.serialui.RGBForm;

/**
 * Created by Guy on 06/05/2017.
 */
public interface BaseCommand {

    String name();
    RGBForm.CommandPayload PreparePayload(int[] data);

    public static final byte CMD_SET = 0x01;                    // 1
    public static final byte CMD_PULSE = 0x02;                  // 2
    public static final byte CMD_PULSE_MASK = 0x03;             // 3
    public static final byte CMD_SWEEP = 0x04;                  // 4
    public static final byte CMD_WIPE = 0x05;                   // 5
    public static final byte CMD_DUALWIPE = 0x06;               // 6
    public static final byte CMD_TRIWIPE = 0x07;                // 7
    public static final byte CMD_QUADWIPE = 0x08;               // 8
    public static final byte CMD_TWIN_WIPE_SINGLE = 9;
    public static final byte CMD_TWIN_WIPE = 10;

    public static final byte CMD_INSTRUCTION_WORM = 31;
    public static final byte CMD_INSTRUCTION_DUALWIPE = 32;
    public static final byte CMD_INSTRUCTION_TRIWIPE = 33;
    public static final byte CMD_INSTRUCTION_QUADWIPE = 34;
    public static final byte CMD_INSTRUCTION_BREATHING = 35;

    public static final byte CMD_DISCOVERY = 0x7f;              // 127

}
