/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ public class Homogeneous
/*   4:    */ {
/*   5:    */   public static class Point
/*   6:    */   {
/*   7: 32 */     public final float[] a = new float[4];
/*   8:    */     
/*   9:    */     public Point()
/*  10:    */     {
/*  11: 38 */       this(0.0F, 0.0F, 0.0F);
/*  12:    */     }
/*  13:    */     
/*  14:    */     public Point(float x, float y, float z)
/*  15:    */     {
/*  16: 49 */       this(x, y, z, 1.0F);
/*  17:    */     }
/*  18:    */     
/*  19:    */     public Point(float x, float y, float z, float w)
/*  20:    */     {
/*  21: 61 */       this.a[0] = x;
/*  22: 62 */       this.a[1] = y;
/*  23: 63 */       this.a[2] = z;
/*  24: 64 */       this.a[3] = w;
/*  25:    */     }
/*  26:    */     
/*  27:    */     public void set(float x, float y, float z)
/*  28:    */     {
/*  29: 75 */       this.a[0] = x;
/*  30: 76 */       this.a[1] = y;
/*  31: 77 */       this.a[2] = z;
/*  32: 78 */       this.a[3] = 1.0F;
/*  33:    */     }
/*  34:    */     
/*  35:    */     public Point scale(float r)
/*  36:    */     {
/*  37: 88 */       return new Point(r * x(), r * y(), r * z(), w());
/*  38:    */     }
/*  39:    */     
/*  40:    */     public float x()
/*  41:    */     {
/*  42: 94 */       return this.a[0];
/*  43:    */     }
/*  44:    */     
/*  45:    */     public float y()
/*  46:    */     {
/*  47: 99 */       return this.a[1];
/*  48:    */     }
/*  49:    */     
/*  50:    */     public float z()
/*  51:    */     {
/*  52:104 */       return this.a[2];
/*  53:    */     }
/*  54:    */     
/*  55:    */     public float w()
/*  56:    */     {
/*  57:109 */       return this.a[3];
/*  58:    */     }
/*  59:    */     
/*  60:    */     public void setX(float val)
/*  61:    */     {
/*  62:116 */       this.a[0] = val;
/*  63:    */     }
/*  64:    */     
/*  65:    */     public void setY(float val)
/*  66:    */     {
/*  67:122 */       this.a[1] = val;
/*  68:    */     }
/*  69:    */     
/*  70:    */     public void setZ(float val)
/*  71:    */     {
/*  72:128 */       this.a[2] = val;
/*  73:    */     }
/*  74:    */     
/*  75:    */     public void setW(float val)
/*  76:    */     {
/*  77:134 */       this.a[3] = val;
/*  78:    */     }
/*  79:    */     
/*  80:    */     public void setX(double val)
/*  81:    */     {
/*  82:141 */       this.a[0] = ((float)val);
/*  83:    */     }
/*  84:    */     
/*  85:    */     public void setY(double val)
/*  86:    */     {
/*  87:147 */       this.a[1] = ((float)val);
/*  88:    */     }
/*  89:    */     
/*  90:    */     public void setZ(double val)
/*  91:    */     {
/*  92:153 */       this.a[2] = ((float)val);
/*  93:    */     }
/*  94:    */     
/*  95:    */     public void setW(double val)
/*  96:    */     {
/*  97:159 */       this.a[3] = ((float)val);
/*  98:    */     }
/*  99:    */     
/* 100:    */     public void multiplyInto(Homogeneous.Matrix a, Point x)
/* 101:    */     {
/* 102:168 */       this.a[0] = (a.a[0] * x.a[0] + a.a[4] * x.a[1] + a.a[8] * x.a[2] + a.a[12] * x.a[3]);
/* 103:169 */       this.a[1] = (a.a[1] * x.a[0] + a.a[5] * x.a[1] + a.a[9] * x.a[2] + a.a[13] * x.a[3]);
/* 104:170 */       this.a[2] = (a.a[2] * x.a[0] + a.a[6] * x.a[1] + a.a[10] * x.a[2] + a.a[14] * x.a[3]);
/* 105:171 */       this.a[3] = (a.a[3] * x.a[0] + a.a[7] * x.a[1] + a.a[11] * x.a[2] + a.a[15] * x.a[3]);
/* 106:    */     }
/* 107:    */     
/* 108:    */     public Point normalize()
/* 109:    */     {
/* 110:    */       try
/* 111:    */       {
/* 112:176 */         double r = 1.0D / this.a[3]; int 
/* 113:177 */           tmp15_14 = 0; float[] tmp15_11 = this.a;tmp15_11[tmp15_14] = ((float)(tmp15_11[tmp15_14] * r)); int 
/* 114:178 */           tmp27_26 = 1; float[] tmp27_23 = this.a;tmp27_23[tmp27_26] = ((float)(tmp27_23[tmp27_26] * r)); int 
/* 115:179 */           tmp39_38 = 2; float[] tmp39_35 = this.a;tmp39_35[tmp39_38] = ((float)(tmp39_35[tmp39_38] * r));
/* 116:180 */         this.a[3] = 1.0F;
/* 117:    */       }
/* 118:    */       catch (Exception ex) {}
/* 119:183 */       return this;
/* 120:    */     }
/* 121:    */     
/* 122:    */     public float distance(Point other)
/* 123:    */     {
/* 124:187 */       float dx = this.a[0] - other.a[0];
/* 125:188 */       float dy = this.a[1] - other.a[1];
/* 126:189 */       float dz = this.a[2] - other.a[2];
/* 127:190 */       return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
/* 128:    */     }
/* 129:    */     
/* 130:    */     public float dot(Point other)
/* 131:    */     {
/* 132:194 */       return this.a[0] * other.a[0] + this.a[1] * other.a[1] + this.a[2] * other.a[2];
/* 133:    */     }
/* 134:    */     
/* 135:    */     public String toString()
/* 136:    */     {
/* 137:201 */       return "(" + this.a[0] + "," + this.a[1] + "," + this.a[2] + "," + this.a[3] + ")";
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public static class Matrix
/* 142:    */   {
/* 143:212 */     public final float[] a = new float[16];
/* 144:    */     
/* 145:    */     public Matrix(float a00, float a01, float a02, float a03, float a10, float a11, float a12, float a13, float a20, float a21, float a22, float a23, float a30, float a31, float a32, float a33)
/* 146:    */     {
/* 147:239 */       this.a[0] = a00;this.a[4] = a01;this.a[8] = a02;this.a[12] = a03;
/* 148:240 */       this.a[1] = a10;this.a[5] = a11;this.a[9] = a12;this.a[13] = a13;
/* 149:241 */       this.a[2] = a20;this.a[6] = a21;this.a[10] = a22;this.a[14] = a23;
/* 150:242 */       this.a[3] = a30;this.a[7] = a31;this.a[11] = a32;this.a[15] = a33;
/* 151:    */     }
/* 152:    */     
/* 153:    */     public Matrix() {}
/* 154:    */     
/* 155:    */     public Matrix(float[] a)
/* 156:    */     {
/* 157:251 */       System.arraycopy(a, 0, this.a, 0, this.a.length);
/* 158:    */     }
/* 159:    */     
/* 160:    */     public void set(Matrix m)
/* 161:    */     {
/* 162:255 */       System.arraycopy(m.a, 0, this.a, 0, 16);
/* 163:    */     }
/* 164:    */     
/* 165:    */     public float[] getRow(int i)
/* 166:    */     {
/* 167:259 */       return new float[] { this.a[(i + 0)], this.a[(i + 4)], this.a[(i + 8)], this.a[(i + 12)] };
/* 168:    */     }
/* 169:    */     
/* 170:    */     public void setZero()
/* 171:    */     {
/* 172:263 */       for (int i = 0; i < this.a.length; i++) {
/* 173:264 */         this.a[i] = 0.0F;
/* 174:    */       }
/* 175:    */     }
/* 176:    */     
/* 177:268 */     public static final float[] Ia = { 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F };
/* 178:275 */     public static Matrix I = new Matrix(Ia);
/* 179:    */     
/* 180:    */     public void setIdentity()
/* 181:    */     {
/* 182:278 */       System.arraycopy(Ia, 0, this.a, 0, Ia.length);
/* 183:    */     }
/* 184:    */     
/* 185:    */     public void setTranslation(float dx, float dy, float dz)
/* 186:    */     {
/* 187:282 */       setIdentity();
/* 188:283 */       this.a[12] = dx;
/* 189:284 */       this.a[13] = dy;
/* 190:285 */       this.a[14] = dz;
/* 191:    */     }
/* 192:    */     
/* 193:    */     public void transposeInPlace()
/* 194:    */     {
/* 195:292 */       swap(1, 4);
/* 196:293 */       swap(2, 8);
/* 197:294 */       swap(3, 12);
/* 198:295 */       swap(6, 9);
/* 199:296 */       swap(7, 13);
/* 200:297 */       swap(11, 14);
/* 201:    */     }
/* 202:    */     
/* 203:    */     private void swap(int i, int j)
/* 204:    */     {
/* 205:301 */       float t = this.a[i];
/* 206:302 */       this.a[i] = this.a[j];
/* 207:303 */       this.a[j] = t;
/* 208:    */     }
/* 209:    */     
/* 210:    */     public void multiplyInto(Matrix x, Matrix y)
/* 211:    */     {
/* 212:314 */       for (int j = 0; j < 16; j += 4) {
/* 213:315 */         for (int i = 0; i < 4; i++) {
/* 214:316 */           this.a[(j + i)] = (x.a[(i + 0)] * y.a[(j + 0)] + x.a[(i + 4)] * y.a[(j + 1)] + x.a[(i + 8)] * y.a[(j + 2)] + x.a[(i + 12)] * y.a[(j + 3)]);
/* 215:    */         }
/* 216:    */       }
/* 217:    */     }
/* 218:    */     
/* 219:    */     void invertInto(Matrix x, float epsilon)
/* 220:    */     {
/* 221:325 */       float a0 = x.a[0] * x.a[5] - x.a[4] * x.a[1];
/* 222:326 */       float a1 = x.a[0] * x.a[9] - x.a[8] * x.a[1];
/* 223:327 */       float a2 = x.a[0] * x.a[13] - x.a[12] * x.a[1];
/* 224:328 */       float a3 = x.a[4] * x.a[9] - x.a[8] * x.a[5];
/* 225:329 */       float a4 = x.a[4] * x.a[13] - x.a[12] * x.a[5];
/* 226:330 */       float a5 = x.a[8] * x.a[13] - x.a[12] * x.a[9];
/* 227:331 */       float b0 = x.a[2] * x.a[7] - x.a[6] * x.a[3];
/* 228:332 */       float b1 = x.a[2] * x.a[11] - x.a[10] * x.a[3];
/* 229:333 */       float b2 = x.a[2] * x.a[15] - x.a[14] * x.a[3];
/* 230:334 */       float b3 = x.a[6] * x.a[11] - x.a[10] * x.a[7];
/* 231:335 */       float b4 = x.a[6] * x.a[15] - x.a[14] * x.a[7];
/* 232:336 */       float b5 = x.a[10] * x.a[15] - x.a[14] * x.a[11];
/* 233:    */       
/* 234:338 */       float det = a0 * b5 - a1 * b4 + a2 * b3 + a3 * b2 - a4 * b1 + a5 * b0;
/* 235:339 */       if (Math.abs(det) > epsilon)
/* 236:    */       {
/* 237:340 */         this.a[0] = (x.a[5] * b5 - x.a[9] * b4 + x.a[13] * b3);
/* 238:341 */         this.a[1] = (-x.a[1] * b5 + x.a[9] * b2 - x.a[13] * b1);
/* 239:342 */         this.a[2] = (x.a[1] * b4 - x.a[5] * b2 + x.a[13] * b0);
/* 240:343 */         this.a[3] = (-x.a[1] * b3 + x.a[5] * b1 - x.a[9] * b0);
/* 241:344 */         this.a[4] = (-x.a[4] * b5 + x.a[8] * b4 - x.a[12] * b3);
/* 242:345 */         this.a[5] = (x.a[0] * b5 - x.a[8] * b2 + x.a[12] * b1);
/* 243:346 */         this.a[6] = (-x.a[0] * b4 + x.a[4] * b2 - x.a[12] * b0);
/* 244:347 */         this.a[7] = (x.a[0] * b3 - x.a[4] * b1 + x.a[8] * b0);
/* 245:348 */         this.a[8] = (x.a[7] * a5 - x.a[11] * a4 + x.a[15] * a3);
/* 246:349 */         this.a[9] = (-x.a[3] * a5 + x.a[11] * a2 - x.a[15] * a1);
/* 247:350 */         this.a[10] = (x.a[3] * a4 - x.a[7] * a2 + x.a[15] * a0);
/* 248:351 */         this.a[11] = (-x.a[3] * a3 + x.a[7] * a1 - x.a[11] * a0);
/* 249:352 */         this.a[12] = (-x.a[6] * a5 + x.a[10] * a4 - x.a[14] * a3);
/* 250:353 */         this.a[13] = (x.a[2] * a5 - x.a[10] * a2 + x.a[14] * a1);
/* 251:354 */         this.a[14] = (-x.a[2] * a4 + x.a[6] * a2 - x.a[14] * a0);
/* 252:355 */         this.a[15] = (x.a[2] * a3 - x.a[6] * a1 + x.a[10] * a0);
/* 253:    */         
/* 254:357 */         float invDet = 1.0F / det;
/* 255:358 */         for (int i = 0; i < this.a.length; i++) {
/* 256:359 */           this.a[i] *= invDet;
/* 257:    */         }
/* 258:    */       }
/* 259:    */       else
/* 260:    */       {
/* 261:363 */         setZero();
/* 262:    */       }
/* 263:    */     }
/* 264:    */     
/* 265:    */     public String toString()
/* 266:    */     {
/* 267:368 */       return "[[" + this.a[0] + "," + this.a[4] + "," + this.a[8] + "," + this.a[12] + ")]\n" + "[" + this.a[1] + "," + this.a[5] + "," + this.a[9] + "," + this.a[13] + ")]\n" + "[" + this.a[2] + "," + this.a[6] + "," + this.a[10] + "," + this.a[14] + ")]\n" + "[" + this.a[3] + "," + this.a[7] + "," + this.a[11] + "," + this.a[15] + ")]]";
/* 268:    */     }
/* 269:    */   }
/* 270:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Homogeneous
 * JD-Core Version:    0.7.0.1
 */