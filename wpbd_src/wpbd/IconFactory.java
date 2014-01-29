/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import javax.swing.Icon;
/*  4:   */ import javax.swing.ImageIcon;
/*  5:   */ 
/*  6:   */ public class IconFactory
/*  7:   */ {
/*  8:   */   static IconFactory iconFactory;
/*  9:   */   private final ImageIcon goodIcon;
/* 10:   */   private final ImageIcon badIcon;
/* 11:   */   private final ImageIcon workingIcon;
/* 12:   */   
/* 13:   */   private IconFactory()
/* 14:   */   {
/* 15:34 */     WPBDApp app = WPBDApp.getApplication();
/* 16:35 */     this.goodIcon = app.getIconResource("goodsmall.png");
/* 17:36 */     this.badIcon = app.getIconResource("badsmall.png");
/* 18:37 */     this.workingIcon = app.getIconResource("workingsmall.png");
/* 19:   */   }
/* 20:   */   
/* 21:   */   private static void initialize()
/* 22:   */   {
/* 23:41 */     if (iconFactory == null) {
/* 24:42 */       iconFactory = new IconFactory();
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static Icon bridgeStatus(int status)
/* 29:   */   {
/* 30:   */     
/* 31:48 */     switch (status)
/* 32:   */     {
/* 33:   */     case 4: 
/* 34:50 */       return iconFactory.goodIcon;
/* 35:   */     case 3: 
/* 36:52 */       return iconFactory.badIcon;
/* 37:   */     }
/* 38:54 */     return iconFactory.workingIcon;
/* 39:   */   }
/* 40:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.IconFactory
 * JD-Core Version:    0.7.0.1
 */