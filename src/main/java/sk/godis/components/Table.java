package sk.godis.components;

import javax.swing.*;
import javax.swing.RowSorter.SortKey;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SortOrder.*;


/**
 *
 * @author Rastislav
 */
public class Table extends JTable implements MouseListener, ListSelectionListener ,java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private Color alternateRowColor = Color.LIGHT_GRAY;
    private final List<Integer> selectedRows;
    private boolean debug; 
    private boolean mouseHold;
    private boolean multySelectionAllow = true;
         
    public Table(){ 
        this.selectedRows = new ArrayList<>();
        Table.this.setDefaultRenderer(BigDecimal.class, new tableRenderer(alternateRowColor));
        Table.this.setDefaultRenderer(Object.class, new tableRenderer(alternateRowColor));
        Table.this.setDefaultRenderer(Number.class, new tableRenderer(alternateRowColor));
        Table.this.setDefaultRenderer(Boolean.class, new tableRenderer(alternateRowColor));
        Table.this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Table.this.addMouseListener(Table.this);
        
        //Table.this.setSelectionForeground(Color.BLACK);
        Table.this.addKeyListener(new KeyAdapter(){
        @Override
            public void keyPressed(KeyEvent e) {
                DefaultTableModel model = (DefaultTableModel)Table.this.getModel();
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    e.consume();
                    if(multySelectionAllow){
                        int row = Table.this.convertRowIndexToView(Table.this.getSelectedRow());
                        if(selectedRows.contains(row)){
                            model.setValueAt(null, row, 0);
                            selectedRows.remove(Integer.valueOf(row));
                        }else{
                            model.setValueAt(">", row, 0);
                            selectedRows.add(row);
                        }
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_TAB){
                    e.consume();
                    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    manager.focusNextComponent();
                }
                /*if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    e.consume();
                }*/
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    e.consume();
                }
                 
                 
                 
                 
            }
        
        });
    }
    
    
    // Getter
    public boolean isDebug() {
        return debug;
      }
    
    public void setDebug(boolean debug) {
           Table.this.debug = debug;
           Debug(debug);
        }
    
    
    public boolean isMultySelectionAllow() {
        return multySelectionAllow;
    }

    public void setMultySelectionAllow(boolean SelThat) {
        Table.this.multySelectionAllow = SelThat;
    }
    
    // Getter
    public Color getAlternateRowColor() {
        return alternateRowColor;
      }
    
    public void setAlternateRowColor(Color newColor) {
            Table.this.alternateRowColor = newColor;
            Table.this.setDefaultRenderer(Object.class, new tableRenderer(alternateRowColor));
            Table.this.setDefaultRenderer(Number.class, new tableRenderer(alternateRowColor));
            Table.this.setDefaultRenderer(BigDecimal.class, new tableRenderer(alternateRowColor));
        }
    
    
    
    public void Debug(boolean debug) {
        Table.this.setAutoCreateRowSorter(true);
        Table.this.getTableHeader().setReorderingAllowed(false);
        
        if(this.getColumnCount() > 0){
            ((DefaultRowSorter<?, ?>) getRowSorter()).setSortable(0, false);
            getColumnModel().getColumn(0).setMaxWidth(20);
            getColumnModel().getColumn(0).setResizable(false);
            getColumnModel().getColumn(0).setHeaderValue(null);
        }
        
        //setDefaultEditor(Object.class, null);
        
        getTableHeader().addMouseListener(new MouseAdapter() {
            private SortOrder   currentOrder    = SortOrder.UNSORTED;
            private int         lastCol         = -1;

            @Override
            public void mouseClicked(MouseEvent e) {
                int column = getTableHeader().columnAtPoint(e.getPoint());
                if(((DefaultRowSorter<?, ?>) getRowSorter()).isSortable(column)){
                    TableColumn tableColumn = Table.this.getColumnModel().getColumn(column);
                    column = convertColumnIndexToModel(column);
                    if (column != lastCol) {
                        currentOrder = SortOrder.UNSORTED;
                        if(lastCol != -1){
                            TableColumn lastTableColumn = Table.this.getColumnModel().getColumn(lastCol);
                            lastTableColumn.setHeaderRenderer(Table.this.getTableHeader().getDefaultRenderer());
                        }
                        lastCol = column;
                    }
                    RowSorter<?> sorter = getRowSorter();
                    List<SortKey> sortKeys = new ArrayList<>();
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        switch (currentOrder) {
                            case UNSORTED -> sortKeys.add(new RowSorter.SortKey(column, currentOrder = SortOrder.ASCENDING));
                            case ASCENDING -> sortKeys.add(new RowSorter.SortKey(column, currentOrder = SortOrder.DESCENDING));
                            case DESCENDING -> sortKeys.add(new RowSorter.SortKey(column, currentOrder = SortOrder.UNSORTED));
                        }
                        sorter.setSortKeys(sortKeys);

                        if(currentOrder != SortOrder.UNSORTED){
                            tableColumn.setHeaderRenderer(new HeaderHighlightRenderer(Table.this.getTableHeader().getDefaultRenderer()));
                        }else{
                            tableColumn.setHeaderRenderer(Table.this.getTableHeader().getDefaultRenderer());
                        }

                    }
                }
            }
        });
        
    }
    
    
    
    
    
    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        //super.changeSelection(rowIndex, columnIndex, toggle, extend);
         DefaultTableModel model = (DefaultTableModel)Table.this.getModel();
    
        if(!selectedRows.contains(this.getSelectionModel().getAnchorSelectionIndex())){
            this.getSelectionModel().removeSelectionInterval(this.getSelectionModel().getAnchorSelectionIndex(), this.getSelectionModel().getAnchorSelectionIndex());    
        }
        
            if(columnIndex == 0){
                if(!mouseHold && multySelectionAllow){
                    if(selectedRows.contains(this.convertRowIndexToView(rowIndex))){
                        model.setValueAt(null, this.convertRowIndexToView(rowIndex), 0);
                        selectedRows.remove(Integer.valueOf(this.convertRowIndexToView(rowIndex)));
                    }else{
                        model.setValueAt(">", this.convertRowIndexToView(rowIndex), 0);
                        selectedRows.add(this.convertRowIndexToView(rowIndex));
                    }
                }
            }
            super.changeSelection(rowIndex, columnIndex, toggle, extend);

           
        
   }
    
    
    
    
    
    
    

    @Override
    public void mouseClicked(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseHold = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseHold = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    
    private class tableRenderer extends DefaultTableCellRenderer {

        @Serial
        private static final long serialVersionUID = 1L;
        
        private final Color alternateRowColor;
        
        public tableRenderer(Color g){
            this.alternateRowColor = g;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            /*DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment( JLabel.CENTER );
            table.setDefaultRenderer(String.class, centerRenderer);
            */
            if(value != null){
                if(value.getClass().equals(String.class)){
                    comp.setHorizontalAlignment(LEFT);
                }
                if(value.getClass().equals(Integer.class)){
                    comp.setHorizontalAlignment(RIGHT);
                }
                if(value.getClass().equals(BigDecimal.class)){
                    comp.setHorizontalAlignment(RIGHT);
                }
            }
            
            if (isSelected){
                //System.out.println(row);
                if(column != 0){
                    comp.setBackground(Color.red);
                    
                }
                
                if(table.getSelectionModel().getLeadSelectionIndex() == row && column != 0){
                    comp.setForeground(Color.BLACK);
                    comp.setBackground(Color.CYAN);
                }
                
                if(column == 0){
                    comp.setForeground(Color.BLACK);
                    comp.setBackground(table.getTableHeader().getBackground());
                    if(value != null)
                      comp.setBackground(Color.red);  
                }
            }else{
                if(selectedRows.contains(table.convertRowIndexToView(row))){
                    if(column != 0){
                        comp.setBackground(Color.red);

                    }
                    if(column == 0){
                        comp.setForeground(Color.BLACK);
                        comp.setBackground(table.getTableHeader().getBackground());
                        if(value != null)
                          comp.setBackground(Color.red);  
                    }
                
                }else{
                    if(row%2==0 && column != 0){
                        comp.setBackground(null);
                    }else{
                        comp.setBackground(alternateRowColor);
                    }
                    if(column == 0){
                        comp.setBackground(table.getTableHeader().getBackground());
                    }
                }
            }
            return comp;
        }
    }
    

    private static class HeaderHighlightRenderer implements TableCellRenderer {
     
    private final TableCellRenderer defaultRenderer;
     
    public HeaderHighlightRenderer(TableCellRenderer defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
    }
     
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
        JComponent comp = (JComponent) defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        comp.setForeground(Color.white);
        comp.setBackground(Color.decode("#46863F"));
        comp.setBorder(new LineBorder(Color.ORANGE, 2, true));
        comp.setForeground(Color.white);
        
        return comp;
    }
 
}

}