/*    1:     */ package wpbd;
/*    2:     */ 
/*    3:     */ import java.awt.Canvas;
/*    4:     */ import java.awt.CardLayout;
/*    5:     */ import java.awt.Color;
/*    6:     */ import java.awt.Container;
/*    7:     */ import java.awt.Cursor;
/*    8:     */ import java.awt.Dialog;
/*    9:     */ import java.awt.Dimension;
/*   10:     */ import java.awt.Font;
/*   11:     */ import java.awt.Frame;
/*   12:     */ import java.awt.Graphics;
/*   13:     */ import java.awt.GridBagConstraints;
/*   14:     */ import java.awt.GridBagLayout;
/*   15:     */ import java.awt.Image;
/*   16:     */ import java.awt.Insets;
/*   17:     */ import java.awt.KeyEventDispatcher;
/*   18:     */ import java.awt.KeyboardFocusManager;
/*   19:     */ import java.awt.Toolkit;
/*   20:     */ import java.awt.Window;
/*   21:     */ import java.awt.event.ActionEvent;
/*   22:     */ import java.awt.event.ActionListener;
/*   23:     */ import java.awt.event.ComponentAdapter;
/*   24:     */ import java.awt.event.ComponentEvent;
/*   25:     */ import java.awt.event.ItemEvent;
/*   26:     */ import java.awt.event.ItemListener;
/*   27:     */ import java.awt.event.KeyAdapter;
/*   28:     */ import java.awt.event.KeyEvent;
/*   29:     */ import java.io.File;
/*   30:     */ import java.io.IOException;
/*   31:     */ import java.text.NumberFormat;
/*   32:     */ import java.util.ArrayList;
/*   33:     */ import java.util.Locale;
/*   34:     */ import javax.accessibility.AccessibleContext;
/*   35:     */ import javax.help.HelpBroker;
/*   36:     */ import javax.swing.AbstractButton;
/*   37:     */ import javax.swing.ActionMap;
/*   38:     */ import javax.swing.BorderFactory;
/*   39:     */ import javax.swing.BoxLayout;
/*   40:     */ import javax.swing.ButtonGroup;
/*   41:     */ import javax.swing.GroupLayout;
/*   42:     */ import javax.swing.GroupLayout.Alignment;
/*   43:     */ import javax.swing.GroupLayout.ParallelGroup;
/*   44:     */ import javax.swing.GroupLayout.SequentialGroup;
/*   45:     */ import javax.swing.JButton;
/*   46:     */ import javax.swing.JCheckBox;
/*   47:     */ import javax.swing.JCheckBoxMenuItem;
/*   48:     */ import javax.swing.JComboBox;
/*   49:     */ import javax.swing.JDialog;
/*   50:     */ import javax.swing.JFileChooser;
/*   51:     */ import javax.swing.JFrame;
/*   52:     */ import javax.swing.JLabel;
/*   53:     */ import javax.swing.JMenu;
/*   54:     */ import javax.swing.JMenuBar;
/*   55:     */ import javax.swing.JMenuItem;
/*   56:     */ import javax.swing.JOptionPane;
/*   57:     */ import javax.swing.JPanel;
/*   58:     */ import javax.swing.JPopupMenu;
/*   59:     */ import javax.swing.JPopupMenu.Separator;
/*   60:     */ import javax.swing.JProgressBar;
/*   61:     */ import javax.swing.JRadioButtonMenuItem;
/*   62:     */ import javax.swing.JRootPane;
/*   63:     */ import javax.swing.JScrollPane;
/*   64:     */ import javax.swing.JSeparator;
/*   65:     */ import javax.swing.JTabbedPane;
/*   66:     */ import javax.swing.JTable;
/*   67:     */ import javax.swing.JTextField;
/*   68:     */ import javax.swing.JToggleButton;
/*   69:     */ import javax.swing.JToolBar;
/*   70:     */ import javax.swing.JToolBar.Separator;
/*   71:     */ import javax.swing.LayoutStyle.ComponentPlacement;
/*   72:     */ import javax.swing.OverlayLayout;
/*   73:     */ import javax.swing.SwingUtilities;
/*   74:     */ import javax.swing.event.ChangeEvent;
/*   75:     */ import javax.swing.event.ChangeListener;
/*   76:     */ import javax.swing.event.UndoableEditEvent;
/*   77:     */ import javax.swing.event.UndoableEditListener;
/*   78:     */ import javax.swing.filechooser.FileFilter;
/*   79:     */ import javax.swing.table.DefaultTableModel;
/*   80:     */ import javax.swing.table.TableColumn;
/*   81:     */ import javax.swing.table.TableColumnModel;
/*   82:     */ import jogamp.common.Debug;
/*   83:     */ import org.jdesktop.application.Application;
/*   84:     */ import org.jdesktop.application.ApplicationContext;
/*   85:     */ import org.jdesktop.application.FrameView;
/*   86:     */ import org.jdesktop.application.ResourceMap;
/*   87:     */ import org.netbeans.lib.awtextra.AbsoluteConstraints;
/*   88:     */ import org.netbeans.lib.awtextra.AbsoluteLayout;
/*   89:     */ 
/*   90:     */ public final class WPBDView
/*   91:     */   extends FrameView
/*   92:     */   implements ContextComponentProvider, RecentFileManager.Listener, KeyEventDispatcher, WelcomeDialog.AboutProvider
/*   93:     */ {
/*   94:     */   private EditableBridgeModel bridge;
/*   95:     */   private BridgeDraftingView bridgeDraftingView;
/*   96:     */   private Dispatcher dispatcher;
/*   97:     */   private EnabledStateManager enabledStateManager;
/*   98:     */   private static final int NO_BRIDGE_STATE = 0;
/*   99:     */   private static final int DRAFTING_STATE = 1;
/*  100:     */   private static final int ANIMATING_STATE = 2;
/*  101:     */   private RecentFileManager recentFileManager;
/*  102:     */   private JFileChooser fileChooser;
/*  103:     */   private JDialog aboutBox;
/*  104:     */   private TipDialog tipDialog;
/*  105:     */   private WelcomeDialog welcomeDialog;
/*  106:     */   private SetupWizard setupWizard;
/*  107:     */   private LoadTestReport loadTestReport;
/*  108:     */   private CostReport costReport;
/*  109:     */   private LoadTemplateDialog loadTemplateDialog;
/*  110:     */   private LoadSampleDialog loadSampleDialog;
/*  111:     */   private DesignIterationDialog designIterationDialog;
/*  112:     */   private UnstableModelDialog unstableModelDialog;
/*  113:     */   private SlendernessTestFailDialog slendernessTestFailDialog;
/*  114:     */   private MemberDetail memberDetail;
/*  115:     */   private StockSelector stockSelector;
/*  116:     */   private StockSelector popupStockSelector;
/*  117:     */   private Animation animation;
/*  118:     */   private Animation fixedEyeAnimation;
/*  119:     */   private Animation flyThruAnimation;
/*  120: 174 */   private boolean toolsDialogInitialized = false;
/*  121: 175 */   private boolean toolsDialogVisibleState = true;
/*  122:     */   private static final String nullPanelCard = "nullPanel";
/*  123:     */   private static final String designPanelCardName = "designPanel";
/*  124:     */   private static final String flyThruAnimationPanelCardName = "flyThruAnimationPanel";
/*  125:     */   private static final String fixedEyeAnimationPanelCardName = "fixedEyeAnimationPanel";
/*  126: 184 */   private String animationPanelCardName = "flyThruAnimationPanel";
/*  127:     */   private String selectedCard;
/*  128:     */   private static final String screenDimensionStorage = "screenDimension.xml";
/*  129:     */   private static final String fileChooserPathStorage = "fileChooserPath.xml";
/*  130:     */   private static final String keyCodeStorage = "keyCode.xml";
/*  131:     */   private static final String graphicsCapabilityStorage = "graphicsCapability.xml";
/*  132: 196 */   private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
/*  133:     */   private JButton closeMemberTableButton;
/*  134:     */   private ButtonGroup animationButtonGroup;
/*  135:     */   private JButton back1iterationButton;
/*  136:     */   private JMenuItem back1iterationItem;
/*  137:     */   private JToolBar bottomToolBar;
/*  138:     */   private JMenuItem bridgeDesignWindowMenuItem;
/*  139:     */   private JMenuItem browseOurWebSiteMenuItem;
/*  140:     */   private JPanel cardPanel;
/*  141:     */   private JRadioButtonMenuItem coarseGridMenuItem;
/*  142:     */   private JLabel corner;
/*  143:     */   private JLabel costLabel;
/*  144:     */   private JButton costReportButton;
/*  145:     */   private JMenuItem costReportMenuItem;
/*  146:     */   private JLabel crossSectionSketchLabel;
/*  147:     */   private JLabel curveLabel;
/*  148:     */   private JButton decreaseMemberSizeButton;
/*  149:     */   private JButton deleteButton;
/*  150:     */   private JMenuItem deleteItem;
/*  151:     */   private JPanel designPanel;
/*  152:     */   private ButtonGroup designTestGroup;
/*  153:     */   private JTextField designedByField;
/*  154:     */   private JLabel designedByLabel;
/*  155:     */   private JLabel dimensionsLabel;
/*  156:     */   private JTable dimensionsTable;
/*  157:     */   private JPanel draftingJPanel;
/*  158:     */   private DraftingPanel draftingPanel;
/*  159:     */   private JPopupMenu draftingPopup;
/*  160:     */   private JRadioButtonMenuItem draftingPopupCoarseGrid;
/*  161:     */   private JRadioButtonMenuItem draftingPopupErase;
/*  162:     */   private JRadioButtonMenuItem draftingPopupFineGrid;
/*  163:     */   private JRadioButtonMenuItem draftingPopupJoints;
/*  164:     */   private JRadioButtonMenuItem draftingPopupMediumGrid;
/*  165:     */   private JCheckBoxMenuItem draftingPopupMemberList;
/*  166:     */   private JRadioButtonMenuItem draftingPopupMembers;
/*  167:     */   private JRadioButtonMenuItem draftingPopupSelect;
/*  168:     */   private JMenuItem draftingPopupSelectAll;
/*  169:     */   private JSeparator draftingPopupSep01;
/*  170:     */   private JSeparator draftingPopupSep02;
/*  171:     */   private JSeparator draftingPopupSep03;
/*  172:     */   private JToggleButton drawingBoardButton;
/*  173:     */   private JLabel drawingBoardLabel;
/*  174:     */   private JRadioButtonMenuItem drawingBoardMenuItem;
/*  175:     */   private JPanel drawingPanel;
/*  176:     */   private JToggleButton editEraseButton;
/*  177:     */   private JRadioButtonMenuItem editEraseMenuItem;
/*  178:     */   private JToggleButton editJointsButton;
/*  179:     */   private JRadioButtonMenuItem editJointsMenuItem;
/*  180:     */   private JToggleButton editMembersButton;
/*  181:     */   private JRadioButtonMenuItem editMembersMenuItem;
/*  182:     */   private JMenu editMenu;
/*  183:     */   private JSeparator editMenuSeparator1;
/*  184:     */   private JSeparator editMenuSeparator2;
/*  185:     */   private JToggleButton editSelectButton;
/*  186:     */   private JRadioButtonMenuItem editSelectMenuItem;
/*  187:     */   private JMenuItem exitMenuItem;
/*  188:     */   private JMenu fileMenu;
/*  189:     */   private JSeparator fileMenuSeparator1;
/*  190:     */   private JSeparator fileMenuSeparator2;
/*  191:     */   private JSeparator fileMenuSeparator3;
/*  192:     */   private JSeparator fileMenuSeparator4;
/*  193:     */   private JRadioButtonMenuItem fineGridMenuItem;
/*  194:     */   private Canvas fixedEyeAnimationCanvas;
/*  195:     */   private Canvas flyThruAnimationCanvas;
/*  196:     */   private JButton forward1iterationButton;
/*  197:     */   private JMenuItem forward1iterationItem;
/*  198:     */   private JMenuItem gotoIterationItem;
/*  199:     */   private JCheckBox graphAllCheck;
/*  200:     */   private ButtonGroup gridSizeButtonGroup;
/*  201:     */   private JSeparator helpSeparator01;
/*  202:     */   private JSeparator helpSeparator02;
/*  203:     */   private JMenuItem helpTopicsMenuItem;
/*  204:     */   private JLabel horizontalRuler;
/*  205:     */   private JMenuItem howToDesignMenuItem;
/*  206:     */   private JButton increaseMemberSizeButton;
/*  207:     */   private JLabel iterationLabel;
/*  208:     */   private JLabel iterationNumberLabel;
/*  209:     */   private JMenuItem jMenuItem1;
/*  210:     */   private JButton keyCodeCancelButton;
/*  211:     */   private JDialog keyCodeDialog;
/*  212:     */   private JLabel keyCodeErrorLabel;
/*  213:     */   private JLabel keyCodeLabel;
/*  214:     */   private JButton keyCodeOkButton;
/*  215:     */   private JTextField keyCodeTextField;
/*  216:     */   private JMenuItem loadTemplateMenuItem;
/*  217:     */   private JToggleButton loadTestButton;
/*  218:     */   private JRadioButtonMenuItem loadTestMenuItem;
/*  219:     */   private JButton loadTestReportButton;
/*  220:     */   private JMenuItem loadTestReportMenuItem;
/*  221:     */   private JLabel loadTestResultsLabel;
/*  222:     */   private JPanel mainPanel;
/*  223:     */   private JComboBox materialBox;
/*  224:     */   private JLabel materialPropertiesLabel;
/*  225:     */   private JTable materialPropertiesTable;
/*  226:     */   private JRadioButtonMenuItem mediumGridMenuItem;
/*  227:     */   private JLabel memberCostLabel;
/*  228:     */   private JTable memberCostTable;
/*  229:     */   private JPanel memberDetailPanel;
/*  230:     */   private JTabbedPane memberDetailTabs;
/*  231:     */   private JDialog memberEditPopup;
/*  232:     */   private JTable memberJTable;
/*  233:     */   private MemberTable memberTable;
/*  234:     */   private JPanel memberListPanel;
/*  235:     */   private JPanel memberPanel;
/*  236:     */   private JButton memberPopupDecreaseSizeButton;
/*  237:     */   private JButton memberPopupDeleteButton;
/*  238:     */   private JButton memberPopupDoneButton;
/*  239:     */   private JButton memberPopupIncreaseSizeButton;
/*  240:     */   private JComboBox memberPopupMaterialBox;
/*  241:     */   private JLabel memberPopupMaterialLabel;
/*  242:     */   private JToggleButton memberPopupMemberListButton;
/*  243:     */   private JPanel memberPopupPanel;
/*  244:     */   private JComboBox memberPopupSectionBox;
/*  245:     */   private JLabel memberPopupSectionLabel;
/*  246:     */   private JComboBox memberPopupSizeBox;
/*  247:     */   private JLabel memberPopupSizeLabel;
/*  248:     */   private JScrollPane memberScroll;
/*  249:     */   private JPanel memberSelecButtonPanel;
/*  250:     */   private JComboBox memberSelectBox;
/*  251:     */   private JLabel memberSelectLabel;
/*  252:     */   private JButton memberSelectLeftButton;
/*  253:     */   private JButton memberSelectRightButton;
/*  254:     */   private JTabbedPane memberTabs;
/*  255:     */   private JMenuBar menuBar;
/*  256:     */   private JButton newButton;
/*  257:     */   private JMenuItem newDesignMenuItem;
/*  258:     */   private JPanel nullPanel;
/*  259:     */   private JButton openButton;
/*  260:     */   private JButton openMemberTableButton;
/*  261:     */   private JMenuItem openMenuItem;
/*  262:     */   private JMenuItem openSampleDesignMenuItem;
/*  263:     */   private JButton printButton;
/*  264:     */   private JMenuItem printLoadedClassesMenuItem;
/*  265:     */   private JMenuItem printMenuItem;
/*  266:     */   private JProgressBar progressBar;
/*  267:     */   private JTextField projectIDField;
/*  268:     */   private JLabel projectIDLabel;
/*  269:     */   private JButton redoButton;
/*  270:     */   private JToggleButton redoDropButton;
/*  271:     */   private JMenuItem redoItem;
/*  272:     */   private JMenu reportMenu;
/*  273:     */   private JMenuItem saveAsMenuItem;
/*  274:     */   private JMenuItem saveAsSample;
/*  275:     */   private JMenuItem saveAsTemplate;
/*  276:     */   private JButton saveButton;
/*  277:     */   private JMenuItem saveMenuItem;
/*  278:     */   private JLabel scenarioIDLabel;
/*  279:     */   private JMenuItem searchForHelpMenuItem;
/*  280:     */   private JComboBox sectionBox;
/*  281:     */   private JButton selectAllButton;
/*  282:     */   private JMenuItem selectallItem;
/*  283:     */   private JToolBar.Separator separator1;
/*  284:     */   private JToolBar.Separator separator2;
/*  285:     */   private JToolBar.Separator separator3;
/*  286:     */   private JToolBar.Separator separator4;
/*  287:     */   private JToolBar.Separator separator5;
/*  288:     */   private JToolBar.Separator separator6;
/*  289:     */   private JToolBar.Separator separator7;
/*  290:     */   private JToolBar.Separator separator8;
/*  291:     */   private JToggleButton setCoarseGridButton;
/*  292:     */   private JToggleButton setFineGridButton;
/*  293:     */   private JToggleButton setMediumGridButton;
/*  294:     */   private JButton showGoToIterationButton;
/*  295:     */   private JComboBox sizeBox;
/*  296:     */   private JLabel sketchLabel;
/*  297:     */   private JLabel spacer0;
/*  298:     */   private JLabel spacer1;
/*  299:     */   private JLabel spacer2;
/*  300:     */   private JLabel spacer3;
/*  301:     */   private JLabel spacer4;
/*  302:     */   private JLabel spacer5;
/*  303:     */   private JLabel spacer6;
/*  304:     */   private JLabel spacer7;
/*  305:     */   private JLabel statusAnimationLabel;
/*  306:     */   private JLabel statusLabel;
/*  307:     */   private JLabel statusMessageLabel;
/*  308:     */   private JPanel statusPanel;
/*  309:     */   private JLabel strengthCurveLabel;
/*  310:     */   private JMenu testMenu;
/*  311:     */   private JSeparator testMenuSep01;
/*  312:     */   private JPopupMenu.Separator testMenuSep02;
/*  313:     */   private JMenuItem tipOfTheDayMenuItem;
/*  314:     */   private JPanel titleBlockPanel;
/*  315:     */   private JLabel titleLabel;
/*  316:     */   private JCheckBoxMenuItem toggleAnimationControlsMenuItem;
/*  317:     */   private JCheckBoxMenuItem toggleAnimationMenuItem;
/*  318:     */   private JCheckBoxMenuItem toggleAutoCorrectMenuItem;
/*  319:     */   private JToggleButton toggleGuidesButton;
/*  320:     */   private JCheckBoxMenuItem toggleGuidesMenuItem;
/*  321:     */   private JCheckBoxMenuItem toggleLegacyGraphicsMenuItem;
/*  322:     */   private JToggleButton toggleMemberListButton;
/*  323:     */   private JCheckBoxMenuItem toggleMemberListMenuItem;
/*  324:     */   private JToggleButton toggleMemberNumbersButton;
/*  325:     */   private JCheckBoxMenuItem toggleMemberNumbersMenuItem;
/*  326:     */   private JCheckBoxMenuItem toggleRulerMenuItem;
/*  327:     */   private JToggleButton toggleTemplateButton;
/*  328:     */   private JCheckBoxMenuItem toggleTemplateMenuItem;
/*  329:     */   private JCheckBoxMenuItem toggleTitleBlockMenuItem;
/*  330:     */   private JCheckBoxMenuItem toggleToolsMenuItem;
/*  331:     */   private ButtonGroup toolMenuGroup;
/*  332:     */   private ButtonGroup toolsButtonGroup;
/*  333:     */   private JDialog toolsDialog;
/*  334:     */   private JMenu toolsMenu;
/*  335:     */   private JToolBar toolsToolbar;
/*  336:     */   private JToolBar topToolBar;
/*  337:     */   private JButton undoButton;
/*  338:     */   private JToggleButton undoDropButton;
/*  339:     */   private JMenuItem undoItem;
/*  340:     */   private JLabel verticalRuler;
/*  341:     */   private JMenu viewMenu;
/*  342:     */   private JSeparator viewSeparator1;
/*  343:     */   
/*  344:     */   public WPBDView(SingleFrameApplication app)
/*  345:     */   {
/*  346: 209 */     super(app);
/*  347:     */     
/*  348: 211 */     preinitComponents();
/*  349: 212 */     initComponents();
/*  350: 213 */     postInitComponents();
/*  351:     */   }
/*  352:     */   
/*  353:     */   private class TitleBlockPanel
/*  354:     */     extends JPanel
/*  355:     */   {
/*  356:     */     static final int margin = 4;
/*  357:     */     static final int spacing = 2;
/*  358:     */     
/*  359:     */     private TitleBlockPanel() {}
/*  360:     */     
/*  361:     */     public void paintComponent(Graphics g)
/*  362:     */     {
/*  363: 232 */       super.paintComponent(g);
/*  364: 233 */       int w = getWidth();
/*  365: 234 */       int h = getHeight();
/*  366: 235 */       g.setColor(Color.BLACK);
/*  367: 236 */       g.drawRect(0, 0, w - 1, h - 1);
/*  368: 237 */       g.drawRect(4, 4, w - 8 - 1, h - 8 - 1);
/*  369: 238 */       int y = 6 + WPBDView.this.titleLabel.getHeight() + 2;
/*  370: 239 */       g.drawLine(4, y, w - 4 - 1, y);
/*  371: 240 */       y += 3 + WPBDView.this.designedByField.getHeight() + 2;
/*  372: 241 */       g.drawLine(4, y, w - 4 - 1, y);
/*  373:     */     }
/*  374:     */   }
/*  375:     */   
/*  376:     */   void preinitComponents()
/*  377:     */   {
/*  378: 251 */     Help.initialize();
/*  379: 252 */     this.dispatcher = new Dispatcher();
/*  380: 253 */     this.bridge = new EditableBridgeModel();
/*  381: 254 */     this.bridgeDraftingView = new BridgeDraftingView(this.bridge);
/*  382:     */     
/*  383: 256 */     JPopupMenu.setDefaultLightWeightPopupEnabled(false);
/*  384:     */     
/*  385:     */ 
/*  386: 259 */     this.fixedEyeAnimation = FixedEyeAnimation.create(getFrame(), this.bridge);
/*  387: 260 */     this.animation = (this.flyThruAnimation = WPBDApp.isLegacyGraphics() ? this.fixedEyeAnimation : FlyThruAnimation.create(getFrame(), this.bridge));
/*  388:     */   }
/*  389:     */   
/*  390:     */   private void postInitComponents()
/*  391:     */   {
/*  392: 268 */     this.closeMemberTableButton = new JButton();
/*  393:     */     
/*  394: 270 */     ActionMap actionMap = WPBDApp.getApplication().getContext().getActionMap(WPBDView.class, this);
/*  395: 271 */     this.closeMemberTableButton.setAction(actionMap.get("closeMemberTable"));
/*  396: 272 */     this.closeMemberTableButton.setAlignmentX(1.0F);
/*  397: 273 */     this.closeMemberTableButton.setAlignmentY(0.0F);
/*  398: 274 */     this.closeMemberTableButton.setHideActionText(true);
/*  399: 275 */     this.closeMemberTableButton.setFocusable(false);
/*  400: 276 */     this.closeMemberTableButton.setMargin(new Insets(0, 0, 0, 0));
/*  401: 277 */     this.closeMemberTableButton.setName("closeMemberTableButton");
/*  402: 278 */     this.memberPanel.setLayout(new OverlayLayout(this.memberPanel));
/*  403: 279 */     this.memberPanel.add(this.closeMemberTableButton);
/*  404: 280 */     this.memberPanel.add(this.memberTabs);
/*  405: 282 */     if (!Debug.isPropertyDefined("wpbd.develop", false))
/*  406:     */     {
/*  407: 283 */       this.saveAsSample.setVisible(false);
/*  408: 284 */       this.saveAsTemplate.setVisible(false);
/*  409: 285 */       this.printLoadedClassesMenuItem.setVisible(false);
/*  410:     */     }
/*  411: 289 */     this.fileChooser = new JFileChooser();
/*  412: 290 */     Dimension fileChooserSize = this.fileChooser.getPreferredSize();
/*  413: 291 */     PreviewAccessory preview = new PreviewAccessory();
/*  414: 292 */     if (WPBDApp.isEnhancedMacUI()) {
/*  415: 293 */       this.fileChooser.putClientProperty("Quaqua.FileChooser.preview", preview);
/*  416:     */     } else {
/*  417: 296 */       this.fileChooser.setAccessory(preview);
/*  418:     */     }
/*  419: 298 */     this.fileChooser.addPropertyChangeListener(preview);
/*  420: 299 */     Dimension accessorySize = preview.getPreferredSize();
/*  421: 300 */     Dimension maxSize = Toolkit.getDefaultToolkit().getScreenSize();
/*  422: 301 */     fileChooserSize.width = Math.min(maxSize.width, fileChooserSize.width + accessorySize.width);
/*  423: 302 */     this.fileChooser.setPreferredSize(fileChooserSize);
/*  424: 303 */     this.fileChooser.addChoosableFileFilter(new BDCFileFilter(null));
/*  425: 304 */     restoreFileChooserState();
/*  426:     */     
/*  427:     */ 
/*  428: 307 */     this.stockSelector = new StockSelector(this.materialBox, this.sectionBox, this.sizeBox);
/*  429: 308 */     this.draftingPanel.setMemberStockSelector(this.stockSelector);
/*  430: 309 */     this.stockSelector.addChangeListener(new ChangeListener()
/*  431:     */     {
/*  432:     */       public void stateChanged(ChangeEvent e)
/*  433:     */       {
/*  434: 311 */         if (!WPBDView.this.bridge.isSelectedMember()) {
/*  435: 314 */           WPBDView.this.setSizeButtonsEnabled();
/*  436:     */         }
/*  437:     */       }
/*  438: 319 */     });
/*  439: 320 */     this.animation.getControls().getDialog().addComponentListener(new ComponentAdapter()
/*  440:     */     {
/*  441:     */       public void componentHidden(ComponentEvent e)
/*  442:     */       {
/*  443: 323 */         WPBDView.setSelected(WPBDView.this.toggleAnimationControlsMenuItem, false);
/*  444:     */       }
/*  445: 327 */     });
/*  446: 328 */     this.toolsDialog.addComponentListener(new ComponentAdapter()
/*  447:     */     {
/*  448:     */       public void componentHidden(ComponentEvent e)
/*  449:     */       {
/*  450: 331 */         WPBDView.setSelected(WPBDView.this.toggleToolsMenuItem, false);
/*  451:     */       }
/*  452: 335 */     });
/*  453: 336 */     this.cardPanel.addComponentListener(new ComponentAdapter()
/*  454:     */     {
/*  455:     */       public void componentResized(ComponentEvent e)
/*  456:     */       {
/*  457: 339 */         WPBDView.this.animation.applyCanvasResizeBugWorkaround();
/*  458:     */       }
/*  459: 342 */     });
/*  460: 343 */     this.popupStockSelector = new StockSelector(this.memberPopupMaterialBox, this.memberPopupSectionBox, this.memberPopupSizeBox);
/*  461:     */     
/*  462: 345 */     this.memberDetail = new MemberDetail(this.bridge, this.stockSelector, this.memberDetailTabs, this.memberDetailPanel, this.materialPropertiesTable, this.dimensionsTable, this.memberCostTable, this.crossSectionSketchLabel, this.memberSelectBox, this.graphAllCheck, this.strengthCurveLabel);
/*  463:     */     
/*  464:     */ 
/*  465:     */ 
/*  466:     */ 
/*  467:     */ 
/*  468:     */ 
/*  469:     */ 
/*  470:     */ 
/*  471:     */ 
/*  472:     */ 
/*  473:     */ 
/*  474: 357 */     this.bridge.addIterationChangeListener(this.memberDetail);
/*  475: 358 */     this.graphAllCheck.addItemListener(new ItemListener()
/*  476:     */     {
/*  477:     */       public void itemStateChanged(ItemEvent e)
/*  478:     */       {
/*  479: 360 */         WPBDView.this.memberDetail.handleShowAllStateChange(e);
/*  480:     */       }
/*  481: 362 */     });
/*  482: 363 */     this.memberSelectBox.addItemListener(new ItemListener()
/*  483:     */     {
/*  484:     */       public void itemStateChanged(ItemEvent e)
/*  485:     */       {
/*  486: 365 */         WPBDView.this.memberDetail.handleMemberSelectChange(e);
/*  487:     */       }
/*  488: 367 */     });
/*  489: 368 */     this.dispatcher.initialize(this.bridge, this.memberTable, this.memberDetail, this.stockSelector, this.popupStockSelector, this.draftingPanel);
/*  490:     */     
/*  491:     */ 
/*  492:     */ 
/*  493:     */ 
/*  494: 373 */     setSelected(this.toggleTitleBlockMenuItem, true);
/*  495: 374 */     setSelected(this.toggleMemberNumbersMenuItem, false);
/*  496: 375 */     setSelected(this.toggleRulerMenuItem, true);
/*  497: 376 */     setSelected(this.toggleToolsMenuItem, true);
/*  498: 377 */     setSelected(this.toggleAnimationControlsMenuItem, true);
/*  499: 378 */     setSelected(this.toggleLegacyGraphicsMenuItem, setDefaultGraphics());
/*  500: 379 */     setSelected(this.toggleGuidesMenuItem, false);
/*  501: 380 */     setSelected(this.toggleTemplateMenuItem, true);
/*  502:     */     
/*  503: 382 */     setSelected(this.setCoarseGridButton, true);
/*  504: 383 */     setSelected(this.setMediumGridButton, false);
/*  505: 384 */     setSelected(this.setFineGridButton, false);
/*  506:     */     
/*  507: 386 */     setSelected(this.drawingBoardButton, true);
/*  508: 387 */     setSelected(this.loadTestButton, false);
/*  509:     */     
/*  510: 389 */     setSelected(this.editJointsMenuItem, true);
/*  511: 390 */     setSelected(this.editMembersMenuItem, false);
/*  512: 391 */     setSelected(this.editSelectMenuItem, false);
/*  513: 392 */     setSelected(this.editEraseMenuItem, false);
/*  514:     */     
/*  515: 394 */     setSelected(this.toggleAnimationMenuItem, true);
/*  516: 395 */     setSelected(this.toggleAutoCorrectMenuItem, true);
/*  517:     */     
/*  518: 397 */     this.undoButton.getAction().setEnabled(false);
/*  519: 398 */     this.redoButton.getAction().setEnabled(false);
/*  520: 399 */     this.saveButton.getAction().setEnabled(false);
/*  521: 400 */     this.deleteButton.getAction().setEnabled(false);
/*  522: 401 */     this.loadTestButton.getAction().setEnabled(false);
/*  523: 402 */     this.costReportButton.getAction().setEnabled(false);
/*  524: 403 */     this.showGoToIterationButton.getAction().setEnabled(false);
/*  525: 404 */     this.loadTestReportButton.getAction().setEnabled(false);
/*  526: 405 */     this.back1iterationButton.getAction().setEnabled(false);
/*  527: 406 */     this.forward1iterationButton.getAction().setEnabled(false);
/*  528: 407 */     this.toggleTemplateButton.getAction().setEnabled(false);
/*  529:     */     
/*  530:     */ 
/*  531: 410 */     this.enabledStateManager = new EnabledStateManager(3);
/*  532:     */     
/*  533: 412 */     boolean[] draftingOnly = { false, true, false };
/*  534: 413 */     boolean[] animationOnly = { false, false, true };
/*  535: 414 */     boolean[] draftingOrAnimation = { false, true, true };
/*  536: 415 */     this.enabledStateManager.add(this.toggleMemberListButton, draftingOnly);
/*  537: 416 */     this.enabledStateManager.add(this.loadTemplateMenuItem, draftingOnly);
/*  538: 417 */     this.enabledStateManager.add(this.saveAsMenuItem, draftingOrAnimation);
/*  539: 418 */     this.enabledStateManager.add(this.printButton, draftingOnly);
/*  540: 419 */     this.enabledStateManager.add(this.printMenuItem, draftingOnly);
/*  541: 420 */     this.enabledStateManager.add(this.selectAllButton, draftingOnly);
/*  542: 421 */     this.enabledStateManager.add(this.deleteButton, draftingOnly);
/*  543: 422 */     this.enabledStateManager.add(this.undoButton, draftingOnly);
/*  544: 423 */     this.enabledStateManager.add(this.undoDropButton, draftingOnly);
/*  545: 424 */     this.enabledStateManager.add(this.redoButton, draftingOnly);
/*  546: 425 */     this.enabledStateManager.add(this.redoDropButton, draftingOnly);
/*  547: 426 */     this.enabledStateManager.add(this.back1iterationButton, draftingOnly);
/*  548: 427 */     this.enabledStateManager.add(this.forward1iterationButton, draftingOnly);
/*  549: 428 */     this.enabledStateManager.add(this.showGoToIterationButton, draftingOnly);
/*  550: 429 */     this.enabledStateManager.add(this.toggleToolsMenuItem, draftingOnly);
/*  551: 430 */     this.enabledStateManager.add(this.toggleRulerMenuItem, draftingOnly);
/*  552: 431 */     this.enabledStateManager.add(this.toggleTitleBlockMenuItem, draftingOnly);
/*  553: 432 */     this.enabledStateManager.add(this.toggleMemberNumbersButton, draftingOnly);
/*  554: 433 */     this.enabledStateManager.add(this.toggleTemplateButton, draftingOnly);
/*  555: 434 */     this.enabledStateManager.add(this.toggleGuidesButton, draftingOnly);
/*  556: 435 */     this.enabledStateManager.add(this.coarseGridMenuItem, draftingOnly);
/*  557: 436 */     this.enabledStateManager.add(this.mediumGridMenuItem, draftingOnly);
/*  558: 437 */     this.enabledStateManager.add(this.fineGridMenuItem, draftingOnly);
/*  559: 438 */     this.enabledStateManager.add(this.toolsMenu, draftingOnly);
/*  560: 439 */     this.enabledStateManager.add(this.drawingBoardButton, draftingOrAnimation);
/*  561: 440 */     this.enabledStateManager.add(this.loadTestButton, draftingOrAnimation);
/*  562: 441 */     this.enabledStateManager.add(this.toggleAnimationControlsMenuItem, animationOnly);
/*  563: 442 */     this.enabledStateManager.add(this.toggleAnimationMenuItem, draftingOnly);
/*  564: 443 */     this.enabledStateManager.add(this.toggleAutoCorrectMenuItem, draftingOnly);
/*  565: 444 */     this.enabledStateManager.add(this.costReportButton, draftingOrAnimation);
/*  566: 445 */     this.enabledStateManager.add(this.loadTestReportButton, draftingOrAnimation);
/*  567: 446 */     this.enabledStateManager.add(this.increaseMemberSizeButton, draftingOnly);
/*  568: 447 */     this.enabledStateManager.add(this.decreaseMemberSizeButton, draftingOnly);
/*  569: 448 */     this.enabledStateManager.add(this.materialBox, draftingOnly);
/*  570: 449 */     this.enabledStateManager.add(this.sectionBox, draftingOnly);
/*  571: 450 */     this.enabledStateManager.add(this.sizeBox, draftingOnly);
/*  572: 453 */     if (WPBDApp.isLegacyGraphics())
/*  573:     */     {
/*  574: 454 */       setSelected(this.toggleLegacyGraphicsMenuItem, true);
/*  575: 455 */       this.toggleLegacyGraphicsMenuItem.getAction().setEnabled(false);
/*  576:     */     }
/*  577:     */     else
/*  578:     */     {
/*  579: 459 */       this.enabledStateManager.add(this.toggleLegacyGraphicsMenuItem, draftingOnly);
/*  580:     */     }
/*  581: 463 */     this.bridge.getUndoManager().addUndoableAfterEditListener(new UndoableEditListener()
/*  582:     */     {
/*  583:     */       public void undoableEditHappened(UndoableEditEvent e)
/*  584:     */       {
/*  585: 466 */         ExtendedUndoManager undoManager = WPBDView.this.bridge.getUndoManager();
/*  586: 467 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.undoButton, undoManager.canUndo());
/*  587: 468 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.undoDropButton, undoManager.canUndo());
/*  588: 469 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.redoButton, undoManager.canRedo());
/*  589: 470 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.redoDropButton, undoManager.canRedo());
/*  590: 471 */         WPBDView.this.undoButton.setToolTipText(undoManager.getUndoPresentationName());
/*  591: 472 */         WPBDView.this.redoButton.setToolTipText(undoManager.getRedoPresentationName());
/*  592: 473 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.saveButton, (undoManager.isDirty()) || (!undoManager.isStored()));
/*  593:     */       }
/*  594: 476 */     });
/*  595: 477 */     this.bridge.addAnalysisChangeListener(new ChangeListener()
/*  596:     */     {
/*  597:     */       public void stateChanged(ChangeEvent e)
/*  598:     */       {
/*  599: 480 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.loadTestReportButton, WPBDView.this.bridge.isAnalysisValid());
/*  600: 481 */         WPBDView.this.setLoadTestButtonEnabled();
/*  601: 482 */         WPBDView.this.setStatusIcon();
/*  602:     */         
/*  603: 484 */         WPBDView.this.setSizeButtonsEnabled();
/*  604:     */       }
/*  605: 487 */     });
/*  606: 488 */     this.bridge.addIterationChangeListener(new ChangeListener()
/*  607:     */     {
/*  608:     */       public void stateChanged(ChangeEvent e)
/*  609:     */       {
/*  610: 491 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.forward1iterationButton, WPBDView.this.bridge.canLoadNextIteration(1));
/*  611: 492 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.back1iterationButton, WPBDView.this.bridge.canLoadNextIteration(-1));
/*  612: 493 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.showGoToIterationButton, WPBDView.this.bridge.canGotoIteration());
/*  613: 494 */         WPBDView.this.setIterationLabel();
/*  614:     */       }
/*  615: 497 */     });
/*  616: 498 */     this.bridge.addStructureChangeListener(new ChangeListener()
/*  617:     */     {
/*  618:     */       public void stateChanged(ChangeEvent e)
/*  619:     */       {
/*  620: 501 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.loadTestReportButton, WPBDView.this.bridge.isAnalysisValid());
/*  621: 502 */         WPBDView.this.costLabel.setText(WPBDView.this.currencyFormatter.format(WPBDView.this.bridge.getTotalCost()));
/*  622: 503 */         WPBDView.this.setLoadTestButtonEnabled();
/*  623: 504 */         WPBDView.this.setStatusIcon();
/*  624: 505 */         WPBDView.this.setSizeButtonsEnabled();
/*  625:     */       }
/*  626: 508 */     });
/*  627: 509 */     this.bridge.addSelectionChangeListener(new ChangeListener()
/*  628:     */     {
/*  629:     */       public void stateChanged(ChangeEvent e)
/*  630:     */       {
/*  631: 512 */         if (WPBDView.this.bridge.getSelectedJoint() == null) {
/*  632: 513 */           WPBDView.this.draftingPanel.eraseCrosshairs();
/*  633:     */         }
/*  634: 515 */         WPBDView.this.enabledStateManager.setEnabled(WPBDView.this.deleteButton, WPBDView.this.bridge.isSelection());
/*  635: 516 */         WPBDView.this.setSizeButtonsEnabled();
/*  636:     */       }
/*  637: 520 */     });
/*  638: 521 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
/*  639: 524 */     if (WPBDApp.getOS() == 2) {
/*  640: 525 */       this.exitMenuItem.setVisible(false);
/*  641:     */     }
/*  642: 528 */     this.recentFileManager = new RecentFileManager(5, this);
/*  643: 529 */     addRecentFilesToFileMenu();
/*  644:     */     
/*  645: 531 */     checkHardware();
/*  646:     */   }
/*  647:     */   
/*  648:     */   private void addRecentFilesToFileMenu()
/*  649:     */   {
/*  650: 535 */     this.recentFileManager.addRecentFileMenuItemsAtSep(this.fileMenu, 4, this.fileChooser.getCurrentDirectory());
/*  651:     */   }
/*  652:     */   
/*  653:     */   private void print(boolean dialog)
/*  654:     */   {
/*  655: 539 */     String fileName = this.bridge.getUndoManager().isStored() ? this.fileChooser.getSelectedFile().getAbsolutePath() : null;
/*  656: 540 */     PrinterUI.print(getFrame(), fileName, this.bridge, WPBDView.class, dialog);
/*  657:     */   }
/*  658:     */   
/*  659:     */   private class BDCFileFilter
/*  660:     */     extends FileFilter
/*  661:     */   {
/*  662:     */     private BDCFileFilter() {}
/*  663:     */     
/*  664:     */     public boolean accept(File f)
/*  665:     */     {
/*  666: 546 */       if (f.isDirectory()) {
/*  667: 547 */         return true;
/*  668:     */       }
/*  669: 549 */       String name = f.getPath();
/*  670: 550 */       int dotIndex = name.lastIndexOf('.');
/*  671: 551 */       if (dotIndex < 0) {
/*  672: 552 */         return false;
/*  673:     */       }
/*  674: 554 */       return name.substring(dotIndex + 1).equalsIgnoreCase("bdc");
/*  675:     */     }
/*  676:     */     
/*  677:     */     public String getDescription()
/*  678:     */     {
/*  679: 559 */       return WPBDView.this.getResourceMap().getString("fileDescription.text", new Object[0]);
/*  680:     */     }
/*  681:     */   }
/*  682:     */   
/*  683:     */   public static boolean isModalDialogVisible()
/*  684:     */   {
/*  685: 575 */     Frame[] frames = JFrame.getFrames();
/*  686: 576 */     for (int i = 0; i < frames.length; i++)
/*  687:     */     {
/*  688: 577 */       Window[] windows = frames[i].getOwnedWindows();
/*  689: 578 */       for (int j = 0; j < windows.length; j++) {
/*  690: 579 */         if ((windows[j].isVisible()) && ((windows[j] instanceof JDialog)) && (((JDialog)windows[j]).isModal())) {
/*  691: 580 */           return true;
/*  692:     */         }
/*  693:     */       }
/*  694:     */     }
/*  695: 584 */     return false;
/*  696:     */   }
/*  697:     */   
/*  698:     */   public static boolean isSelected(AbstractButton button)
/*  699:     */   {
/*  700: 594 */     return button.getAction().getValue("SwingSelectedKey") == Boolean.TRUE;
/*  701:     */   }
/*  702:     */   
/*  703:     */   public static void setSelected(AbstractButton button, boolean val)
/*  704:     */   {
/*  705: 604 */     button.getAction().putValue("SwingSelectedKey", Boolean.valueOf(val));
/*  706:     */   }
/*  707:     */   
/*  708:     */   public boolean dispatchKeyEvent(KeyEvent e)
/*  709:     */   {
/*  710: 614 */     if (e.getID() == 401)
/*  711:     */     {
/*  712: 615 */       int code = e.getKeyCode();
/*  713: 617 */       if ((e.isControlDown()) && (e.isShiftDown()) && ((code == 36) || (code == 38)) && (!isModalDialogVisible()))
/*  714:     */       {
/*  715: 619 */         this.keyCodeDialog.setLocationRelativeTo(WPBDApp.getFrame());
/*  716: 620 */         String keyCode = (String)WPBDApp.loadFromLocalStorage("keyCode.xml");
/*  717: 621 */         this.keyCodeTextField.setText(keyCode);
/*  718: 622 */         this.keyCodeErrorLabel.setVisible(false);
/*  719: 623 */         this.keyCodeDialog.getRootPane().setDefaultButton(this.keyCodeOkButton);
/*  720: 624 */         this.bridge.clearSelectedJoint(true);
/*  721: 625 */         this.keyCodeDialog.setVisible(true);
/*  722: 626 */         this.keyCodeTextField.requestFocusInWindow();
/*  723: 627 */         return true;
/*  724:     */       }
/*  725: 631 */       if (this.bridge.getSelectedJoint() != null) {
/*  726: 632 */         switch (code)
/*  727:     */         {
/*  728:     */         case 37: 
/*  729:     */         case 38: 
/*  730:     */         case 39: 
/*  731:     */         case 40: 
/*  732: 637 */           KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(this.draftingPanel, e);
/*  733: 638 */           return true;
/*  734:     */         case 27: 
/*  735: 640 */           this.bridge.clearSelectedJoint(true);
/*  736: 641 */           this.draftingPanel.eraseCrosshairs();
/*  737: 642 */           return true;
/*  738:     */         }
/*  739:     */       }
/*  740:     */     }
/*  741: 646 */     return false;
/*  742:     */   }
/*  743:     */   
/*  744:     */   private void restoreFileChooserState()
/*  745:     */   {
/*  746: 653 */     String path = (String)WPBDApp.loadFromLocalStorage("fileChooserPath.xml");
/*  747: 654 */     if (path != null) {
/*  748: 655 */       this.fileChooser.setCurrentDirectory(new File(path));
/*  749:     */     }
/*  750:     */   }
/*  751:     */   
/*  752:     */   private void setSizeButtonsEnabled()
/*  753:     */   {
/*  754: 663 */     int mask = this.bridge.isSelectedMember() ? this.bridge.getAllowedShapeChanges() : this.stockSelector.getAllowedShapeChanges();
/*  755: 664 */     this.increaseMemberSizeButton.setEnabled((mask & 0x1) != 0);
/*  756: 665 */     this.decreaseMemberSizeButton.setEnabled((mask & 0x2) != 0);
/*  757:     */   }
/*  758:     */   
/*  759:     */   private void setLoadTestButtonEnabled()
/*  760:     */   {
/*  761: 672 */     this.enabledStateManager.setEnabled(this.loadTestButton, (autofixEnabled()) || ((this.bridge.isAnalyzable()) && ((animationEnabled()) || (!this.bridge.isAnalysisValid()))));
/*  762:     */   }
/*  763:     */   
/*  764:     */   private void setStatusIcon()
/*  765:     */   {
/*  766:     */     String tipKey;
/*  767:     */     String iconName;
/*  768:     */     String tipKey;
/*  769: 683 */     if (!this.bridge.isAnalysisValid())
/*  770:     */     {
/*  771: 684 */       String iconName = "working.png";
/*  772: 685 */       tipKey = "workingTip.text";
/*  773:     */     }
/*  774:     */     else
/*  775:     */     {
/*  776:     */       String tipKey;
/*  777: 687 */       if (!this.bridge.isPassing())
/*  778:     */       {
/*  779: 688 */         String iconName = "bad.png";
/*  780: 689 */         tipKey = "badTip.text";
/*  781:     */       }
/*  782:     */       else
/*  783:     */       {
/*  784: 692 */         iconName = "good.png";
/*  785: 693 */         tipKey = "goodTip.text";
/*  786:     */       }
/*  787:     */     }
/*  788: 695 */     this.statusLabel.setIcon(WPBDApp.getApplication().getIconResource(iconName));
/*  789: 696 */     this.statusLabel.setToolTipText(getResourceMap().getString(tipKey, new Object[0]));
/*  790:     */   }
/*  791:     */   
/*  792:     */   private void setIterationLabel()
/*  793:     */   {
/*  794: 703 */     this.iterationNumberLabel.setText(" " + Integer.toString(this.bridge.getIteration()));
/*  795:     */   }
/*  796:     */   
/*  797:     */   public void initComponentsPostShow()
/*  798:     */   {
/*  799: 710 */     WPBDApp app = WPBDApp.getApplication();
/*  800: 711 */     ArrayList<Image> icons = new ArrayList();
/*  801: 712 */     icons.add(app.getImageResource("appicon.png"));
/*  802: 713 */     icons.add(app.getImageResource("appicon32.png"));
/*  803: 714 */     JFrame mainFrame = app.getMainFrame();
/*  804: 715 */     mainFrame.setIconImages(icons);
/*  805:     */     
/*  806:     */ 
/*  807: 718 */     this.setupWizard = new SetupWizard(mainFrame);
/*  808: 719 */     this.setupWizard.setLocationRelativeTo(mainFrame);
/*  809: 720 */     this.tipDialog = new TipDialog(mainFrame);
/*  810: 721 */     this.tipDialog.setLocationRelativeTo(mainFrame);
/*  811: 722 */     if (WPBDApp.getFileName() == null)
/*  812:     */     {
/*  813: 723 */       this.tipDialog.showTip(true, 1);
/*  814: 724 */       showWelcomeDialog();
/*  815:     */     }
/*  816:     */     else
/*  817:     */     {
/*  818:     */       try
/*  819:     */       {
/*  820: 728 */         File file = new File(WPBDApp.getFileName());
/*  821: 729 */         this.bridge.read(file);
/*  822:     */         
/*  823: 731 */         this.fileChooser.setSelectedFile(file);
/*  824: 732 */         initializePostBridgeLoad();
/*  825:     */       }
/*  826:     */       catch (IOException e)
/*  827:     */       {
/*  828: 734 */         selectCard("nullPanel");
/*  829: 735 */         showReadFailedMessage(e);
/*  830:     */       }
/*  831:     */     }
/*  832: 738 */     this.animation.applyCanvasResizeBugWorkaround();
/*  833:     */   }
/*  834:     */   
/*  835:     */   private void uploadBridgeToDraftingPanel()
/*  836:     */   {
/*  837: 745 */     this.designedByField.setText(this.bridge.getDesignedBy());
/*  838: 746 */     String pid = this.bridge.getProjectId();
/*  839: 747 */     int dashIndex = pid.indexOf('-');
/*  840: 748 */     this.scenarioIDLabel.setText(pid.substring(0, dashIndex + 1));
/*  841: 749 */     this.projectIDField.setText(pid.substring(dashIndex + 1));
/*  842: 750 */     this.memberTable.fireTableDataChanged();
/*  843:     */     
/*  844:     */ 
/*  845: 753 */     this.draftingPanel.setViewport(true);
/*  846:     */     
/*  847: 755 */     this.horizontalRuler.repaint();
/*  848: 756 */     this.verticalRuler.repaint();
/*  849:     */   }
/*  850:     */   
/*  851:     */   private void downloadBridgeFromDraftingPanel()
/*  852:     */   {
/*  853: 763 */     this.bridge.setDesignedBy(this.designedByField.getText());
/*  854: 764 */     this.bridge.setProjectId(this.scenarioIDLabel.getText() + this.projectIDField.getText());
/*  855:     */   }
/*  856:     */   
/*  857:     */   private void setTitleFileName()
/*  858:     */   {
/*  859: 771 */     String title = getFrame().getTitle();
/*  860: 772 */     int dashIndex = title.indexOf('-');
/*  861: 773 */     if (dashIndex >= 0) {
/*  862: 774 */       getFrame().setTitle(title.substring(0, dashIndex + 2) + this.fileChooser.getSelectedFile());
/*  863:     */     } else {
/*  864: 776 */       getFrame().setTitle(title + " - " + this.fileChooser.getSelectedFile());
/*  865:     */     }
/*  866:     */   }
/*  867:     */   
/*  868:     */   private void setDefaultFile()
/*  869:     */   {
/*  870: 784 */     this.fileChooser.setSelectedFile(getDefaultFile());
/*  871: 785 */     setTitleFileName();
/*  872:     */   }
/*  873:     */   
/*  874:     */   public boolean querySaveIfDirty()
/*  875:     */   {
/*  876: 797 */     if (this.bridge.getUndoManager().isDirty())
/*  877:     */     {
/*  878: 798 */       int yesNoCancel = JOptionPane.showConfirmDialog(getFrame(), getResourceMap().getString("saveDialog.text", new Object[0]), getResourceMap().getString("saveDialog.title", new Object[0]), 1, 2);
/*  879: 802 */       switch (yesNoCancel)
/*  880:     */       {
/*  881:     */       case 0: 
/*  882: 804 */         save();
/*  883: 805 */         break;
/*  884:     */       case 1: 
/*  885:     */         break;
/*  886:     */       case 2: 
/*  887: 809 */         return false;
/*  888:     */       }
/*  889:     */     }
/*  890: 812 */     return true;
/*  891:     */   }
/*  892:     */   
/*  893:     */   private void checkHardware()
/*  894:     */   {
/*  895: 819 */     Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
/*  896:     */     
/*  897:     */ 
/*  898: 822 */     Dimension storedScreenDim = (Dimension)WPBDApp.loadFromLocalStorage("screenDimension.xml");
/*  899: 823 */     WPBDApp.saveToLocalStorage(screenDim, "screenDimension.xml");
/*  900: 826 */     if (((storedScreenDim == null) || (!storedScreenDim.equals(screenDim))) && ((screenDim.width < 1200) || (screenDim.height < 900)))
/*  901:     */     {
/*  902: 828 */       int yesNo = JOptionPane.showConfirmDialog(getFrame(), getResourceMap().getString("hardwareWarning.text", new Object[] { Integer.valueOf(screenDim.width), Integer.valueOf(screenDim.height) }), getResourceMap().getString("hardwareWarning.title", new Object[0]), 0, 2);
/*  903: 832 */       if (yesNo == 1) {
/*  904: 833 */         WPBDApp.getApplication().exit();
/*  905:     */       }
/*  906:     */     }
/*  907:     */   }
/*  908:     */   
/*  909:     */   private void initializePostBridgeLoad()
/*  910:     */   {
/*  911: 843 */     setSketchModel(null);
/*  912:     */     
/*  913: 845 */     setGridFiner(DraftingGrid.toDensity(this.bridge.getSnapMultiple()));
/*  914:     */     
/*  915: 847 */     this.stockSelector.setMostCommonStockOf(this.bridge);
/*  916:     */     
/*  917: 849 */     this.bridgeDraftingView.initialize(this.bridge.getDesignConditions());
/*  918:     */     
/*  919: 851 */     uploadBridgeToDraftingPanel();
/*  920:     */     
/*  921: 853 */     showDrawingBoard();
/*  922:     */     
/*  923: 855 */     setTitleFileName();
/*  924:     */     
/*  925: 857 */     setSelected(this.editJointsMenuItem, true);
/*  926: 858 */     editJoints();
/*  927:     */   }
/*  928:     */   
/*  929:     */   private void recordRecentFileUse(File file)
/*  930:     */   {
/*  931: 862 */     this.recentFileManager.addNew(file);
/*  932: 863 */     this.recentFileManager.save();
/*  933: 864 */     addRecentFilesToFileMenu();
/*  934:     */   }
/*  935:     */   
/*  936:     */   private void recordRecentFileUse()
/*  937:     */   {
/*  938: 868 */     recordRecentFileUse(this.fileChooser.getSelectedFile());
/*  939:     */   }
/*  940:     */   
/*  941:     */   private boolean overwriteOk(File f, String title)
/*  942:     */   {
/*  943: 880 */     if (f.exists())
/*  944:     */     {
/*  945: 881 */       int yesNo = JOptionPane.showConfirmDialog(getFrame(), getResourceMap().getString("pathExistsMessage.text", new Object[] { f.getPath() }), title, 0, 2);
/*  946:     */       
/*  947:     */ 
/*  948: 884 */       return yesNo == 0;
/*  949:     */     }
/*  950: 886 */     return true;
/*  951:     */   }
/*  952:     */   
/*  953:     */   private static void appendDefaultSuffix(JFileChooser fileChooser)
/*  954:     */   {
/*  955: 896 */     if ((fileChooser.getFileFilter() instanceof BDCFileFilter)) {
/*  956:     */       try
/*  957:     */       {
/*  958: 898 */         String path = fileChooser.getSelectedFile().getCanonicalPath();
/*  959: 899 */         if (path.indexOf('.') < 0) {
/*  960: 900 */           fileChooser.setSelectedFile(new File(path + ".bdc"));
/*  961:     */         }
/*  962:     */       }
/*  963:     */       catch (IOException ex) {}
/*  964:     */     }
/*  965:     */   }
/*  966:     */   
/*  967:     */   private void setDesignMenuItemEnabled(boolean b)
/*  968:     */   {
/*  969: 909 */     this.enabledStateManager.setEnabled(this.toggleToolsMenuItem, b);
/*  970: 910 */     this.enabledStateManager.setEnabled(this.toggleAnimationControlsMenuItem, !b);
/*  971:     */   }
/*  972:     */   
/*  973:     */   public void shutdown()
/*  974:     */   {
/*  975: 917 */     recordRecentFileUse();
/*  976: 918 */     this.animation.stop();
/*  977: 919 */     this.animation.getControls().saveState();
/*  978:     */   }
/*  979:     */   
/*  980:     */   private void selectCard(String name)
/*  981:     */   {
/*  982: 929 */     if ((this.selectedCard != null) && (this.selectedCard.equals(name))) {
/*  983: 930 */       return;
/*  984:     */     }
/*  985: 933 */     if (this.animationPanelCardName.equals(this.selectedCard)) {
/*  986: 934 */       this.animation.getControls().saveVisibilityAndHide();
/*  987:     */     }
/*  988: 936 */     if (("designPanel".equals(this.selectedCard)) && (this.toolsDialogInitialized))
/*  989:     */     {
/*  990: 937 */       this.toolsDialogVisibleState = this.toolsDialog.isVisible();
/*  991: 938 */       this.bridge.clearSelectedJoint(true);
/*  992: 939 */       this.toolsDialog.setVisible(false);
/*  993:     */     }
/*  994: 942 */     CardLayout cl = (CardLayout)this.cardPanel.getLayout();
/*  995: 943 */     cl.show(this.cardPanel, name);
/*  996: 944 */     this.selectedCard = name;
/*  997: 946 */     if ("designPanel".equals(name))
/*  998:     */     {
/*  999: 948 */       this.animation.stop();
/* 1000:     */       
/* 1001: 950 */       this.enabledStateManager.setGUIState(1);
/* 1002: 952 */       if (!this.toolsDialogInitialized)
/* 1003:     */       {
/* 1004: 953 */         this.toolsDialog.pack();
/* 1005: 954 */         this.toolsDialog.setLocation(this.draftingPanel.getReasonableDrawingToolsLocation());
/* 1006: 955 */         this.toolsDialogVisibleState = true;
/* 1007: 956 */         this.toolsDialogInitialized = true;
/* 1008:     */       }
/* 1009: 959 */       setDesignMenuItemEnabled(true);
/* 1010: 960 */       this.enabledStateManager.setEnabled(this.costReportButton, true);
/* 1011: 961 */       setSelected(this.drawingBoardButton, true);
/* 1012: 962 */       this.draftingPanel.requestFocusInWindow();
/* 1013: 965 */       if (this.toolsDialogVisibleState) {
/* 1014: 966 */         SwingUtilities.invokeLater(new Runnable()
/* 1015:     */         {
/* 1016:     */           public void run()
/* 1017:     */           {
/* 1018: 968 */             WPBDView.this.bridge.clearSelectedJoint(true);
/* 1019: 969 */             WPBDView.this.toolsDialog.setVisible(true);
/* 1020: 970 */             WPBDView.setSelected(WPBDView.this.toggleToolsMenuItem, true);
/* 1021:     */           }
/* 1022:     */         });
/* 1023:     */       }
/* 1024:     */     }
/* 1025: 974 */     else if (this.animationPanelCardName.equals(name))
/* 1026:     */     {
/* 1027: 976 */       this.enabledStateManager.setGUIState(2);
/* 1028:     */       
/* 1029: 978 */       setDesignMenuItemEnabled(false);
/* 1030: 979 */       setSelected(this.loadTestButton, true);
/* 1031: 980 */       this.animation.getControls().startAnimation();
/* 1032: 981 */       setSelected(this.toggleAnimationControlsMenuItem, this.animation.getControls().getVisibleState());
/* 1033:     */     }
/* 1034:     */     else
/* 1035:     */     {
/* 1036: 985 */       this.enabledStateManager.setGUIState(0);
/* 1037: 986 */       setDefaultFile();
/* 1038: 987 */       this.bridge.getUndoManager().newSession();
/* 1039:     */     }
/* 1040:     */   }
/* 1041:     */   
/* 1042:     */   private boolean autofixEnabled()
/* 1043:     */   {
/* 1044: 997 */     return isSelected(this.toggleAutoCorrectMenuItem);
/* 1045:     */   }
/* 1046:     */   
/* 1047:     */   private boolean animationEnabled()
/* 1048:     */   {
/* 1049:1006 */     return isSelected(this.toggleAnimationMenuItem);
/* 1050:     */   }
/* 1051:     */   
/* 1052:     */   private void selectMemberList(boolean val)
/* 1053:     */   {
/* 1054:1015 */     setSelected(this.toggleMemberListButton, val);
/* 1055:1016 */     this.openMemberTableButton.setVisible(!val);
/* 1056:1017 */     this.memberPanel.setVisible(val);
/* 1057:     */   }
/* 1058:     */   
/* 1059:     */   private void setSketchModel(BridgeSketchModel designTemplate)
/* 1060:     */   {
/* 1061:1026 */     this.bridgeDraftingView.getBridgeSketchView().setModel(designTemplate);
/* 1062:1027 */     if (designTemplate == null)
/* 1063:     */     {
/* 1064:1028 */       this.enabledStateManager.setEnabled(this.toggleTemplateButton, false);
/* 1065:     */     }
/* 1066:     */     else
/* 1067:     */     {
/* 1068:1031 */       this.enabledStateManager.setEnabled(this.toggleTemplateButton, true);
/* 1069:1032 */       setSelected(this.toggleTemplateButton, true);
/* 1070:1033 */       toggleTemplate();
/* 1071:1034 */       setGridFiner(DraftingGrid.toDensity(designTemplate.getSnapMultiple()));
/* 1072:     */     }
/* 1073:     */   }
/* 1074:     */   
/* 1075:     */   private File getDefaultFile()
/* 1076:     */   {
/* 1077:1039 */     return new File("MyDesign.bdc");
/* 1078:     */   }
/* 1079:     */   
/* 1080:     */   private void showSetupWizard()
/* 1081:     */   {
/* 1082:1043 */     if (this.bridge.isInitialized()) {
/* 1083:1044 */       this.setupWizard.initialize(this.bridge.getDesignConditions(), this.bridge.getProjectId(), this.bridge.getDesignedBy(), this.bridgeDraftingView.getBridgeSketchView().getModel());
/* 1084:     */     }
/* 1085:1048 */     if (this.animationPanelCardName.equals(this.selectedCard)) {
/* 1086:1049 */       selectCard("designPanel");
/* 1087:     */     }
/* 1088:1051 */     this.bridge.clearSelectedJoint(true);
/* 1089:1052 */     this.setupWizard.setVisible(true);
/* 1090:1053 */     if (this.setupWizard.isOk())
/* 1091:     */     {
/* 1092:1054 */       recordRecentFileUse();
/* 1093:1055 */       this.bridge.initialize(this.setupWizard.getDesignConditions(), this.setupWizard.getProjectId(), this.setupWizard.getDesignedBy());
/* 1094:1056 */       this.bridgeDraftingView.initialize(this.setupWizard.getDesignConditions());
/* 1095:1057 */       setSketchModel(this.setupWizard.getSketchModel());
/* 1096:1058 */       showDrawingBoard();
/* 1097:1059 */       uploadBridgeToDraftingPanel();
/* 1098:1060 */       setDefaultFile();
/* 1099:1061 */       setLoadTestButtonEnabled();
/* 1100:     */       
/* 1101:1063 */       setSelected(this.editJointsMenuItem, true);
/* 1102:1064 */       editJoints();
/* 1103:     */     }
/* 1104:     */   }
/* 1105:     */   
/* 1106:     */   private void restartWithCurrentConditions()
/* 1107:     */   {
/* 1108:1069 */     recordRecentFileUse();
/* 1109:1070 */     this.bridge.initialize(this.bridge.getDesignConditions(), null, null);
/* 1110:1071 */     setSketchModel(null);
/* 1111:1072 */     showDrawingBoard();
/* 1112:1073 */     uploadBridgeToDraftingPanel();
/* 1113:1074 */     setDefaultFile();
/* 1114:1075 */     setLoadTestButtonEnabled();
/* 1115:1076 */     setSelected(this.editJointsMenuItem, true);
/* 1116:1077 */     editJoints();
/* 1117:     */   }
/* 1118:     */   
/* 1119:     */   private void setGrid(int density)
/* 1120:     */   {
/* 1121:1081 */     this.draftingPanel.getDraftingCoordinates().setDensity(density);
/* 1122:1082 */     this.horizontalRuler.repaint();
/* 1123:1083 */     this.verticalRuler.repaint();
/* 1124:     */   }
/* 1125:     */   
/* 1126:     */   private void setGridFiner(int density)
/* 1127:     */   {
/* 1128:1093 */     if (this.draftingPanel.getDraftingCoordinates().isFiner(density))
/* 1129:     */     {
/* 1130:1094 */       setGrid(density);
/* 1131:1095 */       switch (density)
/* 1132:     */       {
/* 1133:     */       case 0: 
/* 1134:1097 */         setSelected(this.setCoarseGridButton, true);
/* 1135:1098 */         break;
/* 1136:     */       case 1: 
/* 1137:1100 */         setSelected(this.setMediumGridButton, true);
/* 1138:1101 */         break;
/* 1139:     */       case 2: 
/* 1140:1103 */         setSelected(this.setFineGridButton, true);
/* 1141:     */       }
/* 1142:     */     }
/* 1143:     */   }
/* 1144:     */   
/* 1145:     */   private void selectRulers(boolean val)
/* 1146:     */   {
/* 1147:1115 */     setSelected(this.toggleRulerMenuItem, val);
/* 1148:1116 */     this.verticalRuler.setVisible(val);
/* 1149:1117 */     this.corner.setVisible(val);
/* 1150:1118 */     this.horizontalRuler.setVisible(val);
/* 1151:     */   }
/* 1152:     */   
/* 1153:     */   private void showWelcomeDialog()
/* 1154:     */   {
/* 1155:1125 */     if (this.welcomeDialog == null)
/* 1156:     */     {
/* 1157:1126 */       JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 1158:1127 */       this.welcomeDialog = new WelcomeDialog(mainFrame, this);
/* 1159:1128 */       this.welcomeDialog.setLocationRelativeTo(mainFrame);
/* 1160:     */     }
/* 1161:1130 */     this.welcomeDialog.setVisible(true);
/* 1162:1131 */     switch (this.welcomeDialog.getResult())
/* 1163:     */     {
/* 1164:     */     case 1: 
/* 1165:1133 */       newDesign();
/* 1166:1134 */       break;
/* 1167:     */     case 2: 
/* 1168:1136 */       loadSampleDesign();
/* 1169:1137 */       break;
/* 1170:     */     case 3: 
/* 1171:1139 */       open();
/* 1172:1140 */       break;
/* 1173:     */     case 0: 
/* 1174:1142 */       selectCard("nullPanel");
/* 1175:     */     }
/* 1176:     */   }
/* 1177:     */   
/* 1178:     */   public void showMemberEditPopup(int x, int y)
/* 1179:     */   {
/* 1180:1154 */     this.memberEditPopup.getRootPane().setDefaultButton(this.memberPopupDoneButton);
/* 1181:1155 */     this.memberEditPopup.pack();
/* 1182:1156 */     this.memberEditPopup.setLocation(x, y);
/* 1183:1157 */     this.popupStockSelector.matchSelection(this.bridge);
/* 1184:1158 */     this.bridge.clearSelectedJoint(true);
/* 1185:1159 */     this.memberEditPopup.setVisible(true);
/* 1186:     */   }
/* 1187:     */   
/* 1188:     */   public void showDraftingPopup(int x, int y)
/* 1189:     */   {
/* 1190:1169 */     this.draftingPopup.show(this.draftingJPanel, x, y);
/* 1191:     */   }
/* 1192:     */   
/* 1193:     */   private void showReadFailedMessage(Exception e)
/* 1194:     */   {
/* 1195:1178 */     showMessageDialog(getResourceMap().getString("readFailedMessage.text", new Object[] { e.getMessage() }));
/* 1196:     */   }
/* 1197:     */   
/* 1198:     */   private void showMessageDialog(String msg)
/* 1199:     */   {
/* 1200:1187 */     JOptionPane.showMessageDialog(getFrame(), msg, getResourceMap().getString("messageDialog.title", new Object[0]), 1);
/* 1201:     */   }
/* 1202:     */   
/* 1203:     */   public void openRecentFile(File file)
/* 1204:     */   {
/* 1205:1199 */     if (querySaveIfDirty())
/* 1206:     */     {
/* 1207:1200 */       this.animation.stop();
/* 1208:     */       try
/* 1209:     */       {
/* 1210:1202 */         this.bridge.read(file);
/* 1211:1203 */         this.fileChooser.setSelectedFile(file);
/* 1212:1204 */         initializePostBridgeLoad();
/* 1213:     */       }
/* 1214:     */       catch (IOException e)
/* 1215:     */       {
/* 1216:1206 */         selectCard("nullPanel");
/* 1217:1207 */         showReadFailedMessage(e);
/* 1218:     */       }
/* 1219:     */       finally
/* 1220:     */       {
/* 1221:1210 */         addRecentFilesToFileMenu();
/* 1222:     */       }
/* 1223:     */     }
/* 1224:     */   }
/* 1225:     */   
/* 1226:     */   public void showAbout()
/* 1227:     */   {
/* 1228:1219 */     about();
/* 1229:     */   }
/* 1230:     */   
/* 1231:     */   private void initComponents()
/* 1232:     */   {
/* 1233:1232 */     this.menuBar = new JMenuBar();
/* 1234:1233 */     this.fileMenu = new JMenu();
/* 1235:1234 */     this.newDesignMenuItem = new JMenuItem();
/* 1236:1235 */     this.fileMenuSeparator1 = new JSeparator();
/* 1237:1236 */     this.openMenuItem = new JMenuItem();
/* 1238:1237 */     this.saveMenuItem = new JMenuItem();
/* 1239:1238 */     this.saveAsMenuItem = new JMenuItem();
/* 1240:1239 */     this.fileMenuSeparator2 = new JSeparator();
/* 1241:1240 */     this.openSampleDesignMenuItem = new JMenuItem();
/* 1242:1241 */     this.loadTemplateMenuItem = new JMenuItem();
/* 1243:1242 */     this.saveAsSample = new JMenuItem();
/* 1244:1243 */     this.saveAsTemplate = new JMenuItem();
/* 1245:1244 */     this.printLoadedClassesMenuItem = new JMenuItem();
/* 1246:1245 */     this.fileMenuSeparator3 = new JSeparator();
/* 1247:1246 */     this.printMenuItem = new JMenuItem();
/* 1248:1247 */     this.fileMenuSeparator4 = new JSeparator();
/* 1249:1248 */     this.exitMenuItem = new JMenuItem();
/* 1250:1249 */     this.editMenu = new JMenu();
/* 1251:1250 */     this.selectallItem = new JMenuItem();
/* 1252:1251 */     this.deleteItem = new JMenuItem();
/* 1253:1252 */     this.editMenuSeparator1 = new JSeparator();
/* 1254:1253 */     this.undoItem = new JMenuItem();
/* 1255:1254 */     this.redoItem = new JMenuItem();
/* 1256:1255 */     this.editMenuSeparator2 = new JSeparator();
/* 1257:1256 */     this.back1iterationItem = new JMenuItem();
/* 1258:1257 */     this.forward1iterationItem = new JMenuItem();
/* 1259:1258 */     this.gotoIterationItem = new JMenuItem();
/* 1260:1259 */     this.viewMenu = new JMenu();
/* 1261:1260 */     this.toggleToolsMenuItem = new JCheckBoxMenuItem();
/* 1262:1261 */     this.toggleAnimationControlsMenuItem = new JCheckBoxMenuItem();
/* 1263:1262 */     this.toggleMemberListMenuItem = new JCheckBoxMenuItem();
/* 1264:1263 */     this.toggleRulerMenuItem = new JCheckBoxMenuItem();
/* 1265:1264 */     this.toggleTitleBlockMenuItem = new JCheckBoxMenuItem();
/* 1266:1265 */     this.toggleMemberNumbersMenuItem = new JCheckBoxMenuItem();
/* 1267:1266 */     this.toggleGuidesMenuItem = new JCheckBoxMenuItem();
/* 1268:1267 */     this.toggleTemplateMenuItem = new JCheckBoxMenuItem();
/* 1269:1268 */     this.viewSeparator1 = new JSeparator();
/* 1270:1269 */     this.coarseGridMenuItem = new JRadioButtonMenuItem();
/* 1271:1270 */     this.mediumGridMenuItem = new JRadioButtonMenuItem();
/* 1272:1271 */     this.fineGridMenuItem = new JRadioButtonMenuItem();
/* 1273:1272 */     this.toolsMenu = new JMenu();
/* 1274:1273 */     this.editJointsMenuItem = new JRadioButtonMenuItem();
/* 1275:1274 */     this.editMembersMenuItem = new JRadioButtonMenuItem();
/* 1276:1275 */     this.editSelectMenuItem = new JRadioButtonMenuItem();
/* 1277:1276 */     this.editEraseMenuItem = new JRadioButtonMenuItem();
/* 1278:1277 */     this.testMenu = new JMenu();
/* 1279:1278 */     this.drawingBoardMenuItem = new JRadioButtonMenuItem();
/* 1280:1279 */     this.loadTestMenuItem = new JRadioButtonMenuItem();
/* 1281:1280 */     this.testMenuSep01 = new JSeparator();
/* 1282:1281 */     this.toggleAnimationMenuItem = new JCheckBoxMenuItem();
/* 1283:1282 */     this.toggleLegacyGraphicsMenuItem = new JCheckBoxMenuItem();
/* 1284:1283 */     this.testMenuSep02 = new JPopupMenu.Separator();
/* 1285:1284 */     this.toggleAutoCorrectMenuItem = new JCheckBoxMenuItem();
/* 1286:1285 */     this.reportMenu = new JMenu();
/* 1287:1286 */     this.costReportMenuItem = new JMenuItem();
/* 1288:1287 */     this.loadTestReportMenuItem = new JMenuItem();
/* 1289:1288 */     JMenu helpMenu = new JMenu();
/* 1290:1289 */     this.howToDesignMenuItem = new JMenuItem();
/* 1291:1290 */     this.bridgeDesignWindowMenuItem = new JMenuItem();
/* 1292:1291 */     this.helpSeparator01 = new JSeparator();
/* 1293:1292 */     this.helpTopicsMenuItem = new JMenuItem();
/* 1294:1293 */     this.searchForHelpMenuItem = new JMenuItem();
/* 1295:1294 */     this.helpSeparator02 = new JSeparator();
/* 1296:1295 */     this.tipOfTheDayMenuItem = new JMenuItem();
/* 1297:1296 */     this.browseOurWebSiteMenuItem = new JMenuItem();
/* 1298:1297 */     JMenuItem aboutMenuItem = new JMenuItem();
/* 1299:1298 */     this.jMenuItem1 = new JMenuItem();
/* 1300:1299 */     this.statusPanel = new JPanel();
/* 1301:1300 */     JSeparator statusPanelSeparator = new JSeparator();
/* 1302:1301 */     this.statusMessageLabel = new JLabel();
/* 1303:1302 */     this.statusAnimationLabel = new JLabel();
/* 1304:1303 */     this.progressBar = new JProgressBar();
/* 1305:1304 */     this.mainPanel = new JPanel();
/* 1306:1305 */     this.topToolBar = new JToolBar();
/* 1307:1306 */     this.spacer3 = new JLabel();
/* 1308:1307 */     this.newButton = new JButton();
/* 1309:1308 */     this.openButton = new JButton();
/* 1310:1309 */     this.saveButton = new JButton();
/* 1311:1310 */     this.printButton = new JButton();
/* 1312:1311 */     this.separator6 = new JToolBar.Separator();
/* 1313:1312 */     this.drawingBoardButton = new JToggleButton();
/* 1314:1313 */     this.loadTestButton = new JToggleButton();
/* 1315:1314 */     this.separator3 = new JToolBar.Separator();
/* 1316:1315 */     this.selectAllButton = new JButton();
/* 1317:1316 */     this.deleteButton = new JButton();
/* 1318:1317 */     this.separator1 = new JToolBar.Separator();
/* 1319:1318 */     this.undoButton = new JButton();
/* 1320:1319 */     this.undoDropButton = UndoRedoDropButton.getUndoDropButton(this.bridge.getUndoManager());
/* 1321:1320 */     this.spacer6 = new JLabel();
/* 1322:1321 */     this.redoButton = new JButton();
/* 1323:1322 */     this.redoDropButton = UndoRedoDropButton.getRedoDropButton(this.bridge.getUndoManager());
/* 1324:1323 */     this.separator2 = new JToolBar.Separator();
/* 1325:1324 */     this.iterationLabel = new JLabel();
/* 1326:1325 */     this.iterationNumberLabel = new JLabel();
/* 1327:1326 */     this.back1iterationButton = new JButton();
/* 1328:1327 */     this.forward1iterationButton = new JButton();
/* 1329:1328 */     this.showGoToIterationButton = new JButton();
/* 1330:1329 */     this.separator8 = new JToolBar.Separator();
/* 1331:1330 */     this.costLabel = new JLabel();
/* 1332:1331 */     this.spacer7 = new JLabel();
/* 1333:1332 */     this.costReportButton = new JButton();
/* 1334:1333 */     this.separator4 = new JToolBar.Separator();
/* 1335:1334 */     this.statusLabel = new JLabel();
/* 1336:1335 */     this.separator5 = new JToolBar.Separator();
/* 1337:1336 */     this.loadTestReportButton = new JButton();
/* 1338:1337 */     this.bottomToolBar = new JToolBar();
/* 1339:1338 */     this.spacer0 = new JLabel();
/* 1340:1339 */     this.materialBox = new JComboBox();
/* 1341:1340 */     this.spacer1 = new JLabel();
/* 1342:1341 */     this.sectionBox = new JComboBox();
/* 1343:1342 */     this.spacer2 = new JLabel();
/* 1344:1343 */     this.sizeBox = new JComboBox();
/* 1345:1344 */     this.spacer4 = new JLabel();
/* 1346:1345 */     this.increaseMemberSizeButton = new JButton();
/* 1347:1346 */     this.decreaseMemberSizeButton = new JButton();
/* 1348:1347 */     this.spacer5 = new JLabel();
/* 1349:1348 */     this.toggleMemberListButton = new JToggleButton();
/* 1350:1349 */     this.toggleMemberNumbersButton = new JToggleButton();
/* 1351:1350 */     this.toggleGuidesButton = new JToggleButton();
/* 1352:1351 */     this.toggleTemplateButton = new JToggleButton();
/* 1353:1352 */     this.separator7 = new JToolBar.Separator();
/* 1354:1353 */     this.setCoarseGridButton = new JToggleButton();
/* 1355:1354 */     this.setMediumGridButton = new JToggleButton();
/* 1356:1355 */     this.setFineGridButton = new JToggleButton();
/* 1357:1356 */     this.cardPanel = new JPanel();
/* 1358:1357 */     this.nullPanel = new JPanel();
/* 1359:1358 */     this.designPanel = new JPanel();
/* 1360:1359 */     this.drawingPanel = new JPanel();
/* 1361:1360 */     this.draftingJPanel = new DraftingPanel(this.bridge, this.bridgeDraftingView, this);
/* 1362:1361 */     this.openMemberTableButton = new JButton();
/* 1363:1362 */     this.titleBlockPanel = new TitleBlockPanel(null);
/* 1364:1363 */     this.titleLabel = new JLabel();
/* 1365:1364 */     this.designedByLabel = new JLabel();
/* 1366:1365 */     this.designedByField = new JTextField();
/* 1367:1366 */     this.projectIDLabel = new JLabel();
/* 1368:1367 */     this.scenarioIDLabel = new JLabel();
/* 1369:1368 */     this.projectIDField = new JTextField();
/* 1370:1369 */     this.verticalRuler = new Ruler((RulerHost)this.draftingJPanel, 7);
/* 1371:1370 */     this.corner = new JLabel();
/* 1372:1371 */     this.horizontalRuler = new Ruler((RulerHost)this.draftingJPanel, 5);
/* 1373:1372 */     this.memberPanel = new JPanel();
/* 1374:1373 */     this.memberTabs = new JTabbedPane();
/* 1375:1374 */     this.memberListPanel = new JPanel();
/* 1376:1375 */     this.loadTestResultsLabel = new JLabel();
/* 1377:1376 */     this.memberScroll = new JScrollPane();
/* 1378:1377 */     this.memberJTable = new MemberTable();
/* 1379:1378 */     this.memberDetailTabs = new JTabbedPane();
/* 1380:1379 */     this.memberDetailPanel = new JPanel();
/* 1381:1380 */     this.materialPropertiesLabel = new JLabel();
/* 1382:1381 */     this.materialPropertiesTable = new JTable();
/* 1383:1382 */     this.dimensionsLabel = new JLabel();
/* 1384:1383 */     this.dimensionsTable = new JTable();
/* 1385:1384 */     this.sketchLabel = new JLabel();
/* 1386:1385 */     this.crossSectionSketchLabel = new CrossSectionSketch();
/* 1387:1386 */     this.memberCostLabel = new JLabel();
/* 1388:1387 */     this.memberCostTable = new JTable();
/* 1389:1388 */     this.strengthCurveLabel = new StrengthCurve();
/* 1390:1389 */     this.graphAllCheck = new JCheckBox();
/* 1391:1390 */     this.curveLabel = new JLabel();
/* 1392:1391 */     this.memberSelectLabel = new JLabel();
/* 1393:1392 */     this.memberSelecButtonPanel = new JPanel();
/* 1394:1393 */     this.memberSelectLeftButton = new JButton();
/* 1395:1394 */     this.memberSelectRightButton = new JButton();
/* 1396:1395 */     this.memberSelectBox = new ExtendedComboBox(this.memberSelectLeftButton, this.memberSelectRightButton);
/* 1397:1396 */     this.flyThruAnimationCanvas = this.flyThruAnimation.getCanvas();
/* 1398:1397 */     this.fixedEyeAnimationCanvas = this.fixedEyeAnimation.getCanvas();
/* 1399:1398 */     this.gridSizeButtonGroup = new ButtonGroup();
/* 1400:1399 */     this.toolMenuGroup = new ButtonGroup();
/* 1401:1400 */     this.designTestGroup = new ButtonGroup();
/* 1402:1401 */     this.toolsDialog = new JDialog(getFrame());
/* 1403:1402 */     this.toolsToolbar = new JToolBar();
/* 1404:1403 */     this.editJointsButton = new JToggleButton();
/* 1405:1404 */     this.editMembersButton = new JToggleButton();
/* 1406:1405 */     this.editSelectButton = new JToggleButton();
/* 1407:1406 */     this.editEraseButton = new JToggleButton();
/* 1408:1407 */     this.memberEditPopup = new JDialog();
/* 1409:1408 */     this.memberPopupPanel = new JPanel();
/* 1410:1409 */     this.memberPopupMaterialBox = new JComboBox();
/* 1411:1410 */     this.memberPopupMaterialLabel = new JLabel();
/* 1412:1411 */     this.memberPopupSectionBox = new JComboBox();
/* 1413:1412 */     this.memberPopupSectionLabel = new JLabel();
/* 1414:1413 */     this.memberPopupSizeLabel = new JLabel();
/* 1415:1414 */     this.memberPopupSizeBox = new JComboBox();
/* 1416:1415 */     this.memberPopupIncreaseSizeButton = new JButton();
/* 1417:1416 */     this.memberPopupDecreaseSizeButton = new JButton();
/* 1418:1417 */     this.memberPopupDeleteButton = new JButton();
/* 1419:1418 */     this.memberPopupDoneButton = new JButton();
/* 1420:1419 */     this.memberPopupMemberListButton = new JToggleButton();
/* 1421:1420 */     this.draftingPopup = new JPopupMenu();
/* 1422:1421 */     this.draftingPopupJoints = new JRadioButtonMenuItem();
/* 1423:1422 */     this.draftingPopupMembers = new JRadioButtonMenuItem();
/* 1424:1423 */     this.draftingPopupSelect = new JRadioButtonMenuItem();
/* 1425:1424 */     this.draftingPopupErase = new JRadioButtonMenuItem();
/* 1426:1425 */     this.draftingPopupSep01 = new JSeparator();
/* 1427:1426 */     this.draftingPopupSelectAll = new JMenuItem();
/* 1428:1427 */     this.draftingPopupSep02 = new JSeparator();
/* 1429:1428 */     this.draftingPopupMemberList = new JCheckBoxMenuItem();
/* 1430:1429 */     this.draftingPopupSep03 = new JSeparator();
/* 1431:1430 */     this.draftingPopupCoarseGrid = new JRadioButtonMenuItem();
/* 1432:1431 */     this.draftingPopupMediumGrid = new JRadioButtonMenuItem();
/* 1433:1432 */     this.draftingPopupFineGrid = new JRadioButtonMenuItem();
/* 1434:1433 */     this.animationButtonGroup = new ButtonGroup();
/* 1435:1434 */     this.toolsButtonGroup = new ButtonGroup();
/* 1436:1435 */     this.keyCodeDialog = new JDialog();
/* 1437:1436 */     this.keyCodeLabel = new JLabel();
/* 1438:1437 */     this.keyCodeTextField = new JTextField();
/* 1439:1438 */     this.keyCodeOkButton = new JButton();
/* 1440:1439 */     this.keyCodeCancelButton = new JButton();
/* 1441:1440 */     this.keyCodeErrorLabel = new JLabel();
/* 1442:1441 */     this.drawingBoardLabel = new JLabel();
/* 1443:     */     
/* 1444:1443 */     this.menuBar.setName("menuBar");
/* 1445:     */     
/* 1446:1445 */     this.fileMenu.setMnemonic('F');
/* 1447:1446 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(WPBDView.class);
/* 1448:1447 */     this.fileMenu.setText(resourceMap.getString("fileMenu.text", new Object[0]));
/* 1449:1448 */     this.fileMenu.setName("fileMenu");
/* 1450:     */     
/* 1451:1450 */     ActionMap actionMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getActionMap(WPBDView.class, this);
/* 1452:1451 */     this.newDesignMenuItem.setAction(actionMap.get("newDesign"));
/* 1453:1452 */     this.newDesignMenuItem.setText(resourceMap.getString("newDesignMenuItem.text", new Object[0]));
/* 1454:1453 */     this.newDesignMenuItem.setName("newDesignMenuItem");
/* 1455:1454 */     this.fileMenu.add(this.newDesignMenuItem);
/* 1456:     */     
/* 1457:1456 */     this.fileMenuSeparator1.setName("fileMenuSeparator1");
/* 1458:1457 */     this.fileMenu.add(this.fileMenuSeparator1);
/* 1459:     */     
/* 1460:1459 */     this.openMenuItem.setAction(actionMap.get("open"));
/* 1461:1460 */     this.openMenuItem.setName("openMenuItem");
/* 1462:1461 */     this.fileMenu.add(this.openMenuItem);
/* 1463:     */     
/* 1464:1463 */     this.saveMenuItem.setAction(actionMap.get("save"));
/* 1465:1464 */     this.saveMenuItem.setName("saveMenuItem");
/* 1466:1465 */     this.fileMenu.add(this.saveMenuItem);
/* 1467:     */     
/* 1468:1467 */     this.saveAsMenuItem.setAction(actionMap.get("saveas"));
/* 1469:1468 */     this.saveAsMenuItem.setText(resourceMap.getString("saveAsMenuItem.text", new Object[0]));
/* 1470:1469 */     this.saveAsMenuItem.setName("saveAsMenuItem");
/* 1471:1470 */     this.fileMenu.add(this.saveAsMenuItem);
/* 1472:     */     
/* 1473:1472 */     this.fileMenuSeparator2.setName("fileMenuSeparator2");
/* 1474:1473 */     this.fileMenu.add(this.fileMenuSeparator2);
/* 1475:     */     
/* 1476:1475 */     this.openSampleDesignMenuItem.setAction(actionMap.get("loadSampleDesign"));
/* 1477:1476 */     this.openSampleDesignMenuItem.setText(resourceMap.getString("openSampleDesignMenuItem.text", new Object[0]));
/* 1478:1477 */     this.openSampleDesignMenuItem.setName("openSampleDesignMenuItem");
/* 1479:1478 */     this.fileMenu.add(this.openSampleDesignMenuItem);
/* 1480:     */     
/* 1481:1480 */     this.loadTemplateMenuItem.setAction(actionMap.get("loadTemplate"));
/* 1482:1481 */     this.loadTemplateMenuItem.setText(resourceMap.getString("loadTemplateMenuItem.text", new Object[0]));
/* 1483:1482 */     this.loadTemplateMenuItem.setName("loadTemplateMenuItem");
/* 1484:1483 */     this.fileMenu.add(this.loadTemplateMenuItem);
/* 1485:     */     
/* 1486:1485 */     this.saveAsSample.setAction(actionMap.get("saveAsSample"));
/* 1487:1486 */     this.saveAsSample.setName("saveAsSample");
/* 1488:1487 */     this.fileMenu.add(this.saveAsSample);
/* 1489:     */     
/* 1490:1489 */     this.saveAsTemplate.setAction(actionMap.get("saveAsTemplate"));
/* 1491:1490 */     this.saveAsTemplate.setName("saveAsTemplate");
/* 1492:1491 */     this.fileMenu.add(this.saveAsTemplate);
/* 1493:     */     
/* 1494:1493 */     this.printLoadedClassesMenuItem.setAction(actionMap.get("printClasses"));
/* 1495:1494 */     this.printLoadedClassesMenuItem.setName("printLoadedClassesMenuItem");
/* 1496:1495 */     this.fileMenu.add(this.printLoadedClassesMenuItem);
/* 1497:     */     
/* 1498:1497 */     this.fileMenuSeparator3.setName("fileMenuSeparator3");
/* 1499:1498 */     this.fileMenu.add(this.fileMenuSeparator3);
/* 1500:     */     
/* 1501:1500 */     this.printMenuItem.setAction(actionMap.get("print"));
/* 1502:1501 */     this.printMenuItem.setName("printMenuItem");
/* 1503:1502 */     this.fileMenu.add(this.printMenuItem);
/* 1504:     */     
/* 1505:1504 */     this.fileMenuSeparator4.setName("fileMenuSeparator4");
/* 1506:1505 */     this.fileMenu.add(this.fileMenuSeparator4);
/* 1507:     */     
/* 1508:1507 */     this.exitMenuItem.setAction(actionMap.get("quit"));
/* 1509:1508 */     this.exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon"));
/* 1510:1509 */     this.exitMenuItem.setName("exitMenuItem");
/* 1511:1510 */     this.fileMenu.add(this.exitMenuItem);
/* 1512:1511 */     this.exitMenuItem.getAccessibleContext().setAccessibleDescription(resourceMap.getString("exitMenuItem.AccessibleContext.accessibleDescription", new Object[0]));
/* 1513:     */     
/* 1514:1513 */     this.menuBar.add(this.fileMenu);
/* 1515:     */     
/* 1516:1515 */     this.editMenu.setMnemonic('E');
/* 1517:1516 */     this.editMenu.setText(resourceMap.getString("editMenu.text", new Object[0]));
/* 1518:1517 */     this.editMenu.setName("editMenu");
/* 1519:     */     
/* 1520:1519 */     this.selectallItem.setAction(actionMap.get("selectAll"));
/* 1521:1520 */     this.selectallItem.setName("selectallItem");
/* 1522:1521 */     this.editMenu.add(this.selectallItem);
/* 1523:     */     
/* 1524:1523 */     this.deleteItem.setAction(actionMap.get("delete"));
/* 1525:1524 */     this.deleteItem.setName("deleteItem");
/* 1526:1525 */     this.editMenu.add(this.deleteItem);
/* 1527:     */     
/* 1528:1527 */     this.editMenuSeparator1.setName("editMenuSeparator1");
/* 1529:1528 */     this.editMenu.add(this.editMenuSeparator1);
/* 1530:     */     
/* 1531:1530 */     this.undoItem.setAction(actionMap.get("undo"));
/* 1532:1531 */     this.undoItem.setName("undoItem");
/* 1533:1532 */     this.editMenu.add(this.undoItem);
/* 1534:     */     
/* 1535:1534 */     this.redoItem.setAction(actionMap.get("redo"));
/* 1536:1535 */     this.redoItem.setName("redoItem");
/* 1537:1536 */     this.editMenu.add(this.redoItem);
/* 1538:     */     
/* 1539:1538 */     this.editMenuSeparator2.setName("editMenuSeparator2");
/* 1540:1539 */     this.editMenu.add(this.editMenuSeparator2);
/* 1541:     */     
/* 1542:1541 */     this.back1iterationItem.setAction(actionMap.get("back1iteration"));
/* 1543:1542 */     this.back1iterationItem.setName("back1iterationItem");
/* 1544:1543 */     this.editMenu.add(this.back1iterationItem);
/* 1545:     */     
/* 1546:1545 */     this.forward1iterationItem.setAction(actionMap.get("forward1iteration"));
/* 1547:1546 */     this.forward1iterationItem.setName("forward1iterationItem");
/* 1548:1547 */     this.editMenu.add(this.forward1iterationItem);
/* 1549:     */     
/* 1550:1549 */     this.gotoIterationItem.setAction(actionMap.get("gotoIteration"));
/* 1551:1550 */     this.gotoIterationItem.setName("gotoIterationItem");
/* 1552:1551 */     this.editMenu.add(this.gotoIterationItem);
/* 1553:     */     
/* 1554:1553 */     this.menuBar.add(this.editMenu);
/* 1555:     */     
/* 1556:1555 */     this.viewMenu.setMnemonic('V');
/* 1557:1556 */     this.viewMenu.setText(resourceMap.getString("viewMenu.text", new Object[0]));
/* 1558:     */     
/* 1559:1558 */     this.toggleToolsMenuItem.setAction(actionMap.get("toggleTools"));
/* 1560:1559 */     this.toggleToolsMenuItem.setSelected(true);
/* 1561:1560 */     this.toggleToolsMenuItem.setName("toggleToolsMenuItem");
/* 1562:1561 */     this.viewMenu.add(this.toggleToolsMenuItem);
/* 1563:     */     
/* 1564:1563 */     this.toggleAnimationControlsMenuItem.setAction(actionMap.get("toggleAnimationControls"));
/* 1565:1564 */     this.toggleAnimationControlsMenuItem.setSelected(true);
/* 1566:1565 */     this.toggleAnimationControlsMenuItem.setName("toggleAnimationControlsMenuItem");
/* 1567:1566 */     this.viewMenu.add(this.toggleAnimationControlsMenuItem);
/* 1568:     */     
/* 1569:1568 */     this.toggleMemberListMenuItem.setAction(actionMap.get("toggleMemberList"));
/* 1570:1569 */     this.toggleMemberListMenuItem.setSelected(true);
/* 1571:1570 */     this.toggleMemberListMenuItem.setName("toggleMemberListMenuItem");
/* 1572:1571 */     this.viewMenu.add(this.toggleMemberListMenuItem);
/* 1573:     */     
/* 1574:1573 */     this.toggleRulerMenuItem.setAction(actionMap.get("toggleRulers"));
/* 1575:1574 */     this.toggleRulerMenuItem.setSelected(true);
/* 1576:1575 */     this.toggleRulerMenuItem.setName("toggleRulerMenuItem");
/* 1577:1576 */     this.viewMenu.add(this.toggleRulerMenuItem);
/* 1578:     */     
/* 1579:1578 */     this.toggleTitleBlockMenuItem.setAction(actionMap.get("toggleTitleBlock"));
/* 1580:1579 */     this.toggleTitleBlockMenuItem.setSelected(true);
/* 1581:1580 */     this.toggleTitleBlockMenuItem.setName("toggleTitleBlockMenuItem");
/* 1582:1581 */     this.viewMenu.add(this.toggleTitleBlockMenuItem);
/* 1583:     */     
/* 1584:1583 */     this.toggleMemberNumbersMenuItem.setAction(actionMap.get("toggleMemberNumbers"));
/* 1585:1584 */     this.toggleMemberNumbersMenuItem.setSelected(true);
/* 1586:1585 */     this.toggleMemberNumbersMenuItem.setName("toggleMemberNumbersMenuItem");
/* 1587:1586 */     this.viewMenu.add(this.toggleMemberNumbersMenuItem);
/* 1588:     */     
/* 1589:1588 */     this.toggleGuidesMenuItem.setAction(actionMap.get("toggleGuides"));
/* 1590:1589 */     this.toggleGuidesMenuItem.setSelected(true);
/* 1591:1590 */     this.toggleGuidesMenuItem.setName("toggleGuidesMenuItem");
/* 1592:1591 */     this.viewMenu.add(this.toggleGuidesMenuItem);
/* 1593:     */     
/* 1594:1593 */     this.toggleTemplateMenuItem.setAction(actionMap.get("toggleTemplate"));
/* 1595:1594 */     this.toggleTemplateMenuItem.setSelected(true);
/* 1596:1595 */     this.toggleTemplateMenuItem.setName("toggleTemplateMenuItem");
/* 1597:1596 */     this.viewMenu.add(this.toggleTemplateMenuItem);
/* 1598:     */     
/* 1599:1598 */     this.viewSeparator1.setName("viewSeparator1");
/* 1600:1599 */     this.viewMenu.add(this.viewSeparator1);
/* 1601:     */     
/* 1602:1601 */     this.coarseGridMenuItem.setAction(actionMap.get("setCoarseGrid"));
/* 1603:1602 */     this.coarseGridMenuItem.setSelected(true);
/* 1604:1603 */     this.coarseGridMenuItem.setName("coarseGridMenuItem");
/* 1605:1604 */     this.viewMenu.add(this.coarseGridMenuItem);
/* 1606:     */     
/* 1607:1606 */     this.mediumGridMenuItem.setAction(actionMap.get("setMediumGrid"));
/* 1608:1607 */     this.mediumGridMenuItem.setName("mediumGridMenuItem");
/* 1609:1608 */     this.viewMenu.add(this.mediumGridMenuItem);
/* 1610:     */     
/* 1611:1610 */     this.fineGridMenuItem.setAction(actionMap.get("setFineGrid"));
/* 1612:1611 */     this.fineGridMenuItem.setName("fineGridMenuItem");
/* 1613:1612 */     this.viewMenu.add(this.fineGridMenuItem);
/* 1614:     */     
/* 1615:1614 */     this.menuBar.add(this.viewMenu);
/* 1616:     */     
/* 1617:1616 */     this.toolsMenu.setMnemonic('T');
/* 1618:1617 */     this.toolsMenu.setText(resourceMap.getString("toolsMenu.text", new Object[0]));
/* 1619:1618 */     this.toolsMenu.setName("toolsMenu");
/* 1620:     */     
/* 1621:1620 */     this.editJointsMenuItem.setAction(actionMap.get("editJoints"));
/* 1622:1621 */     this.toolMenuGroup.add(this.editJointsMenuItem);
/* 1623:1622 */     this.editJointsMenuItem.setSelected(true);
/* 1624:1623 */     this.editJointsMenuItem.setName("editJointsMenuItem");
/* 1625:1624 */     this.toolsMenu.add(this.editJointsMenuItem);
/* 1626:     */     
/* 1627:1626 */     this.editMembersMenuItem.setAction(actionMap.get("editMembers"));
/* 1628:1627 */     this.toolMenuGroup.add(this.editMembersMenuItem);
/* 1629:1628 */     this.editMembersMenuItem.setName("editMembersMenuItem");
/* 1630:1629 */     this.toolsMenu.add(this.editMembersMenuItem);
/* 1631:     */     
/* 1632:1631 */     this.editSelectMenuItem.setAction(actionMap.get("editSelect"));
/* 1633:1632 */     this.toolMenuGroup.add(this.editSelectMenuItem);
/* 1634:1633 */     this.editSelectMenuItem.setName("editSelectMenuItem");
/* 1635:1634 */     this.toolsMenu.add(this.editSelectMenuItem);
/* 1636:     */     
/* 1637:1636 */     this.editEraseMenuItem.setAction(actionMap.get("editErase"));
/* 1638:1637 */     this.toolMenuGroup.add(this.editEraseMenuItem);
/* 1639:1638 */     this.editEraseMenuItem.setName("editEraseMenuItem");
/* 1640:1639 */     this.toolsMenu.add(this.editEraseMenuItem);
/* 1641:     */     
/* 1642:1641 */     this.menuBar.add(this.toolsMenu);
/* 1643:     */     
/* 1644:1643 */     this.testMenu.setMnemonic('s');
/* 1645:1644 */     this.testMenu.setText(resourceMap.getString("testMenu.text", new Object[0]));
/* 1646:1645 */     this.testMenu.setName("testMenu");
/* 1647:     */     
/* 1648:1647 */     this.drawingBoardMenuItem.setAction(actionMap.get("showDrawingBoard"));
/* 1649:1648 */     this.drawingBoardMenuItem.setSelected(true);
/* 1650:1649 */     this.drawingBoardMenuItem.setName("drawingBoardMenuItem");
/* 1651:1650 */     this.testMenu.add(this.drawingBoardMenuItem);
/* 1652:     */     
/* 1653:1652 */     this.loadTestMenuItem.setAction(actionMap.get("runLoadTest"));
/* 1654:1653 */     this.loadTestMenuItem.setName("loadTestMenuItem");
/* 1655:1654 */     this.testMenu.add(this.loadTestMenuItem);
/* 1656:     */     
/* 1657:1656 */     this.testMenuSep01.setName("testMenuSep01");
/* 1658:1657 */     this.testMenu.add(this.testMenuSep01);
/* 1659:     */     
/* 1660:1659 */     this.toggleAnimationMenuItem.setAction(actionMap.get("toggleShowAnimation"));
/* 1661:1660 */     this.toggleAnimationMenuItem.setSelected(true);
/* 1662:1661 */     this.toggleAnimationMenuItem.setName("toggleAnimationMenuItem");
/* 1663:1662 */     this.testMenu.add(this.toggleAnimationMenuItem);
/* 1664:     */     
/* 1665:1664 */     this.toggleLegacyGraphicsMenuItem.setAction(actionMap.get("toggleLegacyGraphics"));
/* 1666:1665 */     this.toggleLegacyGraphicsMenuItem.setSelected(true);
/* 1667:1666 */     this.toggleLegacyGraphicsMenuItem.setName("toggleLegacyGraphicsMenuItem");
/* 1668:1667 */     this.testMenu.add(this.toggleLegacyGraphicsMenuItem);
/* 1669:     */     
/* 1670:1669 */     this.testMenuSep02.setName("testMenuSep02");
/* 1671:1670 */     this.testMenu.add(this.testMenuSep02);
/* 1672:     */     
/* 1673:1672 */     this.toggleAutoCorrectMenuItem.setAction(actionMap.get("toggleAutoCorrect"));
/* 1674:1673 */     this.toggleAutoCorrectMenuItem.setSelected(true);
/* 1675:1674 */     this.toggleAutoCorrectMenuItem.setName("toggleAutoCorrectMenuItem");
/* 1676:1675 */     this.testMenu.add(this.toggleAutoCorrectMenuItem);
/* 1677:     */     
/* 1678:1677 */     this.menuBar.add(this.testMenu);
/* 1679:     */     
/* 1680:1679 */     this.reportMenu.setMnemonic('R');
/* 1681:1680 */     this.reportMenu.setText(resourceMap.getString("reportMenu.text", new Object[0]));
/* 1682:1681 */     this.reportMenu.setName("reportMenu");
/* 1683:     */     
/* 1684:1683 */     this.costReportMenuItem.setAction(actionMap.get("showCostDialog"));
/* 1685:1684 */     this.costReportMenuItem.setName("costReportMenuItem");
/* 1686:1685 */     this.reportMenu.add(this.costReportMenuItem);
/* 1687:     */     
/* 1688:1687 */     this.loadTestReportMenuItem.setAction(actionMap.get("showLoadTestReport"));
/* 1689:1688 */     this.loadTestReportMenuItem.setName("loadTestReportMenuItem");
/* 1690:1689 */     this.reportMenu.add(this.loadTestReportMenuItem);
/* 1691:     */     
/* 1692:1691 */     this.menuBar.add(this.reportMenu);
/* 1693:     */     
/* 1694:1693 */     helpMenu.setMnemonic('H');
/* 1695:1694 */     helpMenu.setText(resourceMap.getString("helpMenu.text", new Object[0]));
/* 1696:1695 */     helpMenu.setName("helpMenu");
/* 1697:     */     
/* 1698:1697 */     this.howToDesignMenuItem.setAction(actionMap.get("howToDesignABridge"));
/* 1699:1698 */     this.howToDesignMenuItem.setName("howToDesignMenuItem");
/* 1700:1699 */     helpMenu.add(this.howToDesignMenuItem);
/* 1701:     */     
/* 1702:1701 */     this.bridgeDesignWindowMenuItem.setAction(actionMap.get("theBridgeDesignWindow"));
/* 1703:1702 */     this.bridgeDesignWindowMenuItem.setName("bridgeDesignWindowMenuItem");
/* 1704:1703 */     helpMenu.add(this.bridgeDesignWindowMenuItem);
/* 1705:     */     
/* 1706:1705 */     this.helpSeparator01.setName("helpSeparator01");
/* 1707:1706 */     helpMenu.add(this.helpSeparator01);
/* 1708:     */     
/* 1709:1708 */     this.helpTopicsMenuItem.setText(resourceMap.getString("helpTopicsMenuItem.text", new Object[0]));
/* 1710:1709 */     this.helpTopicsMenuItem.setName("helpTopicsMenuItem");
/* 1711:1710 */     this.helpTopicsMenuItem.addActionListener(new ActionListener()
/* 1712:     */     {
/* 1713:     */       public void actionPerformed(ActionEvent evt)
/* 1714:     */       {
/* 1715:1712 */         WPBDView.this.helpTopicsMenuItemActionPerformed(evt);
/* 1716:     */       }
/* 1717:1714 */     });
/* 1718:1715 */     helpMenu.add(this.helpTopicsMenuItem);
/* 1719:     */     
/* 1720:1717 */     this.searchForHelpMenuItem.setText(resourceMap.getString("searchForHelpMenuItem.text", new Object[0]));
/* 1721:1718 */     this.searchForHelpMenuItem.setName("searchForHelpMenuItem");
/* 1722:1719 */     this.searchForHelpMenuItem.addActionListener(new ActionListener()
/* 1723:     */     {
/* 1724:     */       public void actionPerformed(ActionEvent evt)
/* 1725:     */       {
/* 1726:1721 */         WPBDView.this.searchForHelpMenuItemActionPerformed(evt);
/* 1727:     */       }
/* 1728:1723 */     });
/* 1729:1724 */     helpMenu.add(this.searchForHelpMenuItem);
/* 1730:     */     
/* 1731:1726 */     this.helpSeparator02.setName("helpSeparator02");
/* 1732:1727 */     helpMenu.add(this.helpSeparator02);
/* 1733:     */     
/* 1734:1729 */     this.tipOfTheDayMenuItem.setAction(actionMap.get("showTipOfTheDay"));
/* 1735:1730 */     this.tipOfTheDayMenuItem.setName("tipOfTheDayMenuItem");
/* 1736:1731 */     helpMenu.add(this.tipOfTheDayMenuItem);
/* 1737:     */     
/* 1738:1733 */     this.browseOurWebSiteMenuItem.setAction(actionMap.get("browseOurWebSite"));
/* 1739:1734 */     this.browseOurWebSiteMenuItem.setName("browseOurWebSiteMenuItem");
/* 1740:1735 */     helpMenu.add(this.browseOurWebSiteMenuItem);
/* 1741:     */     
/* 1742:1737 */     aboutMenuItem.setAction(actionMap.get("about"));
/* 1743:1738 */     aboutMenuItem.setName("aboutMenuItem");
/* 1744:1739 */     helpMenu.add(aboutMenuItem);
/* 1745:     */     
/* 1746:1741 */     this.jMenuItem1.setAction(actionMap.get("whatsNew"));
/* 1747:1742 */     this.jMenuItem1.setName("jMenuItem1");
/* 1748:1743 */     helpMenu.add(this.jMenuItem1);
/* 1749:     */     
/* 1750:1745 */     this.menuBar.add(helpMenu);
/* 1751:     */     
/* 1752:1747 */     this.statusPanel.setName("statusPanel");
/* 1753:     */     
/* 1754:1749 */     statusPanelSeparator.setName("statusPanelSeparator");
/* 1755:     */     
/* 1756:1751 */     this.statusMessageLabel.setName("statusMessageLabel");
/* 1757:     */     
/* 1758:1753 */     this.statusAnimationLabel.setHorizontalAlignment(2);
/* 1759:1754 */     this.statusAnimationLabel.setName("statusAnimationLabel");
/* 1760:     */     
/* 1761:1756 */     this.progressBar.setName("progressBar");
/* 1762:     */     
/* 1763:1758 */     GroupLayout statusPanelLayout = new GroupLayout(this.statusPanel);
/* 1764:1759 */     this.statusPanel.setLayout(statusPanelLayout);
/* 1765:1760 */     statusPanelLayout.setHorizontalGroup(statusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(statusPanelSeparator, -1, 1154, 32767).addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.statusMessageLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 979, 32767).addComponent(this.progressBar, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.statusAnimationLabel).addContainerGap()));
/* 1766:     */     
/* 1767:     */ 
/* 1768:     */ 
/* 1769:     */ 
/* 1770:     */ 
/* 1771:     */ 
/* 1772:     */ 
/* 1773:     */ 
/* 1774:     */ 
/* 1775:     */ 
/* 1776:     */ 
/* 1777:1772 */     statusPanelLayout.setVerticalGroup(statusPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(statusPanelLayout.createSequentialGroup().addComponent(statusPanelSeparator, -2, 2, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(statusPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.statusMessageLabel).addComponent(this.statusAnimationLabel).addComponent(this.progressBar, -2, -1, -2)).addGap(3, 3, 3)));
/* 1778:     */     
/* 1779:     */ 
/* 1780:     */ 
/* 1781:     */ 
/* 1782:     */ 
/* 1783:     */ 
/* 1784:     */ 
/* 1785:     */ 
/* 1786:     */ 
/* 1787:     */ 
/* 1788:     */ 
/* 1789:1784 */     this.mainPanel.setName("mainPanel");
/* 1790:1785 */     this.mainPanel.setPreferredSize(new Dimension(800, 600));
/* 1791:     */     
/* 1792:1787 */     this.topToolBar.setFloatable(false);
/* 1793:1788 */     this.topToolBar.setRollover(true);
/* 1794:1789 */     this.topToolBar.setMaximumSize(new Dimension(32767, 32));
/* 1795:1790 */     this.topToolBar.setMinimumSize(new Dimension(1, 32));
/* 1796:1791 */     this.topToolBar.setName("topToolBar");
/* 1797:1792 */     this.topToolBar.setPreferredSize(new Dimension(100, 32));
/* 1798:     */     
/* 1799:1794 */     this.spacer3.setHorizontalAlignment(2);
/* 1800:1795 */     this.spacer3.setText(resourceMap.getString("spacer3.text", new Object[0]));
/* 1801:1796 */     this.spacer3.setEnabled(false);
/* 1802:1797 */     this.spacer3.setFocusable(false);
/* 1803:1798 */     this.spacer3.setHorizontalTextPosition(2);
/* 1804:1799 */     this.spacer3.setInheritsPopupMenu(false);
/* 1805:1800 */     this.spacer3.setMaximumSize(new Dimension(8, 16));
/* 1806:1801 */     this.spacer3.setMinimumSize(new Dimension(8, 16));
/* 1807:1802 */     this.spacer3.setName("spacer3");
/* 1808:1803 */     this.spacer3.setOpaque(true);
/* 1809:1804 */     this.spacer3.setPreferredSize(new Dimension(8, 16));
/* 1810:1805 */     this.spacer3.setRequestFocusEnabled(false);
/* 1811:1806 */     this.spacer3.setVerifyInputWhenFocusTarget(false);
/* 1812:1807 */     this.topToolBar.add(this.spacer3);
/* 1813:     */     
/* 1814:1809 */     this.newButton.setAction(actionMap.get("newDesign"));
/* 1815:1810 */     this.newButton.setHideActionText(true);
/* 1816:1811 */     this.newButton.setHorizontalTextPosition(0);
/* 1817:1812 */     this.newButton.setMaximumSize(new Dimension(27, 27));
/* 1818:1813 */     this.newButton.setMinimumSize(new Dimension(27, 27));
/* 1819:1814 */     this.newButton.setName("newButton");
/* 1820:1815 */     this.newButton.setPreferredSize(new Dimension(27, 27));
/* 1821:1816 */     this.newButton.setVerticalTextPosition(3);
/* 1822:1817 */     this.topToolBar.add(this.newButton);
/* 1823:     */     
/* 1824:1819 */     this.openButton.setAction(actionMap.get("open"));
/* 1825:1820 */     this.openButton.setHideActionText(true);
/* 1826:1821 */     this.openButton.setHorizontalTextPosition(0);
/* 1827:1822 */     this.openButton.setMaximumSize(new Dimension(27, 27));
/* 1828:1823 */     this.openButton.setMinimumSize(new Dimension(27, 27));
/* 1829:1824 */     this.openButton.setName("openButton");
/* 1830:1825 */     this.openButton.setPreferredSize(new Dimension(27, 27));
/* 1831:1826 */     this.openButton.setVerticalTextPosition(3);
/* 1832:1827 */     this.topToolBar.add(this.openButton);
/* 1833:     */     
/* 1834:1829 */     this.saveButton.setAction(actionMap.get("save"));
/* 1835:1830 */     this.saveButton.setHideActionText(true);
/* 1836:1831 */     this.saveButton.setHorizontalTextPosition(0);
/* 1837:1832 */     this.saveButton.setMaximumSize(new Dimension(27, 27));
/* 1838:1833 */     this.saveButton.setMinimumSize(new Dimension(27, 27));
/* 1839:1834 */     this.saveButton.setName("saveButton");
/* 1840:1835 */     this.saveButton.setPreferredSize(new Dimension(27, 27));
/* 1841:1836 */     this.saveButton.setVerticalTextPosition(3);
/* 1842:1837 */     this.topToolBar.add(this.saveButton);
/* 1843:     */     
/* 1844:1839 */     this.printButton.setAction(actionMap.get("printToDefaultPrinter"));
/* 1845:1840 */     this.printButton.setHideActionText(true);
/* 1846:1841 */     this.printButton.setHorizontalTextPosition(0);
/* 1847:1842 */     this.printButton.setMaximumSize(new Dimension(29, 29));
/* 1848:1843 */     this.printButton.setMinimumSize(new Dimension(29, 29));
/* 1849:1844 */     this.printButton.setName("printButton");
/* 1850:1845 */     this.printButton.setPreferredSize(new Dimension(29, 29));
/* 1851:1846 */     this.printButton.setVerticalTextPosition(3);
/* 1852:1847 */     this.topToolBar.add(this.printButton);
/* 1853:     */     
/* 1854:1849 */     this.separator6.setMaximumSize(new Dimension(12, 32767));
/* 1855:1850 */     this.separator6.setName("separator6");
/* 1856:1851 */     this.topToolBar.add(this.separator6);
/* 1857:     */     
/* 1858:1853 */     this.drawingBoardButton.setAction(actionMap.get("showDrawingBoard"));
/* 1859:1854 */     this.designTestGroup.add(this.drawingBoardButton);
/* 1860:1855 */     this.drawingBoardButton.setSelected(true);
/* 1861:1856 */     this.drawingBoardButton.setHideActionText(true);
/* 1862:1857 */     this.drawingBoardButton.setHorizontalTextPosition(0);
/* 1863:1858 */     this.drawingBoardButton.setMaximumSize(new Dimension(29, 29));
/* 1864:1859 */     this.drawingBoardButton.setMinimumSize(new Dimension(29, 29));
/* 1865:1860 */     this.drawingBoardButton.setName("drawingBoardButton");
/* 1866:1861 */     this.drawingBoardButton.setPreferredSize(new Dimension(29, 29));
/* 1867:1862 */     this.drawingBoardButton.setVerticalTextPosition(3);
/* 1868:1863 */     this.topToolBar.add(this.drawingBoardButton);
/* 1869:     */     
/* 1870:1865 */     this.loadTestButton.setAction(actionMap.get("runLoadTest"));
/* 1871:1866 */     this.designTestGroup.add(this.loadTestButton);
/* 1872:1867 */     this.loadTestButton.setHideActionText(true);
/* 1873:1868 */     this.loadTestButton.setHorizontalTextPosition(0);
/* 1874:1869 */     this.loadTestButton.setMaximumSize(new Dimension(29, 29));
/* 1875:1870 */     this.loadTestButton.setMinimumSize(new Dimension(29, 29));
/* 1876:1871 */     this.loadTestButton.setName("loadTestButton");
/* 1877:1872 */     this.loadTestButton.setPreferredSize(new Dimension(29, 29));
/* 1878:1873 */     this.loadTestButton.setVerticalTextPosition(3);
/* 1879:1874 */     this.topToolBar.add(this.loadTestButton);
/* 1880:     */     
/* 1881:1876 */     this.separator3.setMaximumSize(new Dimension(12, 32767));
/* 1882:1877 */     this.separator3.setName("separator3");
/* 1883:1878 */     this.topToolBar.add(this.separator3);
/* 1884:     */     
/* 1885:1880 */     this.selectAllButton.setAction(actionMap.get("selectAll"));
/* 1886:1881 */     this.selectAllButton.setHideActionText(true);
/* 1887:1882 */     this.selectAllButton.setHorizontalTextPosition(0);
/* 1888:1883 */     this.selectAllButton.setMaximumSize(new Dimension(27, 27));
/* 1889:1884 */     this.selectAllButton.setMinimumSize(new Dimension(27, 27));
/* 1890:1885 */     this.selectAllButton.setName("selectAllButton");
/* 1891:1886 */     this.selectAllButton.setPreferredSize(new Dimension(27, 27));
/* 1892:1887 */     this.selectAllButton.setVerticalTextPosition(3);
/* 1893:1888 */     this.topToolBar.add(this.selectAllButton);
/* 1894:     */     
/* 1895:1890 */     this.deleteButton.setAction(actionMap.get("delete"));
/* 1896:1891 */     this.deleteButton.setHideActionText(true);
/* 1897:1892 */     this.deleteButton.setHorizontalTextPosition(0);
/* 1898:1893 */     this.deleteButton.setMaximumSize(new Dimension(27, 27));
/* 1899:1894 */     this.deleteButton.setMinimumSize(new Dimension(27, 27));
/* 1900:1895 */     this.deleteButton.setName("deleteButton");
/* 1901:1896 */     this.deleteButton.setPreferredSize(new Dimension(27, 27));
/* 1902:1897 */     this.deleteButton.setVerticalTextPosition(3);
/* 1903:1898 */     this.topToolBar.add(this.deleteButton);
/* 1904:     */     
/* 1905:1900 */     this.separator1.setMaximumSize(new Dimension(12, 32767));
/* 1906:1901 */     this.separator1.setName("separator1");
/* 1907:1902 */     this.topToolBar.add(this.separator1);
/* 1908:     */     
/* 1909:1904 */     this.undoButton.setAction(actionMap.get("undo"));
/* 1910:1905 */     this.undoButton.setHideActionText(true);
/* 1911:1906 */     this.undoButton.setHorizontalTextPosition(0);
/* 1912:1907 */     this.undoButton.setMaximumSize(new Dimension(27, 27));
/* 1913:1908 */     this.undoButton.setMinimumSize(new Dimension(27, 27));
/* 1914:1909 */     this.undoButton.setName("undoButton");
/* 1915:1910 */     this.undoButton.setPreferredSize(new Dimension(27, 27));
/* 1916:1911 */     this.undoButton.setVerticalTextPosition(3);
/* 1917:1912 */     this.topToolBar.add(this.undoButton);
/* 1918:     */     
/* 1919:1914 */     this.undoDropButton.setIcon(resourceMap.getIcon("undoDropButton.icon"));
/* 1920:1915 */     this.undoDropButton.setText(resourceMap.getString("undoDropButton.text", new Object[0]));
/* 1921:1916 */     this.undoDropButton.setToolTipText(resourceMap.getString("undoDropButton.toolTipText", new Object[0]));
/* 1922:1917 */     this.undoDropButton.setFocusable(false);
/* 1923:1918 */     this.undoDropButton.setHorizontalTextPosition(0);
/* 1924:1919 */     this.undoDropButton.setName("undoDropButton");
/* 1925:1920 */     this.topToolBar.add(this.undoDropButton);
/* 1926:     */     
/* 1927:1922 */     this.spacer6.setHorizontalAlignment(2);
/* 1928:1923 */     this.spacer6.setEnabled(false);
/* 1929:1924 */     this.spacer6.setFocusable(false);
/* 1930:1925 */     this.spacer6.setHorizontalTextPosition(2);
/* 1931:1926 */     this.spacer6.setInheritsPopupMenu(false);
/* 1932:1927 */     this.spacer6.setMaximumSize(new Dimension(8, 16));
/* 1933:1928 */     this.spacer6.setMinimumSize(new Dimension(8, 16));
/* 1934:1929 */     this.spacer6.setName("spacer6");
/* 1935:1930 */     this.spacer6.setOpaque(true);
/* 1936:1931 */     this.spacer6.setPreferredSize(new Dimension(8, 16));
/* 1937:1932 */     this.spacer6.setRequestFocusEnabled(false);
/* 1938:1933 */     this.spacer6.setVerifyInputWhenFocusTarget(false);
/* 1939:1934 */     this.topToolBar.add(this.spacer6);
/* 1940:     */     
/* 1941:1936 */     this.redoButton.setAction(actionMap.get("redo"));
/* 1942:1937 */     this.redoButton.setHideActionText(true);
/* 1943:1938 */     this.redoButton.setHorizontalTextPosition(0);
/* 1944:1939 */     this.redoButton.setMaximumSize(new Dimension(27, 27));
/* 1945:1940 */     this.redoButton.setMinimumSize(new Dimension(27, 27));
/* 1946:1941 */     this.redoButton.setName("redoButton");
/* 1947:1942 */     this.redoButton.setPreferredSize(new Dimension(27, 27));
/* 1948:1943 */     this.redoButton.setVerticalTextPosition(3);
/* 1949:1944 */     this.topToolBar.add(this.redoButton);
/* 1950:     */     
/* 1951:1946 */     this.redoDropButton.setIcon(resourceMap.getIcon("redoDropButton.icon"));
/* 1952:1947 */     this.redoDropButton.setToolTipText(resourceMap.getString("redoDropButton.toolTipText", new Object[0]));
/* 1953:1948 */     this.redoDropButton.setFocusable(false);
/* 1954:1949 */     this.redoDropButton.setHorizontalTextPosition(0);
/* 1955:1950 */     this.redoDropButton.setName("redoDropButton");
/* 1956:1951 */     this.topToolBar.add(this.redoDropButton);
/* 1957:     */     
/* 1958:1953 */     this.separator2.setMaximumSize(new Dimension(12, 32767));
/* 1959:1954 */     this.separator2.setName("separator2");
/* 1960:1955 */     this.topToolBar.add(this.separator2);
/* 1961:     */     
/* 1962:1957 */     this.iterationLabel.setText(resourceMap.getString("iterationLabel.text", new Object[0]));
/* 1963:1958 */     this.iterationLabel.setName("iterationLabel");
/* 1964:1959 */     this.topToolBar.add(this.iterationLabel);
/* 1965:     */     
/* 1966:1961 */     this.iterationNumberLabel.setHorizontalAlignment(2);
/* 1967:1962 */     this.iterationNumberLabel.setText(resourceMap.getString("iterationNumberLabel.text", new Object[0]));
/* 1968:1963 */     this.iterationNumberLabel.setMaximumSize(new Dimension(32, 16));
/* 1969:1964 */     this.iterationNumberLabel.setMinimumSize(new Dimension(32, 16));
/* 1970:1965 */     this.iterationNumberLabel.setName("iterationNumberLabel");
/* 1971:1966 */     this.iterationNumberLabel.setPreferredSize(new Dimension(32, 16));
/* 1972:1967 */     this.topToolBar.add(this.iterationNumberLabel);
/* 1973:     */     
/* 1974:1969 */     this.back1iterationButton.setAction(actionMap.get("back1iteration"));
/* 1975:1970 */     this.back1iterationButton.setHideActionText(true);
/* 1976:1971 */     this.back1iterationButton.setHorizontalTextPosition(0);
/* 1977:1972 */     this.back1iterationButton.setMaximumSize(new Dimension(27, 27));
/* 1978:1973 */     this.back1iterationButton.setMinimumSize(new Dimension(27, 27));
/* 1979:1974 */     this.back1iterationButton.setName("back1iterationButton");
/* 1980:1975 */     this.back1iterationButton.setPreferredSize(new Dimension(27, 27));
/* 1981:1976 */     this.back1iterationButton.setVerticalTextPosition(3);
/* 1982:1977 */     this.topToolBar.add(this.back1iterationButton);
/* 1983:     */     
/* 1984:1979 */     this.forward1iterationButton.setAction(actionMap.get("forward1iteration"));
/* 1985:1980 */     this.forward1iterationButton.setHideActionText(true);
/* 1986:1981 */     this.forward1iterationButton.setHorizontalTextPosition(0);
/* 1987:1982 */     this.forward1iterationButton.setMaximumSize(new Dimension(27, 27));
/* 1988:1983 */     this.forward1iterationButton.setMinimumSize(new Dimension(27, 27));
/* 1989:1984 */     this.forward1iterationButton.setName("forward1iterationButton");
/* 1990:1985 */     this.forward1iterationButton.setPreferredSize(new Dimension(27, 27));
/* 1991:1986 */     this.forward1iterationButton.setVerticalTextPosition(3);
/* 1992:1987 */     this.topToolBar.add(this.forward1iterationButton);
/* 1993:     */     
/* 1994:1989 */     this.showGoToIterationButton.setAction(actionMap.get("gotoIteration"));
/* 1995:1990 */     this.showGoToIterationButton.setHideActionText(true);
/* 1996:1991 */     this.showGoToIterationButton.setHorizontalTextPosition(0);
/* 1997:1992 */     this.showGoToIterationButton.setMaximumSize(new Dimension(27, 27));
/* 1998:1993 */     this.showGoToIterationButton.setMinimumSize(new Dimension(27, 27));
/* 1999:1994 */     this.showGoToIterationButton.setName("showGoToIterationButton");
/* 2000:1995 */     this.showGoToIterationButton.setPreferredSize(new Dimension(27, 27));
/* 2001:1996 */     this.showGoToIterationButton.setVerticalTextPosition(3);
/* 2002:1997 */     this.topToolBar.add(this.showGoToIterationButton);
/* 2003:     */     
/* 2004:1999 */     this.separator8.setMaximumSize(new Dimension(12, 32767));
/* 2005:2000 */     this.separator8.setName("separator8");
/* 2006:2001 */     this.topToolBar.add(this.separator8);
/* 2007:     */     
/* 2008:2003 */     this.costLabel.setHorizontalAlignment(11);
/* 2009:2004 */     this.costLabel.setText(resourceMap.getString("costLabel.text", new Object[0]));
/* 2010:2005 */     this.costLabel.setToolTipText(resourceMap.getString("costLabel.toolTipText", new Object[0]));
/* 2011:2006 */     this.costLabel.setMaximumSize(new Dimension(100, 16));
/* 2012:2007 */     this.costLabel.setMinimumSize(new Dimension(100, 16));
/* 2013:2008 */     this.costLabel.setName("costLabel");
/* 2014:2009 */     this.costLabel.setPreferredSize(new Dimension(100, 16));
/* 2015:2010 */     this.topToolBar.add(this.costLabel);
/* 2016:     */     
/* 2017:2012 */     this.spacer7.setHorizontalAlignment(2);
/* 2018:2013 */     this.spacer7.setEnabled(false);
/* 2019:2014 */     this.spacer7.setFocusable(false);
/* 2020:2015 */     this.spacer7.setHorizontalTextPosition(2);
/* 2021:2016 */     this.spacer7.setInheritsPopupMenu(false);
/* 2022:2017 */     this.spacer7.setMaximumSize(new Dimension(4, 16));
/* 2023:2018 */     this.spacer7.setMinimumSize(new Dimension(4, 16));
/* 2024:2019 */     this.spacer7.setName("spacer7");
/* 2025:2020 */     this.spacer7.setOpaque(true);
/* 2026:2021 */     this.spacer7.setPreferredSize(new Dimension(4, 16));
/* 2027:2022 */     this.spacer7.setRequestFocusEnabled(false);
/* 2028:2023 */     this.spacer7.setVerifyInputWhenFocusTarget(false);
/* 2029:2024 */     this.topToolBar.add(this.spacer7);
/* 2030:     */     
/* 2031:2026 */     this.costReportButton.setAction(actionMap.get("showCostDialog"));
/* 2032:2027 */     this.costReportButton.setHideActionText(true);
/* 2033:2028 */     this.costReportButton.setHorizontalTextPosition(0);
/* 2034:2029 */     this.costReportButton.setMaximumSize(new Dimension(27, 27));
/* 2035:2030 */     this.costReportButton.setMinimumSize(new Dimension(27, 27));
/* 2036:2031 */     this.costReportButton.setName("costReportButton");
/* 2037:2032 */     this.costReportButton.setPreferredSize(new Dimension(27, 27));
/* 2038:2033 */     this.costReportButton.setVerticalTextPosition(3);
/* 2039:2034 */     this.topToolBar.add(this.costReportButton);
/* 2040:     */     
/* 2041:2036 */     this.separator4.setMaximumSize(new Dimension(12, 32767));
/* 2042:2037 */     this.separator4.setName("separator4");
/* 2043:2038 */     this.topToolBar.add(this.separator4);
/* 2044:     */     
/* 2045:2040 */     this.statusLabel.setIcon(resourceMap.getIcon("statusLabel.icon"));
/* 2046:2041 */     this.statusLabel.setText(resourceMap.getString("statusLabel.text", new Object[0]));
/* 2047:2042 */     this.statusLabel.setHorizontalTextPosition(10);
/* 2048:2043 */     this.statusLabel.setName("statusLabel");
/* 2049:2044 */     this.topToolBar.add(this.statusLabel);
/* 2050:     */     
/* 2051:2046 */     this.separator5.setMaximumSize(new Dimension(12, 32767));
/* 2052:2047 */     this.separator5.setName("separator5");
/* 2053:2048 */     this.topToolBar.add(this.separator5);
/* 2054:     */     
/* 2055:2050 */     this.loadTestReportButton.setAction(actionMap.get("showLoadTestReport"));
/* 2056:2051 */     this.loadTestReportButton.setFocusable(false);
/* 2057:2052 */     this.loadTestReportButton.setHideActionText(true);
/* 2058:2053 */     this.loadTestReportButton.setHorizontalTextPosition(0);
/* 2059:2054 */     this.loadTestReportButton.setMaximumSize(new Dimension(27, 27));
/* 2060:2055 */     this.loadTestReportButton.setMinimumSize(new Dimension(27, 27));
/* 2061:2056 */     this.loadTestReportButton.setName("loadTestReportButton");
/* 2062:2057 */     this.loadTestReportButton.setPreferredSize(new Dimension(27, 27));
/* 2063:2058 */     this.loadTestReportButton.setVerticalTextPosition(3);
/* 2064:2059 */     this.topToolBar.add(this.loadTestReportButton);
/* 2065:     */     
/* 2066:2061 */     this.bottomToolBar.setFloatable(false);
/* 2067:2062 */     this.bottomToolBar.setRollover(true);
/* 2068:2063 */     this.bottomToolBar.setMaximumSize(new Dimension(559, 32));
/* 2069:2064 */     this.bottomToolBar.setMinimumSize(new Dimension(272, 32));
/* 2070:2065 */     this.bottomToolBar.setName("bottomToolBar");
/* 2071:     */     
/* 2072:2067 */     this.spacer0.setHorizontalAlignment(2);
/* 2073:2068 */     this.spacer0.setText(resourceMap.getString("spacer0.text", new Object[0]));
/* 2074:2069 */     this.spacer0.setEnabled(false);
/* 2075:2070 */     this.spacer0.setFocusable(false);
/* 2076:2071 */     this.spacer0.setHorizontalTextPosition(2);
/* 2077:2072 */     this.spacer0.setInheritsPopupMenu(false);
/* 2078:2073 */     this.spacer0.setMaximumSize(new Dimension(8, 16));
/* 2079:2074 */     this.spacer0.setMinimumSize(new Dimension(8, 16));
/* 2080:2075 */     this.spacer0.setName("spacer0");
/* 2081:2076 */     this.spacer0.setOpaque(true);
/* 2082:2077 */     this.spacer0.setPreferredSize(new Dimension(8, 16));
/* 2083:2078 */     this.spacer0.setRequestFocusEnabled(false);
/* 2084:2079 */     this.spacer0.setVerifyInputWhenFocusTarget(false);
/* 2085:2080 */     this.bottomToolBar.add(this.spacer0);
/* 2086:     */     
/* 2087:2082 */     this.materialBox.setModel(this.bridge.getInventory().getMaterialBoxModel());
/* 2088:2083 */     this.materialBox.setMaximumSize(new Dimension(250, 24));
/* 2089:2084 */     this.materialBox.setName("materialBox");
/* 2090:2085 */     this.bottomToolBar.add(this.materialBox);
/* 2091:     */     
/* 2092:2087 */     this.spacer1.setHorizontalAlignment(2);
/* 2093:2088 */     this.spacer1.setText(resourceMap.getString("spacer1.text", new Object[0]));
/* 2094:2089 */     this.spacer1.setEnabled(false);
/* 2095:2090 */     this.spacer1.setFocusable(false);
/* 2096:2091 */     this.spacer1.setHorizontalTextPosition(2);
/* 2097:2092 */     this.spacer1.setInheritsPopupMenu(false);
/* 2098:2093 */     this.spacer1.setMaximumSize(new Dimension(8, 16));
/* 2099:2094 */     this.spacer1.setMinimumSize(new Dimension(8, 16));
/* 2100:2095 */     this.spacer1.setName("spacer1");
/* 2101:2096 */     this.spacer1.setOpaque(true);
/* 2102:2097 */     this.spacer1.setPreferredSize(new Dimension(8, 16));
/* 2103:2098 */     this.spacer1.setRequestFocusEnabled(false);
/* 2104:2099 */     this.spacer1.setVerifyInputWhenFocusTarget(false);
/* 2105:2100 */     this.bottomToolBar.add(this.spacer1);
/* 2106:     */     
/* 2107:2102 */     this.sectionBox.setModel(this.bridge.getInventory().getSectionBoxModel());
/* 2108:2103 */     this.sectionBox.setMaximumSize(new Dimension(180, 24));
/* 2109:2104 */     this.sectionBox.setName("sectionBox");
/* 2110:2105 */     this.bottomToolBar.add(this.sectionBox);
/* 2111:     */     
/* 2112:2107 */     this.spacer2.setHorizontalAlignment(2);
/* 2113:2108 */     this.spacer2.setText(resourceMap.getString("spacer2.text", new Object[0]));
/* 2114:2109 */     this.spacer2.setEnabled(false);
/* 2115:2110 */     this.spacer2.setFocusable(false);
/* 2116:2111 */     this.spacer2.setHorizontalTextPosition(2);
/* 2117:2112 */     this.spacer2.setInheritsPopupMenu(false);
/* 2118:2113 */     this.spacer2.setMaximumSize(new Dimension(8, 16));
/* 2119:2114 */     this.spacer2.setMinimumSize(new Dimension(8, 16));
/* 2120:2115 */     this.spacer2.setName("spacer2");
/* 2121:2116 */     this.spacer2.setOpaque(true);
/* 2122:2117 */     this.spacer2.setPreferredSize(new Dimension(8, 16));
/* 2123:2118 */     this.spacer2.setRequestFocusEnabled(false);
/* 2124:2119 */     this.spacer2.setVerifyInputWhenFocusTarget(false);
/* 2125:2120 */     this.bottomToolBar.add(this.spacer2);
/* 2126:     */     
/* 2127:2122 */     this.sizeBox.setModel(this.bridge.getInventory().getSizeBoxModel());
/* 2128:2123 */     this.sizeBox.setMaximumSize(new Dimension(150, 24));
/* 2129:2124 */     this.sizeBox.setName("sizeBox");
/* 2130:2125 */     this.bottomToolBar.add(this.sizeBox);
/* 2131:     */     
/* 2132:2127 */     this.spacer4.setHorizontalAlignment(2);
/* 2133:2128 */     this.spacer4.setEnabled(false);
/* 2134:2129 */     this.spacer4.setFocusable(false);
/* 2135:2130 */     this.spacer4.setHorizontalTextPosition(2);
/* 2136:2131 */     this.spacer4.setInheritsPopupMenu(false);
/* 2137:2132 */     this.spacer4.setMaximumSize(new Dimension(8, 16));
/* 2138:2133 */     this.spacer4.setMinimumSize(new Dimension(8, 16));
/* 2139:2134 */     this.spacer4.setName("spacer4");
/* 2140:2135 */     this.spacer4.setOpaque(true);
/* 2141:2136 */     this.spacer4.setPreferredSize(new Dimension(8, 16));
/* 2142:2137 */     this.spacer4.setRequestFocusEnabled(false);
/* 2143:2138 */     this.spacer4.setVerifyInputWhenFocusTarget(false);
/* 2144:2139 */     this.bottomToolBar.add(this.spacer4);
/* 2145:     */     
/* 2146:2141 */     this.increaseMemberSizeButton.setAction(actionMap.get("increaseMemberSize"));
/* 2147:2142 */     this.increaseMemberSizeButton.setHideActionText(true);
/* 2148:2143 */     this.increaseMemberSizeButton.setHorizontalTextPosition(0);
/* 2149:2144 */     this.increaseMemberSizeButton.setMaximumSize(new Dimension(29, 29));
/* 2150:2145 */     this.increaseMemberSizeButton.setMinimumSize(new Dimension(29, 29));
/* 2151:2146 */     this.increaseMemberSizeButton.setName("increaseMemberSizeButton");
/* 2152:2147 */     this.increaseMemberSizeButton.setPreferredSize(new Dimension(29, 29));
/* 2153:2148 */     this.increaseMemberSizeButton.setVerticalTextPosition(3);
/* 2154:2149 */     this.bottomToolBar.add(this.increaseMemberSizeButton);
/* 2155:     */     
/* 2156:2151 */     this.decreaseMemberSizeButton.setAction(actionMap.get("decreaseMemberSize"));
/* 2157:2152 */     this.decreaseMemberSizeButton.setHideActionText(true);
/* 2158:2153 */     this.decreaseMemberSizeButton.setMaximumSize(new Dimension(29, 29));
/* 2159:2154 */     this.decreaseMemberSizeButton.setMinimumSize(new Dimension(29, 29));
/* 2160:2155 */     this.decreaseMemberSizeButton.setName("decreaseMemberSizeButton");
/* 2161:2156 */     this.decreaseMemberSizeButton.setPreferredSize(new Dimension(29, 29));
/* 2162:2157 */     this.bottomToolBar.add(this.decreaseMemberSizeButton);
/* 2163:     */     
/* 2164:2159 */     this.spacer5.setHorizontalAlignment(2);
/* 2165:2160 */     this.spacer5.setEnabled(false);
/* 2166:2161 */     this.spacer5.setFocusable(false);
/* 2167:2162 */     this.spacer5.setHorizontalTextPosition(2);
/* 2168:2163 */     this.spacer5.setInheritsPopupMenu(false);
/* 2169:2164 */     this.spacer5.setMaximumSize(new Dimension(8, 16));
/* 2170:2165 */     this.spacer5.setMinimumSize(new Dimension(8, 16));
/* 2171:2166 */     this.spacer5.setName("spacer5");
/* 2172:2167 */     this.spacer5.setOpaque(true);
/* 2173:2168 */     this.spacer5.setPreferredSize(new Dimension(8, 16));
/* 2174:2169 */     this.spacer5.setRequestFocusEnabled(false);
/* 2175:2170 */     this.spacer5.setVerifyInputWhenFocusTarget(false);
/* 2176:2171 */     this.bottomToolBar.add(this.spacer5);
/* 2177:     */     
/* 2178:2173 */     this.toggleMemberListButton.setAction(actionMap.get("toggleMemberList"));
/* 2179:2174 */     this.toggleMemberListButton.setSelected(true);
/* 2180:2175 */     this.toggleMemberListButton.setHideActionText(true);
/* 2181:2176 */     this.toggleMemberListButton.setHorizontalTextPosition(0);
/* 2182:2177 */     this.toggleMemberListButton.setMaximumSize(new Dimension(29, 29));
/* 2183:2178 */     this.toggleMemberListButton.setMinimumSize(new Dimension(29, 29));
/* 2184:2179 */     this.toggleMemberListButton.setName("toggleMemberListButton");
/* 2185:2180 */     this.toggleMemberListButton.setPreferredSize(new Dimension(29, 29));
/* 2186:2181 */     this.toggleMemberListButton.setVerticalTextPosition(3);
/* 2187:2182 */     this.bottomToolBar.add(this.toggleMemberListButton);
/* 2188:     */     
/* 2189:2184 */     this.toggleMemberNumbersButton.setAction(actionMap.get("toggleMemberNumbers"));
/* 2190:2185 */     this.toggleMemberNumbersButton.setHideActionText(true);
/* 2191:2186 */     this.toggleMemberNumbersButton.setHorizontalTextPosition(0);
/* 2192:2187 */     this.toggleMemberNumbersButton.setMaximumSize(new Dimension(29, 29));
/* 2193:2188 */     this.toggleMemberNumbersButton.setMinimumSize(new Dimension(29, 29));
/* 2194:2189 */     this.toggleMemberNumbersButton.setName("toggleMemberNumbersButton");
/* 2195:2190 */     this.toggleMemberNumbersButton.setPreferredSize(new Dimension(29, 29));
/* 2196:2191 */     this.toggleMemberNumbersButton.setVerticalTextPosition(3);
/* 2197:2192 */     this.bottomToolBar.add(this.toggleMemberNumbersButton);
/* 2198:     */     
/* 2199:2194 */     this.toggleGuidesButton.setAction(actionMap.get("toggleGuides"));
/* 2200:2195 */     this.toggleGuidesButton.setHideActionText(true);
/* 2201:2196 */     this.toggleGuidesButton.setHorizontalTextPosition(0);
/* 2202:2197 */     this.toggleGuidesButton.setMaximumSize(new Dimension(29, 29));
/* 2203:2198 */     this.toggleGuidesButton.setMinimumSize(new Dimension(29, 29));
/* 2204:2199 */     this.toggleGuidesButton.setName("toggleGuidesButton");
/* 2205:2200 */     this.toggleGuidesButton.setPreferredSize(new Dimension(29, 29));
/* 2206:2201 */     this.toggleGuidesButton.setVerticalTextPosition(3);
/* 2207:2202 */     this.bottomToolBar.add(this.toggleGuidesButton);
/* 2208:     */     
/* 2209:2204 */     this.toggleTemplateButton.setAction(actionMap.get("toggleTemplate"));
/* 2210:2205 */     this.toggleTemplateButton.setFocusable(false);
/* 2211:2206 */     this.toggleTemplateButton.setHideActionText(true);
/* 2212:2207 */     this.toggleTemplateButton.setHorizontalTextPosition(0);
/* 2213:2208 */     this.toggleTemplateButton.setMaximumSize(new Dimension(29, 29));
/* 2214:2209 */     this.toggleTemplateButton.setMinimumSize(new Dimension(29, 29));
/* 2215:2210 */     this.toggleTemplateButton.setName("toggleTemplateButton");
/* 2216:2211 */     this.toggleTemplateButton.setPreferredSize(new Dimension(29, 29));
/* 2217:2212 */     this.toggleTemplateButton.setVerticalTextPosition(3);
/* 2218:2213 */     this.bottomToolBar.add(this.toggleTemplateButton);
/* 2219:     */     
/* 2220:2215 */     this.separator7.setMaximumSize(new Dimension(12, 32767));
/* 2221:2216 */     this.separator7.setName("separator7");
/* 2222:2217 */     this.bottomToolBar.add(this.separator7);
/* 2223:     */     
/* 2224:2219 */     this.setCoarseGridButton.setAction(actionMap.get("setCoarseGrid"));
/* 2225:2220 */     this.gridSizeButtonGroup.add(this.setCoarseGridButton);
/* 2226:2221 */     this.setCoarseGridButton.setSelected(true);
/* 2227:2222 */     this.setCoarseGridButton.setHideActionText(true);
/* 2228:2223 */     this.setCoarseGridButton.setHorizontalTextPosition(0);
/* 2229:2224 */     this.setCoarseGridButton.setMaximumSize(new Dimension(29, 29));
/* 2230:2225 */     this.setCoarseGridButton.setMinimumSize(new Dimension(29, 29));
/* 2231:2226 */     this.setCoarseGridButton.setName("setCoarseGridButton");
/* 2232:2227 */     this.setCoarseGridButton.setPreferredSize(new Dimension(29, 29));
/* 2233:2228 */     this.setCoarseGridButton.setVerticalTextPosition(3);
/* 2234:2229 */     this.bottomToolBar.add(this.setCoarseGridButton);
/* 2235:     */     
/* 2236:2231 */     this.setMediumGridButton.setAction(actionMap.get("setMediumGrid"));
/* 2237:2232 */     this.gridSizeButtonGroup.add(this.setMediumGridButton);
/* 2238:2233 */     this.setMediumGridButton.setHideActionText(true);
/* 2239:2234 */     this.setMediumGridButton.setHorizontalTextPosition(0);
/* 2240:2235 */     this.setMediumGridButton.setMaximumSize(new Dimension(29, 29));
/* 2241:2236 */     this.setMediumGridButton.setMinimumSize(new Dimension(29, 29));
/* 2242:2237 */     this.setMediumGridButton.setName("setMediumGridButton");
/* 2243:2238 */     this.setMediumGridButton.setPreferredSize(new Dimension(29, 29));
/* 2244:2239 */     this.setMediumGridButton.setVerticalTextPosition(3);
/* 2245:2240 */     this.bottomToolBar.add(this.setMediumGridButton);
/* 2246:     */     
/* 2247:2242 */     this.setFineGridButton.setAction(actionMap.get("setFineGrid"));
/* 2248:2243 */     this.gridSizeButtonGroup.add(this.setFineGridButton);
/* 2249:2244 */     this.setFineGridButton.setHideActionText(true);
/* 2250:2245 */     this.setFineGridButton.setHorizontalTextPosition(0);
/* 2251:2246 */     this.setFineGridButton.setMaximumSize(new Dimension(29, 29));
/* 2252:2247 */     this.setFineGridButton.setMinimumSize(new Dimension(29, 29));
/* 2253:2248 */     this.setFineGridButton.setName("setFineGridButton");
/* 2254:2249 */     this.setFineGridButton.setPreferredSize(new Dimension(29, 29));
/* 2255:2250 */     this.setFineGridButton.setVerticalTextPosition(3);
/* 2256:2251 */     this.bottomToolBar.add(this.setFineGridButton);
/* 2257:     */     
/* 2258:2253 */     this.cardPanel.setName("cardPanel");
/* 2259:2254 */     this.cardPanel.setPreferredSize(new Dimension(640, 480));
/* 2260:2255 */     this.cardPanel.setLayout(new CardLayout());
/* 2261:     */     
/* 2262:2257 */     this.nullPanel.setBackground(resourceMap.getColor("nullPanel.background"));
/* 2263:2258 */     this.nullPanel.setBorder(BorderFactory.createBevelBorder(1));
/* 2264:2259 */     this.nullPanel.setName("nullPanel");
/* 2265:     */     
/* 2266:2261 */     GroupLayout nullPanelLayout = new GroupLayout(this.nullPanel);
/* 2267:2262 */     this.nullPanel.setLayout(nullPanelLayout);
/* 2268:2263 */     nullPanelLayout.setHorizontalGroup(nullPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 1150, 32767));
/* 2269:     */     
/* 2270:     */ 
/* 2271:     */ 
/* 2272:2267 */     nullPanelLayout.setVerticalGroup(nullPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 599, 32767));
/* 2273:     */     
/* 2274:     */ 
/* 2275:     */ 
/* 2276:     */ 
/* 2277:2272 */     this.cardPanel.add(this.nullPanel, "nullPanel");
/* 2278:     */     
/* 2279:2274 */     this.designPanel.setName("designPanel");
/* 2280:     */     
/* 2281:2276 */     this.drawingPanel.setName("drawingPanel");
/* 2282:2277 */     this.drawingPanel.setLayout(new GridBagLayout());
/* 2283:     */     
/* 2284:2279 */     this.draftingPanel = ((DraftingPanel)this.draftingJPanel);
/* 2285:2280 */     this.draftingJPanel.setBackground(resourceMap.getColor("draftingJPanel.background"));
/* 2286:2281 */     this.draftingJPanel.setName("draftingJPanel");
/* 2287:     */     
/* 2288:2283 */     this.openMemberTableButton.setAction(actionMap.get("openMemberTable"));
/* 2289:2284 */     this.openMemberTableButton.setFocusable(false);
/* 2290:2285 */     this.openMemberTableButton.setHideActionText(true);
/* 2291:2286 */     this.openMemberTableButton.setMargin(new Insets(0, 0, 0, 0));
/* 2292:2287 */     this.openMemberTableButton.setName("openMemberTableButton");
/* 2293:2288 */     this.openMemberTableButton.setCursor(Cursor.getPredefinedCursor(0));
/* 2294:2289 */     this.openMemberTableButton.setVisible(false);
/* 2295:     */     
/* 2296:2291 */     this.titleBlockPanel.setBackground(resourceMap.getColor("titleBlockPanel.background"));
/* 2297:2292 */     this.titleBlockPanel.setName("titleBlockPanel");
/* 2298:2293 */     this.titleBlockPanel.setPreferredSize(new Dimension(260, 71));
/* 2299:2294 */     this.titleBlockPanel.setLayout(new AbsoluteLayout());
/* 2300:     */     
/* 2301:2296 */     this.titleLabel.setBackground(resourceMap.getColor("titleLabel.background"));
/* 2302:2297 */     this.titleLabel.setFont(resourceMap.getFont("titleLabel.font"));
/* 2303:2298 */     this.titleLabel.setForeground(resourceMap.getColor("titleLabel.foreground"));
/* 2304:2299 */     this.titleLabel.setHorizontalAlignment(0);
/* 2305:2300 */     this.titleLabel.setText(resourceMap.getString("titleLabel.text", new Object[0]));
/* 2306:2301 */     this.titleLabel.setName("titleLabel");
/* 2307:2302 */     this.titleLabel.setPreferredSize(new Dimension(258, 17));
/* 2308:2303 */     this.titleBlockPanel.add(this.titleLabel, new AbsoluteConstraints(6, 6, 248, -1));
/* 2309:     */     
/* 2310:2305 */     this.designedByLabel.setFont(resourceMap.getFont("designedByLabel.font"));
/* 2311:2306 */     this.designedByLabel.setText(resourceMap.getString("designedByLabel.text", new Object[0]));
/* 2312:2307 */     this.designedByLabel.setName("designedByLabel");
/* 2313:2308 */     this.titleBlockPanel.add(this.designedByLabel, new AbsoluteConstraints(6, 28, -1, -1));
/* 2314:     */     
/* 2315:2310 */     this.designedByField.setForeground(resourceMap.getColor("designedByField.foreground"));
/* 2316:2311 */     this.designedByField.setText(resourceMap.getString("designedByField.text", new Object[0]));
/* 2317:2312 */     this.designedByField.setBorder(null);
/* 2318:2313 */     this.designedByField.setName("designedByField");
/* 2319:2314 */     this.titleBlockPanel.add(this.designedByField, new AbsoluteConstraints(70, 28, 180, -1));
/* 2320:     */     
/* 2321:2316 */     this.projectIDLabel.setFont(resourceMap.getFont("projectIDLabel.font"));
/* 2322:2317 */     this.projectIDLabel.setText(resourceMap.getString("projectIDLabel.text", new Object[0]));
/* 2323:2318 */     this.projectIDLabel.setName("projectIDLabel");
/* 2324:2319 */     this.titleBlockPanel.add(this.projectIDLabel, new AbsoluteConstraints(6, 49, -1, -1));
/* 2325:     */     
/* 2326:2321 */     this.scenarioIDLabel.setForeground(resourceMap.getColor("scenarioIDLabel.foreground"));
/* 2327:2322 */     this.scenarioIDLabel.setText(resourceMap.getString("scenarioIDLabel.text", new Object[0]));
/* 2328:2323 */     this.scenarioIDLabel.setName("scenarioIDLabel");
/* 2329:2324 */     this.titleBlockPanel.add(this.scenarioIDLabel, new AbsoluteConstraints(60, 49, -1, -1));
/* 2330:     */     
/* 2331:2326 */     this.projectIDField.setForeground(resourceMap.getColor("projectIDField.foreground"));
/* 2332:2327 */     this.projectIDField.setText(resourceMap.getString("projectIDField.text", new Object[0]));
/* 2333:2328 */     this.projectIDField.setBorder(null);
/* 2334:2329 */     this.projectIDField.setName("projectIDField");
/* 2335:2330 */     this.titleBlockPanel.add(this.projectIDField, new AbsoluteConstraints(110, 49, 140, -1));
/* 2336:     */     
/* 2337:2332 */     GroupLayout draftingJPanelLayout = new GroupLayout(this.draftingJPanel);
/* 2338:2333 */     this.draftingJPanel.setLayout(draftingJPanelLayout);
/* 2339:2334 */     draftingJPanelLayout.setHorizontalGroup(draftingJPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(draftingJPanelLayout.createSequentialGroup().addContainerGap(389, 32767).addGroup(draftingJPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.openMemberTableButton, GroupLayout.Alignment.TRAILING).addComponent(this.titleBlockPanel, GroupLayout.Alignment.TRAILING, -2, 260, -2))));
/* 2340:     */     
/* 2341:     */ 
/* 2342:     */ 
/* 2343:     */ 
/* 2344:     */ 
/* 2345:     */ 
/* 2346:     */ 
/* 2347:2342 */     draftingJPanelLayout.setVerticalGroup(draftingJPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(draftingJPanelLayout.createSequentialGroup().addComponent(this.openMemberTableButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 485, 32767).addComponent(this.titleBlockPanel, -2, 71, -2)));
/* 2348:     */     
/* 2349:     */ 
/* 2350:     */ 
/* 2351:     */ 
/* 2352:     */ 
/* 2353:     */ 
/* 2354:     */ 
/* 2355:2350 */     GridBagConstraints gridBagConstraints = new GridBagConstraints();
/* 2356:2351 */     gridBagConstraints.gridx = 1;
/* 2357:2352 */     gridBagConstraints.gridy = 0;
/* 2358:2353 */     gridBagConstraints.fill = 1;
/* 2359:2354 */     gridBagConstraints.weightx = 1.0D;
/* 2360:2355 */     gridBagConstraints.weighty = 1.0D;
/* 2361:2356 */     this.drawingPanel.add(this.draftingJPanel, gridBagConstraints);
/* 2362:     */     
/* 2363:2358 */     this.verticalRuler.setFocusable(false);
/* 2364:2359 */     this.verticalRuler.setMinimumSize(new Dimension(32, 1));
/* 2365:2360 */     this.verticalRuler.setName("verticalRuler");
/* 2366:2361 */     this.verticalRuler.setPreferredSize(new Dimension(32, 1));
/* 2367:2362 */     this.verticalRuler.setRequestFocusEnabled(false);
/* 2368:2363 */     gridBagConstraints = new GridBagConstraints();
/* 2369:2364 */     gridBagConstraints.gridx = 0;
/* 2370:2365 */     gridBagConstraints.gridy = 0;
/* 2371:2366 */     gridBagConstraints.fill = 3;
/* 2372:2367 */     this.drawingPanel.add(this.verticalRuler, gridBagConstraints);
/* 2373:     */     
/* 2374:2369 */     this.corner.setFocusable(false);
/* 2375:2370 */     this.corner.setMinimumSize(new Dimension(32, 32));
/* 2376:2371 */     this.corner.setName("corner");
/* 2377:2372 */     this.corner.setPreferredSize(new Dimension(32, 32));
/* 2378:2373 */     gridBagConstraints = new GridBagConstraints();
/* 2379:2374 */     gridBagConstraints.gridx = 0;
/* 2380:2375 */     gridBagConstraints.gridy = 1;
/* 2381:2376 */     this.drawingPanel.add(this.corner, gridBagConstraints);
/* 2382:     */     
/* 2383:2378 */     this.horizontalRuler.setFocusable(false);
/* 2384:2379 */     this.horizontalRuler.setMinimumSize(new Dimension(1, 32));
/* 2385:2380 */     this.horizontalRuler.setName("horizontalRuler");
/* 2386:2381 */     this.horizontalRuler.setPreferredSize(new Dimension(1, 32));
/* 2387:2382 */     gridBagConstraints = new GridBagConstraints();
/* 2388:2383 */     gridBagConstraints.gridx = 1;
/* 2389:2384 */     gridBagConstraints.gridy = 1;
/* 2390:2385 */     gridBagConstraints.fill = 2;
/* 2391:2386 */     this.drawingPanel.add(this.horizontalRuler, gridBagConstraints);
/* 2392:     */     
/* 2393:2388 */     this.memberPanel.setName("memberPanel");
/* 2394:     */     
/* 2395:2390 */     this.memberTabs.setAlignmentX(1.0F);
/* 2396:2391 */     this.memberTabs.setAlignmentY(0.0F);
/* 2397:2392 */     this.memberTabs.setName(null);
/* 2398:     */     
/* 2399:2394 */     this.memberListPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
/* 2400:2395 */     this.memberListPanel.setMaximumSize(new Dimension(540, 32767));
/* 2401:2396 */     this.memberListPanel.setMinimumSize(new Dimension(540, 100));
/* 2402:2397 */     this.memberListPanel.setName("memberListPanel");
/* 2403:2398 */     this.memberListPanel.setPreferredSize(new Dimension(540, 100));
/* 2404:2399 */     this.memberListPanel.setRequestFocusEnabled(false);
/* 2405:     */     
/* 2406:2401 */     this.loadTestResultsLabel.setFont(this.loadTestResultsLabel.getFont().deriveFont(this.loadTestResultsLabel.getFont().getStyle() | 0x1));
/* 2407:2402 */     this.loadTestResultsLabel.setText(resourceMap.getString("loadTestResultsLabel.text", new Object[0]));
/* 2408:2403 */     this.loadTestResultsLabel.setVerticalAlignment(1);
/* 2409:2404 */     this.loadTestResultsLabel.setAlignmentY(0.0F);
/* 2410:2405 */     this.loadTestResultsLabel.setName("loadTestResultsLabel");
/* 2411:     */     
/* 2412:2407 */     this.memberScroll.setName("memberScroll");
/* 2413:2408 */     this.memberScroll.setPreferredSize(new Dimension(454, 32));
/* 2414:     */     
/* 2415:2410 */     this.memberTable = ((MemberTable)this.memberJTable);
/* 2416:2411 */     this.memberJTable.setModel(new MemberTableModel(this.bridge));
/* 2417:2412 */     this.memberJTable.setFillsViewportHeight(true);
/* 2418:2413 */     this.memberJTable.setName("memberJTable");
/* 2419:2414 */     this.memberTable.initialize();
/* 2420:2415 */     this.memberScroll.setViewportView(this.memberJTable);
/* 2421:     */     
/* 2422:2417 */     GroupLayout memberListPanelLayout = new GroupLayout(this.memberListPanel);
/* 2423:2418 */     this.memberListPanel.setLayout(memberListPanelLayout);
/* 2424:2419 */     memberListPanelLayout.setHorizontalGroup(memberListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, memberListPanelLayout.createSequentialGroup().addContainerGap(264, 32767).addComponent(this.loadTestResultsLabel).addGap(80, 80, 80)).addComponent(this.memberScroll, GroupLayout.Alignment.TRAILING, -1, 459, 32767));
/* 2425:     */     
/* 2426:     */ 
/* 2427:     */ 
/* 2428:     */ 
/* 2429:     */ 
/* 2430:     */ 
/* 2431:     */ 
/* 2432:2427 */     memberListPanelLayout.setVerticalGroup(memberListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(memberListPanelLayout.createSequentialGroup().addComponent(this.loadTestResultsLabel).addGap(8, 8, 8).addComponent(this.memberScroll, -1, 547, 32767)));
/* 2433:     */     
/* 2434:     */ 
/* 2435:     */ 
/* 2436:     */ 
/* 2437:     */ 
/* 2438:     */ 
/* 2439:     */ 
/* 2440:2435 */     this.memberTabs.addTab(resourceMap.getString("memberListPanel.TabConstraints.tabTitle", new Object[0]), this.memberListPanel);
/* 2441:     */     
/* 2442:2437 */     this.memberDetailTabs.setTabLayoutPolicy(1);
/* 2443:2438 */     this.memberDetailTabs.setName("memberDetailTabs");
/* 2444:     */     
/* 2445:2440 */     this.memberDetailPanel.setName("memberDetailPanel");
/* 2446:     */     
/* 2447:2442 */     this.materialPropertiesLabel.setText(resourceMap.getString("materialPropertiesLabel.text", new Object[0]));
/* 2448:2443 */     this.materialPropertiesLabel.setName("materialPropertiesLabel");
/* 2449:     */     
/* 2450:2445 */     this.materialPropertiesTable.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
/* 2451:2446 */     this.materialPropertiesTable.setModel(new DefaultTableModel(new Object[][] { { "Material", null }, { "Yield Stress (Fy)", null }, { "Modulus of Elasticity (E)", null }, { "Mass Density", null } }, new String[] { "Title 1", "Title 2" })
/* 2452:     */     {
/* 2453:2457 */       boolean[] canEdit = { false, false };
/* 2454:     */       
/* 2455:     */       public boolean isCellEditable(int rowIndex, int columnIndex)
/* 2456:     */       {
/* 2457:2462 */         return this.canEdit[columnIndex];
/* 2458:     */       }
/* 2459:2464 */     });
/* 2460:2465 */     this.materialPropertiesTable.setFocusable(false);
/* 2461:2466 */     this.materialPropertiesTable.setIntercellSpacing(new Dimension(6, 4));
/* 2462:2467 */     this.materialPropertiesTable.setName("materialPropertiesTable");
/* 2463:2468 */     this.materialPropertiesTable.setRowSelectionAllowed(false);
/* 2464:     */     
/* 2465:2470 */     this.dimensionsLabel.setText(resourceMap.getString("dimensionsLabel.text", new Object[0]));
/* 2466:2471 */     this.dimensionsLabel.setName("dimensionsLabel");
/* 2467:     */     
/* 2468:2473 */     this.dimensionsTable.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
/* 2469:2474 */     this.dimensionsTable.setModel(new DefaultTableModel(new Object[][] { { "Cross-Section Type", null }, { "Cross-Section Size", null }, { "Area", null }, { "Moment of Inertia", null }, { "Member Length", null } }, new String[] { "Title 1", "Title 2" }));
/* 2470:     */     
/* 2471:     */ 
/* 2472:     */ 
/* 2473:     */ 
/* 2474:     */ 
/* 2475:     */ 
/* 2476:     */ 
/* 2477:     */ 
/* 2478:     */ 
/* 2479:     */ 
/* 2480:     */ 
/* 2481:2486 */     this.dimensionsTable.setFocusable(false);
/* 2482:2487 */     this.dimensionsTable.setIntercellSpacing(new Dimension(6, 4));
/* 2483:2488 */     this.dimensionsTable.setName("dimensionsTable");
/* 2484:2489 */     this.dimensionsTable.setRowSelectionAllowed(false);
/* 2485:     */     
/* 2486:2491 */     this.sketchLabel.setText(resourceMap.getString("sketchLabel.text", new Object[0]));
/* 2487:2492 */     this.sketchLabel.setName("sketchLabel");
/* 2488:     */     
/* 2489:2494 */     this.crossSectionSketchLabel.setText(null);
/* 2490:2495 */     this.crossSectionSketchLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
/* 2491:2496 */     this.crossSectionSketchLabel.setName("crossSectionSketchLabel");
/* 2492:     */     
/* 2493:2498 */     this.memberCostLabel.setText(resourceMap.getString("memberCostLabel.text", new Object[0]));
/* 2494:2499 */     this.memberCostLabel.setName("memberCostLabel");
/* 2495:     */     
/* 2496:2501 */     this.memberCostTable.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
/* 2497:2502 */     this.memberCostTable.setModel(new DefaultTableModel(new Object[][] { { "Unit Cost", null }, { "Member Cost", null } }, new String[] { "Title 1", "Title 2" })
/* 2498:     */     {
/* 2499:2511 */       boolean[] canEdit = { false, false };
/* 2500:     */       
/* 2501:     */       public boolean isCellEditable(int rowIndex, int columnIndex)
/* 2502:     */       {
/* 2503:2516 */         return this.canEdit[columnIndex];
/* 2504:     */       }
/* 2505:2518 */     });
/* 2506:2519 */     this.memberCostTable.setFocusable(false);
/* 2507:2520 */     this.memberCostTable.setIntercellSpacing(new Dimension(6, 4));
/* 2508:2521 */     this.memberCostTable.setName("memberCostTable");
/* 2509:2522 */     this.memberCostTable.setRowSelectionAllowed(false);
/* 2510:     */     
/* 2511:2524 */     this.strengthCurveLabel.setText(null);
/* 2512:2525 */     this.strengthCurveLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
/* 2513:2526 */     this.strengthCurveLabel.setName("strengthCurveLabel");
/* 2514:     */     
/* 2515:2528 */     this.graphAllCheck.setText(resourceMap.getString("graphAllCheck.text", new Object[0]));
/* 2516:2529 */     this.graphAllCheck.setName("graphAllCheck");
/* 2517:     */     
/* 2518:2531 */     this.curveLabel.setText(resourceMap.getString("curveLabel.text", new Object[0]));
/* 2519:2532 */     this.curveLabel.setName("curveLabel");
/* 2520:     */     
/* 2521:2534 */     this.memberSelectLabel.setText(resourceMap.getString("memberSelectLabel.text", new Object[0]));
/* 2522:2535 */     this.memberSelectLabel.setName("memberSelectLabel");
/* 2523:     */     
/* 2524:2537 */     this.memberSelecButtonPanel.setName("memberSelecButtonPanel");
/* 2525:2538 */     this.memberSelecButtonPanel.setLayout(new BoxLayout(this.memberSelecButtonPanel, 0));
/* 2526:     */     
/* 2527:2540 */     this.memberSelectLeftButton.setIcon(resourceMap.getIcon("memberSelectLeftButton.icon"));
/* 2528:2541 */     this.memberSelectLeftButton.setEnabled(false);
/* 2529:2542 */     this.memberSelectLeftButton.setMargin(new Insets(1, 1, 1, 1));
/* 2530:2543 */     this.memberSelectLeftButton.setMaximumSize(new Dimension(16, 23));
/* 2531:2544 */     this.memberSelectLeftButton.setMinimumSize(new Dimension(16, 23));
/* 2532:2545 */     this.memberSelectLeftButton.setName("memberSelectLeftButton");
/* 2533:2546 */     this.memberSelectLeftButton.setPreferredSize(new Dimension(16, 23));
/* 2534:2547 */     this.memberSelecButtonPanel.add(this.memberSelectLeftButton);
/* 2535:     */     
/* 2536:2549 */     this.memberSelectRightButton.setIcon(resourceMap.getIcon("memberSelectRightButton.icon"));
/* 2537:2550 */     this.memberSelectRightButton.setEnabled(false);
/* 2538:2551 */     this.memberSelectRightButton.setMargin(new Insets(1, 1, 1, 1));
/* 2539:2552 */     this.memberSelectRightButton.setMaximumSize(new Dimension(16, 23));
/* 2540:2553 */     this.memberSelectRightButton.setMinimumSize(new Dimension(16, 23));
/* 2541:2554 */     this.memberSelectRightButton.setName("memberSelectRightButton");
/* 2542:2555 */     this.memberSelectRightButton.setPreferredSize(new Dimension(16, 23));
/* 2543:2556 */     this.memberSelecButtonPanel.add(this.memberSelectRightButton);
/* 2544:     */     
/* 2545:2558 */     this.memberSelectBox.setModel(new ExtendedComboBoxModel());
/* 2546:2559 */     this.memberSelectBox.setName("memberSelectBox");
/* 2547:     */     
/* 2548:2561 */     GroupLayout memberDetailPanelLayout = new GroupLayout(this.memberDetailPanel);
/* 2549:2562 */     this.memberDetailPanel.setLayout(memberDetailPanelLayout);
/* 2550:2563 */     memberDetailPanelLayout.setHorizontalGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, memberDetailPanelLayout.createSequentialGroup().addContainerGap().addGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.strengthCurveLabel, GroupLayout.Alignment.LEADING, -1, 432, 32767).addComponent(this.materialPropertiesTable, GroupLayout.Alignment.LEADING, -1, 432, 32767).addComponent(this.materialPropertiesLabel, GroupLayout.Alignment.LEADING).addComponent(this.memberCostLabel, GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.LEADING, memberDetailPanelLayout.createSequentialGroup().addGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.dimensionsLabel).addComponent(this.dimensionsTable, -1, 309, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.sketchLabel).addComponent(this.crossSectionSketchLabel, -2, 118, -2))).addComponent(this.memberCostTable, GroupLayout.Alignment.LEADING, -1, 432, 32767).addGroup(GroupLayout.Alignment.LEADING, memberDetailPanelLayout.createSequentialGroup().addComponent(this.curveLabel).addGap(18, 18, 18).addComponent(this.graphAllCheck).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 43, 32767).addComponent(this.memberSelectLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.memberSelectBox, -2, 59, -2).addGap(0, 0, 0).addComponent(this.memberSelecButtonPanel, -2, -1, -2))).addContainerGap()));
/* 2551:     */     
/* 2552:     */ 
/* 2553:     */ 
/* 2554:     */ 
/* 2555:     */ 
/* 2556:     */ 
/* 2557:     */ 
/* 2558:     */ 
/* 2559:     */ 
/* 2560:     */ 
/* 2561:     */ 
/* 2562:     */ 
/* 2563:     */ 
/* 2564:     */ 
/* 2565:     */ 
/* 2566:     */ 
/* 2567:     */ 
/* 2568:     */ 
/* 2569:     */ 
/* 2570:     */ 
/* 2571:     */ 
/* 2572:     */ 
/* 2573:     */ 
/* 2574:     */ 
/* 2575:     */ 
/* 2576:     */ 
/* 2577:     */ 
/* 2578:     */ 
/* 2579:     */ 
/* 2580:2593 */     memberDetailPanelLayout.setVerticalGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(memberDetailPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.materialPropertiesLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.materialPropertiesTable, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.dimensionsLabel).addComponent(this.sketchLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(memberDetailPanelLayout.createSequentialGroup().addComponent(this.dimensionsTable, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.memberCostLabel)).addComponent(this.crossSectionSketchLabel, -2, 81, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.memberCostTable, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(memberDetailPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.curveLabel).addComponent(this.graphAllCheck).addComponent(this.memberSelectLabel).addComponent(this.memberSelectBox, -2, -1, -2)).addComponent(this.memberSelecButtonPanel, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.strengthCurveLabel, -1, 221, 32767).addContainerGap()));
/* 2581:     */     
/* 2582:     */ 
/* 2583:     */ 
/* 2584:     */ 
/* 2585:     */ 
/* 2586:     */ 
/* 2587:     */ 
/* 2588:     */ 
/* 2589:     */ 
/* 2590:     */ 
/* 2591:     */ 
/* 2592:     */ 
/* 2593:     */ 
/* 2594:     */ 
/* 2595:     */ 
/* 2596:     */ 
/* 2597:     */ 
/* 2598:     */ 
/* 2599:     */ 
/* 2600:     */ 
/* 2601:     */ 
/* 2602:     */ 
/* 2603:     */ 
/* 2604:     */ 
/* 2605:     */ 
/* 2606:     */ 
/* 2607:     */ 
/* 2608:     */ 
/* 2609:     */ 
/* 2610:     */ 
/* 2611:     */ 
/* 2612:     */ 
/* 2613:2626 */     this.materialPropertiesTable.getColumnModel().getColumn(0).setResizable(false);
/* 2614:2627 */     this.materialPropertiesTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("materialPropertiesTable.columnModel.title0", new Object[0]));
/* 2615:2628 */     this.materialPropertiesTable.getColumnModel().getColumn(1).setResizable(false);
/* 2616:2629 */     this.materialPropertiesTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("materialPropertiesTable.columnModel.title1", new Object[0]));
/* 2617:2630 */     this.dimensionsTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("dimensionsTable.columnModel.title0", new Object[0]));
/* 2618:2631 */     this.dimensionsTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("dimensionsTable.columnModel.title1", new Object[0]));
/* 2619:2632 */     this.memberCostTable.getColumnModel().getColumn(0).setResizable(false);
/* 2620:2633 */     this.memberCostTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("materialPropertiesTable.columnModel.title0", new Object[0]));
/* 2621:2634 */     this.memberCostTable.getColumnModel().getColumn(1).setResizable(false);
/* 2622:2635 */     this.memberCostTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("materialPropertiesTable.columnModel.title1", new Object[0]));
/* 2623:     */     
/* 2624:2637 */     this.memberDetailTabs.addTab(resourceMap.getString("memberDetailPanel.TabConstraints.tabTitle", new Object[0]), this.memberDetailPanel);
/* 2625:     */     
/* 2626:2639 */     this.memberTabs.addTab(resourceMap.getString("memberDetailTabs.TabConstraints.tabTitle", new Object[0]), this.memberDetailTabs);
/* 2627:     */     
/* 2628:2641 */     GroupLayout memberPanelLayout = new GroupLayout(this.memberPanel);
/* 2629:2642 */     this.memberPanel.setLayout(memberPanelLayout);
/* 2630:2643 */     memberPanelLayout.setHorizontalGroup(memberPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.memberTabs, GroupLayout.Alignment.TRAILING, -2, 466, 32767));
/* 2631:     */     
/* 2632:     */ 
/* 2633:     */ 
/* 2634:2647 */     memberPanelLayout.setVerticalGroup(memberPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.memberTabs, -1, 603, 32767));
/* 2635:     */     
/* 2636:     */ 
/* 2637:     */ 
/* 2638:     */ 
/* 2639:2652 */     GroupLayout designPanelLayout = new GroupLayout(this.designPanel);
/* 2640:2653 */     this.designPanel.setLayout(designPanelLayout);
/* 2641:2654 */     designPanelLayout.setHorizontalGroup(designPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, designPanelLayout.createSequentialGroup().addComponent(this.drawingPanel, -1, 681, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.memberPanel, -2, -1, -2)));
/* 2642:     */     
/* 2643:     */ 
/* 2644:     */ 
/* 2645:     */ 
/* 2646:     */ 
/* 2647:     */ 
/* 2648:2661 */     designPanelLayout.setVerticalGroup(designPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.drawingPanel, -1, 603, 32767).addComponent(this.memberPanel, -1, -1, 32767));
/* 2649:     */     
/* 2650:     */ 
/* 2651:     */ 
/* 2652:     */ 
/* 2653:     */ 
/* 2654:2667 */     this.cardPanel.add(this.designPanel, "designPanel");
/* 2655:     */     
/* 2656:2669 */     this.flyThruAnimationCanvas.setName("flyThruAnimationCanvas");
/* 2657:2670 */     this.cardPanel.add(this.flyThruAnimationCanvas, "flyThruAnimationPanel");
/* 2658:     */     
/* 2659:2672 */     this.fixedEyeAnimationCanvas.setName("fixedEyeAnimationCanvas");
/* 2660:2673 */     this.cardPanel.add(this.fixedEyeAnimationCanvas, "fixedEyeAnimationPanel");
/* 2661:     */     
/* 2662:2675 */     GroupLayout mainPanelLayout = new GroupLayout(this.mainPanel);
/* 2663:2676 */     this.mainPanel.setLayout(mainPanelLayout);
/* 2664:2677 */     mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.cardPanel, GroupLayout.Alignment.TRAILING, -1, 1154, 32767).addComponent(this.bottomToolBar, -1, 1154, 32767).addComponent(this.topToolBar, -1, 1154, 32767));
/* 2665:     */     
/* 2666:     */ 
/* 2667:     */ 
/* 2668:     */ 
/* 2669:     */ 
/* 2670:2683 */     mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addComponent(this.topToolBar, -2, 32, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.bottomToolBar, -2, 31, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.cardPanel, -1, 603, 32767)));
/* 2671:     */     
/* 2672:     */ 
/* 2673:     */ 
/* 2674:     */ 
/* 2675:     */ 
/* 2676:     */ 
/* 2677:     */ 
/* 2678:     */ 
/* 2679:     */ 
/* 2680:2693 */     this.toolsDialog.setTitle(resourceMap.getString("toolsDialog.title", new Object[0]));
/* 2681:2694 */     this.toolsDialog.setFocusable(false);
/* 2682:2695 */     this.toolsDialog.setFocusableWindowState(false);
/* 2683:2696 */     this.toolsDialog.setIconImage(WPBDApp.getApplication().getImageResource("tools.png"));
/* 2684:2697 */     this.toolsDialog.setMinimumSize(new Dimension(80, 33));
/* 2685:2698 */     this.toolsDialog.setName("toolsDialog");
/* 2686:2699 */     this.toolsDialog.setResizable(false);
/* 2687:2700 */     this.toolsDialog.getContentPane().setLayout(new BoxLayout(this.toolsDialog.getContentPane(), 2));
/* 2688:     */     
/* 2689:2702 */     this.toolsToolbar.setFloatable(false);
/* 2690:2703 */     this.toolsToolbar.setName("toolsToolbar");
/* 2691:     */     
/* 2692:2705 */     this.editJointsButton.setAction(actionMap.get("editJoints"));
/* 2693:2706 */     this.toolsButtonGroup.add(this.editJointsButton);
/* 2694:2707 */     this.editJointsButton.setFocusable(false);
/* 2695:2708 */     this.editJointsButton.setHideActionText(true);
/* 2696:2709 */     this.editJointsButton.setHorizontalTextPosition(0);
/* 2697:2710 */     this.editJointsButton.setName("editJointsButton");
/* 2698:2711 */     this.editJointsButton.setVerticalTextPosition(3);
/* 2699:2712 */     this.toolsToolbar.add(this.editJointsButton);
/* 2700:     */     
/* 2701:2714 */     this.editMembersButton.setAction(actionMap.get("editMembers"));
/* 2702:2715 */     this.toolsButtonGroup.add(this.editMembersButton);
/* 2703:2716 */     this.editMembersButton.setFocusable(false);
/* 2704:2717 */     this.editMembersButton.setHideActionText(true);
/* 2705:2718 */     this.editMembersButton.setHorizontalTextPosition(0);
/* 2706:2719 */     this.editMembersButton.setName("editMembersButton");
/* 2707:2720 */     this.editMembersButton.setVerticalTextPosition(3);
/* 2708:2721 */     this.toolsToolbar.add(this.editMembersButton);
/* 2709:     */     
/* 2710:2723 */     this.editSelectButton.setAction(actionMap.get("editSelect"));
/* 2711:2724 */     this.toolsButtonGroup.add(this.editSelectButton);
/* 2712:2725 */     this.editSelectButton.setFocusable(false);
/* 2713:2726 */     this.editSelectButton.setHideActionText(true);
/* 2714:2727 */     this.editSelectButton.setHorizontalTextPosition(0);
/* 2715:2728 */     this.editSelectButton.setName("editSelectButton");
/* 2716:2729 */     this.editSelectButton.setVerticalTextPosition(3);
/* 2717:2730 */     this.toolsToolbar.add(this.editSelectButton);
/* 2718:     */     
/* 2719:2732 */     this.editEraseButton.setAction(actionMap.get("editErase"));
/* 2720:2733 */     this.toolsButtonGroup.add(this.editEraseButton);
/* 2721:2734 */     this.editEraseButton.setFocusable(false);
/* 2722:2735 */     this.editEraseButton.setHideActionText(true);
/* 2723:2736 */     this.editEraseButton.setHorizontalTextPosition(0);
/* 2724:2737 */     this.editEraseButton.setName("editEraseButton");
/* 2725:2738 */     this.editEraseButton.setVerticalTextPosition(3);
/* 2726:2739 */     this.toolsToolbar.add(this.editEraseButton);
/* 2727:     */     
/* 2728:2741 */     this.toolsDialog.getContentPane().add(this.toolsToolbar);
/* 2729:     */     
/* 2730:2743 */     this.memberEditPopup.setModal(true);
/* 2731:2744 */     this.memberEditPopup.setName("memberEditPopup");
/* 2732:2745 */     this.memberEditPopup.setResizable(false);
/* 2733:2746 */     this.memberEditPopup.setUndecorated(true);
/* 2734:     */     
/* 2735:2748 */     this.memberPopupPanel.setBorder(BorderFactory.createBevelBorder(0));
/* 2736:2749 */     this.memberPopupPanel.setName("memberPopupPanel");
/* 2737:     */     
/* 2738:2751 */     this.memberPopupMaterialBox.setModel(this.bridge.getInventory().getMaterialBoxModel());
/* 2739:2752 */     this.memberPopupMaterialBox.setName("memberPopupMaterialBox");
/* 2740:     */     
/* 2741:2754 */     this.memberPopupMaterialLabel.setLabelFor(this.memberPopupMaterialBox);
/* 2742:2755 */     this.memberPopupMaterialLabel.setText(resourceMap.getString("memberPopupMaterialLabel.text", new Object[0]));
/* 2743:2756 */     this.memberPopupMaterialLabel.setName("memberPopupMaterialLabel");
/* 2744:     */     
/* 2745:2758 */     this.memberPopupSectionBox.setModel(this.bridge.getInventory().getSectionBoxModel());
/* 2746:2759 */     this.memberPopupSectionBox.setName("memberPopupSectionBox");
/* 2747:     */     
/* 2748:2761 */     this.memberPopupSectionLabel.setLabelFor(this.memberPopupSectionBox);
/* 2749:2762 */     this.memberPopupSectionLabel.setText(resourceMap.getString("memberPopupSectionLabel.text", new Object[0]));
/* 2750:2763 */     this.memberPopupSectionLabel.setName("memberPopupSectionLabel");
/* 2751:     */     
/* 2752:2765 */     this.memberPopupSizeLabel.setLabelFor(this.memberPopupSizeBox);
/* 2753:2766 */     this.memberPopupSizeLabel.setText(resourceMap.getString("memberPopupSizeLabel.text", new Object[0]));
/* 2754:2767 */     this.memberPopupSizeLabel.setName("memberPopupSizeLabel");
/* 2755:     */     
/* 2756:2769 */     this.memberPopupSizeBox.setModel(this.bridge.getInventory().getSizeBoxModel());
/* 2757:2770 */     this.memberPopupSizeBox.setName("memberPopupSizeBox");
/* 2758:     */     
/* 2759:2772 */     this.memberPopupIncreaseSizeButton.setAction(actionMap.get("increaseMemberSize"));
/* 2760:2773 */     this.memberPopupIncreaseSizeButton.setHorizontalAlignment(10);
/* 2761:2774 */     this.memberPopupIncreaseSizeButton.setMargin(new Insets(0, 0, 0, 2));
/* 2762:2775 */     this.memberPopupIncreaseSizeButton.setName("memberPopupIncreaseSizeButton");
/* 2763:     */     
/* 2764:2777 */     this.memberPopupDecreaseSizeButton.setAction(actionMap.get("decreaseMemberSize"));
/* 2765:2778 */     this.memberPopupDecreaseSizeButton.setHorizontalAlignment(10);
/* 2766:2779 */     this.memberPopupDecreaseSizeButton.setMargin(new Insets(0, 0, 0, 2));
/* 2767:2780 */     this.memberPopupDecreaseSizeButton.setName("memberPopupDecreaseSizeButton");
/* 2768:     */     
/* 2769:2782 */     this.memberPopupDeleteButton.setAction(actionMap.get("delete"));
/* 2770:2783 */     this.memberPopupDeleteButton.setHorizontalAlignment(10);
/* 2771:2784 */     this.memberPopupDeleteButton.setMargin(new Insets(0, 0, 0, 2));
/* 2772:2785 */     this.memberPopupDeleteButton.setName("memberPopupDeleteButton");
/* 2773:2786 */     this.memberPopupDeleteButton.addActionListener(new ActionListener()
/* 2774:     */     {
/* 2775:     */       public void actionPerformed(ActionEvent evt)
/* 2776:     */       {
/* 2777:2788 */         WPBDView.this.memberPopupDeleteButtonActionPerformed(evt);
/* 2778:     */       }
/* 2779:2791 */     });
/* 2780:2792 */     this.memberPopupDoneButton.setText(resourceMap.getString("memberPopupDoneButton.text", new Object[0]));
/* 2781:2793 */     this.memberPopupDoneButton.setName("memberPopupDoneButton");
/* 2782:2794 */     this.memberPopupDoneButton.addActionListener(new ActionListener()
/* 2783:     */     {
/* 2784:     */       public void actionPerformed(ActionEvent evt)
/* 2785:     */       {
/* 2786:2796 */         WPBDView.this.memberPopupDoneButtonActionPerformed(evt);
/* 2787:     */       }
/* 2788:2799 */     });
/* 2789:2800 */     this.memberPopupMemberListButton.setAction(actionMap.get("toggleMemberList"));
/* 2790:2801 */     this.memberPopupMemberListButton.setMargin(new Insets(0, 0, 0, 2));
/* 2791:2802 */     this.memberPopupMemberListButton.setName("memberPopupMemberListButton");
/* 2792:     */     
/* 2793:2804 */     GroupLayout memberPopupPanelLayout = new GroupLayout(this.memberPopupPanel);
/* 2794:2805 */     this.memberPopupPanel.setLayout(memberPopupPanelLayout);
/* 2795:2806 */     memberPopupPanelLayout.setHorizontalGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(memberPopupPanelLayout.createSequentialGroup().addContainerGap().addGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, memberPopupPanelLayout.createSequentialGroup().addGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.memberPopupDeleteButton, GroupLayout.Alignment.LEADING, -1, 169, 32767).addComponent(this.memberPopupDecreaseSizeButton, GroupLayout.Alignment.LEADING, -1, -1, 32767).addComponent(this.memberPopupIncreaseSizeButton, -1, 169, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.memberPopupDoneButton).addComponent(this.memberPopupMemberListButton))).addGroup(memberPopupPanelLayout.createSequentialGroup().addComponent(this.memberPopupSizeLabel).addGap(27, 27, 27).addComponent(this.memberPopupSizeBox, 0, 225, 32767)).addGroup(memberPopupPanelLayout.createSequentialGroup().addComponent(this.memberPopupSectionLabel).addGap(9, 9, 9).addComponent(this.memberPopupSectionBox, 0, 225, 32767)).addGroup(memberPopupPanelLayout.createSequentialGroup().addComponent(this.memberPopupMaterialLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.memberPopupMaterialBox, 0, 225, 32767))).addContainerGap()));
/* 2796:     */     
/* 2797:     */ 
/* 2798:     */ 
/* 2799:     */ 
/* 2800:     */ 
/* 2801:     */ 
/* 2802:     */ 
/* 2803:     */ 
/* 2804:     */ 
/* 2805:     */ 
/* 2806:     */ 
/* 2807:     */ 
/* 2808:     */ 
/* 2809:     */ 
/* 2810:     */ 
/* 2811:     */ 
/* 2812:     */ 
/* 2813:     */ 
/* 2814:     */ 
/* 2815:     */ 
/* 2816:     */ 
/* 2817:     */ 
/* 2818:     */ 
/* 2819:     */ 
/* 2820:     */ 
/* 2821:     */ 
/* 2822:     */ 
/* 2823:2834 */     memberPopupPanelLayout.setVerticalGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(memberPopupPanelLayout.createSequentialGroup().addContainerGap().addGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.memberPopupMaterialLabel).addComponent(this.memberPopupMaterialBox, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.memberPopupSectionLabel).addComponent(this.memberPopupSectionBox, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.memberPopupSizeLabel).addComponent(this.memberPopupSizeBox, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.memberPopupIncreaseSizeButton).addComponent(this.memberPopupMemberListButton)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.memberPopupDecreaseSizeButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(memberPopupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.memberPopupDeleteButton).addComponent(this.memberPopupDoneButton, -1, 29, 32767)).addContainerGap()));
/* 2824:     */     
/* 2825:     */ 
/* 2826:     */ 
/* 2827:     */ 
/* 2828:     */ 
/* 2829:     */ 
/* 2830:     */ 
/* 2831:     */ 
/* 2832:     */ 
/* 2833:     */ 
/* 2834:     */ 
/* 2835:     */ 
/* 2836:     */ 
/* 2837:     */ 
/* 2838:     */ 
/* 2839:     */ 
/* 2840:     */ 
/* 2841:     */ 
/* 2842:     */ 
/* 2843:     */ 
/* 2844:     */ 
/* 2845:     */ 
/* 2846:     */ 
/* 2847:     */ 
/* 2848:     */ 
/* 2849:     */ 
/* 2850:     */ 
/* 2851:2862 */     GroupLayout memberEditPopupLayout = new GroupLayout(this.memberEditPopup.getContentPane());
/* 2852:2863 */     this.memberEditPopup.getContentPane().setLayout(memberEditPopupLayout);
/* 2853:2864 */     memberEditPopupLayout.setHorizontalGroup(memberEditPopupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.memberPopupPanel, -2, -1, -2));
/* 2854:     */     
/* 2855:     */ 
/* 2856:     */ 
/* 2857:2868 */     memberEditPopupLayout.setVerticalGroup(memberEditPopupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.memberPopupPanel, -2, -1, -2));
/* 2858:     */     
/* 2859:     */ 
/* 2860:     */ 
/* 2861:     */ 
/* 2862:2873 */     this.draftingPopup.setName("draftingPopup");
/* 2863:     */     
/* 2864:2875 */     this.draftingPopupJoints.setAction(actionMap.get("editJoints"));
/* 2865:2876 */     this.draftingPopupJoints.setSelected(true);
/* 2866:2877 */     this.draftingPopupJoints.setName("draftingPopupJoints");
/* 2867:2878 */     this.draftingPopup.add(this.draftingPopupJoints);
/* 2868:     */     
/* 2869:2880 */     this.draftingPopupMembers.setAction(actionMap.get("editMembers"));
/* 2870:2881 */     this.draftingPopupMembers.setSelected(true);
/* 2871:2882 */     this.draftingPopupMembers.setName("draftingPopupMembers");
/* 2872:2883 */     this.draftingPopup.add(this.draftingPopupMembers);
/* 2873:     */     
/* 2874:2885 */     this.draftingPopupSelect.setAction(actionMap.get("editSelect"));
/* 2875:2886 */     this.draftingPopupSelect.setSelected(true);
/* 2876:2887 */     this.draftingPopupSelect.setName("draftingPopupSelect");
/* 2877:2888 */     this.draftingPopup.add(this.draftingPopupSelect);
/* 2878:     */     
/* 2879:2890 */     this.draftingPopupErase.setAction(actionMap.get("editErase"));
/* 2880:2891 */     this.draftingPopupErase.setSelected(true);
/* 2881:2892 */     this.draftingPopupErase.setName("draftingPopupErase");
/* 2882:2893 */     this.draftingPopup.add(this.draftingPopupErase);
/* 2883:     */     
/* 2884:2895 */     this.draftingPopupSep01.setName("draftingPopupSep01");
/* 2885:2896 */     this.draftingPopup.add(this.draftingPopupSep01);
/* 2886:     */     
/* 2887:2898 */     this.draftingPopupSelectAll.setAction(actionMap.get("selectAll"));
/* 2888:2899 */     this.draftingPopupSelectAll.setName("draftingPopupSelectAll");
/* 2889:2900 */     this.draftingPopup.add(this.draftingPopupSelectAll);
/* 2890:     */     
/* 2891:2902 */     this.draftingPopupSep02.setName("draftingPopupSep02");
/* 2892:2903 */     this.draftingPopup.add(this.draftingPopupSep02);
/* 2893:     */     
/* 2894:2905 */     this.draftingPopupMemberList.setAction(actionMap.get("toggleMemberList"));
/* 2895:2906 */     this.draftingPopupMemberList.setSelected(true);
/* 2896:2907 */     this.draftingPopupMemberList.setName("draftingPopupMemberList");
/* 2897:2908 */     this.draftingPopup.add(this.draftingPopupMemberList);
/* 2898:     */     
/* 2899:2910 */     this.draftingPopupSep03.setName("draftingPopupSep03");
/* 2900:2911 */     this.draftingPopup.add(this.draftingPopupSep03);
/* 2901:     */     
/* 2902:2913 */     this.draftingPopupCoarseGrid.setAction(actionMap.get("setCoarseGrid"));
/* 2903:2914 */     this.draftingPopupCoarseGrid.setSelected(true);
/* 2904:2915 */     this.draftingPopupCoarseGrid.setName("draftingPopupCoarseGrid");
/* 2905:2916 */     this.draftingPopup.add(this.draftingPopupCoarseGrid);
/* 2906:     */     
/* 2907:2918 */     this.draftingPopupMediumGrid.setAction(actionMap.get("setMediumGrid"));
/* 2908:2919 */     this.draftingPopupMediumGrid.setSelected(true);
/* 2909:2920 */     this.draftingPopupMediumGrid.setName("draftingPopupMediumGrid");
/* 2910:2921 */     this.draftingPopup.add(this.draftingPopupMediumGrid);
/* 2911:     */     
/* 2912:2923 */     this.draftingPopupFineGrid.setAction(actionMap.get("setFineGrid"));
/* 2913:2924 */     this.draftingPopupFineGrid.setSelected(true);
/* 2914:2925 */     this.draftingPopupFineGrid.setName("draftingPopupFineGrid");
/* 2915:2926 */     this.draftingPopup.add(this.draftingPopupFineGrid);
/* 2916:     */     
/* 2917:2928 */     this.keyCodeDialog.setTitle(resourceMap.getString("keyCodeDialog.title", new Object[0]));
/* 2918:2929 */     this.keyCodeDialog.setIconImage(WPBDApp.getApplication().getImageResource("appicon.png"));
/* 2919:2930 */     this.keyCodeDialog.setName("keyCodeDialog");
/* 2920:2931 */     this.keyCodeDialog.setResizable(false);
/* 2921:     */     
/* 2922:2933 */     this.keyCodeLabel.setText(resourceMap.getString("keyCodeLabel.text", new Object[0]));
/* 2923:2934 */     this.keyCodeLabel.setName("keyCodeLabel");
/* 2924:     */     
/* 2925:2936 */     this.keyCodeTextField.setColumns(10);
/* 2926:2937 */     this.keyCodeTextField.setName("keyCodeTextField");
/* 2927:2938 */     this.keyCodeTextField.addKeyListener(new KeyAdapter()
/* 2928:     */     {
/* 2929:     */       public void keyTyped(KeyEvent evt)
/* 2930:     */       {
/* 2931:2940 */         WPBDView.this.keyCodeTextFieldKeyTyped(evt);
/* 2932:     */       }
/* 2933:2943 */     });
/* 2934:2944 */     this.keyCodeOkButton.setText(resourceMap.getString("keyCodeOkButton.text", new Object[0]));
/* 2935:2945 */     this.keyCodeOkButton.setName("keyCodeOkButton");
/* 2936:2946 */     this.keyCodeOkButton.addActionListener(new ActionListener()
/* 2937:     */     {
/* 2938:     */       public void actionPerformed(ActionEvent evt)
/* 2939:     */       {
/* 2940:2948 */         WPBDView.this.keyCodeOkButtonActionPerformed(evt);
/* 2941:     */       }
/* 2942:2951 */     });
/* 2943:2952 */     this.keyCodeCancelButton.setText(resourceMap.getString("keyCodeCancelButton.text", new Object[0]));
/* 2944:2953 */     this.keyCodeCancelButton.setName("keyCodeCancelButton");
/* 2945:2954 */     this.keyCodeCancelButton.addActionListener(new ActionListener()
/* 2946:     */     {
/* 2947:     */       public void actionPerformed(ActionEvent evt)
/* 2948:     */       {
/* 2949:2956 */         WPBDView.this.keyCodeCancelButtonActionPerformed(evt);
/* 2950:     */       }
/* 2951:2959 */     });
/* 2952:2960 */     this.keyCodeErrorLabel.setFont(this.keyCodeErrorLabel.getFont().deriveFont(this.keyCodeErrorLabel.getFont().getStyle() | 0x1));
/* 2953:2961 */     this.keyCodeErrorLabel.setForeground(resourceMap.getColor("keyCodeErrorLabel.foreground"));
/* 2954:2962 */     this.keyCodeErrorLabel.setText(resourceMap.getString("keyCodeErrorLabel.text", new Object[0]));
/* 2955:2963 */     this.keyCodeErrorLabel.setName("keyCodeErrorLabel");
/* 2956:     */     
/* 2957:2965 */     GroupLayout keyCodeDialogLayout = new GroupLayout(this.keyCodeDialog.getContentPane());
/* 2958:2966 */     this.keyCodeDialog.getContentPane().setLayout(keyCodeDialogLayout);
/* 2959:2967 */     keyCodeDialogLayout.setHorizontalGroup(keyCodeDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, keyCodeDialogLayout.createSequentialGroup().addContainerGap().addComponent(this.keyCodeLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(keyCodeDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.keyCodeTextField, -2, 131, -2).addComponent(this.keyCodeErrorLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(keyCodeDialogLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.keyCodeOkButton, -1, -1, 32767).addComponent(this.keyCodeCancelButton, -1, -1, 32767)).addContainerGap()));
/* 2960:     */     
/* 2961:     */ 
/* 2962:     */ 
/* 2963:     */ 
/* 2964:     */ 
/* 2965:     */ 
/* 2966:     */ 
/* 2967:     */ 
/* 2968:     */ 
/* 2969:     */ 
/* 2970:     */ 
/* 2971:     */ 
/* 2972:     */ 
/* 2973:     */ 
/* 2974:2982 */     keyCodeDialogLayout.setVerticalGroup(keyCodeDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(keyCodeDialogLayout.createSequentialGroup().addContainerGap().addGroup(keyCodeDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.keyCodeCancelButton).addComponent(this.keyCodeTextField, -2, -1, -2).addComponent(this.keyCodeLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(keyCodeDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.keyCodeErrorLabel).addComponent(this.keyCodeOkButton)).addContainerGap(-1, 32767)));
/* 2975:     */     
/* 2976:     */ 
/* 2977:     */ 
/* 2978:     */ 
/* 2979:     */ 
/* 2980:     */ 
/* 2981:     */ 
/* 2982:     */ 
/* 2983:     */ 
/* 2984:     */ 
/* 2985:     */ 
/* 2986:     */ 
/* 2987:     */ 
/* 2988:     */ 
/* 2989:2997 */     this.keyCodeDialog.pack();
/* 2990:2998 */     this.keyCodeErrorLabel.setVisible(false);
/* 2991:     */     
/* 2992:3000 */     this.drawingBoardLabel.setFont(resourceMap.getFont("drawingBoardLabel.font"));
/* 2993:3001 */     this.drawingBoardLabel.setForeground(resourceMap.getColor("drawingBoardLabel.foreground"));
/* 2994:3002 */     this.drawingBoardLabel.setHorizontalAlignment(0);
/* 2995:3003 */     this.drawingBoardLabel.setText(resourceMap.getString("drawingBoardLabel.text", new Object[0]));
/* 2996:3004 */     this.drawingBoardLabel.setVerticalAlignment(1);
/* 2997:3005 */     this.drawingBoardLabel.setName("drawingBoardLabel");
/* 2998:     */     
/* 2999:3007 */     setComponent(this.mainPanel);
/* 3000:3008 */     setMenuBar(this.menuBar);
/* 3001:3009 */     setStatusBar(this.statusPanel);
/* 3002:     */   }
/* 3003:     */   
/* 3004:     */   private void memberPopupDoneButtonActionPerformed(ActionEvent evt)
/* 3005:     */   {
/* 3006:3013 */     this.memberEditPopup.setVisible(false);
/* 3007:     */   }
/* 3008:     */   
/* 3009:     */   private void memberPopupDeleteButtonActionPerformed(ActionEvent evt)
/* 3010:     */   {
/* 3011:3017 */     this.memberEditPopup.setVisible(false);
/* 3012:     */   }
/* 3013:     */   
/* 3014:     */   private void searchForHelpMenuItemActionPerformed(ActionEvent evt)
/* 3015:     */   {
/* 3016:3021 */     Help.getBroker().setCurrentView("Search");
/* 3017:3022 */     Help.getBroker().setDisplayed(true);
/* 3018:     */   }
/* 3019:     */   
/* 3020:     */   private void helpTopicsMenuItemActionPerformed(ActionEvent evt)
/* 3021:     */   {
/* 3022:3026 */     Help.getBroker().setCurrentView("TOC");
/* 3023:3027 */     Help.getBroker().setCurrentID("hlp_purposes");
/* 3024:3028 */     Help.getBroker().setDisplayed(true);
/* 3025:     */   }
/* 3026:     */   
/* 3027:     */   private void keyCodeOkButtonActionPerformed(ActionEvent evt)
/* 3028:     */   {
/* 3029:3032 */     if (querySaveIfDirty())
/* 3030:     */     {
/* 3031:3033 */       DesignConditions conditions = DesignConditions.fromKeyCode(this.keyCodeTextField.getText());
/* 3032:3034 */       if (conditions == null)
/* 3033:     */       {
/* 3034:3036 */         this.keyCodeErrorLabel.setVisible(true);
/* 3035:3037 */         return;
/* 3036:     */       }
/* 3037:3040 */       recordRecentFileUse();
/* 3038:     */       
/* 3039:3042 */       this.keyCodeDialog.setVisible(false);
/* 3040:     */       
/* 3041:3044 */       WPBDApp.saveToLocalStorage(this.keyCodeTextField.getText(), "keyCode.xml");
/* 3042:     */       
/* 3043:3046 */       this.bridge.initialize(conditions, "00000A-", null);
/* 3044:3047 */       this.bridgeDraftingView.initialize(conditions);
/* 3045:     */       
/* 3046:3049 */       setSketchModel(null);
/* 3047:     */       
/* 3048:3051 */       showDrawingBoard();
/* 3049:     */       
/* 3050:3053 */       uploadBridgeToDraftingPanel();
/* 3051:     */       
/* 3052:3055 */       setDefaultFile();
/* 3053:     */       
/* 3054:3057 */       setLoadTestButtonEnabled();
/* 3055:     */       
/* 3056:3059 */       setSelected(this.editJointsMenuItem, true);
/* 3057:3060 */       editJoints();
/* 3058:     */     }
/* 3059:     */   }
/* 3060:     */   
/* 3061:     */   private void keyCodeTextFieldKeyTyped(KeyEvent evt)
/* 3062:     */   {
/* 3063:3065 */     this.keyCodeErrorLabel.setVisible(false);
/* 3064:     */   }
/* 3065:     */   
/* 3066:     */   private void keyCodeCancelButtonActionPerformed(ActionEvent evt)
/* 3067:     */   {
/* 3068:3069 */     this.keyCodeDialog.setVisible(false);
/* 3069:     */   }
/* 3070:     */   
/* 3071:     */   @org.jdesktop.application.Action
/* 3072:     */   public void open()
/* 3073:     */   {
/* 3074:3075 */     if (this.animationPanelCardName.equals(this.selectedCard)) {
/* 3075:3076 */       selectCard("designPanel");
/* 3076:     */     }
/* 3077:3078 */     if (querySaveIfDirty())
/* 3078:     */     {
/* 3079:3079 */       File oldFile = this.fileChooser.getSelectedFile();
/* 3080:3080 */       int okCancel = this.fileChooser.showOpenDialog(this.mainPanel);
/* 3081:3081 */       if (okCancel == 0)
/* 3082:     */       {
/* 3083:3082 */         WPBDApp.saveToLocalStorage(this.fileChooser.getCurrentDirectory().getPath(), "fileChooserPath.xml");
/* 3084:     */         try
/* 3085:     */         {
/* 3086:3084 */           this.animation.stop();
/* 3087:3085 */           recordRecentFileUse(oldFile);
/* 3088:3086 */           this.bridge.read(this.fileChooser.getSelectedFile());
/* 3089:3087 */           if (this.bridge.getDesignConditions().isFromKeyCode()) {
/* 3090:3088 */             WPBDApp.saveToLocalStorage(this.bridge.getDesignConditions().getCodeString(), "keyCode.xml");
/* 3091:     */           }
/* 3092:3090 */           initializePostBridgeLoad();
/* 3093:     */         }
/* 3094:     */         catch (IOException e)
/* 3095:     */         {
/* 3096:3092 */           selectCard("nullPanel");
/* 3097:3093 */           showReadFailedMessage(e);
/* 3098:     */         }
/* 3099:     */       }
/* 3100:     */     }
/* 3101:     */   }
/* 3102:     */   
/* 3103:     */   @org.jdesktop.application.Action
/* 3104:     */   public void save()
/* 3105:     */   {
/* 3106:3101 */     if (!this.bridge.getUndoManager().isStored())
/* 3107:     */     {
/* 3108:3102 */       int okCancel = this.fileChooser.showSaveDialog(this.mainPanel);
/* 3109:3103 */       if (okCancel != 0) {
/* 3110:3104 */         return;
/* 3111:     */       }
/* 3112:3106 */       appendDefaultSuffix(this.fileChooser);
/* 3113:3107 */       if (!overwriteOk(this.fileChooser.getSelectedFile(), getResourceMap().getString("saveDialog.title", new Object[0]))) {
/* 3114:3108 */         return;
/* 3115:     */       }
/* 3116:3110 */       WPBDApp.saveToLocalStorage(this.fileChooser.getCurrentDirectory().getPath(), "fileChooserPath.xml");
/* 3117:     */     }
/* 3118:3112 */     downloadBridgeFromDraftingPanel();
/* 3119:     */     try
/* 3120:     */     {
/* 3121:3114 */       this.bridge.write(this.fileChooser.getSelectedFile());
/* 3122:3115 */       setTitleFileName();
/* 3123:     */     }
/* 3124:     */     catch (IOException e)
/* 3125:     */     {
/* 3126:3117 */       showMessageDialog(getResourceMap().getString("saveDialog.error", new Object[0]) + e.getMessage());
/* 3127:     */     }
/* 3128:     */   }
/* 3129:     */   
/* 3130:     */   @org.jdesktop.application.Action
/* 3131:     */   public void saveas()
/* 3132:     */   {
/* 3133:3123 */     int ok = this.fileChooser.showDialog(this.mainPanel, getResourceMap().getString("saveAsDialog.title", new Object[0]));
/* 3134:3124 */     if (ok != 0) {
/* 3135:3125 */       return;
/* 3136:     */     }
/* 3137:3127 */     appendDefaultSuffix(this.fileChooser);
/* 3138:3128 */     if (!overwriteOk(this.fileChooser.getSelectedFile(), getResourceMap().getString("saveAsDialog.title", new Object[0]))) {
/* 3139:3129 */       return;
/* 3140:     */     }
/* 3141:3131 */     WPBDApp.saveToLocalStorage(this.fileChooser.getCurrentDirectory().getPath(), "fileChooserPath.xml");
/* 3142:3132 */     downloadBridgeFromDraftingPanel();
/* 3143:     */     try
/* 3144:     */     {
/* 3145:3134 */       this.bridge.write(this.fileChooser.getSelectedFile());
/* 3146:3135 */       setTitleFileName();
/* 3147:     */     }
/* 3148:     */     catch (IOException e)
/* 3149:     */     {
/* 3150:3137 */       showMessageDialog(getResourceMap().getString("saveDialog.error", new Object[0]) + e.getMessage());
/* 3151:     */     }
/* 3152:     */   }
/* 3153:     */   
/* 3154:     */   @org.jdesktop.application.Action
/* 3155:     */   public void print()
/* 3156:     */   {
/* 3157:3143 */     print(true);
/* 3158:     */   }
/* 3159:     */   
/* 3160:     */   @org.jdesktop.application.Action
/* 3161:     */   public void showDrawingBoard()
/* 3162:     */   {
/* 3163:3148 */     selectCard("designPanel");
/* 3164:     */     
/* 3165:3150 */     setSelected(this.editSelectButton, true);
/* 3166:3151 */     editSelect();
/* 3167:     */   }
/* 3168:     */   
/* 3169:     */   @org.jdesktop.application.Action
/* 3170:     */   public void runLoadTest()
/* 3171:     */   {
/* 3172:3157 */     FixupCommand ruleEnforcer = new FixupCommand(this.bridge);
/* 3173:3158 */     int revisedMemberCount = ruleEnforcer.revisedMemberCount();
/* 3174:3159 */     if (revisedMemberCount > 0)
/* 3175:     */     {
/* 3176:3160 */       ruleEnforcer.execute(this.bridge.getUndoManager());
/* 3177:3161 */       showMessageDialog(revisedMemberCount == 1 ? getResourceMap().getString("autoCorrectMessageSingle.text", new Object[0]) : getResourceMap().getString("autoCorrectMessageMany.text", new Object[] { Integer.valueOf(revisedMemberCount) }));
/* 3178:     */     }
/* 3179:3166 */     this.bridge.analyze();
/* 3180:3168 */     if ((this.bridge.getAnalysis().getStatus() == 2) && (autofixEnabled()))
/* 3181:     */     {
/* 3182:3169 */       this.bridge.autofix();
/* 3183:3170 */       this.bridge.analyze();
/* 3184:     */     }
/* 3185:3173 */     this.enabledStateManager.setEnabled(this.loadTestReportButton, this.bridge.isAnalysisValid());
/* 3186:     */     
/* 3187:3175 */     setStatusIcon();
/* 3188:3176 */     if (this.bridge.getAnalysis().getStatus() == 2)
/* 3189:     */     {
/* 3190:3178 */       setSelected(this.drawingBoardButton, true);
/* 3191:3179 */       if (this.unstableModelDialog == null)
/* 3192:     */       {
/* 3193:3180 */         JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 3194:3181 */         this.unstableModelDialog = new UnstableModelDialog(mainFrame);
/* 3195:3182 */         this.unstableModelDialog.pack();
/* 3196:3183 */         this.unstableModelDialog.setLocationRelativeTo(mainFrame);
/* 3197:     */       }
/* 3198:3185 */       this.unstableModelDialog.setVisible(true);
/* 3199:     */     }
/* 3200:3186 */     else if (this.bridge.getAnalysis().getStatus() == 1)
/* 3201:     */     {
/* 3202:3188 */       setSelected(this.drawingBoardButton, true);
/* 3203:3189 */       if (this.slendernessTestFailDialog == null)
/* 3204:     */       {
/* 3205:3190 */         JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 3206:3191 */         this.slendernessTestFailDialog = new SlendernessTestFailDialog(mainFrame);
/* 3207:3192 */         this.slendernessTestFailDialog.pack();
/* 3208:3193 */         this.slendernessTestFailDialog.setLocationRelativeTo(mainFrame);
/* 3209:     */       }
/* 3210:3195 */       this.slendernessTestFailDialog.setVisible(true);
/* 3211:     */     }
/* 3212:3196 */     else if (animationEnabled())
/* 3213:     */     {
/* 3214:3198 */       selectCard(this.animationPanelCardName);
/* 3215:     */     }
/* 3216:     */     else
/* 3217:     */     {
/* 3218:3202 */       setSelected(this.drawingBoardButton, true);
/* 3219:     */     }
/* 3220:3204 */     if (!this.bridge.isPassing()) {
/* 3221:3206 */       this.memberTabs.setSelectedIndex(0);
/* 3222:     */     }
/* 3223:     */   }
/* 3224:     */   
/* 3225:     */   @org.jdesktop.application.Action
/* 3226:     */   public void openMemberTable()
/* 3227:     */   {
/* 3228:3212 */     selectMemberList(true);
/* 3229:     */   }
/* 3230:     */   
/* 3231:     */   @org.jdesktop.application.Action
/* 3232:     */   public void closeMemberTable()
/* 3233:     */   {
/* 3234:3217 */     selectMemberList(false);
/* 3235:     */   }
/* 3236:     */   
/* 3237:     */   @org.jdesktop.application.Action
/* 3238:     */   public void toggleMemberList()
/* 3239:     */   {
/* 3240:3222 */     selectMemberList(isSelected(this.toggleMemberListButton));
/* 3241:     */   }
/* 3242:     */   
/* 3243:     */   @org.jdesktop.application.Action
/* 3244:     */   public void increaseMemberSize()
/* 3245:     */   {
/* 3246:3227 */     if (this.bridge.isSelectedMember()) {
/* 3247:3228 */       this.dispatcher.incrementMemberSize(1);
/* 3248:     */     } else {
/* 3249:3231 */       this.stockSelector.incrementSize(1);
/* 3250:     */     }
/* 3251:     */   }
/* 3252:     */   
/* 3253:     */   @org.jdesktop.application.Action
/* 3254:     */   public void decreaseMemberSize()
/* 3255:     */   {
/* 3256:3237 */     if (this.bridge.isSelectedMember()) {
/* 3257:3238 */       this.dispatcher.incrementMemberSize(-1);
/* 3258:     */     } else {
/* 3259:3241 */       this.stockSelector.incrementSize(-1);
/* 3260:     */     }
/* 3261:     */   }
/* 3262:     */   
/* 3263:     */   @org.jdesktop.application.Action
/* 3264:     */   public void newDesign()
/* 3265:     */   {
/* 3266:3247 */     if (querySaveIfDirty())
/* 3267:     */     {
/* 3268:3248 */       DesignConditions conditions = this.bridge.getDesignConditions();
/* 3269:3249 */       if ((conditions != null) && (conditions.isFromKeyCode()))
/* 3270:     */       {
/* 3271:3250 */         showMessageDialog(getResourceMap().getString("keyCodeRestart.text", new Object[] { conditions.getCodeString() }));
/* 3272:3251 */         restartWithCurrentConditions();
/* 3273:     */       }
/* 3274:     */       else
/* 3275:     */       {
/* 3276:3254 */         showSetupWizard();
/* 3277:     */       }
/* 3278:     */     }
/* 3279:     */   }
/* 3280:     */   
/* 3281:     */   @org.jdesktop.application.Action
/* 3282:     */   public void loadSampleDesign()
/* 3283:     */   {
/* 3284:3261 */     if (this.loadSampleDialog == null)
/* 3285:     */     {
/* 3286:3262 */       JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 3287:3263 */       this.loadSampleDialog = new LoadSampleDialog(mainFrame);
/* 3288:3264 */       this.loadSampleDialog.setLocationRelativeTo(mainFrame);
/* 3289:3265 */       this.loadSampleDialog.initialize();
/* 3290:     */     }
/* 3291:3267 */     this.loadSampleDialog.setVisible(true);
/* 3292:3268 */     if (this.loadSampleDialog.isOk())
/* 3293:     */     {
/* 3294:3269 */       this.animation.stop();
/* 3295:3270 */       recordRecentFileUse();
/* 3296:3271 */       this.loadSampleDialog.loadUsingSelectedSample(this.bridge);
/* 3297:3272 */       this.fileChooser.setSelectedFile(getDefaultFile());
/* 3298:3273 */       initializePostBridgeLoad();
/* 3299:     */     }
/* 3300:     */   }
/* 3301:     */   
/* 3302:     */   @org.jdesktop.application.Action
/* 3303:     */   public void loadTemplate()
/* 3304:     */   {
/* 3305:3279 */     if (this.loadTemplateDialog == null)
/* 3306:     */     {
/* 3307:3280 */       JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 3308:3281 */       this.loadTemplateDialog = new LoadTemplateDialog(mainFrame);
/* 3309:3282 */       this.loadTemplateDialog.setLocationRelativeTo(mainFrame);
/* 3310:     */     }
/* 3311:3284 */     this.loadTemplateDialog.initialize(this.bridge.getDesignConditions(), this.bridgeDraftingView.getBridgeSketchView().getModel());
/* 3312:3285 */     this.loadTemplateDialog.setVisible(true);
/* 3313:3286 */     if (this.loadTemplateDialog.isOk())
/* 3314:     */     {
/* 3315:3287 */       setSketchModel(this.loadTemplateDialog.getSketchModel());
/* 3316:3288 */       this.draftingPanel.paintBackingStore();
/* 3317:3289 */       this.draftingPanel.repaint();
/* 3318:     */     }
/* 3319:     */   }
/* 3320:     */   
/* 3321:     */   @org.jdesktop.application.Action
/* 3322:     */   public void editJoints()
/* 3323:     */   {
/* 3324:3295 */     this.draftingPanel.editJoints();
/* 3325:     */   }
/* 3326:     */   
/* 3327:     */   @org.jdesktop.application.Action
/* 3328:     */   public void editMembers()
/* 3329:     */   {
/* 3330:3300 */     this.draftingPanel.editMembers();
/* 3331:     */   }
/* 3332:     */   
/* 3333:     */   @org.jdesktop.application.Action
/* 3334:     */   public void editSelect()
/* 3335:     */   {
/* 3336:3305 */     this.draftingPanel.editSelect();
/* 3337:     */   }
/* 3338:     */   
/* 3339:     */   @org.jdesktop.application.Action
/* 3340:     */   public void editErase()
/* 3341:     */   {
/* 3342:3310 */     this.draftingPanel.editErase();
/* 3343:     */   }
/* 3344:     */   
/* 3345:     */   @org.jdesktop.application.Action
/* 3346:     */   public void setCoarseGrid()
/* 3347:     */   {
/* 3348:3315 */     setGrid(0);
/* 3349:     */   }
/* 3350:     */   
/* 3351:     */   @org.jdesktop.application.Action
/* 3352:     */   public void setMediumGrid()
/* 3353:     */   {
/* 3354:3320 */     setGrid(1);
/* 3355:     */   }
/* 3356:     */   
/* 3357:     */   @org.jdesktop.application.Action
/* 3358:     */   public void setFineGrid()
/* 3359:     */   {
/* 3360:3325 */     setGrid(2);
/* 3361:     */   }
/* 3362:     */   
/* 3363:     */   @org.jdesktop.application.Action
/* 3364:     */   public void toggleRulers()
/* 3365:     */   {
/* 3366:3330 */     selectRulers(isSelected(this.toggleRulerMenuItem));
/* 3367:     */   }
/* 3368:     */   
/* 3369:     */   @org.jdesktop.application.Action
/* 3370:     */   public void undo()
/* 3371:     */   {
/* 3372:3335 */     this.draftingPanel.eraseCrosshairs();
/* 3373:3336 */     this.bridge.getUndoManager().undo();
/* 3374:     */   }
/* 3375:     */   
/* 3376:     */   @org.jdesktop.application.Action
/* 3377:     */   public void redo()
/* 3378:     */   {
/* 3379:3341 */     this.draftingPanel.eraseCrosshairs();
/* 3380:3342 */     this.bridge.getUndoManager().redo();
/* 3381:     */   }
/* 3382:     */   
/* 3383:     */   @org.jdesktop.application.Action
/* 3384:     */   public void toggleTitleBlock()
/* 3385:     */   {
/* 3386:3347 */     this.titleBlockPanel.setVisible(isSelected(this.toggleTitleBlockMenuItem));
/* 3387:     */   }
/* 3388:     */   
/* 3389:     */   @org.jdesktop.application.Action
/* 3390:     */   public void toggleMemberNumbers()
/* 3391:     */   {
/* 3392:3352 */     boolean label = isSelected(this.toggleMemberNumbersMenuItem);
/* 3393:3353 */     this.draftingPanel.setLabel(label);
/* 3394:3354 */     this.memberTable.setLabel(label);
/* 3395:     */   }
/* 3396:     */   
/* 3397:     */   @org.jdesktop.application.Action
/* 3398:     */   public void selectAll()
/* 3399:     */   {
/* 3400:3359 */     this.bridge.selectAllMembers();
/* 3401:     */   }
/* 3402:     */   
/* 3403:     */   @org.jdesktop.application.Action
/* 3404:     */   public void delete()
/* 3405:     */   {
/* 3406:3364 */     this.bridge.deleteSelection();
/* 3407:     */   }
/* 3408:     */   
/* 3409:     */   @org.jdesktop.application.Action
/* 3410:     */   public void toggleTools()
/* 3411:     */   {
/* 3412:3369 */     this.toolsDialog.setVisible(isSelected(this.toggleToolsMenuItem));
/* 3413:     */   }
/* 3414:     */   
/* 3415:     */   @org.jdesktop.application.Action
/* 3416:     */   public void toggleGuides()
/* 3417:     */   {
/* 3418:3374 */     this.draftingPanel.setGuidesVisible(isSelected(this.toggleGuidesButton));
/* 3419:     */   }
/* 3420:     */   
/* 3421:     */   @org.jdesktop.application.Action
/* 3422:     */   public void saveAsTemplate()
/* 3423:     */   {
/* 3424:3379 */     downloadBridgeFromDraftingPanel();
/* 3425:3380 */     String id = JOptionPane.showInputDialog(getFrame(), "Enter unique id", "Save As Template", 3);
/* 3426:3381 */     if (id != null) {
/* 3427:     */       try
/* 3428:     */       {
/* 3429:3383 */         this.bridge.writeTemplate(id);
/* 3430:     */       }
/* 3431:     */       catch (IOException e)
/* 3432:     */       {
/* 3433:3385 */         showMessageDialog("Could not write template: " + e.getMessage());
/* 3434:     */       }
/* 3435:     */     }
/* 3436:     */   }
/* 3437:     */   
/* 3438:     */   @org.jdesktop.application.Action
/* 3439:     */   public void saveAsSample()
/* 3440:     */   {
/* 3441:3392 */     downloadBridgeFromDraftingPanel();
/* 3442:3393 */     String id = JOptionPane.showInputDialog(getFrame(), "Enter unique id", "Save As Template", 3);
/* 3443:3394 */     if (id != null) {
/* 3444:     */       try
/* 3445:     */       {
/* 3446:3396 */         this.bridge.writeSample(id);
/* 3447:     */       }
/* 3448:     */       catch (IOException e)
/* 3449:     */       {
/* 3450:3398 */         showMessageDialog("Could not write sample: " + e.getMessage());
/* 3451:     */       }
/* 3452:     */     }
/* 3453:     */   }
/* 3454:     */   
/* 3455:     */   @org.jdesktop.application.Action
/* 3456:     */   public void showLoadTestReport()
/* 3457:     */   {
/* 3458:3405 */     if (this.loadTestReport == null)
/* 3459:     */     {
/* 3460:3406 */       this.loadTestReport = new LoadTestReport(getFrame(), this.bridge);
/* 3461:3407 */       JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 3462:3408 */       this.loadTestReport.setLocationRelativeTo(mainFrame);
/* 3463:     */     }
/* 3464:3410 */     downloadBridgeFromDraftingPanel();
/* 3465:3411 */     this.loadTestReport.setVisible(true);
/* 3466:     */   }
/* 3467:     */   
/* 3468:     */   @org.jdesktop.application.Action
/* 3469:     */   public void showTipOfTheDay()
/* 3470:     */   {
/* 3471:3416 */     this.tipDialog.showTip(false, 0);
/* 3472:     */   }
/* 3473:     */   
/* 3474:     */   @org.jdesktop.application.Action
/* 3475:     */   public void howToDesignABridge()
/* 3476:     */   {
/* 3477:3421 */     Help.getBroker().setCurrentView("TOC");
/* 3478:3422 */     Help.getBroker().setCurrentID("hlp_how_to_design_a_bridge");
/* 3479:3423 */     Help.getBroker().setDisplayed(true);
/* 3480:     */   }
/* 3481:     */   
/* 3482:     */   @org.jdesktop.application.Action
/* 3483:     */   public void theBridgeDesignWindow()
/* 3484:     */   {
/* 3485:3428 */     Help.getBroker().setCurrentView("TOC");
/* 3486:3429 */     Help.getBroker().setCurrentID("hlp_bridge_design_window");
/* 3487:3430 */     Help.getBroker().setDisplayed(true);
/* 3488:     */   }
/* 3489:     */   
/* 3490:     */   @org.jdesktop.application.Action
/* 3491:     */   public void browseOurWebSite()
/* 3492:     */   {
/* 3493:     */     try
/* 3494:     */     {
/* 3495:3436 */       Browser.openUrl("http://bridgecontest.usma.edu");
/* 3496:     */     }
/* 3497:     */     catch (IOException ex)
/* 3498:     */     {
/* 3499:3438 */       showMessageDialog(getResourceMap().getString("browseOurWebSite.error", new Object[0]));
/* 3500:     */     }
/* 3501:     */   }
/* 3502:     */   
/* 3503:     */   @org.jdesktop.application.Action
/* 3504:     */   public void about()
/* 3505:     */   {
/* 3506:3444 */     if (this.aboutBox == null)
/* 3507:     */     {
/* 3508:3445 */       JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 3509:3446 */       this.aboutBox = new AboutBox(mainFrame);
/* 3510:3447 */       this.aboutBox.setLocationRelativeTo(mainFrame);
/* 3511:     */     }
/* 3512:3449 */     this.aboutBox.setVisible(true);
/* 3513:     */   }
/* 3514:     */   
/* 3515:     */   @org.jdesktop.application.Action
/* 3516:     */   public void showCostDialog()
/* 3517:     */   {
/* 3518:3454 */     if (this.costReport == null)
/* 3519:     */     {
/* 3520:3455 */       JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 3521:3456 */       this.costReport = new CostReport(mainFrame);
/* 3522:3457 */       this.costReport.setLocationRelativeTo(mainFrame);
/* 3523:     */     }
/* 3524:3459 */     downloadBridgeFromDraftingPanel();
/* 3525:3460 */     this.costReport.initialize(this.bridge.getCostsWithNotes());
/* 3526:3461 */     this.costReport.setVisible(true);
/* 3527:     */   }
/* 3528:     */   
/* 3529:     */   @org.jdesktop.application.Action
/* 3530:     */   public void back1iteration()
/* 3531:     */   {
/* 3532:3466 */     this.bridge.saveSnapshot();
/* 3533:3467 */     int nextIterationIndex = this.bridge.getNextIterationIndex(-1);
/* 3534:3468 */     if (nextIterationIndex >= 0)
/* 3535:     */     {
/* 3536:3469 */       this.bridge.loadIteration(nextIterationIndex);
/* 3537:3470 */       this.bridgeDraftingView.initialize(this.bridge.getDesignConditions());
/* 3538:3471 */       uploadBridgeToDraftingPanel();
/* 3539:     */     }
/* 3540:     */   }
/* 3541:     */   
/* 3542:     */   @org.jdesktop.application.Action
/* 3543:     */   public void forward1iteration()
/* 3544:     */   {
/* 3545:3477 */     int nextIterationIndex = this.bridge.getNextIterationIndex(1);
/* 3546:3478 */     if (nextIterationIndex >= 0)
/* 3547:     */     {
/* 3548:3479 */       this.bridge.loadIteration(nextIterationIndex);
/* 3549:3480 */       this.bridgeDraftingView.initialize(this.bridge.getDesignConditions());
/* 3550:3481 */       uploadBridgeToDraftingPanel();
/* 3551:     */     }
/* 3552:     */   }
/* 3553:     */   
/* 3554:     */   @org.jdesktop.application.Action
/* 3555:     */   public void gotoIteration()
/* 3556:     */   {
/* 3557:3487 */     if (this.designIterationDialog == null)
/* 3558:     */     {
/* 3559:3488 */       JFrame mainFrame = WPBDApp.getApplication().getMainFrame();
/* 3560:3489 */       this.designIterationDialog = new DesignIterationDialog(mainFrame, this.bridge);
/* 3561:3490 */       this.designIterationDialog.setLocationRelativeTo(mainFrame);
/* 3562:     */     }
/* 3563:3492 */     this.bridge.clearSelectedJoint(true);
/* 3564:3493 */     this.designIterationDialog.setVisible(true);
/* 3565:3494 */     if (this.designIterationDialog.isOk())
/* 3566:     */     {
/* 3567:3495 */       this.designIterationDialog.loadSelectedIteration();
/* 3568:3496 */       this.bridgeDraftingView.initialize(this.bridge.getDesignConditions());
/* 3569:3497 */       uploadBridgeToDraftingPanel();
/* 3570:     */     }
/* 3571:     */   }
/* 3572:     */   
/* 3573:     */   @org.jdesktop.application.Action
/* 3574:     */   public void toggleAnimationControls()
/* 3575:     */   {
/* 3576:3503 */     this.animation.getControls().getDialog().setVisible(isSelected(this.toggleAnimationControlsMenuItem));
/* 3577:     */   }
/* 3578:     */   
/* 3579:     */   @org.jdesktop.application.Action
/* 3580:     */   public void toggleTemplate()
/* 3581:     */   {
/* 3582:3508 */     this.draftingPanel.setTemplateVisible(isSelected(this.toggleTemplateMenuItem));
/* 3583:     */   }
/* 3584:     */   
/* 3585:     */   @org.jdesktop.application.Action
/* 3586:     */   public void toggleShowAnimation()
/* 3587:     */   {
/* 3588:3513 */     setLoadTestButtonEnabled();
/* 3589:     */   }
/* 3590:     */   
/* 3591:     */   private void setLegacyGraphics()
/* 3592:     */   {
/* 3593:3518 */     this.animationPanelCardName = "fixedEyeAnimationPanel";
/* 3594:3519 */     this.animation = this.fixedEyeAnimation;
/* 3595:3520 */     WPBDApp.saveToLocalStorage(Boolean.valueOf(true), "graphicsCapability.xml");
/* 3596:     */   }
/* 3597:     */   
/* 3598:     */   private void setStandardGraphics()
/* 3599:     */   {
/* 3600:3525 */     this.animationPanelCardName = "flyThruAnimationPanel";
/* 3601:3526 */     this.animation = this.flyThruAnimation;
/* 3602:3527 */     WPBDApp.saveToLocalStorage(Boolean.valueOf(false), "graphicsCapability.xml");
/* 3603:     */   }
/* 3604:     */   
/* 3605:     */   private boolean setDefaultGraphics()
/* 3606:     */   {
/* 3607:3538 */     Boolean useLegacyStorage = (Boolean)WPBDApp.loadFromLocalStorage("graphicsCapability.xml");
/* 3608:3539 */     boolean useLegacy = (WPBDApp.isLegacyGraphics()) || (useLegacyStorage == null) || (useLegacyStorage.booleanValue());
/* 3609:3540 */     if (useLegacy) {
/* 3610:3541 */       setLegacyGraphics();
/* 3611:     */     } else {
/* 3612:3544 */       setStandardGraphics();
/* 3613:     */     }
/* 3614:3546 */     return useLegacy;
/* 3615:     */   }
/* 3616:     */   
/* 3617:     */   @org.jdesktop.application.Action
/* 3618:     */   public void toggleLegacyGraphics()
/* 3619:     */   {
/* 3620:3551 */     boolean selected = isSelected(this.toggleLegacyGraphicsMenuItem);
/* 3621:3552 */     if (selected) {
/* 3622:3553 */       setLegacyGraphics();
/* 3623:     */     } else {
/* 3624:3556 */       setStandardGraphics();
/* 3625:     */     }
/* 3626:     */   }
/* 3627:     */   
/* 3628:     */   @org.jdesktop.application.Action
/* 3629:     */   public void toggleAutoCorrect()
/* 3630:     */   {
/* 3631:3562 */     setLoadTestButtonEnabled();
/* 3632:     */   }
/* 3633:     */   
/* 3634:     */   @org.jdesktop.application.Action
/* 3635:     */   public void printToDefaultPrinter()
/* 3636:     */   {
/* 3637:3567 */     print(false);
/* 3638:     */   }
/* 3639:     */   
/* 3640:     */   @org.jdesktop.application.Action
/* 3641:     */   public void whatsNew()
/* 3642:     */   {
/* 3643:3572 */     Help.getBroker().setCurrentID("hlp_whats_new_2ed");
/* 3644:3573 */     Help.getBroker().setDisplayed(true);
/* 3645:     */   }
/* 3646:     */   
/* 3647:     */   @org.jdesktop.application.Action
/* 3648:     */   public void printClasses()
/* 3649:     */   {
/* 3650:3578 */     ClassLister.printPreloader(null);
/* 3651:     */   }
/* 3652:     */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.WPBDView
 * JD-Core Version:    0.7.0.1
 */