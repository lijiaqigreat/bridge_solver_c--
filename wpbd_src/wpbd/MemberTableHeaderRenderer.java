/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Component;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.List;
/*   7:    */ import javax.swing.Icon;
/*   8:    */ import javax.swing.JTable;
/*   9:    */ import javax.swing.RowSorter;
/*  10:    */ import javax.swing.RowSorter.SortKey;
/*  11:    */ import javax.swing.SortOrder;
/*  12:    */ import javax.swing.UIManager;
/*  13:    */ import javax.swing.border.Border;
/*  14:    */ import javax.swing.plaf.UIResource;
/*  15:    */ import javax.swing.table.DefaultTableCellRenderer;
/*  16:    */ import javax.swing.table.JTableHeader;
/*  17:    */ import javax.swing.table.TableColumn;
/*  18:    */ import javax.swing.table.TableColumnModel;
/*  19:    */ 
/*  20:    */ public class MemberTableHeaderRenderer
/*  21:    */   extends DefaultTableCellRenderer
/*  22:    */   implements UIResource
/*  23:    */ {
/*  24:    */   public MemberTableHeaderRenderer()
/*  25:    */   {
/*  26: 30 */     setHorizontalAlignment(0);
/*  27: 31 */     setHorizontalTextPosition(0);
/*  28: 32 */     setVerticalTextPosition(1);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/*  32:    */   {
/*  33: 50 */     Icon sortIcon = null;
/*  34:    */     
/*  35: 52 */     boolean isPaintingForPrint = false;
/*  36: 54 */     if (table != null)
/*  37:    */     {
/*  38: 55 */       JTableHeader header = table.getTableHeader();
/*  39: 56 */       if (header != null)
/*  40:    */       {
/*  41: 57 */         Color fgColor = null;
/*  42: 58 */         Color bgColor = null;
/*  43: 59 */         if (hasFocus)
/*  44:    */         {
/*  45: 60 */           fgColor = UIManager.getColor("TableHeader.focusCellForeground");
/*  46: 61 */           bgColor = UIManager.getColor("TableHeader.focusCellBackground");
/*  47:    */         }
/*  48: 63 */         if (fgColor == null) {
/*  49: 64 */           fgColor = header.getForeground();
/*  50:    */         }
/*  51: 66 */         if (bgColor == null) {
/*  52: 67 */           bgColor = header.getBackground();
/*  53:    */         }
/*  54: 69 */         setForeground(fgColor);
/*  55: 70 */         setBackground(bgColor);
/*  56:    */         
/*  57: 72 */         setFont(header.getFont());
/*  58:    */         
/*  59: 74 */         isPaintingForPrint = header.isPaintingForPrint();
/*  60:    */       }
/*  61: 77 */       if ((!isPaintingForPrint) && (table.getRowSorter() != null))
/*  62:    */       {
/*  63: 78 */         SortOrder sortOrder = getColumnSortOrder(table, column);
/*  64: 79 */         if (sortOrder == null) {
/*  65: 80 */           sortIcon = WPBDApp.getApplication().getIconResource("sortnull.png");
/*  66:    */         } else {
/*  67: 82 */           switch (1.$SwitchMap$javax$swing$SortOrder[sortOrder.ordinal()])
/*  68:    */           {
/*  69:    */           case 1: 
/*  70: 84 */             sortIcon = WPBDApp.getApplication().getIconResource("sortascending.png");
/*  71: 85 */             break;
/*  72:    */           case 2: 
/*  73: 87 */             sortIcon = WPBDApp.getApplication().getIconResource("sortdescending.png");
/*  74: 88 */             break;
/*  75:    */           case 3: 
/*  76: 90 */             sortIcon = WPBDApp.getApplication().getIconResource("sortneutral.png");
/*  77:    */           }
/*  78:    */         }
/*  79:    */       }
/*  80:    */     }
/*  81: 97 */     setText(value == null ? "" : value.toString());
/*  82: 98 */     setIcon(sortIcon);
/*  83:    */     
/*  84:100 */     Border border = null;
/*  85:101 */     if (hasFocus) {
/*  86:102 */       border = UIManager.getBorder("TableHeader.focusCellBorder");
/*  87:    */     }
/*  88:104 */     if (border == null) {
/*  89:105 */       border = UIManager.getBorder("TableHeader.cellBorder");
/*  90:    */     }
/*  91:107 */     setBorder(border);
/*  92:    */     
/*  93:109 */     return this;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static SortOrder getColumnSortOrder(JTable table, int column)
/*  97:    */   {
/*  98:121 */     SortOrder rv = null;
/*  99:122 */     if (table.getRowSorter() != null)
/* 100:    */     {
/* 101:123 */       List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();
/* 102:124 */       if ((sortKeys.size() > 0) && (((RowSorter.SortKey)sortKeys.get(0)).getColumn() == table.convertColumnIndexToModel(column))) {
/* 103:125 */         rv = ((RowSorter.SortKey)sortKeys.get(0)).getSortOrder();
/* 104:    */       }
/* 105:    */     }
/* 106:128 */     return rv;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void installIn(JTable table)
/* 110:    */   {
/* 111:137 */     Enumeration<TableColumn> e = table.getColumnModel().getColumns();
/* 112:138 */     while (e.hasMoreElements()) {
/* 113:139 */       ((TableColumn)e.nextElement()).setHeaderRenderer(this);
/* 114:    */     }
/* 115:    */   }
/* 116:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.MemberTableHeaderRenderer
 * JD-Core Version:    0.7.0.1
 */