/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.Component;
/*  4:   */ import java.awt.Font;
/*  5:   */ import java.text.NumberFormat;
/*  6:   */ import java.util.Locale;
/*  7:   */ import javax.swing.JTable;
/*  8:   */ import javax.swing.UIManager;
/*  9:   */ import javax.swing.table.DefaultTableCellRenderer;
/* 10:   */ import javax.swing.table.TableColumn;
/* 11:   */ import javax.swing.table.TableColumnModel;
/* 12:   */ 
/* 13:   */ public class CostReportTable
/* 14:   */   extends JTable
/* 15:   */ {
/* 16:32 */   private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
/* 17:33 */   private Font bold = UIManager.getFont("Label.font").deriveFont(1);
/* 18:   */   
/* 19:   */   public int initalize()
/* 20:   */   {
/* 21:41 */     for (int i = 0; i < 4; i++) {
/* 22:42 */       getColumnModel().getColumn(i).setCellRenderer(this.specialRender);
/* 23:   */     }
/* 24:45 */     for (int i = 0; i < getRowCount(); i++) {
/* 25:46 */       setRowHeight(i, getValueAt(i, 3) == null ? 2 : getRowHeight());
/* 26:   */     }
/* 27:48 */     return AutofitTableColumns.autoResizeTable(this, true, 4);
/* 28:   */   }
/* 29:   */   
/* 30:51 */   private DefaultTableCellRenderer specialRender = new DefaultTableCellRenderer()
/* 31:   */   {
/* 32:   */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/* 33:   */     {
/* 34:55 */       if ((column == 3) && (value != null)) {
/* 35:56 */         value = CostReportTable.this.currencyFormatter.format(value);
/* 36:   */       }
/* 37:58 */       super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/* 38:59 */       switch (column)
/* 39:   */       {
/* 40:   */       case 0: 
/* 41:   */       case 1: 
/* 42:62 */         setHorizontalAlignment(10);
/* 43:63 */         break;
/* 44:   */       case 2: 
/* 45:   */       case 3: 
/* 46:66 */         setHorizontalAlignment(4);
/* 47:   */       }
/* 48:69 */       if (row == table.getRowCount() - 1) {
/* 49:70 */         setFont(CostReportTable.this.bold);
/* 50:   */       }
/* 51:72 */       return this;
/* 52:   */     }
/* 53:   */   };
/* 54:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.CostReportTable
 * JD-Core Version:    0.7.0.1
 */