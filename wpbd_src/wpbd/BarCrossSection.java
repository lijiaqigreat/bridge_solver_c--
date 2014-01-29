/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ class BarCrossSection
/*  4:   */   extends CrossSection
/*  5:   */ {
/*  6:   */   public BarCrossSection()
/*  7:   */   {
/*  8:27 */     super(0, "Solid Bar", "Bar");
/*  9:   */   }
/* 10:   */   
/* 11:   */   public Shape[] getShapes()
/* 12:   */   {
/* 13:36 */     int nSizes = this.widths.length;
/* 14:37 */     Shape[] s = new Shape[nSizes];
/* 15:38 */     for (int sizeIndex = 0; sizeIndex < nSizes; sizeIndex++)
/* 16:   */     {
/* 17:39 */       int width = this.widths[sizeIndex];
/* 18:40 */       double area = Utility.sqr(width) * 1.0E-006D;
/* 19:41 */       double moment = Utility.p4(width) / 12.0D * 1.0E-012D;
/* 20:42 */       s[sizeIndex] = new Shape(this, sizeIndex, String.format("%dx%d", new Object[] { Integer.valueOf(width), Integer.valueOf(width) }), width, area, moment);
/* 21:   */     }
/* 22:44 */     return s;
/* 23:   */   }
/* 24:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BarCrossSection
 * JD-Core Version:    0.7.0.1
 */