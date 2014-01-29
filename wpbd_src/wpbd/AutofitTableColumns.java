/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.Dimension;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.FontMetrics;
/*   7:    */ import javax.swing.JLabel;
/*   8:    */ import javax.swing.JTable;
/*   9:    */ import javax.swing.SwingUtilities;
/*  10:    */ import javax.swing.table.JTableHeader;
/*  11:    */ import javax.swing.table.TableCellRenderer;
/*  12:    */ import javax.swing.table.TableColumn;
/*  13:    */ import javax.swing.table.TableColumnModel;
/*  14:    */ import javax.swing.text.JTextComponent;
/*  15:    */ 
/*  16:    */ public class AutofitTableColumns
/*  17:    */ {
/*  18:    */   private static final int DEFAULT_COLUMN_PADDING = 5;
/*  19:    */   
/*  20:    */   public static int autoResizeTable(JTable table, boolean includeColumnHeaderWidth)
/*  21:    */   {
/*  22: 49 */     return autoResizeTable(table, includeColumnHeaderWidth, 5);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static int autoResizeTable(JTable table, boolean includeColumnHeaderWidth, int columnPadding)
/*  26:    */   {
/*  27: 61 */     int columnCount = table.getColumnCount();
/*  28: 62 */     int tableWidth = 0;
/*  29:    */     
/*  30: 64 */     Dimension cellSpacing = table.getIntercellSpacing();
/*  31: 66 */     if (columnCount > 0)
/*  32:    */     {
/*  33: 68 */       int[] columnWidth = new int[columnCount];
/*  34: 70 */       for (int i = 0; i < columnCount; i++)
/*  35:    */       {
/*  36: 71 */         columnWidth[i] = getMaxColumnWidth(table, i, includeColumnHeaderWidth, columnPadding);
/*  37: 72 */         tableWidth += columnWidth[i];
/*  38:    */       }
/*  39: 76 */       tableWidth += (columnCount - 1) * cellSpacing.width;
/*  40:    */       
/*  41:    */ 
/*  42: 79 */       JTableHeader tableHeader = table.getTableHeader();
/*  43:    */       
/*  44: 81 */       Dimension headerDim = tableHeader.getPreferredSize();
/*  45:    */       
/*  46:    */ 
/*  47: 84 */       headerDim.width = tableWidth;
/*  48: 85 */       tableHeader.setPreferredSize(headerDim);
/*  49:    */       
/*  50: 87 */       TableColumnModel tableColumnModel = table.getColumnModel();
/*  51: 90 */       for (int i = 0; i < columnCount; i++)
/*  52:    */       {
/*  53: 91 */         TableColumn tableColumn = tableColumnModel.getColumn(i);
/*  54: 92 */         tableColumn.setPreferredWidth(columnWidth[i]);
/*  55:    */       }
/*  56: 95 */       table.invalidate();
/*  57: 96 */       table.doLayout();
/*  58: 97 */       table.repaint();
/*  59:    */     }
/*  60:100 */     return tableWidth;
/*  61:    */   }
/*  62:    */   
/*  63:    */   private static int getMaxColumnWidth(JTable table, int columnNo, boolean includeColumnHeaderWidth, int columnPadding)
/*  64:    */   {
/*  65:115 */     TableColumn column = table.getColumnModel().getColumn(columnNo);
/*  66:116 */     Component comp = null;
/*  67:117 */     int maxWidth = 0;
/*  68:118 */     if (includeColumnHeaderWidth)
/*  69:    */     {
/*  70:119 */       TableCellRenderer headerRenderer = column.getHeaderRenderer();
/*  71:120 */       if (headerRenderer != null)
/*  72:    */       {
/*  73:121 */         comp = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, columnNo);
/*  74:122 */         if ((comp instanceof JTextComponent))
/*  75:    */         {
/*  76:123 */           JTextComponent jtextComp = (JTextComponent)comp;
/*  77:    */           
/*  78:125 */           String text = jtextComp.getText();
/*  79:126 */           Font font = jtextComp.getFont();
/*  80:127 */           FontMetrics fontMetrics = jtextComp.getFontMetrics(font);
/*  81:    */           
/*  82:129 */           maxWidth = SwingUtilities.computeStringWidth(fontMetrics, text);
/*  83:    */         }
/*  84:    */         else
/*  85:    */         {
/*  86:131 */           maxWidth = comp.getPreferredSize().width;
/*  87:    */         }
/*  88:    */       }
/*  89:    */       else
/*  90:    */       {
/*  91:    */         try
/*  92:    */         {
/*  93:135 */           String headerText = (String)column.getHeaderValue();
/*  94:136 */           JLabel defaultLabel = new JLabel(headerText);
/*  95:    */           
/*  96:138 */           Font font = defaultLabel.getFont();
/*  97:139 */           FontMetrics fontMetrics = defaultLabel.getFontMetrics(font);
/*  98:    */           
/*  99:141 */           maxWidth = SwingUtilities.computeStringWidth(fontMetrics, headerText);
/* 100:    */         }
/* 101:    */         catch (ClassCastException ce)
/* 102:    */         {
/* 103:144 */           maxWidth = 0;
/* 104:    */         }
/* 105:    */       }
/* 106:    */     }
/* 107:150 */     int cellWidth = 0;
/* 108:151 */     for (int i = 0; i < table.getRowCount(); i++)
/* 109:    */     {
/* 110:152 */       TableCellRenderer tableCellRenderer = table.getCellRenderer(i, columnNo);
/* 111:    */       
/* 112:154 */       comp = tableCellRenderer.getTableCellRendererComponent(table, table.getValueAt(i, columnNo), false, false, i, columnNo);
/* 113:156 */       if ((comp instanceof JTextComponent))
/* 114:    */       {
/* 115:157 */         JTextComponent jtextComp = (JTextComponent)comp;
/* 116:    */         
/* 117:159 */         String text = jtextComp.getText();
/* 118:160 */         Font font = jtextComp.getFont();
/* 119:161 */         FontMetrics fontMetrics = jtextComp.getFontMetrics(font);
/* 120:    */         
/* 121:163 */         int textWidth = SwingUtilities.computeStringWidth(fontMetrics, text);
/* 122:    */         
/* 123:165 */         maxWidth = Math.max(maxWidth, textWidth);
/* 124:    */       }
/* 125:    */       else
/* 126:    */       {
/* 127:167 */         cellWidth = comp.getPreferredSize().width;
/* 128:    */         
/* 129:    */ 
/* 130:170 */         maxWidth = Math.max(maxWidth, cellWidth);
/* 131:    */       }
/* 132:    */     }
/* 133:174 */     return maxWidth + columnPadding;
/* 134:    */   }
/* 135:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.AutofitTableColumns
 * JD-Core Version:    0.7.0.1
 */