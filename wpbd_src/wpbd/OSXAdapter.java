/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.lang.reflect.Constructor;
/*   5:    */ import java.lang.reflect.InvocationHandler;
/*   6:    */ import java.lang.reflect.InvocationTargetException;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.lang.reflect.Proxy;
/*   9:    */ 
/*  10:    */ public class OSXAdapter
/*  11:    */   implements InvocationHandler
/*  12:    */ {
/*  13:    */   protected Object targetObject;
/*  14:    */   protected Method targetMethod;
/*  15:    */   protected String proxySignature;
/*  16:    */   static Object macOSXApplication;
/*  17:    */   
/*  18:    */   public static void setQuitHandler(Object target, Method quitHandler)
/*  19:    */   {
/*  20: 73 */     setHandler(new OSXAdapter("handleQuit", target, quitHandler));
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static void setAboutHandler(Object target, Method aboutHandler)
/*  24:    */   {
/*  25: 79 */     boolean enableAboutMenu = (target != null) && (aboutHandler != null);
/*  26: 80 */     if (enableAboutMenu) {
/*  27: 81 */       setHandler(new OSXAdapter("handleAbout", target, aboutHandler));
/*  28:    */     }
/*  29:    */     try
/*  30:    */     {
/*  31: 86 */       Method enableAboutMethod = macOSXApplication.getClass().getDeclaredMethod("setEnabledAboutMenu", new Class[] { Boolean.TYPE });
/*  32: 87 */       enableAboutMethod.invoke(macOSXApplication, new Object[] { Boolean.valueOf(enableAboutMenu) });
/*  33:    */     }
/*  34:    */     catch (Exception ex)
/*  35:    */     {
/*  36: 89 */       System.err.println("OSXAdapter could not access the About Menu");
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static void setPreferencesHandler(Object target, Method prefsHandler)
/*  41:    */   {
/*  42: 96 */     boolean enablePrefsMenu = (target != null) && (prefsHandler != null);
/*  43: 97 */     if (enablePrefsMenu) {
/*  44: 98 */       setHandler(new OSXAdapter("handlePreferences", target, prefsHandler));
/*  45:    */     }
/*  46:    */     try
/*  47:    */     {
/*  48:103 */       Method enablePrefsMethod = macOSXApplication.getClass().getDeclaredMethod("setEnabledPreferencesMenu", new Class[] { Boolean.TYPE });
/*  49:104 */       enablePrefsMethod.invoke(macOSXApplication, new Object[] { Boolean.valueOf(enablePrefsMenu) });
/*  50:    */     }
/*  51:    */     catch (Exception ex)
/*  52:    */     {
/*  53:106 */       System.err.println("OSXAdapter could not access the About Menu");
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static void setFileHandler(Object target, Method fileHandler)
/*  58:    */   {
/*  59:114 */     setHandler(new OSXAdapter("handleOpenFile", target, fileHandler)
/*  60:    */     {
/*  61:    */       public boolean callTarget(Object appleEvent)
/*  62:    */       {
/*  63:119 */         if (appleEvent != null) {
/*  64:    */           try
/*  65:    */           {
/*  66:121 */             Method getFilenameMethod = appleEvent.getClass().getDeclaredMethod("getFilename", (Class[])null);
/*  67:122 */             String filename = (String)getFilenameMethod.invoke(appleEvent, (Object[])null);
/*  68:123 */             this.targetMethod.invoke(this.targetObject, new Object[] { filename });
/*  69:    */           }
/*  70:    */           catch (Exception ex) {}
/*  71:    */         }
/*  72:128 */         return true;
/*  73:    */       }
/*  74:    */     });
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static void setHandler(OSXAdapter adapter)
/*  78:    */   {
/*  79:    */     try
/*  80:    */     {
/*  81:136 */       Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
/*  82:137 */       if (macOSXApplication == null)
/*  83:    */       {
/*  84:138 */         Class<?>[] classArray = null;
/*  85:139 */         Object[] objectArray = null;
/*  86:140 */         macOSXApplication = applicationClass.getConstructor(classArray).newInstance(objectArray);
/*  87:    */       }
/*  88:142 */       Class applicationListenerClass = Class.forName("com.apple.eawt.ApplicationListener");
/*  89:143 */       Method addListenerMethod = applicationClass.getDeclaredMethod("addApplicationListener", new Class[] { applicationListenerClass });
/*  90:    */       
/*  91:145 */       Object osxAdapterProxy = Proxy.newProxyInstance(OSXAdapter.class.getClassLoader(), new Class[] { applicationListenerClass }, adapter);
/*  92:146 */       addListenerMethod.invoke(macOSXApplication, new Object[] { osxAdapterProxy });
/*  93:    */     }
/*  94:    */     catch (ClassNotFoundException cnfe)
/*  95:    */     {
/*  96:148 */       System.err.println("This version of Mac OS X does not support the Apple EAWT.  ApplicationEvent handling has been disabled (" + cnfe + ")");
/*  97:    */     }
/*  98:    */     catch (Exception ex)
/*  99:    */     {
/* 100:150 */       System.err.println("Mac OS X Adapter could not talk to EAWT:");
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected OSXAdapter(String proxySignature, Object target, Method handler)
/* 105:    */   {
/* 106:157 */     this.proxySignature = proxySignature;
/* 107:158 */     this.targetObject = target;
/* 108:159 */     this.targetMethod = handler;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public boolean callTarget(Object appleEvent)
/* 112:    */     throws InvocationTargetException, IllegalAccessException
/* 113:    */   {
/* 114:166 */     Object result = this.targetMethod.invoke(this.targetObject, (Object[])null);
/* 115:167 */     if (result == null) {
/* 116:168 */       return true;
/* 117:    */     }
/* 118:170 */     return Boolean.valueOf(result.toString()).booleanValue();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public Object invoke(Object proxy, Method method, Object[] args)
/* 122:    */     throws Throwable
/* 123:    */   {
/* 124:176 */     if (isCorrectMethod(method, args))
/* 125:    */     {
/* 126:177 */       boolean handled = callTarget(args[0]);
/* 127:178 */       setApplicationEventHandled(args[0], handled);
/* 128:    */     }
/* 129:181 */     return null;
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected boolean isCorrectMethod(Method method, Object[] args)
/* 133:    */   {
/* 134:187 */     return (this.targetMethod != null) && (this.proxySignature.equals(method.getName())) && (args.length == 1);
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected void setApplicationEventHandled(Object event, boolean handled)
/* 138:    */   {
/* 139:193 */     if (event != null) {
/* 140:    */       try
/* 141:    */       {
/* 142:195 */         Method setHandledMethod = event.getClass().getDeclaredMethod("setHandled", new Class[] { Boolean.TYPE });
/* 143:    */         
/* 144:197 */         setHandledMethod.invoke(event, new Object[] { Boolean.valueOf(handled) });
/* 145:    */       }
/* 146:    */       catch (Exception ex)
/* 147:    */       {
/* 148:199 */         System.err.println("OSXAdapter was unable to handle an ApplicationEvent: " + event);
/* 149:    */       }
/* 150:    */     }
/* 151:    */   }
/* 152:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.OSXAdapter
 * JD-Core Version:    0.7.0.1
 */