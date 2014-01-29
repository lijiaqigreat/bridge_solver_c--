/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Dimension;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.FontMetrics;
/*   7:    */ import java.awt.Graphics;
/*   8:    */ import java.awt.Graphics2D;
/*   9:    */ import java.awt.Rectangle;
/*  10:    */ import java.awt.Stroke;
/*  11:    */ import java.awt.geom.AffineTransform;
/*  12:    */ import java.awt.geom.Rectangle2D.Double;
/*  13:    */ import java.awt.print.PageFormat;
/*  14:    */ import java.awt.print.Printable;
/*  15:    */ import java.awt.print.PrinterException;
/*  16:    */ import java.text.NumberFormat;
/*  17:    */ import java.text.SimpleDateFormat;
/*  18:    */ import java.util.ArrayList;
/*  19:    */ import java.util.Calendar;
/*  20:    */ import java.util.Iterator;
/*  21:    */ import java.util.Locale;
/*  22:    */ import javax.swing.UIManager;
/*  23:    */ import org.jdesktop.application.ResourceMap;
/*  24:    */ 
/*  25:    */ public class BridgeBlueprintPrintable
/*  26:    */   implements Printable
/*  27:    */ {
/*  28:    */   private final String fileName;
/*  29:    */   private final BridgeModel bridge;
/*  30:    */   private final ViewportTransform viewportTransform;
/*  31:    */   private final BridgeBlueprintView bridgeBlueprintView;
/*  32: 50 */   private final Font standardFont = UIManager.getFont("Label.font").deriveFont(6.5F);
/*  33: 51 */   private final Stroke standardStroke = new BasicStroke(0.25F, 0, 1);
/*  34: 52 */   private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
/*  35: 57 */   private final Table memberTable = new Table(null);
/*  36: 58 */   private final Table titleBlock = new Table(null);
/*  37:    */   private Rectangle blueprintExtent;
/*  38:    */   private static final double PTS_PER_INCH = 72.0D;
/*  39:    */   private static final double TWIPS_PER_PT = 20.0D;
/*  40:    */   private static final double TWIPS_PER_INCH = 1440.0D;
/*  41:    */   private static final double leftViewportMargin = 936.0D;
/*  42:    */   private static final double rightViewportMargin = 936.0D;
/*  43:    */   private static final double topViewportMargin = 936.0D;
/*  44:    */   private static final double bottomViewportMargin = 936.0D;
/*  45:    */   
/*  46:    */   public BridgeBlueprintPrintable(String fileName, BridgeModel bridge)
/*  47:    */   {
/*  48: 87 */     this.fileName = fileName;
/*  49: 88 */     this.bridge = bridge;
/*  50: 89 */     this.viewportTransform = new ViewportTransform();
/*  51: 90 */     this.viewportTransform.setVAlign(-1);
/*  52: 91 */     this.bridgeBlueprintView = new BridgeBlueprintView(bridge);
/*  53: 92 */     this.memberTable.setContents(getMemberTableCells(), 3);
/*  54: 93 */     this.titleBlock.setContents(getTitleBlockCells(), 0);
/*  55:    */   }
/*  56:    */   
/*  57:    */   private Cell[][] getMemberTableCells()
/*  58:    */   {
/*  59:102 */     ResourceMap resourceMap = WPBDApp.getResourceMap(BridgeBlueprintPrintable.class);
/*  60:103 */     Cell[][] cells = new Cell[3 + this.bridge.getMembers().size()][5];
/*  61:104 */     cells[0][0] = new Cell(resourceMap.getString("memberTableTitle.text", new Object[0])).setColSpan(5);
/*  62:105 */     cells[1][0] = new Cell(resourceMap.getString("memberTableNumber.text", new Object[0])).setRowSpan(2);
/*  63:106 */     cells[1][1] = new Cell(resourceMap.getString("memberTableMaterial.text", new Object[0])).setRowSpan(2);
/*  64:    */     
/*  65:108 */     cells[1][2] = new Cell(resourceMap.getString("memberTableCross.text", new Object[0])).setBorder(1, 1, 0, 1);
/*  66:109 */     cells[2][2] = new Cell(resourceMap.getString("memberTableSection.text", new Object[0])).setBorder(0, 1, 1, 1);
/*  67:    */     
/*  68:111 */     cells[1][3] = new Cell(resourceMap.getString("memberTableSize.text", new Object[0])).setBorder(1, 1, 0, 1);
/*  69:112 */     cells[2][3] = new Cell(resourceMap.getString("memberTableSizeUnits.text", new Object[0])).setBorder(0, 1, 1, 1);
/*  70:    */     
/*  71:114 */     cells[1][4] = new Cell(resourceMap.getString("memberTableLength.text", new Object[0])).setBorder(1, 1, 0, 1);
/*  72:115 */     cells[2][4] = new Cell(resourceMap.getString("memberTableLengthUnits.text", new Object[0])).setBorder(0, 1, 1, 1);
/*  73:    */     
/*  74:117 */     int i = 2;
/*  75:118 */     Iterator<Member> e = this.bridge.getMembers().iterator();
/*  76:119 */     while (e.hasNext())
/*  77:    */     {
/*  78:120 */       Member m = (Member)e.next();
/*  79:121 */       i++;
/*  80:122 */       cells[i][0] = new Cell(Integer.toString(m.getNumber())).setHAlign(1);
/*  81:123 */       cells[i][1] = new Cell(m.getMaterial().getShortName());
/*  82:124 */       cells[i][2] = new Cell(m.getShape().getSection().getShortName());
/*  83:125 */       cells[i][3] = new Cell(m.getShape().getName());
/*  84:126 */       cells[i][4] = new Cell(String.format(Locale.US, "%.2f", new Object[] { Double.valueOf(m.getLength()) })).setHAlign(1);
/*  85:    */     }
/*  86:128 */     return cells;
/*  87:    */   }
/*  88:    */   
/*  89:    */   private Cell[][] getTitleBlockCells()
/*  90:    */   {
/*  91:137 */     ResourceMap resourceMap = WPBDApp.getResourceMap(BridgeBlueprintPrintable.class);
/*  92:138 */     Cell[][] cells = new Cell[6][3];
/*  93:139 */     cells[0][0] = new Cell(this.bridge.getProjectName()).setColSpan(3);
/*  94:140 */     cells[1][0] = new Cell(resourceMap.getString("titleBlockElevation.text", new Object[0])).setColSpan(2);
/*  95:141 */     cells[1][2] = new Cell("<pageNo>").setHAlign(-1);
/*  96:142 */     cells[2][0] = new Cell(resourceMap.getString("titleBlockCost.text", new Object[] { this.currencyFormat.format(this.bridge.getTotalCost()) })).setHAlign(-1);
/*  97:143 */     String fmt = resourceMap.getString("titleBlockDate.text", new Object[0]);
/*  98:144 */     SimpleDateFormat dateFormat = new SimpleDateFormat(fmt == null ? "d MMM yyyy" : fmt);
/*  99:145 */     cells[2][1] = new Cell(dateFormat.format(Calendar.getInstance().getTime())).setHAlign(-1);
/* 100:146 */     cells[2][2] = new Cell(resourceMap.getString("titleBlockIteration.text", new Object[] { Integer.valueOf(this.bridge.getIteration()) })).setHAlign(-1);
/* 101:147 */     cells[3][0] = new Cell(resourceMap.getString("titleBlockDesignedBy.text", new Object[0])).setHAlign(-1).setBorder(1, 0, 1, 1);
/* 102:148 */     cells[3][1] = new Cell(this.bridge.getDesignedBy()).setHAlign(-1).setColSpan(2).setBorder(1, 1, 1, 0);
/* 103:149 */     cells[4][0] = new Cell(resourceMap.getString("titleBlockProjectId.text", new Object[0])).setHAlign(-1).setBorder(1, 0, 1, 1);
/* 104:150 */     cells[4][1] = new Cell(this.bridge.getProjectId()).setHAlign(-1).setColSpan(2).setBorder(1, 1, 1, 0);
/* 105:151 */     cells[5][0] = new Cell(resourceMap.getString("titleBlockPath.text", new Object[] { this.fileName })).setHAlign(-1).setColSpan(3);
/* 106:152 */     return cells;
/* 107:    */   }
/* 108:    */   
/* 109:    */   private int printStandardSheet(Graphics2D g, PageFormat pageFormat, int pageNo)
/* 110:    */   {
/* 111:163 */     int pageWidth = (int)pageFormat.getImageableWidth();
/* 112:164 */     int pageHeight = (int)pageFormat.getImageableHeight();
/* 113:165 */     g.drawRect(0, 0, pageWidth - 1, pageHeight - 1);
/* 114:166 */     this.titleBlock.getCell(1, 2).setText(WPBDApp.getResourceMap(BridgeBlueprintPrintable.class).getString("titleBlockPage.text", new Object[] { Integer.valueOf(pageNo) }));
/* 115:167 */     this.titleBlock.setLocation(pageWidth - 1, pageHeight - 1, 1, 1);
/* 116:168 */     this.titleBlock.paint(g);
/* 117:169 */     return pageHeight - this.titleBlock.height;
/* 118:    */   }
/* 119:    */   
/* 120:    */   private void setUpBlueprintViewportTransform(int pageWidth, int pageHeight)
/* 121:    */   {
/* 122:179 */     this.bridgeBlueprintView.initialize(this.bridge.getDesignConditions());
/* 123:180 */     Rectangle2D.Double window = this.bridgeBlueprintView.getPreferredDrawingWindow();
/* 124:181 */     this.viewportTransform.setWindow(window);
/* 125:182 */     double viewportWidth = pageWidth * 20.0D - 936.0D - 936.0D;
/* 126:183 */     double viewportHeight = pageHeight * 20.0D - 936.0D - 936.0D;
/* 127:184 */     this.viewportTransform.setViewport(936.0D, viewportHeight + 936.0D, viewportWidth, -viewportHeight);
/* 128:    */   }
/* 129:    */   
/* 130:    */   private Rectangle getBlueprintExtent(int pageWidth, int pageHeight)
/* 131:    */   {
/* 132:195 */     setUpBlueprintViewportTransform(pageWidth, pageHeight);
/* 133:196 */     return this.bridgeBlueprintView.getPaintedExtent(null, this.viewportTransform);
/* 134:    */   }
/* 135:    */   
/* 136:    */   private void printBlueprint(Graphics2D g, int pageWidth, int pageHeight)
/* 137:    */   {
/* 138:207 */     setUpBlueprintViewportTransform(pageWidth, pageHeight);
/* 139:208 */     AffineTransform savedXform = g.getTransform();
/* 140:209 */     g.scale(0.05D, 0.05D);
/* 141:210 */     this.bridgeBlueprintView.paint(g, this.viewportTransform);
/* 142:211 */     g.setTransform(savedXform);
/* 143:    */   }
/* 144:    */   
/* 145:    */   private class Cell
/* 146:    */     extends Rectangle
/* 147:    */   {
/* 148:    */     public static final int HALIGN_LEFT = -1;
/* 149:    */     public static final int HALIGN_CENTER = 0;
/* 150:    */     public static final int HALIGN_RIGHT = 1;
/* 151:    */     public static final int VALIGN_TOP = -1;
/* 152:    */     public static final int VALIGN_CENTER = 0;
/* 153:    */     public static final int VALIGN_BOTTOM = 1;
/* 154:223 */     public String text = null;
/* 155:224 */     public byte hAlign = 0;
/* 156:225 */     public byte vAlign = 0;
/* 157:226 */     public byte rowSpan = 1;
/* 158:227 */     public byte colSpan = 1;
/* 159:228 */     public byte leftBorder = 1;
/* 160:229 */     public byte rightBorder = 1;
/* 161:230 */     public byte topBorder = 1;
/* 162:231 */     public byte bottomBorder = 1;
/* 163:232 */     public byte leftPad = 2;
/* 164:233 */     public byte rightPad = 2;
/* 165:234 */     public byte topPad = 0;
/* 166:235 */     public byte bottomPad = 0;
/* 167:236 */     public Font font = null;
/* 168:    */     
/* 169:    */     public Cell(String text)
/* 170:    */     {
/* 171:239 */       this.text = text;
/* 172:    */     }
/* 173:    */     
/* 174:    */     public Cell setBorder(int top, int right, int bottom, int left)
/* 175:    */     {
/* 176:243 */       this.topBorder = ((byte)top);
/* 177:244 */       this.rightBorder = ((byte)right);
/* 178:245 */       this.bottomBorder = ((byte)bottom);
/* 179:246 */       this.leftBorder = ((byte)left);
/* 180:247 */       return this;
/* 181:    */     }
/* 182:    */     
/* 183:    */     public Cell setHAlign(int hAlign)
/* 184:    */     {
/* 185:251 */       this.hAlign = ((byte)hAlign);
/* 186:252 */       return this;
/* 187:    */     }
/* 188:    */     
/* 189:    */     public Cell setVAlign(int vAlign)
/* 190:    */     {
/* 191:256 */       this.vAlign = ((byte)vAlign);
/* 192:257 */       return this;
/* 193:    */     }
/* 194:    */     
/* 195:    */     public Cell setRowSpan(int rowSpan)
/* 196:    */     {
/* 197:261 */       this.rowSpan = ((byte)rowSpan);
/* 198:262 */       return this;
/* 199:    */     }
/* 200:    */     
/* 201:    */     public Cell setColSpan(int colSpan)
/* 202:    */     {
/* 203:266 */       this.colSpan = ((byte)colSpan);
/* 204:267 */       return this;
/* 205:    */     }
/* 206:    */     
/* 207:    */     public Cell setFont(Font font)
/* 208:    */     {
/* 209:271 */       this.font = font;
/* 210:272 */       return this;
/* 211:    */     }
/* 212:    */     
/* 213:    */     public void setText(String text)
/* 214:    */     {
/* 215:276 */       this.text = text;
/* 216:    */     }
/* 217:    */     
/* 218:    */     public Dimension getNativeDimension(Dimension dim, Graphics2D g)
/* 219:    */     {
/* 220:280 */       if (dim == null) {
/* 221:281 */         dim = new Dimension();
/* 222:    */       }
/* 223:283 */       FontMetrics fm = g.getFontMetrics(this.font == null ? g.getFont() : this.font);
/* 224:284 */       dim.width = (this.leftBorder + this.leftPad + fm.stringWidth(this.text) + this.rightPad + this.rightBorder);
/* 225:285 */       dim.height = (this.topBorder + this.topPad + fm.getAscent() + fm.getDescent() + this.bottomPad + this.bottomBorder);
/* 226:286 */       return dim;
/* 227:    */     }
/* 228:    */     
/* 229:    */     public void paint(Graphics2D g)
/* 230:    */     {
/* 231:290 */       Font savedFont = null;
/* 232:291 */       if (this.font != null)
/* 233:    */       {
/* 234:292 */         savedFont = g.getFont();
/* 235:293 */         g.setFont(this.font);
/* 236:    */       }
/* 237:295 */       if (this.leftBorder > 0)
/* 238:    */       {
/* 239:296 */         g.setStroke(new BasicStroke(0.25F * this.leftBorder));
/* 240:297 */         g.drawLine(this.x, this.y, this.x, this.y + this.height);
/* 241:    */       }
/* 242:299 */       if (this.rightBorder > 0)
/* 243:    */       {
/* 244:300 */         g.setStroke(new BasicStroke(0.25F * this.rightBorder));
/* 245:301 */         int xRight = this.x + this.width - this.rightBorder + 1;
/* 246:302 */         g.drawLine(xRight, this.y, xRight, this.y + this.height);
/* 247:    */       }
/* 248:304 */       if (this.topBorder > 0)
/* 249:    */       {
/* 250:305 */         g.setStroke(new BasicStroke(0.25F * this.topBorder));
/* 251:306 */         g.drawLine(this.x, this.y, this.x + this.width, this.y);
/* 252:    */       }
/* 253:308 */       if (this.bottomBorder > 0)
/* 254:    */       {
/* 255:309 */         g.setStroke(new BasicStroke(0.25F * this.bottomBorder));
/* 256:310 */         int yBottom = this.y + this.height - this.bottomBorder + 1;
/* 257:311 */         g.drawLine(this.x, yBottom, this.x + this.width, yBottom);
/* 258:    */       }
/* 259:314 */       int xTextBox = this.x + this.leftBorder + this.leftPad;
/* 260:315 */       int yTextBox = this.y + this.topBorder + this.topPad;
/* 261:316 */       int widthTextBox = this.width - this.leftBorder - this.leftPad - this.rightBorder - this.rightPad + 1;
/* 262:317 */       int heightTextBox = this.height - this.topBorder - this.topPad - this.bottomBorder - this.bottomPad + 1;
/* 263:318 */       FontMetrics fm = g.getFontMetrics();
/* 264:    */       int xText;
/* 265:320 */       switch (this.hAlign)
/* 266:    */       {
/* 267:    */       default: 
/* 268:322 */         xText = xTextBox;
/* 269:323 */         break;
/* 270:    */       case 0: 
/* 271:325 */         xText = xTextBox + (widthTextBox - fm.stringWidth(this.text)) / 2;
/* 272:326 */         break;
/* 273:    */       case 1: 
/* 274:328 */         xText = xTextBox + widthTextBox - fm.stringWidth(this.text);
/* 275:    */       }
/* 276:    */       int yText;
/* 277:331 */       switch (this.vAlign)
/* 278:    */       {
/* 279:    */       default: 
/* 280:333 */         yText = yTextBox + fm.getAscent();
/* 281:334 */         break;
/* 282:    */       case 0: 
/* 283:336 */         yText = yTextBox + (heightTextBox + fm.getAscent() - fm.getDescent()) / 2;
/* 284:337 */         break;
/* 285:    */       case 1: 
/* 286:339 */         yText = yTextBox + heightTextBox - fm.getAscent() - fm.getDescent();
/* 287:    */       }
/* 288:342 */       g.drawString(this.text, xText, yText);
/* 289:343 */       if (savedFont != null) {
/* 290:344 */         g.setFont(savedFont);
/* 291:    */       }
/* 292:    */     }
/* 293:    */   }
/* 294:    */   
/* 295:    */   private class Table
/* 296:    */     extends Rectangle
/* 297:    */   {
/* 298:355 */     private byte hAlign = -1;
/* 299:356 */     private byte vAlign = -1;
/* 300:357 */     private boolean dirty = true;
/* 301:    */     private BridgeBlueprintPrintable.Cell[][] cells;
/* 302:    */     private int nHeaderRows;
/* 303:    */     private int[] colWidths;
/* 304:    */     private int[] rowHeights;
/* 305:    */     
/* 306:    */     private Table() {}
/* 307:    */     
/* 308:    */     public boolean isDirty()
/* 309:    */     {
/* 310:364 */       return this.dirty;
/* 311:    */     }
/* 312:    */     
/* 313:    */     public BridgeBlueprintPrintable.Cell getCell(int i, int j)
/* 314:    */     {
/* 315:368 */       return this.cells[i][j];
/* 316:    */     }
/* 317:    */     
/* 318:    */     private void initialize(Graphics2D g)
/* 319:    */     {
/* 320:372 */       if ((!this.dirty) || (this.cells == null)) {
/* 321:373 */         return;
/* 322:    */       }
/* 323:375 */       this.dirty = false;
/* 324:376 */       if ((this.rowHeights == null) || (this.rowHeights.length != this.cells.length)) {
/* 325:377 */         this.rowHeights = new int[this.cells.length];
/* 326:    */       }
/* 327:379 */       int nCols = this.cells[0].length;
/* 328:380 */       if ((this.colWidths == null) || (this.colWidths.length != nCols)) {
/* 329:381 */         this.colWidths = new int[nCols];
/* 330:    */       }
/* 331:383 */       for (int i = 0; i < this.colWidths.length; i++) {
/* 332:384 */         this.colWidths[i] = 0;
/* 333:    */       }
/* 334:386 */       for (int j = 0; j < this.rowHeights.length; j++) {
/* 335:387 */         this.rowHeights[j] = 0;
/* 336:    */       }
/* 337:390 */       Dimension dim = new Dimension();
/* 338:391 */       for (int i = 0; i < this.cells.length; i++) {
/* 339:392 */         for (int j = 0; j < this.colWidths.length; j++) {
/* 340:393 */           if (this.cells[i][j] != null)
/* 341:    */           {
/* 342:394 */             this.cells[i][j].getNativeDimension(dim, g);
/* 343:395 */             this.cells[i][j].setSize(dim);
/* 344:396 */             if ((dim.width > this.colWidths[j]) && (this.cells[i][j].colSpan == 1)) {
/* 345:397 */               this.colWidths[j] = dim.width;
/* 346:    */             }
/* 347:399 */             if ((dim.height > this.rowHeights[i]) && (this.cells[i][j].rowSpan == 1)) {
/* 348:400 */               this.rowHeights[i] = dim.height;
/* 349:    */             }
/* 350:    */           }
/* 351:    */         }
/* 352:    */       }
/* 353:406 */       for (int i = 0; i < this.cells.length; i++) {
/* 354:407 */         for (int j = 0; j < this.colWidths.length; j++)
/* 355:    */         {
/* 356:408 */           BridgeBlueprintPrintable.Cell cell = this.cells[i][j];
/* 357:409 */           if (cell != null)
/* 358:    */           {
/* 359:410 */             int colSpan = this.cells[i][j].colSpan;
/* 360:411 */             if ((cell.width > this.colWidths[j]) && (colSpan > 1))
/* 361:    */             {
/* 362:412 */               int totalMaxWidth = 0;
/* 363:413 */               for (int jj = j; jj < j + colSpan; jj++) {
/* 364:414 */                 totalMaxWidth += this.colWidths[jj];
/* 365:    */               }
/* 366:416 */               int dearth = this.cells[i][j].width - totalMaxWidth;
/* 367:417 */               if (dearth > 0)
/* 368:    */               {
/* 369:419 */                 int fractionalMaxWidth = 0;
/* 370:420 */                 int last = 0;
/* 371:421 */                 for (int jj = j; jj < j + colSpan; jj++)
/* 372:    */                 {
/* 373:422 */                   fractionalMaxWidth += this.colWidths[jj];
/* 374:423 */                   int current = dearth * fractionalMaxWidth / totalMaxWidth;
/* 375:424 */                   this.colWidths[jj] += current - last;
/* 376:425 */                   last = current;
/* 377:    */                 }
/* 378:    */               }
/* 379:    */             }
/* 380:429 */             int rowSpan = cell.rowSpan;
/* 381:430 */             if ((cell.height > this.rowHeights[i]) && (rowSpan > 1))
/* 382:    */             {
/* 383:431 */               int totalMaxHeight = 0;
/* 384:432 */               for (int ii = i; ii < i + rowSpan; ii++) {
/* 385:433 */                 totalMaxHeight += this.rowHeights[ii];
/* 386:    */               }
/* 387:435 */               int dearth = this.cells[i][j].height - totalMaxHeight;
/* 388:436 */               if (dearth > 0)
/* 389:    */               {
/* 390:438 */                 int fractionalMaxHeight = 0;
/* 391:439 */                 int last = 0;
/* 392:440 */                 for (int ii = i; ii < i + rowSpan; ii++)
/* 393:    */                 {
/* 394:441 */                   fractionalMaxHeight += this.rowHeights[ii];
/* 395:442 */                   int current = dearth * fractionalMaxHeight / totalMaxHeight;
/* 396:443 */                   this.rowHeights[ii] += current - last;
/* 397:444 */                   last = current;
/* 398:    */                 }
/* 399:    */               }
/* 400:    */             }
/* 401:    */           }
/* 402:    */         }
/* 403:    */       }
/* 404:452 */       int totalHeight = 0;
/* 405:453 */       for (int i = 0; i < this.rowHeights.length; i++) {
/* 406:454 */         totalHeight += this.rowHeights[i];
/* 407:    */       }
/* 408:456 */       int dearth = this.height - totalHeight;
/* 409:457 */       if (dearth > 0)
/* 410:    */       {
/* 411:458 */         int fractionalHeight = 0;
/* 412:459 */         int last = 0;
/* 413:460 */         for (int i = 0; i < this.rowHeights.length; i++)
/* 414:    */         {
/* 415:461 */           fractionalHeight += this.rowHeights[i];
/* 416:462 */           int current = dearth * fractionalHeight / totalHeight;
/* 417:463 */           this.rowHeights[i] += current - last;
/* 418:464 */           last = current;
/* 419:    */         }
/* 420:    */       }
/* 421:467 */       int totalWidth = 0;
/* 422:468 */       for (int j = 0; j < this.colWidths.length; j++) {
/* 423:469 */         totalWidth += this.colWidths[j];
/* 424:    */       }
/* 425:471 */       dearth = this.width - totalWidth;
/* 426:472 */       if (dearth > 0)
/* 427:    */       {
/* 428:473 */         int fractionalWidth = 0;
/* 429:474 */         int last = 0;
/* 430:475 */         for (int j = 0; j < this.colWidths.length; j++)
/* 431:    */         {
/* 432:476 */           fractionalWidth += this.colWidths[j];
/* 433:477 */           int current = dearth * fractionalWidth / totalWidth;
/* 434:478 */           this.colWidths[j] += current - last;
/* 435:479 */           last = current;
/* 436:    */         }
/* 437:    */       }
/* 438:482 */       int xBase = 0;
/* 439:483 */       int yBase = 0;
/* 440:484 */       switch (this.hAlign)
/* 441:    */       {
/* 442:    */       case -1: 
/* 443:486 */         xBase = this.x;
/* 444:487 */         break;
/* 445:    */       case 0: 
/* 446:489 */         xBase = this.x - totalWidth / 2;
/* 447:490 */         break;
/* 448:    */       case 1: 
/* 449:492 */         xBase = this.x - totalWidth;
/* 450:    */       }
/* 451:495 */       switch (this.vAlign)
/* 452:    */       {
/* 453:    */       case -1: 
/* 454:497 */         yBase = this.y;
/* 455:498 */         break;
/* 456:    */       case 0: 
/* 457:500 */         yBase = this.y - totalHeight / 2;
/* 458:501 */         break;
/* 459:    */       case 1: 
/* 460:503 */         yBase = this.y - totalHeight;
/* 461:    */       }
/* 462:507 */       int yCell = 0;
/* 463:508 */       for (int i = 0; i < this.cells.length; i++)
/* 464:    */       {
/* 465:509 */         int xCell = 0;
/* 466:510 */         for (int j = 0; j < this.colWidths.length; j++)
/* 467:    */         {
/* 468:511 */           BridgeBlueprintPrintable.Cell cell = this.cells[i][j];
/* 469:512 */           if (cell != null)
/* 470:    */           {
/* 471:513 */             int cellWidth = 0;
/* 472:514 */             for (int jj = j; jj < j + cell.colSpan; jj++) {
/* 473:515 */               cellWidth += this.colWidths[jj];
/* 474:    */             }
/* 475:517 */             int cellHeight = 0;
/* 476:518 */             for (int ii = i; ii < i + cell.rowSpan; ii++) {
/* 477:519 */               cellHeight += this.rowHeights[ii];
/* 478:    */             }
/* 479:521 */             cell.setLocation(xBase + xCell, yBase + yCell);
/* 480:522 */             cell.setSize(cellWidth, cellHeight);
/* 481:    */           }
/* 482:524 */           xCell += this.colWidths[j];
/* 483:    */         }
/* 484:526 */         this.width = xCell;
/* 485:527 */         yCell += this.rowHeights[i];
/* 486:    */       }
/* 487:529 */       this.height = yCell;
/* 488:    */     }
/* 489:    */     
/* 490:    */     private void paintRaw(Graphics2D g, int i0, int j0, int nRows, int nCols)
/* 491:    */     {
/* 492:533 */       for (int i = i0; i < i0 + nRows; i++) {
/* 493:534 */         for (int j = j0; j < j0 + nCols; j++)
/* 494:    */         {
/* 495:535 */           BridgeBlueprintPrintable.Cell cell = this.cells[i][j];
/* 496:536 */           if (cell != null) {
/* 497:537 */             cell.paint(g);
/* 498:    */           }
/* 499:    */         }
/* 500:    */       }
/* 501:    */     }
/* 502:    */     
/* 503:    */     public void paint(Graphics2D g, int i0, int j0, int nRows, int nCols)
/* 504:    */     {
/* 505:544 */       if (this.cells == null) {
/* 506:545 */         return;
/* 507:    */       }
/* 508:547 */       initialize(g);
/* 509:548 */       if ((nRows < 0) || (nRows > this.cells.length - this.nHeaderRows - i0)) {
/* 510:549 */         nRows = this.cells.length - this.nHeaderRows - i0;
/* 511:    */       }
/* 512:551 */       if ((nCols < 0) || (nCols > this.colWidths.length - j0)) {
/* 513:552 */         nCols = this.colWidths.length - j0;
/* 514:    */       }
/* 515:554 */       if ((nRows < 0) || (nCols < 0)) {
/* 516:555 */         return;
/* 517:    */       }
/* 518:557 */       paintRaw(g, 0, j0, this.nHeaderRows, nCols);
/* 519:558 */       AffineTransform savedTransform = g.getTransform();
/* 520:559 */       g.translate(0, this.cells[this.nHeaderRows][0].y - this.cells[(this.nHeaderRows + i0)][0].y);
/* 521:560 */       paintRaw(g, this.nHeaderRows + i0, j0, nRows, nCols);
/* 522:561 */       g.setTransform(savedTransform);
/* 523:    */     }
/* 524:    */     
/* 525:    */     public void paintSideBySide(Graphics2D g, int i0, int nRows, int nSlices, int hSep)
/* 526:    */     {
/* 527:565 */       AffineTransform savedTransform = g.getTransform();
/* 528:566 */       if (nRows <= 0) {
/* 529:567 */         return;
/* 530:    */       }
/* 531:569 */       int i = i0;
/* 532:570 */       for (int s = 0; s < nSlices; s++)
/* 533:    */       {
/* 534:571 */         if (i >= this.cells.length) {
/* 535:    */           break;
/* 536:    */         }
/* 537:574 */         paint(g, i, 0, nRows, -1);
/* 538:575 */         g.translate(this.width + hSep, 0);
/* 539:576 */         i += nRows;
/* 540:    */       }
/* 541:578 */       g.setTransform(savedTransform);
/* 542:    */     }
/* 543:    */     
/* 544:    */     public int getNRowsThatFit(int i0, int height)
/* 545:    */     {
/* 546:582 */       if (i0 >= this.rowHeights.length) {
/* 547:583 */         return -1;
/* 548:    */       }
/* 549:585 */       int nRows = 0;
/* 550:586 */       int currentHeight = 0;
/* 551:587 */       for (int i = 0; i < this.nHeaderRows; i++)
/* 552:    */       {
/* 553:588 */         int newHeight = currentHeight + this.rowHeights[i];
/* 554:589 */         if (newHeight > height) {
/* 555:590 */           return 0;
/* 556:    */         }
/* 557:592 */         currentHeight = newHeight;
/* 558:    */       }
/* 559:594 */       for (int i = i0 + this.nHeaderRows; i < this.rowHeights.length; i++)
/* 560:    */       {
/* 561:595 */         int newHeight = currentHeight + this.rowHeights[i];
/* 562:596 */         if (newHeight >= height) {
/* 563:597 */           return nRows;
/* 564:    */         }
/* 565:599 */         currentHeight = newHeight;
/* 566:600 */         nRows++;
/* 567:    */       }
/* 568:602 */       return nRows;
/* 569:    */     }
/* 570:    */     
/* 571:    */     public void paint(Graphics2D g)
/* 572:    */     {
/* 573:606 */       if (this.cells == null) {
/* 574:607 */         return;
/* 575:    */       }
/* 576:609 */       paint(g, 0, 0, -1, -1);
/* 577:    */     }
/* 578:    */     
/* 579:    */     public void setLocation(int x, int y, int hAlign, int vAlign)
/* 580:    */     {
/* 581:613 */       this.x = x;
/* 582:614 */       this.y = y;
/* 583:615 */       this.hAlign = ((byte)hAlign);
/* 584:616 */       this.vAlign = ((byte)vAlign);
/* 585:617 */       this.dirty = true;
/* 586:    */     }
/* 587:    */     
/* 588:    */     public void setSize(int width, int height)
/* 589:    */     {
/* 590:622 */       this.width = width;
/* 591:623 */       this.height = height;
/* 592:624 */       this.dirty = true;
/* 593:    */     }
/* 594:    */     
/* 595:    */     public void setContents(BridgeBlueprintPrintable.Cell[][] cells, int headerCount)
/* 596:    */     {
/* 597:629 */       for (int i = 0; i < cells.length; i++) {
/* 598:630 */         for (int j = 0; j < cells[0].length; j++)
/* 599:    */         {
/* 600:631 */           BridgeBlueprintPrintable.Cell cell = cells[i][j];
/* 601:632 */           if (cell != null)
/* 602:    */           {
/* 603:633 */             byte correctColSpan = 1;
/* 604:634 */             for (int jj = j + 1; (jj < j + cell.colSpan) && (jj < cells[0].length); jj++) {
/* 605:635 */               correctColSpan = (byte)(correctColSpan + 1);
/* 606:    */             }
/* 607:637 */             cell.colSpan = correctColSpan;
/* 608:638 */             byte correctRowSpan = 1;
/* 609:639 */             for (int ii = i + 1; (ii < i + cell.rowSpan) && (ii < cells.length); ii++) {
/* 610:640 */               correctRowSpan = (byte)(correctRowSpan + 1);
/* 611:    */             }
/* 612:642 */             cell.rowSpan = correctRowSpan;
/* 613:643 */             for (int ii = i; ii < i + cell.rowSpan; ii++) {
/* 614:644 */               for (int jj = j; jj < j + cell.colSpan; jj++) {
/* 615:645 */                 if ((ii != i) || (jj != j)) {
/* 616:646 */                   cells[ii][jj] = null;
/* 617:    */                 }
/* 618:    */               }
/* 619:    */             }
/* 620:    */           }
/* 621:    */         }
/* 622:    */       }
/* 623:653 */       this.cells = cells;
/* 624:654 */       this.nHeaderRows = headerCount;
/* 625:655 */       this.dirty = true;
/* 626:    */     }
/* 627:    */   }
/* 628:    */   
/* 629:    */   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
/* 630:    */     throws PrinterException
/* 631:    */   {
/* 632:669 */     Graphics2D g = (Graphics2D)graphics;
/* 633:    */     
/* 634:671 */     g.setFont(this.standardFont);
/* 635:672 */     g.setStroke(this.standardStroke);
/* 636:    */     
/* 637:674 */     int pageWidth = (int)pageFormat.getImageableWidth();
/* 638:675 */     int pageHeight = (int)pageFormat.getImageableHeight();
/* 639:678 */     if (this.blueprintExtent == null) {
/* 640:679 */       this.blueprintExtent = getBlueprintExtent(pageWidth, pageHeight);
/* 641:    */     }
/* 642:681 */     this.memberTable.initialize(g);
/* 643:    */     
/* 644:    */ 
/* 645:684 */     int sliceSep = 36;
/* 646:685 */     int maxNSlices = Math.max(1, (pageWidth - 36) / (this.memberTable.width + 36));
/* 647:    */     
/* 648:687 */     g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
/* 649:688 */     int standardSheetHeight = printStandardSheet(g, pageFormat, pageIndex + 1);
/* 650:    */     
/* 651:690 */     int nMembersRemaining = this.bridge.getMembers().size();
/* 652:691 */     int i0 = -1;
/* 653:692 */     int i1 = 0;
/* 654:693 */     int nRows = 0;
/* 655:694 */     int nSlices = 0;
/* 656:695 */     int yTable = 0;
/* 657:696 */     for (int i = 0;; i++)
/* 658:    */     {
/* 659:697 */       int availableHeight = 0;
/* 660:698 */       yTable = i == 0 ? (this.blueprintExtent.y + 19) / 20 + 36 : 36;
/* 661:699 */       availableHeight = standardSheetHeight - yTable;
/* 662:700 */       availableHeight -= 8;
/* 663:701 */       nRows = this.memberTable.getNRowsThatFit(i1, availableHeight);
/* 664:702 */       if (nRows == -1) {
/* 665:704 */         return 1;
/* 666:    */       }
/* 667:707 */       if ((i == 0) && (nRows <= 4)) {
/* 668:708 */         nRows = 0;
/* 669:    */       }
/* 670:710 */       if ((i > 0) && (nRows == 0)) {
/* 671:712 */         nRows = 1;
/* 672:    */       }
/* 673:714 */       if (nRows == 0)
/* 674:    */       {
/* 675:715 */         nSlices = 1;
/* 676:    */       }
/* 677:    */       else
/* 678:    */       {
/* 679:718 */         nSlices = Math.min((nMembersRemaining + nRows - 1) / nRows, maxNSlices);
/* 680:    */         
/* 681:720 */         nRows -= Math.max(0, (nSlices * nRows - nMembersRemaining) / nSlices);
/* 682:    */       }
/* 683:722 */       i0 = i1;
/* 684:723 */       i1 += nRows * nSlices;
/* 685:724 */       if (i == pageIndex) {
/* 686:    */         break;
/* 687:    */       }
/* 688:728 */       nMembersRemaining -= nSlices * nRows;
/* 689:732 */       if (nMembersRemaining <= 0) {
/* 690:733 */         return 1;
/* 691:    */       }
/* 692:    */     }
/* 693:737 */     if (pageIndex == 0) {
/* 694:738 */       printBlueprint(g, pageWidth, standardSheetHeight);
/* 695:    */     }
/* 696:740 */     AffineTransform savedTransform = g.getTransform();
/* 697:741 */     int margin = pageWidth - (nSlices * this.memberTable.width + (nSlices + 1) * 36);
/* 698:742 */     g.translate(36 + margin / 2, yTable);
/* 699:743 */     this.memberTable.paintSideBySide(g, i0, nRows, nSlices, 36);
/* 700:744 */     g.setTransform(savedTransform);
/* 701:745 */     return 0;
/* 702:    */   }
/* 703:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeBlueprintPrintable
 * JD-Core Version:    0.7.0.1
 */