package sk.godis.components;


import javax.swing.*;
import java.awt.*;
import java.io.Serial;


public class BtnNext extends JButton implements java.io.Serializable{

    
    @Serial
    private static final long serialVersionUID = 1L;
    private static final java.awt.Color COLOR = new java.awt.Color(255, 255, 204);
    
    
    public BtnNext () {
        BtnNext.this.setBackground(COLOR);
        BtnNext.this.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 14)); // NOI18N
        //BtnNext.this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Next16.png"))); // NOI18N
        BtnNext.this.setText("Next�");
        BtnNext.this.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BtnNext.this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BtnNext.this.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        BtnNext.this.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BtnNext.this.setPreferredSize(new Dimension(102, 31));
}
    
}
