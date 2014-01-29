/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ public class Material
/*   4:    */ {
/*   5:    */   private final int index;
/*   6:    */   private final String name;
/*   7:    */   private final String shortName;
/*   8:    */   private final double E;
/*   9:    */   private final double Fy;
/*  10:    */   private final double density;
/*  11:    */   private final double[] cost;
/*  12:    */   
/*  13:    */   public Material(int index, String name, String shortName, double E, double Fy, double density, double[] cost)
/*  14:    */   {
/*  15: 45 */     this.index = index;
/*  16: 46 */     this.name = name;
/*  17: 47 */     this.shortName = shortName;
/*  18: 48 */     this.E = E;
/*  19: 49 */     this.Fy = Fy;
/*  20: 50 */     this.density = density;
/*  21: 51 */     this.cost = cost;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public double getE()
/*  25:    */   {
/*  26: 60 */     return this.E;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public double getFy()
/*  30:    */   {
/*  31: 69 */     return this.Fy;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double getCost(CrossSection crossSection)
/*  35:    */   {
/*  36: 79 */     return this.cost[crossSection.getIndex()];
/*  37:    */   }
/*  38:    */   
/*  39:    */   public double getDensity()
/*  40:    */   {
/*  41: 88 */     return this.density;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int getIndex()
/*  45:    */   {
/*  46: 97 */     return this.index;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String getName()
/*  50:    */   {
/*  51:106 */     return this.name;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getShortName()
/*  55:    */   {
/*  56:115 */     return this.shortName;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String toString()
/*  60:    */   {
/*  61:125 */     return this.name;
/*  62:    */   }
/*  63:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Material
 * JD-Core Version:    0.7.0.1
 */