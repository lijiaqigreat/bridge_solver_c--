/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Font;
/*   5:    */ import java.awt.Graphics;
/*   6:    */ import java.awt.Graphics2D;
/*   7:    */ import java.awt.Rectangle;
/*   8:    */ import java.awt.font.FontRenderContext;
/*   9:    */ import java.awt.font.LineMetrics;
/*  10:    */ import java.awt.geom.AffineTransform;
/*  11:    */ import java.awt.geom.Rectangle2D;
/*  12:    */ import javax.swing.UIManager;
/*  13:    */ 
/*  14:    */ public class Labeler
/*  15:    */ {
/*  16:    */   public static final int JUSTIFY_LEFT = 1;
/*  17:    */   public static final int JUSTIFY_CENTER = 2;
/*  18:    */   public static final int JUSTIFY_RIGHT = 3;
/*  19:    */   public static final int JUSTIFY_TOP = 4;
/*  20:    */   public static final int JUSTIFY_BASELINE = 5;
/*  21:    */   public static final int JUSTIFY_BOTTOM = 6;
/*  22:    */   private static final int xMargin = 2;
/*  23:    */   private static final int yMargin = 1;
/*  24:    */   private static Font font;
/*  25:    */   
/*  26:    */   public static void drawJustified(Graphics g0, String s, int x0, int y0, int h, int v, Color background)
/*  27:    */   {
/*  28: 74 */     drawJustified(g0, s, x0, y0, h, v, background, Color.BLACK, null);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static void drawJustified(Graphics g0, String s, int x0, int y0, int h, int v, Color background, Rectangle bounds)
/*  32:    */   {
/*  33: 90 */     drawJustified(g0, s, x0, y0, h, v, background, null, bounds);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static Font getFont()
/*  37:    */   {
/*  38: 99 */     if (font == null)
/*  39:    */     {
/*  40:100 */       font = UIManager.getFont("Label.font");
/*  41:101 */       font = font.deriveFont(font.getSize() - 2.0F);
/*  42:    */     }
/*  43:103 */     return font;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static void drawRotatedAndJustified(Graphics g0, String s, double angle, int x0, int y0, int h, int v, Color background, Color border, Rectangle bounds)
/*  47:    */   {
/*  48:122 */     Graphics2D g = (Graphics2D)g0;
/*  49:123 */     AffineTransform savedXform = g.getTransform();
/*  50:124 */     g.rotate(Math.toRadians(angle), x0, y0);
/*  51:125 */     drawJustified(g, s, x0, y0, h, v, background, border, bounds);
/*  52:126 */     g.setTransform(savedXform);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static void drawJustified(Graphics g0, String s, int x0, int y0, int h, int v, Color background, Color border, Rectangle bounds)
/*  56:    */   {
/*  57:145 */     getFont();
/*  58:146 */     Graphics2D g = (Graphics2D)g0;
/*  59:147 */     Color savedColor = g.getColor();
/*  60:148 */     FontRenderContext frc = g.getFontRenderContext();
/*  61:149 */     LineMetrics metrics = font.getLineMetrics(s, frc);
/*  62:150 */     float width = (float)font.getStringBounds(s, frc).getWidth();
/*  63:151 */     float ascent = metrics.getAscent();
/*  64:152 */     float height = metrics.getDescent() + ascent;
/*  65:153 */     int x = x0;
/*  66:154 */     switch (h)
/*  67:    */     {
/*  68:    */     case 1: 
/*  69:156 */       if (background != null) {
/*  70:157 */         x += 2;
/*  71:    */       }
/*  72:    */       break;
/*  73:    */     case 2: 
/*  74:161 */       x = Math.round(x0 - width * 0.5F);
/*  75:162 */       break;
/*  76:    */     case 3: 
/*  77:164 */       x = Math.round(x0 - width);
/*  78:165 */       if (background != null) {
/*  79:166 */         x -= 2;
/*  80:    */       }
/*  81:    */       break;
/*  82:    */     }
/*  83:170 */     int y = y0;
/*  84:171 */     switch (v)
/*  85:    */     {
/*  86:    */     case 4: 
/*  87:173 */       y = Math.round(y0 + ascent);
/*  88:174 */       if (background != null) {
/*  89:175 */         y++;
/*  90:    */       }
/*  91:    */       break;
/*  92:    */     case 2: 
/*  93:179 */       y = Math.round(y0 + ascent - height * 0.5F);
/*  94:180 */       break;
/*  95:    */     case 6: 
/*  96:182 */       y = Math.round(y0 + ascent - height);
/*  97:183 */       if (background != null) {
/*  98:184 */         y--;
/*  99:    */       }
/* 100:    */       break;
/* 101:    */     }
/* 102:188 */     if (background != null)
/* 103:    */     {
/* 104:189 */       int xBg = Math.round(x - 2);
/* 105:190 */       int yBg = Math.round(y - ascent - 1.0F + 1.0F);
/* 106:191 */       int wBg = Math.round(width + 4.0F);
/* 107:192 */       int hBg = Math.round(height + 2.0F);
/* 108:193 */       g.setColor(background);
/* 109:194 */       g.fillRect(xBg, yBg, wBg - 1, hBg - 1);
/* 110:195 */       if (border != null)
/* 111:    */       {
/* 112:196 */         g.setColor(border);
/* 113:197 */         g.drawRect(xBg - 1, yBg - 1, wBg, hBg);
/* 114:    */       }
/* 115:    */     }
/* 116:200 */     if (bounds != null) {
/* 117:201 */       bounds.setBounds(x, Math.round(y - ascent), 1 + (int)width, 1 + (int)height);
/* 118:    */     }
/* 119:203 */     g.setFont(font);
/* 120:204 */     g.setColor(savedColor);
/* 121:    */     
/* 122:    */ 
/* 123:207 */     frc = new FontRenderContext(g.getTransform(), true, true);
/* 124:208 */     g.drawGlyphVector(font.createGlyphVector(frc, s), x, y);
/* 125:    */   }
/* 126:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Labeler
 * JD-Core Version:    0.7.0.1
 */