/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Graphics;
/*   5:    */ import java.awt.GraphicsConfiguration;
/*   6:    */ import java.awt.GraphicsDevice;
/*   7:    */ import java.awt.GraphicsEnvironment;
/*   8:    */ import java.awt.Rectangle;
/*   9:    */ import java.io.File;
/*  10:    */ import java.io.FileInputStream;
/*  11:    */ import java.io.IOException;
/*  12:    */ import java.io.InputStream;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ 
/*  15:    */ public class Utility
/*  16:    */ {
/*  17:    */   public static final double small = 1.0E-006D;
/*  18:    */   public static final double smallSq = 1.0E-012D;
/*  19:    */   public static final int arrowHeadLength = 8;
/*  20:    */   public static final int arrowHalfHeadWidth = 3;
/*  21:    */   
/*  22:    */   public static float sqr(float x)
/*  23:    */   {
/*  24: 57 */     return x * x;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static double sqr(double x)
/*  28:    */   {
/*  29: 67 */     return x * x;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static double p4(double x)
/*  33:    */   {
/*  34: 77 */     return sqr(sqr(x));
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static boolean areEqual(Object a, Object b)
/*  38:    */   {
/*  39: 88 */     return (a == b) || ((a != null) && (a.equals(b)));
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static byte[] getBytesFromFile(File file)
/*  43:    */     throws IOException
/*  44:    */   {
/*  45: 99 */     InputStream is = new FileInputStream(file);
/*  46:100 */     byte[] buf = new byte[16];
/*  47:101 */     int offset = 0;
/*  48:    */     for (;;)
/*  49:    */     {
/*  50:103 */       int nRead = is.read(buf, offset, buf.length - offset);
/*  51:104 */       if (nRead < 0)
/*  52:    */       {
/*  53:105 */         byte[] rtn = new byte[offset];
/*  54:106 */         System.arraycopy(buf, 0, rtn, 0, offset);
/*  55:107 */         is.close();
/*  56:108 */         return rtn;
/*  57:    */       }
/*  58:110 */       offset += nRead;
/*  59:111 */       if (offset >= buf.length)
/*  60:    */       {
/*  61:112 */         byte[] newBuf = new byte[2 * buf.length];
/*  62:113 */         System.arraycopy(buf, 0, newBuf, 0, buf.length);
/*  63:114 */         buf = newBuf;
/*  64:    */       }
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static void drawArrowhead(Graphics g, int x0, int y0, int x1, int y1, int halfWidth, int length)
/*  69:    */   {
/*  70:131 */     float dx = x1 - x0;
/*  71:132 */     float dy = y1 - y0;
/*  72:133 */     if ((dx == 0.0F) && (dy == 0.0F)) {
/*  73:134 */       return;
/*  74:    */     }
/*  75:135 */     float len = (float)Math.sqrt(dx * dx + dy * dy);
/*  76:136 */     float ux = dx / len;
/*  77:137 */     float uy = dy / len;
/*  78:138 */     float bx = x1 - ux * length;
/*  79:139 */     float by = y1 - uy * length;
/*  80:140 */     float dhx = -uy * halfWidth;
/*  81:141 */     float dhy = ux * halfWidth;
/*  82:142 */     g.drawLine(x1, y1, Math.round(bx + dhx), Math.round(by + dhy));
/*  83:143 */     g.drawLine(x1, y1, Math.round(bx - dhx), Math.round(by - dhy));
/*  84:    */   }
/*  85:    */   
/*  86:    */   private static void drawArrowhead(Graphics g, int x0, int y0, int x1, int y1)
/*  87:    */   {
/*  88:147 */     drawArrowhead(g, x0, y0, x1, y1, 3, 8);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static void drawArrow(Graphics g, int x0, int y0, int x1, int y1)
/*  92:    */   {
/*  93:160 */     g.drawLine(x0, y0, x1, y1);
/*  94:161 */     drawArrowhead(g, x0, y0, x1, y1);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static void drawDoubleArrow(Graphics g, int x0, int y0, int x1, int y1)
/*  98:    */   {
/*  99:174 */     g.drawLine(x0, y0, x1, y1);
/* 100:175 */     drawArrowhead(g, x0, y0, x1, y1);
/* 101:176 */     drawArrowhead(g, x1, y1, x0, y0);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static void drawDoubleArrow(Graphics g, int x0, int y0, int x1, int y1, int halfWidth, int length)
/* 105:    */   {
/* 106:191 */     g.drawLine(x0, y0, x1, y1);
/* 107:192 */     drawArrowhead(g, x0, y0, x1, y1, halfWidth, length);
/* 108:193 */     drawArrowhead(g, x1, y1, x0, y0, halfWidth, length);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static Affine.Point intersection(Affine.Point p1, Affine.Point p2, Affine.Point p3, Affine.Point p4)
/* 112:    */   {
/* 113:206 */     return intersection(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static void reverse(Object aObj, int iLo, int len)
/* 117:    */   {
/* 118:217 */     Object[] a = (Object[])aObj;
/* 119:218 */     int iHi = len - 1;
/* 120:219 */     while (iLo < iHi)
/* 121:    */     {
/* 122:220 */       Object tmp = a[iLo];
/* 123:221 */       a[iLo] = a[iHi];
/* 124:222 */       a[iHi] = tmp;
/* 125:223 */       iLo++;
/* 126:224 */       iHi--;
/* 127:    */     }
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static void reverse(Object aObj)
/* 131:    */   {
/* 132:234 */     reverse(aObj, 0, ((Object[])aObj).length);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static Affine.Point intersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
/* 136:    */   {
/* 137:254 */     double dx21 = x2 - x1;
/* 138:255 */     double dy21 = y2 - y1;
/* 139:256 */     double dx43 = x4 - x3;
/* 140:257 */     double dy43 = y4 - y3;
/* 141:258 */     double d = dy43 * dx21 - dx43 * dy21;
/* 142:259 */     if (d == 0.0D) {
/* 143:260 */       return null;
/* 144:    */     }
/* 145:262 */     double dx13 = x1 - x3;
/* 146:263 */     double dy13 = y1 - y3;
/* 147:264 */     double uan = dx43 * dy13 - dy43 * dx13;
/* 148:265 */     double ubn = dx21 * dy13 - dy21 * dx13;
/* 149:266 */     double ua = uan / d;
/* 150:267 */     double ub = ubn / d;
/* 151:268 */     return (0.0D <= ua) && (ua <= 1.0D) && (0.0D <= ub) && (ub <= 1.0D) ? new Affine.Point(x1 + ua * dx21, y1 + ua * dy21) : null;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static int getIndexOfGreatestNotGreaterThan(float x, float[] a)
/* 155:    */   {
/* 156:278 */     int lo = 0;
/* 157:279 */     int hi = a.length - 1;
/* 158:280 */     while (lo <= hi)
/* 159:    */     {
/* 160:281 */       int mid = (lo + hi) / 2;
/* 161:282 */       if (a[mid] < x)
/* 162:    */       {
/* 163:283 */         if ((mid == a.length - 1) || (x < a[(mid + 1)])) {
/* 164:284 */           return mid;
/* 165:    */         }
/* 166:286 */         lo = mid + 1;
/* 167:    */       }
/* 168:288 */       else if (x < a[mid])
/* 169:    */       {
/* 170:289 */         hi = mid - 1;
/* 171:    */       }
/* 172:    */       else
/* 173:    */       {
/* 174:292 */         return mid;
/* 175:    */       }
/* 176:    */     }
/* 177:295 */     return -1;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public static int[] mapToInt(String[] s)
/* 181:    */   {
/* 182:305 */     int[] rtn = new int[s.length];
/* 183:306 */     for (int i = 0; i < s.length; i++) {
/* 184:307 */       rtn[i] = Integer.parseInt(s[i]);
/* 185:    */     }
/* 186:309 */     return rtn;
/* 187:    */   }
/* 188:    */   
/* 189:312 */   private static float[] rotationMatrix = { 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F };
/* 190:    */   private static Dimension maxScreenSize;
/* 191:    */   
/* 192:    */   public static float[] rotateAboutZ(double cos, double sin)
/* 193:    */   {
/* 194:328 */     float tmp10_9 = ((float)cos);rotationMatrix[5] = tmp10_9;rotationMatrix[0] = tmp10_9;
/* 195:329 */     rotationMatrix[1] = ((float)tmp10_9);
/* 196:330 */     rotationMatrix[4] = (-rotationMatrix[1]);
/* 197:331 */     return rotationMatrix;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public static double clamp(double x, double min, double max)
/* 201:    */   {
/* 202:345 */     return Math.max(min, Math.min(max, x));
/* 203:    */   }
/* 204:    */   
/* 205:    */   public static float clamp(float x, float min, float max)
/* 206:    */   {
/* 207:348 */     return Math.max(min, Math.min(max, x));
/* 208:    */   }
/* 209:    */   
/* 210:    */   public static Dimension getMaxScreenSize()
/* 211:    */   {
/* 212:361 */     if (maxScreenSize == null)
/* 213:    */     {
/* 214:362 */       maxScreenSize = new Dimension(0, 0);
/* 215:363 */       GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 216:364 */       GraphicsDevice[] devices = ge.getScreenDevices();
/* 217:365 */       for (int id = 0; id < devices.length; id++)
/* 218:    */       {
/* 219:366 */         GraphicsConfiguration[] gcs = devices[id].getConfigurations();
/* 220:367 */         for (int ic = 0; ic < gcs.length; ic++)
/* 221:    */         {
/* 222:368 */           Rectangle bounds = gcs[ic].getBounds();
/* 223:369 */           if (bounds.width > maxScreenSize.width) {
/* 224:370 */             maxScreenSize.width = bounds.width;
/* 225:    */           }
/* 226:372 */           if (bounds.height > maxScreenSize.height) {
/* 227:373 */             maxScreenSize.height = bounds.height;
/* 228:    */           }
/* 229:    */         }
/* 230:    */       }
/* 231:    */     }
/* 232:378 */     return maxScreenSize;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public static void main(String[] args)
/* 236:    */   {
/* 237:392 */     System.out.println("Max screen size=" + getMaxScreenSize());
/* 238:393 */     System.out.println(intersection(0.0D, 0.0D, 5.0D, 5.0D, 5.0D, 0.0D, 0.0D, 5.0D));
/* 239:394 */     System.out.println(intersection(1.0D, 3.0D, 9.0D, 3.0D, 0.0D, 1.0D, 2.0D, 1.0D));
/* 240:395 */     System.out.println(intersection(1.0D, 5.0D, 6.0D, 8.0D, 0.5D, 3.0D, 6.0D, 4.0D));
/* 241:396 */     System.out.println(intersection(1.0D, 1.0D, 3.0D, 8.0D, 0.5D, 2.0D, 4.0D, 7.0D));
/* 242:397 */     System.out.println(intersection(1.0D, 2.0D, 3.0D, 6.0D, 2.0D, 4.0D, 4.0D, 8.0D));
/* 243:398 */     System.out.println(intersection(3.5D, 9.0D, 3.5D, 0.5D, 3.0D, 1.0D, 9.0D, 1.0D));
/* 244:399 */     System.out.println(intersection(2.0D, 3.0D, 7.0D, 9.0D, 1.0D, 2.0D, 5.0D, 7.0D));
/* 245:    */   }
/* 246:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Utility
 * JD-Core Version:    0.7.0.1
 */