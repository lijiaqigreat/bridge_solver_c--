/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Component;
/*   5:    */ import java.text.DecimalFormat;
/*   6:    */ import java.text.NumberFormat;
/*   7:    */ import java.util.Locale;
/*   8:    */ import javax.swing.JTable;
/*   9:    */ import javax.swing.table.DefaultTableCellRenderer;
/*  10:    */ import javax.swing.table.JTableHeader;
/*  11:    */ import javax.swing.table.TableColumn;
/*  12:    */ import javax.swing.table.TableColumnModel;
/*  13:    */ import javax.swing.table.TableRowSorter;
/*  14:    */ import org.jdesktop.application.ResourceMap;
/*  15:    */ 
/*  16:    */ public class MemberTable
/*  17:    */   extends JTable
/*  18:    */ {
/*  19:    */   private static String ok;
/*  20:    */   private static String fail;
/*  21:    */   private boolean sorting;
/*  22: 39 */   private static Color subduedCompressionColor = new Color(255, 192, 192);
/*  23: 40 */   private static Color subduedTensionColor = new Color(192, 192, 255);
/*  24:    */   private final MemberTableCellRenderer specialCellRenderer;
/*  25:    */   
/*  26:    */   public boolean isSorting()
/*  27:    */   {
/*  28: 49 */     return this.sorting;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static String getMemberStatusString(boolean isOk)
/*  32:    */   {
/*  33: 59 */     return isOk ? ok : fail;
/*  34:    */   }
/*  35:    */   
/*  36:    */   private static Color contrastingColor(Color color)
/*  37:    */   {
/*  38: 63 */     return color.getRed() + color.getGreen() + color.getBlue() < 384 ? Color.WHITE : Color.BLACK;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void initialize()
/*  42:    */   {
/*  43: 71 */     new MemberTableHeaderRenderer().installIn(this);
/*  44:    */     
/*  45:    */ 
/*  46: 74 */     MemberTableModel model = (MemberTableModel)getModel();
/*  47: 75 */     TableRowSorter<MemberTableModel> rowSorter = new TableRowSorter(model)
/*  48:    */     {
/*  49:    */       public void toggleSortOrder(int column)
/*  50:    */       {
/*  51: 79 */         MemberTable.this.sorting = true;
/*  52: 80 */         super.toggleSortOrder(column);
/*  53: 81 */         MemberTable.this.sorting = false;
/*  54:    */       }
/*  55: 83 */     };
/*  56: 84 */     setRowSorter(rowSorter);
/*  57:    */     
/*  58:    */ 
/*  59: 87 */     getTableHeader().setReorderingAllowed(false);
/*  60:    */     
/*  61:    */ 
/*  62: 90 */     setRowSelectionAllowed(true);
/*  63: 91 */     setColumnSelectionAllowed(false);
/*  64: 92 */     setSelectionMode(2);
/*  65:    */     
/*  66:    */ 
/*  67: 95 */     ResourceMap resourceMap = WPBDApp.getResourceMap(MemberTable.class);
/*  68: 96 */     ok = resourceMap.getString("ok.text", new Object[0]);
/*  69: 97 */     fail = resourceMap.getString("fail.text", new Object[0]);
/*  70: 98 */     for (int i = 0; i < MemberTableModel.columnNames.length; i++)
/*  71:    */     {
/*  72: 99 */       TableColumn column = getColumnModel().getColumn(i);
/*  73:100 */       column.setResizable(true);
/*  74:101 */       String headerText = resourceMap.getString(MemberTableModel.columnNames[i] + "ColumnHeader.text", new Object[0]);
/*  75:102 */       column.setHeaderValue(headerText);
/*  76:103 */       int width = resourceMap.getInteger(MemberTableModel.columnNames[i] + "Column.width").intValue();
/*  77:104 */       column.setPreferredWidth(width);
/*  78:105 */       if (i < MemberTableModel.columnNames.length - 2)
/*  79:    */       {
/*  80:106 */         column.setMinWidth(width);
/*  81:107 */         if (i == 5) {
/*  82:108 */           column.setMaxWidth(width);
/*  83:    */         }
/*  84:    */       }
/*  85:112 */       column.setCellRenderer(this.specialCellRenderer);
/*  86:    */     }
/*  87:116 */     setSelectionForeground(Color.BLACK);
/*  88:    */     
/*  89:    */ 
/*  90:119 */     getTableHeader().resizeAndRepaint();
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void loadSelection()
/*  94:    */   {
/*  95:126 */     MemberTableModel model = (MemberTableModel)getModel();
/*  96:127 */     model.loadSelection(this);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void fireTableDataChanged()
/* 100:    */   {
/* 101:134 */     MemberTableModel model = (MemberTableModel)getModel();
/* 102:135 */     model.fireTableDataChanged();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setLabel(boolean val)
/* 106:    */   {
/* 107:144 */     this.specialCellRenderer.setLabel(val);
/* 108:145 */     repaint();
/* 109:    */   }
/* 110:    */   
/* 111:    */   public MemberTable()
/* 112:    */   {
/* 113: 38 */     this.sorting = false;
/* 114:    */     
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:    */ 
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:    */ 
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:    */ 
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:    */ 
/* 160:    */ 
/* 161:    */ 
/* 162:    */ 
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:    */ 
/* 167:    */ 
/* 168:    */ 
/* 169:    */ 
/* 170:    */ 
/* 171:    */ 
/* 172:    */ 
/* 173:    */ 
/* 174:    */ 
/* 175:    */ 
/* 176:    */ 
/* 177:    */ 
/* 178:    */ 
/* 179:    */ 
/* 180:    */ 
/* 181:    */ 
/* 182:    */ 
/* 183:    */ 
/* 184:    */ 
/* 185:    */ 
/* 186:    */ 
/* 187:    */ 
/* 188:    */ 
/* 189:    */ 
/* 190:    */ 
/* 191:    */ 
/* 192:    */ 
/* 193:    */ 
/* 194:    */ 
/* 195:    */ 
/* 196:    */ 
/* 197:    */ 
/* 198:    */ 
/* 199:    */ 
/* 200:    */ 
/* 201:    */ 
/* 202:    */ 
/* 203:    */ 
/* 204:    */ 
/* 205:    */ 
/* 206:    */ 
/* 207:    */ 
/* 208:    */ 
/* 209:    */ 
/* 210:    */ 
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:    */ 
/* 219:    */ 
/* 220:    */ 
/* 221:    */ 
/* 222:    */ 
/* 223:148 */     this.specialCellRenderer = new MemberTableCellRenderer(null);
/* 224:    */   }
/* 225:    */   
/* 226:    */   private class MemberTableCellRenderer
/* 227:    */     extends DefaultTableCellRenderer
/* 228:    */   {
/* 229:152 */     private boolean label = false;
/* 230:    */     
/* 231:    */     private MemberTableCellRenderer() {}
/* 232:    */     
/* 233:    */     public void setLabel(boolean label)
/* 234:    */     {
/* 235:155 */       this.label = label;
/* 236:    */     }
/* 237:    */     
/* 238:158 */     private final NumberFormat doubleFormatter = new DecimalFormat("0.00");
/* 239:    */     
/* 240:    */     public void setValue(Object value)
/* 241:    */     {
/* 242:162 */       if ((value instanceof Double)) {
/* 243:163 */         setText(value == null ? "" : this.doubleFormatter.format(value));
/* 244:    */       } else {
/* 245:166 */         super.setValue(value);
/* 246:    */       }
/* 247:    */     }
/* 248:    */     
/* 249:    */     private Color selectionBackground(JTable table, int row)
/* 250:    */     {
/* 251:171 */       return Member.selectedColors[getMember(table, row).getMaterial().getIndex()];
/* 252:    */     }
/* 253:    */     
/* 254:    */     private Member getMember(JTable table, int row)
/* 255:    */     {
/* 256:176 */       return ((MemberTableModel)MemberTable.this.getModel()).getMember(table.convertRowIndexToModel(row));
/* 257:    */     }
/* 258:    */     
/* 259:    */     private void setValueForNullableDouble(Object value)
/* 260:    */     {
/* 261:180 */       double x = ((Double)value).doubleValue();
/* 262:181 */       setText(x < 0.0D ? "--" : String.format(Locale.US, "%.2f", new Object[] { Double.valueOf(x) }));
/* 263:    */     }
/* 264:    */     
/* 265:    */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/* 266:    */     {
/* 267:187 */       super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/* 268:188 */       switch (column)
/* 269:    */       {
/* 270:    */       case 0: 
/* 271:190 */         setHorizontalAlignment(4);
/* 272:191 */         setBackground((this.label) || (isSelected) ? Member.labelBackground : table.getBackground());
/* 273:192 */         setForeground(table.getForeground());
/* 274:193 */         break;
/* 275:    */       case 1: 
/* 276:    */       case 2: 
/* 277:196 */         setHorizontalAlignment(0);
/* 278:197 */         setBackground(isSelected ? selectionBackground(table, row) : table.getBackground());
/* 279:198 */         setForeground(MemberTable.contrastingColor(getBackground()));
/* 280:199 */         break;
/* 281:    */       case 3: 
/* 282:    */       case 4: 
/* 283:202 */         setHorizontalAlignment(0);
/* 284:203 */         setBackground(isSelected ? selectionBackground(table, row) : table.getBackground());
/* 285:204 */         setForeground(MemberTable.contrastingColor(getBackground()));
/* 286:205 */         break;
/* 287:    */       case 6: 
/* 288:208 */         setHorizontalAlignment(4);
/* 289:209 */         setBackground(getMember(table, row).getSlenderness() > ((MemberTableModel)MemberTable.this.getModel()).getAllowableSlenderness() ? Color.MAGENTA : table.getBackground());
/* 290:210 */         setForeground(table.getForeground());
/* 291:211 */         break;
/* 292:    */       case 7: 
/* 293:213 */         setHorizontalAlignment(0);
/* 294:214 */         if (((MemberTableModel)MemberTable.this.getModel()).isAnalysisValid())
/* 295:    */         {
/* 296:215 */           setBackground(getMember(table, row).getCompressionForceStrengthRatio() <= 1.0D ? table.getBackground() : Color.RED);
/* 297:216 */           setForeground(table.getForeground());
/* 298:    */         }
/* 299:    */         else
/* 300:    */         {
/* 301:219 */           setBackground(getMember(table, row).getCompressionForceStrengthRatio() <= 1.0D ? table.getBackground() : MemberTable.subduedCompressionColor);
/* 302:220 */           setForeground(Color.GRAY);
/* 303:    */         }
/* 304:222 */         setValueForNullableDouble(value);
/* 305:223 */         break;
/* 306:    */       case 8: 
/* 307:225 */         setHorizontalAlignment(0);
/* 308:226 */         if (((MemberTableModel)MemberTable.this.getModel()).isAnalysisValid())
/* 309:    */         {
/* 310:227 */           setBackground(getMember(table, row).getTensionForceStrengthRatio() <= 1.0D ? table.getBackground() : Color.BLUE);
/* 311:228 */           setForeground(table.getForeground());
/* 312:    */         }
/* 313:    */         else
/* 314:    */         {
/* 315:231 */           setBackground(getMember(table, row).getTensionForceStrengthRatio() <= 1.0D ? table.getBackground() : MemberTable.subduedTensionColor);
/* 316:232 */           setForeground(Color.GRAY);
/* 317:    */         }
/* 318:234 */         setValueForNullableDouble(value);
/* 319:235 */         break;
/* 320:    */       case 5: 
/* 321:    */       default: 
/* 322:237 */         setHorizontalAlignment(0);
/* 323:238 */         setBackground(table.getBackground());
/* 324:239 */         setForeground(table.getForeground());
/* 325:    */       }
/* 326:242 */       return this;
/* 327:    */     }
/* 328:    */   }
/* 329:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.MemberTable
 * JD-Core Version:    0.7.0.1
 */