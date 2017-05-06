package net.ncguy.serialui;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;

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
    private SerialPort[] ports;
    private SerialPort activePort;
    private boolean enabled = false;

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
    }

    private void AppendColour(StringBuilder sb, Color c) {
        sb.append(c.getRed()).append(" ");
        sb.append(c.getGreen()).append(" ");
        sb.append(c.getBlue()).append(" ");
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
}
