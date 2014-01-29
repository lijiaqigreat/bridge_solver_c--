/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import ch.randelshofer.quaqua.QuaquaManager;
/*   4:    */ import com.jogamp.opengl.util.texture.Texture;
/*   5:    */ import com.jogamp.opengl.util.texture.TextureData;
/*   6:    */ import com.jogamp.opengl.util.texture.TextureIO;
/*   7:    */ import java.awt.Font;
/*   8:    */ import java.awt.Graphics;
/*   9:    */ import java.awt.Image;
/*  10:    */ import java.awt.Toolkit;
/*  11:    */ import java.awt.Window;
/*  12:    */ import java.awt.image.BufferedImage;
/*  13:    */ import java.io.IOException;
/*  14:    */ import java.io.PrintStream;
/*  15:    */ import java.lang.reflect.Method;
/*  16:    */ import java.net.URL;
/*  17:    */ import java.util.Enumeration;
/*  18:    */ import java.util.EventObject;
/*  19:    */ import java.util.logging.Level;
/*  20:    */ import java.util.logging.Logger;
/*  21:    */ import javax.media.opengl.GLProfile;
/*  22:    */ import javax.swing.ImageIcon;
/*  23:    */ import javax.swing.JFrame;
/*  24:    */ import javax.swing.SwingUtilities;
/*  25:    */ import javax.swing.UIDefaults;
/*  26:    */ import javax.swing.UIManager;
/*  27:    */ import jogamp.common.Debug;
/*  28:    */ import org.jdesktop.application.Application;
/*  29:    */ import org.jdesktop.application.Application.ExitListener;
/*  30:    */ import org.jdesktop.application.ApplicationContext;
/*  31:    */ import org.jdesktop.application.LocalStorage;
/*  32:    */ import org.jdesktop.application.ResourceMap;
/*  33:    */ import org.jdesktop.application.SessionStorage;
/*  34:    */ 
/*  35:    */ public class WPBDApp
/*  36:    */   extends SingleFrameApplication
/*  37:    */ {
/*  38:    */   public static final int WINDOWS_OS = 1;
/*  39:    */   public static final int MAC_OS_X = 2;
/*  40:    */   public static final int LINUX_OS = 3;
/*  41:    */   public static final int UNKNOWN_OS = 4;
/*  42: 43 */   private static WPBDView view = null;
/*  43: 44 */   private static int os = 0;
/*  44: 45 */   private static Level loggerLevel = Debug.isPropertyDefined("wpbd.develop", false) ? Level.ALL : Level.OFF;
/*  45: 46 */   private static String fileName = null;
/*  46: 47 */   private static boolean legacyGraphics = false;
/*  47: 49 */   private static boolean enhancedMacUI = getOS() == 2;
/*  48: 50 */   private static GLProfile glProfile = null;
/*  49:    */   
/*  50:    */   public static GLProfile getGLProfile()
/*  51:    */   {
/*  52: 55 */     if (glProfile == null) {
/*  53: 56 */       glProfile = GLProfile.get("GL2");
/*  54:    */     }
/*  55: 58 */     return glProfile;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static int getOS()
/*  59:    */   {
/*  60: 62 */     if (os == 0)
/*  61:    */     {
/*  62: 63 */       String osName = System.getProperty("os.name").toLowerCase();
/*  63: 64 */       if (osName.indexOf("windows") >= 0) {
/*  64: 65 */         os = 1;
/*  65: 67 */       } else if (osName.indexOf("mac os x") >= 0) {
/*  66: 68 */         os = 2;
/*  67:    */       } else {
/*  68: 71 */         os = 4;
/*  69:    */       }
/*  70:    */     }
/*  71: 74 */     return os;
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected void startup()
/*  75:    */   {
/*  76: 84 */     Toolkit.getDefaultToolkit().setDynamicLayout(false);
/*  77:    */     
/*  78:    */ 
/*  79: 87 */     boolean developMac = Debug.isPropertyDefined("wpbd.develop.mac", false);
/*  80: 88 */     if ((enhancedMacUI) || (developMac)) {
/*  81:    */       try
/*  82:    */       {
/*  83: 90 */         if ((developMac) && (getOS() != 2)) {
/*  84: 91 */           System.setProperty("Quaqua.Debug.crossPlatform", "true");
/*  85:    */         }
/*  86: 93 */         UIManager.setLookAndFeel(QuaquaManager.getLookAndFeel());
/*  87:    */       }
/*  88:    */       catch (Exception e)
/*  89:    */       {
/*  90: 95 */         System.err.println("Could not load L&F (" + QuaquaManager.getLookAndFeelClassName() + ").");
/*  91: 96 */         enhancedMacUI = false;
/*  92:    */       }
/*  93:    */     }
/*  94:101 */     Logger logger = Logger.getLogger(SessionStorage.class.getName());
/*  95:102 */     logger.setLevel(loggerLevel);
/*  96:    */     
/*  97:    */ 
/*  98:105 */     view = new WPBDView(this);
/*  99:    */     
/* 100:    */ 
/* 101:108 */     JFrame frame = view.getFrame();
/* 102:109 */     frame.setExtendedState(frame.getExtendedState() | 0x6);
/* 103:    */     
/* 104:    */ 
/* 105:112 */     addExitListener(new Application.ExitListener()
/* 106:    */     {
/* 107:    */       public boolean canExit(EventObject e)
/* 108:    */       {
/* 109:113 */         return WPBDApp.this.quit();
/* 110:    */       }
/* 111:    */       
/* 112:    */       public void willExit(EventObject e) {}
/* 113:    */     });
/* 114:116 */     if (getOS() == 2)
/* 115:    */     {
/* 116:    */       try
/* 117:    */       {
/* 118:118 */         Method quitMethod = getClass().getDeclaredMethod("quit", (Class[])null);
/* 119:119 */         OSXAdapter.setQuitHandler(this, quitMethod);
/* 120:120 */         Method aboutMethod = getClass().getDeclaredMethod("about", (Class[])null);
/* 121:121 */         OSXAdapter.setAboutHandler(this, aboutMethod);
/* 122:    */       }
/* 123:    */       catch (Exception e)
/* 124:    */       {
/* 125:125 */         System.err.println("Error while loading the OSXAdapter:");
/* 126:    */       }
/* 127:128 */       Enumeration keys = UIManager.getDefaults().keys();
/* 128:129 */       while (keys.hasMoreElements())
/* 129:    */       {
/* 130:130 */         Object key = keys.nextElement();
/* 131:131 */         Object value = UIManager.get(key);
/* 132:132 */         if ((value instanceof Font))
/* 133:    */         {
/* 134:133 */           Font f = (Font)value;
/* 135:134 */           UIManager.put(key, f.deriveFont(0.95F * f.getSize()));
/* 136:    */         }
/* 137:    */       }
/* 138:    */     }
/* 139:138 */     show(view);
/* 140:    */     
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:143 */     SwingUtilities.invokeLater(new Runnable()
/* 145:    */     {
/* 146:    */       public void run()
/* 147:    */       {
/* 148:145 */         WPBDApp.view.initComponentsPostShow();
/* 149:148 */         if (Debug.isPropertyDefined("wpbd.develop", false)) {}
/* 150:    */       }
/* 151:    */     });
/* 152:    */   }
/* 153:    */   
/* 154:    */   protected void configureWindow(Window root) {}
/* 155:    */   
/* 156:    */   public static WPBDApp getApplication()
/* 157:    */   {
/* 158:168 */     return (WPBDApp)Application.getInstance(WPBDApp.class);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static ResourceMap getResourceMap(Class c)
/* 162:    */   {
/* 163:178 */     return getApplication().getContext().getResourceMap(c);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static JFrame getFrame()
/* 167:    */   {
/* 168:187 */     return view.getFrame();
/* 169:    */   }
/* 170:    */   
/* 171:    */   public static String getFileName()
/* 172:    */   {
/* 173:196 */     return fileName;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public static boolean isLegacyGraphics()
/* 177:    */   {
/* 178:200 */     return legacyGraphics;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public static boolean isEnhancedMacUI()
/* 182:    */   {
/* 183:204 */     return enhancedMacUI;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public static void main(String[] args)
/* 187:    */   {
/* 188:211 */     for (int i = 0; i < args.length; i++) {
/* 189:212 */       if (args[i].equals("-legacygraphics"))
/* 190:    */       {
/* 191:213 */         legacyGraphics = true;
/* 192:    */       }
/* 193:215 */       else if (args[i].equals("-noenhancedmacui"))
/* 194:    */       {
/* 195:216 */         enhancedMacUI = false;
/* 196:    */       }
/* 197:218 */       else if (fileName == null)
/* 198:    */       {
/* 199:219 */         fileName = args[i];
/* 200:    */       }
/* 201:    */       else
/* 202:    */       {
/* 203:222 */         System.err.printf("Invalid arguments in command.", new Object[0]);
/* 204:223 */         return;
/* 205:    */       }
/* 206:    */     }
/* 207:226 */     launch(WPBDApp.class, args);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public Image getImageResource(String name)
/* 211:    */   {
/* 212:239 */     return getIconResource(name).getImage();
/* 213:    */   }
/* 214:    */   
/* 215:    */   public BufferedImage getBufferedImageResource(String name)
/* 216:    */   {
/* 217:250 */     ImageIcon icon = getIconResource(name);
/* 218:251 */     Image image = icon.getImage();
/* 219:252 */     BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), 5);
/* 220:253 */     Graphics g = bufferedImage.getGraphics();
/* 221:254 */     g.drawImage(image, 0, 0, null);
/* 222:255 */     g.dispose();
/* 223:256 */     return bufferedImage;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public ImageIcon getIconResource(String name)
/* 227:    */   {
/* 228:267 */     URL url = getClass().getResource("/wpbd/resources/" + name);
/* 229:    */     
/* 230:269 */     return new ImageIcon(url);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public Texture getTextureResource(String name, boolean mipmap, String suffix)
/* 234:    */   {
/* 235:    */     try
/* 236:    */     {
/* 237:282 */       return TextureIO.newTexture(getClass().getResource("/wpbd/resources/" + name), mipmap, suffix);
/* 238:    */     }
/* 239:    */     catch (IOException ex) {}
/* 240:284 */     return null;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public TextureData getTextureDataResource(String name, int internalFormat, int pixelFormat, boolean mipmap, String fileSuffix)
/* 244:    */   {
/* 245:    */     try
/* 246:    */     {
/* 247:290 */       return TextureIO.newTextureData(glProfile, getClass().getResource("/wpbd/resources/" + name), internalFormat, pixelFormat, mipmap, fileSuffix);
/* 248:    */     }
/* 249:    */     catch (IOException ex) {}
/* 250:292 */     return null;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public TextureData getTextureDataResource(String name, boolean mipmap, String suffix)
/* 254:    */   {
/* 255:    */     try
/* 256:    */     {
/* 257:298 */       return TextureIO.newTextureData(glProfile, getClass().getResource("/wpbd/resources/" + name), mipmap, suffix);
/* 258:    */     }
/* 259:    */     catch (IOException ex) {}
/* 260:300 */     return null;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public static boolean saveToLocalStorage(Object item, String name)
/* 264:    */   {
/* 265:    */     try
/* 266:    */     {
/* 267:306 */       getApplication().getContext().getLocalStorage().save(item, name);
/* 268:307 */       return true;
/* 269:    */     }
/* 270:    */     catch (IOException ex) {}
/* 271:309 */     return false;
/* 272:    */   }
/* 273:    */   
/* 274:    */   public static Object loadFromLocalStorage(String name)
/* 275:    */   {
/* 276:    */     try
/* 277:    */     {
/* 278:315 */       return getApplication().getContext().getLocalStorage().load(name);
/* 279:    */     }
/* 280:    */     catch (IOException ex) {}
/* 281:317 */     return null;
/* 282:    */   }
/* 283:    */   
/* 284:    */   public boolean quit()
/* 285:    */   {
/* 286:323 */     view.shutdown();
/* 287:    */     
/* 288:325 */     return view.querySaveIfDirty();
/* 289:    */   }
/* 290:    */   
/* 291:    */   public void about()
/* 292:    */   {
/* 293:329 */     view.about();
/* 294:    */   }
/* 295:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.WPBDApp
 * JD-Core Version:    0.7.0.1
 */