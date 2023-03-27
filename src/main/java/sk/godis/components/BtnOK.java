package sk.godis.components;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;


public class BtnOK extends JButton implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 1L;
    private static final java.awt.Color COLOR = new java.awt.Color(204, 255, 204);
    
    
    public BtnOK (){
        BtnOK.this.setBackground(COLOR);
        BtnOK.this.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 14)); // NOI18N
        //BtnOK.this.setIcon(new ScaleToFitAndAntialiasIcon( new ImageIcon(getClass().getResource("/iconsMe/ok.png")))); // NOI18N
        BtnOK.this.setText("OK");
        BtnOK.this.setPreferredSize(new Dimension(102, 31));
        BtnOK.this.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                UIManager.put( "Button.hoverBackground", COLOR.darker() );
                BtnOK.this.updateUI();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                UIManager.put( "Button.hoverBackground", null );
                BtnOK.this.updateUI();
            }   
        });
        BtnOK.this.addActionListener(e -> {
            UIManager.put( "Button.hoverBackground", null );
            BtnOK.this.updateUI();
        });
            
}
}
