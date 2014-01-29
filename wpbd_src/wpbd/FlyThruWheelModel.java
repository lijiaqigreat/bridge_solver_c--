/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import javax.media.opengl.GL2;
/*   4:    */ 
/*   5:    */ class FlyThruWheelModel
/*   6:    */ {
/*   7:    */   private static final int segCount = 24;
/*   8:    */   private static final float tireWidth = 0.2F;
/*   9:    */   private static final float dualSeparation = 0.03F;
/*  10:    */   public static final float tireRadius = 0.5F;
/*  11:    */   private static final float tireInnerRadius = 0.3F;
/*  12:    */   private static final float rimInnerRadius = 0.25F;
/*  13:    */   private static final float spokeInnerRadius = 0.2F;
/*  14:    */   private static final float holeLocation = 0.6F;
/*  15:    */   private static final float holeRadius = 0.23F;
/*  16:    */   private static final float holeRadialSize = 0.02F;
/*  17:    */   private static final float holeInnerRadius = 0.22F;
/*  18:    */   private static final float holeOuterRadius = 0.24F;
/*  19:    */   private static final float sidewallBulge = 0.4F;
/*  20:    */   private static final float rimBulge = 0.4F;
/*  21:    */   private static final float innerRimDepthOffset = -0.03F;
/*  22:    */   private static final float innerSpokeDepthOffset = 0.04F;
/*  23:    */   private static final float hubApexDepthOffset = 0.05F;
/*  24:    */   private static final float spokeRadialWidth = 0.05F;
/*  25: 59 */   private static final float lengthHubNormal = (float)Math.sqrt(0.00409999955445528D);
/*  26: 60 */   private static final float rHubNormal = 0.04F / lengthHubNormal;
/*  27: 61 */   private static final float zHubNormal = 0.05F / lengthHubNormal;
/*  28:    */   private static final float hubSlope = 1.4F;
/*  29:    */   private static final float holeVisibilityOffset = 0.01F;
/*  30:    */   private static final float holeInnerOffset = 0.022F;
/*  31:    */   private static final float holeOuterOffset = -0.006000012F;
/*  32:    */   private static final int holeWidthInSegs = 3;
/*  33:    */   private static final int holeSpacingInSegs = 6;
/*  34: 75 */   private static final float sidewallBulgeComplement = (float)Math.sqrt(0.8399999886751175D);
/*  35: 76 */   private static final float rimBulgeComplement = (float)Math.sqrt(0.8399999886751175D);
/*  36: 77 */   private static float[] tireMaterial = { 0.3F, 0.3F, 0.3F, 1.0F };
/*  37: 78 */   private static float[] rimMaterial = { 0.5F, 0.5F, 0.5F, 1.0F };
/*  38: 79 */   private static float[] hubMaterial = { 1.0F, 0.549F, 0.0F, 1.0F };
/*  39: 80 */   private static float[] capMaterial = { 0.4F, 0.4F, 0.4F, 1.0F };
/*  40: 81 */   private static float[] flangeMaterial = { 0.5F, 0.2745F, 0.0F, 1.0F };
/*  41: 82 */   private static float[] holeMaterial = { 1.0F, 0.549F, 0.0F, 1.0F };
/*  42: 85 */   private static float[] xCircle = new float[25];
/*  43: 86 */   private static float[] yCircle = new float[25];
/*  44:    */   
/*  45:    */   static
/*  46:    */   {
/*  47: 88 */     for (int i = 0; i <= 24; i++)
/*  48:    */     {
/*  49: 89 */       double theta = 6.283185307179586D * i / 24.0D;
/*  50: 90 */       xCircle[i] = ((float)Math.cos(theta));
/*  51: 91 */       yCircle[i] = ((float)Math.sin(theta));
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setAlpha(float alpha)
/*  56:    */   {
/*  57:101 */     float tmp39_38 = (hubMaterial[3] = capMaterial[3] = capMaterial[3] = flangeMaterial[3] = holeMaterial[3] = alpha);rimMaterial[3] = tmp39_38;tireMaterial[3] = tmp39_38;
/*  58:    */   }
/*  59:    */   
/*  60:    */   private void drawTire(GL2 gl)
/*  61:    */   {
/*  62:113 */     gl.glColor4fv(tireMaterial, 0);
/*  63:114 */     gl.glBegin(8);
/*  64:115 */     for (int i = 0; i <= 24; i++)
/*  65:    */     {
/*  66:116 */       gl.glNormal3f(xCircle[i], yCircle[i], 0.0F);
/*  67:117 */       gl.glVertex3f(0.5F * xCircle[i], 0.5F * yCircle[i], 0.2F);
/*  68:118 */       gl.glVertex3f(0.5F * xCircle[i], 0.5F * yCircle[i], 0.0F);
/*  69:    */     }
/*  70:120 */     gl.glEnd();
/*  71:    */     
/*  72:122 */     gl.glBegin(8);
/*  73:123 */     for (int i = 0; i <= 24; i++)
/*  74:    */     {
/*  75:124 */       gl.glNormal3f(-0.4F * xCircle[i], -0.4F * yCircle[i], sidewallBulgeComplement);
/*  76:125 */       gl.glVertex3f(0.3F * xCircle[i], 0.3F * yCircle[i], 0.2F);
/*  77:126 */       gl.glNormal3f(0.4F * xCircle[i], 0.4F * yCircle[i], sidewallBulgeComplement);
/*  78:127 */       gl.glVertex3f(0.5F * xCircle[i], 0.5F * yCircle[i], 0.2F);
/*  79:    */     }
/*  80:129 */     gl.glEnd();
/*  81:    */     
/*  82:131 */     gl.glBegin(8);
/*  83:132 */     for (int i = 0; i <= 24; i++)
/*  84:    */     {
/*  85:133 */       gl.glNormal3f(0.4F * xCircle[i], 0.4F * yCircle[i], -sidewallBulgeComplement);
/*  86:134 */       gl.glVertex3f(0.5F * xCircle[i], 0.5F * yCircle[i], 0.0F);
/*  87:135 */       gl.glNormal3f(-0.4F * xCircle[i], -0.4F * yCircle[i], -sidewallBulgeComplement);
/*  88:136 */       gl.glVertex3f(0.3F * xCircle[i], 0.3F * yCircle[i], 0.0F);
/*  89:    */     }
/*  90:138 */     gl.glEnd();
/*  91:    */     
/*  92:140 */     gl.glColor4fv(flangeMaterial, 0);
/*  93:141 */     gl.glBegin(8);
/*  94:142 */     for (int i = 0; i <= 24; i++)
/*  95:    */     {
/*  96:143 */       gl.glNormal3f(-xCircle[i], -yCircle[i], 0.0F);
/*  97:144 */       gl.glVertex3f(0.3F * xCircle[i], 0.3F * yCircle[i], 0.0F);
/*  98:145 */       gl.glVertex3f(0.3F * xCircle[i], 0.3F * yCircle[i], 0.2F);
/*  99:    */     }
/* 100:147 */     gl.glEnd();
/* 101:    */   }
/* 102:    */   
/* 103:    */   private void drawWheel(GL2 gl)
/* 104:    */   {
/* 105:157 */     gl.glColor4fv(rimMaterial, 0);
/* 106:158 */     gl.glBegin(8);
/* 107:159 */     for (int i = 0; i <= 24; i++)
/* 108:    */     {
/* 109:160 */       gl.glNormal3f(-0.4F * xCircle[i], -0.4F * yCircle[i], rimBulgeComplement);
/* 110:161 */       gl.glVertex3f(0.25F * xCircle[i], 0.25F * yCircle[i], -0.03F);
/* 111:162 */       gl.glNormal3f(0.4F * xCircle[i], 0.4F * yCircle[i], rimBulgeComplement);
/* 112:163 */       gl.glVertex3f(0.3F * xCircle[i], 0.3F * yCircle[i], 0.0F);
/* 113:    */     }
/* 114:165 */     gl.glEnd();
/* 115:    */     
/* 116:    */ 
/* 117:168 */     gl.glBegin(9);
/* 118:169 */     gl.glNormal3f(0.0F, 0.0F, -1.0F);
/* 119:170 */     for (int i = 24; i >= 0; i--) {
/* 120:171 */       gl.glVertex3f(0.3F * xCircle[i], 0.3F * yCircle[i], -0.04F);
/* 121:    */     }
/* 122:173 */     gl.glEnd();
/* 123:    */     
/* 124:175 */     gl.glColor4fv(hubMaterial, 0);
/* 125:176 */     gl.glBegin(8);
/* 126:177 */     for (int i = 0; i <= 24; i++)
/* 127:    */     {
/* 128:178 */       gl.glNormal3f(rHubNormal * xCircle[i], rHubNormal * yCircle[i], zHubNormal);
/* 129:179 */       gl.glVertex3f(0.2F * xCircle[i], 0.2F * yCircle[i], 0.04F);
/* 130:180 */       gl.glVertex3f(0.25F * xCircle[i], 0.25F * yCircle[i], -0.03F);
/* 131:    */     }
/* 132:182 */     gl.glEnd();
/* 133:    */     
/* 134:184 */     gl.glColor4fv(capMaterial, 0);
/* 135:185 */     gl.glBegin(6);
/* 136:186 */     gl.glNormal3f(0.0F, 0.0F, 1.0F);
/* 137:187 */     gl.glVertex3f(0.0F, 0.0F, 0.05F);
/* 138:188 */     for (int i = 0; i <= 24; i++)
/* 139:    */     {
/* 140:189 */       gl.glNormal3f(rHubNormal * xCircle[i], rHubNormal * yCircle[i], zHubNormal);
/* 141:190 */       gl.glVertex3f(0.2F * xCircle[i], 0.2F * yCircle[i], 0.04F);
/* 142:    */     }
/* 143:192 */     gl.glEnd();
/* 144:    */     
/* 145:194 */     gl.glColor4fv(holeMaterial, 0);
/* 146:195 */     for (int i = 0; i <= 24; i += 6)
/* 147:    */     {
/* 148:196 */       gl.glBegin(8);
/* 149:197 */       gl.glNormal3f(0.0F, 0.0F, 0.0F);
/* 150:198 */       for (int j = 0; j <= 3; j++)
/* 151:    */       {
/* 152:199 */         int k = (i + j) % 24;
/* 153:200 */         gl.glVertex3f(0.22F * xCircle[k], 0.22F * yCircle[k], 0.022F);
/* 154:201 */         gl.glVertex3f(0.24F * xCircle[k], 0.24F * yCircle[k], -0.006000012F);
/* 155:    */       }
/* 156:203 */       gl.glEnd();
/* 157:    */     }
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void displaySingle(GL2 gl)
/* 161:    */   {
/* 162:213 */     drawTire(gl);
/* 163:214 */     gl.glPushMatrix();
/* 164:215 */     gl.glTranslatef(0.0F, 0.0F, 0.2F);
/* 165:216 */     drawWheel(gl);
/* 166:217 */     gl.glPopMatrix();
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void displayDual(GL2 gl)
/* 170:    */   {
/* 171:227 */     gl.glColor4fv(hubMaterial, 0);
/* 172:228 */     gl.glBegin(8);
/* 173:229 */     for (int i = 0; i <= 24; i++)
/* 174:    */     {
/* 175:230 */       gl.glNormal3f(xCircle[i], yCircle[i], 0.0F);
/* 176:231 */       gl.glVertex3f(0.3F * xCircle[i], 0.3F * yCircle[i], 0.03F);
/* 177:232 */       gl.glVertex3f(0.3F * xCircle[i], 0.3F * yCircle[i], -0.03F);
/* 178:    */     }
/* 179:234 */     gl.glEnd();
/* 180:    */     
/* 181:236 */     gl.glPushMatrix();
/* 182:237 */     gl.glTranslatef(0.0F, 0.0F, 0.03F);
/* 183:238 */     drawWheel(gl);
/* 184:239 */     drawTire(gl);
/* 185:240 */     gl.glPopMatrix();
/* 186:    */     
/* 187:242 */     gl.glPushMatrix();
/* 188:243 */     gl.glTranslatef(0.0F, 0.0F, -0.23F);
/* 189:244 */     drawTire(gl);
/* 190:245 */     gl.glPopMatrix();
/* 191:    */   }
/* 192:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FlyThruWheelModel
 * JD-Core Version:    0.7.0.1
 */