package net.ncguy.serialui;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Guy on 05/05/2017.
 */
public class RGBForm {
    private JButton RescanBtn;
    private JComboBox<String> portSelect;
    private JButton useSerialBtn;
    private JPanel root;
    private JTextArea outputArea;
    private JTextField inputArea;
    private JPanel enabledOnly;
    private JButton colABtn;
    private JPanel colABG;
    private JButton colBBtn;
    private JPanel colBBG;
    private JButton colCBtn;
    private JPanel colCBG;
    private JPanel colDBG;
    private JButton colDBtn;
    private JButton btnWipe;
    private JButton dualWipeBtn;
    private JButton TriWipeBtn;
    private JButton quadWipeBtn;
    private JSpinner spnDelay;
    private JButton btnclear;
    private JButton closePortBtn;
    private JTextField hostField;
    private JSpinner pixelSelector;
    private JButton pixelColourBtn;
    private JPanel pixelColourBG;
    private JButton setPixelColour;
    private JSpinner pixelRangeEnd;
    private JSpinner pixelRangeStart;
    private JButton pixelRangeColourBtn;
    private JPanel pixelRangeColourBG;
    private JButton btnSetPixelRange;
    private JList cmdList;
    private JPanel dynamicPropsField;
    private JList<InetAddress> hostList;
    private JButton findHostsBtn;
    private SerialPort[] ports;
    private SerialPort activePort;
    private boolean enabled = false;

    public static final int PIXELCOUNT = 12;

    Timer timer;

    public RGBForm() {
        RescanBtn.addActionListener(e -> {
            portSelect.removeAllItems();
            this.ports = SerialPort.getCommPorts();
            for (SerialPort port : this.ports) {
                String str = "[" + port.getSystemPortName() + "] " + port.getDescriptivePortName();
                portSelect.addItem(str);
            }
        });
        useSerialBtn.addActionListener(e -> {
            activePort = ports[portSelect.getSelectedIndex()];
            boolean hasOpened = activePort.openPort();
            if(hasOpened) {
                activePort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 50, 50);
                activePort.setBaudRate(9600);
                closePortBtn.setEnabled(true);
                EnableForm();
            }else{
                System.err.println("Unable to open serial port");
            }
        });


        spnDelay.setValue(50);
        timer = new Timer((int) (1000f / 16f), (e) -> Tick());
        timer.start();

