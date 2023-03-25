package sk.godis.components;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_DELETE;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

        
public class TxfDate extends JTextField implements KeyListener,FocusListener{

    int caretPosition;  
    private static char dateSeparator = '.';
    private DateFormat dateFormat = new SimpleDateFormat("dd"+dateSeparator+"MM"+dateSeparator+"yyyy");
    private static final Date now = Calendar.getInstance().getTime(); 
    
    /* upgrade Date now to new java.time
    private static final LocalDate k = LocalDate.now();
*/

public TxfDate(){
        addKeyListener(this); 
        addFocusListener(this);
        setFont(new java.awt.Font("Tahoma", 0, 14));
        setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        setPreferredSize(new Dimension(102, 31));
        dateFormat = new SimpleDateFormat("dd"+dateSeparator+"MM"+dateSeparator+"yyyy");
        dateFormat.setLenient(false);
        setText(dateFormat.format(now));
        
        PlainDocument doc = (PlainDocument) getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

// Getter
  public char getDateSeparator() {
    return dateSeparator;
  }

  // Setter
  public void setDateSeparator(char newDateSeparator) {
    TxfDate.dateSeparator = newDateSeparator;
    dateFormat = new SimpleDateFormat("dd"+dateSeparator+"MM"+dateSeparator+"yyyy");
    setText(dateFormat.format(now));
  }
  
  // Setter
  public void setToday() {
    setText(dateFormat.format(now));
  }
  
      
private boolean dateValidation(String date){
    if (!checkDate(date))return false;
    StringBuilder stringBuilder = new StringBuilder(date);
    int day = 32;
    int i = 0;
    do{
        ++i;
        try {
            dateFormat.parse(stringBuilder.toString());
            break;
        } catch (ParseException e) {
            --day;
            stringBuilder.replace(0, 2, String.valueOf(day)); 
        }
    }while(i < 100);
    if(i >= 100)return false;
    int caretHolder = getCaretPosition();
    setText(stringBuilder.toString());
    setCaretPosition(caretHolder);
    return true;
    }
    
    
    private boolean checkDate(String date) {
        String patternDay = "([0-9][0-9])\\"+ dateSeparator +"(0?[1-9]|1[0-2])\\"+ dateSeparator +"([0-9]{4})";
        return date.matches(patternDay);
  }
    public boolean isDateValid(){
        return dateValidation(getText());
    }
    

    @Override
    public void keyTyped(KeyEvent e) {  
        // Do nothing
    }

    
    
    @Override
    public void keyPressed(KeyEvent e) {
        char typedChar = e.getKeyChar();
        if(typedChar != VK_BACK_SPACE && typedChar != VK_DELETE && !Character.isDigit(typedChar))return;
        
        caretPosition = getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder(getText());   
        if(caretPosition < getText().length()){
            int offset = 0;
            if(Character.isDigit(typedChar)){
                //if press number before ',' replace first number after ','
                if(caretPosition == 2 || caretPosition == 5)offset = 1;
                stringBuilder.setCharAt(caretPosition + offset, e.getKeyChar());
            }
            //if try to DELLETE number, replace by 0
            if (typedChar == VK_DELETE){
                stringBuilder.setCharAt(caretPosition, '0');
            }
            
            setText(stringBuilder.toString());
            setCaretPosition(caretPosition +1 + offset);
            return;
        }
        
        //if try to BACKSPACE number, replace by 0
        if (typedChar == VK_BACK_SPACE && caretPosition > 0){
            stringBuilder.setCharAt(caretPosition -1, '0');
            setText(stringBuilder.toString());
            setCaretPosition(caretPosition -1);  
        } 
        
    }

    
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing
    }
    
    

    @Override
    public void focusGained(FocusEvent e) {
        this.setCaretPosition(0);
    }


    @Override
    public void focusLost(FocusEvent e) {
        if(dateValidation(getText())==false){
            setBackground(Color.red);    
            requestFocusInWindow();
        }   
    }

    
    
    
    class MyIntFilter extends DocumentFilter{
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
              AttributeSet attr) throws BadLocationException {
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
             AttributeSet attrs) throws BadLocationException {

             String pattern = "([0-9][0-9])\\"+ dateSeparator +"([0-9][0-9])\\"+ dateSeparator +"([0-9][0-9][0-9][0-9])";
             if(text.matches(pattern) && offset != 3){
                 super.replace(fb, offset, length, text, attrs);
             }
        }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
        }
    }
}
