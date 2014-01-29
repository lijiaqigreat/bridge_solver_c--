/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.Set;
/*  6:   */ import org.jdesktop.application.ResourceMap;
/*  7:   */ 
/*  8:   */ public class BridgeSample
/*  9:   */ {
/* 10:   */   private String name;
/* 11:   */   private String bridgeAsString;
/* 12:   */   private static Object[] list;
/* 13:   */   
/* 14:   */   public BridgeSample(String name, String bridgeAsString)
/* 15:   */   {
/* 16:47 */     this.name = name;
/* 17:48 */     this.bridgeAsString = bridgeAsString;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getBridgeAsString()
/* 21:   */   {
/* 22:57 */     return this.bridgeAsString;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getName()
/* 26:   */   {
/* 27:66 */     return this.name;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String toString()
/* 31:   */   {
/* 32:76 */     return this.name;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public static Object[] getList()
/* 36:   */   {
/* 37:86 */     if (list == null)
/* 38:   */     {
/* 39:87 */       ArrayList<Object> v = new ArrayList();
/* 40:88 */       ResourceMap resourceMap = WPBDApp.getResourceMap(BridgeSample.class);
/* 41:89 */       Iterator<String> i = resourceMap.keySet().iterator();
/* 42:90 */       while (i.hasNext())
/* 43:   */       {
/* 44:91 */         String nameKey = (String)i.next();
/* 45:92 */         if (nameKey.endsWith(".bridgeSampleName"))
/* 46:   */         {
/* 47:93 */           String sampleKey = nameKey.substring(0, nameKey.lastIndexOf('.')).concat(".bridgeSample");
/* 48:94 */           v.add(new BridgeSample(resourceMap.getString(nameKey, new Object[0]), resourceMap.getString(sampleKey, new Object[0])));
/* 49:   */         }
/* 50:   */       }
/* 51:97 */       list = v.toArray();
/* 52:   */     }
/* 53:99 */     return list;
/* 54:   */   }
/* 55:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeSample
 * JD-Core Version:    0.7.0.1
 */