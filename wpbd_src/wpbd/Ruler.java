/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Component;
/*   5:    */ import java.awt.Graphics;
/*   6:    */ import java.awt.event.ComponentEvent;
/*   7:    */ import java.awt.event.ComponentListener;
/*   8:    */ import java.awt.geom.Rectangle2D.Double;
/*   9:    */ import java.text.DecimalFormat;
/*  10:    */ import java.text.NumberFormat;
/*  11:    */ import javax.swing.JLabel;
/*  12:    */ 
/*  13:    */ public class Ruler
/*  14:    */   extends JLabel
/*  15:    */   implements ComponentListener
/*  16:    */ {
/*  17:    */   private int side;
/*  18:    */   private RulerHost host;
/*  19: 33 */   private static final int[] tickSize = { 1, 3, 5 };
/*  20: 34 */   private static final NumberFormat labelFormatter = new DecimalFormat("0");
/*  21:    */   
/*  22:    */   public Ruler(RulerHost host, int side)
/*  23:    */   {
/*  24: 44 */     this.side = side;
/*  25: 45 */     this.host = host;
/*  26:    */     
/*  27:    */ 
/*  28: 48 */     host.getComponent().addComponentListener(this);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void paint(Graphics g)
/*  32:    */   {
/*  33: 58 */     super.paint(g);
/*  34: 59 */     if (!this.host.getViewportTransform().isSet()) {
/*  35: 60 */       return;
/*  36:    */     }
/*  37: 62 */     if (this.side == 5)
/*  38:    */     {
/*  39: 63 */       g.setColor(Color.WHITE);
/*  40: 64 */       g.drawLine(0, 0, getWidth() - 1, 0);
/*  41: 65 */       double xWorld = this.host.getDraftingCoordinates().getExtent().getX();
/*  42: 66 */       int snapMultiple = this.host.getDraftingCoordinates().getSnapMultiple();
/*  43: 67 */       double dxWorld = this.host.getDraftingCoordinates().getGridSize();
/*  44: 68 */       int i0 = this.host.getDraftingCoordinates().worldToGridX(this.host.getDraftingCoordinates().getExtent().getMinX());
/*  45: 69 */       int i1 = this.host.getDraftingCoordinates().worldToGridX(this.host.getDraftingCoordinates().getExtent().getMaxX());
/*  46: 70 */       int y0 = 0;
/*  47: 71 */       int i = i0;
/*  48: 72 */       while (i <= i1)
/*  49:    */       {
/*  50: 73 */         int x = this.host.getViewportTransform().worldToViewportX(xWorld);
/*  51: 74 */         g.setColor(Color.BLACK);
/*  52: 75 */         int level = DraftingGrid.graduationLevel(i);
/*  53: 76 */         int y1 = tickSize[level];
/*  54: 77 */         g.drawLine(x, 0, x, y1);
/*  55: 78 */         g.setColor(Color.WHITE);
/*  56: 79 */         g.drawLine(x + 1, 0, x + 1, y1);
/*  57: 80 */         if (level == 2)
/*  58:    */         {
/*  59: 81 */           g.setColor(Color.BLACK);
/*  60: 82 */           Labeler.drawJustified(g, labelFormatter.format(xWorld), x, y1 + 2, 2, 4, null);
/*  61:    */         }
/*  62: 85 */         xWorld += dxWorld;
/*  63: 86 */         i += snapMultiple;
/*  64:    */       }
/*  65:    */     }
/*  66:    */     else
/*  67:    */     {
/*  68: 89 */       double yWorld = this.host.getDraftingCoordinates().getExtent().getY();
/*  69: 90 */       int snapMultiple = this.host.getDraftingCoordinates().getSnapMultiple();
/*  70: 91 */       double dyWorld = this.host.getDraftingCoordinates().getGridSize();
/*  71:    */       
/*  72: 93 */       int i0 = this.host.getDraftingCoordinates().worldToGridY(this.host.getDraftingCoordinates().getExtent().getMinY());
/*  73: 94 */       int i1 = this.host.getDraftingCoordinates().worldToGridY(this.host.getDraftingCoordinates().getExtent().getMaxY());
/*  74: 95 */       int x1 = getWidth() - 1;
/*  75: 96 */       int i = i0;
/*  76: 97 */       while (i <= i1)
/*  77:    */       {
/*  78: 98 */         int y = this.host.getViewportTransform().worldToViewportY(yWorld);
/*  79: 99 */         g.setColor(Color.BLACK);
/*  80:100 */         int level = DraftingGrid.graduationLevel(i);
/*  81:101 */         int x0 = x1 - tickSize[level];
/*  82:102 */         g.drawLine(x0, y, x1, y);
/*  83:103 */         g.setColor(Color.WHITE);
/*  84:104 */         g.drawLine(x0, y + 1, x1, y + 1);
/*  85:105 */         if (level == 2)
/*  86:    */         {
/*  87:106 */           g.setColor(Color.BLACK);
/*  88:107 */           Labeler.drawJustified(g, labelFormatter.format(yWorld), x0 - 2, y, 3, 2, null);
/*  89:    */         }
/*  90:110 */         yWorld += dyWorld;
/*  91:111 */         i += snapMultiple;
/*  92:    */       }
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void componentResized(ComponentEvent e)
/*  97:    */   {
/*  98:122 */     repaint();
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void componentMoved(ComponentEvent e) {}
/* 102:    */   
/* 103:    */   public void componentShown(ComponentEvent e) {}
/* 104:    */   
/* 105:    */   public void componentHidden(ComponentEvent e) {}
/* 106:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Ruler
 * JD-Core Version:    0.7.0.1
 */