/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ class TubeCrossSection
/*  4:   */   extends CrossSection
/*  5:   */ {
/*  6:   */   public TubeCrossSection()
/*  7:   */   {
/*  8:27 */     super(1, "Hollow Tube", "Tube");
/*  9:   */   }
/* 10:   */   
/* 11:   */   public Shape[] getShapes()
/* 12:   */   {
/* 13:36 */     int nSizes = this.widths.length;
/* 14:37 */     Shape[] s = new Shape[nSizes];
/* 15:38 */     for (int sizeIndex = 0; sizeIndex < nSizes; sizeIndex++)
/* 16:   */     {
/* 17:39 */       int width = this.widths[sizeIndex];
/* 18:   */       
/* 19:   */ 
/* 20:   */ 
/* 21:   */ 
/* 22:   */ 
/* 23:   */ 
/* 24:   */ 
/* 25:   */ 
/* 26:   */ 
/* 27:   */ 
/* 28:50 */       int thickness = Math.max(width / 20, 2);
/* 29:51 */       double area = (Utility.sqr(width) - Utility.sqr(width - 2 * thickness)) * 1.0E-006D;
/* 30:52 */       double moment = (Utility.p4(width) - Utility.p4(width - 2 * thickness)) / 12.0D * 1.0E-012D;
/* 31:53 */       s[sizeIndex] = new Shape(this, sizeIndex, String.format("%dx%dx%d", new Object[] { Integer.valueOf(width), Integer.valueOf(width), Integer.valueOf(thickness) }), width, area, moment, thickness);
/* 32:   */     }
/* 33:55 */     return s;
/* 34:   */   }
/* 35:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.TubeCrossSection
 * JD-Core Version:    0.7.0.1
 */