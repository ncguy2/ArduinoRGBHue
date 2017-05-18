package net.ncguy.serialui;

import com.fazecast.jSerialComm.SerialPort;
import net.ncguy.serialui.cmd.BaseCommand;
import net.ncguy.serialui.factory.BaseCommandFactory;
import org.reflections.Reflections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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
    private JList<InetAddressWrapper> discoveredHostList;
    private JButton discoverHostsBtn;
    private JScrollPane discoveredHostList_Container;
    private JList discover_CmdList;
    private JPanel discover_CmdPanel;
    private JTabbedPane TabControl;
    private JButton findHostsBtn;
    private SerialPort[] ports;
    private SerialPort activePort;
    private boolean enabled = false;

    private Set<InetAddressWrapper> discoveredHosts = new LinkedHashSet<>();

    public static final int PIXELCOUNT = 12;
    public static final int PORT = 3300;
    public static final boolean USE_TCP = false;

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

            Color col = pixelRangeColourBG.getBackground();
            int r = col.getRed();
            int g = col.getGreen();
            int b = col.getBlue();
            byte cmd = 1;
            CommandPayload payload = new CommandPayload(cmd);
            payload.SetColour(0, r, g, b);
            try {
                SendCommandPayload(hostField.getText(), payload);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // TODO populate list with command factories
        Reflections ref = new Reflections();
        Set<Class<? extends BaseCommandFactory>> subTypesOf = ref.getSubTypesOf(BaseCommandFactory.class);
        ArrayList<BaseCommandFactory> factories = new ArrayList<>();
        subTypesOf.forEach(cls -> {
            try {
                Constructor<? extends BaseCommandFactory> ctor = cls.getConstructor(RGBForm.class);
                if(ctor != null)
                    factories.add(ctor.newInstance(RGBForm.this));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        discoveredHostList.setModel(new DefaultListModel<>());
//        factories.add(new PulseCommandFactory(this));
//        factories.add(new PulseCommandFactory(this));
        BaseCommandFactory[] fs = new BaseCommandFactory[factories.size()];
        factories.toArray(fs);
        cmdList.setListData(fs);
        discover_CmdList.setListData(fs);
        cmdList.addListSelectionListener(e -> {
            BaseCommandFactory factory = (BaseCommandFactory) cmdList.getSelectedValue();
            try{
                dynamicPropsField.removeAll();
            }catch (Exception ignored) {}
            JPanel ui = factory.BuildUI();
            dynamicPropsField.setLayout(new BorderLayout());
            dynamicPropsField.add(ui, BorderLayout.CENTER);
            dynamicPropsField.updateUI();
        });
        discover_CmdList.addListSelectionListener(e -> {
            BaseCommandFactory factory = (BaseCommandFactory) discover_CmdList.getSelectedValue();
            try{
                discover_CmdPanel.removeAll();
            }catch (Exception ignored) {}
            JPanel ui = factory.BuildUI();
            discover_CmdPanel.setLayout(new BorderLayout());
            discover_CmdPanel.add(ui, BorderLayout.CENTER);
            discover_CmdPanel.updateUI();
        });
        discoverHostsBtn.addActionListener(e -> DiscoverHosts());
    }

    private void AppendColour(StringBuilder sb, Color c) {
        sb.append(c.getRed()).append(" ");
        sb.append(c.getGreen()).append(" ");
        sb.append(c.getBlue()).append(" ");
    }

    private static void AppendColourBytes(char[] sb, int offset, Color c) {

        int r = Math.max(1, c.getRed());
        int g = Math.max(1, c.getGreen());
        int b = Math.max(1, c.getBlue());

        byte br = (byte)r;
        byte bg = (byte)g;
        byte bb = (byte)b;

        char tmpr = (char)r;
        char tmpg = (char)g;
        char tmpb = (char)b;

        char cr = (char)br;
        char cg = (char)bg;
        char cb = (char)bb;

        {
            String test = "";
            test += br;
            System.out.println("Red: " + test + ", " + test.length());
        }
        {
            String test = "";
            test += bg;
            System.out.println("Green: " + test + ", " + test.length());
        }
        {
            String test = "";
            test += bb;
            System.out.println("Blue: " + test + ", " + test.length());
        }

        sb[offset + 0] = cr;
        sb[offset + 1] = cg;
        sb[offset + 2] = cb;
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

    public void executePost(String targetURL, char[] urlParameters) throws IOException {
        System.out.println("Sending payload to " + targetURL);
        new Thread(() -> ExecutePost_ThreadProcess(targetURL, urlParameters)).start();
    }

    private byte[] BuildByteBuffer(char[] payload) {
        byte[] buffer = new byte[payload.length];
        for (int i = 0; i < payload.length; i++)
            buffer[i] = (byte) payload[i];
        return buffer;
    }

    private void ExecutePost_ThreadProcess(String targetUrl, char[] payload) {
        byte[] buffer = BuildByteBuffer(payload);
        if(USE_TCP) {
            try {
                Socket skt = new Socket(targetUrl, PORT);
                DataOutputStream out = new DataOutputStream(skt.getOutputStream());
                out.write(buffer, 0, buffer.length);
                skt.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                String[] ipSegs = targetUrl.split("\\.");
                byte[] ip = new byte[4];
                for(int i = 0; i < ip.length; i++)
                    ip[i] = (byte) Integer.parseInt(ipSegs[i].trim());
                InetAddress addr = InetAddress.getByAddress(ip);
                DatagramPacket pkt = new DatagramPacket(buffer, buffer.length, addr, PORT);
                DatagramSocket skt = new DatagramSocket();
                skt.send(pkt);
            } catch (IOException | IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public void SendCommandPayload(String target, CommandPayload payload) throws IOException {
        System.out.println(payload.toString());
        char[] prepared = payload.prepare();
        System.out.println(prepared);
        executePost(target, prepared);
    }

    public void SendCommandPayload(CommandPayload payload) throws IOException {
        int tabIndex = TabControl.getSelectedIndex();
        switch(tabIndex) {
            case 0: return; // Serial
            case 1:         // Ethernet
                SendCommandPayload(hostField.getText(), payload);
                return;
            case 2:         // Discovered
                discoveredHostList.getSelectedValuesList().forEach(wrapper -> {
                    try {
                        SendCommandPayload(wrapper.address.toString().substring(1), payload);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                return;
        }
    }

    private void AddHost(InetAddress address) {
        AddHost(new InetAddressWrapper(address));
    }
    private void AddHost(InetAddress address, boolean broadcast) {
        AddHost(new InetAddressWrapper(address, broadcast));
    }

    private void AddHost(InetAddressWrapper wrapper) {
        // Manual Set::Contains
//        if(discoveredHosts.contains(wrapper)) {
//            System.out.printf("Duplicate address [%s] detected, ignoring...\n", wrapper.address);
//            return;
//        }

        InetAddressWrapper[] items = new InetAddressWrapper[discoveredHosts.size()];
        discoveredHosts.toArray(items);
        for (InetAddressWrapper item : items) {
            if(item.equals(wrapper)) {
                System.out.printf("Duplicate address [%s] detected, ignoring...\n", wrapper.address);
                return;
            }
        }

        discoveredHosts.add(wrapper);
        DefaultListModel<InetAddressWrapper> model = (DefaultListModel<InetAddressWrapper>)discoveredHostList.getModel();
        model.addElement(wrapper);
    }

    public void DiscoverHosts() {
        java.util.List<InetAddress> localAddrs = Utils.LocalInterfaceBroadcasts();
        localAddrs.stream().filter(Objects::nonNull).forEach(localAddr -> {
            AddHost(localAddr, true);
            Thread t = new Thread(() -> {
                try {
                    DiscoverHosts_ThreadProcess(localAddr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        });
    }

    private void DiscoverHosts_ThreadProcess(InetAddress addr) throws IOException {
        CommandPayload cmdPayload = new CommandPayload(BaseCommand.CMD_DISCOVERY);
        char[] payload = cmdPayload.prepare();

        byte[] buffer = BuildByteBuffer(payload);
        DatagramPacket pkt = new DatagramPacket(buffer, buffer.length, addr, PORT);
        DatagramSocket skt = new DatagramSocket();
        skt.send(pkt);
        byte[] inputBuffer = new byte[10240];

        skt.setSoTimeout(3000);
        boolean socketActive = true;
        while(socketActive) {
            try{
                DatagramPacket rPkt = new DatagramPacket(inputBuffer, inputBuffer.length);
                skt.receive(rPkt);
                System.out.println("Potential Discovery response received: ");
                System.out.printf("\tFrom %s:%s\n", rPkt.getAddress(), rPkt.getPort());
                String data = Utils.AssembleBytes(rPkt.getData());
                System.out.printf("\tData: \"%s\"", data);
                if(data.toUpperCase().startsWith("DISCOVERY_RESPONSE"))
                    DiscoverHosts_HostDiscovered(rPkt.getAddress());
                System.out.println();
            }catch (SocketTimeoutException ste) {
                System.out.println("Socket timeout");
                socketActive = false;
            }
        }
        skt.close();
    }

    private void DiscoverHosts_HostDiscovered(InetAddress address) {
        System.out.println("Host discovered at " + address);
        AddHost(address, false);
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

        public CommandPayload(byte commandId) {
            this.commandId = commandId;
            for(int i = 0; i < colours.length; i++)
                colours[i] = new Color(Color.WHITE.getRGB());
        }
        public void SetWait(short wait) {
            this.wait = wait;
        }
        public void SetColour(int id, int r, int g, int b) {
            if(id < 0 || id > colours.length) return;
            colours[id] = new Color(r, g, b);
        }

        @Override
        public String toString() {
            return String.format("ID: %s, Wait: %s, Colours: {\n\t1: %s,\n\t2: %s, \n\t3: %s, \n\t4: %s \n}", commandId, wait, colours[0], colours[1], colours[2], colours[3]);
        }

        public char[] prepare() {
            char[] sb = new char[14];
            sb[0] = (char)commandId;
            for (int i = 0; i < colours.length; i++)
                AppendColourBytes(sb, (i * 3) + 1, colours[i]);
            sb[13] = '\0';
            return sb;
        }

    };

    public static class InetAddressWrapper {

        public InetAddressWrapper(InetAddress address) {
            this(address, false);
        }

        public InetAddressWrapper(InetAddress address, boolean broadcast) {
            this.address = address;
            this.broadcast = broadcast;
        }

        public InetAddress address;
        public boolean broadcast;

        @Override
        public String toString() {
            String body = address.toString();
            if(broadcast)
                body = "[B] " + body;
            return body;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) return true;
            if(obj instanceof InetAddressWrapper) {
                if(address.equals(((InetAddressWrapper) obj).address)) return true;
            }
            if(address.equals(obj)) return true;
            return super.equals(obj);
        }
    }

}
