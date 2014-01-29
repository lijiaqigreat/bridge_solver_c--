/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Point;
/*   4:    */ import java.awt.Rectangle;
/*   5:    */ import java.awt.geom.Rectangle2D.Double;
/*   6:    */ 
/*   7:    */ public class ViewportTransform
/*   8:    */ {
/*   9:    */   public static final int LEFT = -1;
/*  10:    */   public static final int CENTER = 0;
/*  11:    */   public static final int RIGHT = 1;
/*  12:    */   public static final int TOP = -1;
/*  13:    */   public static final int BOTTOM = 1;
/*  14: 51 */   private double xWindow = 0.0D;
/*  15: 52 */   private double yWindow = 0.0D;
/*  16: 53 */   private double zWindow = 0.0D;
/*  17: 54 */   private double widthWindow = 1.0D;
/*  18: 55 */   private double heightWindow = 1.0D;
/*  19: 56 */   private double vpTx = 0.5D;
/*  20: 57 */   private double vpTy = 0.5D;
/*  21: 58 */   private double vpX = 0.5D;
/*  22: 59 */   private double vpY = 0.5D;
/*  23: 60 */   private double xViewport = 0.0D;
/*  24: 61 */   private double yViewport = 0.0D;
/*  25: 62 */   private double widthViewport = 1.0D;
/*  26: 63 */   private double heightViewport = 1.0D;
/*  27: 64 */   private double xMargin = 0.0D;
/*  28: 65 */   private double yMargin = 0.0D;
/*  29: 66 */   private double xScaleFactor = 1.0D;
/*  30: 67 */   private double yScaleFactor = 1.0D;
/*  31: 68 */   private double zScaleFactor = 1.0D;
/*  32: 69 */   private int nWindowUpdates = 0;
/*  33: 70 */   private int nViewportUpdates = 0;
/*  34: 71 */   private int hAlign = 0;
/*  35: 72 */   private int vAlign = 0;
/*  36:    */   
/*  37:    */   public int getAbsWidthViewport()
/*  38:    */   {
/*  39: 80 */     return Math.abs((int)(0.5D + this.widthViewport));
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getAbsHeightViewport()
/*  43:    */   {
/*  44: 89 */     return Math.abs((int)(0.5D + this.heightViewport));
/*  45:    */   }
/*  46:    */   
/*  47:    */   public int getUsedViewportWidth()
/*  48:    */   {
/*  49: 98 */     return Math.abs((int)(0.5D + this.widthViewport - this.xMargin));
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getUsedHeightViewport()
/*  53:    */   {
/*  54:107 */     return Math.abs((int)(0.5D + this.heightViewport - this.yMargin));
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setHAlign(int hAlign)
/*  58:    */   {
/*  59:116 */     this.hAlign = hAlign;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setVAlign(int vAlign)
/*  63:    */   {
/*  64:125 */     this.vAlign = vAlign;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setVanishingPoint(double vpTx, double vpTy, double zWindow)
/*  68:    */   {
/*  69:136 */     this.vpTx = vpTx;
/*  70:137 */     this.vpTy = vpTy;
/*  71:138 */     this.zWindow = zWindow;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setZScale(double zScale)
/*  75:    */   {
/*  76:148 */     this.zScaleFactor = zScale;
/*  77:    */   }
/*  78:    */   
/*  79:    */   private void setScaleFactor()
/*  80:    */   {
/*  81:152 */     if ((this.widthWindow == 0.0D) || (this.heightWindow == 0.0D))
/*  82:    */     {
/*  83:153 */       this.xScaleFactor = (this.yScaleFactor = 1.0D);
/*  84:154 */       return;
/*  85:    */     }
/*  86:156 */     double sfX = this.widthViewport / this.widthWindow;
/*  87:157 */     double sfY = this.heightViewport / this.heightWindow;
/*  88:158 */     if (Math.abs(sfX) < Math.abs(sfY))
/*  89:    */     {
/*  90:159 */       this.xMargin = 0.0D;
/*  91:160 */       this.xScaleFactor = sfX;
/*  92:161 */       this.yScaleFactor = Math.copySign(sfX, sfY);
/*  93:162 */       double margin = this.heightViewport - this.heightWindow * this.yScaleFactor;
/*  94:163 */       switch (this.vAlign)
/*  95:    */       {
/*  96:    */       case 1: 
/*  97:165 */         this.yMargin = 0.0D;
/*  98:166 */         break;
/*  99:    */       case -1: 
/* 100:168 */         this.yMargin = margin;
/* 101:169 */         break;
/* 102:    */       default: 
/* 103:171 */         this.yMargin = (0.5D * margin);
/* 104:    */       }
/* 105:    */     }
/* 106:    */     else
/* 107:    */     {
/* 108:175 */       this.yMargin = 0.0D;
/* 109:176 */       this.yScaleFactor = sfY;
/* 110:177 */       this.xScaleFactor = Math.copySign(sfY, sfX);
/* 111:178 */       double margin = this.widthViewport - this.widthWindow * this.xScaleFactor;
/* 112:179 */       switch (this.hAlign)
/* 113:    */       {
/* 114:    */       case -1: 
/* 115:181 */         this.xMargin = 0.0D;
/* 116:182 */         break;
/* 117:    */       case 1: 
/* 118:184 */         this.xMargin = margin;
/* 119:185 */         break;
/* 120:    */       default: 
/* 121:187 */         this.xMargin = (0.5D * margin);
/* 122:    */       }
/* 123:    */     }
/* 124:191 */     this.vpX = worldToViewportX(this.xWindow + this.vpTx * this.widthWindow);
/* 125:192 */     this.vpY = worldToViewportY(this.yWindow + this.vpTy * this.heightWindow);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setWindow(double x, double y, double width, double height)
/* 129:    */   {
/* 130:204 */     this.xWindow = x;
/* 131:205 */     this.yWindow = y;
/* 132:206 */     this.widthWindow = width;
/* 133:207 */     this.heightWindow = height;
/* 134:208 */     this.nWindowUpdates += 1;
/* 135:209 */     setScaleFactor();
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setWindow(Rectangle2D.Double w)
/* 139:    */   {
/* 140:218 */     setWindow(w.x, w.y, w.width, w.height);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void setViewport(double x, double y, double width, double height)
/* 144:    */   {
/* 145:230 */     this.xViewport = x;
/* 146:231 */     this.yViewport = y;
/* 147:232 */     this.widthViewport = width;
/* 148:233 */     this.heightViewport = height;
/* 149:234 */     this.nViewportUpdates += 1;
/* 150:235 */     setScaleFactor();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public boolean isSet()
/* 154:    */   {
/* 155:244 */     return (this.nViewportUpdates > 0) && (this.nWindowUpdates > 0);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public int getUpdateKey()
/* 159:    */   {
/* 160:254 */     return this.nWindowUpdates << 16 | this.nViewportUpdates;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public int worldToViewportX(double x)
/* 164:    */   {
/* 165:264 */     return (int)(0.5D + this.xMargin + this.xViewport + (x - this.xWindow) * this.xScaleFactor);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public int worldToViewportY(double y)
/* 169:    */   {
/* 170:274 */     return (int)(0.5D + this.yMargin + this.yViewport + (y - this.yWindow) * this.yScaleFactor);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public boolean worldToViewport(int[] dst_x, int[] dst_y, int dst_ofs, double x, double y, double z)
/* 174:    */   {
/* 175:292 */     double xp = this.xMargin + this.xViewport + (x - this.xWindow) * this.xScaleFactor;
/* 176:293 */     double yp = this.yMargin + this.yViewport + (y - this.yWindow) * this.yScaleFactor;
/* 177:294 */     double zp = (z - this.zWindow) * this.zScaleFactor;
/* 178:295 */     if (zp > 0.99D) {
/* 179:296 */       return false;
/* 180:    */     }
/* 181:298 */     double t = zp / (zp - 1.0D);
/* 182:299 */     dst_x[dst_ofs] = ((int)(0.5D + xp + t * (this.vpX - xp)));
/* 183:300 */     dst_y[dst_ofs] = ((int)(0.5D + yp + t * (this.vpY - yp)));
/* 184:301 */     return true;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public boolean worldToViewport(double[] dst_x, double[] dst_y, int dst_ofs, double x, double y, double z)
/* 188:    */   {
/* 189:306 */     double xp = this.xMargin + this.xViewport + (x - this.xWindow) * this.xScaleFactor;
/* 190:307 */     double yp = this.yMargin + this.yViewport + (y - this.yWindow) * this.yScaleFactor;
/* 191:308 */     double zp = (z - this.zWindow) * this.zScaleFactor;
/* 192:309 */     if (zp > 0.99D) {
/* 193:310 */       return false;
/* 194:    */     }
/* 195:312 */     double t = zp / (zp - 1.0D);
/* 196:313 */     dst_x[dst_ofs] = (xp + t * (this.vpX - xp));
/* 197:314 */     dst_y[dst_ofs] = (yp + t * (this.vpY - yp));
/* 198:315 */     return true;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public boolean worldToViewport(Point dst, double x, double y, double z)
/* 202:    */   {
/* 203:319 */     double xp = this.xMargin + this.xViewport + (x - this.xWindow) * this.xScaleFactor;
/* 204:320 */     double yp = this.yMargin + this.yViewport + (y - this.yWindow) * this.yScaleFactor;
/* 205:321 */     double zp = (z - this.zWindow) * this.zScaleFactor;
/* 206:322 */     if (zp > 0.99D) {
/* 207:323 */       return false;
/* 208:    */     }
/* 209:325 */     double t = zp / (zp - 1.0D);
/* 210:326 */     dst.x = ((int)(0.5D + xp + t * (this.vpX - xp)));
/* 211:327 */     dst.y = ((int)(0.5D + yp + t * (this.vpY - yp)));
/* 212:328 */     return true;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public boolean isAboveVanishingPoint(double y)
/* 216:    */   {
/* 217:332 */     return this.yMargin + this.yViewport + (y - this.yWindow) * this.yScaleFactor < this.vpY;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public boolean isRightOfVanishingPoint(double x)
/* 221:    */   {
/* 222:336 */     return this.xMargin + this.xViewport + (x - this.xWindow) * this.xScaleFactor > this.vpX;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public Point getVanishingPoint(Point buf)
/* 226:    */   {
/* 227:340 */     if (buf == null) {
/* 228:341 */       buf = new Point();
/* 229:    */     }
/* 230:343 */     buf.x = ((int)(0.5D + this.vpX));
/* 231:344 */     buf.y = ((int)(0.5D + this.vpY));
/* 232:345 */     return buf;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public Point worldToViewport(Point dst, Affine.Point src)
/* 236:    */   {
/* 237:357 */     if (dst == null) {
/* 238:358 */       dst = new Point();
/* 239:    */     }
/* 240:360 */     dst.x = worldToViewportX(src.x);
/* 241:361 */     dst.y = worldToViewportY(src.y);
/* 242:362 */     return dst;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public Rectangle worldToViewport(Rectangle dst, Rectangle2D.Double src)
/* 246:    */   {
/* 247:374 */     if (dst == null) {
/* 248:375 */       dst = new Rectangle();
/* 249:    */     }
/* 250:377 */     int x = worldToViewportX(src.x);
/* 251:378 */     int y = worldToViewportY(src.y);
/* 252:379 */     int width = (int)(0.5D + src.width * this.xScaleFactor);
/* 253:380 */     int height = (int)(0.5D + src.height * this.yScaleFactor);
/* 254:381 */     dst.setFrameFromDiagonal(x, y, x + width, y + height);
/* 255:382 */     return dst;
/* 256:    */   }
/* 257:    */   
/* 258:    */   public double viewportToWorldX(int x)
/* 259:    */   {
/* 260:392 */     return this.xWindow + (x - this.xMargin - this.xViewport) / this.xScaleFactor;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public double viewportToWorldY(int y)
/* 264:    */   {
/* 265:402 */     return this.yWindow + (y - this.yMargin - this.yViewport) / this.yScaleFactor;
/* 266:    */   }
/* 267:    */   
/* 268:    */   public Affine.Point viewportToWorld(Affine.Point dst, Point src)
/* 269:    */   {
/* 270:414 */     if (dst == null) {
/* 271:415 */       dst = new Affine.Point();
/* 272:    */     }
/* 273:417 */     dst.x = viewportToWorldX(src.x);
/* 274:418 */     dst.y = viewportToWorldY(src.y);
/* 275:419 */     return dst;
/* 276:    */   }
/* 277:    */   
/* 278:    */   public Rectangle2D.Double viewportToWorld(Rectangle2D.Double dst, Rectangle src)
/* 279:    */   {
/* 280:431 */     if (dst == null) {
/* 281:432 */       dst = new Rectangle2D.Double();
/* 282:    */     }
/* 283:434 */     double x = viewportToWorldX(src.x);
/* 284:435 */     double y = viewportToWorldY(src.y);
/* 285:436 */     double width = src.width / this.xScaleFactor;
/* 286:437 */     double height = src.height / this.yScaleFactor;
/* 287:438 */     dst.setFrameFromDiagonal(x, y, x + width, y + height);
/* 288:439 */     return dst;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public double viewportToWorldDistance(int d)
/* 292:    */   {
/* 293:449 */     return d / Math.abs(this.xScaleFactor);
/* 294:    */   }
/* 295:    */   
/* 296:    */   public int worldToViewportDistance(double d)
/* 297:    */   {
/* 298:459 */     return (int)(0.5D + d * Math.abs(this.xScaleFactor));
/* 299:    */   }
/* 300:    */   
/* 301:    */   public String toString()
/* 302:    */   {
/* 303:469 */     return "Window:   [" + this.xWindow + "," + this.yWindow + ";" + this.widthWindow + "," + this.heightWindow + "]\n" + "Viewport: [" + this.xViewport + "," + this.yViewport + ";" + this.widthViewport + "," + this.heightViewport + "]\n" + "Scale:    [" + this.xScaleFactor + "," + this.yScaleFactor + "]\n" + "Margin:   [" + this.xMargin + "," + this.yMargin + "]\n";
/* 304:    */   }
/* 305:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.ViewportTransform
 * JD-Core Version:    0.7.0.1
 */