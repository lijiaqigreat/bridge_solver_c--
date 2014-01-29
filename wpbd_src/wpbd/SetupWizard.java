/*    1:     */ package wpbd;
/*    2:     */ 
/*    3:     */ import java.awt.CardLayout;
/*    4:     */ import java.awt.Color;
/*    5:     */ import java.awt.Component;
/*    6:     */ import java.awt.Container;
/*    7:     */ import java.awt.Dimension;
/*    8:     */ import java.awt.Font;
/*    9:     */ import java.awt.Frame;
/*   10:     */ import java.awt.Insets;
/*   11:     */ import java.awt.Toolkit;
/*   12:     */ import java.awt.event.ActionEvent;
/*   13:     */ import java.awt.event.ActionListener;
/*   14:     */ import java.awt.event.ItemEvent;
/*   15:     */ import java.awt.event.ItemListener;
/*   16:     */ import java.text.NumberFormat;
/*   17:     */ import java.util.Locale;
/*   18:     */ import java.util.regex.Matcher;
/*   19:     */ import java.util.regex.Pattern;
/*   20:     */ import javax.help.HelpBroker;
/*   21:     */ import javax.swing.AbstractListModel;
/*   22:     */ import javax.swing.BorderFactory;
/*   23:     */ import javax.swing.BoxLayout;
/*   24:     */ import javax.swing.ButtonGroup;
/*   25:     */ import javax.swing.GroupLayout;
/*   26:     */ import javax.swing.GroupLayout.Alignment;
/*   27:     */ import javax.swing.GroupLayout.ParallelGroup;
/*   28:     */ import javax.swing.GroupLayout.SequentialGroup;
/*   29:     */ import javax.swing.Icon;
/*   30:     */ import javax.swing.JButton;
/*   31:     */ import javax.swing.JComboBox;
/*   32:     */ import javax.swing.JDialog;
/*   33:     */ import javax.swing.JLabel;
/*   34:     */ import javax.swing.JList;
/*   35:     */ import javax.swing.JPanel;
/*   36:     */ import javax.swing.JRadioButton;
/*   37:     */ import javax.swing.JRootPane;
/*   38:     */ import javax.swing.JTable;
/*   39:     */ import javax.swing.JTextField;
/*   40:     */ import javax.swing.JTextPane;
/*   41:     */ import javax.swing.LayoutStyle.ComponentPlacement;
/*   42:     */ import javax.swing.border.SoftBevelBorder;
/*   43:     */ import javax.swing.event.ChangeEvent;
/*   44:     */ import javax.swing.event.ChangeListener;
/*   45:     */ import javax.swing.event.ListSelectionEvent;
/*   46:     */ import javax.swing.event.ListSelectionListener;
/*   47:     */ import javax.swing.table.JTableHeader;
/*   48:     */ import javax.swing.table.TableColumn;
/*   49:     */ import javax.swing.table.TableColumnModel;
/*   50:     */ import javax.swing.text.AbstractDocument;
/*   51:     */ import javax.swing.text.AttributeSet;
/*   52:     */ import javax.swing.text.BadLocationException;
/*   53:     */ import javax.swing.text.Document;
/*   54:     */ import javax.swing.text.DocumentFilter;
/*   55:     */ import javax.swing.text.DocumentFilter.FilterBypass;
/*   56:     */ import org.jdesktop.application.Application;
/*   57:     */ import org.jdesktop.application.ApplicationContext;
/*   58:     */ import org.jdesktop.application.ResourceMap;
/*   59:     */ 
/*   60:     */ public class SetupWizard
/*   61:     */   extends JDialog
/*   62:     */ {
/*   63:  40 */   private final Page[] pages = new Page[8];
/*   64:     */   private int currentPage;
/*   65:  42 */   private DesignConditions conditions = DesignConditions.conditions[0];
/*   66:  43 */   private final BridgeCartoonView bridgeCartoonView = new BridgeCartoonView();
/*   67:  44 */   private int updateDepth = 0;
/*   68:  45 */   private int pagesLoadedBits = 0;
/*   69:  46 */   private boolean ok = false;
/*   70:     */   private Icon deckCartoonIcon;
/*   71:     */   private final Icon noDeckNoLoad;
/*   72:     */   private final Icon medDeckStdLoad;
/*   73:     */   private final Icon hghDeckStdLoad;
/*   74:     */   private final Icon medDeckPmtLoad;
/*   75:     */   private final Icon hghDeckPmtLoad;
/*   76:     */   private static final int LCC_ERROR = -1;
/*   77:     */   private static final int LCC_PREFIX = 1;
/*   78:     */   private static final int LCC_COMPLETE = 0;
/*   79:     */   
/*   80:     */   public SetupWizard(Frame parent)
/*   81:     */   {
/*   82:  60 */     super(parent, true);
/*   83:     */     
/*   84:     */ 
/*   85:  63 */     this.bridgeCartoonView.initialize(DesignConditions.conditions[0]);
/*   86:     */     
/*   87:  65 */     initComponents();
/*   88:  66 */     this.localContest4charOKMsgLabel.setVisible(false);
/*   89:  67 */     getRootPane().setDefaultButton(this.finish);
/*   90:  68 */     Help.getBroker().enableHelpOnButton(this.help, "hlp_design_specifications", Help.getSet());
/*   91:     */     
/*   92:     */ 
/*   93:  71 */     WPBDApp app = WPBDApp.getApplication();
/*   94:  72 */     this.noDeckNoLoad = app.getIconResource("nodecknoload.png");
/*   95:  73 */     this.medDeckStdLoad = app.getIconResource("meddeckstdload.png");
/*   96:  74 */     this.hghDeckStdLoad = app.getIconResource("hghdeckstdload.png");
/*   97:  75 */     this.medDeckPmtLoad = app.getIconResource("meddeckpmtload.png");
/*   98:  76 */     this.hghDeckPmtLoad = app.getIconResource("hghdeckpmtload.png");
/*   99:     */     
/*  100:  78 */     this.pages[1] = new Page1(null);
/*  101:  79 */     this.pages[2] = new Page2(null);
/*  102:  80 */     this.pages[3] = new Page3(null);
/*  103:  81 */     this.pages[4] = new Page4(null);
/*  104:  82 */     this.pages[5] = new Page5(null);
/*  105:  83 */     this.pages[6] = new Page6(null);
/*  106:  84 */     this.pages[7] = new Page7(null);
/*  107:     */     
/*  108:  86 */     this.pages[1].load();
/*  109:  87 */     showDetailPane(false);
/*  110:     */   }
/*  111:     */   
/*  112:     */   public BridgeSketchModel getSketchModel()
/*  113:     */   {
/*  114:  96 */     return this.bridgeCartoonView.getBridgeSketchView().getModel();
/*  115:     */   }
/*  116:     */   
/*  117:     */   private class LocalContestCodeFilter
/*  118:     */     extends DocumentFilter
/*  119:     */   {
/*  120:     */     private LocalContestCodeFilter() {}
/*  121:     */     
/*  122:     */     public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
/*  123:     */       throws BadLocationException
/*  124:     */     {
/*  125: 103 */       string = string.toUpperCase(Locale.ENGLISH);
/*  126: 104 */       Document doc = fb.getDocument();
/*  127:     */       
/*  128: 106 */       String s = doc.getText(0, offset) + string + doc.getText(offset, doc.getLength() - offset);
/*  129:     */       
/*  130: 108 */       int lccScan = SetupWizard.this.localContestCodeScan(s);
/*  131: 110 */       if (lccScan != -1) {
/*  132: 111 */         super.insertString(fb, offset, string, attr);
/*  133:     */       }
/*  134: 114 */       handleScan(lccScan, s);
/*  135:     */     }
/*  136:     */     
/*  137:     */     public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
/*  138:     */       throws BadLocationException
/*  139:     */     {
/*  140: 119 */       Document doc = fb.getDocument();
/*  141:     */       
/*  142: 121 */       String s = doc.getText(0, offset) + doc.getText(offset + length, doc.getLength() - offset - length);
/*  143:     */       
/*  144: 123 */       int lccScan = SetupWizard.this.localContestCodeScan(s);
/*  145: 125 */       if (lccScan != -1) {
/*  146: 126 */         super.remove(fb, offset, length);
/*  147:     */       }
/*  148: 129 */       handleScan(lccScan, s);
/*  149:     */     }
/*  150:     */     
/*  151:     */     public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
/*  152:     */       throws BadLocationException
/*  153:     */     {
/*  154: 134 */       text = text.toUpperCase(Locale.ENGLISH);
/*  155: 135 */       Document doc = fb.getDocument();
/*  156:     */       
/*  157: 137 */       String s = doc.getText(0, offset) + text + doc.getText(offset, doc.getLength() - offset);
/*  158:     */       
/*  159: 139 */       int lccScan = SetupWizard.this.localContestCodeScan(s);
/*  160: 141 */       if (lccScan != -1) {
/*  161: 142 */         super.replace(fb, offset, length, text, attrs);
/*  162:     */       }
/*  163: 145 */       handleScan(lccScan, s);
/*  164:     */     }
/*  165:     */     
/*  166: 147 */     private ResourceMap resourceMap = WPBDApp.getResourceMap(SetupWizard.class);
/*  167:     */     
/*  168:     */     private void handleScan(int lccScan, String s)
/*  169:     */     {
/*  170: 158 */       switch (lccScan)
/*  171:     */       {
/*  172:     */       case -1: 
/*  173: 160 */         Toolkit.getDefaultToolkit().beep();
/*  174: 161 */         SetupWizard.this.localContestMsgLabel.setText(this.resourceMap.getString("localContestCodeError.text", new Object[0]));
/*  175: 162 */         SetupWizard.this.next.setEnabled(false);
/*  176: 163 */         break;
/*  177:     */       case 1: 
/*  178: 165 */         SetupWizard.this.localContestMsgLabel.setText("");
/*  179: 166 */         SetupWizard.this.next.setEnabled(false);
/*  180: 167 */         break;
/*  181:     */       case 0: 
/*  182: 169 */         SetupWizard.this.initialize(DesignConditions.getDesignConditions(s.substring(3)));
/*  183: 170 */         SetupWizard.this.localContestMsgLabel.setText(this.resourceMap.getString("localContestCodeComplete.text", new Object[0]));
/*  184: 171 */         SetupWizard.this.next.setEnabled(true);
/*  185: 172 */         SetupWizard.this.setSiteCostEnable(true);
/*  186:     */       }
/*  187:     */     }
/*  188:     */   }
/*  189:     */   
/*  190:     */   public DesignConditions getDesignConditions()
/*  191:     */   {
/*  192: 184 */     return this.conditions;
/*  193:     */   }
/*  194:     */   
/*  195:     */   public boolean isOk()
/*  196:     */   {
/*  197: 193 */     return this.ok;
/*  198:     */   }
/*  199:     */   
/*  200:     */   public String getProjectId()
/*  201:     */   {
/*  202: 202 */     return this.projectIdPrefixLabel.getText() + this.projectIdEdit.getText();
/*  203:     */   }
/*  204:     */   
/*  205:     */   public String getDesignedBy()
/*  206:     */   {
/*  207: 211 */     return this.designedByEdit.getText();
/*  208:     */   }
/*  209:     */   
/*  210:     */   public void setVisible(boolean b)
/*  211:     */   {
/*  212: 221 */     if (b)
/*  213:     */     {
/*  214: 222 */       this.ok = false;
/*  215: 223 */       this.pages[1].load();
/*  216:     */     }
/*  217: 226 */     super.setVisible(b);
/*  218:     */   }
/*  219:     */   
/*  220: 232 */   private final Pattern localContestPattern = Pattern.compile("\\A[0-9A-Z]{3}\\d\\d[A-D]\\Z");
/*  221:     */   
/*  222:     */   private int localContestCodeScan(String s)
/*  223:     */   {
/*  224: 235 */     if (this.localContestPattern.matcher(s).matches()) {
/*  225: 236 */       return DesignConditions.getDesignConditions(s.substring(3)) == null ? -1 : 0;
/*  226:     */     }
/*  227: 238 */     if (this.localContestPattern.matcher(s + "00001A".substring(s.length())).matches()) {
/*  228: 239 */       return (s.length() < 3) || (DesignConditions.isTagPrefix(s.substring(3))) ? 1 : -1;
/*  229:     */     }
/*  230: 241 */     return -1;
/*  231:     */   }
/*  232:     */   
/*  233:     */   public void initialize(DesignConditions conditions, String projectId, String designedBy, BridgeSketchModel model)
/*  234:     */   {
/*  235: 253 */     if (projectId == null)
/*  236:     */     {
/*  237: 254 */       this.projectIdPrefixLabel.setText("000" + conditions.getTag() + "-");
/*  238: 255 */       this.projectIdEdit.setText("");
/*  239:     */     }
/*  240:     */     else
/*  241:     */     {
/*  242: 257 */       String[] idParts = projectId.split("-");
/*  243: 258 */       if (idParts.length > 0)
/*  244:     */       {
/*  245: 259 */         this.projectIdPrefixLabel.setText(idParts[0]);
/*  246: 260 */         if (idParts.length > 1) {
/*  247: 261 */           this.projectIdEdit.setText(idParts[1]);
/*  248:     */         }
/*  249:     */       }
/*  250:     */     }
/*  251: 265 */     this.designedByEdit.setText(designedBy == null ? "" : designedBy);
/*  252: 266 */     this.bridgeCartoonView.getBridgeSketchView().setModel(model);
/*  253: 267 */     initialize(conditions);
/*  254:     */   }
/*  255:     */   
/*  256:     */   public void initialize(DesignConditions conditions)
/*  257:     */   {
/*  258: 272 */     if ((conditions == null) || (DesignConditions.getDesignConditions(conditions.getCodeLong()) == null)) {
/*  259: 273 */       conditions = DesignConditions.conditions[0];
/*  260:     */     }
/*  261: 276 */     this.conditions = conditions;
/*  262: 277 */     this.deckElevationBox.setSelectedIndex((24 - (int)conditions.getDeckElevation()) / 4);
/*  263: 278 */     if (conditions.isArch())
/*  264:     */     {
/*  265: 279 */       this.archAbutmentsButton.setSelected(true);
/*  266: 280 */       ((ExtendedComboBox)this.archHeightBox).setRawSelectedIndex((24 - (int)conditions.getArchHeight()) / 4);
/*  267:     */     }
/*  268:     */     else
/*  269:     */     {
/*  270: 282 */       this.standardAbutmentsButton.setSelected(true);
/*  271:     */     }
/*  272: 284 */     if (conditions.isPier())
/*  273:     */     {
/*  274: 285 */       this.pierButton.setSelected(true);
/*  275: 286 */       ((ExtendedComboBox)this.pierHeightBox).setRawSelectedIndex((24 - (int)conditions.getPierHeight()) / 4);
/*  276:     */     }
/*  277:     */     else
/*  278:     */     {
/*  279: 288 */       this.noPierButton.setSelected(true);
/*  280:     */     }
/*  281: 290 */     switch (conditions.getNAnchorages())
/*  282:     */     {
/*  283:     */     case 0: 
/*  284:     */     default: 
/*  285: 293 */       this.noAnchorageButton.setSelected(true);
/*  286: 294 */       break;
/*  287:     */     case 1: 
/*  288: 296 */       this.oneAnchorageButton.setSelected(true);
/*  289: 297 */       break;
/*  290:     */     case 2: 
/*  291: 299 */       this.twoAnchoragesButton.setSelected(true);
/*  292:     */     }
/*  293: 302 */     if (conditions.getDeckType() == 1) {
/*  294: 303 */       this.highConcreteButton.setSelected(true);
/*  295:     */     } else {
/*  296: 305 */       this.mediumConcreteButton.setSelected(true);
/*  297:     */     }
/*  298: 307 */     if (conditions.getLoadType() == 1) {
/*  299: 308 */       this.permitLoadButton.setSelected(true);
/*  300:     */     } else {
/*  301: 310 */       this.standardTruckButton.setSelected(true);
/*  302:     */     }
/*  303: 313 */     updateDependencies();
/*  304:     */   }
/*  305:     */   
/*  306:     */   private void beginUpdate()
/*  307:     */   {
/*  308: 317 */     this.updateDepth += 1;
/*  309:     */   }
/*  310:     */   
/*  311:     */   private void endUpdate()
/*  312:     */   {
/*  313: 321 */     this.updateDepth -= 1;
/*  314: 322 */     update();
/*  315:     */   }
/*  316:     */   
/*  317:     */   private void update()
/*  318:     */   {
/*  319: 326 */     if (this.updateDepth == 0)
/*  320:     */     {
/*  321: 327 */       this.conditions = DesignConditions.getDesignConditions(24 - 4 * this.deckElevationBox.getSelectedIndex(), this.archAbutmentsButton.isSelected() ? 24 - 4 * ((ExtendedComboBox)this.archHeightBox).getRawSelectedIndex() : -1.0D, this.pierButton.isSelected() ? 24 - 4 * ((ExtendedComboBox)this.pierHeightBox).getRawSelectedIndex() : -1.0D, this.twoAnchoragesButton.isSelected() ? 2 : this.oneAnchorageButton.isSelected() ? 1 : this.noAnchorageButton.isSelected() ? 0 : 0, this.permitLoadButton.isSelected() ? 1 : 0, this.highConcreteButton.isSelected() ? 1 : 0);
/*  322:     */       
/*  323:     */ 
/*  324:     */ 
/*  325:     */ 
/*  326:     */ 
/*  327:     */ 
/*  328: 334 */       updateDependencies();
/*  329:     */     }
/*  330:     */   }
/*  331:     */   
/*  332: 339 */   private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
/*  333:     */   private ButtonGroup abutmentGroup;
/*  334:     */   private JPanel abutmentPanel;
/*  335:     */   private ButtonGroup anchorageGroup;
/*  336:     */   private JPanel anchoragePanel;
/*  337:     */   private JRadioButton archAbutmentsButton;
/*  338:     */   private JComboBox archHeightBox;
/*  339:     */   private JPanel archHeightButtonPanel;
/*  340:     */   private JButton archHeightDownButton;
/*  341:     */   private JLabel archHeightLabel;
/*  342:     */   private JButton archHeightUpButton;
/*  343:     */   private JButton back;
/*  344:     */   private JButton cancel;
/*  345:     */   private JPanel card1;
/*  346:     */   private JPanel card2;
/*  347:     */   private JPanel card3;
/*  348:     */   private JPanel card4;
/*  349:     */   private JPanel card5;
/*  350:     */   private JPanel card6;
/*  351:     */   private JPanel card7;
/*  352:     */   private JLabel costNoteLabel;
/*  353:     */   private JLabel deckCartoonLabel;
/*  354:     */   private JPanel deckCrossSectionPanel;
/*  355:     */   private JComboBox deckElevationBox;
/*  356:     */   private JPanel deckElevationButtonPanel;
/*  357:     */   private JButton deckElevationDownButton;
/*  358:     */   private JPanel deckElevationPanel;
/*  359:     */   private JButton deckElevationUpButton;
/*  360:     */   private ButtonGroup deckMaterialGroup;
/*  361:     */   private JPanel deckMaterialPanel;
/*  362:     */   private JPanel designPanel;
/*  363:     */   private JTextField designedByEdit;
/*  364:     */   private JLabel designedByLabel;
/*  365:     */   private JButton dropRaiseButton;
/*  366:     */   private JLabel elevationViewLabel;
/*  367:     */   private JPanel elevationViewPanel;
/*  368:     */   private JButton finish;
/*  369:     */   private JButton help;
/*  370:     */   private JRadioButton highConcreteButton;
/*  371:     */   private JTextPane instructionsPane;
/*  372:     */   private JLabel legendAbutmentLabel;
/*  373:     */   private JLabel legendBottomSpacerLabel;
/*  374:     */   private JLabel legendDeckLabel;
/*  375:     */   private JLabel legendExcavationLabel;
/*  376:     */   private JPanel legendPanel;
/*  377:     */   private JLabel legendPierLabel;
/*  378:     */   private JLabel legendRiverBankLabel;
/*  379:     */   private JLabel legendRiverLabel;
/*  380:     */   private JLabel legendTopSpacerLabel;
/*  381:     */   private ButtonGroup loadingGroup;
/*  382:     */   private JPanel loadingPanel;
/*  383:     */   private JRadioButton localContest4YesButton;
/*  384:     */   private JLabel localContest4charOKMsgLabel;
/*  385:     */   private JRadioButton localContest6YesButton;
/*  386:     */   private JTextField localContestCodeField;
/*  387:     */   private JTextPane localContestCodeLabel;
/*  388:     */   private JPanel localContestCodePanel;
/*  389:     */   private ButtonGroup localContestGroup;
/*  390:     */   private JLabel localContestLabel;
/*  391:     */   private JLabel localContestMsgLabel;
/*  392:     */   private JRadioButton localContestNoButton;
/*  393:     */   private JRadioButton mediumConcreteButton;
/*  394:     */   private JButton next;
/*  395:     */   private JRadioButton noAnchorageButton;
/*  396:     */   private JRadioButton noPierButton;
/*  397:     */   private JRadioButton oneAnchorageButton;
/*  398:     */   private JLabel pageNumber;
/*  399:     */   private JLabel pageTitle;
/*  400:     */   private JRadioButton permitLoadButton;
/*  401:     */   private JRadioButton pierButton;
/*  402:     */   private ButtonGroup pierGroup;
/*  403:     */   private JComboBox pierHeightBox;
/*  404:     */   private JPanel pierHeightButtonPanel;
/*  405:     */   private JButton pierHeightDownButton;
/*  406:     */   private JLabel pierHeightLabel;
/*  407:     */   private JButton pierHeightUpButton;
/*  408:     */   private JPanel pierPanel;
/*  409:     */   private JTextField projectIdEdit;
/*  410:     */   private JLabel projectIdLabel;
/*  411:     */   private JLabel projectIdPrefixLabel;
/*  412:     */   private JTextField projectNameEdit;
/*  413:     */   private JLabel projectNameLabel;
/*  414:     */   private JTextPane requirementPane;
/*  415:     */   private JPanel requirementPanel;
/*  416:     */   private JPanel selectTemplatePanel;
/*  417:     */   private JLabel siteConditionsLabel;
/*  418:     */   private JTable siteCostDetailTable;
/*  419:     */   private JLabel siteCostLabel;
/*  420:     */   private JPanel siteCostPanel;
/*  421:     */   private JRadioButton standardAbutmentsButton;
/*  422:     */   private JRadioButton standardTruckButton;
/*  423:     */   private JPanel supportConfigPanel;
/*  424:     */   private JList templateList;
/*  425:     */   private JTextPane tipPane;
/*  426:     */   private JPanel tipPanel;
/*  427:     */   private JPanel titleBlockInfoPanel;
/*  428:     */   private JRadioButton twoAnchoragesButton;
/*  429:     */   private JPanel widgetPanel;
/*  430:     */   
/*  431:     */   private void updateDependencies()
/*  432:     */   {
/*  433: 342 */     String val = this.projectIdPrefixLabel.getText();
/*  434: 343 */     if (val.length() != 7) {
/*  435: 344 */       val = "00001A-";
/*  436:     */     }
/*  437: 346 */     this.projectIdPrefixLabel.setText(val.substring(0, 3) + this.conditions.getTag() + val.substring(6));
/*  438:     */     
/*  439: 348 */     ((SiteCostTableModel)this.siteCostDetailTable.getModel()).initialize(this.conditions);
/*  440:     */     
/*  441: 350 */     this.siteCostLabel.setText(this.currencyFormat.format(this.conditions.getTotalFixedCost()));
/*  442: 353 */     if (isSiteCostEnabled())
/*  443:     */     {
/*  444: 354 */       ResourceMap resourceMap = WPBDApp.getResourceMap(SetupWizard.class);
/*  445: 355 */       this.siteConditionsLabel.setText(resourceMap.getString("siteCondition.text", new Object[] { this.conditions.getTag() }));
/*  446:     */     }
/*  447: 358 */     switch (this.conditions.getDeckType())
/*  448:     */     {
/*  449:     */     case 0: 
/*  450: 360 */       switch (this.conditions.getLoadType())
/*  451:     */       {
/*  452:     */       case 0: 
/*  453: 362 */         this.deckCartoonIcon = this.medDeckStdLoad;
/*  454: 363 */         break;
/*  455:     */       case 1: 
/*  456: 365 */         this.deckCartoonIcon = this.medDeckPmtLoad;
/*  457:     */       }
/*  458: 368 */       break;
/*  459:     */     case 1: 
/*  460: 370 */       switch (this.conditions.getLoadType())
/*  461:     */       {
/*  462:     */       case 0: 
/*  463: 372 */         this.deckCartoonIcon = this.hghDeckStdLoad;
/*  464: 373 */         break;
/*  465:     */       case 1: 
/*  466: 375 */         this.deckCartoonIcon = this.hghDeckPmtLoad;
/*  467:     */       }
/*  468:     */       break;
/*  469:     */     }
/*  470: 380 */     this.bridgeCartoonView.initialize(this.conditions);
/*  471: 381 */     this.pages[this.currentPage].showDeckCartoon();
/*  472: 382 */     this.pages[this.currentPage].showElevationCartoon();
/*  473:     */   }
/*  474:     */   
/*  475:     */   private boolean isSiteCostEnabled()
/*  476:     */   {
/*  477: 386 */     return this.dropRaiseButton.isEnabled();
/*  478:     */   }
/*  479:     */   
/*  480:     */   private void setSiteCostEnable(boolean val)
/*  481:     */   {
/*  482: 390 */     this.dropRaiseButton.setEnabled(val);
/*  483: 391 */     this.costNoteLabel.setEnabled(val);
/*  484: 392 */     this.siteCostLabel.setEnabled(val);
/*  485: 393 */     if (val)
/*  486:     */     {
/*  487: 394 */       update();
/*  488:     */     }
/*  489:     */     else
/*  490:     */     {
/*  491: 396 */       showDetailPane(false);
/*  492: 397 */       this.siteCostLabel.setText("$0.0");
/*  493: 398 */       this.siteConditionsLabel.setText("");
/*  494:     */     }
/*  495:     */   }
/*  496:     */   
/*  497:     */   private void showDetailPane(boolean visible)
/*  498:     */   {
/*  499: 403 */     if (visible != this.siteCostDetailTable.isVisible())
/*  500:     */     {
/*  501: 404 */       this.siteCostDetailTable.setVisible(visible);
/*  502: 405 */       this.dropRaiseButton.setIcon(WPBDApp.getApplication().getIconResource(visible ? "undrop.png" : "drop.png"));
/*  503: 406 */       pack();
/*  504:     */     }
/*  505:     */   }
/*  506:     */   
/*  507:     */   private boolean hasPageBeenLoaded(int n)
/*  508:     */   {
/*  509: 411 */     return (this.pagesLoadedBits & 1 << n) != 0;
/*  510:     */   }
/*  511:     */   
/*  512:     */   private abstract class Page
/*  513:     */   {
/*  514:     */     private Page() {}
/*  515:     */     
/*  516:     */     public void setDefaults()
/*  517:     */     {
/*  518: 418 */       SetupWizard.this.currentPage = getPageNumber();
/*  519: 419 */       SetupWizard.access$1276(SetupWizard.this, 1 << SetupWizard.this.currentPage);
/*  520:     */       
/*  521: 421 */       ResourceMap resourceMap = WPBDApp.getResourceMap(SetupWizard.class);
/*  522: 422 */       String pageNoString = Integer.toString(SetupWizard.this.currentPage);
/*  523:     */       
/*  524:     */ 
/*  525: 425 */       SetupWizard.this.pageNumber.setText(pageNoString);
/*  526: 426 */       SetupWizard.this.pageTitle.setText(resourceMap.getString("title" + pageNoString + ".text", new Object[0]));
/*  527:     */       
/*  528:     */ 
/*  529: 429 */       CardLayout cl = (CardLayout)SetupWizard.this.widgetPanel.getLayout();
/*  530: 430 */       cl.show(SetupWizard.this.widgetPanel, pageNoString);
/*  531:     */       
/*  532:     */ 
/*  533: 433 */       showDeckCartoon();
/*  534: 434 */       showElevationCartoon();
/*  535: 435 */       updateLegend();
/*  536:     */       
/*  537:     */ 
/*  538: 438 */       boolean[] elementsEnabled = getElementsEnabled();
/*  539: 439 */       SetupWizard.this.back.setEnabled(elementsEnabled[0]);
/*  540: 440 */       SetupWizard.this.next.setEnabled(elementsEnabled[1]);
/*  541: 441 */       SetupWizard.this.finish.setEnabled(elementsEnabled[2]);
/*  542:     */       
/*  543:     */ 
/*  544: 444 */       loadTip(resourceMap, pageNoString);
/*  545:     */     }
/*  546:     */     
/*  547:     */     public void updateLegend()
/*  548:     */     {
/*  549: 449 */       boolean[] legendsVisible = getLegendsVisible();
/*  550: 450 */       SetupWizard.this.legendRiverBankLabel.setVisible(legendsVisible[0]);
/*  551: 451 */       SetupWizard.this.legendExcavationLabel.setVisible(legendsVisible[1]);
/*  552: 452 */       SetupWizard.this.legendRiverLabel.setVisible(legendsVisible[2]);
/*  553: 453 */       SetupWizard.this.legendDeckLabel.setVisible(legendsVisible[3]);
/*  554: 454 */       SetupWizard.this.legendAbutmentLabel.setVisible(legendsVisible[4]);
/*  555: 455 */       SetupWizard.this.legendPierLabel.setVisible((legendsVisible[5] != 0) && (SetupWizard.this.pierButton.isSelected()));
/*  556:     */     }
/*  557:     */     
/*  558:     */     public void load()
/*  559:     */     {
/*  560: 459 */       setDefaults();
/*  561: 460 */       SetupWizard.this.setSiteCostEnable(true);
/*  562:     */     }
/*  563:     */     
/*  564:     */     abstract int getPageNumber();
/*  565:     */     
/*  566:     */     protected int getNextPageNumber()
/*  567:     */     {
/*  568: 466 */       return getPageNumber() + 1;
/*  569:     */     }
/*  570:     */     
/*  571:     */     protected int getBackPageNumber()
/*  572:     */     {
/*  573: 470 */       return getPageNumber() - 1;
/*  574:     */     }
/*  575:     */     
/*  576:     */     protected void showDeckCartoon()
/*  577:     */     {
/*  578: 474 */       SetupWizard.this.deckCartoonLabel.setIcon((SetupWizard.this.hasPageBeenLoaded(4)) || (SetupWizard.this.localContestCodeScan(SetupWizard.this.localContestCodeField.getText()) == 0) ? SetupWizard.this.deckCartoonIcon : SetupWizard.this.noDeckNoLoad);
/*  579:     */       
/*  580:     */ 
/*  581: 477 */       SetupWizard.this.deckCartoonLabel.repaint();
/*  582:     */     }
/*  583:     */     
/*  584:     */     protected void showElevationCartoon()
/*  585:     */     {
/*  586: 481 */       if ((SetupWizard.this.hasPageBeenLoaded(3)) || (SetupWizard.this.localContestCodeScan(SetupWizard.this.localContestCodeField.getText()) == 0)) {
/*  587: 482 */         SetupWizard.this.bridgeCartoonView.setMode(2);
/*  588:     */       } else {
/*  589: 485 */         SetupWizard.this.bridgeCartoonView.setMode(0);
/*  590:     */       }
/*  591: 487 */       SetupWizard.this.elevationViewLabel.repaint();
/*  592:     */     }
/*  593:     */     
/*  594:     */     protected boolean[] getLegendsVisible()
/*  595:     */     {
/*  596: 491 */       return new boolean[] { true, true, true, true, true, true };
/*  597:     */     }
/*  598:     */     
/*  599:     */     protected boolean[] getElementsEnabled()
/*  600:     */     {
/*  601: 495 */       return new boolean[] { true, true, true };
/*  602:     */     }
/*  603:     */     
/*  604:     */     protected void loadTip(ResourceMap resourceMap, String pageNoString)
/*  605:     */     {
/*  606: 499 */       SetupWizard.this.tipPane.setText(resourceMap.getString("tipPane" + pageNoString + ".text", new Object[0]));
/*  607:     */     }
/*  608:     */   }
/*  609:     */   
/*  610:     */   private class Page1
/*  611:     */     extends SetupWizard.Page
/*  612:     */   {
/*  613:     */     private Page1()
/*  614:     */     {
/*  615: 503 */       super(null);
/*  616:     */     }
/*  617:     */     
/*  618:     */     int getPageNumber()
/*  619:     */     {
/*  620: 507 */       return 1;
/*  621:     */     }
/*  622:     */     
/*  623:     */     protected void showElevationCartoon()
/*  624:     */     {
/*  625: 512 */       SetupWizard.this.bridgeCartoonView.setMode(1);
/*  626: 513 */       SetupWizard.this.deckCartoonLabel.repaint();
/*  627:     */     }
/*  628:     */     
/*  629:     */     protected void showDeckCartoon()
/*  630:     */     {
/*  631: 518 */       SetupWizard.this.deckCartoonLabel.setIcon(SetupWizard.this.noDeckNoLoad);
/*  632:     */     }
/*  633:     */     
/*  634:     */     protected boolean[] getLegendsVisible()
/*  635:     */     {
/*  636: 523 */       return new boolean[] { true, false, true, false, false, false };
/*  637:     */     }
/*  638:     */     
/*  639:     */     protected boolean[] getElementsEnabled()
/*  640:     */     {
/*  641: 528 */       return new boolean[] { false, true, false };
/*  642:     */     }
/*  643:     */     
/*  644:     */     protected int getBackPageNumber()
/*  645:     */     {
/*  646: 533 */       return 1;
/*  647:     */     }
/*  648:     */     
/*  649:     */     public void load()
/*  650:     */     {
/*  651: 538 */       setDefaults();
/*  652: 539 */       SetupWizard.this.setSiteCostEnable(false);
/*  653:     */     }
/*  654:     */   }
/*  655:     */   
/*  656:     */   private class Page2
/*  657:     */     extends SetupWizard.Page
/*  658:     */   {
/*  659:     */     private Page2()
/*  660:     */     {
/*  661: 543 */       super(null);
/*  662:     */     }
/*  663:     */     
/*  664:     */     int getPageNumber()
/*  665:     */     {
/*  666: 547 */       return 2;
/*  667:     */     }
/*  668:     */     
/*  669:     */     protected boolean[] getElementsEnabled()
/*  670:     */     {
/*  671: 552 */       return new boolean[] { true, true, false };
/*  672:     */     }
/*  673:     */     
/*  674:     */     protected int getNextPageNumber()
/*  675:     */     {
/*  676: 557 */       return SetupWizard.this.localContestCodeScan(SetupWizard.this.localContestCodeField.getText()) == 0 ? 5 : 3;
/*  677:     */     }
/*  678:     */     
/*  679:     */     public void load()
/*  680:     */     {
/*  681: 562 */       setDefaults();
/*  682: 563 */       SetupWizard.this.setSiteCostEnable(SetupWizard.access$2500(SetupWizard.this, 3));
/*  683:     */     }
/*  684:     */   }
/*  685:     */   
/*  686:     */   private class Page3
/*  687:     */     extends SetupWizard.Page
/*  688:     */   {
/*  689:     */     private Page3()
/*  690:     */     {
/*  691: 567 */       super(null);
/*  692:     */     }
/*  693:     */     
/*  694:     */     int getPageNumber()
/*  695:     */     {
/*  696: 571 */       return 3;
/*  697:     */     }
/*  698:     */     
/*  699:     */     protected boolean[] getElementsEnabled()
/*  700:     */     {
/*  701: 576 */       return new boolean[] { true, true, false };
/*  702:     */     }
/*  703:     */   }
/*  704:     */   
/*  705:     */   private class Page4
/*  706:     */     extends SetupWizard.Page
/*  707:     */   {
/*  708:     */     private Page4()
/*  709:     */     {
/*  710: 580 */       super(null);
/*  711:     */     }
/*  712:     */     
/*  713:     */     int getPageNumber()
/*  714:     */     {
/*  715: 584 */       return 4;
/*  716:     */     }
/*  717:     */   }
/*  718:     */   
/*  719:     */   private class Page5
/*  720:     */     extends SetupWizard.Page
/*  721:     */   {
/*  722:     */     private Page5()
/*  723:     */     {
/*  724: 589 */       super(null);
/*  725:     */     }
/*  726:     */     
/*  727:     */     int getPageNumber()
/*  728:     */     {
/*  729: 593 */       return 5;
/*  730:     */     }
/*  731:     */     
/*  732:     */     protected int getBackPageNumber()
/*  733:     */     {
/*  734: 598 */       return SetupWizard.this.localContestCodeScan(SetupWizard.this.localContestCodeField.getText()) == 0 ? 2 : 4;
/*  735:     */     }
/*  736:     */     
/*  737:     */     public void load()
/*  738:     */     {
/*  739: 603 */       super.load();
/*  740: 604 */       BridgeSketchModel model = SetupWizard.this.bridgeCartoonView.getBridgeSketchView().getModel();
/*  741:     */       
/*  742: 606 */       SetupWizard.this.templateList.setListData(BridgeSketchModel.getList(SetupWizard.this.conditions));
/*  743: 607 */       if (model == null) {
/*  744: 608 */         SetupWizard.this.templateList.setSelectedIndex(0);
/*  745:     */       } else {
/*  746: 611 */         SetupWizard.this.templateList.setSelectedValue(model, true);
/*  747:     */       }
/*  748:     */     }
/*  749:     */   }
/*  750:     */   
/*  751:     */   private class Page6
/*  752:     */     extends SetupWizard.Page
/*  753:     */   {
/*  754:     */     private Page6()
/*  755:     */     {
/*  756: 616 */       super(null);
/*  757:     */     }
/*  758:     */     
/*  759:     */     int getPageNumber()
/*  760:     */     {
/*  761: 620 */       return 6;
/*  762:     */     }
/*  763:     */     
/*  764:     */     protected void showElevationCartoon()
/*  765:     */     {
/*  766: 625 */       SetupWizard.this.bridgeCartoonView.setMode(6);
/*  767: 626 */       SetupWizard.this.deckCartoonLabel.repaint();
/*  768:     */     }
/*  769:     */   }
/*  770:     */   
/*  771:     */   private class Page7
/*  772:     */     extends SetupWizard.Page
/*  773:     */   {
/*  774:     */     private Page7()
/*  775:     */     {
/*  776: 630 */       super(null);
/*  777:     */     }
/*  778:     */     
/*  779:     */     int getPageNumber()
/*  780:     */     {
/*  781: 634 */       return 7;
/*  782:     */     }
/*  783:     */     
/*  784:     */     protected boolean[] getElementsEnabled()
/*  785:     */     {
/*  786: 639 */       return new boolean[] { true, false, true };
/*  787:     */     }
/*  788:     */     
/*  789:     */     protected int getNextPageNumber()
/*  790:     */     {
/*  791: 644 */       return 7;
/*  792:     */     }
/*  793:     */     
/*  794:     */     protected void showElevationCartoon()
/*  795:     */     {
/*  796: 649 */       SetupWizard.this.bridgeCartoonView.setMode(14);
/*  797: 650 */       SetupWizard.this.deckCartoonLabel.repaint();
/*  798:     */     }
/*  799:     */   }
/*  800:     */   
/*  801:     */   private void initComponents()
/*  802:     */   {
/*  803: 658 */     this.localContestGroup = new ButtonGroup();
/*  804: 659 */     this.abutmentGroup = new ButtonGroup();
/*  805: 660 */     this.pierGroup = new ButtonGroup();
/*  806: 661 */     this.anchorageGroup = new ButtonGroup();
/*  807: 662 */     this.deckMaterialGroup = new ButtonGroup();
/*  808: 663 */     this.loadingGroup = new ButtonGroup();
/*  809: 664 */     this.pageNumber = new JLabel();
/*  810: 665 */     this.pageTitle = new JLabel();
/*  811: 666 */     this.widgetPanel = new JPanel();
/*  812: 667 */     this.card1 = new JPanel();
/*  813: 668 */     this.requirementPanel = new JPanel();
/*  814: 669 */     this.requirementPane = new TipTextPane();
/*  815: 670 */     this.card2 = new JPanel();
/*  816: 671 */     this.localContestCodePanel = new JPanel();
/*  817: 672 */     this.localContestLabel = new JLabel();
/*  818: 673 */     this.localContestNoButton = new JRadioButton();
/*  819: 674 */     this.localContest4YesButton = new JRadioButton();
/*  820: 675 */     this.localContest6YesButton = new JRadioButton();
/*  821: 676 */     this.localContestCodeLabel = new JTextPane();
/*  822: 677 */     this.localContestCodeField = new JTextField();
/*  823: 678 */     this.localContestMsgLabel = new JLabel();
/*  824: 679 */     this.localContest4charOKMsgLabel = new JLabel();
/*  825: 680 */     this.card3 = new JPanel();
/*  826: 681 */     this.deckElevationPanel = new JPanel();
/*  827: 682 */     this.deckElevationButtonPanel = new JPanel();
/*  828: 683 */     this.deckElevationUpButton = new JButton();
/*  829: 684 */     this.deckElevationDownButton = new JButton();
/*  830: 685 */     this.deckElevationBox = new ExtendedComboBox(this.deckElevationUpButton, this.deckElevationDownButton);
/*  831: 686 */     this.supportConfigPanel = new JPanel();
/*  832: 687 */     this.abutmentPanel = new JPanel();
/*  833: 688 */     this.standardAbutmentsButton = new JRadioButton();
/*  834: 689 */     this.archAbutmentsButton = new JRadioButton();
/*  835: 690 */     this.archHeightLabel = new JLabel();
/*  836: 691 */     this.archHeightButtonPanel = new JPanel();
/*  837: 692 */     this.archHeightUpButton = new JButton();
/*  838: 693 */     this.archHeightDownButton = new JButton();
/*  839: 694 */     this.archHeightBox = new ExtendedComboBox(this.archHeightUpButton, this.archHeightDownButton);
/*  840: 695 */     this.pierPanel = new JPanel();
/*  841: 696 */     this.noPierButton = new JRadioButton();
/*  842: 697 */     this.pierButton = new JRadioButton();
/*  843: 698 */     this.pierHeightLabel = new JLabel();
/*  844: 699 */     this.pierHeightButtonPanel = new JPanel();
/*  845: 700 */     this.pierHeightUpButton = new JButton();
/*  846: 701 */     this.pierHeightDownButton = new JButton();
/*  847: 702 */     this.pierHeightBox = new ExtendedComboBox(this.pierHeightUpButton, this.pierHeightDownButton);
/*  848: 703 */     this.anchoragePanel = new JPanel();
/*  849: 704 */     this.noAnchorageButton = new JRadioButton();
/*  850: 705 */     this.oneAnchorageButton = new JRadioButton();
/*  851: 706 */     this.twoAnchoragesButton = new JRadioButton();
/*  852: 707 */     this.card4 = new JPanel();
/*  853: 708 */     this.deckMaterialPanel = new JPanel();
/*  854: 709 */     this.mediumConcreteButton = new JRadioButton();
/*  855: 710 */     this.highConcreteButton = new JRadioButton();
/*  856: 711 */     this.loadingPanel = new JPanel();
/*  857: 712 */     this.standardTruckButton = new JRadioButton();
/*  858: 713 */     this.permitLoadButton = new JRadioButton();
/*  859: 714 */     this.card5 = new JPanel();
/*  860: 715 */     this.selectTemplatePanel = new JPanel();
/*  861: 716 */     this.templateList = new JList();
/*  862: 717 */     this.card6 = new JPanel();
/*  863: 718 */     this.titleBlockInfoPanel = new JPanel();
/*  864: 719 */     this.projectNameLabel = new JLabel();
/*  865: 720 */     this.projectNameEdit = new JTextField();
/*  866: 721 */     this.designedByLabel = new JLabel();
/*  867: 722 */     this.designedByEdit = new JTextField();
/*  868: 723 */     this.projectIdLabel = new JLabel();
/*  869: 724 */     this.projectIdEdit = new JTextField();
/*  870: 725 */     this.projectIdPrefixLabel = new JLabel();
/*  871: 726 */     this.card7 = new JPanel();
/*  872: 727 */     this.designPanel = new JPanel();
/*  873: 728 */     this.instructionsPane = new TipTextPane();
/*  874: 729 */     this.deckCrossSectionPanel = new JPanel();
/*  875: 730 */     this.deckCartoonLabel = new JLabel();
/*  876: 731 */     this.elevationViewPanel = new JPanel();
/*  877: 732 */     this.elevationViewLabel = this.bridgeCartoonView.getDrawing(1.0D);
/*  878: 733 */     this.legendPanel = new JPanel();
/*  879: 734 */     this.legendRiverBankLabel = new JLabel();
/*  880: 735 */     this.legendExcavationLabel = new JLabel();
/*  881: 736 */     this.legendRiverLabel = new JLabel();
/*  882: 737 */     this.legendTopSpacerLabel = new JLabel();
/*  883: 738 */     this.legendDeckLabel = new JLabel();
/*  884: 739 */     this.legendAbutmentLabel = new JLabel();
/*  885: 740 */     this.legendPierLabel = new JLabel();
/*  886: 741 */     this.legendBottomSpacerLabel = new JLabel();
/*  887: 742 */     this.tipPanel = new JPanel();
/*  888: 743 */     this.tipPane = new TipTextPane();
/*  889: 744 */     this.siteCostPanel = new JPanel();
/*  890: 745 */     this.siteCostDetailTable = new SiteCostTable();
/*  891: 746 */     this.siteCostLabel = new JLabel();
/*  892: 747 */     this.dropRaiseButton = new JButton();
/*  893: 748 */     this.costNoteLabel = new JLabel();
/*  894: 749 */     this.siteConditionsLabel = new JLabel();
/*  895: 750 */     this.help = new JButton();
/*  896: 751 */     this.cancel = new JButton();
/*  897: 752 */     this.back = new JButton();
/*  898: 753 */     this.next = new JButton();
/*  899: 754 */     this.finish = new JButton();
/*  900:     */     
/*  901: 756 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(SetupWizard.class);
/*  902: 757 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/*  903: 758 */     setName("Form");
/*  904: 759 */     setResizable(false);
/*  905:     */     
/*  906: 761 */     this.pageNumber.setBackground(resourceMap.getColor("pageNumber.background"));
/*  907: 762 */     this.pageNumber.setFont(resourceMap.getFont("pageNumber.font"));
/*  908: 763 */     this.pageNumber.setForeground(resourceMap.getColor("pageNumber.foreground"));
/*  909: 764 */     this.pageNumber.setHorizontalAlignment(0);
/*  910: 765 */     this.pageNumber.setText(resourceMap.getString("pageNumber.text", new Object[0]));
/*  911: 766 */     this.pageNumber.setMaximumSize(new Dimension(35, 35));
/*  912: 767 */     this.pageNumber.setMinimumSize(new Dimension(35, 35));
/*  913: 768 */     this.pageNumber.setName("pageNumber");
/*  914: 769 */     this.pageNumber.setOpaque(true);
/*  915: 770 */     this.pageNumber.setPreferredSize(new Dimension(35, 35));
/*  916: 771 */     this.pageNumber.setRequestFocusEnabled(false);
/*  917:     */     
/*  918: 773 */     this.pageTitle.setFont(resourceMap.getFont("pageTitle.font"));
/*  919: 774 */     this.pageTitle.setForeground(resourceMap.getColor("pageTitle.foreground"));
/*  920: 775 */     this.pageTitle.setText(resourceMap.getString("pageTitle.text", new Object[0]));
/*  921: 776 */     this.pageTitle.setName("pageTitle");
/*  922:     */     
/*  923: 778 */     this.widgetPanel.setName("widgetPanel");
/*  924: 779 */     this.widgetPanel.setLayout(new CardLayout());
/*  925:     */     
/*  926: 781 */     this.card1.setName("card1");
/*  927:     */     
/*  928: 783 */     this.requirementPanel.setBorder(BorderFactory.createTitledBorder(null, "Design Requirement:", 0, 0, new Font("Arial", 1, 14), new Color(0, 0, 128)));
/*  929: 784 */     this.requirementPanel.setName("requirementPanel");
/*  930:     */     
/*  931: 786 */     this.requirementPane.setText(resourceMap.getString("requirementPane.text", new Object[0]));
/*  932: 787 */     this.requirementPane.setName("requirementPane");
/*  933:     */     
/*  934: 789 */     GroupLayout requirementPanelLayout = new GroupLayout(this.requirementPanel);
/*  935: 790 */     this.requirementPanel.setLayout(requirementPanelLayout);
/*  936: 791 */     requirementPanelLayout.setHorizontalGroup(requirementPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, requirementPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.requirementPane, -1, 186, 32767).addContainerGap()));
/*  937:     */     
/*  938:     */ 
/*  939:     */ 
/*  940:     */ 
/*  941:     */ 
/*  942:     */ 
/*  943: 798 */     requirementPanelLayout.setVerticalGroup(requirementPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(requirementPanelLayout.createSequentialGroup().addComponent(this.requirementPane).addContainerGap()));
/*  944:     */     
/*  945:     */ 
/*  946:     */ 
/*  947:     */ 
/*  948:     */ 
/*  949:     */ 
/*  950: 805 */     GroupLayout card1Layout = new GroupLayout(this.card1);
/*  951: 806 */     this.card1.setLayout(card1Layout);
/*  952: 807 */     card1Layout.setHorizontalGroup(card1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.requirementPanel, -1, -1, 32767));
/*  953:     */     
/*  954:     */ 
/*  955:     */ 
/*  956: 811 */     card1Layout.setVerticalGroup(card1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.requirementPanel, -1, -1, 32767));
/*  957:     */     
/*  958:     */ 
/*  959:     */ 
/*  960:     */ 
/*  961: 816 */     this.widgetPanel.add(this.card1, "1");
/*  962:     */     
/*  963: 818 */     this.card2.setName("card2");
/*  964:     */     
/*  965: 820 */     this.localContestCodePanel.setBorder(BorderFactory.createTitledBorder(null, "Local Contest Code", 0, 0, new Font("Arial", 1, 14), new Color(0, 0, 128)));
/*  966: 821 */     this.localContestCodePanel.setName("localContestCodePanel");
/*  967:     */     
/*  968: 823 */     this.localContestLabel.setText(resourceMap.getString("localContestLabel.text", new Object[0]));
/*  969: 824 */     this.localContestLabel.setName("localContestLabel");
/*  970:     */     
/*  971: 826 */     this.localContestGroup.add(this.localContestNoButton);
/*  972: 827 */     this.localContestNoButton.setSelected(true);
/*  973: 828 */     this.localContestNoButton.setText(resourceMap.getString("localContestNoButton.text", new Object[0]));
/*  974: 829 */     this.localContestNoButton.setName("localContestNoButton");
/*  975:     */     
/*  976: 831 */     this.localContestGroup.add(this.localContest4YesButton);
/*  977: 832 */     this.localContest4YesButton.setText(resourceMap.getString("localContest4YesButton.text", new Object[0]));
/*  978: 833 */     this.localContest4YesButton.setName("localContest4YesButton");
/*  979: 834 */     this.localContest4YesButton.addItemListener(new ItemListener()
/*  980:     */     {
/*  981:     */       public void itemStateChanged(ItemEvent evt)
/*  982:     */       {
/*  983: 836 */         SetupWizard.this.localContest4YesButtonItemStateChanged(evt);
/*  984:     */       }
/*  985: 839 */     });
/*  986: 840 */     this.localContestGroup.add(this.localContest6YesButton);
/*  987: 841 */     this.localContest6YesButton.setText(resourceMap.getString("localContest6YesButton.text", new Object[0]));
/*  988: 842 */     this.localContest6YesButton.setName("localContest6YesButton");
/*  989: 843 */     this.localContest6YesButton.addChangeListener(new ChangeListener()
/*  990:     */     {
/*  991:     */       public void stateChanged(ChangeEvent evt)
/*  992:     */       {
/*  993: 845 */         SetupWizard.this.localContest6YesButtonStateChanged(evt);
/*  994:     */       }
/*  995: 848 */     });
/*  996: 849 */     this.localContestCodeLabel.setEditable(false);
/*  997: 850 */     this.localContestCodeLabel.setText(resourceMap.getString("localContestCodeLabel.text", new Object[0]));
/*  998: 851 */     this.localContestCodeLabel.setEnabled(false);
/*  999: 852 */     this.localContestCodeLabel.setFocusable(false);
/* 1000: 853 */     this.localContestCodeLabel.setName("localContestCodeLabel");
/* 1001: 854 */     this.localContestCodeLabel.setOpaque(false);
/* 1002:     */     
/* 1003: 856 */     ((AbstractDocument)this.localContestCodeField.getDocument()).setDocumentFilter(new LocalContestCodeFilter(null));
/* 1004: 857 */     this.localContestCodeField.setEnabled(false);
/* 1005: 858 */     this.localContestCodeField.setName("localContestCodeField");
/* 1006:     */     
/* 1007: 860 */     this.localContestMsgLabel.setFont(resourceMap.getFont("localContestMsgLabel.font"));
/* 1008: 861 */     this.localContestMsgLabel.setForeground(resourceMap.getColor("localContestMsgLabel.foreground"));
/* 1009: 862 */     this.localContestMsgLabel.setText(resourceMap.getString("localContestMsgLabel.text", new Object[0]));
/* 1010: 863 */     this.localContestMsgLabel.setVerticalAlignment(1);
/* 1011: 864 */     this.localContestMsgLabel.setName("localContestMsgLabel");
/* 1012:     */     
/* 1013: 866 */     this.localContest4charOKMsgLabel.setText(resourceMap.getString("localContest4charOKMsgLabel.text", new Object[0]));
/* 1014: 867 */     this.localContest4charOKMsgLabel.setName("localContest4charOKMsgLabel");
/* 1015:     */     
/* 1016: 869 */     GroupLayout localContestCodePanelLayout = new GroupLayout(this.localContestCodePanel);
/* 1017: 870 */     this.localContestCodePanel.setLayout(localContestCodePanelLayout);
/* 1018: 871 */     localContestCodePanelLayout.setHorizontalGroup(localContestCodePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(localContestCodePanelLayout.createSequentialGroup().addContainerGap().addGroup(localContestCodePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.localContest6YesButton, -2, -1, -2).addComponent(this.localContest4YesButton, -2, -1, -2).addComponent(this.localContestLabel, -2, 170, -2).addComponent(this.localContestNoButton, -2, 77, -2).addGroup(localContestCodePanelLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(localContestCodePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.localContestCodeField).addComponent(this.localContestCodeLabel).addComponent(this.localContest4charOKMsgLabel, GroupLayout.Alignment.TRAILING, -1, 177, 32767))).addComponent(this.localContestMsgLabel, -1, -1, 32767))));
/* 1019:     */     
/* 1020:     */ 
/* 1021:     */ 
/* 1022:     */ 
/* 1023:     */ 
/* 1024:     */ 
/* 1025:     */ 
/* 1026:     */ 
/* 1027:     */ 
/* 1028:     */ 
/* 1029:     */ 
/* 1030:     */ 
/* 1031:     */ 
/* 1032:     */ 
/* 1033:     */ 
/* 1034:     */ 
/* 1035: 888 */     localContestCodePanelLayout.setVerticalGroup(localContestCodePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(localContestCodePanelLayout.createSequentialGroup().addComponent(this.localContestLabel, -2, 38, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.localContestNoButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.localContest4YesButton, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.localContest6YesButton, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.localContestCodeLabel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.localContestCodeField, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.localContest4charOKMsgLabel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.localContestMsgLabel, -1, 229, 32767).addContainerGap()));
/* 1036:     */     
/* 1037:     */ 
/* 1038:     */ 
/* 1039:     */ 
/* 1040:     */ 
/* 1041:     */ 
/* 1042:     */ 
/* 1043:     */ 
/* 1044:     */ 
/* 1045:     */ 
/* 1046:     */ 
/* 1047:     */ 
/* 1048:     */ 
/* 1049:     */ 
/* 1050:     */ 
/* 1051:     */ 
/* 1052:     */ 
/* 1053:     */ 
/* 1054:     */ 
/* 1055:     */ 
/* 1056: 909 */     GroupLayout card2Layout = new GroupLayout(this.card2);
/* 1057: 910 */     this.card2.setLayout(card2Layout);
/* 1058: 911 */     card2Layout.setHorizontalGroup(card2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.localContestCodePanel, -1, -1, 32767));
/* 1059:     */     
/* 1060:     */ 
/* 1061:     */ 
/* 1062: 915 */     card2Layout.setVerticalGroup(card2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.localContestCodePanel, -1, -1, 32767));
/* 1063:     */     
/* 1064:     */ 
/* 1065:     */ 
/* 1066:     */ 
/* 1067: 920 */     this.widgetPanel.add(this.card2, "2");
/* 1068:     */     
/* 1069: 922 */     this.card3.setName("card3");
/* 1070:     */     
/* 1071: 924 */     this.deckElevationPanel.setBorder(BorderFactory.createTitledBorder(null, "Deck Elevation", 0, 0, new Font("Arial", 1, 14), new Color(0, 0, 128)));
/* 1072: 925 */     this.deckElevationPanel.setName("deckElevationPanel");
/* 1073:     */     
/* 1074: 927 */     this.deckElevationButtonPanel.setName("deckElevationButtonPanel");
/* 1075: 928 */     this.deckElevationButtonPanel.setLayout(new BoxLayout(this.deckElevationButtonPanel, 1));
/* 1076:     */     
/* 1077: 930 */     this.deckElevationUpButton.setIcon(resourceMap.getIcon("deckElevationUpButton.icon"));
/* 1078: 931 */     this.deckElevationUpButton.setEnabled(false);
/* 1079: 932 */     this.deckElevationUpButton.setMargin(new Insets(1, 1, 1, 1));
/* 1080: 933 */     this.deckElevationUpButton.setMaximumSize(new Dimension(19, 12));
/* 1081: 934 */     this.deckElevationUpButton.setMinimumSize(new Dimension(19, 12));
/* 1082: 935 */     this.deckElevationUpButton.setName("deckElevationUpButton");
/* 1083: 936 */     this.deckElevationUpButton.setPreferredSize(new Dimension(19, 12));
/* 1084: 937 */     this.deckElevationButtonPanel.add(this.deckElevationUpButton);
/* 1085:     */     
/* 1086: 939 */     this.deckElevationDownButton.setIcon(resourceMap.getIcon("deckElevationDownButton.icon"));
/* 1087: 940 */     this.deckElevationDownButton.setMargin(new Insets(1, 1, 1, 1));
/* 1088: 941 */     this.deckElevationDownButton.setMaximumSize(new Dimension(19, 12));
/* 1089: 942 */     this.deckElevationDownButton.setMinimumSize(new Dimension(19, 12));
/* 1090: 943 */     this.deckElevationDownButton.setName("deckElevationDownButton");
/* 1091: 944 */     this.deckElevationDownButton.setPreferredSize(new Dimension(19, 12));
/* 1092: 945 */     this.deckElevationButtonPanel.add(this.deckElevationDownButton);
/* 1093:     */     
/* 1094: 947 */     ((ExtendedComboBox)this.deckElevationBox).fillUsingResources(new String[] { "24meters", "20meters", "16meters", "12meters", "8meters", "4meters", "0meters" }, SetupWizard.class);
/* 1095: 948 */     this.deckElevationBox.setName("deckElevationBox");
/* 1096: 949 */     this.deckElevationBox.addItemListener(new ItemListener()
/* 1097:     */     {
/* 1098:     */       public void itemStateChanged(ItemEvent evt)
/* 1099:     */       {
/* 1100: 951 */         SetupWizard.this.deckElevationBoxItemStateChanged(evt);
/* 1101:     */       }
/* 1102: 954 */     });
/* 1103: 955 */     GroupLayout deckElevationPanelLayout = new GroupLayout(this.deckElevationPanel);
/* 1104: 956 */     this.deckElevationPanel.setLayout(deckElevationPanelLayout);
/* 1105: 957 */     deckElevationPanelLayout.setHorizontalGroup(deckElevationPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, deckElevationPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.deckElevationBox, 0, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.deckElevationButtonPanel, -2, 19, -2).addContainerGap()));
/* 1106:     */     
/* 1107:     */ 
/* 1108:     */ 
/* 1109:     */ 
/* 1110:     */ 
/* 1111:     */ 
/* 1112:     */ 
/* 1113:     */ 
/* 1114: 966 */     deckElevationPanelLayout.setVerticalGroup(deckElevationPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(deckElevationPanelLayout.createSequentialGroup().addContainerGap().addGroup(deckElevationPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.deckElevationBox, -2, -1, -2).addComponent(this.deckElevationButtonPanel, -2, -1, -2)).addContainerGap(-1, 32767)));
/* 1115:     */     
/* 1116:     */ 
/* 1117:     */ 
/* 1118:     */ 
/* 1119:     */ 
/* 1120:     */ 
/* 1121:     */ 
/* 1122:     */ 
/* 1123:     */ 
/* 1124: 976 */     this.supportConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "Support Configuration", 0, 0, new Font("Arial", 1, 14), new Color(0, 0, 128)));
/* 1125: 977 */     this.supportConfigPanel.setName("supportConfigPanel");
/* 1126:     */     
/* 1127: 979 */     this.abutmentPanel.setBorder(BorderFactory.createEtchedBorder());
/* 1128: 980 */     this.abutmentPanel.setName("abutmentPanel");
/* 1129:     */     
/* 1130: 982 */     this.abutmentGroup.add(this.standardAbutmentsButton);
/* 1131: 983 */     this.standardAbutmentsButton.setSelected(true);
/* 1132: 984 */     this.standardAbutmentsButton.setText(resourceMap.getString("standardAbutmentsButton.text", new Object[0]));
/* 1133: 985 */     this.standardAbutmentsButton.setName("standardAbutmentsButton");
/* 1134:     */     
/* 1135: 987 */     this.abutmentGroup.add(this.archAbutmentsButton);
/* 1136: 988 */     this.archAbutmentsButton.setText(resourceMap.getString("archAbutmentsButton.text", new Object[0]));
/* 1137: 989 */     this.archAbutmentsButton.setName("archAbutmentsButton");
/* 1138: 990 */     this.archAbutmentsButton.addItemListener(new ItemListener()
/* 1139:     */     {
/* 1140:     */       public void itemStateChanged(ItemEvent evt)
/* 1141:     */       {
/* 1142: 992 */         SetupWizard.this.archAbutmentsButtonItemStateChanged(evt);
/* 1143:     */       }
/* 1144: 995 */     });
/* 1145: 996 */     this.archHeightLabel.setText(resourceMap.getString("archHeightLabel.text", new Object[0]));
/* 1146: 997 */     this.archHeightLabel.setEnabled(false);
/* 1147: 998 */     this.archHeightLabel.setName("archHeightLabel");
/* 1148:     */     
/* 1149:1000 */     this.archHeightButtonPanel.setName("archHeightButtonPanel");
/* 1150:1001 */     this.archHeightButtonPanel.setLayout(new BoxLayout(this.archHeightButtonPanel, 1));
/* 1151:     */     
/* 1152:1003 */     this.archHeightUpButton.setIcon(resourceMap.getIcon("archHeightUpButton.icon"));
/* 1153:1004 */     this.archHeightUpButton.setEnabled(false);
/* 1154:1005 */     this.archHeightUpButton.setMargin(new Insets(1, 1, 1, 1));
/* 1155:1006 */     this.archHeightUpButton.setMaximumSize(new Dimension(19, 12));
/* 1156:1007 */     this.archHeightUpButton.setMinimumSize(new Dimension(19, 12));
/* 1157:1008 */     this.archHeightUpButton.setName("archHeightUpButton");
/* 1158:1009 */     this.archHeightUpButton.setPreferredSize(new Dimension(19, 12));
/* 1159:1010 */     this.archHeightButtonPanel.add(this.archHeightUpButton);
/* 1160:     */     
/* 1161:1012 */     this.archHeightDownButton.setIcon(resourceMap.getIcon("archHeightDownButton.icon"));
/* 1162:1013 */     this.archHeightDownButton.setEnabled(false);
/* 1163:1014 */     this.archHeightDownButton.setMargin(new Insets(1, 1, 1, 1));
/* 1164:1015 */     this.archHeightDownButton.setMaximumSize(new Dimension(19, 12));
/* 1165:1016 */     this.archHeightDownButton.setMinimumSize(new Dimension(19, 12));
/* 1166:1017 */     this.archHeightDownButton.setName("archHeightDownButton");
/* 1167:1018 */     this.archHeightDownButton.setPreferredSize(new Dimension(19, 12));
/* 1168:1019 */     this.archHeightButtonPanel.add(this.archHeightDownButton);
/* 1169:     */     
/* 1170:1021 */     ((ExtendedComboBox)this.archHeightBox).fillUsingResources(new String[] { "24meters", "20meters", "16meters", "12meters", "8meters", "4meters" }, SetupWizard.class);
/* 1171:1022 */     this.archHeightBox.setSelectedIndex(this.archHeightBox.getItemCount() - 1);
/* 1172:1023 */     this.archHeightBox.setEnabled(false);
/* 1173:1024 */     this.archHeightBox.setName("archHeightBox");
/* 1174:1025 */     this.archHeightBox.addItemListener(new ItemListener()
/* 1175:     */     {
/* 1176:     */       public void itemStateChanged(ItemEvent evt)
/* 1177:     */       {
/* 1178:1027 */         SetupWizard.this.archHeightBoxItemStateChanged(evt);
/* 1179:     */       }
/* 1180:1030 */     });
/* 1181:1031 */     GroupLayout abutmentPanelLayout = new GroupLayout(this.abutmentPanel);
/* 1182:1032 */     this.abutmentPanel.setLayout(abutmentPanelLayout);
/* 1183:1033 */     abutmentPanelLayout.setHorizontalGroup(abutmentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(abutmentPanelLayout.createSequentialGroup().addContainerGap().addGroup(abutmentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.standardAbutmentsButton, -1, -1, 32767).addComponent(this.archAbutmentsButton, -1, -1, 32767).addGroup(abutmentPanelLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(abutmentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.archHeightLabel, -1, -1, 32767).addGroup(abutmentPanelLayout.createSequentialGroup().addComponent(this.archHeightBox, -2, 104, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.archHeightButtonPanel, -2, -1, -2))))).addContainerGap()));
/* 1184:     */     
/* 1185:     */ 
/* 1186:     */ 
/* 1187:     */ 
/* 1188:     */ 
/* 1189:     */ 
/* 1190:     */ 
/* 1191:     */ 
/* 1192:     */ 
/* 1193:     */ 
/* 1194:     */ 
/* 1195:     */ 
/* 1196:     */ 
/* 1197:     */ 
/* 1198:     */ 
/* 1199:     */ 
/* 1200:1050 */     abutmentPanelLayout.setVerticalGroup(abutmentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(abutmentPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.standardAbutmentsButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.archAbutmentsButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.archHeightLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(abutmentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.archHeightBox, -2, -1, -2).addComponent(this.archHeightButtonPanel, -2, -1, -2)).addContainerGap(-1, 32767)));
/* 1201:     */     
/* 1202:     */ 
/* 1203:     */ 
/* 1204:     */ 
/* 1205:     */ 
/* 1206:     */ 
/* 1207:     */ 
/* 1208:     */ 
/* 1209:     */ 
/* 1210:     */ 
/* 1211:     */ 
/* 1212:     */ 
/* 1213:     */ 
/* 1214:     */ 
/* 1215:     */ 
/* 1216:1066 */     this.pierPanel.setBorder(BorderFactory.createEtchedBorder());
/* 1217:1067 */     this.pierPanel.setName("pierPanel");
/* 1218:     */     
/* 1219:1069 */     this.pierGroup.add(this.noPierButton);
/* 1220:1070 */     this.noPierButton.setSelected(true);
/* 1221:1071 */     this.noPierButton.setText(resourceMap.getString("noPierButton.text", new Object[0]));
/* 1222:1072 */     this.noPierButton.setName("noPierButton");
/* 1223:     */     
/* 1224:1074 */     this.pierGroup.add(this.pierButton);
/* 1225:1075 */     this.pierButton.setText(resourceMap.getString("pierButton.text", new Object[0]));
/* 1226:1076 */     this.pierButton.setName("pierButton");
/* 1227:1077 */     this.pierButton.addItemListener(new ItemListener()
/* 1228:     */     {
/* 1229:     */       public void itemStateChanged(ItemEvent evt)
/* 1230:     */       {
/* 1231:1079 */         SetupWizard.this.pierButtonItemStateChanged(evt);
/* 1232:     */       }
/* 1233:1082 */     });
/* 1234:1083 */     this.pierHeightLabel.setText(resourceMap.getString("pierHeightLabel.text", new Object[0]));
/* 1235:1084 */     this.pierHeightLabel.setEnabled(false);
/* 1236:1085 */     this.pierHeightLabel.setName("pierHeightLabel");
/* 1237:     */     
/* 1238:1087 */     this.pierHeightButtonPanel.setName("pierHeightButtonPanel");
/* 1239:1088 */     this.pierHeightButtonPanel.setLayout(new BoxLayout(this.pierHeightButtonPanel, 1));
/* 1240:     */     
/* 1241:1090 */     this.pierHeightUpButton.setIcon(resourceMap.getIcon("pierHeightUpButton.icon"));
/* 1242:1091 */     this.pierHeightUpButton.setEnabled(false);
/* 1243:1092 */     this.pierHeightUpButton.setMargin(new Insets(1, 1, 1, 1));
/* 1244:1093 */     this.pierHeightUpButton.setMaximumSize(new Dimension(19, 12));
/* 1245:1094 */     this.pierHeightUpButton.setMinimumSize(new Dimension(19, 12));
/* 1246:1095 */     this.pierHeightUpButton.setName("pierHeightUpButton");
/* 1247:1096 */     this.pierHeightUpButton.setPreferredSize(new Dimension(19, 12));
/* 1248:1097 */     this.pierHeightButtonPanel.add(this.pierHeightUpButton);
/* 1249:     */     
/* 1250:1099 */     this.pierHeightDownButton.setIcon(resourceMap.getIcon("pierHeightDownButton.icon"));
/* 1251:1100 */     this.pierHeightDownButton.setEnabled(false);
/* 1252:1101 */     this.pierHeightDownButton.setMargin(new Insets(1, 1, 1, 1));
/* 1253:1102 */     this.pierHeightDownButton.setMaximumSize(new Dimension(19, 12));
/* 1254:1103 */     this.pierHeightDownButton.setMinimumSize(new Dimension(19, 12));
/* 1255:1104 */     this.pierHeightDownButton.setName("pierHeightDownButton");
/* 1256:1105 */     this.pierHeightDownButton.setPreferredSize(new Dimension(19, 12));
/* 1257:1106 */     this.pierHeightButtonPanel.add(this.pierHeightDownButton);
/* 1258:     */     
/* 1259:1108 */     ((ExtendedComboBox)this.pierHeightBox).fillUsingResources(new String[] { "24meters", "20meters", "16meters", "12meters", "8meters", "4meters", "0meters" }, SetupWizard.class);
/* 1260:1109 */     this.pierHeightBox.setSelectedIndex(this.pierHeightBox.getItemCount() - 1);
/* 1261:1110 */     this.pierHeightBox.setEnabled(false);
/* 1262:1111 */     this.pierHeightBox.setName("pierHeightBox");
/* 1263:1112 */     this.pierHeightBox.addItemListener(new ItemListener()
/* 1264:     */     {
/* 1265:     */       public void itemStateChanged(ItemEvent evt)
/* 1266:     */       {
/* 1267:1114 */         SetupWizard.this.pierHeightBoxItemStateChanged(evt);
/* 1268:     */       }
/* 1269:1117 */     });
/* 1270:1118 */     GroupLayout pierPanelLayout = new GroupLayout(this.pierPanel);
/* 1271:1119 */     this.pierPanel.setLayout(pierPanelLayout);
/* 1272:1120 */     pierPanelLayout.setHorizontalGroup(pierPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pierPanelLayout.createSequentialGroup().addContainerGap().addGroup(pierPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.noPierButton, -1, -1, 32767).addGroup(pierPanelLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(pierPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pierHeightLabel, -1, -1, 32767).addGroup(pierPanelLayout.createSequentialGroup().addComponent(this.pierHeightBox, -2, 104, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.pierHeightButtonPanel, -2, -1, -2)))).addComponent(this.pierButton, -1, -1, 32767)).addContainerGap()));
/* 1273:     */     
/* 1274:     */ 
/* 1275:     */ 
/* 1276:     */ 
/* 1277:     */ 
/* 1278:     */ 
/* 1279:     */ 
/* 1280:     */ 
/* 1281:     */ 
/* 1282:     */ 
/* 1283:     */ 
/* 1284:     */ 
/* 1285:     */ 
/* 1286:     */ 
/* 1287:     */ 
/* 1288:     */ 
/* 1289:1137 */     pierPanelLayout.setVerticalGroup(pierPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pierPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.noPierButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.pierButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.pierHeightLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pierPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pierHeightBox, -2, -1, -2).addComponent(this.pierHeightButtonPanel, -2, -1, -2)).addContainerGap(-1, 32767)));
/* 1290:     */     
/* 1291:     */ 
/* 1292:     */ 
/* 1293:     */ 
/* 1294:     */ 
/* 1295:     */ 
/* 1296:     */ 
/* 1297:     */ 
/* 1298:     */ 
/* 1299:     */ 
/* 1300:     */ 
/* 1301:     */ 
/* 1302:     */ 
/* 1303:     */ 
/* 1304:     */ 
/* 1305:1153 */     this.anchoragePanel.setBorder(BorderFactory.createEtchedBorder());
/* 1306:1154 */     this.anchoragePanel.setName("anchoragePanel");
/* 1307:     */     
/* 1308:1156 */     this.anchorageGroup.add(this.noAnchorageButton);
/* 1309:1157 */     this.noAnchorageButton.setSelected(true);
/* 1310:1158 */     this.noAnchorageButton.setText(resourceMap.getString("noAnchorageButton.text", new Object[0]));
/* 1311:1159 */     this.noAnchorageButton.setName("noAnchorageButton");
/* 1312:1160 */     this.noAnchorageButton.addItemListener(new ItemListener()
/* 1313:     */     {
/* 1314:     */       public void itemStateChanged(ItemEvent evt)
/* 1315:     */       {
/* 1316:1162 */         SetupWizard.this.noAnchorageButtonItemStateChanged(evt);
/* 1317:     */       }
/* 1318:1165 */     });
/* 1319:1166 */     this.anchorageGroup.add(this.oneAnchorageButton);
/* 1320:1167 */     this.oneAnchorageButton.setText(resourceMap.getString("oneAnchorageButton.text", new Object[0]));
/* 1321:1168 */     this.oneAnchorageButton.setName("oneAnchorageButton");
/* 1322:1169 */     this.oneAnchorageButton.addItemListener(new ItemListener()
/* 1323:     */     {
/* 1324:     */       public void itemStateChanged(ItemEvent evt)
/* 1325:     */       {
/* 1326:1171 */         SetupWizard.this.oneAnchorageButtonItemStateChanged(evt);
/* 1327:     */       }
/* 1328:1174 */     });
/* 1329:1175 */     this.anchorageGroup.add(this.twoAnchoragesButton);
/* 1330:1176 */     this.twoAnchoragesButton.setText(resourceMap.getString("twoAnchoragesButton.text", new Object[0]));
/* 1331:1177 */     this.twoAnchoragesButton.setName("twoAnchoragesButton");
/* 1332:1178 */     this.twoAnchoragesButton.addItemListener(new ItemListener()
/* 1333:     */     {
/* 1334:     */       public void itemStateChanged(ItemEvent evt)
/* 1335:     */       {
/* 1336:1180 */         SetupWizard.this.twoAnchoragesButtonItemStateChanged(evt);
/* 1337:     */       }
/* 1338:1183 */     });
/* 1339:1184 */     GroupLayout anchoragePanelLayout = new GroupLayout(this.anchoragePanel);
/* 1340:1185 */     this.anchoragePanel.setLayout(anchoragePanelLayout);
/* 1341:1186 */     anchoragePanelLayout.setHorizontalGroup(anchoragePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(anchoragePanelLayout.createSequentialGroup().addContainerGap().addGroup(anchoragePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.noAnchorageButton, -1, 182, 32767).addComponent(this.twoAnchoragesButton).addComponent(this.oneAnchorageButton, -1, 166, 32767)).addContainerGap()));
/* 1342:     */     
/* 1343:     */ 
/* 1344:     */ 
/* 1345:     */ 
/* 1346:     */ 
/* 1347:     */ 
/* 1348:     */ 
/* 1349:     */ 
/* 1350:     */ 
/* 1351:1196 */     anchoragePanelLayout.setVerticalGroup(anchoragePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(anchoragePanelLayout.createSequentialGroup().addContainerGap().addComponent(this.noAnchorageButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.oneAnchorageButton, -2, 25, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.twoAnchoragesButton).addContainerGap(15, 32767)));
/* 1352:     */     
/* 1353:     */ 
/* 1354:     */ 
/* 1355:     */ 
/* 1356:     */ 
/* 1357:     */ 
/* 1358:     */ 
/* 1359:     */ 
/* 1360:     */ 
/* 1361:     */ 
/* 1362:     */ 
/* 1363:1208 */     GroupLayout supportConfigPanelLayout = new GroupLayout(this.supportConfigPanel);
/* 1364:1209 */     this.supportConfigPanel.setLayout(supportConfigPanelLayout);
/* 1365:1210 */     supportConfigPanelLayout.setHorizontalGroup(supportConfigPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, supportConfigPanelLayout.createSequentialGroup().addContainerGap().addGroup(supportConfigPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.abutmentPanel, GroupLayout.Alignment.LEADING, -1, -1, 32767).addComponent(this.pierPanel, GroupLayout.Alignment.LEADING, -1, -1, 32767).addComponent(this.anchoragePanel, -1, -1, 32767)).addContainerGap()));
/* 1366:     */     
/* 1367:     */ 
/* 1368:     */ 
/* 1369:     */ 
/* 1370:     */ 
/* 1371:     */ 
/* 1372:     */ 
/* 1373:     */ 
/* 1374:     */ 
/* 1375:1220 */     supportConfigPanelLayout.setVerticalGroup(supportConfigPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(supportConfigPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.abutmentPanel, -2, -1, -2).addGap(18, 18, 18).addComponent(this.pierPanel, -2, -1, -2).addGap(18, 18, 18).addComponent(this.anchoragePanel, -1, -1, 32767).addContainerGap()));
/* 1376:     */     
/* 1377:     */ 
/* 1378:     */ 
/* 1379:     */ 
/* 1380:     */ 
/* 1381:     */ 
/* 1382:     */ 
/* 1383:     */ 
/* 1384:     */ 
/* 1385:     */ 
/* 1386:     */ 
/* 1387:1232 */     GroupLayout card3Layout = new GroupLayout(this.card3);
/* 1388:1233 */     this.card3.setLayout(card3Layout);
/* 1389:1234 */     card3Layout.setHorizontalGroup(card3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.supportConfigPanel, -1, -1, 32767).addComponent(this.deckElevationPanel, -1, -1, 32767));
/* 1390:     */     
/* 1391:     */ 
/* 1392:     */ 
/* 1393:     */ 
/* 1394:1239 */     card3Layout.setVerticalGroup(card3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(card3Layout.createSequentialGroup().addComponent(this.deckElevationPanel, -2, -1, -2).addGap(13, 13, 13).addComponent(this.supportConfigPanel, -1, -1, 32767)));
/* 1395:     */     
/* 1396:     */ 
/* 1397:     */ 
/* 1398:     */ 
/* 1399:     */ 
/* 1400:     */ 
/* 1401:     */ 
/* 1402:1247 */     this.widgetPanel.add(this.card3, "3");
/* 1403:     */     
/* 1404:1249 */     this.card4.setName("card4");
/* 1405:     */     
/* 1406:1251 */     this.deckMaterialPanel.setBorder(BorderFactory.createTitledBorder(null, resourceMap.getString("deckMaterialPanel.border.title", new Object[0]), 0, 0, resourceMap.getFont("deckMaterialPanel.border.titleFont"), resourceMap.getColor("deckMaterialPanel.border.titleColor")));
/* 1407:1252 */     this.deckMaterialPanel.setName("deckMaterialPanel");
/* 1408:     */     
/* 1409:1254 */     this.deckMaterialGroup.add(this.mediumConcreteButton);
/* 1410:1255 */     this.mediumConcreteButton.setSelected(true);
/* 1411:1256 */     this.mediumConcreteButton.setText(resourceMap.getString("mediumConcreteButton.text", new Object[0]));
/* 1412:1257 */     this.mediumConcreteButton.setName("mediumConcreteButton");
/* 1413:1258 */     this.mediumConcreteButton.addItemListener(new ItemListener()
/* 1414:     */     {
/* 1415:     */       public void itemStateChanged(ItemEvent evt)
/* 1416:     */       {
/* 1417:1260 */         SetupWizard.this.mediumConcreteButtonItemStateChanged(evt);
/* 1418:     */       }
/* 1419:1263 */     });
/* 1420:1264 */     this.deckMaterialGroup.add(this.highConcreteButton);
/* 1421:1265 */     this.highConcreteButton.setText(resourceMap.getString("highConcreteButton.text", new Object[0]));
/* 1422:1266 */     this.highConcreteButton.setName("highConcreteButton");
/* 1423:1267 */     this.highConcreteButton.addItemListener(new ItemListener()
/* 1424:     */     {
/* 1425:     */       public void itemStateChanged(ItemEvent evt)
/* 1426:     */       {
/* 1427:1269 */         SetupWizard.this.highConcreteButtonItemStateChanged(evt);
/* 1428:     */       }
/* 1429:1272 */     });
/* 1430:1273 */     GroupLayout deckMaterialPanelLayout = new GroupLayout(this.deckMaterialPanel);
/* 1431:1274 */     this.deckMaterialPanel.setLayout(deckMaterialPanelLayout);
/* 1432:1275 */     deckMaterialPanelLayout.setHorizontalGroup(deckMaterialPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(deckMaterialPanelLayout.createSequentialGroup().addContainerGap().addGroup(deckMaterialPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.mediumConcreteButton, -1, 194, 32767).addComponent(this.highConcreteButton, -1, 198, 32767)).addContainerGap()));
/* 1433:     */     
/* 1434:     */ 
/* 1435:     */ 
/* 1436:     */ 
/* 1437:     */ 
/* 1438:     */ 
/* 1439:     */ 
/* 1440:     */ 
/* 1441:1284 */     deckMaterialPanelLayout.setVerticalGroup(deckMaterialPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(deckMaterialPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.mediumConcreteButton, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.highConcreteButton, -2, -1, -2).addContainerGap(42, 32767)));
/* 1442:     */     
/* 1443:     */ 
/* 1444:     */ 
/* 1445:     */ 
/* 1446:     */ 
/* 1447:     */ 
/* 1448:     */ 
/* 1449:     */ 
/* 1450:     */ 
/* 1451:1294 */     this.loadingPanel.setBorder(BorderFactory.createTitledBorder(null, resourceMap.getString("loadingPanel.border.title", new Object[0]), 0, 0, resourceMap.getFont("loadingPanel.border.titleFont"), resourceMap.getColor("loadingPanel.border.titleColor")));
/* 1452:1295 */     this.loadingPanel.setName("loadingPanel");
/* 1453:     */     
/* 1454:1297 */     this.loadingGroup.add(this.standardTruckButton);
/* 1455:1298 */     this.standardTruckButton.setSelected(true);
/* 1456:1299 */     this.standardTruckButton.setText(resourceMap.getString("standardTruckButton.text", new Object[0]));
/* 1457:1300 */     this.standardTruckButton.setName("standardTruckButton");
/* 1458:1301 */     this.standardTruckButton.addItemListener(new ItemListener()
/* 1459:     */     {
/* 1460:     */       public void itemStateChanged(ItemEvent evt)
/* 1461:     */       {
/* 1462:1303 */         SetupWizard.this.standardTruckButtonItemStateChanged(evt);
/* 1463:     */       }
/* 1464:1306 */     });
/* 1465:1307 */     this.loadingGroup.add(this.permitLoadButton);
/* 1466:1308 */     this.permitLoadButton.setText(resourceMap.getString("permitLoadButton.text", new Object[0]));
/* 1467:1309 */     this.permitLoadButton.setName("permitLoadButton");
/* 1468:1310 */     this.permitLoadButton.addItemListener(new ItemListener()
/* 1469:     */     {
/* 1470:     */       public void itemStateChanged(ItemEvent evt)
/* 1471:     */       {
/* 1472:1312 */         SetupWizard.this.permitLoadButtonItemStateChanged(evt);
/* 1473:     */       }
/* 1474:1315 */     });
/* 1475:1316 */     GroupLayout loadingPanelLayout = new GroupLayout(this.loadingPanel);
/* 1476:1317 */     this.loadingPanel.setLayout(loadingPanelLayout);
/* 1477:1318 */     loadingPanelLayout.setHorizontalGroup(loadingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(loadingPanelLayout.createSequentialGroup().addContainerGap().addGroup(loadingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.standardTruckButton).addComponent(this.permitLoadButton, GroupLayout.Alignment.TRAILING)).addContainerGap()));
/* 1478:     */     
/* 1479:     */ 
/* 1480:     */ 
/* 1481:     */ 
/* 1482:     */ 
/* 1483:     */ 
/* 1484:     */ 
/* 1485:     */ 
/* 1486:1327 */     loadingPanelLayout.setVerticalGroup(loadingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(loadingPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.standardTruckButton, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.permitLoadButton, -2, -1, -2).addContainerGap(240, 32767)));
/* 1487:     */     
/* 1488:     */ 
/* 1489:     */ 
/* 1490:     */ 
/* 1491:     */ 
/* 1492:     */ 
/* 1493:     */ 
/* 1494:     */ 
/* 1495:     */ 
/* 1496:1337 */     GroupLayout card4Layout = new GroupLayout(this.card4);
/* 1497:1338 */     this.card4.setLayout(card4Layout);
/* 1498:1339 */     card4Layout.setHorizontalGroup(card4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.loadingPanel, GroupLayout.Alignment.TRAILING, -1, -1, 32767).addComponent(this.deckMaterialPanel, GroupLayout.Alignment.TRAILING, -1, -1, 32767));
/* 1499:     */     
/* 1500:     */ 
/* 1501:     */ 
/* 1502:     */ 
/* 1503:1344 */     card4Layout.setVerticalGroup(card4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, card4Layout.createSequentialGroup().addComponent(this.deckMaterialPanel, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.loadingPanel, -2, -1, -2)));
/* 1504:     */     
/* 1505:     */ 
/* 1506:     */ 
/* 1507:     */ 
/* 1508:     */ 
/* 1509:     */ 
/* 1510:     */ 
/* 1511:1352 */     this.widgetPanel.add(this.card4, "4");
/* 1512:     */     
/* 1513:1354 */     this.card5.setName("card5");
/* 1514:     */     
/* 1515:1356 */     this.selectTemplatePanel.setBorder(BorderFactory.createTitledBorder(null, "Select A Template", 0, 0, new Font("Arial 14", 1, 12), new Color(0, 0, 128)));
/* 1516:1357 */     this.selectTemplatePanel.setName("selectTemplatePanel");
/* 1517:     */     
/* 1518:1359 */     this.templateList.setBackground(resourceMap.getColor("templateList.background"));
/* 1519:1360 */     this.templateList.setBorder(new SoftBevelBorder(1));
/* 1520:1361 */     this.templateList.setModel(new AbstractListModel()
/* 1521:     */     {
/* 1522:1362 */       String[] strings = { "<none>" };
/* 1523:     */       
/* 1524:     */       public int getSize()
/* 1525:     */       {
/* 1526:1363 */         return this.strings.length;
/* 1527:     */       }
/* 1528:     */       
/* 1529:     */       public Object getElementAt(int i)
/* 1530:     */       {
/* 1531:1364 */         return this.strings[i];
/* 1532:     */       }
/* 1533:1365 */     });
/* 1534:1366 */     this.templateList.setSelectionMode(0);
/* 1535:1367 */     this.templateList.setName("templateList");
/* 1536:1368 */     this.templateList.addListSelectionListener(new ListSelectionListener()
/* 1537:     */     {
/* 1538:     */       public void valueChanged(ListSelectionEvent evt)
/* 1539:     */       {
/* 1540:1370 */         SetupWizard.this.templateListValueChanged(evt);
/* 1541:     */       }
/* 1542:1373 */     });
/* 1543:1374 */     GroupLayout selectTemplatePanelLayout = new GroupLayout(this.selectTemplatePanel);
/* 1544:1375 */     this.selectTemplatePanel.setLayout(selectTemplatePanelLayout);
/* 1545:1376 */     selectTemplatePanelLayout.setHorizontalGroup(selectTemplatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(selectTemplatePanelLayout.createSequentialGroup().addContainerGap().addComponent(this.templateList, -1, 186, 32767).addContainerGap()));
/* 1546:     */     
/* 1547:     */ 
/* 1548:     */ 
/* 1549:     */ 
/* 1550:     */ 
/* 1551:     */ 
/* 1552:1383 */     selectTemplatePanelLayout.setVerticalGroup(selectTemplatePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(selectTemplatePanelLayout.createSequentialGroup().addComponent(this.templateList, -1, 494, 32767).addContainerGap()));
/* 1553:     */     
/* 1554:     */ 
/* 1555:     */ 
/* 1556:     */ 
/* 1557:     */ 
/* 1558:     */ 
/* 1559:1390 */     GroupLayout card5Layout = new GroupLayout(this.card5);
/* 1560:1391 */     this.card5.setLayout(card5Layout);
/* 1561:1392 */     card5Layout.setHorizontalGroup(card5Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.selectTemplatePanel, GroupLayout.Alignment.TRAILING, -1, -1, 32767));
/* 1562:     */     
/* 1563:     */ 
/* 1564:     */ 
/* 1565:1396 */     card5Layout.setVerticalGroup(card5Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.selectTemplatePanel, -1, -1, 32767));
/* 1566:     */     
/* 1567:     */ 
/* 1568:     */ 
/* 1569:     */ 
/* 1570:1401 */     this.widgetPanel.add(this.card5, "5");
/* 1571:     */     
/* 1572:1403 */     this.card6.setName("card6");
/* 1573:     */     
/* 1574:1405 */     this.titleBlockInfoPanel.setBorder(BorderFactory.createTitledBorder(null, resourceMap.getString("titleBlockInfoPanel.border.title", new Object[0]), 0, 0, resourceMap.getFont("titleBlockInfoPanel.border.titleFont"), resourceMap.getColor("titleBlockInfoPanel.border.titleColor")));
/* 1575:1406 */     this.titleBlockInfoPanel.setName("titleBlockInfoPanel");
/* 1576:     */     
/* 1577:1408 */     this.projectNameLabel.setText(resourceMap.getString("projectNameLabel.text", new Object[0]));
/* 1578:1409 */     this.projectNameLabel.setName("projectNameLabel");
/* 1579:     */     
/* 1580:1411 */     this.projectNameEdit.setText(resourceMap.getString("projectNameEdit.text", new Object[0]));
/* 1581:1412 */     this.projectNameEdit.setEnabled(false);
/* 1582:1413 */     this.projectNameEdit.setName("projectNameEdit");
/* 1583:     */     
/* 1584:1415 */     this.designedByLabel.setText(resourceMap.getString("designedByLabel.text", new Object[0]));
/* 1585:1416 */     this.designedByLabel.setName("designedByLabel");
/* 1586:     */     
/* 1587:1418 */     this.designedByEdit.setText(resourceMap.getString("designedByEdit.text", new Object[0]));
/* 1588:1419 */     this.designedByEdit.setName("designedByEdit");
/* 1589:     */     
/* 1590:1421 */     this.projectIdLabel.setText(resourceMap.getString("projectIdLabel.text", new Object[0]));
/* 1591:1422 */     this.projectIdLabel.setName("projectIdLabel");
/* 1592:     */     
/* 1593:1424 */     this.projectIdEdit.setText(resourceMap.getString("projectIdEdit.text", new Object[0]));
/* 1594:1425 */     this.projectIdEdit.setName("projectIdEdit");
/* 1595:     */     
/* 1596:1427 */     this.projectIdPrefixLabel.setHorizontalAlignment(4);
/* 1597:1428 */     this.projectIdPrefixLabel.setText(resourceMap.getString("projectIdPrefixLabel.text", new Object[0]));
/* 1598:1429 */     this.projectIdPrefixLabel.setEnabled(false);
/* 1599:1430 */     this.projectIdPrefixLabel.setName("projectIdPrefixLabel");
/* 1600:     */     
/* 1601:1432 */     GroupLayout titleBlockInfoPanelLayout = new GroupLayout(this.titleBlockInfoPanel);
/* 1602:1433 */     this.titleBlockInfoPanel.setLayout(titleBlockInfoPanelLayout);
/* 1603:1434 */     titleBlockInfoPanelLayout.setHorizontalGroup(titleBlockInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(titleBlockInfoPanelLayout.createSequentialGroup().addContainerGap().addGroup(titleBlockInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, titleBlockInfoPanelLayout.createSequentialGroup().addComponent(this.projectIdPrefixLabel, -1, 52, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.projectIdEdit, -2, 129, -2)).addComponent(this.designedByEdit).addComponent(this.projectNameEdit, GroupLayout.Alignment.TRAILING, -2, 0, 32767).addComponent(this.projectIdLabel, -2, 98, -2).addComponent(this.projectNameLabel, -2, 142, -2).addComponent(this.designedByLabel, -2, 161, -2)).addContainerGap()));
/* 1604:     */     
/* 1605:     */ 
/* 1606:     */ 
/* 1607:     */ 
/* 1608:     */ 
/* 1609:     */ 
/* 1610:     */ 
/* 1611:     */ 
/* 1612:     */ 
/* 1613:     */ 
/* 1614:     */ 
/* 1615:     */ 
/* 1616:     */ 
/* 1617:     */ 
/* 1618:     */ 
/* 1619:1450 */     titleBlockInfoPanelLayout.setVerticalGroup(titleBlockInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(titleBlockInfoPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.projectNameLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.projectNameEdit, -2, -1, -2).addGap(18, 18, 18).addComponent(this.designedByLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.designedByEdit, -2, -1, -2).addGap(18, 18, 18).addComponent(this.projectIdLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(titleBlockInfoPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.projectIdEdit, -2, -1, -2).addComponent(this.projectIdPrefixLabel)).addContainerGap(322, 32767)));
/* 1620:     */     
/* 1621:     */ 
/* 1622:     */ 
/* 1623:     */ 
/* 1624:     */ 
/* 1625:     */ 
/* 1626:     */ 
/* 1627:     */ 
/* 1628:     */ 
/* 1629:     */ 
/* 1630:     */ 
/* 1631:     */ 
/* 1632:     */ 
/* 1633:     */ 
/* 1634:     */ 
/* 1635:     */ 
/* 1636:     */ 
/* 1637:     */ 
/* 1638:     */ 
/* 1639:1470 */     GroupLayout card6Layout = new GroupLayout(this.card6);
/* 1640:1471 */     this.card6.setLayout(card6Layout);
/* 1641:1472 */     card6Layout.setHorizontalGroup(card6Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.titleBlockInfoPanel, GroupLayout.Alignment.TRAILING, -1, -1, 32767));
/* 1642:     */     
/* 1643:     */ 
/* 1644:     */ 
/* 1645:1476 */     card6Layout.setVerticalGroup(card6Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.titleBlockInfoPanel, -1, -1, 32767));
/* 1646:     */     
/* 1647:     */ 
/* 1648:     */ 
/* 1649:     */ 
/* 1650:1481 */     this.widgetPanel.add(this.card6, "6");
/* 1651:     */     
/* 1652:1483 */     this.card7.setName("card7");
/* 1653:     */     
/* 1654:1485 */     this.designPanel.setBorder(BorderFactory.createTitledBorder(null, resourceMap.getString("designPanel.border.title", new Object[0]), 0, 0, resourceMap.getFont("designPanel.border.titleFont"), resourceMap.getColor("designPanel.border.titleColor")));
/* 1655:1486 */     this.designPanel.setName("designPanel");
/* 1656:     */     
/* 1657:1488 */     this.instructionsPane.setText(resourceMap.getString("instructionsPane.text", new Object[0]));
/* 1658:1489 */     this.instructionsPane.setName("instructionsPane");
/* 1659:     */     
/* 1660:1491 */     GroupLayout designPanelLayout = new GroupLayout(this.designPanel);
/* 1661:1492 */     this.designPanel.setLayout(designPanelLayout);
/* 1662:1493 */     designPanelLayout.setHorizontalGroup(designPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(designPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.instructionsPane, -1, 186, 32767).addContainerGap()));
/* 1663:     */     
/* 1664:     */ 
/* 1665:     */ 
/* 1666:     */ 
/* 1667:     */ 
/* 1668:     */ 
/* 1669:1500 */     designPanelLayout.setVerticalGroup(designPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(designPanelLayout.createSequentialGroup().addComponent(this.instructionsPane, -1, 494, 32767).addContainerGap()));
/* 1670:     */     
/* 1671:     */ 
/* 1672:     */ 
/* 1673:     */ 
/* 1674:     */ 
/* 1675:     */ 
/* 1676:1507 */     GroupLayout card7Layout = new GroupLayout(this.card7);
/* 1677:1508 */     this.card7.setLayout(card7Layout);
/* 1678:1509 */     card7Layout.setHorizontalGroup(card7Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.designPanel, -1, -1, 32767));
/* 1679:     */     
/* 1680:     */ 
/* 1681:     */ 
/* 1682:1513 */     card7Layout.setVerticalGroup(card7Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.designPanel, -1, -1, 32767));
/* 1683:     */     
/* 1684:     */ 
/* 1685:     */ 
/* 1686:     */ 
/* 1687:1518 */     this.widgetPanel.add(this.card7, "7");
/* 1688:     */     
/* 1689:1520 */     this.deckCrossSectionPanel.setBorder(BorderFactory.createTitledBorder(null, "Deck Cross-Section", 0, 0, new Font("Arial", 1, 14), new Color(0, 0, 128)));
/* 1690:1521 */     this.deckCrossSectionPanel.setName("deckCrossSectionPanel");
/* 1691:     */     
/* 1692:1523 */     this.deckCartoonLabel.setIcon(resourceMap.getIcon("deckCartoonLabel.icon"));
/* 1693:1524 */     this.deckCartoonLabel.setBorder(BorderFactory.createBevelBorder(1));
/* 1694:1525 */     this.deckCartoonLabel.setName("deckCartoonLabel");
/* 1695:     */     
/* 1696:1527 */     GroupLayout deckCrossSectionPanelLayout = new GroupLayout(this.deckCrossSectionPanel);
/* 1697:1528 */     this.deckCrossSectionPanel.setLayout(deckCrossSectionPanelLayout);
/* 1698:1529 */     deckCrossSectionPanelLayout.setHorizontalGroup(deckCrossSectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(deckCrossSectionPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.deckCartoonLabel).addContainerGap(-1, 32767)));
/* 1699:     */     
/* 1700:     */ 
/* 1701:     */ 
/* 1702:     */ 
/* 1703:     */ 
/* 1704:     */ 
/* 1705:1536 */     deckCrossSectionPanelLayout.setVerticalGroup(deckCrossSectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(deckCrossSectionPanelLayout.createSequentialGroup().addComponent(this.deckCartoonLabel).addContainerGap(-1, 32767)));
/* 1706:     */     
/* 1707:     */ 
/* 1708:     */ 
/* 1709:     */ 
/* 1710:     */ 
/* 1711:     */ 
/* 1712:1543 */     this.elevationViewPanel.setBorder(BorderFactory.createTitledBorder(null, "Elevation View", 0, 0, new Font("Arial", 1, 14), new Color(0, 0, 128)));
/* 1713:1544 */     this.elevationViewPanel.setName("elevationViewPanel");
/* 1714:     */     
/* 1715:1546 */     this.elevationViewLabel.setIcon(resourceMap.getIcon("elevationViewLabel.icon"));
/* 1716:1547 */     this.elevationViewLabel.setBorder(BorderFactory.createBevelBorder(1));
/* 1717:1548 */     this.elevationViewLabel.setName("elevationViewLabel");
/* 1718:     */     
/* 1719:1550 */     this.legendPanel.setBorder(BorderFactory.createEtchedBorder());
/* 1720:1551 */     this.legendPanel.setName("legendPanel");
/* 1721:     */     
/* 1722:1553 */     this.legendRiverBankLabel.setIcon(resourceMap.getIcon("legendRiverBankLabel.icon"));
/* 1723:1554 */     this.legendRiverBankLabel.setText(resourceMap.getString("legendRiverBankLabel.text", new Object[0]));
/* 1724:1555 */     this.legendRiverBankLabel.setName("legendRiverBankLabel");
/* 1725:     */     
/* 1726:1557 */     this.legendExcavationLabel.setIcon(resourceMap.getIcon("legendExcavationLabel.icon"));
/* 1727:1558 */     this.legendExcavationLabel.setText(resourceMap.getString("legendExcavationLabel.text", new Object[0]));
/* 1728:1559 */     this.legendExcavationLabel.setName("legendExcavationLabel");
/* 1729:     */     
/* 1730:1561 */     this.legendRiverLabel.setIcon(resourceMap.getIcon("legendRiverLabel.icon"));
/* 1731:1562 */     this.legendRiverLabel.setText(resourceMap.getString("legendRiverLabel.text", new Object[0]));
/* 1732:1563 */     this.legendRiverLabel.setName("legendRiverLabel");
/* 1733:     */     
/* 1734:1565 */     this.legendTopSpacerLabel.setText(" ");
/* 1735:1566 */     this.legendTopSpacerLabel.setName("legendTopSpacerLabel");
/* 1736:     */     
/* 1737:1568 */     this.legendDeckLabel.setIcon(resourceMap.getIcon("legendDeckLabel.icon"));
/* 1738:1569 */     this.legendDeckLabel.setText(resourceMap.getString("legendDeckLabel.text", new Object[0]));
/* 1739:1570 */     this.legendDeckLabel.setName("legendDeckLabel");
/* 1740:     */     
/* 1741:1572 */     this.legendAbutmentLabel.setIcon(resourceMap.getIcon("legendAbutmentLabel.icon"));
/* 1742:1573 */     this.legendAbutmentLabel.setText(resourceMap.getString("legendAbutmentLabel.text", new Object[0]));
/* 1743:1574 */     this.legendAbutmentLabel.setName("legendAbutmentLabel");
/* 1744:     */     
/* 1745:1576 */     this.legendPierLabel.setIcon(resourceMap.getIcon("legendPierLabel.icon"));
/* 1746:1577 */     this.legendPierLabel.setText(resourceMap.getString("legendPierLabel.text", new Object[0]));
/* 1747:1578 */     this.legendPierLabel.setName("legendPierLabel");
/* 1748:1579 */     this.legendPierLabel.setOpaque(true);
/* 1749:     */     
/* 1750:1581 */     this.legendBottomSpacerLabel.setText(" ");
/* 1751:1582 */     this.legendBottomSpacerLabel.setMaximumSize(new Dimension(3, 19));
/* 1752:1583 */     this.legendBottomSpacerLabel.setMinimumSize(new Dimension(3, 19));
/* 1753:1584 */     this.legendBottomSpacerLabel.setName("legendBottomSpacerLabel");
/* 1754:1585 */     this.legendBottomSpacerLabel.setPreferredSize(new Dimension(3, 19));
/* 1755:     */     
/* 1756:1587 */     GroupLayout legendPanelLayout = new GroupLayout(this.legendPanel);
/* 1757:1588 */     this.legendPanel.setLayout(legendPanelLayout);
/* 1758:1589 */     legendPanelLayout.setHorizontalGroup(legendPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(legendPanelLayout.createSequentialGroup().addContainerGap().addGroup(legendPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.legendDeckLabel).addComponent(this.legendRiverBankLabel, -2, 102, -2)).addGroup(legendPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(legendPanelLayout.createSequentialGroup().addGap(11, 11, 11).addComponent(this.legendExcavationLabel, -2, 107, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.legendRiverLabel, -2, 78, -2)).addGroup(legendPanelLayout.createSequentialGroup().addGap(12, 12, 12).addComponent(this.legendAbutmentLabel, -2, 99, -2).addGap(18, 18, 18).addComponent(this.legendPierLabel, -2, 73, -2))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(legendPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.legendTopSpacerLabel, -1, -1, 32767).addComponent(this.legendBottomSpacerLabel, -1, -1, 32767)).addContainerGap(-1, 32767)));
/* 1759:     */     
/* 1760:     */ 
/* 1761:     */ 
/* 1762:     */ 
/* 1763:     */ 
/* 1764:     */ 
/* 1765:     */ 
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
/* 1777:     */ 
/* 1778:     */ 
/* 1779:     */ 
/* 1780:     */ 
/* 1781:     */ 
/* 1782:1613 */     legendPanelLayout.setVerticalGroup(legendPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(legendPanelLayout.createSequentialGroup().addContainerGap().addGroup(legendPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.legendRiverBankLabel).addComponent(this.legendRiverLabel).addComponent(this.legendExcavationLabel).addComponent(this.legendTopSpacerLabel, -2, 14, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(legendPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.legendDeckLabel).addComponent(this.legendAbutmentLabel).addComponent(this.legendPierLabel, -2, 19, -2).addComponent(this.legendBottomSpacerLabel, -2, -1, -2)).addContainerGap()));
/* 1783:     */     
/* 1784:     */ 
/* 1785:     */ 
/* 1786:     */ 
/* 1787:     */ 
/* 1788:     */ 
/* 1789:     */ 
/* 1790:     */ 
/* 1791:     */ 
/* 1792:     */ 
/* 1793:     */ 
/* 1794:     */ 
/* 1795:     */ 
/* 1796:     */ 
/* 1797:     */ 
/* 1798:     */ 
/* 1799:     */ 
/* 1800:1631 */     legendPanelLayout.linkSize(1, new Component[] { this.legendExcavationLabel, this.legendRiverBankLabel, this.legendRiverLabel, this.legendTopSpacerLabel });
/* 1801:     */     
/* 1802:1633 */     legendPanelLayout.linkSize(1, new Component[] { this.legendAbutmentLabel, this.legendBottomSpacerLabel, this.legendDeckLabel, this.legendPierLabel });
/* 1803:     */     
/* 1804:1635 */     GroupLayout elevationViewPanelLayout = new GroupLayout(this.elevationViewPanel);
/* 1805:1636 */     this.elevationViewPanel.setLayout(elevationViewPanelLayout);
/* 1806:1637 */     elevationViewPanelLayout.setHorizontalGroup(elevationViewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(elevationViewPanelLayout.createSequentialGroup().addContainerGap().addGroup(elevationViewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.elevationViewLabel).addComponent(this.legendPanel, -1, -1, 32767)).addContainerGap()));
/* 1807:     */     
/* 1808:     */ 
/* 1809:     */ 
/* 1810:     */ 
/* 1811:     */ 
/* 1812:     */ 
/* 1813:     */ 
/* 1814:     */ 
/* 1815:1646 */     elevationViewPanelLayout.setVerticalGroup(elevationViewPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, elevationViewPanelLayout.createSequentialGroup().addComponent(this.elevationViewLabel).addGap(18, 18, 18).addComponent(this.legendPanel, -1, -1, 32767).addContainerGap()));
/* 1816:     */     
/* 1817:     */ 
/* 1818:     */ 
/* 1819:     */ 
/* 1820:     */ 
/* 1821:     */ 
/* 1822:     */ 
/* 1823:     */ 
/* 1824:1655 */     this.tipPanel.setBorder(BorderFactory.createTitledBorder("Design Tip:"));
/* 1825:1656 */     this.tipPanel.setName("tipPanel");
/* 1826:     */     
/* 1827:1658 */     this.tipPane.setName("tipPane");
/* 1828:     */     
/* 1829:1660 */     GroupLayout tipPanelLayout = new GroupLayout(this.tipPanel);
/* 1830:1661 */     this.tipPanel.setLayout(tipPanelLayout);
/* 1831:1662 */     tipPanelLayout.setHorizontalGroup(tipPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(tipPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.tipPane, -1, 191, 32767).addContainerGap()));
/* 1832:     */     
/* 1833:     */ 
/* 1834:     */ 
/* 1835:     */ 
/* 1836:     */ 
/* 1837:     */ 
/* 1838:1669 */     tipPanelLayout.setVerticalGroup(tipPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(tipPanelLayout.createSequentialGroup().addComponent(this.tipPane).addContainerGap()));
/* 1839:     */     
/* 1840:     */ 
/* 1841:     */ 
/* 1842:     */ 
/* 1843:     */ 
/* 1844:     */ 
/* 1845:1676 */     this.siteCostPanel.setBorder(BorderFactory.createTitledBorder("Site Cost:"));
/* 1846:1677 */     this.siteCostPanel.setName("siteCostPanel");
/* 1847:     */     
/* 1848:1679 */     this.siteCostDetailTable.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
/* 1849:1680 */     this.siteCostDetailTable.setModel(new SiteCostTableModel());
/* 1850:1681 */     ((SiteCostTable)this.siteCostDetailTable).initalize();
/* 1851:1682 */     this.siteCostDetailTable.setFocusable(false);
/* 1852:1683 */     this.siteCostDetailTable.setIntercellSpacing(new Dimension(6, 1));
/* 1853:1684 */     this.siteCostDetailTable.setName("siteCostDetailTable");
/* 1854:1685 */     this.siteCostDetailTable.setRowSelectionAllowed(false);
/* 1855:1686 */     this.siteCostDetailTable.getTableHeader().setReorderingAllowed(false);
/* 1856:     */     
/* 1857:1688 */     this.siteCostLabel.setHorizontalAlignment(11);
/* 1858:1689 */     this.siteCostLabel.setText(resourceMap.getString("siteCostLabel.text", new Object[0]));
/* 1859:1690 */     this.siteCostLabel.setName("siteCostLabel");
/* 1860:     */     
/* 1861:1692 */     this.dropRaiseButton.setIcon(resourceMap.getIcon("dropRaiseButton.icon"));
/* 1862:1693 */     this.dropRaiseButton.setMaximumSize(new Dimension(27, 27));
/* 1863:1694 */     this.dropRaiseButton.setMinimumSize(new Dimension(27, 27));
/* 1864:1695 */     this.dropRaiseButton.setName("dropRaiseButton");
/* 1865:1696 */     this.dropRaiseButton.setPreferredSize(new Dimension(27, 27));
/* 1866:1697 */     this.dropRaiseButton.addActionListener(new ActionListener()
/* 1867:     */     {
/* 1868:     */       public void actionPerformed(ActionEvent evt)
/* 1869:     */       {
/* 1870:1699 */         SetupWizard.this.dropRaiseButtonActionPerformed(evt);
/* 1871:     */       }
/* 1872:1702 */     });
/* 1873:1703 */     this.costNoteLabel.setText(resourceMap.getString("costNoteLabel.text", new Object[0]));
/* 1874:1704 */     this.costNoteLabel.setName("costNoteLabel");
/* 1875:     */     
/* 1876:1706 */     this.siteConditionsLabel.setText(resourceMap.getString("siteConditionsLabel.text", new Object[0]));
/* 1877:1707 */     this.siteConditionsLabel.setName("siteConditionsLabel");
/* 1878:     */     
/* 1879:1709 */     GroupLayout siteCostPanelLayout = new GroupLayout(this.siteCostPanel);
/* 1880:1710 */     this.siteCostPanel.setLayout(siteCostPanelLayout);
/* 1881:1711 */     siteCostPanelLayout.setHorizontalGroup(siteCostPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, siteCostPanelLayout.createSequentialGroup().addContainerGap().addGroup(siteCostPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.siteCostDetailTable, GroupLayout.Alignment.LEADING, -1, -1, 32767).addGroup(siteCostPanelLayout.createSequentialGroup().addComponent(this.siteCostLabel, -1, 74, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.costNoteLabel, -2, 539, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.siteConditionsLabel, -2, 140, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.dropRaiseButton, -2, -1, -2))).addContainerGap()));
/* 1882:     */     
/* 1883:     */ 
/* 1884:     */ 
/* 1885:     */ 
/* 1886:     */ 
/* 1887:     */ 
/* 1888:     */ 
/* 1889:     */ 
/* 1890:     */ 
/* 1891:     */ 
/* 1892:     */ 
/* 1893:     */ 
/* 1894:     */ 
/* 1895:     */ 
/* 1896:     */ 
/* 1897:1727 */     siteCostPanelLayout.setVerticalGroup(siteCostPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(siteCostPanelLayout.createSequentialGroup().addGroup(siteCostPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.dropRaiseButton, -1, -1, 32767).addGroup(siteCostPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.siteCostLabel, -2, 24, -2).addComponent(this.costNoteLabel, GroupLayout.Alignment.LEADING, -2, 24, -2)).addComponent(this.siteConditionsLabel, -1, -1, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.siteCostDetailTable, -1, 35, 32767).addContainerGap()));
/* 1898:     */     
/* 1899:     */ 
/* 1900:     */ 
/* 1901:     */ 
/* 1902:     */ 
/* 1903:     */ 
/* 1904:     */ 
/* 1905:     */ 
/* 1906:     */ 
/* 1907:     */ 
/* 1908:     */ 
/* 1909:     */ 
/* 1910:     */ 
/* 1911:1741 */     this.siteCostDetailTable.getColumnModel().getColumn(0).setResizable(false);
/* 1912:1742 */     this.siteCostDetailTable.getColumnModel().getColumn(1).setResizable(false);
/* 1913:1743 */     this.siteCostDetailTable.getColumnModel().getColumn(1).setPreferredWidth(400);
/* 1914:1744 */     this.siteCostDetailTable.getColumnModel().getColumn(2).setResizable(false);
/* 1915:     */     
/* 1916:1746 */     this.help.setText(resourceMap.getString("help.text", new Object[0]));
/* 1917:1747 */     this.help.setName("help");
/* 1918:     */     
/* 1919:1749 */     this.cancel.setText(resourceMap.getString("cancel.text", new Object[0]));
/* 1920:1750 */     this.cancel.setName("cancel");
/* 1921:1751 */     this.cancel.addActionListener(new ActionListener()
/* 1922:     */     {
/* 1923:     */       public void actionPerformed(ActionEvent evt)
/* 1924:     */       {
/* 1925:1753 */         SetupWizard.this.cancelActionPerformed(evt);
/* 1926:     */       }
/* 1927:1756 */     });
/* 1928:1757 */     this.back.setText(resourceMap.getString("back.text", new Object[0]));
/* 1929:1758 */     this.back.setName("back");
/* 1930:1759 */     this.back.addActionListener(new ActionListener()
/* 1931:     */     {
/* 1932:     */       public void actionPerformed(ActionEvent evt)
/* 1933:     */       {
/* 1934:1761 */         SetupWizard.this.backActionPerformed(evt);
/* 1935:     */       }
/* 1936:1764 */     });
/* 1937:1765 */     this.next.setText(resourceMap.getString("next.text", new Object[0]));
/* 1938:1766 */     this.next.setName("next");
/* 1939:1767 */     this.next.addActionListener(new ActionListener()
/* 1940:     */     {
/* 1941:     */       public void actionPerformed(ActionEvent evt)
/* 1942:     */       {
/* 1943:1769 */         SetupWizard.this.nextActionPerformed(evt);
/* 1944:     */       }
/* 1945:1772 */     });
/* 1946:1773 */     this.finish.setText(resourceMap.getString("finish.text", new Object[0]));
/* 1947:1774 */     this.finish.setName("finish");
/* 1948:1775 */     this.finish.addActionListener(new ActionListener()
/* 1949:     */     {
/* 1950:     */       public void actionPerformed(ActionEvent evt)
/* 1951:     */       {
/* 1952:1777 */         SetupWizard.this.finishActionPerformed(evt);
/* 1953:     */       }
/* 1954:1780 */     });
/* 1955:1781 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 1956:1782 */     getContentPane().setLayout(layout);
/* 1957:1783 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.siteCostPanel, -1, -1, 32767).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.help).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.cancel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.back).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.next).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.finish)).addGroup(layout.createSequentialGroup().addComponent(this.pageNumber, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.pageTitle).addGap(0, 0, 32767)).addGroup(layout.createSequentialGroup().addComponent(this.widgetPanel, -2, 222, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.deckCrossSectionPanel, -1, -1, 32767).addComponent(this.elevationViewPanel, -1, -1, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.tipPanel, -1, -1, 32767))).addContainerGap()));
/* 1958:     */     
/* 1959:     */ 
/* 1960:     */ 
/* 1961:     */ 
/* 1962:     */ 
/* 1963:     */ 
/* 1964:     */ 
/* 1965:     */ 
/* 1966:     */ 
/* 1967:     */ 
/* 1968:     */ 
/* 1969:     */ 
/* 1970:     */ 
/* 1971:     */ 
/* 1972:     */ 
/* 1973:     */ 
/* 1974:     */ 
/* 1975:     */ 
/* 1976:     */ 
/* 1977:     */ 
/* 1978:     */ 
/* 1979:     */ 
/* 1980:     */ 
/* 1981:     */ 
/* 1982:     */ 
/* 1983:     */ 
/* 1984:     */ 
/* 1985:     */ 
/* 1986:     */ 
/* 1987:     */ 
/* 1988:     */ 
/* 1989:1815 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.pageNumber, -2, -1, -2).addComponent(this.pageTitle)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.tipPanel, -1, -1, 32767).addGroup(layout.createSequentialGroup().addComponent(this.deckCrossSectionPanel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.elevationViewPanel, -2, -1, -2).addGap(0, 0, 32767)).addComponent(this.widgetPanel, -1, 0, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.siteCostPanel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.finish).addComponent(this.next).addComponent(this.back).addComponent(this.cancel).addComponent(this.help)).addContainerGap()));
/* 1990:     */     
/* 1991:     */ 
/* 1992:     */ 
/* 1993:     */ 
/* 1994:     */ 
/* 1995:     */ 
/* 1996:     */ 
/* 1997:     */ 
/* 1998:     */ 
/* 1999:     */ 
/* 2000:     */ 
/* 2001:     */ 
/* 2002:     */ 
/* 2003:     */ 
/* 2004:     */ 
/* 2005:     */ 
/* 2006:     */ 
/* 2007:     */ 
/* 2008:     */ 
/* 2009:     */ 
/* 2010:     */ 
/* 2011:     */ 
/* 2012:     */ 
/* 2013:     */ 
/* 2014:     */ 
/* 2015:     */ 
/* 2016:     */ 
/* 2017:1843 */     pack();
/* 2018:     */   }
/* 2019:     */   
/* 2020:     */   private void cancelActionPerformed(ActionEvent evt)
/* 2021:     */   {
/* 2022:1847 */     setVisible(false);
/* 2023:     */   }
/* 2024:     */   
/* 2025:     */   private void backActionPerformed(ActionEvent evt)
/* 2026:     */   {
/* 2027:1851 */     this.currentPage = this.pages[this.currentPage].getBackPageNumber();
/* 2028:1852 */     this.pages[this.currentPage].load();
/* 2029:     */   }
/* 2030:     */   
/* 2031:     */   private void nextActionPerformed(ActionEvent evt)
/* 2032:     */   {
/* 2033:1856 */     this.currentPage = this.pages[this.currentPage].getNextPageNumber();
/* 2034:1857 */     this.pages[this.currentPage].load();
/* 2035:     */   }
/* 2036:     */   
/* 2037:     */   private void finishActionPerformed(ActionEvent evt)
/* 2038:     */   {
/* 2039:1861 */     this.ok = true;
/* 2040:1862 */     setVisible(false);
/* 2041:     */   }
/* 2042:     */   
/* 2043:     */   private void localContest6YesButtonStateChanged(ChangeEvent evt)
/* 2044:     */   {
/* 2045:1866 */     boolean localContest = this.localContest6YesButton.isSelected();
/* 2046:1867 */     this.next.setEnabled(!localContest);
/* 2047:1868 */     this.localContestCodeLabel.setEnabled(localContest);
/* 2048:1869 */     this.localContestCodeField.setEnabled(localContest);
/* 2049:1870 */     if (localContest)
/* 2050:     */     {
/* 2051:1871 */       this.localContestCodeField.requestFocusInWindow();
/* 2052:     */     }
/* 2053:     */     else
/* 2054:     */     {
/* 2055:1874 */       this.localContestCodeField.setText("");
/* 2056:1875 */       this.localContestMsgLabel.setText("");
/* 2057:     */     }
/* 2058:     */   }
/* 2059:     */   
/* 2060:     */   private void deckElevationBoxItemStateChanged(ItemEvent evt)
/* 2061:     */   {
/* 2062:1880 */     if (evt.getStateChange() == 1)
/* 2063:     */     {
/* 2064:1881 */       beginUpdate();
/* 2065:1882 */       int nItems = this.deckElevationBox.getItemCount();
/* 2066:1883 */       int selectedIndex = this.deckElevationBox.getSelectedIndex();
/* 2067:1884 */       ((ExtendedComboBoxModel)this.archHeightBox.getModel()).setBase(selectedIndex);
/* 2068:1885 */       ((ExtendedComboBoxModel)this.pierHeightBox.getModel()).setBase(selectedIndex);
/* 2069:1886 */       boolean zeroMeters = selectedIndex == nItems - 1;
/* 2070:1887 */       this.standardAbutmentsButton.setSelected(zeroMeters);
/* 2071:1888 */       this.archAbutmentsButton.setEnabled(!zeroMeters);
/* 2072:1889 */       if (!zeroMeters)
/* 2073:     */       {
/* 2074:1890 */         if (this.archHeightBox.getSelectedIndex() == -1) {
/* 2075:1891 */           this.archHeightBox.setSelectedIndex(this.archHeightBox.getItemCount() - 1);
/* 2076:     */         }
/* 2077:1893 */         if (this.pierHeightBox.getSelectedIndex() == -1) {
/* 2078:1894 */           this.pierHeightBox.setSelectedIndex(this.pierHeightBox.getItemCount() - 1);
/* 2079:     */         }
/* 2080:     */       }
/* 2081:1897 */       endUpdate();
/* 2082:     */     }
/* 2083:     */   }
/* 2084:     */   
/* 2085:     */   private void archHeightBoxItemStateChanged(ItemEvent evt)
/* 2086:     */   {
/* 2087:1902 */     if (evt.getStateChange() == 1) {
/* 2088:1903 */       update();
/* 2089:     */     }
/* 2090:     */   }
/* 2091:     */   
/* 2092:     */   private void pierHeightBoxItemStateChanged(ItemEvent evt)
/* 2093:     */   {
/* 2094:1908 */     if (evt.getStateChange() == 1) {
/* 2095:1909 */       update();
/* 2096:     */     }
/* 2097:     */   }
/* 2098:     */   
/* 2099:     */   private void archAbutmentsButtonItemStateChanged(ItemEvent evt)
/* 2100:     */   {
/* 2101:1914 */     beginUpdate();
/* 2102:1915 */     boolean selected = evt.getStateChange() == 1;
/* 2103:1916 */     if (selected)
/* 2104:     */     {
/* 2105:1917 */       this.noPierButton.setSelected(true);
/* 2106:1918 */       this.noAnchorageButton.setSelected(true);
/* 2107:     */     }
/* 2108:1920 */     this.archHeightLabel.setEnabled(selected);
/* 2109:1921 */     this.archHeightBox.setEnabled(selected);
/* 2110:1922 */     this.pierButton.setEnabled(!selected);
/* 2111:1923 */     this.oneAnchorageButton.setEnabled(!selected);
/* 2112:1924 */     this.twoAnchoragesButton.setEnabled(!selected);
/* 2113:1925 */     endUpdate();
/* 2114:     */   }
/* 2115:     */   
/* 2116:     */   private void pierButtonItemStateChanged(ItemEvent evt)
/* 2117:     */   {
/* 2118:1929 */     beginUpdate();
/* 2119:1930 */     boolean isSelected = evt.getStateChange() == 1;
/* 2120:1931 */     this.pierHeightLabel.setEnabled(isSelected);
/* 2121:1932 */     this.pierHeightBox.setEnabled(isSelected);
/* 2122:1933 */     this.oneAnchorageButton.setEnabled(!isSelected);
/* 2123:1934 */     if ((isSelected) && 
/* 2124:1935 */       (this.oneAnchorageButton.isSelected())) {
/* 2125:1936 */       this.noAnchorageButton.setSelected(true);
/* 2126:     */     }
/* 2127:1940 */     this.pages[this.currentPage].updateLegend();
/* 2128:1941 */     endUpdate();
/* 2129:     */   }
/* 2130:     */   
/* 2131:     */   private void noAnchorageButtonItemStateChanged(ItemEvent evt)
/* 2132:     */   {
/* 2133:1945 */     if (evt.getStateChange() == 1) {
/* 2134:1946 */       update();
/* 2135:     */     }
/* 2136:     */   }
/* 2137:     */   
/* 2138:     */   private void oneAnchorageButtonItemStateChanged(ItemEvent evt)
/* 2139:     */   {
/* 2140:1951 */     if (evt.getStateChange() == 1) {
/* 2141:1952 */       update();
/* 2142:     */     }
/* 2143:     */   }
/* 2144:     */   
/* 2145:     */   private void twoAnchoragesButtonItemStateChanged(ItemEvent evt)
/* 2146:     */   {
/* 2147:1957 */     if (evt.getStateChange() == 1) {
/* 2148:1958 */       update();
/* 2149:     */     }
/* 2150:     */   }
/* 2151:     */   
/* 2152:     */   private void mediumConcreteButtonItemStateChanged(ItemEvent evt)
/* 2153:     */   {
/* 2154:1963 */     if (evt.getStateChange() == 1) {
/* 2155:1964 */       update();
/* 2156:     */     }
/* 2157:     */   }
/* 2158:     */   
/* 2159:     */   private void highConcreteButtonItemStateChanged(ItemEvent evt)
/* 2160:     */   {
/* 2161:1969 */     if (evt.getStateChange() == 1) {
/* 2162:1970 */       update();
/* 2163:     */     }
/* 2164:     */   }
/* 2165:     */   
/* 2166:     */   private void standardTruckButtonItemStateChanged(ItemEvent evt)
/* 2167:     */   {
/* 2168:1975 */     if (evt.getStateChange() == 1) {
/* 2169:1976 */       update();
/* 2170:     */     }
/* 2171:     */   }
/* 2172:     */   
/* 2173:     */   private void permitLoadButtonItemStateChanged(ItemEvent evt)
/* 2174:     */   {
/* 2175:1981 */     if (evt.getStateChange() == 1) {
/* 2176:1982 */       update();
/* 2177:     */     }
/* 2178:     */   }
/* 2179:     */   
/* 2180:     */   private void templateListValueChanged(ListSelectionEvent evt)
/* 2181:     */   {
/* 2182:1987 */     this.bridgeCartoonView.getBridgeSketchView().setModel(this.templateList.getSelectedIndex() == 0 ? null : (BridgeSketchModel)this.templateList.getSelectedValue());
/* 2183:1988 */     this.elevationViewLabel.repaint();
/* 2184:     */   }
/* 2185:     */   
/* 2186:     */   private void localContest4YesButtonItemStateChanged(ItemEvent evt)
/* 2187:     */   {
/* 2188:1992 */     this.localContest4charOKMsgLabel.setVisible(this.localContest4YesButton.isSelected());
/* 2189:     */   }
/* 2190:     */   
/* 2191:     */   private void dropRaiseButtonActionPerformed(ActionEvent evt)
/* 2192:     */   {
/* 2193:1996 */     showDetailPane(!this.siteCostDetailTable.isVisible());
/* 2194:     */   }
/* 2195:     */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.SetupWizard
 * JD-Core Version:    0.7.0.1
 */