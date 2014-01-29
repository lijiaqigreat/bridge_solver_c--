/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Point;
/*   4:    */ 
/*   5:    */ public class DraftingGrid
/*   6:    */ {
/*   7: 28 */   private static final int[] snapMultiples = { 4, 2, 1 };
/*   8:    */   public static final int COARSE_GRID = 0;
/*   9:    */   public static final int MEDIUM_GRID = 1;
/*  10:    */   public static final int FINE_GRID = 2;
/*  11: 44 */   protected static final int maxSnapMultiple = snapMultiples[0];
/*  12: 48 */   protected int snapMultiple = maxSnapMultiple;
/*  13:    */   protected static final double fineGridSize = 0.25D;
/*  14:    */   
/*  15:    */   public DraftingGrid(int density)
/*  16:    */   {
/*  17: 60 */     this.snapMultiple = snapMultiples[density];
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setDensity(int density)
/*  21:    */   {
/*  22: 69 */     this.snapMultiple = snapMultiples[density];
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static int toDensity(int snapMultiple)
/*  26:    */   {
/*  27: 80 */     for (int i = 0; i < snapMultiples.length; i++) {
/*  28: 81 */       if (snapMultiple == snapMultiples[i]) {
/*  29: 82 */         return i;
/*  30:    */       }
/*  31:    */     }
/*  32: 85 */     return -1;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean isFiner(int density)
/*  36:    */   {
/*  37: 95 */     return snapMultiples[density] < this.snapMultiple;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int getDensity()
/*  41:    */   {
/*  42:104 */     return toDensity(this.snapMultiple);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public double getGridSize()
/*  46:    */   {
/*  47:113 */     return 0.25D * this.snapMultiple;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setSnapMultiple(int snapMultiple)
/*  51:    */   {
/*  52:122 */     this.snapMultiple = snapMultiple;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int getSnapMultiple()
/*  56:    */   {
/*  57:131 */     return this.snapMultiple;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static int snapMultipleOf(double c)
/*  61:    */   {
/*  62:142 */     int grid = (int)Math.round(c / 0.25D);
/*  63:143 */     int lsb = grid & (grid - 1 ^ 0xFFFFFFFF);
/*  64:144 */     return lsb > maxSnapMultiple ? maxSnapMultiple : lsb == 0 ? maxSnapMultiple : lsb;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int worldToGridX(double xWorld)
/*  68:    */   {
/*  69:154 */     return this.snapMultiple * (int)Math.round(xWorld / (0.25D * this.snapMultiple));
/*  70:    */   }
/*  71:    */   
/*  72:    */   public int worldToGridY(double yWorld)
/*  73:    */   {
/*  74:164 */     return this.snapMultiple * (int)Math.round(yWorld / (0.25D * this.snapMultiple));
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void worldToGrid(Point dst, Affine.Point src)
/*  78:    */   {
/*  79:174 */     dst.x = worldToGridX(src.x);
/*  80:175 */     dst.y = worldToGridY(src.y);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public static int graduationLevel(int grid)
/*  84:    */   {
/*  85:187 */     int mask = 3;
/*  86:188 */     int level = 2;
/*  87:189 */     while ((mask & grid) != 0)
/*  88:    */     {
/*  89:190 */       mask >>= 1;
/*  90:191 */       level--;
/*  91:    */     }
/*  92:193 */     return level;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public double gridToWorldX(int xGrid)
/*  96:    */   {
/*  97:203 */     return xGrid * 0.25D;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public double gridToWorldY(int yGrid)
/* 101:    */   {
/* 102:213 */     return yGrid * 0.25D;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void gridToWorld(Affine.Point dst, Point src)
/* 106:    */   {
/* 107:223 */     dst.x = gridToWorldX(src.x);
/* 108:224 */     dst.y = gridToWorldY(src.y);
/* 109:    */   }
/* 110:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DraftingGrid
 * JD-Core Version:    0.7.0.1
 */