/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.Dialog;
/*   5:    */ import java.awt.Dimension;
/*   6:    */ import java.awt.Frame;
/*   7:    */ import java.awt.event.ItemEvent;
/*   8:    */ import java.awt.event.ItemListener;
/*   9:    */ import java.util.ResourceBundle;
/*  10:    */ import javax.swing.ActionMap;
/*  11:    */ import javax.swing.GroupLayout;
/*  12:    */ import javax.swing.GroupLayout.Alignment;
/*  13:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  14:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  15:    */ import javax.swing.Icon;
/*  16:    */ import javax.swing.JButton;
/*  17:    */ import javax.swing.JCheckBox;
/*  18:    */ import javax.swing.JDialog;
/*  19:    */ import javax.swing.JLabel;
/*  20:    */ import javax.swing.JPanel;
/*  21:    */ import javax.swing.JSlider;
/*  22:    */ import javax.swing.JToggleButton;
/*  23:    */ import javax.swing.JToolBar;
/*  24:    */ import javax.swing.JToolBar.Separator;
/*  25:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  26:    */ import javax.swing.SwingUtilities;
/*  27:    */ import javax.swing.event.ChangeEvent;
/*  28:    */ import javax.swing.event.ChangeListener;
/*  29:    */ import org.jdesktop.application.Action;
/*  30:    */ import org.jdesktop.application.Application;
/*  31:    */ import org.jdesktop.application.ApplicationContext;
/*  32:    */ import org.jdesktop.application.ResourceMap;
/*  33:    */ 
/*  34:    */ public final class FlyThruControls
/*  35:    */   extends JDialog
/*  36:    */   implements AnimationControls
/*  37:    */ {
/*  38:    */   private final FlyThruAnimation animation;
/*  39:    */   private boolean dropped;
/*  40:    */   private int panelHeight;
/*  41: 32 */   private boolean visibleState = true;
/*  42: 33 */   private boolean initialized = false;
/*  43:    */   private Icon playIcon;
/*  44:    */   private Icon pauseIcon;
/*  45:    */   private static final String animationDialogStorage = "flyThruAnimationControlsState.xml";
/*  46:    */   private JCheckBox abutmentsCheckBox;
/*  47:    */   private JButton animationControlsDialogDropButton;
/*  48:    */   private JPanel animationControlsPanel;
/*  49:    */   private JToolBar.Separator animationControlsSep02;
/*  50:    */   private JToolBar animationControlsToolbar;
/*  51:    */   private JLabel brightLabel;
/*  52:    */   private JSlider brightnessSlider;
/*  53:    */   private JCheckBox colorsCheckBox;
/*  54:    */   private JLabel dimLabel;
/*  55:    */   private JCheckBox erosionCheckbox;
/*  56:    */   private JCheckBox exaggerationCheckBox;
/*  57:    */   private JLabel lightLabel;
/*  58:    */   private JToggleButton playButton;
/*  59:    */   private JButton resetButton;
/*  60:    */   private JToolBar.Separator sep100;
/*  61:    */   private JCheckBox shadowsCheckBox;
/*  62:    */   private JCheckBox skyCheckBox;
/*  63:    */   private JLabel speedLabel;
/*  64:    */   private JSlider speedSlider;
/*  65:    */   private JCheckBox terrainCheckBox;
/*  66:    */   private JCheckBox truckCheckBox;
/*  67:    */   
/*  68:    */   public FlyThruControls(Frame parent, FlyThruAnimation animation)
/*  69:    */   {
/*  70: 40 */     super(parent);
/*  71: 41 */     this.animation = animation;
/*  72: 42 */     initComponents();
/*  73: 43 */     this.playIcon = WPBDApp.getApplication().getIconResource("play.png");
/*  74: 44 */     this.pauseIcon = WPBDApp.getApplication().getIconResource("pause.png");
/*  75:    */   }
/*  76:    */   
/*  77:    */   public Dialog getDialog()
/*  78:    */   {
/*  79: 53 */     return this;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean getVisibleState()
/*  83:    */   {
/*  84: 57 */     return this.visibleState;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void saveVisibilityAndHide()
/*  88:    */   {
/*  89: 61 */     saveState();
/*  90: 62 */     this.visibleState = isVisible();
/*  91: 63 */     setVisible(false);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void startAnimation()
/*  95:    */   {
/*  96: 67 */     this.playButton.setIcon(this.pauseIcon);
/*  97: 68 */     this.animation.getConfig().paused = false;
/*  98: 69 */     if (this.visibleState)
/*  99:    */     {
/* 100: 70 */       if (!this.initialized)
/* 101:    */       {
/* 102: 71 */         setLocation(this.animation.getCanvas().getLocationOnScreen());
/* 103: 72 */         this.panelHeight = this.animationControlsPanel.getHeight();
/* 104: 73 */         setSize(getWidth(), getHeight() - this.panelHeight);
/* 105: 74 */         this.dropped = false;
/* 106: 75 */         this.visibleState = true;
/* 107: 76 */         restoreState();
/* 108: 77 */         this.initialized = true;
/* 109:    */       }
/* 110: 80 */       SwingUtilities.invokeLater(new Runnable()
/* 111:    */       {
/* 112:    */         public void run()
/* 113:    */         {
/* 114: 82 */           FlyThruControls.this.restoreState();
/* 115: 83 */           FlyThruControls.this.setVisible(true);
/* 116: 84 */           FlyThruControls.this.animation.start();
/* 117: 85 */           FlyThruControls.this.animation.getCanvas().requestFocusInWindow();
/* 118:    */         }
/* 119:    */       });
/* 120:    */     }
/* 121:    */     else
/* 122:    */     {
/* 123: 90 */       this.animation.start();
/* 124: 91 */       this.animation.getCanvas().requestFocusInWindow();
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   private JCheckBox[] getAnimationControlsCheckBoxes()
/* 129:    */   {
/* 130:101 */     return new JCheckBox[] { this.shadowsCheckBox, this.skyCheckBox, this.terrainCheckBox, this.abutmentsCheckBox, this.colorsCheckBox, this.erosionCheckbox, this.exaggerationCheckBox, this.truckCheckBox };
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void restoreState()
/* 134:    */   {
/* 135:114 */     ComponentStateLocalStorable s = ComponentStateLocalStorable.load("flyThruAnimationControlsState.xml");
/* 136:115 */     if (s != null)
/* 137:    */     {
/* 138:116 */       s.apply(getAnimationControlsCheckBoxes());
/* 139:117 */       s.apply(new JSlider[] { this.speedSlider, this.brightnessSlider });
/* 140:    */     }
/* 141:120 */     if (!this.animation.getConfig().canShowShadows)
/* 142:    */     {
/* 143:121 */       this.shadowsCheckBox.setSelected(false);
/* 144:122 */       this.shadowsCheckBox.setEnabled(false);
/* 145:    */     }
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void saveState()
/* 149:    */   {
/* 150:128 */     ComponentStateLocalStorable s = new ComponentStateLocalStorable();
/* 151:129 */     s.add(getAnimationControlsCheckBoxes());
/* 152:130 */     s.add(new JSlider[] { this.speedSlider, this.brightnessSlider });
/* 153:131 */     s.save("flyThruAnimationControlsState.xml");
/* 154:    */   }
/* 155:    */   
/* 156:    */   private void initComponents()
/* 157:    */   {
/* 158:143 */     this.animationControlsToolbar = new JToolBar();
/* 159:144 */     this.resetButton = new JButton();
/* 160:145 */     this.playButton = new JToggleButton();
/* 161:146 */     this.sep100 = new JToolBar.Separator();
/* 162:147 */     this.speedSlider = new JSlider();
/* 163:148 */     this.animationControlsSep02 = new JToolBar.Separator();
/* 164:149 */     this.animationControlsDialogDropButton = new JButton();
/* 165:150 */     this.animationControlsPanel = new JPanel();
/* 166:151 */     this.shadowsCheckBox = new JCheckBox();
/* 167:152 */     this.terrainCheckBox = new JCheckBox();
/* 168:153 */     this.skyCheckBox = new JCheckBox();
/* 169:154 */     this.speedLabel = new JLabel();
/* 170:155 */     this.colorsCheckBox = new JCheckBox();
/* 171:156 */     this.exaggerationCheckBox = new JCheckBox();
/* 172:157 */     this.abutmentsCheckBox = new JCheckBox();
/* 173:158 */     this.truckCheckBox = new JCheckBox();
/* 174:159 */     this.erosionCheckbox = new JCheckBox();
/* 175:160 */     this.brightnessSlider = new JSlider(1);
/* 176:161 */     this.lightLabel = new JLabel();
/* 177:162 */     this.dimLabel = new JLabel();
/* 178:163 */     this.brightLabel = new JLabel();
/* 179:    */     
/* 180:165 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(FlyThruControls.class);
/* 181:166 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/* 182:167 */     setFocusable(false);
/* 183:168 */     setFocusableWindowState(false);
/* 184:169 */     setIconImage(WPBDApp.getApplication().getImageResource("animate.png"));
/* 185:170 */     setName("Form");
/* 186:171 */     setResizable(false);
/* 187:    */     
/* 188:173 */     this.animationControlsToolbar.setFloatable(false);
/* 189:174 */     this.animationControlsToolbar.setRollover(true);
/* 190:175 */     this.animationControlsToolbar.setName("animationControlsToolbar");
/* 191:    */     
/* 192:177 */     ActionMap actionMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getActionMap(FlyThruControls.class, this);
/* 193:178 */     this.resetButton.setAction(actionMap.get("reset"));
/* 194:179 */     this.resetButton.setIcon(resourceMap.getIcon("resetButton.icon"));
/* 195:180 */     this.resetButton.setFocusable(false);
/* 196:181 */     this.resetButton.setHideActionText(true);
/* 197:182 */     this.resetButton.setHorizontalTextPosition(0);
/* 198:183 */     this.resetButton.setName("resetButton");
/* 199:184 */     this.resetButton.setVerticalTextPosition(3);
/* 200:185 */     this.animationControlsToolbar.add(this.resetButton);
/* 201:    */     
/* 202:187 */     this.playButton.setAction(actionMap.get("playAnimation"));
/* 203:188 */     this.playButton.setToolTipText(resourceMap.getString("playButton.toolTipText", new Object[0]));
/* 204:189 */     this.playButton.setFocusable(false);
/* 205:190 */     this.playButton.setHideActionText(true);
/* 206:191 */     this.playButton.setHorizontalTextPosition(0);
/* 207:192 */     this.playButton.setName("playButton");
/* 208:193 */     this.playButton.setVerticalTextPosition(3);
/* 209:194 */     this.animationControlsToolbar.add(this.playButton);
/* 210:    */     
/* 211:196 */     this.sep100.setMaximumSize(new Dimension(8, 32767));
/* 212:197 */     this.sep100.setMinimumSize(new Dimension(8, 0));
/* 213:198 */     this.sep100.setName("sep100");
/* 214:199 */     this.sep100.setPreferredSize(new Dimension(8, 0));
/* 215:200 */     this.animationControlsToolbar.add(this.sep100);
/* 216:    */     
/* 217:202 */     this.speedSlider.setMajorTickSpacing(5);
/* 218:203 */     this.speedSlider.setMaximum(30);
/* 219:204 */     this.speedSlider.setMinimum(5);
/* 220:205 */     this.speedSlider.setMinorTickSpacing(5);
/* 221:206 */     this.speedSlider.setPaintTicks(true);
/* 222:207 */     this.speedSlider.setSnapToTicks(true);
/* 223:208 */     ResourceBundle bundle = ResourceBundle.getBundle("wpbd/resources/WPBDView");
/* 224:209 */     this.speedSlider.setToolTipText(bundle.getString("animationSpeedTip"));
/* 225:210 */     this.speedSlider.setMaximumSize(new Dimension(120, 34));
/* 226:211 */     this.speedSlider.setMinimumSize(new Dimension(120, 34));
/* 227:212 */     this.speedSlider.setName("speedSlider");
/* 228:213 */     this.speedSlider.setPreferredSize(new Dimension(120, 34));
/* 229:214 */     this.speedSlider.addChangeListener(new ChangeListener()
/* 230:    */     {
/* 231:    */       public void stateChanged(ChangeEvent evt)
/* 232:    */       {
/* 233:216 */         FlyThruControls.this.speedSliderStateChanged(evt);
/* 234:    */       }
/* 235:218 */     });
/* 236:219 */     this.animationControlsToolbar.add(this.speedSlider);
/* 237:    */     
/* 238:221 */     this.animationControlsSep02.setMaximumSize(new Dimension(8, 32767));
/* 239:222 */     this.animationControlsSep02.setMinimumSize(new Dimension(8, 0));
/* 240:223 */     this.animationControlsSep02.setName("animationControlsSep02");
/* 241:224 */     this.animationControlsSep02.setPreferredSize(new Dimension(8, 0));
/* 242:225 */     this.animationControlsToolbar.add(this.animationControlsSep02);
/* 243:    */     
/* 244:227 */     this.animationControlsDialogDropButton.setAction(actionMap.get("toggleAnimationDrop"));
/* 245:228 */     this.animationControlsDialogDropButton.setHideActionText(true);
/* 246:229 */     this.animationControlsDialogDropButton.setMaximumSize(new Dimension(37, 37));
/* 247:230 */     this.animationControlsDialogDropButton.setMinimumSize(new Dimension(37, 37));
/* 248:231 */     this.animationControlsDialogDropButton.setName("animationControlsDialogDropButton");
/* 249:232 */     this.animationControlsDialogDropButton.setPreferredSize(new Dimension(37, 37));
/* 250:233 */     this.animationControlsToolbar.add(this.animationControlsDialogDropButton);
/* 251:    */     
/* 252:235 */     this.animationControlsPanel.setName("animationControlsPanel");
/* 253:    */     
/* 254:237 */     this.shadowsCheckBox.setText(resourceMap.getString("shadowsCheckBox.text", new Object[0]));
/* 255:238 */     this.shadowsCheckBox.setName("shadowsCheckBox");
/* 256:239 */     this.shadowsCheckBox.addItemListener(new ItemListener()
/* 257:    */     {
/* 258:    */       public void itemStateChanged(ItemEvent evt)
/* 259:    */       {
/* 260:241 */         FlyThruControls.this.shadowsCheckBoxItemStateChanged(evt);
/* 261:    */       }
/* 262:244 */     });
/* 263:245 */     this.terrainCheckBox.setSelected(true);
/* 264:246 */     this.terrainCheckBox.setText(resourceMap.getString("terrainCheckBox.text", new Object[0]));
/* 265:247 */     this.terrainCheckBox.setName("terrainCheckBox");
/* 266:248 */     this.terrainCheckBox.addItemListener(new ItemListener()
/* 267:    */     {
/* 268:    */       public void itemStateChanged(ItemEvent evt)
/* 269:    */       {
/* 270:250 */         FlyThruControls.this.terrainCheckBoxItemStateChanged(evt);
/* 271:    */       }
/* 272:253 */     });
/* 273:254 */     this.skyCheckBox.setSelected(true);
/* 274:255 */     this.skyCheckBox.setText(resourceMap.getString("skyCheckBox.text", new Object[0]));
/* 275:256 */     this.skyCheckBox.setName("skyCheckBox");
/* 276:257 */     this.skyCheckBox.addItemListener(new ItemListener()
/* 277:    */     {
/* 278:    */       public void itemStateChanged(ItemEvent evt)
/* 279:    */       {
/* 280:259 */         FlyThruControls.this.skyCheckBoxItemStateChanged(evt);
/* 281:    */       }
/* 282:262 */     });
/* 283:263 */     this.speedLabel.setHorizontalAlignment(0);
/* 284:264 */     this.speedLabel.setText(resourceMap.getString("speedLabel.text", new Object[0]));
/* 285:265 */     this.speedLabel.setMaximumSize(new Dimension(64, 16));
/* 286:266 */     this.speedLabel.setMinimumSize(new Dimension(64, 16));
/* 287:267 */     this.speedLabel.setName("speedLabel");
/* 288:268 */     this.speedLabel.setPreferredSize(new Dimension(64, 16));
/* 289:    */     
/* 290:270 */     this.colorsCheckBox.setSelected(true);
/* 291:271 */     this.colorsCheckBox.setText(resourceMap.getString("colorsCheckBox.text", new Object[0]));
/* 292:272 */     this.colorsCheckBox.setName("colorsCheckBox");
/* 293:273 */     this.colorsCheckBox.addItemListener(new ItemListener()
/* 294:    */     {
/* 295:    */       public void itemStateChanged(ItemEvent evt)
/* 296:    */       {
/* 297:275 */         FlyThruControls.this.colorsCheckBoxItemStateChanged(evt);
/* 298:    */       }
/* 299:278 */     });
/* 300:279 */     this.exaggerationCheckBox.setSelected(true);
/* 301:280 */     this.exaggerationCheckBox.setText(resourceMap.getString("exaggerationCheckBox.text", new Object[0]));
/* 302:281 */     this.exaggerationCheckBox.setName("exaggerationCheckBox");
/* 303:282 */     this.exaggerationCheckBox.addItemListener(new ItemListener()
/* 304:    */     {
/* 305:    */       public void itemStateChanged(ItemEvent evt)
/* 306:    */       {
/* 307:284 */         FlyThruControls.this.exaggerationCheckBoxItemStateChanged(evt);
/* 308:    */       }
/* 309:287 */     });
/* 310:288 */     this.abutmentsCheckBox.setSelected(true);
/* 311:289 */     this.abutmentsCheckBox.setText(resourceMap.getString("abutmentsCheckBox.text", new Object[0]));
/* 312:290 */     this.abutmentsCheckBox.setName("abutmentsCheckBox");
/* 313:291 */     this.abutmentsCheckBox.addItemListener(new ItemListener()
/* 314:    */     {
/* 315:    */       public void itemStateChanged(ItemEvent evt)
/* 316:    */       {
/* 317:293 */         FlyThruControls.this.abutmentsCheckBoxItemStateChanged(evt);
/* 318:    */       }
/* 319:296 */     });
/* 320:297 */     this.truckCheckBox.setSelected(true);
/* 321:298 */     this.truckCheckBox.setText(resourceMap.getString("truckCheckBox.text", new Object[0]));
/* 322:299 */     this.truckCheckBox.setName("truckCheckBox");
/* 323:300 */     this.truckCheckBox.addItemListener(new ItemListener()
/* 324:    */     {
/* 325:    */       public void itemStateChanged(ItemEvent evt)
/* 326:    */       {
/* 327:302 */         FlyThruControls.this.truckCheckBoxItemStateChanged(evt);
/* 328:    */       }
/* 329:305 */     });
/* 330:306 */     this.erosionCheckbox.setText(resourceMap.getString("erosionCheckbox.text", new Object[0]));
/* 331:307 */     this.erosionCheckbox.setName("erosionCheckbox");
/* 332:308 */     this.erosionCheckbox.addItemListener(new ItemListener()
/* 333:    */     {
/* 334:    */       public void itemStateChanged(ItemEvent evt)
/* 335:    */       {
/* 336:310 */         FlyThruControls.this.erosionCheckboxItemStateChanged(evt);
/* 337:    */       }
/* 338:313 */     });
/* 339:314 */     this.brightnessSlider.setOrientation(1);
/* 340:315 */     this.brightnessSlider.setName("brightnessSlider");
/* 341:316 */     this.brightnessSlider.addChangeListener(new ChangeListener()
/* 342:    */     {
/* 343:    */       public void stateChanged(ChangeEvent evt)
/* 344:    */       {
/* 345:318 */         FlyThruControls.this.brightnessSliderStateChanged(evt);
/* 346:    */       }
/* 347:321 */     });
/* 348:322 */     this.lightLabel.setText(resourceMap.getString("lightLabel.text", new Object[0]));
/* 349:323 */     this.lightLabel.setName("lightLabel");
/* 350:    */     
/* 351:325 */     this.dimLabel.setIcon(resourceMap.getIcon("dimLabel.icon"));
/* 352:326 */     this.dimLabel.setName("dimLabel");
/* 353:    */     
/* 354:328 */     this.brightLabel.setIcon(resourceMap.getIcon("brightLabel.icon"));
/* 355:329 */     this.brightLabel.setName("brightLabel");
/* 356:    */     
/* 357:331 */     GroupLayout animationControlsPanelLayout = new GroupLayout(this.animationControlsPanel);
/* 358:332 */     this.animationControlsPanel.setLayout(animationControlsPanelLayout);
/* 359:333 */     animationControlsPanelLayout.setHorizontalGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(animationControlsPanelLayout.createSequentialGroup().addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(animationControlsPanelLayout.createSequentialGroup().addContainerGap().addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.truckCheckBox, -1, -1, 32767).addComponent(this.shadowsCheckBox, -1, -1, 32767).addComponent(this.skyCheckBox, -1, -1, 32767).addComponent(this.terrainCheckBox, -1, -1, 32767).addComponent(this.erosionCheckbox, -1, -1, 32767).addComponent(this.abutmentsCheckBox, -1, -1, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.colorsCheckBox).addGroup(animationControlsPanelLayout.createSequentialGroup().addComponent(this.brightnessSlider, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.lightLabel, -2, 70, -2).addComponent(this.dimLabel).addComponent(this.brightLabel))).addComponent(this.exaggerationCheckBox, -1, 124, 32767))).addGroup(animationControlsPanelLayout.createSequentialGroup().addGap(100, 100, 100).addComponent(this.speedLabel, -2, -1, -2))).addContainerGap()));
/* 360:    */     
/* 361:    */ 
/* 362:    */ 
/* 363:    */ 
/* 364:    */ 
/* 365:    */ 
/* 366:    */ 
/* 367:    */ 
/* 368:    */ 
/* 369:    */ 
/* 370:    */ 
/* 371:    */ 
/* 372:    */ 
/* 373:    */ 
/* 374:    */ 
/* 375:    */ 
/* 376:    */ 
/* 377:    */ 
/* 378:    */ 
/* 379:    */ 
/* 380:    */ 
/* 381:    */ 
/* 382:    */ 
/* 383:    */ 
/* 384:    */ 
/* 385:    */ 
/* 386:    */ 
/* 387:    */ 
/* 388:362 */     animationControlsPanelLayout.setVerticalGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, animationControlsPanelLayout.createSequentialGroup().addComponent(this.speedLabel, -2, -1, -2).addGap(3, 3, 3).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(animationControlsPanelLayout.createSequentialGroup().addComponent(this.shadowsCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.skyCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.terrainCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.erosionCheckbox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.abutmentsCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.truckCheckBox)).addGroup(animationControlsPanelLayout.createSequentialGroup().addComponent(this.colorsCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.exaggerationCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(animationControlsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.brightnessSlider, -1, 97, 32767).addGroup(animationControlsPanelLayout.createSequentialGroup().addComponent(this.brightLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, 32767).addComponent(this.lightLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 9, 32767).addComponent(this.dimLabel))))).addContainerGap()));
/* 389:    */     
/* 390:    */ 
/* 391:    */ 
/* 392:    */ 
/* 393:    */ 
/* 394:    */ 
/* 395:    */ 
/* 396:    */ 
/* 397:    */ 
/* 398:    */ 
/* 399:    */ 
/* 400:    */ 
/* 401:    */ 
/* 402:    */ 
/* 403:    */ 
/* 404:    */ 
/* 405:    */ 
/* 406:    */ 
/* 407:    */ 
/* 408:    */ 
/* 409:    */ 
/* 410:    */ 
/* 411:    */ 
/* 412:    */ 
/* 413:    */ 
/* 414:    */ 
/* 415:    */ 
/* 416:    */ 
/* 417:    */ 
/* 418:    */ 
/* 419:    */ 
/* 420:    */ 
/* 421:    */ 
/* 422:396 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 423:397 */     getContentPane().setLayout(layout);
/* 424:398 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.animationControlsPanel, 0, -1, 32767).addComponent(this.animationControlsToolbar, -1, -1, 32767));
/* 425:    */     
/* 426:    */ 
/* 427:    */ 
/* 428:    */ 
/* 429:403 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.animationControlsToolbar, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.animationControlsPanel, -1, -1, 32767)));
/* 430:    */     
/* 431:    */ 
/* 432:    */ 
/* 433:    */ 
/* 434:    */ 
/* 435:    */ 
/* 436:    */ 
/* 437:411 */     pack();
/* 438:    */   }
/* 439:    */   
/* 440:    */   private void speedSliderStateChanged(ChangeEvent evt)
/* 441:    */   {
/* 442:415 */     int speed = this.speedSlider.getValue();
/* 443:416 */     this.speedLabel.setText(Integer.toString(speed) + " km/hr");
/* 444:417 */     this.animation.getConfig().truckSpeed = speed;
/* 445:    */   }
/* 446:    */   
/* 447:    */   private void shadowsCheckBoxItemStateChanged(ItemEvent evt)
/* 448:    */   {
/* 449:421 */     this.animation.getConfig().showShadows = (evt.getStateChange() == 1);
/* 450:    */   }
/* 451:    */   
/* 452:    */   private void terrainCheckBoxItemStateChanged(ItemEvent evt)
/* 453:    */   {
/* 454:425 */     this.animation.getConfig().showTerrain = (evt.getStateChange() == 1);
/* 455:    */   }
/* 456:    */   
/* 457:    */   private void skyCheckBoxItemStateChanged(ItemEvent evt)
/* 458:    */   {
/* 459:429 */     this.animation.getConfig().showSky = (evt.getStateChange() == 1);
/* 460:    */   }
/* 461:    */   
/* 462:    */   private void colorsCheckBoxItemStateChanged(ItemEvent evt)
/* 463:    */   {
/* 464:433 */     this.animation.getConfig().showForcesAsColors = (evt.getStateChange() == 1);
/* 465:    */   }
/* 466:    */   
/* 467:    */   private void exaggerationCheckBoxItemStateChanged(ItemEvent evt)
/* 468:    */   {
/* 469:437 */     this.animation.getConfig().displacementExaggeration = (evt.getStateChange() == 1 ? 30.0D : 1.0D);
/* 470:    */   }
/* 471:    */   
/* 472:    */   private void abutmentsCheckBoxItemStateChanged(ItemEvent evt)
/* 473:    */   {
/* 474:441 */     this.animation.getConfig().showAbutments = (evt.getStateChange() == 1);
/* 475:    */   }
/* 476:    */   
/* 477:    */   private void truckCheckBoxItemStateChanged(ItemEvent evt)
/* 478:    */   {
/* 479:445 */     this.animation.getConfig().showTruck = (evt.getStateChange() == 1);
/* 480:    */   }
/* 481:    */   
/* 482:    */   private void erosionCheckboxItemStateChanged(ItemEvent evt)
/* 483:    */   {
/* 484:449 */     this.animation.getConfig().showErrosion = (evt.getStateChange() == 1);
/* 485:    */   }
/* 486:    */   
/* 487:    */   private void brightnessSliderStateChanged(ChangeEvent evt)
/* 488:    */   {
/* 489:453 */     int brightness = this.brightnessSlider.getValue();
/* 490:454 */     float[] lightBrightness = this.animation.getConfig().lightBrightness; float 
/* 491:455 */       tmp41_40 = (lightBrightness[2] = 0.2F + brightness * 0.01F * 0.6F);lightBrightness[1] = tmp41_40;lightBrightness[0] = tmp41_40;
/* 492:    */   }
/* 493:    */   
/* 494:    */   @Action
/* 495:    */   public void toggleAnimationDrop()
/* 496:    */   {
/* 497:460 */     Dimension size = getSize();
/* 498:461 */     if (this.dropped)
/* 499:    */     {
/* 500:462 */       size.height -= this.panelHeight;
/* 501:463 */       this.animationControlsPanel.setVisible(false);
/* 502:464 */       setSize(size);
/* 503:465 */       this.animationControlsDialogDropButton.setIcon(WPBDApp.getApplication().getIconResource("drop.png"));
/* 504:466 */       this.dropped = false;
/* 505:    */     }
/* 506:    */     else
/* 507:    */     {
/* 508:469 */       size.height += this.panelHeight;
/* 509:470 */       setSize(size);
/* 510:471 */       this.animationControlsPanel.setVisible(true);
/* 511:472 */       this.animationControlsDialogDropButton.setIcon(WPBDApp.getApplication().getIconResource("undrop.png"));
/* 512:473 */       this.dropped = true;
/* 513:    */     }
/* 514:    */   }
/* 515:    */   
/* 516:    */   @Action
/* 517:    */   public void playAnimation()
/* 518:    */   {
/* 519:479 */     if (this.playButton.getIcon() == this.playIcon)
/* 520:    */     {
/* 521:480 */       this.playButton.setIcon(this.pauseIcon);
/* 522:481 */       this.animation.getConfig().paused = false;
/* 523:    */     }
/* 524:    */     else
/* 525:    */     {
/* 526:484 */       this.playButton.setIcon(this.playIcon);
/* 527:485 */       this.animation.getConfig().paused = true;
/* 528:    */     }
/* 529:    */   }
/* 530:    */   
/* 531:    */   @Action
/* 532:    */   public void reset()
/* 533:    */   {
/* 534:491 */     this.animation.reset();
/* 535:    */   }
/* 536:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FlyThruControls
 * JD-Core Version:    0.7.0.1
 */