/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ public class Shape
/*   4:    */ {
/*   5:    */   private final int sizeIndex;
/*   6:    */   private final CrossSection section;
/*   7:    */   private final String name;
/*   8:    */   private final double width;
/*   9:    */   private final double area;
/*  10:    */   private final double moment;
/*  11:    */   private final double inverseRadiusOfGyration;
/*  12:    */   private final double thickness;
/*  13:    */   
/*  14:    */   public Shape(CrossSection section, int sizeIndex, String name, double width, double area, double moment)
/*  15:    */   {
/*  16: 43 */     this(section, sizeIndex, name, width, area, moment, width);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public Shape(CrossSection section, int sizeIndex, String name, double width, double area, double moment, double thickness)
/*  20:    */   {
/*  21: 58 */     this.section = section;
/*  22: 59 */     this.sizeIndex = sizeIndex;
/*  23: 60 */     this.name = name;
/*  24: 61 */     this.width = width;
/*  25: 62 */     this.area = area;
/*  26: 63 */     this.moment = moment;
/*  27: 64 */     this.inverseRadiusOfGyration = Math.sqrt(area / moment);
/*  28: 65 */     this.thickness = thickness;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public double getArea()
/*  32:    */   {
/*  33: 74 */     return this.area;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public double getMoment()
/*  37:    */   {
/*  38: 83 */     return this.moment;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double getInverseRadiusOfGyration()
/*  42:    */   {
/*  43: 92 */     return this.inverseRadiusOfGyration;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public double getMaxSlendernessLength()
/*  47:    */   {
/*  48:101 */     return 300.0D / this.inverseRadiusOfGyration;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getName()
/*  52:    */   {
/*  53:110 */     return this.name;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public CrossSection getSection()
/*  57:    */   {
/*  58:119 */     return this.section;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int getSizeIndex()
/*  62:    */   {
/*  63:128 */     return this.sizeIndex;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public double getWidth()
/*  67:    */   {
/*  68:137 */     return this.width;
/*  69:    */   }
/*  70:    */   
/*  71:    */   int getNominalWidth()
/*  72:    */   {
/*  73:146 */     return (int)Math.round(this.width);
/*  74:    */   }
/*  75:    */   
/*  76:    */   double getThickness()
/*  77:    */   {
/*  78:155 */     return this.thickness;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String toString()
/*  82:    */   {
/*  83:165 */     return this.name + " mm " + this.section;
/*  84:    */   }
/*  85:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Shape
 * JD-Core Version:    0.7.0.1
 */