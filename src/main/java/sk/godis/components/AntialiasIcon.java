/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sk.godis.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Rastislav
 */
public class AntialiasIcon extends ImageIcon{
    private final ImageIcon icon;
    private int AlligmentX,AlligmentY;
    
    
    

    public AntialiasIcon(ImageIcon icon,int AlligmentX,int AlligmentY)
    {
        this.icon = icon;
        this.AlligmentX = AlligmentX;
        this.AlligmentY = AlligmentY;
    }
    
    public void setIconAlligmentX(int AlligmentX)
    {
        this.AlligmentX = AlligmentX;
    }

    public void setIconAlligmentY(int AlligmentY)
    {
        this.AlligmentY = AlligmentY;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        Graphics2D g2d = (Graphics2D)g.create();
        AffineTransform at = g2d.getTransform();
        
        double scaleToFit = ((double)c.getHeight() / (double)icon.getIconHeight());
        
        
        if(icon.getIconHeight()*scaleToFit == c.getHeight()){
            scaleToFit = ((double)c.getHeight() / (double)icon.getIconHeight()) - 0.1;
        }

        AffineTransform scaled = AffineTransform.getScaleInstance(scaleToFit, scaleToFit);
        at.concatenate( scaled );
        g2d.setTransform( at );
        
        
        
        
        //int lineupMinus = (int)((double)c.getHeight() - ((double)icon.getIconHeight() * scaleToFit));
       /* int lineupMinus = (int)((double)icon.getIconWidth() *((double)c.getHeight() / (double)icon.getIconHeight()));
        int lineup = (int)((double)icon.getIconWidth() * scaleToFit);
        
        int ff = (int)(lineupMinus - lineup);
        System.out.println(ff);
        */
        //System.out.println(lineupMinus);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        icon.paintIcon(c, g2d, AlligmentX,AlligmentY);
        
        
        g2d.dispose();
    }


}
