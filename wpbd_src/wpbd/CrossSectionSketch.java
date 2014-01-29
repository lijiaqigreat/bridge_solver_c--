/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Font;
/*   5:    */ import java.awt.Graphics;
/*   6:    */ import java.awt.Graphics2D;
/*   7:    */ import java.awt.font.FontRenderContext;
/*   8:    */ import java.awt.font.LineMetrics;
/*   9:    */ import java.awt.geom.Rectangle2D;
/*  10:    */ import javax.swing.JLabel;
/*  11:    */ import org.jdesktop.application.ResourceMap;
/*  12:    */ 
/*  13:    */ public class CrossSectionSketch
/*  14:    */   extends JLabel
/*  15:    */ {
/*  16: 33 */   private int widthDimension = 500;
/*  17: 34 */   private int heightDimension = 500;
/*  18: 35 */   private int thicknessDimension = 10;
/*  19: 36 */   private boolean tube = true;
/*  20:    */   private String message;
/*  21: 39 */   private float labelWidth = -1.0F;
/*  22: 40 */   private float labelHeight = -1.0F;
/*  23:    */   private static final String longestString = "888";
/*  24:    */   private static final float minMargin = 12.0F;
/*  25:    */   private static final float labelSep = 4.0F;
/*  26:    */   private static final float aspectRatio = 1.0F;
/*  27:    */   private static final int tickSep = 4;
/*  28:    */   private static final int tickSize = 9;
/*  29:    */   private static final int halfTickSize = 4;
/*  30:    */   private static final int thicknessDimSize = 16;
/*  31: 49 */   private final ResourceMap resourceMap = WPBDApp.getResourceMap(CrossSectionSketch.class);
/*  32:    */   
/*  33:    */   public void initialize(Shape shape)
/*  34:    */   {
/*  35: 57 */     if (shape == null)
/*  36:    */     {
/*  37: 58 */       this.message = this.resourceMap.getString("noCurrent.text", new Object[0]);
/*  38:    */     }
/*  39:    */     else
/*  40:    */     {
/*  41: 61 */       this.heightDimension = (this.widthDimension = shape.getNominalWidth());
/*  42: 62 */       this.thicknessDimension = ((int)Math.round(shape.getThickness()));
/*  43: 63 */       this.tube = (shape.getSection() instanceof TubeCrossSection);
/*  44: 64 */       this.message = null;
/*  45:    */     }
/*  46: 66 */     repaint();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void initialize(String message)
/*  50:    */   {
/*  51: 75 */     this.message = message;
/*  52: 76 */     repaint();
/*  53:    */   }
/*  54:    */   
/*  55:    */   private void hTick(Graphics2D g, float xRight, float y)
/*  56:    */   {
/*  57: 80 */     int ix = Math.round(xRight) - 4;
/*  58: 81 */     int iy = Math.round(y);
/*  59: 82 */     g.drawLine(ix, iy, ix - 9, iy);
/*  60:    */   }
/*  61:    */   
/*  62:    */   private void vTick(Graphics2D g, float x, float yTop)
/*  63:    */   {
/*  64: 86 */     int ix = Math.round(x);
/*  65: 87 */     int iy = Math.round(yTop) + 4;
/*  66: 88 */     g.drawLine(ix, iy, ix, iy + 9);
/*  67:    */   }
/*  68:    */   
/*  69:    */   private void hDim(Graphics2D g, float x0, float x1, float y)
/*  70:    */   {
/*  71: 92 */     int ix0 = Math.round(x0);
/*  72: 93 */     int ix1 = Math.round(x1);
/*  73: 94 */     int iy = Math.round(y) + 4 + 4;
/*  74: 95 */     Utility.drawDoubleArrow(g, ix0, iy, ix1, iy);
/*  75:    */   }
/*  76:    */   
/*  77:    */   private void vDim(Graphics2D g, float x, float y0, float y1)
/*  78:    */   {
/*  79: 99 */     int ix = Math.round(x) - 4 - 4;
/*  80:100 */     int iy0 = Math.round(y0);
/*  81:101 */     int iy1 = Math.round(y1);
/*  82:102 */     Utility.drawDoubleArrow(g, ix, iy0, ix, iy1);
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected void paintComponent(Graphics g0)
/*  86:    */   {
/*  87:112 */     Graphics2D g = (Graphics2D)g0;
/*  88:113 */     Color savedColor = g.getColor();
/*  89:114 */     int w = getWidth();
/*  90:115 */     int h = getHeight();
/*  91:116 */     g.setColor(Color.WHITE);
/*  92:117 */     g.fillRect(0, 0, w, h);
/*  93:118 */     if (this.labelWidth < 0.0F)
/*  94:    */     {
/*  95:119 */       FontRenderContext frc = g.getFontRenderContext();
/*  96:120 */       Font font = Labeler.getFont();
/*  97:121 */       LineMetrics metrics = font.getLineMetrics("888", frc);
/*  98:122 */       this.labelWidth = ((float)font.getStringBounds("888", frc).getWidth());
/*  99:123 */       this.labelHeight = (metrics.getAscent() + metrics.getDescent());
/* 100:    */     }
/* 101:125 */     if (this.message != null)
/* 102:    */     {
/* 103:126 */       Labeler.drawJustified(g0, this.message, w / 2, h / 2, 2, 2, null);
/* 104:127 */       return;
/* 105:    */     }
/* 106:129 */     float widthAvailable = w - (12.0F + this.labelWidth + 4.0F + 16.0F + 12.0F);
/* 107:130 */     float heightAvailable = h - (16.0F + this.labelHeight + 12.0F);
/* 108:131 */     float xMargin = 12.0F;
/* 109:132 */     float yMargin = 12.0F;
/* 110:133 */     float widthSection = 0.0F;
/* 111:134 */     float heightSection = 0.0F;
/* 112:135 */     if (widthAvailable > 1.0F * heightAvailable)
/* 113:    */     {
/* 114:136 */       xMargin += (widthAvailable - 1.0F * heightAvailable) / 2.0F;
/* 115:137 */       widthSection = 1.0F * heightAvailable;
/* 116:138 */       heightSection = heightAvailable;
/* 117:    */     }
/* 118:    */     else
/* 119:    */     {
/* 120:141 */       yMargin += (1.0F * heightAvailable - widthAvailable) / 2.0F;
/* 121:142 */       widthSection = widthAvailable;
/* 122:143 */       heightSection = widthAvailable / 1.0F;
/* 123:    */     }
/* 124:145 */     float xSection = xMargin + this.labelWidth + 4.0F;
/* 125:146 */     float ySection = yMargin;
/* 126:147 */     g.setColor(Color.GRAY);
/* 127:148 */     int tubeThickness = 5;
/* 128:149 */     g.fillRoundRect(Math.round(xSection), Math.round(ySection), 1 + Math.round(widthSection), 1 + Math.round(heightSection), 5, 5);
/* 129:152 */     if (this.tube)
/* 130:    */     {
/* 131:153 */       g.setColor(Color.WHITE);
/* 132:154 */       g.fillRect(Math.round(xSection) + 5, Math.round(ySection) + 5, 1 + Math.round(widthSection) - 10, 1 + Math.round(heightSection) - 10);
/* 133:    */       
/* 134:156 */       int y = Math.round(ySection + 0.75F * heightSection);
/* 135:157 */       int xRight = Math.round(xSection + widthSection) + 2;
/* 136:158 */       int xLeft = xRight - 5 - 1;
/* 137:159 */       g.setColor(Color.BLACK);
/* 138:160 */       Utility.drawArrow(g, xLeft - 16, y, xLeft, y);
/* 139:161 */       Utility.drawArrow(g, xRight + 16, y, xRight, y);
/* 140:162 */       Labeler.drawJustified(g, Integer.toString(this.thicknessDimension), xRight + 3, y - 3, 1, 6, null);
/* 141:    */     }
/* 142:    */     else
/* 143:    */     {
/* 144:166 */       g.setColor(Color.BLACK);
/* 145:    */     }
/* 146:168 */     hTick(g, xSection, ySection);
/* 147:169 */     hTick(g, xSection, ySection + heightSection);
/* 148:170 */     vDim(g, xSection, ySection, ySection + heightSection);
/* 149:171 */     Labeler.drawJustified(g, Integer.toString(this.heightDimension), Math.round(xSection - 4.0F), Math.round(ySection + heightSection / 2.0F), 3, 2, Color.WHITE, null);
/* 150:    */     
/* 151:    */ 
/* 152:    */ 
/* 153:    */ 
/* 154:    */ 
/* 155:177 */     vTick(g, xSection, ySection + heightAvailable);
/* 156:178 */     vTick(g, xSection + widthSection, ySection + heightSection);
/* 157:179 */     hDim(g, xSection, xSection + widthSection, ySection + heightSection);
/* 158:180 */     Labeler.drawJustified(g, Integer.toString(this.widthDimension), Math.round(xSection + widthSection / 2.0F), Math.round(ySection + heightSection + 4.0F + 9.0F), 2, 4, null);
/* 159:    */     
/* 160:    */ 
/* 161:    */ 
/* 162:184 */     g.setColor(savedColor);
/* 163:    */   }
/* 164:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.CrossSectionSketch
 * JD-Core Version:    0.7.0.1
 */