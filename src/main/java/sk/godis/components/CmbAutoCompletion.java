package sk.godis.components;

import javax.swing.*;
import java.awt.event.*;
import java.io.Serial;


public class CmbAutoCompletion extends JComboBox<Object> implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 1L;
    
    public CmbAutoCompletion(){
        
        CmbAutoCompletion.this.getEditor().setItem(0);
        CmbAutoCompletion.this.setEditable(true);
        CmbAutoCompletion.this.addMouseListener(new MouseAdapter(){
            public void keyPressed(KeyEvent e) {
                if (CmbAutoCompletion.this.isDisplayable()) {
                    CmbAutoCompletion.this.setPopupVisible(true);
                }


            }
        });
   }
    
}