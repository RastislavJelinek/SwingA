package sk.godis.components;

import javax.swing.*;
import java.awt.*;


public class BtnCancel extends JButton{

    private static final java.awt.Color COLOR = new java.awt.Color(255, 204, 204);
    
  public BtnCancel() {
        BtnCancel.this.setBackground(COLOR);
        //BtnCancel.this.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        //BtnCancel.this.setIcon(new ScaleToFitAndAntialiasIcon( new ImageIcon(getClass().getResource("/iconsMe/cancel.png")))); // NOI18N
        BtnCancel.this.setText("Cancel");
        BtnCancel.this.setPreferredSize(new Dimension(102, 31));
}  
}

