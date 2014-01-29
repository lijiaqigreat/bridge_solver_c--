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
/* 13:   */ public class SiteCostTable
/* 14:   */   extends JTable
/* 15:   */ {
/* 16:33 */   final Font bold = UIManager.getFont("Label.font").deriveFont(1);
/* 17:   */   
/* 18:   */   public void initalize()
/* 19:   */   {
/* 20:39 */     for (int i = 0; i < 3; i++) {
/* 21:40 */       getColumnModel().getColumn(i).setCellRenderer(this.specialRender);
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:44 */   private DefaultTableCellRenderer specialRender = new DefaultTableCellRenderer()
/* 26:   */   {
/* 27:45 */     private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
/* 28:   */     
/* 29:   */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/* 30:   */     {
/* 31:49 */       if (column == 2) {
/* 32:50 */         value = this.currencyFormatter.format(value);
/* 33:   */       }
/* 34:52 */       super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/* 35:53 */       switch (column)
/* 36:   */       {
/* 37:   */       case 0: 
/* 38:55 */         setHorizontalAlignment(0);
/* 39:56 */         break;
/* 40:   */       case 1: 
/* 41:   */       case 2: 
/* 42:59 */         setHorizontalAlignment(4);
/* 43:   */       }
/* 44:62 */       if (row == table.getRowCount() - 1) {
/* 45:63 */         setFont(SiteCostTable.this.bold);
/* 46:   */       }
/* 47:65 */       return this;
/* 48:   */     }
/* 49:   */   };
/* 50:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.SiteCostTable
 * JD-Core Version:    0.7.0.1
 */