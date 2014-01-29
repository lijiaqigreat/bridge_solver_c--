/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.lang.reflect.Constructor;
/*   6:    */ import java.lang.reflect.Field;
/*   7:    */ import java.lang.reflect.InvocationTargetException;
/*   8:    */ import java.lang.reflect.Method;
/*   9:    */ import java.net.URL;
/*  10:    */ 
/*  11:    */ public class Browser
/*  12:    */ {
/*  13:    */   private static int jvm;
/*  14:    */   private static Object browser;
/*  15:    */   
/*  16:    */   static
/*  17:    */   {
/*  18:237 */     boolean ok = true;
/*  19:238 */     String osName = System.getProperty("os.name");
/*  20:239 */     if (osName.startsWith("Mac OS"))
/*  21:    */     {
/*  22:240 */       String mrjVersion = System.getProperty("mrj.version");
/*  23:241 */       String majorMRJVersion = mrjVersion.substring(0, 3);
/*  24:    */       try
/*  25:    */       {
/*  26:243 */         double version = Double.valueOf(majorMRJVersion).doubleValue();
/*  27:244 */         if (version == 2.0D)
/*  28:    */         {
/*  29:245 */           jvm = 0;
/*  30:    */         }
/*  31:247 */         else if ((version >= 2.1D) && (version < 3.0D))
/*  32:    */         {
/*  33:251 */           jvm = 1;
/*  34:    */         }
/*  35:253 */         else if (version == 3.0D)
/*  36:    */         {
/*  37:254 */           jvm = 3;
/*  38:    */         }
/*  39:256 */         else if (version >= 3.1D)
/*  40:    */         {
/*  41:258 */           jvm = 4;
/*  42:    */         }
/*  43:    */         else
/*  44:    */         {
/*  45:261 */           ok = false;
/*  46:262 */           errorMessage = "Unsupported MRJ version: " + version;
/*  47:    */         }
/*  48:    */       }
/*  49:    */       catch (NumberFormatException nfe)
/*  50:    */       {
/*  51:266 */         ok = false;
/*  52:267 */         errorMessage = "Invalid MRJ version: " + mrjVersion;
/*  53:    */       }
/*  54:    */     }
/*  55:270 */     else if (osName.startsWith("Windows"))
/*  56:    */     {
/*  57:271 */       if (osName.indexOf("9") != -1) {
/*  58:272 */         jvm = 6;
/*  59:    */       } else {
/*  60:275 */         jvm = 5;
/*  61:    */       }
/*  62:    */     }
/*  63:    */     else
/*  64:    */     {
/*  65:279 */       jvm = -1;
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:281 */   private static final boolean loadedWithoutErrors = ok ? loadClasses() : false;
/*  70:    */   private static Class<?> mrjFileUtilsClass;
/*  71:    */   private static Class<?> mrjOSTypeClass;
/*  72:    */   private static Class<?> aeDescClass;
/*  73:    */   private static Constructor<?> aeTargetConstructor;
/*  74:    */   private static Constructor<?> appleEventConstructor;
/*  75:    */   private static Constructor<?> aeDescConstructor;
/*  76:    */   private static Method findFolder;
/*  77:    */   private static Method getFileCreator;
/*  78:    */   private static Method getFileType;
/*  79:    */   private static Method openURL;
/*  80:    */   private static Method makeOSType;
/*  81:    */   private static Method putParameter;
/*  82:    */   private static Method sendNoReply;
/*  83:    */   private static Object kSystemFolderType;
/*  84:    */   private static Integer keyDirectObject;
/*  85:    */   private static Integer kAutoGenerateReturnID;
/*  86:    */   private static Integer kAnyTransactionID;
/*  87:    */   private static Object linkage;
/*  88:    */   private static final String JDirect_MacOSX = "/System/Library/Frameworks/Carbon.framework/Frameworks/HIToolbox.framework/HIToolbox";
/*  89:    */   private static final int MRJ_2_0 = 0;
/*  90:    */   private static final int MRJ_2_1 = 1;
/*  91:    */   private static final int MRJ_3_0 = 3;
/*  92:    */   private static final int MRJ_3_1 = 4;
/*  93:    */   private static final int WINDOWS_NT = 5;
/*  94:    */   private static final int WINDOWS_9x = 6;
/*  95:    */   private static final int OTHER = -1;
/*  96:    */   private static final String FINDER_TYPE = "FNDR";
/*  97:    */   private static final String FINDER_CREATOR = "MACS";
/*  98:    */   private static final String GURL_EVENT = "GURL";
/*  99:    */   private static final String FIRST_WINDOWS_PARAMETER = "/c";
/* 100:    */   private static final String SECOND_WINDOWS_PARAMETER = "start";
/* 101:    */   private static final String THIRD_WINDOWS_PARAMETER = "\"\"";
/* 102:    */   private static final String NETSCAPE_REMOTE_PARAMETER = "-remote";
/* 103:    */   private static final String NETSCAPE_OPEN_PARAMETER_START = "openURL(";
/* 104:    */   private static final String NETSCAPE_OPEN_PARAMETER_END = ")";
/* 105:    */   private static String errorMessage;
/* 106:    */   
/* 107:    */   private static boolean loadClasses()
/* 108:    */   {
/* 109:296 */     switch (jvm)
/* 110:    */     {
/* 111:    */     case 0: 
/* 112:    */       try
/* 113:    */       {
/* 114:299 */         Class<?> aeTargetClass = Class.forName("com.apple.MacOS.AETarget");
/* 115:300 */         Class<?> osUtilsClass = Class.forName("com.apple.MacOS.OSUtils");
/* 116:301 */         Class<?> appleEventClass = Class.forName("com.apple.MacOS.AppleEvent");
/* 117:302 */         Class<?> aeClass = Class.forName("com.apple.MacOS.ae");
/* 118:303 */         aeDescClass = Class.forName("com.apple.MacOS.AEDesc");
/* 119:    */         
/* 120:305 */         aeTargetConstructor = aeTargetClass.getDeclaredConstructor(new Class[] { Integer.TYPE });
/* 121:306 */         appleEventConstructor = appleEventClass.getDeclaredConstructor(new Class[] { Integer.TYPE, Integer.TYPE, aeTargetClass, Integer.TYPE, Integer.TYPE });
/* 122:307 */         aeDescConstructor = aeDescClass.getDeclaredConstructor(new Class[] { String.class });
/* 123:    */         
/* 124:309 */         makeOSType = osUtilsClass.getDeclaredMethod("makeOSType", new Class[] { String.class });
/* 125:310 */         putParameter = appleEventClass.getDeclaredMethod("putParameter", new Class[] { Integer.TYPE, aeDescClass });
/* 126:311 */         sendNoReply = appleEventClass.getDeclaredMethod("sendNoReply", new Class[0]);
/* 127:    */         
/* 128:313 */         Field keyDirectObjectField = aeClass.getDeclaredField("keyDirectObject");
/* 129:314 */         keyDirectObject = (Integer)keyDirectObjectField.get(null);
/* 130:315 */         Field autoGenerateReturnIDField = appleEventClass.getDeclaredField("kAutoGenerateReturnID");
/* 131:316 */         kAutoGenerateReturnID = (Integer)autoGenerateReturnIDField.get(null);
/* 132:317 */         Field anyTransactionIDField = appleEventClass.getDeclaredField("kAnyTransactionID");
/* 133:318 */         kAnyTransactionID = (Integer)anyTransactionIDField.get(null);
/* 134:    */       }
/* 135:    */       catch (ClassNotFoundException cnfe)
/* 136:    */       {
/* 137:321 */         errorMessage = cnfe.getMessage();
/* 138:322 */         return false;
/* 139:    */       }
/* 140:    */       catch (NoSuchMethodException nsme)
/* 141:    */       {
/* 142:325 */         errorMessage = nsme.getMessage();
/* 143:326 */         return false;
/* 144:    */       }
/* 145:    */       catch (NoSuchFieldException nsfe)
/* 146:    */       {
/* 147:329 */         errorMessage = nsfe.getMessage();
/* 148:330 */         return false;
/* 149:    */       }
/* 150:    */       catch (IllegalAccessException iae)
/* 151:    */       {
/* 152:333 */         errorMessage = iae.getMessage();
/* 153:334 */         return false;
/* 154:    */       }
/* 155:    */     case 1: 
/* 156:    */       try
/* 157:    */       {
/* 158:340 */         mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
/* 159:341 */         mrjOSTypeClass = Class.forName("com.apple.mrj.MRJOSType");
/* 160:342 */         Field systemFolderField = mrjFileUtilsClass.getDeclaredField("kSystemFolderType");
/* 161:343 */         kSystemFolderType = systemFolderField.get(null);
/* 162:344 */         findFolder = mrjFileUtilsClass.getDeclaredMethod("findFolder", new Class[] { mrjOSTypeClass });
/* 163:345 */         getFileCreator = mrjFileUtilsClass.getDeclaredMethod("getFileCreator", new Class[] { File.class });
/* 164:346 */         getFileType = mrjFileUtilsClass.getDeclaredMethod("getFileType", new Class[] { File.class });
/* 165:    */       }
/* 166:    */       catch (ClassNotFoundException cnfe)
/* 167:    */       {
/* 168:349 */         errorMessage = cnfe.getMessage();
/* 169:350 */         return false;
/* 170:    */       }
/* 171:    */       catch (NoSuchFieldException nsfe)
/* 172:    */       {
/* 173:353 */         errorMessage = nsfe.getMessage();
/* 174:354 */         return false;
/* 175:    */       }
/* 176:    */       catch (NoSuchMethodException nsme)
/* 177:    */       {
/* 178:357 */         errorMessage = nsme.getMessage();
/* 179:358 */         return false;
/* 180:    */       }
/* 181:    */       catch (SecurityException se)
/* 182:    */       {
/* 183:361 */         errorMessage = se.getMessage();
/* 184:362 */         return false;
/* 185:    */       }
/* 186:    */       catch (IllegalAccessException iae)
/* 187:    */       {
/* 188:365 */         errorMessage = iae.getMessage();
/* 189:366 */         return false;
/* 190:    */       }
/* 191:    */     case 3: 
/* 192:    */       try
/* 193:    */       {
/* 194:372 */         Class<?> linker = Class.forName("com.apple.mrj.jdirect.Linker");
/* 195:373 */         Constructor constructor = linker.getConstructor(new Class[] { Class.class });
/* 196:374 */         linkage = constructor.newInstance(new Object[] { Browser.class });
/* 197:    */       }
/* 198:    */       catch (ClassNotFoundException cnfe)
/* 199:    */       {
/* 200:377 */         errorMessage = cnfe.getMessage();
/* 201:378 */         return false;
/* 202:    */       }
/* 203:    */       catch (NoSuchMethodException nsme)
/* 204:    */       {
/* 205:381 */         errorMessage = nsme.getMessage();
/* 206:382 */         return false;
/* 207:    */       }
/* 208:    */       catch (InvocationTargetException ite)
/* 209:    */       {
/* 210:385 */         errorMessage = ite.getMessage();
/* 211:386 */         return false;
/* 212:    */       }
/* 213:    */       catch (InstantiationException ie)
/* 214:    */       {
/* 215:389 */         errorMessage = ie.getMessage();
/* 216:390 */         return false;
/* 217:    */       }
/* 218:    */       catch (IllegalAccessException iae)
/* 219:    */       {
/* 220:393 */         errorMessage = iae.getMessage();
/* 221:394 */         return false;
/* 222:    */       }
/* 223:    */     case 4: 
/* 224:    */       try
/* 225:    */       {
/* 226:400 */         mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
/* 227:401 */         openURL = mrjFileUtilsClass.getDeclaredMethod("openURL", new Class[] { String.class });
/* 228:    */       }
/* 229:    */       catch (ClassNotFoundException cnfe)
/* 230:    */       {
/* 231:404 */         errorMessage = cnfe.getMessage();
/* 232:405 */         return false;
/* 233:    */       }
/* 234:    */       catch (NoSuchMethodException nsme)
/* 235:    */       {
/* 236:408 */         errorMessage = nsme.getMessage();
/* 237:409 */         return false;
/* 238:    */       }
/* 239:    */     }
/* 240:416 */     return true;
/* 241:    */   }
/* 242:    */   
/* 243:    */   private static Object locateBrowser()
/* 244:    */   {
/* 245:433 */     if (browser != null) {
/* 246:434 */       return browser;
/* 247:    */     }
/* 248:436 */     switch (jvm)
/* 249:    */     {
/* 250:    */     case 0: 
/* 251:    */       try
/* 252:    */       {
/* 253:439 */         Integer finderCreatorCode = (Integer)makeOSType.invoke(null, new Object[] { "MACS" });
/* 254:440 */         Object aeTarget = aeTargetConstructor.newInstance(new Object[] { finderCreatorCode });
/* 255:441 */         Integer gurlType = (Integer)makeOSType.invoke(null, new Object[] { "GURL" });
/* 256:442 */         return appleEventConstructor.newInstance(new Object[] { gurlType, gurlType, aeTarget, kAutoGenerateReturnID, kAnyTransactionID });
/* 257:    */       }
/* 258:    */       catch (IllegalAccessException iae)
/* 259:    */       {
/* 260:453 */         browser = null;
/* 261:454 */         errorMessage = iae.getMessage();
/* 262:455 */         return browser;
/* 263:    */       }
/* 264:    */       catch (InstantiationException ie)
/* 265:    */       {
/* 266:458 */         browser = null;
/* 267:459 */         errorMessage = ie.getMessage();
/* 268:460 */         return browser;
/* 269:    */       }
/* 270:    */       catch (InvocationTargetException ite)
/* 271:    */       {
/* 272:463 */         browser = null;
/* 273:464 */         errorMessage = ite.getMessage();
/* 274:465 */         return browser;
/* 275:    */       }
/* 276:    */     case 1: 
/* 277:    */       File systemFolder;
/* 278:    */       try
/* 279:    */       {
/* 280:471 */         systemFolder = (File)findFolder.invoke(null, new Object[] { kSystemFolderType });
/* 281:    */       }
/* 282:    */       catch (IllegalArgumentException iare)
/* 283:    */       {
/* 284:475 */         browser = null;
/* 285:476 */         errorMessage = iare.getMessage();
/* 286:477 */         return browser;
/* 287:    */       }
/* 288:    */       catch (IllegalAccessException iae)
/* 289:    */       {
/* 290:480 */         browser = null;
/* 291:481 */         errorMessage = iae.getMessage();
/* 292:482 */         return browser;
/* 293:    */       }
/* 294:    */       catch (InvocationTargetException ite)
/* 295:    */       {
/* 296:485 */         browser = null;
/* 297:486 */         errorMessage = ite.getTargetException().getClass() + ": " + ite.getTargetException().getMessage();
/* 298:    */         
/* 299:488 */         return browser;
/* 300:    */       }
/* 301:492 */       String[] systemFolderFiles = systemFolder.list();
/* 302:494 */       for (int i = 0; i < systemFolderFiles.length; i++) {
/* 303:    */         try
/* 304:    */         {
/* 305:496 */           File file = new File(systemFolder, systemFolderFiles[i]);
/* 306:497 */           if (file.isFile())
/* 307:    */           {
/* 308:505 */             Object fileType = getFileType.invoke(null, new Object[] { file });
/* 309:506 */             if ("FNDR".equals(fileType.toString()))
/* 310:    */             {
/* 311:507 */               Object fileCreator = getFileCreator.invoke(null, new Object[] { file });
/* 312:509 */               if ("MACS".equals(fileCreator.toString()))
/* 313:    */               {
/* 314:511 */                 browser = file.toString();
/* 315:512 */                 return browser;
/* 316:    */               }
/* 317:    */             }
/* 318:    */           }
/* 319:    */         }
/* 320:    */         catch (IllegalArgumentException iare)
/* 321:    */         {
/* 322:518 */           errorMessage = iare.getMessage();
/* 323:519 */           return null;
/* 324:    */         }
/* 325:    */         catch (IllegalAccessException iae)
/* 326:    */         {
/* 327:522 */           browser = null;
/* 328:523 */           errorMessage = iae.getMessage();
/* 329:524 */           return browser;
/* 330:    */         }
/* 331:    */         catch (InvocationTargetException ite)
/* 332:    */         {
/* 333:527 */           browser = null;
/* 334:528 */           errorMessage = ite.getTargetException().getClass() + ": " + ite.getTargetException().getMessage();
/* 335:    */           
/* 336:530 */           return browser;
/* 337:    */         }
/* 338:    */       }
/* 339:533 */       browser = null;
/* 340:534 */       break;
/* 341:    */     case 3: 
/* 342:    */     case 4: 
/* 343:538 */       browser = "";
/* 344:539 */       break;
/* 345:    */     case 5: 
/* 346:542 */       browser = "cmd.exe";
/* 347:543 */       break;
/* 348:    */     case 6: 
/* 349:546 */       browser = "command.com";
/* 350:547 */       break;
/* 351:    */     }
/* 352:551 */     browser = "netscape";
/* 353:    */     
/* 354:    */ 
/* 355:    */ 
/* 356:555 */     return browser;
/* 357:    */   }
/* 358:    */   
/* 359:    */   public static void openUrl(File file)
/* 360:    */     throws IOException
/* 361:    */   {
/* 362:570 */     openUrl("file://" + file.getPath());
/* 363:    */   }
/* 364:    */   
/* 365:    */   public static void openUrl(URL url)
/* 366:    */     throws IOException
/* 367:    */   {
/* 368:584 */     openUrl(url.toString());
/* 369:    */   }
/* 370:    */   
/* 371:    */   public static void openUrl(String url)
/* 372:    */     throws IOException
/* 373:    */   {
/* 374:598 */     if (!loadedWithoutErrors) {
/* 375:599 */       throw new IOException("Exception in finding browser: " + errorMessage);
/* 376:    */     }
/* 377:601 */     Object locatedBrowser = locateBrowser();
/* 378:602 */     if (locatedBrowser == null) {
/* 379:603 */       throw new IOException("Unable to locate browser: " + errorMessage);
/* 380:    */     }
/* 381:    */     Process process;
/* 382:606 */     switch (jvm)
/* 383:    */     {
/* 384:    */     case 0: 
/* 385:608 */       Object aeDesc = null;
/* 386:    */       try
/* 387:    */       {
/* 388:610 */         aeDesc = aeDescConstructor.newInstance(new Object[] { url });
/* 389:611 */         putParameter.invoke(locatedBrowser, new Object[] { keyDirectObject, aeDesc });
/* 390:612 */         sendNoReply.invoke(locatedBrowser, new Object[0]);
/* 391:    */       }
/* 392:    */       catch (InvocationTargetException ite)
/* 393:    */       {
/* 394:615 */         throw new IOException("InvocationTargetException while creating AEDesc: " + ite.getMessage());
/* 395:    */       }
/* 396:    */       catch (IllegalAccessException iae)
/* 397:    */       {
/* 398:618 */         throw new IOException("IllegalAccessException while building AppleEvent: " + iae.getMessage());
/* 399:    */       }
/* 400:    */       catch (InstantiationException ie)
/* 401:    */       {
/* 402:621 */         throw new IOException("InstantiationException while creating AEDesc: " + ie.getMessage());
/* 403:    */       }
/* 404:    */       finally
/* 405:    */       {
/* 406:624 */         aeDesc = null;
/* 407:625 */         locatedBrowser = null;
/* 408:    */       }
/* 409:627 */       break;
/* 410:    */     case 1: 
/* 411:630 */       Runtime.getRuntime().exec(new String[] { (String)locatedBrowser, url });
/* 412:631 */       break;
/* 413:    */     case 3: 
/* 414:634 */       int[] instance = new int[1];
/* 415:635 */       int result = ICStart(instance, 0);
/* 416:636 */       if (result == 0)
/* 417:    */       {
/* 418:637 */         int[] selectionStart = { 0 };
/* 419:638 */         byte[] urlBytes = url.getBytes();
/* 420:639 */         int[] selectionEnd = { urlBytes.length };
/* 421:640 */         result = ICLaunchURL(instance[0], new byte[] { 0 }, urlBytes, urlBytes.length, selectionStart, selectionEnd);
/* 422:643 */         if (result == 0) {
/* 423:646 */           ICStop(instance);
/* 424:    */         } else {
/* 425:649 */           throw new IOException("Unable to launch URL: " + result);
/* 426:    */         }
/* 427:    */       }
/* 428:    */       else
/* 429:    */       {
/* 430:653 */         throw new IOException("Unable to create an Internet Config instance: " + result);
/* 431:    */       }
/* 432:    */       break;
/* 433:    */     case 4: 
/* 434:    */       try
/* 435:    */       {
/* 436:659 */         openURL.invoke(null, new Object[] { url });
/* 437:    */       }
/* 438:    */       catch (InvocationTargetException ite)
/* 439:    */       {
/* 440:662 */         throw new IOException("InvocationTargetException while calling openURL: " + ite.getMessage());
/* 441:    */       }
/* 442:    */       catch (IllegalAccessException iae)
/* 443:    */       {
/* 444:665 */         throw new IOException("IllegalAccessException while calling openURL: " + iae.getMessage());
/* 445:    */       }
/* 446:    */     case 5: 
/* 447:    */     case 6: 
/* 448:673 */       process = Runtime.getRuntime().exec(new String[] { (String)locatedBrowser, "/c", "start", "\"\"", '"' + url + '"' });
/* 449:    */       try
/* 450:    */       {
/* 451:684 */         process.waitFor();
/* 452:685 */         process.exitValue();
/* 453:    */       }
/* 454:    */       catch (InterruptedException ie)
/* 455:    */       {
/* 456:688 */         throw new IOException("InterruptedException while launching browser: " + ie.getMessage());
/* 457:    */       }
/* 458:    */     case -1: 
/* 459:696 */       String command = locatedBrowser.toString() + " -raise " + "-remote" + " " + "openURL(" + url + ")";
/* 460:    */       
/* 461:    */ 
/* 462:    */ 
/* 463:    */ 
/* 464:701 */       process = Runtime.getRuntime().exec(command);
/* 465:    */       try
/* 466:    */       {
/* 467:704 */         int exitCode = process.waitFor();
/* 468:705 */         if (exitCode != 0) {
/* 469:706 */           Runtime.getRuntime().exec(new String[] { (String)locatedBrowser, url });
/* 470:    */         }
/* 471:    */       }
/* 472:    */       catch (InterruptedException exception)
/* 473:    */       {
/* 474:710 */         exception.printStackTrace();
/* 475:711 */         throw new IOException("InterruptedException while launching browser: " + exception.getMessage());
/* 476:    */       }
/* 477:    */     case 2: 
/* 478:    */     default: 
/* 479:717 */       Runtime.getRuntime().exec(new String[] { (String)locatedBrowser, url });
/* 480:    */     }
/* 481:    */   }
/* 482:    */   
/* 483:    */   private static native int ICStart(int[] paramArrayOfInt, int paramInt);
/* 484:    */   
/* 485:    */   private static native int ICStop(int[] paramArrayOfInt);
/* 486:    */   
/* 487:    */   private static native int ICLaunchURL(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
/* 488:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Browser
 * JD-Core Version:    0.7.0.1
 */