/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.Component;
/*  4:   */ import javax.swing.JTable;
/*  5:   */ import javax.swing.table.DefaultTableCellRenderer;
/*  6:   */ import javax.swing.table.TableColumn;
/*  7:   */ import javax.swing.table.TableColumnModel;
/*  8:   */ import org.jdesktop.application.ResourceMap;
/*  9:   */ 
/* 10:   */ public class LoadTestReportTable
/* 11:   */   extends JTable
/* 12:   */ {
/* 13:   */   MemberTableCellRenderer specialCellRenderer;
/* 14:   */   
/* 15:   */   public LoadTestReportTable()
/* 16:   */   {
/* 17:31 */     this.specialCellRenderer = new MemberTableCellRenderer(null);
/* 18:   */   }
/* 19:   */   
/* 20:33 */   public static String cvsHeaders = "";
/* 21:   */   
/* 22:   */   public void initialize()
/* 23:   */   {
/* 24:39 */     ResourceMap resourceMap = WPBDApp.getResourceMap(LoadTestReportTable.class);
/* 25:40 */     for (int i = 0; i < LoadTestReportTableModel.columnNames.length; i++)
/* 26:   */     {
/* 27:41 */       TableColumn column = getColumnModel().getColumn(i);
/* 28:42 */       column.setResizable(true);
/* 29:43 */       String headerText = resourceMap.getString(LoadTestReportTableModel.columnNames[i] + "ColumnHeader.text", new Object[0]);
/* 30:44 */       column.setHeaderValue(headerText);
/* 31:45 */       int width = resourceMap.getInteger(LoadTestReportTableModel.columnNames[i] + "Column.width").intValue();
/* 32:46 */       column.setPreferredWidth(width);
/* 33:47 */       if (headerText.length() == 0)
/* 34:   */       {
/* 35:48 */         column.setMinWidth(width);
/* 36:49 */         column.setMaxWidth(width);
/* 37:   */       }
/* 38:52 */       column.setCellRenderer(this.specialCellRenderer);
/* 39:   */     }
/* 40:54 */     cvsHeaders = resourceMap.getString("cvsHeaders.text", new Object[0]);
/* 41:   */   }
/* 42:   */   
/* 43:   */   private class MemberTableCellRenderer
/* 44:   */     extends DefaultTableCellRenderer
/* 45:   */   {
/* 46:   */     private MemberTableCellRenderer() {}
/* 47:   */     
/* 48:   */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/* 49:   */     {
/* 50:61 */       super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/* 51:62 */       switch (column)
/* 52:   */       {
/* 53:   */       case 0: 
/* 54:   */       case 6: 
/* 55:   */       case 7: 
/* 56:   */       case 10: 
/* 57:   */       case 11: 
/* 58:68 */         setHorizontalAlignment(4);
/* 59:69 */         break;
/* 60:   */       case 1: 
/* 61:   */       case 2: 
/* 62:   */       case 3: 
/* 63:   */       case 4: 
/* 64:   */       case 5: 
/* 65:   */       case 8: 
/* 66:   */       case 9: 
/* 67:   */       default: 
/* 68:71 */         setHorizontalAlignment(0);
/* 69:   */       }
/* 70:74 */       return this;
/* 71:   */     }
/* 72:   */   }
/* 73:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.LoadTestReportTable
 * JD-Core Version:    0.7.0.1
 */