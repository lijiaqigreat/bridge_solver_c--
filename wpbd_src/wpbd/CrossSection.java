/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ public abstract class CrossSection
/*   4:    */ {
/*   5:    */   protected final int index;
/*   6:    */   protected final String name;
/*   7:    */   protected final String shortName;
/*   8: 38 */   protected int[] widths = { 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 220, 240, 260, 280, 300, 320, 340, 360, 400, 500 };
/*   9:    */   
/*  10:    */   public CrossSection(int index, String name, String shortName)
/*  11:    */   {
/*  12: 54 */     this.index = index;
/*  13: 55 */     this.name = name;
/*  14: 56 */     this.shortName = shortName;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public abstract Shape[] getShapes();
/*  18:    */   
/*  19:    */   public int getIndex()
/*  20:    */   {
/*  21: 72 */     return this.index;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public int getNSizes()
/*  25:    */   {
/*  26: 81 */     return this.widths.length;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String getName()
/*  30:    */   {
/*  31: 90 */     return this.name;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getShortName()
/*  35:    */   {
/*  36: 99 */     return this.shortName;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String toString()
/*  40:    */   {
/*  41:108 */     return this.shortName;
/*  42:    */   }
/*  43:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.CrossSection
 * JD-Core Version:    0.7.0.1
 */