/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.io.PrintWriter;
/*   8:    */ import java.lang.reflect.Field;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.Vector;
/*  11:    */ 
/*  12:    */ public class ClassLister
/*  13:    */ {
/*  14:    */   public static Iterator list(ClassLoader cl)
/*  15:    */     throws NoSuchFieldException, IllegalAccessException
/*  16:    */   {
/*  17: 41 */     Class clClass = cl.getClass();
/*  18: 42 */     while (clClass != ClassLoader.class) {
/*  19: 43 */       clClass = clClass.getSuperclass();
/*  20:    */     }
/*  21: 45 */     Field clClassesField = clClass.getDeclaredField("classes");
/*  22: 46 */     clClassesField.setAccessible(true);
/*  23: 47 */     Vector classes = (Vector)clClassesField.get(cl);
/*  24: 48 */     return classes.iterator();
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static void printPreloader(ClassLoader cl)
/*  28:    */   {
/*  29: 57 */     if (cl == null) {
/*  30: 58 */       cl = WPBDApp.class.getClassLoader();
/*  31:    */     }
/*  32: 60 */     File f = new File("src/wpbd/Preloader.java");
/*  33:    */     FileOutputStream os;
/*  34:    */     try
/*  35:    */     {
/*  36: 63 */       os = new FileOutputStream(f, false);
/*  37:    */     }
/*  38:    */     catch (FileNotFoundException ex)
/*  39:    */     {
/*  40: 65 */       return;
/*  41:    */     }
/*  42: 67 */     PrintWriter ps = new PrintWriter(os);
/*  43: 68 */     ps.println("package wpbd;");
/*  44: 69 */     ps.println();
/*  45: 70 */     ps.println("public class Preloader {");
/*  46: 71 */     ps.println();
/*  47: 72 */     ps.println("    private static final String [] classes = {");
/*  48: 73 */     while (cl != null)
/*  49:    */     {
/*  50:    */       Iterator iter;
/*  51:    */       try
/*  52:    */       {
/*  53: 76 */         iter = list(cl);
/*  54:    */       }
/*  55:    */       catch (Exception ex) {}
/*  56: 78 */       continue;
/*  57: 80 */       while (iter.hasNext())
/*  58:    */       {
/*  59: 81 */         String name = iter.next().toString();
/*  60: 82 */         if (name.indexOf('$') < 0)
/*  61:    */         {
/*  62: 85 */           int i = name.indexOf(' ');
/*  63: 86 */           if (i >= 0) {
/*  64: 87 */             name = name.substring(i + 1);
/*  65:    */           }
/*  66: 89 */           ps.println("        \"" + name + "\",");
/*  67:    */         }
/*  68:    */       }
/*  69: 91 */       cl = cl.getParent();
/*  70:    */     }
/*  71: 93 */     ps.println("    };");
/*  72: 94 */     ps.println();
/*  73: 95 */     ps.println("    public static void preload() {");
/*  74: 96 */     ps.println("        for (int i = 0; i < classes.length; i++) {");
/*  75: 97 */     ps.println("            try {");
/*  76: 98 */     ps.println("                Class.forName(classes[i]);");
/*  77: 99 */     ps.println("            } catch (ClassNotFoundException ex) { }");
/*  78:100 */     ps.println("        }");
/*  79:101 */     ps.println("    }");
/*  80:102 */     ps.println("}");
/*  81:103 */     ps.close();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static void print(ClassLoader cl)
/*  85:    */   {
/*  86:112 */     while (cl != null)
/*  87:    */     {
/*  88:113 */       System.out.println("ClassLoader: " + cl);
/*  89:    */       Iterator iter;
/*  90:    */       try
/*  91:    */       {
/*  92:116 */         iter = list(cl);
/*  93:    */       }
/*  94:    */       catch (Exception ex) {}
/*  95:118 */       continue;
/*  96:120 */       while (iter.hasNext()) {
/*  97:121 */         System.out.println("\"" + iter.next() + "\",");
/*  98:    */       }
/*  99:123 */       cl = cl.getParent();
/* 100:    */     }
/* 101:    */   }
/* 102:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.ClassLister
 * JD-Core Version:    0.7.0.1
 */