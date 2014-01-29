/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Canvas;
/*   4:    */ import java.awt.Container;
/*   5:    */ import java.awt.Dialog;
/*   6:    */ import java.awt.Dimension;
/*   7:    */ import java.awt.Frame;
/*   8:    */ import java.awt.event.ItemEvent;
/*   9:    */ import java.awt.event.ItemListener;
/*  10:    */ import java.util.ResourceBundle;
/*  11:    */ import javax.swing.ActionMap;
/*  12:    */ import javax.swing.GroupLayout;
/*  13:    */ import javax.swing.GroupLayout.Alignment;
/*  14:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  15:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  16:    */ import javax.swing.Icon;
/*  17:    */ import javax.swing.JButton;
/*  18:    */ import javax.swing.JCheckBox;
/*  19:    */ import javax.swing.JDialog;
/*  20:    */ import javax.swing.JLabel;
/*  21:    */ import javax.swing.JPanel;
/*  22:    */ import javax.swing.JSlider;
/*  23:    */ import javax.swing.JToggleButton;
/*  24:    */ import javax.swing.JToolBar;
/*  25:    */ import javax.swing.JToolBar.Separator;
/*  26:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  27:    */ import javax.swing.SwingUtilities;
/*  28:    */ import javax.swing.event.ChangeEvent;
/*  29:    */ import javax.swing.event.ChangeListener;
/*  30:    */ import org.jdesktop.application.Action;
/*  31:    */ import org.jdesktop.application.Application;
/*  32:    */ import org.jdesktop.application.ApplicationContext;
/*  33:    */ import org.jdesktop.application.ResourceMap;
/*  34:    */ 
/*  35:    */ public final class FixedEyeControls
/*  36:    */   extends JDialog
/*  37:    */   implements AnimationControls
/*  38:    */ {
/*  39:    */   private final FixedEyeAnimation animation;
/*  40:    */   private boolean dropped;
/*  41:    */   private int panelHeight;
/*  42: 31 */   private boolean visibleState = true;
/*  43: 32 */   private boolean initialized = false;
/*  44:    */   private Icon playIcon;
/*  45:    */   private Icon pauseIcon;
/*  46:    */   private static final String animationDialogStorage = "fixedEyeAnimationControlsState.xml";
/*  47:    */   private JCheckBox abutmentsCheckBox;
/*  48:    */   private JButton animationControlsDialogDropButton;
/*  49:    */   private JPanel animationControlsPanel;
/*  50:    */   private JToolBar.Separator animationControlsSep02;
/*  51:    */   private JToolBar animationControlsToolbar;
/*  52:    */   private JCheckBox backgroundCheckBox;
/*  53:    */   private JCheckBox colorsCheckBox;
/*  54:    */   private JCheckBox exaggerationCheckBox;
/*  55:    */   private JToggleButton playButton;
/*  56:    */   private JButton resetButton;
/*  57:    */   private JToolBar.Separator sep100;
/*  58:    */   private JCheckBox smoothTerrainCheckBox;
/*  59:    */   private JLabel speedLabel;
/*  60:    */   private JSlider speedSlider;
/*  61:    */   private JCheckBox truckCheckBox;
/*  62:    */   
/*  63:    */   public FixedEyeControls(Frame parent, FixedEyeAnimation animation)
/*  64:    */   {
/*  65: 39 */     super(parent);
/*  66: 40 */     this.animation = animation;
/*  67: 41 */     initComponents();
/*  68: 42 */     this.playIcon = WPBDApp.getApplication().getIconResource("play.png");
/*  69: 43 */     this.pauseIcon = WPBDApp.getApplication().getIconResource("pause.png");
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Dialog getDialog()
/*  73:    */   {
/*  74: 52 */     return this;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public boolean getVisibleState()
/*  78:    */   {
/*  79: 56 */     return this.visibleState;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void saveVisibilityAndHide()
/*  83:    */   {
/*  84: 60 */     saveState();
/*  85: 61 */     this.visibleState = isVisible();
/*  86: 62 */     setVisible(false);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void startAnimation()
/*  90:    */   {
/*  91: 66 */     this.playButton.setIcon(this.pauseIcon);
/*  92: 67 */     this.animation.getConfig().paused = false;
/*  93: 68 */     if (this.visibleState)
/*  94:    */     {
/*  95: 69 */       if (!this.initialized)
/*  96:    */       {
/*  97: 70 */         setLocation(this.animation.getCanvas().getLocationOnScreen());
/*  98: 71 */         this.panelHeight = this.animationControlsPanel.getHeight();
/*  99: 72 */         setSize(getWidth(), getHeight() - this.panelHeight);
/* 100: 73 */         this.dropped = false;
/* 101: 74 */         this.visibleState = true;
/* 102: 75 */         restoreState();
/* 103: 76 */         this.initialized = true;
/* 104:    */       }
/* 105: 79 */       SwingUtilities.invokeLater(new Runnable()
/* 106:    */       {
/* 107:    */         public void run()
/* 108:    */         {
/* 109: 81 */           FixedEyeControls.this.restoreState();
/* 110: 82 */           FixedEyeControls.this.setVisible(true);
/* 111: 83 */           FixedEyeControls.this.animation.start();
/* 112: 84 */           FixedEyeControls.this.animation.getCanvas().requestFocusInWindow();
/* 113:    */         }
/* 114:    */       });
/* 115:    */     }
/* 116:    */     else
/* 117:    */     {
/* 118: 89 */       this.animation.start();
/* 119: 90 */       this.animation.getCanvas().requestFocusInWindow();
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   private JCheckBox[] getAnimationControlsCheckBoxes()
/* 124:    */   {
/* 125:100 */     return new JCheckBox[] { this.backgroundCheckBox, this.abutmentsCheckBox, this.colorsCheckBox, this.exaggerationCheckBox, this.truckCheckBox };
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void restoreState()
/* 129:    */   {
/* 130:110 */     ComponentStateLocalStorable s = ComponentStateLocalStorable.load("fixedEyeAnimationControlsState.xml");
/* 131:111 */     if (s != null) {
/* 132:112 */       s.apply(getAnimationControlsCheckBoxes());
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void saveState()
/* 137:    */   {
/* 138:118 */     ComponentStateLocalStorable s = new ComponentStateLocalStorable();
/* 139:119 */     s.add(getAnimationControlsCheckBoxes());
/* 140:120 */     s.save("fixedEyeAnimationControlsState.xml");
/* 141:    */   }
/* 142:    */   
/* 143:    */   private void initComponents()
/* 144:    */   {
/* 145:132 */     this.animationControlsToolbar = new JToolBar();
/* 146:133 */     this.resetButton = new JButton();
/* 147:134 */     this.playButton = new JToggleButton();
/* 148:135 */     this.sep100 = new JToolBar.Separator();
/* 149:136 */     this.speedSlider = new JSlider();
/* 150:137 */     this.animationControlsSep02 = new JToolBar.Separator();
/* 151:138 */     this.animationControlsDialogDropButton = new JButton();
/* 152:139 */     this.animationControlsPanel = new JPanel();
/* 153:140 */     this.backgroundCheckBox = new JCheckBox();
/* 154:141 */     this.speedLabel = new JLabel();
/* 155:142 */     this.colorsCheckBox = new JCheckBox();
/* 156:143 */     this.exaggerationCheckBox = new JCheckBox();
/* 157:144 */     this.smoothTerrainCheckBox = new JCheckBox();
/* 158:145 */     this.abutmentsCheckBox = new JCheckBox();
/* 159:146 */     this.truckCheckBox = new JCheckBox();
/* 160:    */     
/* 161:148 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(FixedEyeControls.class);
/* 162:149 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/* 163:150 */     setFocusable(false);
/* 164:151 */     setFocusableWindowState(false);
/* 165:152 */     setIconImage(WPBDApp.getApplication().getImageResource("animate.png"));
/* 166:153 */     setName("Form");
/* 167:154 */     setResizable(false);
/* 168:    */     
/* 169:156 */     this.animationControlsToolbar.setFloatable(false);
/* 170:157 */     this.animationControlsToolbar.setRollover(true);
/* 171:158 */     this.animationControlsToolbar.setName("animationControlsToolbar");
/* 172:    */     
/* 173:160 */     ActionMap actionMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getActionMap(FixedEyeControls.class, this);
/* 174:161 */     this.resetButton.setAction(actionMap.get("reset"));
/* 175:162 */     this.resetButton.setIcon(resourceMap.getIcon("resetButton.icon"));
/* 176:163 */     this.resetButton.setFocusable(false);
/* 177:164 */     this.resetButton.setHideActionText(true);
/* 178:165 */     this.resetButton.setHorizontalTextPosition(0);
/* 179:166 */     this.resetButton.setName("resetButton");
/* 180:167 */     this.resetButton.setVerticalTextPosition(3);
/* 181:168 */     this.animationControlsToolbar.add(this.resetButton);
/* 182:    */     
/* 183:170 */     this.playButton.setAction(actionMap.get("playAnimation"));
/* 184:171 */     this.playButton.setFocusable(false);
/* 185:172 */     this.playButton.setHideActionText(true);
/* 186:173 */     this.playButton.setHorizontalTextPosition(0);
/* 187:174 */     this.playButton.setName("playButton");
/* 188:175 */     this.playButton.setVerticalTextPosition(3);
/* 189:176 */     this.animationControlsToolbar.add(this.playButton);
/* 190:    */     
/* 191:178 */     this.sep100.setMaximumSize(new Dimension(8, 32767));
/* 192:179 */     this.sep100.setMinimumSize(new Dimension(8, 0));
/* 193:180 */     this.sep100.setName("sep100");
/* 194:181 */     this.sep100.setPreferredSize(new Dimension(8, 0));
/* 195:182 */     this.animationControlsToolbar.add(this.sep100);
/* 196:    */     
/* 197:184 */     this.speedSlider.setMajorTickSpacing(5);
/* 198:185 */     this.speedSlider.setMaximum(30);
/* 199:186 */     this.speedSlider.setMinimum(5);
/* 200:187 */     this.speedSlider.setMinorTickSpacing(5);
/* 201:188 */     this.speedSlider.setPaintTicks(true);
/* 202:189 */     this.speedSlider.setSnapToTicks(true);
/* 203:190 */     ResourceBundle bundle = ResourceBundle.getBundle("wpbd/resources/WPBDView");
/* 204:191 */     this.speedSlider.setToolTipText(bundle.getString("animationSpeedTip"));
/* 205:192 */     this.speedSlider.setMaximumSize(new Dimension(120, 34));
/* 206:193 */     this.speedSlider.setMinimumSize(new Dimension(120, 34));
/* 207:194 */     this.speedSlider.setName("speedSlider");
/* 208:195 */     this.speedSlider.setPreferredSize(new Dimension(120, 34));
/* 209:196 */     this.speedSlider.addChangeListener(new ChangeListener()
/* 210:    */     {
/* 211:    */       public void stateChanged(ChangeEvent evt)
/* 212:    */       {
/* 213:198 */         FixedEyeControls.this.speedSliderStateChanged(evt);
/* 214:    */       }
/* 215:200 */     });
/* 216:201 */     this.animationControlsToolbar.add(this.speedSlider);
/* 217:    */     
/* 218:203 */     this.animationControlsSep02.setMaximumSize(new Dimension(8, 32767));
/* 219:204 */     this.animationControlsSep02.setMinimumSize(new Dimension(8, 0));
/* 220:205 */     this.animationControlsSep02.setName("animationControlsSep02");
/* 221:206 */     this.animationControlsSep02.setPreferredSize(new Dimension(8, 0));
/* 222:207 */     this.animationControlsToolbar.add(this.animationControlsSep02);
/* 223:    */     
/* 224:209 */     this.animationControlsDialogDropButton.setAction(actionMap.get("toggleAnimationDrop"));
/* 225:210 */     this.animationControlsDialogDropButton.setHideActionText(true);
/* 226:211 */     this.animationControlsDialogDropButton.setMaximumSize(new Dimension(37, 37));
/* 227:212 */     this.animationControlsDialogDropButton.setMinimumSize(new Dimension(37, 37));
/* 228:213 */     this.animationControlsDialogDropButton.setName("animationControlsDialogDropButton");
/* 229:214 */     this.animationControlsDialogDropButton.setPreferredSize(new Dimension(37, 37));
/* 230:215 */     this.animationControlsToolbar.add(this.animationControlsDialogDropButton);
/* 231:    */     
/* 232:217 */     this.animationControlsPanel.setName("animationControlsPanel");
/* 233:    */     
/* 234:219 */     this.backgroundCheckBox.setSelected(true);
/* 235:220 */     this.backgroundCheckBox.setText(resourceMap.getString("backgroundCheckBox.text", new Object[0]));
/* 236:221 */     this.backgroundCheckBox.setActionCommand(resourceMap.getString("backgroundCheckBox.actionCommand", new Object[0]));
/* 237:222 */     this.backgroundCheckBox.setName("backgroundCheckBox");
/* 238:223 */     this.backgroundCheckBox.addItemListener(new ItemListener()
/* 239:    */     {
/* 240:    */       public void itemStateChanged(ItemEvent evt)
/* 241:    */       {
/* 242:225 */         FixedEyeControls.this.backgroundCheckBoxItemStateChanged(evt);
/* 243:    */       }
/* 244:228 */     });
/* 245:229 */     this.speedLabel.setHorizontalAlignment(0);
/* 246:230 */     this.speedLabel.setText(resourceMap.getString("speedLabel.text", new Object[0]));
/* 247:231 */     this.speedLabel.setMaximumSize(new Dimension(64, 16));
/* 248:232 */     this.speedLabel.setMinimumSize(new Dimension(64, 16));
/* 249:233 */     this.speedLabel.setName("speedLabel");
/* 250:234 */     this.speedLabel.setPreferredSize(new Dimension(64, 16));
/* 251:    */     
/* 252:236 */     this.colorsCheckBox.setSelected(true);
/* 253:237 */     this.colorsCheckBox.setText(resourceMap.getString("colorsCheckBox.text", new Object[0]));
/* 254:238 */     this.colorsCheckBox.setName("colorsCheckBox");
/* 255:239 */     this.colorsCheckBox.addItemListener(new ItemListener()
/* 256:    */     {
/* 257:    */       public void itemStateChanged(ItemEvent evt)
/* 258:    */       {
/* 259:241 */         FixedEyeControls.this.colorsCheckBoxItemStateChanged(evt);
/* 260:    */       }
/* 261:244 */     });
/* 262:245 */     this.exaggerationCheckBox.setSelected(true);
/* 263:246 */     this.exaggerationCheckBox.setText(resourceMap.getString("exaggerationCheckBox.text", new Object[0]));
/* 264:247 */     this.exaggerationCheckBox.setName("exaggerationCheckBox");
/* 265:248 */     this.exaggerationCheckBox.addItemListener(new ItemListener()
/* 266:    */     {
/* 267:    */       public void itemStateChanged(ItemEvent evt)
/* 268:    */       {
/* 269:250 */         FixedEyeControls.this.exaggerationCheckBoxItemStateChanged(evt);
/* 270:    */       }
/* 271:253 */     });
/* 272:254 */     this.smoothTerrainCheckBox.setSelected(true);
/* 273:255 */     this.smoothTerrainCheckBox.setText(resourceMap.getString("smoothTerrainCheckBox.text", new Object[0]));
/* 274:256 */     this.smoothTerrainCheckBox.setName("smoothTerrainCheckBox");
/* 275:257 */     this.smoothTerrainCheckBox.addItemListener(new ItemListener()
/* 276:    */     {
/* 277:    */       public void itemStateChanged(ItemEvent evt)
/* 278:    */       {
/* 279:259 */         FixedEyeControls.this.smoothTerrainCheckBoxItemStateChanged(evt);
/* 280:    */       }
/* 281:262 */     });
/* 282:263 */     this.abutmentsCheckBox.setSelected(true);
/* 283:264 */     this.abutmentsCheckBox.setText(resourceMap.getString("abutmentsCheckBox.text", new Object[0]));
/* 284:265 */     this.abutmentsCheckBox.setName("abutmentsCheckBox");
/* 285:266 */     this.abutmentsCheckBox.addItemListener(new ItemListener()
/* 286:    */     {
/* 287:    */       public void itemStateChanged(ItemEvent evt)
/* 288:    */       {
/* 289:268 */         FixedEyeControls.this.abutmentsCheckBoxItemStateChanged(evt);
/* 290:    */       }
/* 291:271 */     });
/* 292:272 */     this.truckCheckBox.setSelected(true);
/* 293:273 */     this.truckCheckBox.setText(resourceMap.getString("truckCheckBox.text", new Object[0]));
/* 294:274 */     this.truckCheckBox.setName("truckCheckBox");
/* 295:275 */     this.truckCheckBox.addItemListener(new ItemListener()
/* 296:    */     {
/* 297:    */       public void itemStateChanged(ItemEvent evt)
/* 298:    */       {
/* 299:277 */         FixedEyeControls.this.truckCheckBoxItemStateChanged(evt);
/* 300:    */       }
/* 301:280 */     });
/* 302:281 */     GroupLayout animationControlsPanelLayout = new GroupLayout(this.animationControlsPanel);
/* 303:282 */     this.animationControlsPanel.setLayout(animationControlsPanelLayout);
/* 304:283 */     animationControlsPanelLayout.setHorizontalGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(animationControlsPanelLayout.createSequentialGroup().addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(animationControlsPanelLayout.createSequentialGroup().addContainerGap().addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.backgroundCheckBox).addComponent(this.abutmentsCheckBox, -1, 105, 32767).addComponent(this.truckCheckBox, GroupLayout.Alignment.TRAILING, -1, 105, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.exaggerationCheckBox, -1, 112, 32767).addComponent(this.smoothTerrainCheckBox, -2, 110, -2).addGroup(animationControlsPanelLayout.createSequentialGroup().addComponent(this.colorsCheckBox, -1, 70, 32767).addGap(42, 42, 42)))).addGroup(animationControlsPanelLayout.createSequentialGroup().addGap(100, 100, 100).addComponent(this.speedLabel, -2, -1, -2))).addContainerGap()));
/* 305:    */     
/* 306:    */ 
/* 307:    */ 
/* 308:    */ 
/* 309:    */ 
/* 310:    */ 
/* 311:    */ 
/* 312:    */ 
/* 313:    */ 
/* 314:    */ 
/* 315:    */ 
/* 316:    */ 
/* 317:    */ 
/* 318:    */ 
/* 319:    */ 
/* 320:    */ 
/* 321:    */ 
/* 322:    */ 
/* 323:    */ 
/* 324:    */ 
/* 325:    */ 
/* 326:305 */     animationControlsPanelLayout.setVerticalGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, animationControlsPanelLayout.createSequentialGroup().addComponent(this.speedLabel, -2, -1, -2).addGap(3, 3, 3).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.backgroundCheckBox).addComponent(this.colorsCheckBox)).addGroup(animationControlsPanelLayout.createSequentialGroup().addGap(23, 23, 23).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.abutmentsCheckBox).addComponent(this.exaggerationCheckBox)))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.truckCheckBox).addComponent(this.smoothTerrainCheckBox)).addContainerGap()));
/* 327:    */     
/* 328:    */ 
/* 329:    */ 
/* 330:    */ 
/* 331:    */ 
/* 332:    */ 
/* 333:    */ 
/* 334:    */ 
/* 335:    */ 
/* 336:    */ 
/* 337:    */ 
/* 338:    */ 
/* 339:    */ 
/* 340:    */ 
/* 341:    */ 
/* 342:    */ 
/* 343:    */ 
/* 344:    */ 
/* 345:    */ 
/* 346:    */ 
/* 347:326 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 348:327 */     getContentPane().setLayout(layout);
/* 349:328 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.animationControlsToolbar, -1, -1, 32767).addComponent(this.animationControlsPanel, 0, -1, 32767));
/* 350:    */     
/* 351:    */ 
/* 352:    */ 
/* 353:    */ 
/* 354:333 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.animationControlsToolbar, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.animationControlsPanel, -1, -1, 32767)));
/* 355:    */     
/* 356:    */ 
/* 357:    */ 
/* 358:    */ 
/* 359:    */ 
/* 360:    */ 
/* 361:    */ 
/* 362:341 */     pack();
/* 363:    */   }
/* 364:    */   
/* 365:    */   private void speedSliderStateChanged(ChangeEvent evt)
/* 366:    */   {
/* 367:345 */     int speed = this.speedSlider.getValue();
/* 368:346 */     this.speedLabel.setText(Integer.toString(speed) + " km/hr");
/* 369:347 */     this.animation.getConfig().truckSpeed = speed;
/* 370:    */   }
/* 371:    */   
/* 372:    */   private void colorsCheckBoxItemStateChanged(ItemEvent evt)
/* 373:    */   {
/* 374:351 */     this.animation.getConfig().showForcesAsColors = (evt.getStateChange() == 1);
/* 375:    */   }
/* 376:    */   
/* 377:    */   private void exaggerationCheckBoxItemStateChanged(ItemEvent evt)
/* 378:    */   {
/* 379:355 */     this.animation.getConfig().displacementExaggeration = (evt.getStateChange() == 1 ? 30.0D : 1.0D);
/* 380:    */   }
/* 381:    */   
/* 382:    */   private void abutmentsCheckBoxItemStateChanged(ItemEvent evt)
/* 383:    */   {
/* 384:359 */     this.animation.getConfig().showAbutments = (evt.getStateChange() == 1);
/* 385:360 */     this.animation.invalidateBackground();
/* 386:    */   }
/* 387:    */   
/* 388:    */   private void truckCheckBoxItemStateChanged(ItemEvent evt)
/* 389:    */   {
/* 390:364 */     this.animation.getConfig().showTruck = (evt.getStateChange() == 1);
/* 391:    */   }
/* 392:    */   
/* 393:    */   private void backgroundCheckBoxItemStateChanged(ItemEvent evt)
/* 394:    */   {
/* 395:368 */     this.animation.getConfig().showBackground = (evt.getStateChange() == 1);
/* 396:369 */     this.animation.invalidateBackground();
/* 397:    */   }
/* 398:    */   
/* 399:    */   private void smoothTerrainCheckBoxItemStateChanged(ItemEvent evt)
/* 400:    */   {
/* 401:373 */     this.animation.getConfig().showSmoothTerrain = (evt.getStateChange() == 1);
/* 402:374 */     this.animation.invalidateBackground();
/* 403:    */   }
/* 404:    */   
/* 405:    */   @Action
/* 406:    */   public void toggleAnimationDrop()
/* 407:    */   {
/* 408:379 */     Dimension size = getSize();
/* 409:380 */     if (this.dropped)
/* 410:    */     {
/* 411:381 */       size.height -= this.panelHeight;
/* 412:382 */       this.animationControlsPanel.setVisible(false);
/* 413:383 */       setSize(size);
/* 414:384 */       this.animationControlsDialogDropButton.setIcon(WPBDApp.getApplication().getIconResource("drop.png"));
/* 415:385 */       this.dropped = false;
/* 416:    */     }
/* 417:    */     else
/* 418:    */     {
/* 419:388 */       size.height += this.panelHeight;
/* 420:389 */       setSize(size);
/* 421:390 */       this.animationControlsPanel.setVisible(true);
/* 422:391 */       this.animationControlsDialogDropButton.setIcon(WPBDApp.getApplication().getIconResource("undrop.png"));
/* 423:392 */       this.dropped = true;
/* 424:    */     }
/* 425:    */   }
/* 426:    */   
/* 427:    */   @Action
/* 428:    */   public void playAnimation()
/* 429:    */   {
/* 430:398 */     if (this.playButton.getIcon() == this.playIcon)
/* 431:    */     {
/* 432:399 */       this.playButton.setIcon(this.pauseIcon);
/* 433:400 */       this.animation.getConfig().paused = false;
/* 434:    */     }
/* 435:    */     else
/* 436:    */     {
/* 437:403 */       this.playButton.setIcon(this.playIcon);
/* 438:404 */       this.animation.getConfig().paused = true;
/* 439:    */     }
/* 440:    */   }
/* 441:    */   
/* 442:    */   @Action
/* 443:    */   public void reset()
/* 444:    */   {
/* 445:410 */     this.animation.reset();
/* 446:    */   }
/* 447:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FixedEyeControls
 * JD-Core Version:    0.7.0.1
 */