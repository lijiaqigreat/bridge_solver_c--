/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.Container;
/*   5:    */ import java.awt.Dimension;
/*   6:    */ import java.awt.Point;
/*   7:    */ import java.awt.Toolkit;
/*   8:    */ import java.awt.Window;
/*   9:    */ import java.awt.event.ComponentEvent;
/*  10:    */ import java.awt.event.ComponentListener;
/*  11:    */ import java.awt.event.HierarchyEvent;
/*  12:    */ import java.awt.event.HierarchyListener;
/*  13:    */ import java.awt.event.WindowAdapter;
/*  14:    */ import java.awt.event.WindowEvent;
/*  15:    */ import java.io.IOException;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import java.util.List;
/*  18:    */ import java.util.logging.Level;
/*  19:    */ import java.util.logging.Logger;
/*  20:    */ import javax.swing.JComponent;
/*  21:    */ import javax.swing.JDialog;
/*  22:    */ import javax.swing.JFrame;
/*  23:    */ import javax.swing.JRootPane;
/*  24:    */ import javax.swing.JWindow;
/*  25:    */ import javax.swing.RootPaneContainer;
/*  26:    */ import org.jdesktop.application.Application;
/*  27:    */ import org.jdesktop.application.ApplicationContext;
/*  28:    */ import org.jdesktop.application.FrameView;
/*  29:    */ import org.jdesktop.application.ResourceMap;
/*  30:    */ import org.jdesktop.application.SessionStorage;
/*  31:    */ import org.jdesktop.application.View;
/*  32:    */ import org.jdesktop.application.utils.SwingHelper;
/*  33:    */ 
/*  34:    */ public abstract class SingleFrameApplication
/*  35:    */   extends Application
/*  36:    */ {
/*  37:    */   private static final String INITIALIZATION_MARKER = "SingleFrameApplication.initRootPaneContainer";
/*  38: 87 */   private static final Logger logger = Logger.getLogger(SingleFrameApplication.class.getName());
/*  39:    */   
/*  40:    */   public final JFrame getMainFrame()
/*  41:    */   {
/*  42:115 */     return getMainView().getFrame();
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected final void setMainFrame(JFrame mainFrame)
/*  46:    */   {
/*  47:139 */     getMainView().setFrame(mainFrame);
/*  48:    */   }
/*  49:    */   
/*  50:    */   private String sessionFilename(Window window)
/*  51:    */   {
/*  52:143 */     if (window == null) {
/*  53:144 */       return null;
/*  54:    */     }
/*  55:146 */     String name = window.getName();
/*  56:147 */     return name + ".session.xml";
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void configureWindow(Window root)
/*  60:    */   {
/*  61:171 */     getContext().getResourceMap().injectComponents(root);
/*  62:    */   }
/*  63:    */   
/*  64:    */   private void initRootPaneContainer(RootPaneContainer c)
/*  65:    */   {
/*  66:175 */     JComponent rootPane = c.getRootPane();
/*  67:177 */     if (rootPane.getClientProperty("SingleFrameApplication.initRootPaneContainer") != null) {
/*  68:178 */       return;
/*  69:    */     }
/*  70:180 */     rootPane.putClientProperty("SingleFrameApplication.initRootPaneContainer", Boolean.TRUE);
/*  71:    */     
/*  72:182 */     Container root = rootPane.getParent();
/*  73:183 */     if ((root instanceof Window)) {
/*  74:184 */       configureWindow((Window)root);
/*  75:    */     }
/*  76:187 */     JFrame mainFrame = getMainFrame();
/*  77:188 */     if (c == mainFrame)
/*  78:    */     {
/*  79:189 */       mainFrame.addWindowListener(new MainFrameListener(null));
/*  80:190 */       mainFrame.setDefaultCloseOperation(0);
/*  81:    */     }
/*  82:191 */     else if ((root instanceof Window))
/*  83:    */     {
/*  84:192 */       Window window = (Window)root;
/*  85:193 */       window.addHierarchyListener(new SecondaryWindowListener(null));
/*  86:    */     }
/*  87:196 */     if ((root instanceof JFrame)) {
/*  88:197 */       root.addComponentListener(new FrameBoundsListener(null));
/*  89:    */     }
/*  90:200 */     if ((root instanceof Window))
/*  91:    */     {
/*  92:201 */       Window window = (Window)root;
/*  93:204 */       if ((!root.isValid()) || (root.getWidth() == 0) || (root.getHeight() == 0)) {
/*  94:205 */         window.pack();
/*  95:    */       }
/*  96:209 */       String filename = sessionFilename((Window)root);
/*  97:210 */       if (filename != null) {
/*  98:    */         try
/*  99:    */         {
/* 100:212 */           getContext().getSessionStorage().restore(root, filename);
/* 101:    */         }
/* 102:    */         catch (Exception e)
/* 103:    */         {
/* 104:214 */           String msg = String.format("couldn't restore session [%s]", new Object[] { filename });
/* 105:215 */           logger.log(Level.WARNING, msg, e);
/* 106:    */         }
/* 107:    */       }
/* 108:221 */       Point defaultLocation = SwingHelper.defaultLocation(window);
/* 109:222 */       if ((!window.isLocationByPlatform()) && (root.getX() == defaultLocation.getX()) && (root.getY() == defaultLocation.getY()))
/* 110:    */       {
/* 111:226 */         Dimension screenSize = window.getToolkit().getScreenSize();
/* 112:227 */         Dimension windowSIze = window.getSize();
/* 113:229 */         if ((screenSize.getWidth() / windowSIze.getWidth() > 1.25D) && (screenSize.getHeight() / windowSIze.getHeight() > 1.25D))
/* 114:    */         {
/* 115:232 */           Component owner = window.getOwner();
/* 116:233 */           if (owner == null) {
/* 117:234 */             owner = window != mainFrame ? mainFrame : null;
/* 118:    */           }
/* 119:236 */           window.setLocationRelativeTo(owner);
/* 120:    */         }
/* 121:    */       }
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   protected void show(JComponent c)
/* 126:    */   {
/* 127:265 */     if (c == null) {
/* 128:266 */       throw new IllegalArgumentException("null JComponent");
/* 129:    */     }
/* 130:268 */     JFrame f = getMainFrame();
/* 131:269 */     f.getContentPane().add(c, "Center");
/* 132:270 */     initRootPaneContainer(f);
/* 133:271 */     f.setVisible(true);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void show(JDialog c)
/* 137:    */   {
/* 138:293 */     if (c == null) {
/* 139:294 */       throw new IllegalArgumentException("null JDialog");
/* 140:    */     }
/* 141:296 */     initRootPaneContainer(c);
/* 142:297 */     c.setVisible(true);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void show(JFrame c)
/* 146:    */   {
/* 147:319 */     if (c == null) {
/* 148:320 */       throw new IllegalArgumentException("null JFrame");
/* 149:    */     }
/* 150:322 */     initRootPaneContainer(c);
/* 151:323 */     c.setVisible(true);
/* 152:    */   }
/* 153:    */   
/* 154:    */   private void saveSession(Window window)
/* 155:    */   {
/* 156:327 */     String filename = sessionFilename(window);
/* 157:328 */     if (filename != null) {
/* 158:    */       try
/* 159:    */       {
/* 160:330 */         getContext().getSessionStorage().save(window, filename);
/* 161:    */       }
/* 162:    */       catch (IOException e)
/* 163:    */       {
/* 164:332 */         logger.log(Level.WARNING, "couldn't save session", e);
/* 165:    */       }
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   private boolean isVisibleWindow(Window w)
/* 170:    */   {
/* 171:338 */     return (w.isVisible()) && (((w instanceof JFrame)) || ((w instanceof JDialog)) || ((w instanceof JWindow)));
/* 172:    */   }
/* 173:    */   
/* 174:    */   private List<Window> getVisibleSecondaryWindows()
/* 175:    */   {
/* 176:347 */     List<Window> rv = new ArrayList();
/* 177:349 */     for (Window window : Window.getWindows()) {
/* 178:350 */       if (isVisibleWindow(window)) {
/* 179:351 */         rv.add(window);
/* 180:    */       }
/* 181:    */     }
/* 182:354 */     return rv;
/* 183:    */   }
/* 184:    */   
/* 185:    */   protected void shutdown()
/* 186:    */   {
/* 187:364 */     if (isReady()) {
/* 188:365 */       for (Window window : getVisibleSecondaryWindows()) {
/* 189:366 */         saveSession(window);
/* 190:    */       }
/* 191:    */     }
/* 192:    */   }
/* 193:    */   
/* 194:    */   private class MainFrameListener
/* 195:    */     extends WindowAdapter
/* 196:    */   {
/* 197:    */     private MainFrameListener() {}
/* 198:    */     
/* 199:    */     public void windowClosing(WindowEvent e)
/* 200:    */     {
/* 201:375 */       SingleFrameApplication.this.exit(e);
/* 202:    */     }
/* 203:    */   }
/* 204:    */   
/* 205:    */   private class SecondaryWindowListener
/* 206:    */     implements HierarchyListener
/* 207:    */   {
/* 208:    */     private SecondaryWindowListener() {}
/* 209:    */     
/* 210:    */     public void hierarchyChanged(HierarchyEvent e)
/* 211:    */     {
/* 212:391 */       if (((e.getChangeFlags() & 0x4) != 0L) && 
/* 213:392 */         ((e.getSource() instanceof Window)))
/* 214:    */       {
/* 215:393 */         Window secondaryWindow = (Window)e.getSource();
/* 216:394 */         if (!secondaryWindow.isShowing()) {
/* 217:395 */           SingleFrameApplication.this.saveSession(secondaryWindow);
/* 218:    */         }
/* 219:    */       }
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   private static class FrameBoundsListener
/* 224:    */     implements ComponentListener
/* 225:    */   {
/* 226:    */     private void maybeSaveFrameSize(ComponentEvent e)
/* 227:    */     {
/* 228:410 */       if ((e.getComponent() instanceof JFrame))
/* 229:    */       {
/* 230:411 */         JFrame f = (JFrame)e.getComponent();
/* 231:412 */         if ((f.getExtendedState() & 0x6) == 0) {
/* 232:413 */           SwingHelper.putWindowNormalBounds(f, f.getBounds());
/* 233:    */         }
/* 234:    */       }
/* 235:    */     }
/* 236:    */     
/* 237:    */     public void componentResized(ComponentEvent e)
/* 238:    */     {
/* 239:420 */       maybeSaveFrameSize(e);
/* 240:    */     }
/* 241:    */     
/* 242:    */     public void componentMoved(ComponentEvent e) {}
/* 243:    */     
/* 244:    */     public void componentHidden(ComponentEvent e) {}
/* 245:    */     
/* 246:    */     public void componentShown(ComponentEvent e) {}
/* 247:    */   }
/* 248:    */   
/* 249:435 */   private FrameView mainView = null;
/* 250:    */   
/* 251:    */   public FrameView getMainView()
/* 252:    */   {
/* 253:442 */     if (this.mainView == null) {
/* 254:443 */       this.mainView = new FrameView(this);
/* 255:    */     }
/* 256:445 */     return this.mainView;
/* 257:    */   }
/* 258:    */   
/* 259:    */   public void show(View view)
/* 260:    */   {
/* 261:450 */     FrameView fv = null;
/* 262:451 */     JFrame f = null;
/* 263:452 */     int initialExtendedState = 0;
/* 264:453 */     if ((view instanceof FrameView))
/* 265:    */     {
/* 266:454 */       fv = (FrameView)view;
/* 267:455 */       if (this.mainView == null) {
/* 268:456 */         this.mainView = fv;
/* 269:    */       }
/* 270:458 */       f = fv.getFrame();
/* 271:459 */       initialExtendedState = f.getExtendedState();
/* 272:460 */       f.setExtendedState(0);
/* 273:    */     }
/* 274:462 */     RootPaneContainer c = (RootPaneContainer)view.getRootPane().getParent();
/* 275:463 */     initRootPaneContainer(c);
/* 276:464 */     if (f != null) {
/* 277:465 */       SwingHelper.putWindowNormalBounds(f, f.getBounds());
/* 278:    */     }
/* 279:468 */     if (initialExtendedState != 0) {
/* 280:469 */       fv.getFrame().setExtendedState(initialExtendedState);
/* 281:    */     }
/* 282:471 */     ((Window)c).setVisible(true);
/* 283:    */   }
/* 284:    */   
/* 285:    */   protected void end()
/* 286:    */   {
/* 287:476 */     JFrame mainFrame = getMainFrame();
/* 288:477 */     if ((mainFrame != null) || (mainFrame.isDisplayable()))
/* 289:    */     {
/* 290:478 */       mainFrame.setVisible(false);
/* 291:479 */       mainFrame.dispose();
/* 292:    */     }
/* 293:481 */     super.end();
/* 294:    */   }
/* 295:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.SingleFrameApplication
 * JD-Core Version:    0.7.0.1
 */