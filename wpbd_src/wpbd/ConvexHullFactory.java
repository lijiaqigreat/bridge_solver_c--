/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.ArrayDeque;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.Comparator;
/*   8:    */ import java.util.Deque;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.List;
/*  11:    */ 
/*  12:    */ public class ConvexHullFactory
/*  13:    */ {
/*  14: 34 */   private final List<Affine.Point> pts = new ArrayList(64);
/*  15: 35 */   private final Deque<Affine.Point> orderedPts = new ArrayDeque(64);
/*  16: 36 */   private final List<Affine.Point> hull = new ArrayList(64);
/*  17:    */   
/*  18:    */   public void add(double x, double y)
/*  19:    */   {
/*  20: 45 */     this.pts.add(new Affine.Point(x, y));
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void add(Affine.Point pt)
/*  24:    */   {
/*  25: 54 */     this.pts.add(pt);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void clear()
/*  29:    */   {
/*  30: 61 */     this.orderedPts.clear();
/*  31: 62 */     this.hull.clear();
/*  32: 63 */     this.pts.clear();
/*  33:    */   }
/*  34:    */   
/*  35: 66 */   private static final Comparator<Affine.Point> xThenYComparator = new Comparator()
/*  36:    */   {
/*  37:    */     public int compare(Affine.Point a, Affine.Point b)
/*  38:    */     {
/*  39: 69 */       return a.y > b.y ? 1 : a.y < b.y ? -1 : a.x > b.x ? 1 : a.x < b.x ? -1 : 0;
/*  40:    */     }
/*  41:    */   };
/*  42:    */   
/*  43:    */   public Affine.Point[] getHull(Affine.Point[] rtn)
/*  44:    */   {
/*  45: 83 */     this.orderedPts.clear();
/*  46: 84 */     this.hull.clear();
/*  47: 87 */     if (this.pts.size() <= 2) {
/*  48: 88 */       return (Affine.Point[])this.pts.toArray(new Affine.Point[this.pts.size()]);
/*  49:    */     }
/*  50: 93 */     Collections.sort(this.pts, xThenYComparator);
/*  51:    */     
/*  52:    */ 
/*  53:    */ 
/*  54: 97 */     Affine.Point leftmost = (Affine.Point)this.pts.get(0);
/*  55: 98 */     Affine.Point rightmost = (Affine.Point)this.pts.get(this.pts.size() - 1);
/*  56: 99 */     Affine.Vector vLeftRight = rightmost.minus(leftmost);
/*  57:100 */     Iterator<Affine.Point> ipts = this.pts.listIterator();
/*  58:101 */     while (ipts.hasNext())
/*  59:    */     {
/*  60:102 */       Affine.Point p = (Affine.Point)ipts.next();
/*  61:103 */       if ((p == rightmost) || (vLeftRight.cross(p.minus(leftmost)) > 0.0D)) {
/*  62:104 */         this.orderedPts.addFirst(p);
/*  63:    */       } else {
/*  64:107 */         this.orderedPts.addLast(p);
/*  65:    */       }
/*  66:    */     }
/*  67:112 */     Iterator<Affine.Point> iOrderedPts = this.orderedPts.iterator();
/*  68:113 */     while (iOrderedPts.hasNext())
/*  69:    */     {
/*  70:114 */       Affine.Point p0 = (Affine.Point)iOrderedPts.next();
/*  71:115 */       makeHullConvex(p0);
/*  72:116 */       this.hull.add(p0);
/*  73:    */     }
/*  74:119 */     makeHullConvex((Affine.Point)this.orderedPts.peekFirst());
/*  75:    */     
/*  76:    */ 
/*  77:    */ 
/*  78:123 */     return (Affine.Point[])this.hull.toArray(rtn == null ? new Affine.Point[this.hull.size()] : rtn);
/*  79:    */   }
/*  80:    */   
/*  81:    */   private void makeHullConvex(Affine.Point p0)
/*  82:    */   {
/*  83:133 */     while (this.hull.size() >= 2)
/*  84:    */     {
/*  85:134 */       Affine.Point p1 = (Affine.Point)this.hull.get(this.hull.size() - 1);
/*  86:135 */       Affine.Point p2 = (Affine.Point)this.hull.get(this.hull.size() - 2);
/*  87:136 */       if ((p1.x - p2.x) * (p0.y - p1.y) - (p1.y - p2.y) * (p0.x - p1.x) > 0.0D) {
/*  88:    */         break;
/*  89:    */       }
/*  90:139 */       this.hull.remove(this.hull.size() - 1);
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static void main(String[] args)
/*  95:    */   {
/*  96:144 */     ConvexHullFactory ch = new ConvexHullFactory();
/*  97:145 */     ch.add(0.0D, 0.0D);
/*  98:146 */     ch.add(1.0D, 1.0D);
/*  99:147 */     ch.add(0.0D, 1.0D);
/* 100:148 */     ch.add(0.25D, 0.25D);
/* 101:149 */     ch.add(1.0D, 0.0D);
/* 102:150 */     ch.add(0.25D, 0.55D);
/* 103:151 */     ch.add(0.5D, 1.25D);
/* 104:152 */     ch.add(0.55D, 0.25D);
/* 105:153 */     ch.add(0.5D, -0.25D);
/* 106:154 */     ch.add(1.25D, 0.5D);
/* 107:155 */     ch.add(0.5D, 0.5D);
/* 108:156 */     ch.add(-0.25D, 0.5D);
/* 109:157 */     Affine.Point[] ans = null;
/* 110:158 */     long start = System.nanoTime();
/* 111:159 */     int n = 1000000;
/* 112:160 */     for (int i = 0; i < n; i++) {
/* 113:161 */       ans = ch.getHull(null);
/* 114:    */     }
/* 115:163 */     long stop = System.nanoTime();
/* 116:164 */     double usec = (stop - start) / 1000.0D / n;
/* 117:165 */     System.out.println("uSec per iteration for " + n + " iterations: " + usec);
/* 118:166 */     for (int i = 0; i < ans.length; i++) {
/* 119:167 */       System.out.println(i + ": " + ans[i]);
/* 120:    */     }
/* 121:169 */     System.out.println("Area=" + Affine.getPolygonArea(ans, ans.length));
/* 122:    */   }
/* 123:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.ConvexHullFactory
 * JD-Core Version:    0.7.0.1
 */