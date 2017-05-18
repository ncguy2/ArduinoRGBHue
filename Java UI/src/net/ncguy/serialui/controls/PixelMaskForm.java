package net.ncguy.serialui.controls;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 07/05/2017.
 */
public class PixelMaskForm {
    private JList<Integer> available;
    private JList<Integer> masked;
    private JButton addSelected;
    private JButton removeSelected;
    private JPanel root;

    DefaultListModel<Integer> maskedModel;

    ArrayList<Integer> maskedItems;
    public int pixelCount;

    public PixelMaskForm() {
        maskedModel = new DefaultListModel<>();
        masked.setModel(maskedModel);
    }

    public void Initialize(int pixelCount) {
        this.pixelCount = pixelCount;
        Integer[] pixels = new Integer[pixelCount];
        for(int i = 0; i < pixelCount; i++) {
            pixels[i] = i;
        }
        available.setListData(pixels);
        maskedItems = new ArrayList<>();

        addSelected.addActionListener(e -> {
            List<Integer> selected = available.getSelectedValuesList();
            selected.forEach(sel -> {
                if(maskedItems.contains(sel)) return;
                maskedItems.add(sel);
                maskedModel.addElement(sel);
            });
        });
        removeSelected.addActionListener(e -> {
            List<Integer> selected = masked.getSelectedValuesList();
            selected.forEach(sel -> {
                if(!maskedItems.contains(sel)) return;
                maskedItems.remove(sel);
                maskedModel.removeElement(sel);
            });
        });
    }

    public boolean[] ToMask() {
        boolean[] mask = new boolean[pixelCount];
        for(int i = 0; i < pixelCount; i++)
            mask[i] = maskedItems.contains(i);
        return mask;
    }

}
