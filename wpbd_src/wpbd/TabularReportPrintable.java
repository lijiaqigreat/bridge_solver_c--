/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.ComponentOrientation;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.FontMetrics;
/*   7:    */ import java.awt.Graphics;
/*   8:    */ import java.awt.Graphics2D;
/*   9:    */ import java.awt.Rectangle;
/*  10:    */ import java.awt.Shape;
/*  11:    */ import java.awt.geom.AffineTransform;
/*  12:    */ import java.awt.geom.Rectangle2D;
/*  13:    */ import java.awt.print.PageFormat;
/*  14:    */ import java.awt.print.Printable;
/*  15:    */ import java.awt.print.PrinterException;
/*  16:    */ import java.text.MessageFormat;
/*  17:    */ import javax.swing.JTable;
/*  18:    */ import javax.swing.JTable.PrintMode;
/*  19:    */ import javax.swing.table.JTableHeader;
/*  20:    */ import javax.swing.table.TableColumn;
/*  21:    */ import javax.swing.table.TableColumnModel;
/*  22:    */ 
/*  23:    */ public class TabularReportPrintable
/*  24:    */   implements Printable
/*  25:    */ {
/*  26:    */   private JTable table;
/*  27:    */   private JTableHeader header;
/*  28:    */   private TableColumnModel colModel;
/*  29:    */   private int totalColWidth;
/*  30:    */   private JTable.PrintMode printMode;
/*  31:    */   private MessageFormat headerFormat;
/*  32:    */   private String[] subheaderText;
/*  33:    */   private MessageFormat footerFormat;
/*  34: 68 */   private int last = -1;
/*  35: 71 */   private int row = 0;
/*  36: 74 */   private int col = 0;
/*  37: 77 */   private final Rectangle clip = new Rectangle(0, 0, 0, 0);
/*  38: 80 */   private final Rectangle hclip = new Rectangle(0, 0, 0, 0);
/*  39: 83 */   private final Rectangle tempRect = new Rectangle(0, 0, 0, 0);
/*  40:    */   private static final int H_F_SPACE = 8;
/*  41:    */   private static final float HEADER_FONT_SIZE = 10.0F;
/*  42:    */   private static final float SUBHEADER_FONT_SIZE = 8.0F;
/*  43:    */   private static final float FOOTER_FONT_SIZE = 8.0F;
/*  44:    */   private Font headerFont;
/*  45:    */   private Font subheaderFont;
/*  46:    */   private Font footerFont;
/*  47:    */   
/*  48:    */   public TabularReportPrintable(JTable table, JTable.PrintMode printMode, MessageFormat headerFormat, String[] subheaderText, MessageFormat footerFormat)
/*  49:    */   {
/*  50:126 */     this.table = table;
/*  51:    */     
/*  52:128 */     this.header = table.getTableHeader();
/*  53:129 */     this.colModel = table.getColumnModel();
/*  54:130 */     this.totalColWidth = this.colModel.getTotalColumnWidth();
/*  55:132 */     if (this.header != null) {
/*  56:134 */       this.hclip.height = this.header.getHeight();
/*  57:    */     }
/*  58:137 */     this.printMode = printMode;
/*  59:    */     
/*  60:139 */     this.headerFormat = headerFormat;
/*  61:140 */     this.subheaderText = subheaderText;
/*  62:141 */     this.footerFormat = footerFormat;
/*  63:    */     
/*  64:    */ 
/*  65:144 */     this.headerFont = table.getFont().deriveFont(1, 10.0F);
/*  66:145 */     this.subheaderFont = table.getFont().deriveFont(0, 8.0F);
/*  67:146 */     this.footerFont = table.getFont().deriveFont(0, 8.0F);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
/*  71:    */     throws PrinterException
/*  72:    */   {
/*  73:164 */     int imgWidth = (int)pageFormat.getImageableWidth();
/*  74:165 */     int imgHeight = (int)pageFormat.getImageableHeight();
/*  75:167 */     if (imgWidth <= 0) {
/*  76:168 */       throw new PrinterException("Width of printable area is too small.");
/*  77:    */     }
/*  78:172 */     Object[] pageNumber = { new Integer(pageIndex + 1) };
/*  79:    */     
/*  80:    */ 
/*  81:175 */     String headerText = null;
/*  82:176 */     if (this.headerFormat != null) {
/*  83:177 */       headerText = this.headerFormat.format(pageNumber);
/*  84:    */     }
/*  85:180 */     FontMetrics subheaderFontMetrics = null;
/*  86:181 */     if (this.subheaderText != null) {
/*  87:182 */       subheaderFontMetrics = graphics.getFontMetrics(this.subheaderFont);
/*  88:    */     }
/*  89:186 */     String footerText = null;
/*  90:187 */     if (this.footerFormat != null) {
/*  91:188 */       footerText = this.footerFormat.format(pageNumber);
/*  92:    */     }
/*  93:192 */     Rectangle2D headerRect = null;
/*  94:193 */     int subheaderTextHeight = 0;
/*  95:194 */     Rectangle2D footerRect = null;
/*  96:    */     
/*  97:    */ 
/*  98:197 */     int availableSpace = imgHeight;
/*  99:201 */     if (headerText != null)
/* 100:    */     {
/* 101:202 */       headerRect = graphics.getFontMetrics(this.headerFont).getStringBounds(headerText, graphics);
/* 102:203 */       availableSpace -= round(headerRect.getHeight()) + 8;
/* 103:    */     }
/* 104:208 */     if (this.subheaderText != null)
/* 105:    */     {
/* 106:209 */       subheaderTextHeight = subheaderFontMetrics.getHeight() * this.subheaderText.length;
/* 107:210 */       availableSpace -= subheaderTextHeight + 8;
/* 108:    */     }
/* 109:215 */     if (footerText != null)
/* 110:    */     {
/* 111:216 */       footerRect = graphics.getFontMetrics(this.footerFont).getStringBounds(footerText, graphics);
/* 112:217 */       availableSpace -= round(footerRect.getHeight()) + 8;
/* 113:    */     }
/* 114:220 */     if (availableSpace <= 0) {
/* 115:221 */       throw new PrinterException("Height of printable area is too small.");
/* 116:    */     }
/* 117:226 */     double sf = 1.0D;
/* 118:227 */     if ((this.printMode == JTable.PrintMode.FIT_WIDTH) && (this.totalColWidth > imgWidth))
/* 119:    */     {
/* 120:231 */       assert (imgWidth > 0);
/* 121:    */       
/* 122:    */ 
/* 123:234 */       assert (this.totalColWidth > 1);
/* 124:    */       
/* 125:236 */       sf = imgWidth / this.totalColWidth;
/* 126:    */     }
/* 127:240 */     assert (sf > 0.0D);
/* 128:246 */     if (pageIndex < this.last)
/* 129:    */     {
/* 130:247 */       this.row = (this.col = 0);
/* 131:248 */       this.clip.x = (this.clip.y = this.clip.width = this.clip.height = 0);
/* 132:249 */       this.last = -1;
/* 133:    */     }
/* 134:259 */     while (this.last < pageIndex)
/* 135:    */     {
/* 136:261 */       if ((this.row >= this.table.getRowCount()) && (this.col == 0)) {
/* 137:262 */         return 1;
/* 138:    */       }
/* 139:268 */       int scaledWidth = (int)(imgWidth / sf);
/* 140:269 */       int scaledHeight = (int)((availableSpace - this.hclip.height) / sf);
/* 141:    */       
/* 142:    */ 
/* 143:272 */       findNextClip(scaledWidth, scaledHeight);
/* 144:    */       
/* 145:274 */       this.last += 1;
/* 146:    */     }
/* 147:278 */     Graphics2D g2d = (Graphics2D)graphics;
/* 148:279 */     g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
/* 149:282 */     if (footerText != null)
/* 150:    */     {
/* 151:283 */       AffineTransform oldTrans = g2d.getTransform();
/* 152:284 */       g2d.translate(0, imgHeight - ceil(footerRect.getHeight()));
/* 153:285 */       printText(g2d, footerText, round(footerRect.getWidth()), ceil(-footerRect.getY()), this.footerFont, imgWidth);
/* 154:286 */       g2d.setTransform(oldTrans);
/* 155:    */     }
/* 156:291 */     if (headerText != null)
/* 157:    */     {
/* 158:292 */       printText(g2d, headerText, round(headerRect.getWidth()), ceil(-headerRect.getY()), this.headerFont, imgWidth);
/* 159:293 */       g2d.translate(0, round(headerRect.getHeight()) + 8);
/* 160:    */     }
/* 161:298 */     int yBaseline = 0;
/* 162:299 */     if (this.subheaderText != null)
/* 163:    */     {
/* 164:300 */       for (int i = 0; i < this.subheaderText.length; i++)
/* 165:    */       {
/* 166:301 */         yBaseline += subheaderFontMetrics.getAscent();
/* 167:302 */         Rectangle2D r = subheaderFontMetrics.getStringBounds(this.subheaderText[i], graphics);
/* 168:303 */         printText(g2d, this.subheaderText[i], round(r.getWidth()), yBaseline, this.subheaderFont, imgWidth);
/* 169:304 */         yBaseline += subheaderFontMetrics.getDescent() + subheaderFontMetrics.getLeading();
/* 170:    */       }
/* 171:306 */       g2d.translate(0, subheaderTextHeight + 8);
/* 172:    */     }
/* 173:310 */     this.tempRect.x = 0;
/* 174:311 */     this.tempRect.y = 0;
/* 175:312 */     this.tempRect.width = imgWidth;
/* 176:313 */     this.tempRect.height = availableSpace;
/* 177:314 */     g2d.clip(this.tempRect);
/* 178:318 */     if (sf != 1.0D)
/* 179:    */     {
/* 180:319 */       g2d.scale(sf, sf);
/* 181:    */     }
/* 182:    */     else
/* 183:    */     {
/* 184:324 */       int diff = (imgWidth - this.clip.width) / 2;
/* 185:325 */       g2d.translate(diff, 0);
/* 186:    */     }
/* 187:329 */     AffineTransform oldTrans = g2d.getTransform();
/* 188:330 */     Shape oldClip = g2d.getClip();
/* 189:334 */     if (this.header != null)
/* 190:    */     {
/* 191:335 */       this.hclip.x = this.clip.x;
/* 192:336 */       this.hclip.width = this.clip.width;
/* 193:    */       
/* 194:338 */       g2d.translate(-this.hclip.x, 0);
/* 195:339 */       g2d.clip(this.hclip);
/* 196:340 */       this.header.print(g2d);
/* 197:    */       
/* 198:    */ 
/* 199:343 */       g2d.setTransform(oldTrans);
/* 200:344 */       g2d.setClip(oldClip);
/* 201:    */       
/* 202:    */ 
/* 203:347 */       g2d.translate(0, this.hclip.height);
/* 204:    */     }
/* 205:351 */     g2d.translate(-this.clip.x, -this.clip.y);
/* 206:352 */     g2d.clip(this.clip);
/* 207:353 */     this.table.print(g2d);
/* 208:    */     
/* 209:    */ 
/* 210:356 */     g2d.setTransform(oldTrans);
/* 211:357 */     g2d.setClip(oldClip);
/* 212:    */     
/* 213:    */ 
/* 214:360 */     g2d.setColor(Color.BLACK);
/* 215:361 */     g2d.drawRect(0, 0, this.clip.width, this.hclip.height + this.clip.height);
/* 216:    */     
/* 217:363 */     return 0;
/* 218:    */   }
/* 219:    */   
/* 220:    */   private void printText(Graphics2D g2d, String text, int width, int yBaseline, Font font, int imgWidth)
/* 221:    */   {
/* 222:    */     int tx;
/* 223:    */     int tx;
/* 224:387 */     if (width < imgWidth)
/* 225:    */     {
/* 226:388 */       tx = (imgWidth - width) / 2;
/* 227:    */     }
/* 228:    */     else
/* 229:    */     {
/* 230:    */       int tx;
/* 231:391 */       if (this.table.getComponentOrientation().isLeftToRight()) {
/* 232:392 */         tx = 0;
/* 233:    */       } else {
/* 234:395 */         tx = imgWidth - width;
/* 235:    */       }
/* 236:    */     }
/* 237:398 */     g2d.setColor(Color.BLACK);
/* 238:399 */     g2d.setFont(font);
/* 239:400 */     g2d.drawString(text, tx, yBaseline);
/* 240:    */   }
/* 241:    */   
/* 242:    */   private void findNextClip(int pw, int ph)
/* 243:    */   {
/* 244:415 */     boolean ltr = this.table.getComponentOrientation().isLeftToRight();
/* 245:418 */     if (this.col == 0)
/* 246:    */     {
/* 247:419 */       if (ltr) {
/* 248:421 */         this.clip.x = 0;
/* 249:    */       } else {
/* 250:424 */         this.clip.x = this.totalColWidth;
/* 251:    */       }
/* 252:427 */       this.clip.y += this.clip.height;
/* 253:    */       
/* 254:    */ 
/* 255:430 */       this.clip.width = 0;
/* 256:431 */       this.clip.height = 0;
/* 257:    */       
/* 258:    */ 
/* 259:434 */       int rowCount = this.table.getRowCount();
/* 260:435 */       int rowHeight = this.table.getRowHeight(this.row);
/* 261:    */       do
/* 262:    */       {
/* 263:437 */         this.clip.height += rowHeight;
/* 264:438 */         if (++this.row >= rowCount) {
/* 265:    */           break;
/* 266:    */         }
/* 267:441 */         rowHeight = this.table.getRowHeight(this.row);
/* 268:442 */       } while (this.clip.height + rowHeight <= ph);
/* 269:    */     }
/* 270:447 */     if (this.printMode == JTable.PrintMode.FIT_WIDTH)
/* 271:    */     {
/* 272:448 */       this.clip.x = 0;
/* 273:449 */       this.clip.width = this.totalColWidth;
/* 274:450 */       return;
/* 275:    */     }
/* 276:453 */     if (ltr) {
/* 277:455 */       this.clip.x += this.clip.width;
/* 278:    */     }
/* 279:459 */     this.clip.width = 0;
/* 280:    */     
/* 281:    */ 
/* 282:462 */     int colCount = this.table.getColumnCount();
/* 283:463 */     int colWidth = this.colModel.getColumn(this.col).getWidth();
/* 284:    */     do
/* 285:    */     {
/* 286:465 */       this.clip.width += colWidth;
/* 287:466 */       if (!ltr) {
/* 288:467 */         this.clip.x -= colWidth;
/* 289:    */       }
/* 290:470 */       if (++this.col >= colCount)
/* 291:    */       {
/* 292:472 */         this.col = 0;
/* 293:473 */         break;
/* 294:    */       }
/* 295:476 */       colWidth = this.colModel.getColumn(this.col).getWidth();
/* 296:477 */     } while (this.clip.width + colWidth <= pw);
/* 297:    */   }
/* 298:    */   
/* 299:    */   private static int round(double x)
/* 300:    */   {
/* 301:482 */     return (int)(x + 0.5D);
/* 302:    */   }
/* 303:    */   
/* 304:    */   private static int ceil(double x)
/* 305:    */   {
/* 306:486 */     return (int)Math.ceil(x);
/* 307:    */   }
/* 308:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.TabularReportPrintable
 * JD-Core Version:    0.7.0.1
 */