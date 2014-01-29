/*    1:     */ package wpbd;
/*    2:     */ 
/*    3:     */ import java.io.FileOutputStream;
/*    4:     */ import java.io.IOException;
/*    5:     */ import java.io.OutputStreamWriter;
/*    6:     */ import java.io.PrintStream;
/*    7:     */ import java.io.Writer;
/*    8:     */ import java.util.Arrays;
/*    9:     */ import java.util.Comparator;
/*   10:     */ import java.util.HashMap;
/*   11:     */ import java.util.logging.Level;
/*   12:     */ import java.util.logging.Logger;
/*   13:     */ 
/*   14:     */ public class DesignConditions
/*   15:     */   implements Cloneable
/*   16:     */ {
/*   17:     */   private final String tag;
/*   18:     */   private final byte[] code;
/*   19:     */   private long codeLong;
/*   20:     */   private boolean hiPier;
/*   21:     */   private int leftAnchorageJointIndex;
/*   22:     */   private int rightAnchorageJointIndex;
/*   23:     */   private int pierPanelIndex;
/*   24:     */   private double underClearance;
/*   25:     */   private double overClearance;
/*   26:     */   private double overMargin;
/*   27:     */   private int nPanels;
/*   28:     */   private int nPrescribedJoints;
/*   29:     */   private int loadType;
/*   30:     */   private int deckType;
/*   31:     */   private double deckElevation;
/*   32:     */   private double archHeight;
/*   33:     */   private double pierHeight;
/*   34:     */   private int nAnchorages;
/*   35:     */   private double excavationVolume;
/*   36:     */   private double abutmentCost;
/*   37:     */   private double pierCost;
/*   38:     */   private double deckCostRate;
/*   39:     */   private double totalFixedCost;
/*   40:     */   private int pierJointIndex;
/*   41:     */   private int archJointIndex;
/*   42:     */   private double spanLength;
/*   43:     */   private int nLoadedJoints;
/*   44:     */   private int nJointRestraints;
/*   45:     */   private Joint[] prescribedJoints;
/*   46:     */   private int[] abutmentJointIndices;
/*   47:     */   private double xLeftmostDeckJoint;
/*   48:     */   private double xRightmostDeckJoint;
/*   49:     */   private double allowableSlenderness;
/*   50:     */   public static final double anchorOffset = 8.0D;
/*   51:     */   public static final double panelSizeWorld = 4.0D;
/*   52:     */   public static final double gapDepth = 24.0D;
/*   53:     */   public static final double minOverhead = 8.0D;
/*   54:     */   public static final double maxSlenderness = 300.0D;
/*   55:     */   public static final int STANDARD_TRUCK = 0;
/*   56:     */   public static final int HEAVY_TRUCK = 1;
/*   57:     */   public static final int MEDIUM_STRENGTH_DECK = 0;
/*   58:     */   public static final int HI_STRENGTH_DECK = 1;
/*   59:     */   public static final int maxJointCount = 100;
/*   60:     */   public static final int maxMemberCount = 200;
/*   61:     */   public static final String fromKeyCodeTag = "99Z";
/*   62:     */   public static final double excavationCostRate = 1.0D;
/*   63:     */   public static final double anchorageCost = 6000.0D;
/*   64:     */   public static final double deckCostPerPanelMedStrength = 4700.0D;
/*   65:     */   public static final double deckCostPerPanelHiStrength = 5000.0D;
/*   66:     */   public static final double standardAbutmentBaseCost = 5500.0D;
/*   67:     */   public static final double standardAbutmentIncrementalCostPerDeckPanel = 500.0D;
/*   68:     */   public static final double archIncrementalCostPerDeckPanel = 3600.0D;
/*   69:     */   public static final double pierIncrementalCostPerDeckPanel = 4500.0D;
/*   70:     */   public static final double pierBaseCost = 3000.0D;
/*   71: 183 */   public static final double[] deckElevationIndexToExcavationVolume = { 100000.0D, 85000.0D, 67000.0D, 50000.0D, 34000.0D, 15000.0D, 0.0D };
/*   72: 188 */   public static final double[] deckElevationIndexToKeycodeAbutmentCosts = { 7000.0D, 7000.0D, 7500.0D, 7500.0D, 8000.0D, 8000.0D, 8500.0D };
/*   73:     */   
/*   74:     */   public double getAbutmentCost()
/*   75:     */   {
/*   76: 196 */     return this.abutmentCost;
/*   77:     */   }
/*   78:     */   
/*   79:     */   public boolean isArch()
/*   80:     */   {
/*   81: 205 */     return this.archHeight >= 0.0D;
/*   82:     */   }
/*   83:     */   
/*   84:     */   public double getArchHeight()
/*   85:     */   {
/*   86: 214 */     return this.archHeight;
/*   87:     */   }
/*   88:     */   
/*   89:     */   public byte[] getCode()
/*   90:     */   {
/*   91: 223 */     return this.code;
/*   92:     */   }
/*   93:     */   
/*   94:     */   public String getCodeString()
/*   95:     */   {
/*   96: 232 */     return String.format("%010d", new Object[] { Long.valueOf(this.codeLong) });
/*   97:     */   }
/*   98:     */   
/*   99:     */   public long getCodeLong()
/*  100:     */   {
/*  101: 241 */     return this.codeLong;
/*  102:     */   }
/*  103:     */   
/*  104:     */   public double getDeckCostRate()
/*  105:     */   {
/*  106: 250 */     return this.deckCostRate;
/*  107:     */   }
/*  108:     */   
/*  109:     */   public double getDeckElevation()
/*  110:     */   {
/*  111: 259 */     return this.deckElevation;
/*  112:     */   }
/*  113:     */   
/*  114:     */   public int getDeckType()
/*  115:     */   {
/*  116: 269 */     return this.deckType;
/*  117:     */   }
/*  118:     */   
/*  119:     */   public double getDeckThickness()
/*  120:     */   {
/*  121: 278 */     return this.deckType == 0 ? 0.23D : 0.15D;
/*  122:     */   }
/*  123:     */   
/*  124:     */   public double getExcavationVolume()
/*  125:     */   {
/*  126: 287 */     return this.excavationVolume;
/*  127:     */   }
/*  128:     */   
/*  129:     */   public double getExcavationCost()
/*  130:     */   {
/*  131: 296 */     return this.excavationVolume * 1.0D;
/*  132:     */   }
/*  133:     */   
/*  134:     */   public boolean isPier()
/*  135:     */   {
/*  136: 305 */     return this.pierPanelIndex >= 0;
/*  137:     */   }
/*  138:     */   
/*  139:     */   public boolean isHiPier()
/*  140:     */   {
/*  141: 314 */     return this.hiPier;
/*  142:     */   }
/*  143:     */   
/*  144:     */   public boolean isLeftAnchorage()
/*  145:     */   {
/*  146: 323 */     return this.leftAnchorageJointIndex >= 0;
/*  147:     */   }
/*  148:     */   
/*  149:     */   public boolean isRightAnchorage()
/*  150:     */   {
/*  151: 332 */     return this.rightAnchorageJointIndex >= 0;
/*  152:     */   }
/*  153:     */   
/*  154:     */   public int getLoadType()
/*  155:     */   {
/*  156: 342 */     return this.loadType;
/*  157:     */   }
/*  158:     */   
/*  159:     */   public int getNAnchorages()
/*  160:     */   {
/*  161: 351 */     return this.nAnchorages;
/*  162:     */   }
/*  163:     */   
/*  164:     */   public int getNPanels()
/*  165:     */   {
/*  166: 360 */     return this.nPanels;
/*  167:     */   }
/*  168:     */   
/*  169:     */   public int getNPrescribedJoints()
/*  170:     */   {
/*  171: 369 */     return this.nPrescribedJoints;
/*  172:     */   }
/*  173:     */   
/*  174:     */   public double getOverClearance()
/*  175:     */   {
/*  176: 378 */     return this.overClearance;
/*  177:     */   }
/*  178:     */   
/*  179:     */   public double getOverMargin()
/*  180:     */   {
/*  181: 387 */     return this.overMargin;
/*  182:     */   }
/*  183:     */   
/*  184:     */   public double getPierCost()
/*  185:     */   {
/*  186: 396 */     return this.pierCost;
/*  187:     */   }
/*  188:     */   
/*  189:     */   public double getPierHeight()
/*  190:     */   {
/*  191: 405 */     return this.pierHeight;
/*  192:     */   }
/*  193:     */   
/*  194:     */   public int getPierPanelIndex()
/*  195:     */   {
/*  196: 414 */     return this.pierPanelIndex;
/*  197:     */   }
/*  198:     */   
/*  199:     */   public String getTag()
/*  200:     */   {
/*  201: 423 */     return this.tag;
/*  202:     */   }
/*  203:     */   
/*  204:     */   public double getTotalFixedCost()
/*  205:     */   {
/*  206: 432 */     return this.totalFixedCost;
/*  207:     */   }
/*  208:     */   
/*  209:     */   public double getUnderClearance()
/*  210:     */   {
/*  211: 441 */     return this.underClearance;
/*  212:     */   }
/*  213:     */   
/*  214:     */   public boolean isAtGrade()
/*  215:     */   {
/*  216: 450 */     return this.deckElevation == 24.0D;
/*  217:     */   }
/*  218:     */   
/*  219:     */   public int getLeftAnchorageJointIndex()
/*  220:     */   {
/*  221: 459 */     return this.leftAnchorageJointIndex;
/*  222:     */   }
/*  223:     */   
/*  224:     */   public int getRightAnchorageJointIndex()
/*  225:     */   {
/*  226: 468 */     return this.rightAnchorageJointIndex;
/*  227:     */   }
/*  228:     */   
/*  229:     */   public int getArchJointIndex()
/*  230:     */   {
/*  231: 477 */     return this.archJointIndex;
/*  232:     */   }
/*  233:     */   
/*  234:     */   public int getPierJointIndex()
/*  235:     */   {
/*  236: 486 */     return this.pierJointIndex;
/*  237:     */   }
/*  238:     */   
/*  239:     */   public int[] getAbutmentJointIndices()
/*  240:     */   {
/*  241: 495 */     return this.abutmentJointIndices;
/*  242:     */   }
/*  243:     */   
/*  244:     */   public double getXLeftmostDeckJoint()
/*  245:     */   {
/*  246: 504 */     return this.xLeftmostDeckJoint;
/*  247:     */   }
/*  248:     */   
/*  249:     */   public double getXRightmostDeckJoint()
/*  250:     */   {
/*  251: 513 */     return this.xRightmostDeckJoint;
/*  252:     */   }
/*  253:     */   
/*  254:     */   public Joint getPrescribedJoint(int i)
/*  255:     */   {
/*  256: 523 */     return this.prescribedJoints[i];
/*  257:     */   }
/*  258:     */   
/*  259:     */   public Affine.Point getPrescribedJointLocation(int i)
/*  260:     */   {
/*  261: 533 */     return this.prescribedJoints[i].getPointWorld();
/*  262:     */   }
/*  263:     */   
/*  264:     */   public int getNLoadedJoints()
/*  265:     */   {
/*  266: 542 */     return this.nLoadedJoints;
/*  267:     */   }
/*  268:     */   
/*  269:     */   public double getSpanLength()
/*  270:     */   {
/*  271: 551 */     return this.spanLength;
/*  272:     */   }
/*  273:     */   
/*  274:     */   public int getNJointRestraints()
/*  275:     */   {
/*  276: 560 */     return this.nJointRestraints;
/*  277:     */   }
/*  278:     */   
/*  279:     */   public double getAllowableSlenderness()
/*  280:     */   {
/*  281: 570 */     return this.allowableSlenderness;
/*  282:     */   }
/*  283:     */   
/*  284:     */   public static DesignConditions fromKeyCode(String keyCode)
/*  285:     */   {
/*  286: 582 */     long codeLong = 0L;
/*  287:     */     try
/*  288:     */     {
/*  289: 584 */       codeLong = Long.parseLong(keyCode);
/*  290:     */     }
/*  291:     */     catch (NumberFormatException ex)
/*  292:     */     {
/*  293: 587 */       return null;
/*  294:     */     }
/*  295: 589 */     int errCode = getCodeError(getCode(codeLong));
/*  296: 590 */     if (errCode != 0) {
/*  297: 591 */       return null;
/*  298:     */     }
/*  299: 593 */     return getDesignConditions(codeLong);
/*  300:     */   }
/*  301:     */   
/*  302:     */   public final boolean isFromKeyCode()
/*  303:     */   {
/*  304: 602 */     return this.tag == "99Z";
/*  305:     */   }
/*  306:     */   
/*  307:     */   private static boolean inRange(int b, int lo, int hi)
/*  308:     */   {
/*  309: 606 */     return (lo <= b) && (b <= hi);
/*  310:     */   }
/*  311:     */   
/*  312:     */   private static int getCodeError(byte[] code)
/*  313:     */   {
/*  314: 626 */     if (code == null) {
/*  315: 627 */       return -1;
/*  316:     */     }
/*  317: 630 */     if (!inRange(code[0], 1, 4)) {
/*  318: 631 */       return 1;
/*  319:     */     }
/*  320: 634 */     int nPanels = 10 * code[1] + code[2];
/*  321: 635 */     if (!inRange(nPanels, 1, 20)) {
/*  322: 636 */       return 2;
/*  323:     */     }
/*  324: 639 */     int over = 10 * code[3] + code[4];
/*  325: 640 */     if (!inRange(over, 0, 40)) {
/*  326: 641 */       return 4;
/*  327:     */     }
/*  328: 643 */     if (!inRange(code[9], 0, 1)) {
/*  329: 644 */       return 10;
/*  330:     */     }
/*  331: 647 */     int under = 10 * code[5] + code[6];
/*  332: 648 */     if (!inRange(under, 0, 32)) {
/*  333: 649 */       return 6;
/*  334:     */     }
/*  335: 653 */     if (!inRange(code[7], 0, 3)) {
/*  336: 654 */       return 8;
/*  337:     */     }
/*  338: 656 */     boolean arch = code[7] == 1;
/*  339:     */     
/*  340: 658 */     int pierPanelIndex = code[8] - 1;
/*  341: 659 */     boolean pier = pierPanelIndex >= 0;
/*  342:     */     
/*  343: 661 */     boolean hiPier = code[9] > 0;
/*  344: 664 */     if ((hiPier) && (!pier)) {
/*  345: 665 */       return 90;
/*  346:     */     }
/*  347: 667 */     if (pierPanelIndex >= nPanels) {
/*  348: 668 */       return 91;
/*  349:     */     }
/*  350: 673 */     if ((nPanels < 5) || (nPanels > 11)) {
/*  351: 674 */       return 92;
/*  352:     */     }
/*  353: 679 */     double deckElev = arch ? 4 * (nPanels - 5) + under : 4 * (nPanels - 5);
/*  354: 680 */     if ((deckElev < 0.0D) || (deckElev > 24.0D)) {
/*  355: 681 */       return 93;
/*  356:     */     }
/*  357: 687 */     if (deckElev + over > 32.0D) {
/*  358: 688 */       return 94;
/*  359:     */     }
/*  360: 694 */     if ((!arch) && (deckElev - under < 0.0D)) {
/*  361: 695 */       return 95;
/*  362:     */     }
/*  363: 700 */     if (((pier) && (pierPanelIndex == 0)) || (pierPanelIndex >= nPanels - 1)) {
/*  364: 701 */       return 96;
/*  365:     */     }
/*  366: 705 */     if ((arch) && (pier)) {
/*  367: 706 */       return 97;
/*  368:     */     }
/*  369: 718 */     if ((pier) && (!hiPier))
/*  370:     */     {
/*  371: 719 */       double xp = pierPanelIndex * 4.0D;
/*  372: 720 */       double yp = deckElev - under;
/*  373: 721 */       double xL = 0.0D;
/*  374: 722 */       double yL = deckElev;
/*  375: 723 */       double xR = nPanels * 4.0D;
/*  376: 724 */       double yR = yL;
/*  377: 725 */       if (xp < 0.0D + (yL - yp) * 0.5D) {
/*  378: 726 */         return 98;
/*  379:     */       }
/*  380: 728 */       if (xp > xR - (yR - yp) * 0.5D) {
/*  381: 729 */         return 99;
/*  382:     */       }
/*  383:     */     }
/*  384: 732 */     return 0;
/*  385:     */   }
/*  386:     */   
/*  387:     */   private static byte[] getCode(long codeLong)
/*  388:     */   {
/*  389: 736 */     if (codeLong < 0L) {
/*  390: 737 */       return null;
/*  391:     */     }
/*  392: 739 */     byte[] rtnCode = new byte[10];
/*  393: 740 */     for (int i = 9; i >= 0; i--)
/*  394:     */     {
/*  395: 741 */       rtnCode[i] = ((byte)(int)(codeLong % 10L));
/*  396: 742 */       codeLong /= 10L;
/*  397:     */     }
/*  398: 744 */     if (codeLong > 0L) {
/*  399: 745 */       return null;
/*  400:     */     }
/*  401: 747 */     return rtnCode;
/*  402:     */   }
/*  403:     */   
/*  404:     */   public boolean isGeometricallyIdentical(DesignConditions other)
/*  405:     */   {
/*  406: 758 */     if (other == null) {
/*  407: 759 */       return false;
/*  408:     */     }
/*  409: 762 */     for (int i = 1; i < this.code.length; i++) {
/*  410: 763 */       if (this.code[i] != other.code[i]) {
/*  411: 764 */         return false;
/*  412:     */       }
/*  413:     */     }
/*  414: 767 */     return true;
/*  415:     */   }
/*  416:     */   
/*  417: 770 */   private static double[] underClearanceIndexToCost = { -2000.0D, 5400.0D, 15000.0D, 24400.0D, 35500.0D, 49700.0D };
/*  418:     */   
/*  419:     */   private double archCost(double underClearance)
/*  420:     */   {
/*  421: 776 */     return underClearanceIndexToCost[((int)underClearance / 4 - 1)];
/*  422:     */   }
/*  423:     */   
/*  424: 779 */   private static double[] pierHeightToCost = { 0.0D, 2800.0D, 5600.0D, 8400.0D, 11200.0D, 14000.0D, 16800.0D };
/*  425:     */   
/*  426:     */   private double pierHeightCost(double pierHeight)
/*  427:     */   {
/*  428: 784 */     return pierHeightToCost[((int)pierHeight / 4)];
/*  429:     */   }
/*  430:     */   
/*  431:     */   private DesignConditions(String tag, long codeLong)
/*  432:     */   {
/*  433: 794 */     this.tag = tag;
/*  434: 795 */     this.codeLong = codeLong;
/*  435: 796 */     this.code = getCode(codeLong);
/*  436: 797 */     if (getCodeError(this.code) != 0) {
/*  437: 798 */       return;
/*  438:     */     }
/*  439: 802 */     this.hiPier = (this.code[9] > 0);
/*  440:     */     
/*  441: 804 */     this.pierJointIndex = (this.pierPanelIndex = this.code[8] - 1);
/*  442: 805 */     boolean pier = this.pierPanelIndex >= 0;
/*  443:     */     
/*  444: 807 */     boolean arch = this.code[7] == 1;
/*  445: 808 */     boolean leftCable = (this.code[7] == 2) || (this.code[7] == 3);
/*  446: 809 */     boolean rightCable = this.code[7] == 3;
/*  447:     */     
/*  448: 811 */     this.underClearance = (10 * this.code[5] + this.code[6]);
/*  449:     */     
/*  450: 813 */     this.overClearance = (10 * this.code[3] + this.code[4]);
/*  451:     */     
/*  452: 815 */     this.nPanels = (10 * this.code[1] + this.code[2]);
/*  453:     */     
/*  454:     */ 
/*  455: 818 */     int loadCaseIndex = this.code[0] - 1;
/*  456: 819 */     this.loadType = ((loadCaseIndex & 0x1) == 0 ? 0 : 1);
/*  457: 820 */     this.deckType = ((loadCaseIndex & 0x2) == 0 ? 0 : 1);
/*  458: 827 */     if (arch)
/*  459:     */     {
/*  460: 828 */       this.deckElevation = (4 * (this.nPanels - 5) + this.underClearance);
/*  461: 829 */       this.archHeight = this.underClearance;
/*  462:     */     }
/*  463:     */     else
/*  464:     */     {
/*  465: 832 */       this.deckElevation = (4 * (this.nPanels - 5));
/*  466: 833 */       this.archHeight = -1.0D;
/*  467:     */     }
/*  468: 835 */     this.overMargin = (32.0D - this.deckElevation);
/*  469: 836 */     this.pierHeight = (pier ? this.deckElevation - this.underClearance : this.hiPier ? this.deckElevation : -1.0D);
/*  470:     */     
/*  471:     */ 
/*  472:     */ 
/*  473:     */ 
/*  474: 841 */     this.nPrescribedJoints = (this.nPanels + 1);
/*  475: 842 */     this.archJointIndex = (this.leftAnchorageJointIndex = this.rightAnchorageJointIndex = -1);
/*  476: 844 */     if ((pier) && (!this.hiPier))
/*  477:     */     {
/*  478: 845 */       this.pierJointIndex = this.nPrescribedJoints;
/*  479: 846 */       this.nPrescribedJoints += 1;
/*  480:     */     }
/*  481: 850 */     if (arch)
/*  482:     */     {
/*  483: 851 */       this.archJointIndex = this.nPrescribedJoints;
/*  484: 852 */       this.nPrescribedJoints += 2;
/*  485:     */     }
/*  486: 855 */     this.nAnchorages = 0;
/*  487: 856 */     if (leftCable)
/*  488:     */     {
/*  489: 857 */       this.leftAnchorageJointIndex = this.nPrescribedJoints;
/*  490: 858 */       this.nAnchorages += 1;
/*  491: 859 */       this.nPrescribedJoints += 1;
/*  492:     */     }
/*  493: 861 */     if (rightCable)
/*  494:     */     {
/*  495: 862 */       assert (leftCable);
/*  496: 863 */       this.rightAnchorageJointIndex = this.nPrescribedJoints;
/*  497: 864 */       this.nAnchorages += 1;
/*  498: 865 */       this.nPrescribedJoints += 1;
/*  499:     */     }
/*  500: 868 */     this.spanLength = (this.nPanels * 4.0D);
/*  501: 869 */     this.nLoadedJoints = (this.nPanels + 1);
/*  502: 870 */     this.prescribedJoints = new Joint[this.nPrescribedJoints];
/*  503:     */     
/*  504:     */ 
/*  505: 873 */     double x = 0.0D;
/*  506: 874 */     double y = 0.0D;
/*  507: 875 */     for (int i = 0; i < this.nLoadedJoints; i++)
/*  508:     */     {
/*  509: 876 */       this.prescribedJoints[i] = new Joint(i, new Affine.Point(x, y), true);
/*  510: 877 */       x += 4.0D;
/*  511:     */     }
/*  512: 879 */     this.xLeftmostDeckJoint = this.prescribedJoints[0].getPointWorld().x;
/*  513: 880 */     this.xRightmostDeckJoint = this.prescribedJoints[(this.nLoadedJoints - 1)].getPointWorld().x;
/*  514:     */     
/*  515: 882 */     this.nJointRestraints = 3;
/*  516: 883 */     if (pier) {
/*  517: 884 */       if (this.hiPier)
/*  518:     */       {
/*  519: 886 */         this.nJointRestraints += 1;
/*  520:     */       }
/*  521:     */       else
/*  522:     */       {
/*  523: 889 */         this.prescribedJoints[i] = new Joint(i, new Affine.Point(this.pierPanelIndex * 4.0D, -this.underClearance), true);
/*  524: 890 */         i++;
/*  525: 891 */         this.nJointRestraints += 2;
/*  526:     */       }
/*  527:     */     }
/*  528: 894 */     if (arch)
/*  529:     */     {
/*  530: 895 */       this.prescribedJoints[i] = new Joint(i, new Affine.Point(this.xLeftmostDeckJoint, -this.underClearance), true);
/*  531: 896 */       i++;
/*  532: 897 */       this.prescribedJoints[i] = new Joint(i, new Affine.Point(this.xRightmostDeckJoint, -this.underClearance), true);
/*  533: 898 */       i++;
/*  534:     */       
/*  535: 900 */       this.nJointRestraints += 1;
/*  536:     */     }
/*  537: 902 */     if (leftCable)
/*  538:     */     {
/*  539: 903 */       this.prescribedJoints[i] = new Joint(i, new Affine.Point(this.xLeftmostDeckJoint - 8.0D, 0.0D), true);
/*  540: 904 */       i++;
/*  541: 905 */       this.nJointRestraints += 2;
/*  542:     */     }
/*  543: 907 */     if (rightCable)
/*  544:     */     {
/*  545: 908 */       this.prescribedJoints[i] = new Joint(i, new Affine.Point(this.xRightmostDeckJoint + 8.0D, 0.0D), true);
/*  546: 909 */       i++;
/*  547: 910 */       this.nJointRestraints += 2;
/*  548:     */     }
/*  549: 914 */     this.allowableSlenderness = ((leftCable) || (rightCable) ? 1.0E+100D : 300.0D);
/*  550:     */     
/*  551:     */ 
/*  552: 917 */     this.excavationVolume = deckElevationIndexToExcavationVolume[((int)this.deckElevation / 4)];
/*  553: 918 */     this.deckCostRate = (this.deckType == 0 ? 4700.0D : 5000.0D);
/*  554: 921 */     if (isFromKeyCode())
/*  555:     */     {
/*  556: 923 */       this.totalFixedCost = 170000.0D;
/*  557: 924 */       if (pier)
/*  558:     */       {
/*  559: 925 */         this.abutmentCost = deckElevationIndexToKeycodeAbutmentCosts[((int)this.deckElevation / 4)];
/*  560: 926 */         this.pierCost = (this.totalFixedCost - this.nPanels * this.deckCostRate - this.excavationVolume * 1.0D - this.abutmentCost - this.nAnchorages * 6000.0D);
/*  561:     */       }
/*  562:     */       else
/*  563:     */       {
/*  564: 933 */         this.abutmentCost = (this.totalFixedCost - this.nPanels * this.deckCostRate - this.excavationVolume * 1.0D - this.nAnchorages * 6000.0D);
/*  565:     */         
/*  566:     */ 
/*  567:     */ 
/*  568: 937 */         this.pierCost = 0.0D;
/*  569:     */       }
/*  570:     */     }
/*  571:     */     else
/*  572:     */     {
/*  573: 942 */       this.abutmentCost = (pier ? 5500.0D + Math.max(this.pierPanelIndex, this.nPanels - this.pierPanelIndex) * 500.0D : arch ? this.nPanels * 3600.0D + archCost(this.underClearance) : 5500.0D + this.nPanels * 500.0D);
/*  574:     */       
/*  575:     */ 
/*  576:     */ 
/*  577:     */ 
/*  578:     */ 
/*  579:     */ 
/*  580: 949 */       this.pierCost = (pier ? Math.max(this.pierPanelIndex, this.nPanels - this.pierPanelIndex) * 4500.0D + pierHeightCost(this.pierHeight) + 3000.0D : 0.0D);
/*  581:     */       
/*  582:     */ 
/*  583:     */ 
/*  584: 953 */       this.totalFixedCost = (this.excavationVolume * 1.0D + this.abutmentCost + this.pierCost + this.nPanels * this.deckCostRate + this.nAnchorages * 6000.0D);
/*  585:     */     }
/*  586: 959 */     this.abutmentCost *= 0.5D;
/*  587:     */     
/*  588:     */ 
/*  589: 962 */     this.abutmentJointIndices = new int[] { 0, pier ? new int[] { 0, this.nPanels, this.pierJointIndex } : arch ? new int[] { 0, this.nPanels, this.archJointIndex, this.archJointIndex + 1 } : this.nPanels };
/*  590:     */   }
/*  591:     */   
/*  592:     */   public String toString()
/*  593:     */   {
/*  594: 970 */     return "[" + this.tag + "," + this.codeLong + ",hiPier=" + this.hiPier + ",leftCableIndex=" + this.leftAnchorageJointIndex + ",rightCableIndex=" + this.rightAnchorageJointIndex + ",pierJointIndex=" + this.pierPanelIndex + ",underClearance=" + this.underClearance + ",overClearance=" + this.overClearance + ",nPanels=" + this.nPanels + ",loadType=" + this.loadType + ",deckType=" + this.deckType + ",deckElevation=" + this.deckElevation + ",archHeight=" + this.archHeight + ",pierHeight=" + this.pierHeight + ",nAnchorages=" + this.nAnchorages + ",excavationVolume=" + this.excavationVolume + ",abutmentCost=" + this.abutmentCost + ",pierCost=" + this.pierCost + ",deckCostRate=" + this.deckCostRate + ",totalFixedCost=" + this.totalFixedCost + "]";
/*  595:     */   }
/*  596:     */   
/*  597: 994 */   public static final DesignConditions[] conditions = { new DesignConditions("01A", 1110824000L), new DesignConditions("01B", 2110824000L), new DesignConditions("01C", 3110824000L), new DesignConditions("01D", 4110824000L), new DesignConditions("02A", 1101220000L), new DesignConditions("02B", 2101220000L), new DesignConditions("02C", 3101220000L), new DesignConditions("02D", 4101220000L), new DesignConditions("03A", 1091616000L), new DesignConditions("03B", 2091616000L), new DesignConditions("03C", 3091616000L), new DesignConditions("03D", 4091616000L), new DesignConditions("04A", 1082012000L), new DesignConditions("04B", 2082012000L), new DesignConditions("04C", 3082012000L), new DesignConditions("04D", 4082012000L), new DesignConditions("05A", 1072408000L), new DesignConditions("05B", 2072408000L), new DesignConditions("05C", 3072408000L), new DesignConditions("05D", 4072408000L), new DesignConditions("06A", 1062804000L), new DesignConditions("06B", 2062804000L), new DesignConditions("06C", 3062804000L), new DesignConditions("06D", 4062804000L), new DesignConditions("07A", 1053200000L), new DesignConditions("07B", 2053200000L), new DesignConditions("07C", 3053200000L), new DesignConditions("07D", 4053200000L), new DesignConditions("08A", 1110824200L), new DesignConditions("08B", 2110824200L), new DesignConditions("08C", 3110824200L), new DesignConditions("08D", 4110824200L), new DesignConditions("09A", 1101220200L), new DesignConditions("09B", 2101220200L), new DesignConditions("09C", 3101220200L), new DesignConditions("09D", 4101220200L), new DesignConditions("10A", 1091616200L), new DesignConditions("10B", 2091616200L), new DesignConditions("10C", 3091616200L), new DesignConditions("10D", 4091616200L), new DesignConditions("11A", 1082012200L), new DesignConditions("11B", 2082012200L), new DesignConditions("11C", 3082012200L), new DesignConditions("11D", 4082012200L), new DesignConditions("12A", 1072408200L), new DesignConditions("12B", 2072408200L), new DesignConditions("12C", 3072408200L), new DesignConditions("12D", 4072408200L), new DesignConditions("13A", 1062804200L), new DesignConditions("13B", 2062804200L), new DesignConditions("13C", 3062804200L), new DesignConditions("13D", 4062804200L), new DesignConditions("14A", 1053200200L), new DesignConditions("14B", 2053200200L), new DesignConditions("14C", 3053200200L), new DesignConditions("14D", 4053200200L), new DesignConditions("15A", 1110824300L), new DesignConditions("15B", 2110824300L), new DesignConditions("15C", 3110824300L), new DesignConditions("15D", 4110824300L), new DesignConditions("16A", 1101220300L), new DesignConditions("16B", 2101220300L), new DesignConditions("16C", 3101220300L), new DesignConditions("16D", 4101220300L), new DesignConditions("17A", 1091616300L), new DesignConditions("17B", 2091616300L), new DesignConditions("17C", 3091616300L), new DesignConditions("17D", 4091616300L), new DesignConditions("18A", 1082012300L), new DesignConditions("18B", 2082012300L), new DesignConditions("18C", 3082012300L), new DesignConditions("18D", 4082012300L), new DesignConditions("19A", 1072408300L), new DesignConditions("19B", 2072408300L), new DesignConditions("19C", 3072408300L), new DesignConditions("19D", 4072408300L), new DesignConditions("20A", 1062804300L), new DesignConditions("20B", 2062804300L), new DesignConditions("20C", 3062804300L), new DesignConditions("20D", 4062804300L), new DesignConditions("21A", 1053200300L), new DesignConditions("21B", 2053200300L), new DesignConditions("21C", 3053200300L), new DesignConditions("21D", 4053200300L), new DesignConditions("22A", 1100804100L), new DesignConditions("22B", 2100804100L), new DesignConditions("22C", 3100804100L), new DesignConditions("22D", 4100804100L), new DesignConditions("23A", 1090808100L), new DesignConditions("23B", 2090808100L), new DesignConditions("23C", 3090808100L), new DesignConditions("23D", 4090808100L), new DesignConditions("24A", 1080812100L), new DesignConditions("24B", 2080812100L), new DesignConditions("24C", 3080812100L), new DesignConditions("24D", 4080812100L), new DesignConditions("25A", 1070816100L), new DesignConditions("25B", 2070816100L), new DesignConditions("25C", 3070816100L), new DesignConditions("25D", 4070816100L), new DesignConditions("26A", 1060820100L), new DesignConditions("26B", 2060820100L), new DesignConditions("26C", 3060820100L), new DesignConditions("26D", 4060820100L), new DesignConditions("27A", 1050824100L), new DesignConditions("27B", 2050824100L), new DesignConditions("27C", 3050824100L), new DesignConditions("27D", 4050824100L), new DesignConditions("28A", 1091204100L), new DesignConditions("28B", 2091204100L), new DesignConditions("28C", 3091204100L), new DesignConditions("28D", 4091204100L), new DesignConditions("29A", 1081208100L), new DesignConditions("29B", 2081208100L), new DesignConditions("29C", 3081208100L), new DesignConditions("29D", 4081208100L), new DesignConditions("30A", 1071212100L), new DesignConditions("30B", 2071212100L), new DesignConditions("30C", 3071212100L), new DesignConditions("30D", 4071212100L), new DesignConditions("31A", 1061216100L), new DesignConditions("31B", 2061216100L), new DesignConditions("31C", 3061216100L), new DesignConditions("31D", 4061216100L), new DesignConditions("32A", 1051220100L), new DesignConditions("32B", 2051220100L), new DesignConditions("32C", 3051220100L), new DesignConditions("32D", 4051220100L), new DesignConditions("33A", 1081604100L), new DesignConditions("33B", 2081604100L), new DesignConditions("33C", 3081604100L), new DesignConditions("33D", 4081604100L), new DesignConditions("34A", 1071608100L), new DesignConditions("34B", 2071608100L), new DesignConditions("34C", 3071608100L), new DesignConditions("34D", 4071608100L), new DesignConditions("35A", 1061612100L), new DesignConditions("35B", 2061612100L), new DesignConditions("35C", 3061612100L), new DesignConditions("35D", 4061612100L), new DesignConditions("36A", 1051616100L), new DesignConditions("36B", 2051616100L), new DesignConditions("36C", 3051616100L), new DesignConditions("36D", 4051616100L), new DesignConditions("37A", 1072004100L), new DesignConditions("37B", 2072004100L), new DesignConditions("37C", 3072004100L), new DesignConditions("37D", 4072004100L), new DesignConditions("38A", 1062008100L), new DesignConditions("38B", 2062008100L), new DesignConditions("38C", 3062008100L), new DesignConditions("38D", 4062008100L), new DesignConditions("39A", 1052012100L), new DesignConditions("39B", 2052012100L), new DesignConditions("39C", 3052012100L), new DesignConditions("39D", 4052012100L), new DesignConditions("40A", 1062404100L), new DesignConditions("40B", 2062404100L), new DesignConditions("40C", 3062404100L), new DesignConditions("40D", 4062404100L), new DesignConditions("41A", 1052408100L), new DesignConditions("41B", 2052408100L), new DesignConditions("41C", 3052408100L), new DesignConditions("41D", 4052408100L), new DesignConditions("42A", 1052804100L), new DesignConditions("42B", 2052804100L), new DesignConditions("42C", 3052804100L), new DesignConditions("42D", 4052804100L), new DesignConditions("43A", 1110824060L), new DesignConditions("43B", 2110824060L), new DesignConditions("43C", 3110824060L), new DesignConditions("43D", 4110824060L), new DesignConditions("44A", 1110820060L), new DesignConditions("44B", 2110820060L), new DesignConditions("44C", 3110820060L), new DesignConditions("44D", 4110820060L), new DesignConditions("45A", 1110816060L), new DesignConditions("45B", 2110816060L), new DesignConditions("45C", 3110816060L), new DesignConditions("45D", 4110816060L), new DesignConditions("46A", 1110812060L), new DesignConditions("46B", 2110812060L), new DesignConditions("46C", 3110812060L), new DesignConditions("46D", 4110812060L), new DesignConditions("47A", 1110808060L), new DesignConditions("47B", 2110808060L), new DesignConditions("47C", 3110808060L), new DesignConditions("47D", 4110808060L), new DesignConditions("48A", 1110804060L), new DesignConditions("48B", 2110804060L), new DesignConditions("48C", 3110804060L), new DesignConditions("48D", 4110804060L), new DesignConditions("49A", 1110824061L), new DesignConditions("49B", 2110824061L), new DesignConditions("49C", 3110824061L), new DesignConditions("49D", 4110824061L), new DesignConditions("50A", 1101220060L), new DesignConditions("50B", 2101220060L), new DesignConditions("50C", 3101220060L), new DesignConditions("50D", 4101220060L), new DesignConditions("51A", 1101216060L), new DesignConditions("51B", 2101216060L), new DesignConditions("51C", 3101216060L), new DesignConditions("51D", 4101216060L), new DesignConditions("52A", 1101212060L), new DesignConditions("52B", 2101212060L), new DesignConditions("52C", 3101212060L), new DesignConditions("52D", 4101212060L), new DesignConditions("53A", 1101208060L), new DesignConditions("53B", 2101208060L), new DesignConditions("53C", 3101208060L), new DesignConditions("53D", 4101208060L), new DesignConditions("54A", 1101204060L), new DesignConditions("54B", 2101204060L), new DesignConditions("54C", 3101204060L), new DesignConditions("54D", 4101204060L), new DesignConditions("55A", 1101220061L), new DesignConditions("55B", 2101220061L), new DesignConditions("55C", 3101220061L), new DesignConditions("55D", 4101220061L), new DesignConditions("56A", 1091616050L), new DesignConditions("56B", 2091616050L), new DesignConditions("56C", 3091616050L), new DesignConditions("56D", 4091616050L), new DesignConditions("57A", 1091612050L), new DesignConditions("57B", 2091612050L), new DesignConditions("57C", 3091612050L), new DesignConditions("57D", 4091612050L), new DesignConditions("58A", 1091608050L), new DesignConditions("58B", 2091608050L), new DesignConditions("58C", 3091608050L), new DesignConditions("58D", 4091608050L), new DesignConditions("59A", 1091604050L), new DesignConditions("59B", 2091604050L), new DesignConditions("59C", 3091604050L), new DesignConditions("59D", 4091604050L), new DesignConditions("60A", 1091616051L), new DesignConditions("60B", 2091616051L), new DesignConditions("60C", 3091616051L), new DesignConditions("60D", 4091616051L), new DesignConditions("61A", 1082012050L), new DesignConditions("61B", 2082012050L), new DesignConditions("61C", 3082012050L), new DesignConditions("61D", 4082012050L), new DesignConditions("62A", 1082008050L), new DesignConditions("62B", 2082008050L), new DesignConditions("62C", 3082008050L), new DesignConditions("62D", 4082008050L), new DesignConditions("63A", 1082004050L), new DesignConditions("63B", 2082004050L), new DesignConditions("63C", 3082004050L), new DesignConditions("63D", 4082004050L), new DesignConditions("64A", 1082012051L), new DesignConditions("64B", 2082012051L), new DesignConditions("64C", 3082012051L), new DesignConditions("64D", 4082012051L), new DesignConditions("65A", 1072408040L), new DesignConditions("65B", 2072408040L), new DesignConditions("65C", 3072408040L), new DesignConditions("65D", 4072408040L), new DesignConditions("66A", 1072404040L), new DesignConditions("66B", 2072404040L), new DesignConditions("66C", 3072404040L), new DesignConditions("66D", 4072404040L), new DesignConditions("67A", 1072408041L), new DesignConditions("67B", 2072408041L), new DesignConditions("67C", 3072408041L), new DesignConditions("67D", 4072408041L), new DesignConditions("68A", 1062804040L), new DesignConditions("68B", 2062804040L), new DesignConditions("68C", 3062804040L), new DesignConditions("68D", 4062804040L), new DesignConditions("69A", 1062804041L), new DesignConditions("69B", 2062804041L), new DesignConditions("69C", 3062804041L), new DesignConditions("69D", 4062804041L), new DesignConditions("70A", 1053200031L), new DesignConditions("70B", 2053200031L), new DesignConditions("70C", 3053200031L), new DesignConditions("70D", 4053200031L), new DesignConditions("71A", 1110824360L), new DesignConditions("71B", 2110824360L), new DesignConditions("71C", 3110824360L), new DesignConditions("71D", 4110824360L), new DesignConditions("72A", 1110820360L), new DesignConditions("72B", 2110820360L), new DesignConditions("72C", 3110820360L), new DesignConditions("72D", 4110820360L), new DesignConditions("73A", 1110816360L), new DesignConditions("73B", 2110816360L), new DesignConditions("73C", 3110816360L), new DesignConditions("73D", 4110816360L), new DesignConditions("74A", 1110812360L), new DesignConditions("74B", 2110812360L), new DesignConditions("74C", 3110812360L), new DesignConditions("74D", 4110812360L), new DesignConditions("75A", 1110808360L), new DesignConditions("75B", 2110808360L), new DesignConditions("75C", 3110808360L), new DesignConditions("75D", 4110808360L), new DesignConditions("76A", 1110804360L), new DesignConditions("76B", 2110804360L), new DesignConditions("76C", 3110804360L), new DesignConditions("76D", 4110804360L), new DesignConditions("77A", 1110824361L), new DesignConditions("77B", 2110824361L), new DesignConditions("77C", 3110824361L), new DesignConditions("77D", 4110824361L), new DesignConditions("78A", 1101220360L), new DesignConditions("78B", 2101220360L), new DesignConditions("78C", 3101220360L), new DesignConditions("78D", 4101220360L), new DesignConditions("79A", 1101216360L), new DesignConditions("79B", 2101216360L), new DesignConditions("79C", 3101216360L), new DesignConditions("79D", 4101216360L), new DesignConditions("80A", 1101212360L), new DesignConditions("80B", 2101212360L), new DesignConditions("80C", 3101212360L), new DesignConditions("80D", 4101212360L), new DesignConditions("81A", 1101208360L), new DesignConditions("81B", 2101208360L), new DesignConditions("81C", 3101208360L), new DesignConditions("81D", 4101208360L), new DesignConditions("82A", 1101204360L), new DesignConditions("82B", 2101204360L), new DesignConditions("82C", 3101204360L), new DesignConditions("82D", 4101204360L), new DesignConditions("83A", 1101220361L), new DesignConditions("83B", 2101220361L), new DesignConditions("83C", 3101220361L), new DesignConditions("83D", 4101220361L), new DesignConditions("84A", 1091616350L), new DesignConditions("84B", 2091616350L), new DesignConditions("84C", 3091616350L), new DesignConditions("84D", 4091616350L), new DesignConditions("85A", 1091612350L), new DesignConditions("85B", 2091612350L), new DesignConditions("85C", 3091612350L), new DesignConditions("85D", 4091612350L), new DesignConditions("86A", 1091608350L), new DesignConditions("86B", 2091608350L), new DesignConditions("86C", 3091608350L), new DesignConditions("86D", 4091608350L), new DesignConditions("87A", 1091604350L), new DesignConditions("87B", 2091604350L), new DesignConditions("87C", 3091604350L), new DesignConditions("87D", 4091604350L), new DesignConditions("88A", 1091616351L), new DesignConditions("88B", 2091616351L), new DesignConditions("88C", 3091616351L), new DesignConditions("88D", 4091616351L), new DesignConditions("89A", 1082012350L), new DesignConditions("89B", 2082012350L), new DesignConditions("89C", 3082012350L), new DesignConditions("89D", 4082012350L), new DesignConditions("90A", 1082008350L), new DesignConditions("90B", 2082008350L), new DesignConditions("90C", 3082008350L), new DesignConditions("90D", 4082008350L), new DesignConditions("91A", 1082004350L), new DesignConditions("91B", 2082004350L), new DesignConditions("91C", 3082004350L), new DesignConditions("91D", 4082004350L), new DesignConditions("92A", 1082012351L), new DesignConditions("92B", 2082012351L), new DesignConditions("92C", 3082012351L), new DesignConditions("92D", 4082012351L), new DesignConditions("93A", 1072408340L), new DesignConditions("93B", 2072408340L), new DesignConditions("93C", 3072408340L), new DesignConditions("93D", 4072408340L), new DesignConditions("94A", 1072404340L), new DesignConditions("94B", 2072404340L), new DesignConditions("94C", 3072404340L), new DesignConditions("94D", 4072404340L), new DesignConditions("95A", 1072408341L), new DesignConditions("95B", 2072408341L), new DesignConditions("95C", 3072408341L), new DesignConditions("95D", 4072408341L), new DesignConditions("96A", 1062804340L), new DesignConditions("96B", 2062804340L), new DesignConditions("96C", 3062804340L), new DesignConditions("96D", 4062804340L), new DesignConditions("97A", 1062804341L), new DesignConditions("97B", 2062804341L), new DesignConditions("97C", 3062804341L), new DesignConditions("97D", 4062804341L), new DesignConditions("98A", 1053200331L), new DesignConditions("98B", 2053200331L), new DesignConditions("98C", 3053200331L), new DesignConditions("98D", 4053200331L) };
/*  598:     */   
/*  599:     */   public static boolean isTagPrefix(String s)
/*  600:     */   {
/*  601:1396 */     int lo = 0;
/*  602:1397 */     int hi = conditions.length - 1;
/*  603:1398 */     while (lo < hi)
/*  604:     */     {
/*  605:1399 */       int mid = (lo + hi) / 2;
/*  606:1400 */       int cmp = s.compareTo(conditions[mid].tag.substring(0, s.length()));
/*  607:1401 */       if (cmp < 0) {
/*  608:1402 */         hi = mid - 1;
/*  609:1403 */       } else if (cmp > 0) {
/*  610:1404 */         lo = mid + 1;
/*  611:     */       } else {
/*  612:1406 */         return true;
/*  613:     */       }
/*  614:     */     }
/*  615:1409 */     return false;
/*  616:     */   }
/*  617:     */   
/*  618:1412 */   private static final HashMap<Long, DesignConditions> codeIndex = getCodeIndex();
/*  619:     */   
/*  620:     */   private static HashMap<Long, DesignConditions> getCodeIndex()
/*  621:     */   {
/*  622:1415 */     HashMap<Long, DesignConditions> index = new HashMap(1024);
/*  623:1416 */     for (int i = 0; i < conditions.length; i++) {
/*  624:1417 */       index.put(Long.valueOf(conditions[i].codeLong), conditions[i]);
/*  625:     */     }
/*  626:1419 */     return index;
/*  627:     */   }
/*  628:     */   
/*  629:     */   public static DesignConditions getDesignConditions(long code)
/*  630:     */   {
/*  631:1431 */     DesignConditions designConditions = (DesignConditions)codeIndex.get(Long.valueOf(code));
/*  632:1432 */     return designConditions == null ? new DesignConditions("99Z", code) : designConditions;
/*  633:     */   }
/*  634:     */   
/*  635:1435 */   private static final HashMap<String, DesignConditions> tagIndex = getTagIndex();
/*  636:     */   
/*  637:     */   private static HashMap<String, DesignConditions> getTagIndex()
/*  638:     */   {
/*  639:1438 */     HashMap<String, DesignConditions> index = new HashMap(1024);
/*  640:1439 */     for (int i = 0; i < conditions.length; i++) {
/*  641:1440 */       index.put(conditions[i].tag, conditions[i]);
/*  642:     */     }
/*  643:1442 */     return index;
/*  644:     */   }
/*  645:     */   
/*  646:     */   public static DesignConditions getDesignConditions(String tag)
/*  647:     */   {
/*  648:1452 */     return (DesignConditions)tagIndex.get(tag);
/*  649:     */   }
/*  650:     */   
/*  651:     */   private static class SetupKey
/*  652:     */   {
/*  653:     */     double deckElevation;
/*  654:     */     double archHeight;
/*  655:     */     double pierHeight;
/*  656:     */     int nAnchorages;
/*  657:     */     int loadType;
/*  658:     */     int deckType;
/*  659:     */     
/*  660:     */     public SetupKey(double deckElevation, double archHeight, double pierHeight, int nAnchorages, int loadType, int deckType)
/*  661:     */     {
/*  662:1464 */       this.deckElevation = deckElevation;
/*  663:1465 */       this.archHeight = archHeight;
/*  664:1466 */       this.pierHeight = pierHeight;
/*  665:1467 */       this.nAnchorages = nAnchorages;
/*  666:1468 */       this.loadType = loadType;
/*  667:1469 */       this.deckType = deckType;
/*  668:     */     }
/*  669:     */     
/*  670:     */     public boolean equals(Object obj)
/*  671:     */     {
/*  672:1474 */       if (!(obj instanceof SetupKey)) {
/*  673:1475 */         return false;
/*  674:     */       }
/*  675:1477 */       SetupKey key = (SetupKey)obj;
/*  676:1478 */       return (this.deckElevation == key.deckElevation) && (this.archHeight == key.archHeight) && (this.pierHeight == key.pierHeight) && (this.nAnchorages == key.nAnchorages) && (this.loadType == key.loadType) && (this.deckType == key.deckType);
/*  677:     */     }
/*  678:     */     
/*  679:     */     public int hashCode()
/*  680:     */     {
/*  681:1488 */       long val = Double.valueOf(this.deckElevation).hashCode();
/*  682:1489 */       val = (val << 5) + Double.valueOf(this.archHeight).hashCode();
/*  683:1490 */       val = (val << 5) + Double.valueOf(this.pierHeight).hashCode();
/*  684:1491 */       val = (val << 5) + this.nAnchorages;
/*  685:1492 */       val = (val << 5) + this.loadType;
/*  686:1493 */       val = (val << 5) + this.deckType;
/*  687:1494 */       return Long.valueOf(val).hashCode();
/*  688:     */     }
/*  689:     */     
/*  690:     */     public String toString()
/*  691:     */     {
/*  692:1499 */       return "[deckElevation=" + this.deckElevation + ",archHeight=" + this.archHeight + ",pierHeight=" + this.pierHeight + ",nAnchorages=" + this.nAnchorages + ",loadType=" + this.loadType + ",deckType=" + this.deckType + "]";
/*  693:     */     }
/*  694:     */   }
/*  695:     */   
/*  696:1508 */   private static final HashMap<SetupKey, DesignConditions> setupIndex = getSetupIndex();
/*  697:     */   
/*  698:     */   private static HashMap<SetupKey, DesignConditions> getSetupIndex()
/*  699:     */   {
/*  700:1511 */     HashMap<SetupKey, DesignConditions> index = new HashMap(1024);
/*  701:1512 */     for (int i = 0; i < conditions.length; i++)
/*  702:     */     {
/*  703:1513 */       DesignConditions d = conditions[i];
/*  704:1514 */       index.put(new SetupKey(d.deckElevation, d.archHeight, d.pierHeight, d.nAnchorages, d.loadType, d.deckType), d);
/*  705:     */     }
/*  706:1522 */     return index;
/*  707:     */   }
/*  708:     */   
/*  709:     */   public static DesignConditions getDesignConditions(double deckElevation, double archHeight, double pierHeight, int nAnchorages, int loadType, int deckType)
/*  710:     */   {
/*  711:1537 */     return (DesignConditions)setupIndex.get(new SetupKey(deckElevation, archHeight, pierHeight, nAnchorages, loadType, deckType));
/*  712:     */   }
/*  713:     */   
/*  714:     */   public static void printSiteCostsTable()
/*  715:     */   {
/*  716:1544 */     DesignConditions[] sortedConditions = new DesignConditions[conditions.length];
/*  717:1545 */     System.arraycopy(conditions, 0, sortedConditions, 0, conditions.length);
/*  718:1546 */     Arrays.sort(sortedConditions, new Comparator()
/*  719:     */     {
/*  720:     */       public int compare(DesignConditions a, DesignConditions b)
/*  721:     */       {
/*  722:1549 */         return a.codeLong > b.codeLong ? 1 : a.codeLong < b.codeLong ? -1 : 0;
/*  723:     */       }
/*  724:1552 */     });
/*  725:1553 */     String dest = "eg/2014/scenario_descriptors.h";
/*  726:     */     try
/*  727:     */     {
/*  728:1555 */       Writer out = new OutputStreamWriter(new FileOutputStream("eg/2014/scenario_descriptors.h"));
/*  729:1556 */       out.write("static TScenarioDescriptor scenario_descriptor_tbl[] = {\n");
/*  730:1557 */       for (int i = 0; i < sortedConditions.length; i++)
/*  731:     */       {
/*  732:1558 */         DesignConditions c = sortedConditions[i];
/*  733:1559 */         out.write(String.format("    {%4d, \"%d\", \"%s\", %9.2f },\n", new Object[] { Integer.valueOf(i), Long.valueOf(c.codeLong), c.tag, Double.valueOf(c.totalFixedCost) }));
/*  734:     */         
/*  735:     */ 
/*  736:1562 */         int err = getCodeError(c.getCode());
/*  737:1563 */         if (err != 0)
/*  738:     */         {
/*  739:1564 */           System.out.println("ERROR:" + err);
/*  740:1565 */           System.exit(1);
/*  741:     */         }
/*  742:     */       }
/*  743:1568 */       out.write("};\n");
/*  744:1569 */       out.close();
/*  745:     */     }
/*  746:     */     catch (IOException ex)
/*  747:     */     {
/*  748:1571 */       Logger.getLogger(DesignConditions.class.getName()).log(Level.SEVERE, null, ex);
/*  749:     */     }
/*  750:1573 */     System.out.println("Site costs table writtne to eg/2014/scenario_descriptors.h");
/*  751:     */   }
/*  752:     */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DesignConditions
 * JD-Core Version:    0.7.0.1
 */