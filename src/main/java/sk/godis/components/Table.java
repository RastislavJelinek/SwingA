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

import static java.awt.Color.*;


/**
 *
 * @author Rastislav
 */
public class Table extends JTable implements MouseListener, ListSelectionListener ,java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private Color alternateRowColor = LIGHT_GRAY;
    private final List<Integer> selectedRows;
    private boolean mouseHold;
    private boolean multiSelectionAllow = true;
         
    public Table(){
        selectedRows = new ArrayList<>();
        setDefaultRenderer(BigDecimal.class, new cellRenderer(alternateRowColor));
        setDefaultRenderer(Object.class, new cellRenderer(alternateRowColor));
        setDefaultRenderer(Number.class, new cellRenderer(alternateRowColor));
        setDefaultRenderer(Boolean.class, new cellRenderer(alternateRowColor));
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addMouseListener(this);
        
        //Table.this.setSelectionForeground(Color.BLACK);
        Table.this.addKeyListener(new KeyAdapter(){
        @Override
            public void keyPressed(KeyEvent e) {
                DefaultTableModel model = (DefaultTableModel) getModel();
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    e.consume();
                    if(multiSelectionAllow){
                        int row = convertRowIndexToView(getSelectedRow());
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
        addPropertyChangeListener("model", evt -> debug());
    }
    
    

    
    public boolean isMultiSelectionAllow() {
        return multiSelectionAllow;
    }

    public void setMultiSelectionAllow(boolean SelThat) {
        this.multiSelectionAllow = SelThat;
    }
    
    // Getter
    public Color getAlternateRowColor() {
        return alternateRowColor;
      }
    
    public void setAlternateRowColor(Color newColor) {
            this.alternateRowColor = newColor;
            setDefaultRenderer(Object.class, new cellRenderer(alternateRowColor));
            setDefaultRenderer(Number.class, new cellRenderer(alternateRowColor));
            setDefaultRenderer(BigDecimal.class, new cellRenderer(alternateRowColor));
        }
    
    
    
    private void debug() {
        setAutoCreateRowSorter(true);
        getTableHeader().setReorderingAllowed(false);

        if(getColumnCount() > 0){
            ((DefaultRowSorter<?, ?>) getRowSorter()).setSortable(0, false);
            getColumnModel().getColumn(0).setMaxWidth(20);
            getColumnModel().getColumn(0).setResizable(false);
            getColumnModel().getColumn(0).setHeaderValue(null);
        }

        //setDefaultEditor(Object.class, null);
        
        getTableHeader().addMouseListener(new MouseAdapter() {
            private SortOrder currentOrder = SortOrder.UNSORTED;
            private int lastCol = -1;

            @Override
            public void mouseClicked(MouseEvent e) {
                int column = getTableHeader().columnAtPoint(e.getPoint());
                if(((DefaultRowSorter<?, ?>) getRowSorter()).isSortable(column)){
                    TableColumn tableColumn = getColumnModel().getColumn(column);
                    column = convertColumnIndexToModel(column);
                    if (column != lastCol) {
                        currentOrder = SortOrder.UNSORTED;
                        if(lastCol != -1){
                            TableColumn lastTableColumn = getColumnModel().getColumn(lastCol);
                            lastTableColumn.setHeaderRenderer(getTableHeader().getDefaultRenderer());
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
                            tableColumn.setHeaderRenderer(new HeaderHighlightRenderer(getTableHeader().getDefaultRenderer()));
                        }else{
                            tableColumn.setHeaderRenderer(getTableHeader().getDefaultRenderer());
                        }

                    }
                }
            }
        });
        
    }



    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        DefaultTableModel model = (DefaultTableModel)getModel();
        int anchorSelectionIndex = getSelectionModel().getAnchorSelectionIndex();
        if(!selectedRows.contains(anchorSelectionIndex)){
            getSelectionModel().removeSelectionInterval(anchorSelectionIndex, anchorSelectionIndex);
        }

        if (columnIndex != 0 || mouseHold || !multiSelectionAllow) {
            super.changeSelection(rowIndex, columnIndex, toggle, extend);
            return;
        }

        int rowIndexToView = convertRowIndexToView(rowIndex);

        boolean contains = selectedRows.contains(rowIndexToView);
        model.setValueAt(contains ? null : ">", rowIndexToView, 0);
        if (contains) {
            selectedRows.remove(Integer.valueOf(rowIndexToView));
        } else {
            selectedRows.add(rowIndexToView);
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



    private class cellRenderer extends DefaultTableCellRenderer {

        @Serial
        private static final long serialVersionUID = 1L;

        private final Color alternateRowColor;

        public cellRenderer(Color g){
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