/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.Graphics;
/*   7:    */ import java.awt.Graphics2D;
/*   8:    */ import java.awt.Rectangle;
/*   9:    */ import java.awt.Stroke;
/*  10:    */ import java.awt.font.FontRenderContext;
/*  11:    */ import java.awt.font.LineMetrics;
/*  12:    */ import java.awt.geom.Rectangle2D;
/*  13:    */ import java.text.DecimalFormat;
/*  14:    */ import java.text.NumberFormat;
/*  15:    */ import javax.swing.JLabel;
/*  16:    */ import org.jdesktop.application.ResourceMap;
/*  17:    */ 
/*  18:    */ public class StrengthCurve
/*  19:    */   extends JLabel
/*  20:    */ {
/*  21: 38 */   private Material material = null;
/*  22: 39 */   private Shape shape = null;
/*  23: 40 */   private Member[] members = null;
/*  24: 41 */   private int selectedMemberIndex = 0;
/*  25: 42 */   private Member[][] memberLists = (Member[][])null;
/*  26: 43 */   private Analysis analysis = null;
/*  27: 44 */   private boolean showSlenderness = true;
/*  28: 45 */   private boolean showAll = false;
/*  29:    */   private static final int widthPad = 20;
/*  30:    */   private static final int heightPad = 8;
/*  31: 48 */   private static final int[] multipliers = { 2, 4, 5, 10 };
/*  32: 49 */   private final ResourceMap resourceMap = WPBDApp.getResourceMap(StrengthCurve.class);
/*  33:    */   private static final String longestYLabel = "888888";
/*  34: 51 */   private static int titleLabelSep = 4;
/*  35: 52 */   private static int labelTickSep = 0;
/*  36: 53 */   private static int tickSize = 4;
/*  37:    */   private static final int dotRadius = 2;
/*  38: 55 */   private final NumberFormat labelFormatter = new DecimalFormat("0");
/*  39: 56 */   private final Stroke plotStroke = new BasicStroke(3.0F);
/*  40: 57 */   private float[] gridDash = { 1.0F, 5.0F };
/*  41: 58 */   private final Stroke gridStroke = new BasicStroke(0.0F, 0, 0, 10.0F, this.gridDash, 0.0F);
/*  42: 59 */   private float[] lengthLeaderDash = { 4.0F, 8.0F };
/*  43: 60 */   private final Stroke lengthTicksStroke = new BasicStroke(0.0F, 0, 0, 10.0F, this.lengthLeaderDash, 0.0F);
/*  44: 61 */   private final Stroke bracketOkStroke = new BasicStroke();
/*  45: 62 */   private final Rectangle bounds = new Rectangle();
/*  46: 63 */   private static final Color subduedBlue = new Color(192, 192, 255);
/*  47: 64 */   private static final Color subduedRed = new Color(255, 192, 192);
/*  48: 65 */   private final Color bracketOkColor = new Color(0, 144, 0);
/*  49: 66 */   private final Color subduedBracketOkColor = new Color(112, 255, 112);
/*  50: 67 */   private float widthYLabel = -1.0F;
/*  51: 68 */   private float heightText = -1.0F;
/*  52: 69 */   private int yPlotAreaTop = 0;
/*  53: 70 */   private int yPlotAreaBottom = 1;
/*  54: 71 */   private int xPlotAreaLeft = 0;
/*  55: 72 */   private int xPlotAreaRight = 1;
/*  56: 73 */   private int heightPlotArea = 1;
/*  57: 74 */   private int widthPlotArea = 1;
/*  58: 75 */   private double xMax = 1.0D;
/*  59: 76 */   private double yMax = 1.0D;
/*  60:    */   
/*  61:    */   public void setShowAll(boolean showAll)
/*  62:    */   {
/*  63: 84 */     this.showAll = showAll;
/*  64: 85 */     repaint();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setSelectedMemberIndex(int index)
/*  68:    */   {
/*  69: 94 */     this.selectedMemberIndex = ((this.members != null) && (0 <= index) && (index < this.members.length) ? index : -1);
/*  70: 95 */     repaint();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void initialize(Material material, Shape shape, Member[] members, Member[][] memberLists, Analysis analysis, boolean showSlenderness)
/*  74:    */   {
/*  75:115 */     this.material = material;
/*  76:116 */     this.shape = shape;
/*  77:117 */     this.members = members;
/*  78:118 */     this.memberLists = memberLists;
/*  79:119 */     this.analysis = analysis;
/*  80:120 */     this.showSlenderness = showSlenderness;
/*  81:121 */     repaint();
/*  82:    */   }
/*  83:    */   
/*  84:    */   private double getDivisionSize(double max, int nDivisions)
/*  85:    */   {
/*  86:125 */     double size = Math.pow(10.0D, Math.ceil(Math.log10(max)));
/*  87:126 */     double newSize = 1.0D;
/*  88:    */     for (;;)
/*  89:    */     {
/*  90:128 */       for (int i = 0; i < multipliers.length; i++)
/*  91:    */       {
/*  92:129 */         newSize = size / multipliers[i];
/*  93:130 */         if (newSize * nDivisions <= max) {
/*  94:131 */           return newSize;
/*  95:    */         }
/*  96:    */       }
/*  97:134 */       size = newSize;
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected void paintComponent(Graphics g0)
/* 102:    */   {
/* 103:145 */     Graphics2D g = (Graphics2D)g0;
/* 104:146 */     Stroke savedStroke = g.getStroke();
/* 105:148 */     if (this.widthYLabel < 0.0F)
/* 106:    */     {
/* 107:149 */       FontRenderContext frc = g.getFontRenderContext();
/* 108:150 */       Font font = Labeler.getFont();
/* 109:151 */       LineMetrics metrics = font.getLineMetrics("888888", frc);
/* 110:152 */       this.widthYLabel = ((float)font.getStringBounds("888888", frc).getWidth());
/* 111:153 */       this.heightText = (metrics.getAscent() + metrics.getDescent());
/* 112:    */     }
/* 113:156 */     int w = getWidth();
/* 114:157 */     int h = getHeight();
/* 115:158 */     g.setColor(Color.WHITE);
/* 116:159 */     g.fillRect(0, 0, w, h);
/* 117:    */     
/* 118:    */ 
/* 119:162 */     g.setColor(Color.BLACK);
/* 120:    */     
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:169 */     this.yPlotAreaTop = Math.round(8.0F + this.heightText + labelTickSep + tickSize);
/* 127:170 */     this.yPlotAreaBottom = Math.round(h - 1 - 8 - this.heightText - titleLabelSep - this.heightText - labelTickSep - tickSize);
/* 128:171 */     this.xPlotAreaLeft = Math.round(20.0F + this.heightText + titleLabelSep + this.widthYLabel + labelTickSep + tickSize);
/* 129:172 */     this.xPlotAreaRight = (w - 1 - 20);
/* 130:173 */     this.heightPlotArea = (this.yPlotAreaBottom - this.yPlotAreaTop);
/* 131:174 */     this.widthPlotArea = (this.xPlotAreaRight - this.xPlotAreaLeft);
/* 132:    */     
/* 133:    */ 
/* 134:177 */     g.drawLine(this.xPlotAreaLeft, this.yPlotAreaBottom + tickSize, this.xPlotAreaLeft, this.yPlotAreaTop - tickSize);
/* 135:178 */     g.drawLine(this.xPlotAreaLeft - tickSize, this.yPlotAreaBottom, this.xPlotAreaRight + tickSize, this.yPlotAreaBottom);
/* 136:    */     
/* 137:    */ 
/* 138:181 */     boolean doShowAll = (this.showAll) && (this.memberLists != null) && (this.memberLists.length > 0);
/* 139:182 */     boolean doShowOne = (this.material != null) && (this.shape != null);
/* 140:185 */     if ((!doShowAll) && (!doShowOne)) {
/* 141:186 */       return;
/* 142:    */     }
/* 143:190 */     this.yMax = 0.0D;
/* 144:191 */     if (doShowAll) {
/* 145:192 */       for (int i = 0; i < this.memberLists.length; i++)
/* 146:    */       {
/* 147:193 */         Member member = this.memberLists[i][0];
/* 148:194 */         this.yMax = Math.max(this.yMax, Inventory.tensileStrength(member.getMaterial(), member.getShape()));
/* 149:    */       }
/* 150:    */     }
/* 151:197 */     if (doShowOne) {
/* 152:198 */       this.yMax = Math.max(this.yMax, Inventory.tensileStrength(this.material, this.shape));
/* 153:    */     }
/* 154:200 */     if ((this.analysis != null) && (this.members != null)) {
/* 155:201 */       for (int i = 0; i < this.members.length; i++)
/* 156:    */       {
/* 157:202 */         this.yMax = Math.max(this.yMax, this.analysis.getMemberCompressiveForce(this.members[i].getIndex()));
/* 158:203 */         this.yMax = Math.max(this.yMax, this.analysis.getMemberTensileForce(this.members[i].getIndex()));
/* 159:    */       }
/* 160:    */     }
/* 161:208 */     double dyTick = getDivisionSize(this.yMax, (this.yPlotAreaBottom - this.yPlotAreaTop) / 50);
/* 162:209 */     int nYTicks = (int)Math.ceil(this.yMax / dyTick);
/* 163:210 */     this.yMax = (nYTicks * dyTick);
/* 164:    */     
/* 165:    */ 
/* 166:213 */     int actualLabelWidth = 0;
/* 167:214 */     int ix = this.xPlotAreaLeft - tickSize - titleLabelSep;
/* 168:215 */     for (int i = 0; i <= nYTicks; i++)
/* 169:    */     {
/* 170:216 */       double t = i / nYTicks;
/* 171:217 */       double y = t * this.yMax;
/* 172:218 */       int iy = this.yPlotAreaBottom - (int)Math.round(t * this.heightPlotArea);
/* 173:219 */       if (i > 0)
/* 174:    */       {
/* 175:220 */         g.setColor(Color.GRAY);
/* 176:221 */         g.setStroke(this.gridStroke);
/* 177:222 */         g.drawLine(this.xPlotAreaLeft, iy, this.xPlotAreaRight, iy);
/* 178:223 */         g.setStroke(savedStroke);
/* 179:    */       }
/* 180:225 */       g.setColor(Color.BLACK);
/* 181:226 */       g.drawLine(this.xPlotAreaLeft, iy, this.xPlotAreaLeft - tickSize, iy);
/* 182:227 */       Labeler.drawJustified(g0, this.labelFormatter.format(y), ix, iy, 3, 2, null, this.bounds);
/* 183:    */       
/* 184:229 */       actualLabelWidth = Math.max(actualLabelWidth, this.bounds.width);
/* 185:    */     }
/* 186:233 */     ix -= actualLabelWidth + titleLabelSep;
/* 187:234 */     Labeler.drawRotatedAndJustified(g0, this.resourceMap.getString("yAxisTitle.text", new Object[0]), 90.0D, ix, (this.yPlotAreaBottom + this.yPlotAreaTop) / 2, 2, 4, null, null, null);
/* 188:    */     
/* 189:    */ 
/* 190:    */ 
/* 191:    */ 
/* 192:239 */     this.xMax = 12.0D;
/* 193:    */     
/* 194:241 */     double nXTicks = 12.0D;
/* 195:    */     
/* 196:    */ 
/* 197:244 */     int iy = this.yPlotAreaBottom + tickSize + titleLabelSep;
/* 198:245 */     for (int i = 0; i <= 12.0D; i++)
/* 199:    */     {
/* 200:246 */       double t = i / 12.0D;
/* 201:247 */       double x = t * this.xMax;
/* 202:248 */       ix = this.xPlotAreaLeft + (int)Math.round(t * this.widthPlotArea);
/* 203:249 */       if (i > 0)
/* 204:    */       {
/* 205:250 */         g.setColor(Color.GRAY);
/* 206:251 */         g.setStroke(this.gridStroke);
/* 207:252 */         g.drawLine(ix, this.yPlotAreaBottom, ix, this.yPlotAreaTop);
/* 208:253 */         g.setStroke(savedStroke);
/* 209:    */       }
/* 210:255 */       g.setColor(Color.BLACK);
/* 211:256 */       g.drawLine(ix, this.yPlotAreaBottom, ix, this.yPlotAreaBottom + tickSize);
/* 212:257 */       Labeler.drawJustified(g0, this.labelFormatter.format(x), ix, iy, 2, 4, null);
/* 213:    */     }
/* 214:261 */     iy = (int)(iy + (this.heightText + titleLabelSep));
/* 215:262 */     Labeler.drawJustified(g0, this.resourceMap.getString("xAxisTitle.text", new Object[0]), (this.xPlotAreaLeft + this.xPlotAreaRight) / 2, iy, 2, 4, null);
/* 216:265 */     if (doShowAll) {
/* 217:266 */       for (int i = 0; i < this.memberLists.length; i++)
/* 218:    */       {
/* 219:267 */         Member member = this.memberLists[i][0];
/* 220:268 */         if ((member.getMaterial() != this.material) || (member.getShape() != this.shape)) {
/* 221:269 */           plot(g, member.getMaterial(), member.getShape(), true);
/* 222:    */         }
/* 223:    */       }
/* 224:    */     }
/* 225:273 */     if (doShowOne)
/* 226:    */     {
/* 227:274 */       if (this.members != null) {
/* 228:275 */         plotMemberLengths(g, this.members);
/* 229:    */       }
/* 230:277 */       plot(g, this.material, this.shape, false);
/* 231:    */     }
/* 232:    */   }
/* 233:    */   
/* 234:    */   private void plot(Graphics2D g, Material material, Shape shape, boolean subdued)
/* 235:    */   {
/* 236:282 */     Stroke savedStroke = g.getStroke();
/* 237:283 */     g.setStroke(this.plotStroke);
/* 238:284 */     double yTensile = Inventory.tensileStrength(material, shape);
/* 239:285 */     int iy = this.yPlotAreaBottom - (int)Math.round(yTensile / this.yMax * this.heightPlotArea);
/* 240:286 */     g.setColor(subdued ? subduedBlue : Color.BLUE);
/* 241:287 */     g.drawLine(this.xPlotAreaLeft, iy, this.xPlotAreaRight, iy);
/* 242:    */     
/* 243:289 */     int nPlotPoints = 32;
/* 244:290 */     double yCompressive = Inventory.compressiveStrength(material, shape, 0.0D);
/* 245:291 */     int iy0 = this.yPlotAreaBottom - (int)Math.round(yCompressive / this.yMax * this.heightPlotArea);
/* 246:292 */     int ix0 = this.xPlotAreaLeft;
/* 247:293 */     g.setColor(subdued ? subduedRed : Color.RED);
/* 248:294 */     for (int i = 1; i <= 32; i++)
/* 249:    */     {
/* 250:295 */       double t = i / 32.0D;
/* 251:296 */       double x = t * this.xMax;
/* 252:297 */       int ix1 = this.xPlotAreaLeft + (int)Math.round(t * this.widthPlotArea);
/* 253:298 */       yCompressive = Inventory.compressiveStrength(material, shape, x);
/* 254:299 */       int iy1 = this.yPlotAreaBottom - (int)Math.round(yCompressive / this.yMax * this.heightPlotArea);
/* 255:300 */       g.drawLine(ix0, iy0, ix1, iy1);
/* 256:301 */       ix0 = ix1;
/* 257:302 */       iy0 = iy1;
/* 258:    */     }
/* 259:304 */     g.setStroke(savedStroke);
/* 260:305 */     g.setColor(Color.BLACK);
/* 261:    */   }
/* 262:    */   
/* 263:    */   private void plotBracket(Graphics2D g, int number, double force, int ix, int iy, boolean subdued)
/* 264:    */   {
/* 265:309 */     Color savedColor = g.getColor();
/* 266:310 */     if (force > 0.0D)
/* 267:    */     {
/* 268:312 */       int iyForce = this.yPlotAreaBottom - (int)Math.round(force / this.yMax * this.heightPlotArea);
/* 269:314 */       if (iyForce < iy)
/* 270:    */       {
/* 271:315 */         g.setColor(savedColor);
/* 272:316 */         g.setStroke(this.plotStroke);
/* 273:317 */         g.drawLine(ix, iyForce, ix, iy);
/* 274:    */       }
/* 275:    */       else
/* 276:    */       {
/* 277:319 */         g.setColor(subdued ? this.subduedBracketOkColor : this.bracketOkColor);
/* 278:320 */         g.setStroke(this.bracketOkStroke);
/* 279:321 */         g.drawLine(ix - tickSize, iyForce, ix + tickSize, iyForce);
/* 280:322 */         g.drawLine(ix, iyForce, ix, iy);
/* 281:    */       }
/* 282:325 */       g.setColor(savedColor);
/* 283:326 */       g.setStroke(this.plotStroke);
/* 284:327 */       g.drawLine(ix - tickSize, iyForce, ix + tickSize, iyForce);
/* 285:    */     }
/* 286:    */   }
/* 287:    */   
/* 288:    */   private void plotMemberLengths(Graphics2D g, Member[] members)
/* 289:    */   {
/* 290:332 */     if (members.length == 1)
/* 291:    */     {
/* 292:333 */       if (this.showSlenderness) {
/* 293:334 */         plotSlendernessLimit(g, members[0]);
/* 294:    */       }
/* 295:336 */       plot1MemberLength(g, members[0], false);
/* 296:337 */       return;
/* 297:    */     }
/* 298:339 */     for (int i = 0; i < members.length; i++) {
/* 299:340 */       if (this.selectedMemberIndex != i) {
/* 300:341 */         plot1MemberLength(g, members[i], true);
/* 301:    */       }
/* 302:    */     }
/* 303:344 */     if (this.selectedMemberIndex >= 0) {
/* 304:345 */       plot1MemberLength(g, members[this.selectedMemberIndex], false);
/* 305:    */     }
/* 306:    */   }
/* 307:    */   
/* 308:    */   private void plotSlendernessLimit(Graphics2D g, Member member)
/* 309:    */   {
/* 310:350 */     double length = member.getShape().getMaxSlendernessLength();
/* 311:351 */     double t = length / this.xMax;
/* 312:352 */     if (t <= 1.0D)
/* 313:    */     {
/* 314:354 */       int ix = this.xPlotAreaLeft + (int)Math.round(t * this.widthPlotArea);
/* 315:355 */       g.setColor(Color.MAGENTA);
/* 316:356 */       g.drawLine(ix, this.yPlotAreaBottom, ix, this.yPlotAreaTop - tickSize);
/* 317:357 */       Labeler.drawRotatedAndJustified(g, this.resourceMap.getString("maxSlendernessLength.text", new Object[0]), 90.0D, ix, (this.yPlotAreaBottom + this.yPlotAreaTop) / 2, 2, 4, null, null, null);
/* 318:    */       
/* 319:    */ 
/* 320:360 */       g.setColor(Color.BLACK);
/* 321:    */     }
/* 322:    */   }
/* 323:    */   
/* 324:    */   private void plot1MemberLength(Graphics2D g, Member member, boolean subdued)
/* 325:    */   {
/* 326:365 */     double length = member.getLength();
/* 327:366 */     double t = length / this.xMax;
/* 328:367 */     if (t <= 1.0D)
/* 329:    */     {
/* 330:368 */       Stroke savedStroke = g.getStroke();
/* 331:369 */       double tensileStrength = Inventory.tensileStrength(member.getMaterial(), member.getShape());
/* 332:370 */       int iyTensileStrength = this.yPlotAreaBottom - (int)Math.round(tensileStrength / this.yMax * this.heightPlotArea);
/* 333:371 */       double compressiveStrength = Inventory.compressiveStrength(member.getMaterial(), member.getShape(), length);
/* 334:372 */       int iyCompressiveStrength = this.yPlotAreaBottom - (int)Math.round(compressiveStrength / this.yMax * this.heightPlotArea);
/* 335:    */       
/* 336:    */ 
/* 337:375 */       int ix = this.xPlotAreaLeft + (int)Math.round(t * this.widthPlotArea);
/* 338:376 */       g.setColor(subdued ? Color.LIGHT_GRAY : Color.BLACK);
/* 339:377 */       g.setStroke(this.lengthTicksStroke);
/* 340:378 */       g.drawLine(ix, this.yPlotAreaBottom, ix, this.yPlotAreaTop - tickSize);
/* 341:    */       
/* 342:    */ 
/* 343:381 */       g.setStroke(savedStroke);
/* 344:382 */       if (!subdued) {
/* 345:383 */         Labeler.drawJustified(g, Integer.toString(member.getNumber()), ix, this.yPlotAreaTop - tickSize, 2, 6, Member.labelBackground);
/* 346:    */       }
/* 347:387 */       if (this.analysis != null)
/* 348:    */       {
/* 349:388 */         g.setColor(subdued ? subduedRed : Color.RED);
/* 350:389 */         if (!subdued) {
/* 351:390 */           g.fillRect(ix - 2, iyCompressiveStrength - 2, 5, 5);
/* 352:    */         }
/* 353:392 */         if (this.analysis != null) {
/* 354:393 */           plotBracket(g, member.getNumber(), this.analysis.getMemberCompressiveForce(member.getIndex()), ix, iyCompressiveStrength, subdued);
/* 355:    */         }
/* 356:395 */         g.setColor(subdued ? subduedBlue : Color.BLUE);
/* 357:396 */         if (!subdued) {
/* 358:397 */           g.fillRect(ix - 2, iyTensileStrength - 2, 5, 5);
/* 359:    */         }
/* 360:399 */         if (this.analysis != null) {
/* 361:400 */           plotBracket(g, member.getNumber(), this.analysis.getMemberTensileForce(member.getIndex()), ix, iyTensileStrength, subdued);
/* 362:    */         }
/* 363:    */       }
/* 364:403 */       g.setStroke(savedStroke);
/* 365:    */     }
/* 366:    */   }
/* 367:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.StrengthCurve
 * JD-Core Version:    0.7.0.1
 */