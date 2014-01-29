/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.net.URL;
/*  5:   */ import javax.help.HelpBroker;
/*  6:   */ import javax.help.HelpSet;
/*  7:   */ 
/*  8:   */ public class Help
/*  9:   */ {
/* 10:   */   private static final String helpsetFilename = "/wpbd/help/help.hs";
/* 11:   */   private static HelpSet helpSet;
/* 12:   */   private static HelpBroker helpBroker;
/* 13:   */   
/* 14:   */   public static boolean initialize()
/* 15:   */   {
/* 16:38 */     if (helpSet == null)
/* 17:   */     {
/* 18:   */       try
/* 19:   */       {
/* 20:40 */         URL hsURL = WPBDApp.getApplication().getClass().getResource("/wpbd/help/help.hs");
/* 21:41 */         helpSet = new HelpSet(null, hsURL);
/* 22:   */       }
/* 23:   */       catch (Exception ee)
/* 24:   */       {
/* 25:43 */         System.err.println("Open Help Set: " + ee.getMessage());
/* 26:44 */         System.err.println("Help Set /wpbd/help/help.hs not found");
/* 27:45 */         return false;
/* 28:   */       }
/* 29:47 */       if (helpBroker == null) {
/* 30:48 */         helpBroker = helpSet.createHelpBroker();
/* 31:   */       }
/* 32:   */     }
/* 33:51 */     return true;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public static HelpBroker getBroker()
/* 37:   */   {
/* 38:60 */     return helpBroker;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public static HelpSet getSet()
/* 42:   */   {
/* 43:69 */     return helpSet;
/* 44:   */   }
/* 45:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Help
 * JD-Core Version:    0.7.0.1
 */