        inputArea.setFont(new Font("monospaced", Font.PLAIN, 12));
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SubmitText(inputArea.getText());
                    inputArea.setText("");
                }
            }
        });

        SetEnabledHierarchy(enabledOnly, false);

        colABtn.addActionListener(e -> {
            Color col = colABG.getBackground();
            col = JColorChooser.showDialog(RGBForm.this.root, "Choose a colour", col);
            if(col == null) return;
            colABG.setBackground(col);
            colABG.repaint();
        });
        colBBtn.addActionListener(e -> {
            Color col = colBBG.getBackground();
            col = JColorChooser.showDialog(RGBForm.this.root, "Choose a colour", col);
            if(col == null) return;
            colBBG.setBackground(col);
            colBBG.repaint();
        });
        colCBtn.addActionListener(e -> {
            Color col = colCBG.getBackground();
            col = JColorChooser.showDialog(RGBForm.this.root, "Choose a colour", col);
            if(col == null) return;
            colCBG.setBackground(col);
            colCBG.repaint();
        });
        colDBtn.addActionListener(e -> {
            Color col = colDBG.getBackground();
            col = JColorChooser.showDialog(RGBForm.this.root, "Choose a colour", col);
            if(col == null) return;
            colDBG.setBackground(col);
            colDBG.repaint();
        });

        btnWipe.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("wipe ");
            Color c = colABG.getBackground();
            AppendColour(sb, c);
            sb.append(spnDelay.getValue());
            SubmitText(sb.toString(), true);
        });
        dualWipeBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("dual-wipe ");
            Color ac = colABG.getBackground();
            AppendColour(sb, ac);
            Color bc = colBBG.getBackground();
            AppendColour(sb, bc);
            sb.append(spnDelay.getValue());
            SubmitText(sb.toString(), true);
        });
        TriWipeBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("tri-wipe ");
            Color ac = colABG.getBackground();
            AppendColour(sb, ac);
            Color bc = colBBG.getBackground();
            AppendColour(sb, bc);
            Color cc = colCBG.getBackground();
            AppendColour(sb, cc);
            sb.append(spnDelay.getValue());
            SubmitText(sb.toString(), true);
        });
        quadWipeBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("quad-wipe ");
            Color ac = colABG.getBackground();
            AppendColour(sb, ac);
            Color bc = colBBG.getBackground();
            AppendColour(sb, bc);
            Color cc = colCBG.getBackground();
            AppendColour(sb, cc);
            Color dc = colDBG.getBackground();
            AppendColour(sb, dc);
            sb.append(spnDelay.getValue());
            SubmitText(sb.toString(), true);
        });
        btnclear.addActionListener(e -> outputArea.setText(""));
        closePortBtn.addActionListener(e -> {
            if(activePort == null) return;
            activePort.closePort();
            activePort = null;
            closePortBtn.setEnabled(false);
        });
        pixelColourBtn.addActionListener(e -> {
            Color col = pixelColourBG.getBackground();
            col = JColorChooser.showDialog(RGBForm.this.root, "Choose a colour", col);
            if(col == null) return;
            pixelColourBG.setBackground(col);
            pixelColourBG.repaint();
        });
        setPixelColour.addActionListener(e -> {
            int pixel = (int) pixelSelector.getValue();
            pixel %= PIXELCOUNT;
            pixelSelector.setValue(pixel);
            Color col = pixelColourBG.getBackground();
            int r = col.getRed();
            int g = col.getGreen();
            int b = col.getBlue();
            byte cmd = 1;
            CommandPayload payload = new CommandPayload(cmd);
            payload.SetColour(0, r, g, b);
            payload.SetPinMask(true, pixel);
            try {
                SendCommandPayload(hostField.getText(), payload);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        pixelRangeColourBtn.addActionListener(e -> {
            Color col = pixelRangeColourBG.getBackground();
            col = JColorChooser.showDialog(RGBForm.this.root, "Choose a colour", col);
            if(col == null) return;
            pixelRangeColourBG.setBackground(col);
            pixelRangeColourBG.repaint();
        });
        btnSetPixelRange.addActionListener(e -> {
            int start = (int) pixelRangeStart.getValue();
            int end = (int) pixelRangeEnd.getValue();

            start %= PIXELCOUNT;
            end %= PIXELCOUNT;

            pixelRangeStart.setValue(start);
            pixelRangeEnd.setValue(end);

            ArrayList<Integer> range = new ArrayList<>();
            for(int i = start; i != end; i++) {
                if(i == PIXELCOUNT) i = 0;
                range.add(i);
            }
            range.add(end);

            int[] pins = new int[range.size()];
            for(int i = 0; i < range.size(); i++)
                pins[i] = range.get(i);

            Color col = pixelRangeColourBG.getBackground();
            int r = col.getRed();
            int g = col.getGreen();
            int b = col.getBlue();
            byte cmd = 1;
            CommandPayload payload = new CommandPayload(cmd);
            payload.SetColour(0, r, g, b);
            payload.SetPinMask(true, pins);
            try {
                SendCommandPayload(hostField.getText(), payload);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // TODO populate list with command factories
//        cmdList.setListData();
        cmdList.addListSelectionListener(e -> {

        });
    }

    private void AppendColour(StringBuilder sb, Color c) {
        sb.append(c.getRed()).append(" ");
        sb.append(c.getGreen()).append(" ");
        sb.append(c.getBlue()).append(" ");
    }

    private static void AppendColourBytes(StringBuilder sb, Color c) {
        sb.append((char)(Math.max(1, c.getRed())))
          .append((char)(Math.max(1, c.getGreen())))
          .append((char)(Math.max(1, c.getBlue())));
    }


    private void SetEnabledHierarchy(Component parent, boolean enabled) {
        parent.setEnabled(enabled);
        if(parent instanceof JComponent) {
            JComponent jparent = (JComponent)parent;
            for (Component c : jparent.getComponents())
                SetEnabledHierarchy(c, enabled);
        }

    }

    private void SubmitText(String text) {
        SubmitText(text, false);
    }

    private void SubmitText(String text, boolean auto) {
        if(!enabled) return;
        if(auto) outputArea.append("[" + text + "]\n");
        byte[] bytes = text.getBytes();
        activePort.writeBytes(bytes, bytes.length);
    }

    private void EnableForm() {
        enabled = true;
        SetEnabledHierarchy(enabledOnly, true);
    }

    public void executePost(String targetURL, String urlParameters) throws IOException {
        System.out.println("Sending payload to " + targetURL);
        for(char c : urlParameters.toCharArray()) {
            System.out.println((byte)c + ", " + c);
        }
        new Thread(() -> {
            try {
                Socket skt = new Socket(targetURL, 3300);
                DataOutputStream out = new DataOutputStream(skt.getOutputStream());
                out.writeBytes(urlParameters);
                skt.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void SendCommandPayload(String target, CommandPayload payload) throws IOException {
        executePost(target, payload.prepare());
    }

    public ArrayList<InetAddress> DiscoverHosts() {
        ArrayList<InetAddress> addrs = new ArrayList<>();
        return addrs;
    }

    private void Tick() {
        if(enabled) {
            int numBytes = activePort.bytesAvailable();
            if (numBytes <= 0) return;

            byte[] readBuffer = new byte[numBytes];
            int numRead = activePort.readBytes(readBuffer, readBuffer.length);
            String str = new String(readBuffer, StandardCharsets.UTF_8);
            outputArea.append(str);
        }
    }

    public static void Start(String[] args) {
        JFrame frame = new JFrame("RGBForm");
        frame.setContentPane(new RGBForm().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public static class CommandPayload {
        public byte commandId;
        public Color colours[] = new Color[4];
        public short wait = 50;
        public boolean pinMask[] = new boolean[PIXELCOUNT];

        public CommandPayload(byte commandId) {
            this.commandId = commandId;
            for(int i = 0; i < colours.length; i++)
                colours[i] = new Color(Color.WHITE.getRGB());
            for(int i = 0; i < pinMask.length; i++)
                pinMask[i] = false;
        }
        public void SetWait(short wait) {
            this.wait = wait;
        }

        public void SetPinMask(boolean state, int... pins) {
            for (int pin : pins) {
                if(pin < 0 || pin > pinMask.length) continue;
                pinMask[pin] = state;
            }
        }
        public void SetColour(int id, int r, int g, int b) {
            if(id < 0 || id > colours.length) return;
            colours[id] = new Color(r, g, b);
        }

        public String prepare() {
            StringBuilder sb = new StringBuilder();
            sb.append((char)commandId);
            for (int i = 0; i < colours.length; i++)
                AppendColourBytes(sb, colours[i]);
            for (int i = 0; i < pinMask.length; i++)
                sb.append(pinMask[i] ? '1' : '0');
            sb.append('\n');
            return sb.toString();
        }

    };

}
