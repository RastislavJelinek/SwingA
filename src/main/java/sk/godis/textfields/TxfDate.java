package sk.godis.textfields;


import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_DELETE;


public class TxfDate extends JTextField implements KeyListener,FocusListener{

    int caretPosition;  
    private static char dateSeparator = '.';
    private DateTimeFormatter dateFormatter;
    private static final LocalDate now = LocalDate.now();

public TxfDate(){
        addKeyListener(this); 
        addFocusListener(this);
        setFont(new java.awt.Font("Tahoma", Font.PLAIN, 14));
        setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        setPreferredSize(new Dimension(102, 31));
        changeFormat();
    }

// Getter
  public char getDateSeparator() {
    return dateSeparator;
  }

  // Setter
  public void setDateSeparator(char newDateSeparator) {
    TxfDate.dateSeparator = newDateSeparator;
      changeFormat();
  }
  private void changeFormat(){
      dateFormatter = DateTimeFormatter.ofPattern("dd" + dateSeparator + "MM" + dateSeparator + "yyyy");
      setText(dateFormatter.format(now));
  }
  
  // Setter
  public void setToday() {
    setText(dateFormatter.format(now));
  }
  
      
private boolean dateValidation(String date){
    if (!checkDate(date))return false;
    StringBuilder stringBuilder = new StringBuilder(date);
    int day = 32;
    int i = 0;
    do{
        try {
            LocalDate.parse(stringBuilder.toString(), dateFormatter);
            break;
        } catch (DateTimeParseException e) {
           --day;
            stringBuilder.replace(0, 2, String.valueOf(day)); 
            ++i;
        }
    }while(i < 100);
    if(i >= 100)return false;
    int caretHolder = getCaretPosition();
    setText(stringBuilder.toString());
    setCaretPosition(caretHolder);
    return true;
    }
    
    
    private boolean checkDate(String date) {
        String patternDay = "(\\d\\d)\\"+ dateSeparator +"(0?[1-9]|1[0-2])\\"+ dateSeparator +"(\\d{4})";
        return date.matches(patternDay);
    }
    
                           
    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    
    
    @Override
    public void keyPressed(KeyEvent e) {
        char typedChar = e.getKeyChar();
        
        if (typedChar != VK_BACK_SPACE && typedChar != VK_DELETE && !Character.isDigit(typedChar)){ 
            return;
        }
        if (typedChar == VK_BACK_SPACE || typedChar == VK_DELETE || Character.isDigit(typedChar)){ 
             e.consume();
        }

        caretPosition = getSelectionStart();
        
        if(typedChar == KeyEvent.VK_BACK_SPACE && caretPosition <= 0)
            return;
        if((typedChar == KeyEvent.VK_DELETE  || Character.isDigit(typedChar)) 
                && caretPosition >= getText().length())
            return;
        
        
        StringBuilder stringBuilder = new StringBuilder(getText());
        int offset = 0;
        
        if (Character.isDigit(typedChar)) {
            if (caretPosition == 2 || caretPosition == 5)
                offset = 1;

            stringBuilder.setCharAt(caretPosition + offset, e.getKeyChar());
            setText(stringBuilder.toString());
            setCaretPosition(caretPosition + offset + 1);
            if(caretPosition == 5){
                dateValidation(getText());
            }
            setBackground(checkDate(getText()) ? null : Color.yellow);
            return;
        } 
        
        switch (typedChar) {
        case KeyEvent.VK_BACK_SPACE -> --caretPosition;
        case KeyEvent.VK_DELETE -> offset = 1;
        }
        
        
        if (caretPosition != 2 && caretPosition != 5){
            stringBuilder.setCharAt(caretPosition, '0');
            setText(stringBuilder.toString());
            setBackground(checkDate(getText()) ? null : Color.yellow);
        }
        setCaretPosition(caretPosition + offset);
    }

    
    
    @Override
    public void keyReleased(KeyEvent e) {
        e.consume();
    }
    
    

    @Override
    public void focusGained(FocusEvent e) {
        this.setCaretPosition(0);
    }


    @Override
    public void focusLost(FocusEvent e) {
        if(!dateValidation(getText())){
            setBackground(Color.red);    
            requestFocusInWindow();
        }   
    }
}