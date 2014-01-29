/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.geom.Point2D.Double;
/*   4:    */ 
/*   5:    */ public abstract class Affine
/*   6:    */ {
/*   7:    */   public static class Point
/*   8:    */     extends Point2D.Double
/*   9:    */   {
/*  10:    */     public Point()
/*  11:    */     {
/*  12: 34 */       this.x = (this.y = 0.0D);
/*  13:    */     }
/*  14:    */     
/*  15:    */     public Point(double x, double y)
/*  16:    */     {
/*  17: 44 */       super(y);
/*  18:    */     }
/*  19:    */     
/*  20:    */     public Point(Point p)
/*  21:    */     {
/*  22: 53 */       this(p.x, p.y);
/*  23:    */     }
/*  24:    */     
/*  25:    */     public Point plus(Affine.Vector v)
/*  26:    */     {
/*  27: 63 */       return new Point(this.x + v.x, this.y + v.y);
/*  28:    */     }
/*  29:    */     
/*  30:    */     public Point plus(double dx, double dy)
/*  31:    */     {
/*  32: 74 */       return new Point(this.x + dx, this.y + dy);
/*  33:    */     }
/*  34:    */     
/*  35:    */     public Affine.Vector minus(Point p)
/*  36:    */     {
/*  37: 84 */       return new Affine.Vector(this.x - p.x, this.y - p.y);
/*  38:    */     }
/*  39:    */     
/*  40:    */     public Point minus(Affine.Vector v)
/*  41:    */     {
/*  42: 94 */       return new Point(this.x - v.x, this.y - v.y);
/*  43:    */     }
/*  44:    */     
/*  45:    */     public Affine.Vector position()
/*  46:    */     {
/*  47:101 */       return new Affine.Vector(this.x, this.y);
/*  48:    */     }
/*  49:    */     
/*  50:    */     public Point offsetInto(Point a, Affine.Vector v, double t)
/*  51:    */     {
/*  52:105 */       a.x += t * v.x;
/*  53:106 */       a.y += t * v.y;
/*  54:107 */       return this;
/*  55:    */     }
/*  56:    */     
/*  57:    */     public Point orthoOffsetInto(Point a, Affine.Vector v, double t)
/*  58:    */     {
/*  59:111 */       a.x -= t * v.y;
/*  60:112 */       a.y += t * v.x;
/*  61:113 */       return this;
/*  62:    */     }
/*  63:    */     
/*  64:    */     public double distanceToSegment(Point a, Point b)
/*  65:    */     {
/*  66:124 */       Affine.Vector v = b.minus(a);
/*  67:125 */       double vDotV = v.dot(v);
/*  68:126 */       if (vDotV < 1.0E-012D) {
/*  69:127 */         return distance(a);
/*  70:    */       }
/*  71:129 */       Affine.Vector u = minus(a);
/*  72:130 */       if (u.dot(v) <= 0.0D) {
/*  73:131 */         return u.length();
/*  74:    */       }
/*  75:133 */       Affine.Vector w = minus(b);
/*  76:134 */       if (w.dot(v) >= 0.0D) {
/*  77:135 */         return w.length();
/*  78:    */       }
/*  79:137 */       return Math.abs(u.dot(v.perp())) / Math.sqrt(vDotV);
/*  80:    */     }
/*  81:    */     
/*  82:    */     public Point interpolateInto(Point a, Point b, double t)
/*  83:    */     {
/*  84:141 */       this.x = ((1.0D - t) * a.x + t * b.x);
/*  85:142 */       this.y = ((1.0D - t) * a.y + t * b.y);
/*  86:143 */       return this;
/*  87:    */     }
/*  88:    */     
/*  89:    */     public boolean onSegment(Point a, Point b)
/*  90:    */     {
/*  91:153 */       return (!equals(a)) && (!equals(b)) && (((a.x <= this.x) && (this.x <= b.x)) || ((b.x <= this.x) && (this.x <= a.x) && (((a.y <= this.y) && (this.y <= b.y)) || ((b.y <= this.y) && (this.y <= a.y) && (Math.abs((this.x - a.x) * (b.y - a.y) - (this.y - a.y) * (b.x - a.x)) < 1.0E-012D)))));
/*  92:    */     }
/*  93:    */     
/*  94:    */     public String toString()
/*  95:    */     {
/*  96:166 */       return "(" + this.x + ", " + this.y + ")";
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static class Vector
/* 101:    */     extends Point2D.Double
/* 102:    */   {
/* 103:    */     public Vector()
/* 104:    */     {
/* 105:179 */       this.x = (this.y = 0.0D);
/* 106:    */     }
/* 107:    */     
/* 108:    */     public Vector(double x, double y)
/* 109:    */     {
/* 110:189 */       super(y);
/* 111:    */     }
/* 112:    */     
/* 113:    */     public Vector plus(Vector v)
/* 114:    */     {
/* 115:199 */       return new Vector(this.x + v.x, this.y + v.y);
/* 116:    */     }
/* 117:    */     
/* 118:    */     public Affine.Point toPoint()
/* 119:    */     {
/* 120:208 */       return new Affine.Point(this.x, this.y);
/* 121:    */     }
/* 122:    */     
/* 123:    */     public Vector minus(Vector v)
/* 124:    */     {
/* 125:218 */       return new Vector(this.x - v.x, this.y - v.y);
/* 126:    */     }
/* 127:    */     
/* 128:    */     public Vector times(double a)
/* 129:    */     {
/* 130:228 */       return new Vector(a * this.x, a * this.y);
/* 131:    */     }
/* 132:    */     
/* 133:    */     public Vector div(double a)
/* 134:    */     {
/* 135:238 */       return new Vector(this.x / a, this.y / a);
/* 136:    */     }
/* 137:    */     
/* 138:    */     public Vector perp()
/* 139:    */     {
/* 140:247 */       return new Vector(-this.y, this.x);
/* 141:    */     }
/* 142:    */     
/* 143:    */     public Vector minus()
/* 144:    */     {
/* 145:256 */       return new Vector(-this.x, -this.y);
/* 146:    */     }
/* 147:    */     
/* 148:    */     public double dot(Vector v)
/* 149:    */     {
/* 150:266 */       return this.x * v.x + this.y * v.y;
/* 151:    */     }
/* 152:    */     
/* 153:    */     public double cross(Vector v)
/* 154:    */     {
/* 155:278 */       return this.x * v.y - this.y * v.x;
/* 156:    */     }
/* 157:    */     
/* 158:    */     public double lengthSq()
/* 159:    */     {
/* 160:287 */       return dot(this);
/* 161:    */     }
/* 162:    */     
/* 163:    */     public double length()
/* 164:    */     {
/* 165:296 */       return Math.sqrt(lengthSq());
/* 166:    */     }
/* 167:    */     
/* 168:    */     public Vector unit(double c)
/* 169:    */     {
/* 170:305 */       return times(c / length());
/* 171:    */     }
/* 172:    */     
/* 173:    */     public Vector diffInto(Affine.Point a, Affine.Point b)
/* 174:    */     {
/* 175:309 */       this.x = (a.x - b.x);
/* 176:310 */       this.y = (a.y - b.y);
/* 177:311 */       return this;
/* 178:    */     }
/* 179:    */     
/* 180:    */     public Vector scaleInPlace(double r)
/* 181:    */     {
/* 182:316 */       this.x *= r;
/* 183:317 */       this.y *= r;
/* 184:318 */       return this;
/* 185:    */     }
/* 186:    */     
/* 187:    */     public String toString()
/* 188:    */     {
/* 189:323 */       return "[" + this.x + ", " + this.y + "]";
/* 190:    */     }
/* 191:    */   }
/* 192:    */   
/* 193:    */   public static double getPolygonArea(Point[] p, int n)
/* 194:    */   {
/* 195:335 */     double detSum = 0.0D;
/* 196:336 */     int j = n - 1;
/* 197:337 */     for (int i = 0; i < n; j = i++) {
/* 198:338 */       detSum += p[j].x * p[i].y - p[j].y * p[i].x;
/* 199:    */     }
/* 200:340 */     return 0.5D * detSum;
/* 201:    */   }
/* 202:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Affine
 * JD-Core Version:    0.7.0.1
 */