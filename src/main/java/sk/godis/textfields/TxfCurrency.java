package sk.godis.textfields;


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serial;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static javax.swing.SwingConstants.RIGHT;

public class TxfCurrency extends JTextField implements KeyListener, FocusListener, java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int selectionStart, selectionEnd, textLength, maxNumber = 10, maxDecimal = 2, dotIndex;
    private static StringBuilder stringBuilder;
    private boolean minusSign;
    private static final Color BASE_TEXTFIELD_COLOR = UIManager.getColor ( "TextField.background" );

    public TxfCurrency(){
        addKeyListener(this);
        addFocusListener(this);
        setHorizontalAlignment(RIGHT);
        setFont(new java.awt.Font("Tahoma", Font.PLAIN, 14));
        setPreferredSize(new Dimension(102, 31));
        setText("0."+ "0".repeat(maxDecimal));
        
        PlainDocument doc = (PlainDocument) getDocument();
        doc.setDocumentFilter(new MyIntFilter());
        
    }

    public BigDecimal getBigDecimal(){
        return new BigDecimal(this.getText());
    }
    
  // Getter
    public int getMaxNumber() {
        return maxNumber;
    }

  // Setter
    public void setMaxNumber(int newMaxNumber) {
        if(newMaxNumber > 0){
            maxNumber = newMaxNumber;
            TxfCurrency.this.setText("0."+ "0".repeat(maxDecimal));
        }
    }
  
  // Getter
    public int getMaxDecimal() {
        return maxDecimal;
    }

  // Setter
    public void setMaxDecimal(int newMaxDecimal) {
        maxDecimal = newMaxDecimal;
    }
  
    public void reset(){
        this.setText("0.00");
        this.setBackground(BASE_TEXTFIELD_COLOR);
    }
  

    private void updateData() {
        stringBuilder = new StringBuilder(getText());
        textLength = getText().length();
        dotIndex = textLength - maxDecimal -1;
        selectionStart = getSelectionStart();
        selectionEnd = getSelectionEnd();
    }
    
    
    private boolean zeroComparator(){
        BigDecimal a = new BigDecimal(stringBuilder.toString()).setScale(0 , RoundingMode.HALF_EVEN);
        if(a.compareTo(BigDecimal.ZERO) != 0)return false;
        setText(stringBuilder.toString());
        return true;
        
    }
    
    

    @Override
    public void keyPressed(KeyEvent e) {
        updateData();

        //prevents code to work only if is not sellection made
        if (selectionStart == selectionEnd) {
            //If DELLETE key is passed
            if (e.getKeyCode() == KeyEvent.VK_DELETE && selectionStart < textLength) {
                
                stringBuilder.replace(selectionStart, selectionStart + 1, "0");

                //if trying to DELLETE number befor decimal '.' and value will be less then 1, refill position by 0
                if (selectionStart == dotIndex -1){
                    zeroComparator();
                }
                
                if (selectionStart >= dotIndex -1) {
                    this.setText(stringBuilder.toString());
                    this.setCaretPosition(selectionStart + 1);
                }
            }
            //If BACK SPACE key is passed
            if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && selectionStart > 0) {
                
                stringBuilder.replace(selectionStart - 1, selectionStart, "0");
                
                //if trying to BACK SPACE number '0'.00 and value will be less then 1, refill position by 0
                if (selectionStart == dotIndex && zeroComparator()) {
                    this.setCaretPosition(selectionStart);
                }

                //if trying to BACK SPACE decimal number, refill position by 0 
                if (selectionStart > dotIndex) {
                    this.setText(stringBuilder.toString());
                    this.setCaretPosition(selectionStart - 1);
                }
                
            }
        }
        if (selectionStart != selectionEnd && (e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)) {
            e.consume();

            if(selectionEnd > dotIndex ){
                stringBuilder.replace(dotIndex + 1, selectionEnd, "0".repeat(selectionEnd - dotIndex -1));
            }

            if(selectionStart < dotIndex){
                stringBuilder.delete(selectionStart, Math.min(dotIndex, selectionEnd));
            }
            if(stringBuilder.charAt(0) == '.'){
                stringBuilder.insert(0, "0");
            }
            
            minusSign = stringBuilder.toString().contains("-");
            TxfCurrency.this.setText(stringBuilder.toString());
            TxfCurrency.this.setCaretPosition(selectionStart);
        
        }
        
    }
    
    
    private boolean firstNumberIsZero(){
        char position0 = getText().charAt(0);
        char position1 = this.getText().charAt(1);
        //return getText().matches("^-?[0](\\.)[0-9]{" + String.valueOf(maxDecimal) + "}");
        return (position0 == '0' || (minusSign && position1 == '0'));
    }
    
    

    @Override
    public void keyTyped(KeyEvent e) {
        updateData();
        
        

        //if trying to add '0' on position 0 consume, 
        if (e.getKeyChar() == '0' && (selectionStart == 0) || (selectionStart == 1 && minusSign)) {  
            e.consume();
            return;
        }
        
        
        //allow only 1 decimal dot in text (+ prevention for different keyboard, allow ',' and '.')
        if (e.getKeyChar() == '.' || e.getKeyChar() == ','){ //if you anywhere press '.' it will place your carret after decimal dot
            TxfCurrency.this.setCaretPosition(dotIndex + 1);
        }
        
        if(Character.isDigit(e.getKeyChar())){
            if (selectionStart == selectionEnd && selectionStart < textLength){
                //seting decimal numbers values
                if ((selectionStart == dotIndex -1 && firstNumberIsZero()) || (selectionStart > dotIndex )) {
                    e.consume();
                    stringBuilder.replace(selectionStart, selectionStart + 1, String.valueOf(e.getKeyChar()));
                    setText(stringBuilder.toString());
                    setCaretPosition(selectionStart + 1);
                }

                //seting 'X'.00 number if 0,00
                if (selectionStart == dotIndex && firstNumberIsZero()) {
                    e.consume();
                    stringBuilder.replace(selectionStart -1, selectionStart, String.valueOf(e.getKeyChar()));
                    setText(stringBuilder.toString());
                    setCaretPosition(selectionStart);
                }
            }


            if (selectionStart != selectionEnd){
                e.consume();

                if(selectionEnd > dotIndex ){
                    if(selectionStart < dotIndex)
                        stringBuilder.replace(dotIndex+1, selectionEnd, "0".repeat(selectionEnd - dotIndex -1));
                    else{
                        stringBuilder.replace(selectionStart, selectionEnd, e.getKeyChar() + "0".repeat(selectionEnd - selectionStart -1));
                    }
                }

                if(selectionStart < dotIndex){
                    stringBuilder.replace(selectionStart, Math.min(dotIndex, selectionEnd), String.valueOf(e.getKeyChar()));
                }
                
                minusSign = stringBuilder.toString().contains("-");

                TxfCurrency.this.setText(stringBuilder.toString());
                TxfCurrency.this.setCaretPosition(selectionStart + 1);


            }
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
        TxfCurrency.this.selectAll();
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
    
    
    
    class MyIntFilter extends DocumentFilter{
        
        
        
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
            AttributeSet attr) {
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException {
            String pattern = "^-?[0-9]{1," + maxNumber + "}(\\.)[0-9]{" + maxDecimal + "}";
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, doc.getLength()));

            if(text.matches("-")){
                if(doc.getText(0, doc.getLength()).contains("-")){
                    minusSign = false;
                    super.remove(fb, 0, 1);
                }else{
                    super.replace(fb, 0, 0, text, attrs);
                    minusSign = true;
                }
                return;
            }


            if(length == 0){
                sb.insert(offset, text);
                if(sb.toString().matches(pattern)){
                    super.replace(fb, offset, length, text, attrs);
                }
                return;
            }

            sb.replace(offset, offset + length, text);
            if(sb.toString().matches(pattern)){
                super.replace(fb, offset, length, text, attrs);
            }

       }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, doc.getLength()));
            sb.delete(offset, offset + length);
            minusSign = sb.toString().contains("-");
            String pattern = "^-?[0-9]{1," + maxNumber + "}(\\.)[0-9]{" + maxDecimal + "}";
            if(sb.toString().matches(pattern)){
                super.remove(fb, offset, length);
            }

        }


    }
}
