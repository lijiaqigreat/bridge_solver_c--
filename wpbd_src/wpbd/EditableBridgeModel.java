/*    1:     */ package wpbd;
/*    2:     */ 
/*    3:     */ import java.awt.geom.Rectangle2D.Double;
/*    4:     */ import java.io.BufferedWriter;
/*    5:     */ import java.io.File;
/*    6:     */ import java.io.FileWriter;
/*    7:     */ import java.io.IOException;
/*    8:     */ import java.io.PrintStream;
/*    9:     */ import java.util.ArrayList;
/*   10:     */ import java.util.Formatter;
/*   11:     */ import java.util.HashSet;
/*   12:     */ import java.util.Iterator;
/*   13:     */ import java.util.Locale;
/*   14:     */ import java.util.Set;
/*   15:     */ import java.util.logging.Level;
/*   16:     */ import java.util.logging.Logger;
/*   17:     */ import javax.swing.ListSelectionModel;
/*   18:     */ import javax.swing.event.ChangeEvent;
/*   19:     */ import javax.swing.event.ChangeListener;
/*   20:     */ import javax.swing.event.UndoableEditEvent;
/*   21:     */ import javax.swing.event.UndoableEditListener;
/*   22:     */ import org.jdesktop.application.ResourceMap;
/*   23:     */ 
/*   24:     */ public class EditableBridgeModel
/*   25:     */   extends BridgeModel
/*   26:     */ {
/*   27:     */   public static final int ADD_JOINT_OK = 0;
/*   28:     */   public static final int ADD_JOINT_JOINT_EXISTS = 2;
/*   29:     */   public static final int ADD_JOINT_AT_MAX = 3;
/*   30:     */   public static final int MOVE_JOINT_OK = 0;
/*   31:     */   public static final int MOVE_JOINT_ALREADY_THERE = 1;
/*   32:     */   public static final int MOVE_JOINT_JOINT_EXISTS = 2;
/*   33:     */   public static final int MOVE_JOINT_MEMBER_AT_MAX = 3;
/*   34:     */   public static final int ADD_MEMBER_OK = 0;
/*   35:     */   public static final int ADD_MEMBER_SAME_JOINT = 1;
/*   36:     */   public static final int ADD_MEMBER_MEMBER_EXISTS = 2;
/*   37:     */   public static final int ADD_MEMBER_AT_MAX = 3;
/*   38:     */   public static final int ADD_MEMBER_CROSSES_PIER = 4;
/*   39:     */   public static final int STATUS_WORKING = 0;
/*   40:     */   public static final int STATUS_PASSES = 4;
/*   41:     */   public static final int STATUS_FAILS = 3;
/*   42: 107 */   protected final ExtendedUndoManager undoManager = new ExtendedUndoManager();
/*   43: 111 */   protected final ArrayList<DesignIteration> iterationList = new ArrayList();
/*   44: 115 */   protected final DesignIteration iterationTree = new DesignIteration();
/*   45: 119 */   protected final ArrayList<ChangeListener> selectionChangeListeners = new ArrayList();
/*   46: 123 */   protected final ArrayList<ChangeListener> structureChangeListeners = new ArrayList();
/*   47: 127 */   protected final ArrayList<ChangeListener> analysisChangeListeners = new ArrayList();
/*   48: 131 */   protected final ArrayList<ChangeListener> iterationChangeListeners = new ArrayList();
/*   49: 135 */   protected final Analysis analysis = new Analysis();
/*   50:     */   protected Editable lastSelected;
/*   51: 157 */   protected Object analysisValidMark = null;
/*   52: 161 */   protected int loadedIterationIndex = -1;
/*   53: 165 */   protected int editedIterationIndex = -1;
/*   54: 170 */   protected boolean loadedIterationIsSnapshot = false;
/*   55:     */   private static final String okHTML = "<td bgcolor=\"#C0FFC0\" align=\"center\">OK</td>";
/*   56:     */   private static final String failHTML = "<td bgcolor=\"#FFC0C0\" align=\"center\">Fail</td>";
/*   57:     */   private static final String rowHTML = "<tr>\n <td align=\"right\">%d</td>\n <td align=\"right\">%s</td>\n <td align=\"center\">%s</td>\n <td align=\"center\">%s</td>\n <td align=\"right\">%.2f</td>\n <td>&nbsp;</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">%.4f</td>\n %s\n <td>&nbsp;</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">%.4f</td>\n %s\n</tr>\n";
/*   58:     */   private static final String slendernessFailureRowHTML = "<tr>\n <td align=\"right\">%d</td>\n <td align=\"right\">%s</td>\n <td align=\"center\">%s</td>\n <td align=\"center\">%s</td>\n <td align=\"right\">%.2f</td>\n <td>&nbsp;</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">0.0</td>\n <td align=\"right\">&nbsp;oo</td>\n <td bgcolor=\"#FF60FF\" align=\"center\">Fail</td>\n <td>&nbsp;</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">0.0</td>\n <td align=\"right\">&nbsp;oo</td>\n <td bgcolor=\"#FF60FF\" align=\"center\">Fail</td>\n</tr>\n";
/*   59:     */   private static final String headHTML = "<html><head><title>Load Test Results Report</title>\n<style>\n  table { font-size : 8pt; font-family: arial, helvetica, sans-serif }\n  th { font-weight : bold }\n</style></head><body>\n<table border=1 cellspacing=0 cellpadding=2>\n<tr><th colspan=15>Load Test Results Report (Design Iteration #%d, Scenario id %d, number %s, Cost $%.2f)</th></tr>\n<tr>\n <th colspan=5>Member</th><th>&nbsp;</th><th colspan=4>Compression</th><th>&nbsp;</th><th colspan=4>Tension</th></tr>\n<tr>\n <th>#</th><th>Size</th><th>Section</th><th>Matl.</th><th>Length<br>(m)</th>\n <th>&nbsp;</th>\n <th>Force<br>(kN)</th><th>Strength<br>(kN)</th><th>Force/<br>Strength</th><th>Status</th>\n <th>&nbsp;</th>\n <th>Force<br>(kN)</th><th>Strength<br>(kN)</th><th>Force/<br>Strength</th><th>Status</th>\n</tr>\n";
/*   60:     */   private static final String tailHTLM = "</table>\n</body></html>";
/*   61:     */   
/*   62:     */   public EditableBridgeModel()
/*   63:     */   {
/*   64: 176 */     this.undoManager.addUndoableAfterEditListener(new UndoableEditListener()
/*   65:     */     {
/*   66:     */       public void undoableEditHappened(UndoableEditEvent e)
/*   67:     */       {
/*   68: 179 */         if ((e.getEdit() instanceof EditCommand))
/*   69:     */         {
/*   70: 180 */           EditableBridgeModel.this.editIteration();
/*   71: 181 */           EditableBridgeModel.this.fireStructureChange();
/*   72:     */         }
/*   73:     */       }
/*   74:     */     });
/*   75:     */   }
/*   76:     */   
/*   77:     */   public int getIterationCount()
/*   78:     */   {
/*   79: 193 */     return this.iterationList.size();
/*   80:     */   }
/*   81:     */   
/*   82:     */   public int getCurrentIterationIndex()
/*   83:     */   {
/*   84: 202 */     return this.loadedIterationIndex;
/*   85:     */   }
/*   86:     */   
/*   87:     */   public DesignIteration getDesignIteration(int index)
/*   88:     */   {
/*   89: 212 */     return (DesignIteration)this.iterationList.get(index);
/*   90:     */   }
/*   91:     */   
/*   92:     */   public int getDesignIterationIndex(Object iteration)
/*   93:     */   {
/*   94: 222 */     return this.iterationList.indexOf(iteration);
/*   95:     */   }
/*   96:     */   
/*   97:     */   public DesignIteration getDesignIterationTreeRoot()
/*   98:     */   {
/*   99: 231 */     return this.iterationTree;
/*  100:     */   }
/*  101:     */   
/*  102:     */   public void clearIterations()
/*  103:     */   {
/*  104: 238 */     this.iterationList.clear();
/*  105: 239 */     this.iterationTree.removeAllChildren();
/*  106: 240 */     this.loadedIterationIndex = (this.editedIterationIndex = -1);
/*  107: 241 */     this.loadedIterationIsSnapshot = false;
/*  108: 242 */     this.iterationNumber = 0;
/*  109: 243 */     fireIterationChange();
/*  110:     */   }
/*  111:     */   
/*  112:     */   public void editIteration()
/*  113:     */   {
/*  114: 251 */     if ((this.loadedIterationIndex >= 0) && (!this.loadedIterationIsSnapshot))
/*  115:     */     {
/*  116: 252 */       this.iterationNumber = (((DesignIteration)this.iterationList.get(this.iterationList.size() - 1)).getNumber() + 1);
/*  117: 253 */       this.editedIterationIndex = this.loadedIterationIndex;
/*  118: 254 */       this.loadedIterationIndex = -1;
/*  119: 255 */       this.loadedIterationIsSnapshot = false;
/*  120: 256 */       fireIterationChange();
/*  121:     */     }
/*  122:     */   }
/*  123:     */   
/*  124:     */   public void loadIteration(int index)
/*  125:     */   {
/*  126: 267 */     if (index != this.loadedIterationIndex) {
/*  127:     */       try
/*  128:     */       {
/*  129: 269 */         parseBytes(((DesignIteration)this.iterationList.get(index)).getBridgeModelAsBytes());
/*  130:     */         
/*  131: 271 */         this.undoManager.clear();
/*  132: 272 */         this.loadedIterationIndex = index;
/*  133: 273 */         this.editedIterationIndex = -1;
/*  134: 274 */         this.loadedIterationIsSnapshot = false;
/*  135: 275 */         this.lastSelected = null;
/*  136: 276 */         fireIterationChange();
/*  137: 277 */         fireStructureChange();
/*  138: 278 */         fireSelectionChange();
/*  139: 279 */         fireAnalysisChange();
/*  140:     */       }
/*  141:     */       catch (IOException ex) {}
/*  142:     */     }
/*  143:     */   }
/*  144:     */   
/*  145:     */   private void setNewIteration()
/*  146:     */   {
/*  147: 289 */     DesignIteration iteration = new DesignIteration(this.iterationNumber, getTotalCost(), this.projectId, toBytes(), getAnalysisStatus());
/*  148: 290 */     int currentIterationIndex = this.loadedIterationIndex >= 0 ? this.loadedIterationIndex : this.editedIterationIndex;
/*  149: 291 */     if (currentIterationIndex >= 0)
/*  150:     */     {
/*  151: 292 */       DesignIteration current = (DesignIteration)this.iterationList.get(currentIterationIndex);
/*  152: 293 */       DesignIteration parent = (DesignIteration)current.getParent();
/*  153: 294 */       if (parent.getLastChild() == current) {
/*  154: 295 */         parent.add(iteration);
/*  155:     */       } else {
/*  156: 298 */         current.add(iteration);
/*  157:     */       }
/*  158:     */     }
/*  159:     */     else
/*  160:     */     {
/*  161: 302 */       assert (this.iterationTree.getChildCount() == 0);
/*  162: 303 */       this.iterationTree.add(iteration);
/*  163:     */     }
/*  164: 305 */     this.iterationList.add(iteration);
/*  165:     */   }
/*  166:     */   
/*  167:     */   private void resetCurrentIteration()
/*  168:     */   {
/*  169: 309 */     ((DesignIteration)this.iterationList.get(this.iterationList.size() - 1)).initialize(this.iterationNumber, getTotalCost(), this.projectId, toBytes(), this.analysis.getStatus());
/*  170:     */   }
/*  171:     */   
/*  172:     */   private void updateCurrentIterationStatus()
/*  173:     */   {
/*  174: 313 */     ((DesignIteration)this.iterationList.get(this.loadedIterationIndex)).setAnalysisStatus(this.analysis.getStatus());
/*  175:     */   }
/*  176:     */   
/*  177:     */   private void saveIteration()
/*  178:     */   {
/*  179: 322 */     if ((this.iterationList.size() > 0) && (this.iterationNumber == ((DesignIteration)this.iterationList.get(this.iterationList.size() - 1)).getNumber()))
/*  180:     */     {
/*  181: 323 */       resetCurrentIteration();
/*  182: 324 */       this.editedIterationIndex = -1;
/*  183: 325 */       this.loadedIterationIndex = (this.iterationList.size() - 1);
/*  184:     */     }
/*  185: 327 */     else if ((this.iterationList.isEmpty()) || (this.iterationNumber > ((DesignIteration)this.iterationList.get(this.iterationList.size() - 1)).getNumber()))
/*  186:     */     {
/*  187: 328 */       setNewIteration();
/*  188: 329 */       this.editedIterationIndex = -1;
/*  189: 330 */       this.loadedIterationIndex = (this.iterationList.size() - 1);
/*  190:     */     }
/*  191: 332 */     else if (this.loadedIterationIndex >= 0)
/*  192:     */     {
/*  193: 333 */       updateCurrentIterationStatus();
/*  194:     */     }
/*  195: 335 */     this.loadedIterationIsSnapshot = false;
/*  196:     */   }
/*  197:     */   
/*  198:     */   public void saveSnapshot()
/*  199:     */   {
/*  200: 345 */     if ((this.loadedIterationIndex == -1) || (this.loadedIterationIsSnapshot))
/*  201:     */     {
/*  202: 346 */       saveIteration();
/*  203: 347 */       this.loadedIterationIsSnapshot = true;
/*  204: 348 */       fireIterationChange();
/*  205:     */     }
/*  206:     */   }
/*  207:     */   
/*  208:     */   public boolean canLoadNextIteration(int inc)
/*  209:     */   {
/*  210: 359 */     return getNextIterationIndex(inc) != -1;
/*  211:     */   }
/*  212:     */   
/*  213:     */   public int getNextIterationIndex(int inc)
/*  214:     */   {
/*  215: 370 */     int nIterations = getIterationCount();
/*  216: 371 */     if (this.loadedIterationIndex == -1) {
/*  217: 372 */       return (inc < 0) && (nIterations > 0) ? 0 : -1;
/*  218:     */     }
/*  219: 374 */     int nextIterationIndex = this.loadedIterationIndex + inc;
/*  220: 375 */     if (inc > 0) {
/*  221: 376 */       return nextIterationIndex < nIterations ? nextIterationIndex : -1;
/*  222:     */     }
/*  223: 378 */     if (inc < 0) {
/*  224: 379 */       return nextIterationIndex >= 0 ? nextIterationIndex : -1;
/*  225:     */     }
/*  226: 381 */     return this.loadedIterationIndex;
/*  227:     */   }
/*  228:     */   
/*  229:     */   public boolean canGotoIteration()
/*  230:     */   {
/*  231: 390 */     return (this.iterationList.size() > 1) || ((this.iterationList.size() == 1) && (this.iterationNumber > ((DesignIteration)this.iterationList.get(this.iterationList.size() - 1)).getNumber()));
/*  232:     */   }
/*  233:     */   
/*  234:     */   public ExtendedUndoManager getUndoManager()
/*  235:     */   {
/*  236: 400 */     return this.undoManager;
/*  237:     */   }
/*  238:     */   
/*  239:     */   public void analyze()
/*  240:     */   {
/*  241: 407 */     this.analysis.initialize(this);
/*  242: 408 */     this.analysisValidMark = (this.analysis.getStatus() > 2 ? this.undoManager.getMark() : null);
/*  243: 409 */     fireAnalysisChange();
/*  244: 410 */     saveIteration();
/*  245: 411 */     fireIterationChange();
/*  246:     */   }
/*  247:     */   
/*  248:     */   public boolean isAnalyzable()
/*  249:     */   {
/*  250: 420 */     return this.members.size() >= 2 * this.joints.size() - this.designConditions.getNJointRestraints();
/*  251:     */   }
/*  252:     */   
/*  253:     */   public boolean isAnalysisValid()
/*  254:     */   {
/*  255: 430 */     return this.undoManager.isAtMark(this.analysisValidMark);
/*  256:     */   }
/*  257:     */   
/*  258:     */   public boolean isPassing()
/*  259:     */   {
/*  260: 439 */     return (isAnalysisValid()) && (this.analysis.getStatus() == 4);
/*  261:     */   }
/*  262:     */   
/*  263:     */   public int getAnalysisStatus()
/*  264:     */   {
/*  265: 451 */     if (!isAnalysisValid()) {
/*  266: 452 */       return 0;
/*  267:     */     }
/*  268: 454 */     return this.analysis.getStatus() == 4 ? 4 : 3;
/*  269:     */   }
/*  270:     */   
/*  271:     */   public Analysis getAnalysis()
/*  272:     */   {
/*  273: 463 */     return this.analysis;
/*  274:     */   }
/*  275:     */   
/*  276:     */   public void addSelectionChangeListener(ChangeListener l)
/*  277:     */   {
/*  278: 472 */     this.selectionChangeListeners.add(l);
/*  279:     */   }
/*  280:     */   
/*  281:     */   public void removeSelectionChangeListener(ChangeListener l)
/*  282:     */   {
/*  283: 481 */     this.selectionChangeListeners.remove(l);
/*  284:     */   }
/*  285:     */   
/*  286:     */   public void fireSelectionChange()
/*  287:     */   {
/*  288: 488 */     Iterator<ChangeListener> e = new ArrayList(this.selectionChangeListeners).iterator();
/*  289: 489 */     while (e.hasNext()) {
/*  290: 490 */       ((ChangeListener)e.next()).stateChanged(new ChangeEvent(this));
/*  291:     */     }
/*  292:     */   }
/*  293:     */   
/*  294:     */   public void addStructureChangeListener(ChangeListener l)
/*  295:     */   {
/*  296: 500 */     this.structureChangeListeners.add(l);
/*  297:     */   }
/*  298:     */   
/*  299:     */   public void removeStructureChangeListener(ChangeListener l)
/*  300:     */   {
/*  301: 509 */     this.structureChangeListeners.remove(l);
/*  302:     */   }
/*  303:     */   
/*  304:     */   public void fireStructureChange()
/*  305:     */   {
/*  306: 516 */     Iterator<ChangeListener> e = new ArrayList(this.structureChangeListeners).iterator();
/*  307: 517 */     while (e.hasNext()) {
/*  308: 518 */       ((ChangeListener)e.next()).stateChanged(new ChangeEvent(this));
/*  309:     */     }
/*  310:     */   }
/*  311:     */   
/*  312:     */   public void addAnalysisChangeListener(ChangeListener l)
/*  313:     */   {
/*  314: 528 */     this.analysisChangeListeners.add(l);
/*  315:     */   }
/*  316:     */   
/*  317:     */   public void removeAnalysisChangeListener(ChangeListener l)
/*  318:     */   {
/*  319: 537 */     this.analysisChangeListeners.remove(l);
/*  320:     */   }
/*  321:     */   
/*  322:     */   public void fireAnalysisChange()
/*  323:     */   {
/*  324: 544 */     Iterator<ChangeListener> e = new ArrayList(this.analysisChangeListeners).iterator();
/*  325: 545 */     while (e.hasNext()) {
/*  326: 546 */       ((ChangeListener)e.next()).stateChanged(new ChangeEvent(this));
/*  327:     */     }
/*  328:     */   }
/*  329:     */   
/*  330:     */   public void addIterationChangeListener(ChangeListener l)
/*  331:     */   {
/*  332: 556 */     this.iterationChangeListeners.add(l);
/*  333:     */   }
/*  334:     */   
/*  335:     */   public void removeIterationChangeListener(ChangeListener l)
/*  336:     */   {
/*  337: 565 */     this.iterationChangeListeners.remove(l);
/*  338:     */   }
/*  339:     */   
/*  340:     */   public void fireIterationChange()
/*  341:     */   {
/*  342: 572 */     Iterator<ChangeListener> e = new ArrayList(this.iterationChangeListeners).iterator();
/*  343: 573 */     while (e.hasNext()) {
/*  344: 574 */       ((ChangeListener)e.next()).stateChanged(new ChangeEvent(this));
/*  345:     */     }
/*  346:     */   }
/*  347:     */   
/*  348:     */   public void initialize(DesignConditions conditions, String projectId, String designedBy)
/*  349:     */   {
/*  350: 588 */     super.initialize(conditions, projectId, designedBy);
/*  351: 589 */     this.lastSelected = null;
/*  352: 590 */     this.undoManager.newSession();
/*  353: 591 */     clearIterations();
/*  354: 592 */     saveIteration();
/*  355: 593 */     fireStructureChange();
/*  356: 594 */     fireIterationChange();
/*  357: 595 */     fireSelectionChange();
/*  358: 596 */     fireAnalysisChange();
/*  359:     */   }
/*  360:     */   
/*  361:     */   public int addJoint(Affine.Point ptWorld)
/*  362:     */   {
/*  363: 606 */     if (findJointAt(ptWorld) != null) {
/*  364: 607 */       return 2;
/*  365:     */     }
/*  366: 609 */     if (this.joints.size() >= 100) {
/*  367: 610 */       return 3;
/*  368:     */     }
/*  369: 612 */     if (new InsertJointCommand(this, new Joint(ptWorld)).execute(this.undoManager) != 0) {
/*  370: 613 */       return 3;
/*  371:     */     }
/*  372: 615 */     return 0;
/*  373:     */   }
/*  374:     */   
/*  375:     */   public int addMember(Joint jointA, Joint jointB, int materialIndex, int sectionIndex, int sizeIndex)
/*  376:     */   {
/*  377: 631 */     if (jointA == jointB) {
/*  378: 632 */       return 1;
/*  379:     */     }
/*  380: 634 */     if (getMember(jointA, jointB) != null) {
/*  381: 635 */       return 2;
/*  382:     */     }
/*  383: 639 */     if (this.designConditions.isHiPier())
/*  384:     */     {
/*  385: 640 */       Affine.Point pierLocation = this.designConditions.getPrescribedJointLocation(this.designConditions.getPierJointIndex());
/*  386: 641 */       double eps = 1.0E-006D;
/*  387: 642 */       Affine.Point a = jointA.getPointWorld();
/*  388: 643 */       Affine.Point b = jointB.getPointWorld();
/*  389: 644 */       if (((a.x < pierLocation.x) && (pierLocation.x < b.x)) || ((b.x < pierLocation.x) && (pierLocation.x < a.x)))
/*  390:     */       {
/*  391: 646 */         double dx = b.x - a.x;
/*  392: 647 */         if (Math.abs(dx) > 1.0E-006D)
/*  393:     */         {
/*  394: 648 */           double y = (pierLocation.x - a.x) * (b.y - a.y) / dx + a.y;
/*  395: 649 */           if (y < pierLocation.y - 1.0E-006D) {
/*  396: 650 */             return 4;
/*  397:     */           }
/*  398:     */         }
/*  399:     */       }
/*  400:     */     }
/*  401: 655 */     if (this.members.size() >= 200) {
/*  402: 656 */       return 3;
/*  403:     */     }
/*  404: 658 */     Material material = this.inventory.getMaterial(materialIndex);
/*  405: 659 */     Shape shape = this.inventory.getShape(sectionIndex, sizeIndex);
/*  406: 660 */     Member member = new Member(jointA, jointB, material, shape);
/*  407: 661 */     new InsertMemberCommand(this, member).execute(this.undoManager);
/*  408: 662 */     return 0;
/*  409:     */   }
/*  410:     */   
/*  411:     */   public int moveJoint(Joint joint, Affine.Point ptWorld)
/*  412:     */   {
/*  413: 673 */     if (joint.getPointWorld().equals(ptWorld)) {
/*  414: 674 */       return 1;
/*  415:     */     }
/*  416: 676 */     Joint existing = findJointAt(ptWorld);
/*  417: 677 */     if ((existing != null) && (existing != joint)) {
/*  418: 678 */       return 2;
/*  419:     */     }
/*  420: 680 */     if (new MoveJointCommand(this, joint, ptWorld).execute(this.undoManager) == 3) {
/*  421: 681 */       return 3;
/*  422:     */     }
/*  423: 683 */     return 0;
/*  424:     */   }
/*  425:     */   
/*  426:     */   public void getTranssectedJoints(ArrayList<Joint> rtn, Joint a, Joint b)
/*  427:     */   {
/*  428: 695 */     rtn.clear();
/*  429: 696 */     Iterator<Joint> j = this.joints.iterator();
/*  430: 697 */     while (j.hasNext())
/*  431:     */     {
/*  432: 698 */       Joint joint = (Joint)j.next();
/*  433: 699 */       if (joint.getPointWorld().onSegment(a.getPointWorld(), b.getPointWorld()))
/*  434:     */       {
/*  435: 700 */         double distSq = a.getPointWorld().distanceSq(joint.getPointWorld());
/*  436: 701 */         boolean didInsert = false;
/*  437: 702 */         for (int i = 0; i < rtn.size(); i++) {
/*  438: 703 */           if (a.getPointWorld().distanceSq(((Joint)rtn.get(i)).getPointWorld()) > distSq)
/*  439:     */           {
/*  440: 704 */             rtn.add(i, joint);
/*  441: 705 */             didInsert = true;
/*  442: 706 */             break;
/*  443:     */           }
/*  444:     */         }
/*  445: 709 */         if (!didInsert) {
/*  446: 710 */           rtn.add(joint);
/*  447:     */         }
/*  448:     */       }
/*  449:     */     }
/*  450:     */   }
/*  451:     */   
/*  452:     */   public Joint findJoint(Affine.Point ptWorld, double searchRadius, boolean ignoreFixed)
/*  453:     */   {
/*  454: 726 */     double radiusSquared = searchRadius * searchRadius;
/*  455: 727 */     Joint closest = null;
/*  456: 728 */     double closestDistanceSquared = 1.0E+100D;
/*  457: 729 */     Iterator<Joint> e = this.joints.iterator();
/*  458: 730 */     while (e.hasNext())
/*  459:     */     {
/*  460: 731 */       Joint joint = (Joint)e.next();
/*  461: 732 */       if ((!ignoreFixed) || (!joint.isFixed()))
/*  462:     */       {
/*  463: 735 */         double distanceSquared = joint.getPointWorld().distanceSq(ptWorld);
/*  464: 736 */         if ((distanceSquared <= radiusSquared) && (distanceSquared < closestDistanceSquared))
/*  465:     */         {
/*  466: 737 */           closest = joint;
/*  467: 738 */           closestDistanceSquared = distanceSquared;
/*  468:     */         }
/*  469:     */       }
/*  470:     */     }
/*  471: 741 */     return closest;
/*  472:     */   }
/*  473:     */   
/*  474:     */   public Joint findUnconnectedJoint(Affine.Point ptWorld, Joint from, double searchRadius)
/*  475:     */   {
/*  476: 754 */     double radiusSquared = searchRadius * searchRadius;
/*  477: 755 */     Joint closest = null;
/*  478: 756 */     double closestDistanceSquared = 1.0E+100D;
/*  479: 757 */     Iterator<Joint> e = this.joints.iterator();
/*  480: 758 */     while (e.hasNext())
/*  481:     */     {
/*  482: 759 */       Joint joint = (Joint)e.next();
/*  483: 760 */       if (joint != from)
/*  484:     */       {
/*  485: 761 */         double distanceSquared = joint.getPointWorld().distanceSq(ptWorld);
/*  486: 762 */         if ((distanceSquared <= radiusSquared) && (distanceSquared < closestDistanceSquared) && (getMember(joint, from) == null))
/*  487:     */         {
/*  488: 765 */           closest = joint;
/*  489: 766 */           closestDistanceSquared = distanceSquared;
/*  490:     */         }
/*  491:     */       }
/*  492:     */     }
/*  493: 770 */     return closest;
/*  494:     */   }
/*  495:     */   
/*  496:     */   public Member getMember(Affine.Point ptWorld, ViewportTransform viewportTransform)
/*  497:     */   {
/*  498: 780 */     Member closest = null;
/*  499: 781 */     double closestDistance = 1.0E+100D;
/*  500: 782 */     Iterator<Member> e = this.members.iterator();
/*  501: 783 */     double jointRadius = viewportTransform.viewportToWorldDistance(8);
/*  502: 784 */     while (e.hasNext())
/*  503:     */     {
/*  504: 785 */       Member member = (Member)e.next();
/*  505: 786 */       double searchRadius = viewportTransform.viewportToWorldDistance(Math.max(3, Math.round(0.5F * member.getStrokeWidth())));
/*  506: 787 */       double distance = member.pickDistanceTo(ptWorld, jointRadius);
/*  507: 788 */       if ((distance <= searchRadius) && (distance < closestDistance))
/*  508:     */       {
/*  509: 789 */         closest = member;
/*  510: 790 */         closestDistance = distance;
/*  511:     */       }
/*  512:     */     }
/*  513: 793 */     return closest;
/*  514:     */   }
/*  515:     */   
/*  516:     */   private boolean addMissingDeckMembers()
/*  517:     */   {
/*  518: 797 */     int nPanels = this.designConditions.getNPanels();
/*  519: 798 */     int nMissing = 0;
/*  520: 799 */     for (int i = 0; i < nPanels; i++) {
/*  521: 800 */       if (getMember((Joint)this.joints.get(i), (Joint)this.joints.get(i + 1)) == null) {
/*  522: 801 */         nMissing++;
/*  523:     */       }
/*  524:     */     }
/*  525: 804 */     if (nMissing == 0) {
/*  526: 805 */       return false;
/*  527:     */     }
/*  528: 807 */     StockSelector.Descriptor descriptor = getMostCommonStock();
/*  529: 808 */     Member[] deckMembers = new Member[nMissing];
/*  530: 809 */     int im = 0;
/*  531: 810 */     for (int i = 0; i < nPanels; i++) {
/*  532: 811 */       if (getMember((Joint)this.joints.get(i), (Joint)this.joints.get(i + 1)) == null) {
/*  533: 812 */         deckMembers[(im++)] = new Member((Joint)this.joints.get(i), (Joint)this.joints.get(i + 1), this.inventory.getMaterial(descriptor.materialIndex), this.inventory.getShape(descriptor.sectionIndex, descriptor.sizeIndex));
/*  534:     */       }
/*  535:     */     }
/*  536: 817 */     new InsertMemberCommand(this, deckMembers).execute(this.undoManager);
/*  537: 818 */     return true;
/*  538:     */   }
/*  539:     */   
/*  540:     */   public boolean autofix()
/*  541:     */   {
/*  542: 827 */     return addMissingDeckMembers();
/*  543:     */   }
/*  544:     */   
/*  545:     */   public Member getMember(Joint jointA, Joint jointB)
/*  546:     */   {
/*  547: 838 */     Iterator<Member> e = this.members.iterator();
/*  548: 839 */     while (e.hasNext())
/*  549:     */     {
/*  550: 840 */       Member member = (Member)e.next();
/*  551: 841 */       if (member.hasJoints(jointA, jointB)) {
/*  552: 842 */         return member;
/*  553:     */       }
/*  554:     */     }
/*  555: 845 */     return null;
/*  556:     */   }
/*  557:     */   
/*  558:     */   public Member[] findMembersWithJoint(Joint joint)
/*  559:     */   {
/*  560: 855 */     ArrayList<Member> v = new ArrayList();
/*  561: 856 */     Iterator<Member> e = this.members.iterator();
/*  562: 857 */     while (e.hasNext())
/*  563:     */     {
/*  564: 858 */       Member member = (Member)e.next();
/*  565: 859 */       if (member.hasJoint(joint)) {
/*  566: 860 */         v.add(member);
/*  567:     */       }
/*  568:     */     }
/*  569: 863 */     return (Member[])v.toArray(new Member[v.size()]);
/*  570:     */   }
/*  571:     */   
/*  572:     */   public void incrementMemberSize(int increment)
/*  573:     */   {
/*  574: 872 */     if ((this.lastSelected instanceof Member)) {
/*  575: 873 */       new ChangeMembersCommand(this, increment).execute(this.undoManager);
/*  576:     */     }
/*  577:     */   }
/*  578:     */   
/*  579:     */   public boolean select(Editable element, boolean extendSelection)
/*  580:     */   {
/*  581: 886 */     if (element == null)
/*  582:     */     {
/*  583: 888 */       if (!extendSelection) {
/*  584: 889 */         clearSelection(true);
/*  585:     */       }
/*  586: 891 */       return false;
/*  587:     */     }
/*  588: 893 */     if (((element instanceof Member)) && (extendSelection))
/*  589:     */     {
/*  590: 894 */       clearSelectedJoint(false);
/*  591: 895 */       element.setSelected(!element.isSelected());
/*  592:     */     }
/*  593:     */     else
/*  594:     */     {
/*  595: 898 */       clearSelection(false);
/*  596: 899 */       element.setSelected(true);
/*  597:     */     }
/*  598: 901 */     this.lastSelected = element;
/*  599: 902 */     fireSelectionChange();
/*  600: 903 */     return true;
/*  601:     */   }
/*  602:     */   
/*  603:     */   public boolean clearSelectedJoint(boolean postChange)
/*  604:     */   {
/*  605: 913 */     if ((this.lastSelected instanceof Joint))
/*  606:     */     {
/*  607: 914 */       this.lastSelected.setSelected(false);
/*  608: 915 */       this.lastSelected = null;
/*  609: 916 */       if (postChange) {
/*  610: 917 */         fireSelectionChange();
/*  611:     */       }
/*  612: 919 */       return true;
/*  613:     */     }
/*  614: 921 */     return false;
/*  615:     */   }
/*  616:     */   
/*  617:     */   protected boolean clearSelectedMembers(boolean postChange)
/*  618:     */   {
/*  619: 931 */     boolean change = false;
/*  620: 932 */     if ((this.lastSelected instanceof Member))
/*  621:     */     {
/*  622: 933 */       Iterator<Member> me = this.members.iterator();
/*  623: 934 */       while (me.hasNext()) {
/*  624: 935 */         if (((Member)me.next()).setSelected(false)) {
/*  625: 936 */           change = true;
/*  626:     */         }
/*  627:     */       }
/*  628: 939 */       this.lastSelected = null;
/*  629:     */     }
/*  630: 941 */     if ((change) && (postChange)) {
/*  631: 942 */       fireSelectionChange();
/*  632:     */     }
/*  633: 944 */     return change;
/*  634:     */   }
/*  635:     */   
/*  636:     */   protected boolean clearSelection(boolean postChange)
/*  637:     */   {
/*  638: 954 */     return (clearSelectedJoint(postChange)) || (clearSelectedMembers(postChange));
/*  639:     */   }
/*  640:     */   
/*  641:     */   public void beginAreaSelection(boolean extendSelection)
/*  642:     */   {
/*  643: 968 */     if ((!clearSelectedJoint(true)) && (!extendSelection)) {
/*  644: 969 */       clearSelection(true);
/*  645:     */     }
/*  646:     */   }
/*  647:     */   
/*  648:     */   public boolean selectMembers(Rectangle2D.Double rectangleCursorWorld)
/*  649:     */   {
/*  650: 980 */     boolean change = false;
/*  651: 981 */     Iterator<Member> me = this.members.iterator();
/*  652: 982 */     while (me.hasNext())
/*  653:     */     {
/*  654: 983 */       Member member = (Member)me.next();
/*  655: 984 */       if ((rectangleCursorWorld.contains(member.getJointA().getPointWorld())) && (rectangleCursorWorld.contains(member.getJointB().getPointWorld()))) {
/*  656: 986 */         if (selectMember(member, true)) {
/*  657: 987 */           change = true;
/*  658:     */         }
/*  659:     */       }
/*  660:     */     }
/*  661: 991 */     if (change) {
/*  662: 992 */       fireSelectionChange();
/*  663:     */     }
/*  664: 994 */     return change;
/*  665:     */   }
/*  666:     */   
/*  667:     */   public boolean selectMembers(MemberTable memberTable, int firstIndex, int lastIndex)
/*  668:     */   {
/*  669:1008 */     ListSelectionModel selectionModel = memberTable.getSelectionModel();
/*  670:     */     
/*  671:1010 */     boolean change = clearSelectedJoint(false);
/*  672:1011 */     for (int i = firstIndex; (i <= lastIndex) && (i < this.members.size()); i++)
/*  673:     */     {
/*  674:1012 */       Member member = (Member)this.members.get(memberTable.convertRowIndexToModel(i));
/*  675:1013 */       if (selectMember(member, selectionModel.isSelectedIndex(i))) {
/*  676:1014 */         change = true;
/*  677:     */       }
/*  678:     */     }
/*  679:1017 */     if (change) {
/*  680:1018 */       fireSelectionChange();
/*  681:     */     }
/*  682:1020 */     return change;
/*  683:     */   }
/*  684:     */   
/*  685:     */   public void selectAllMembers()
/*  686:     */   {
/*  687:1027 */     boolean change = false;
/*  688:1028 */     if ((this.lastSelected instanceof Joint)) {
/*  689:1029 */       clearSelection(false);
/*  690:     */     }
/*  691:1031 */     Iterator<Member> me = this.members.iterator();
/*  692:1032 */     while (me.hasNext())
/*  693:     */     {
/*  694:1033 */       Member member = (Member)me.next();
/*  695:1034 */       if (selectMember(member, true)) {
/*  696:1035 */         change = true;
/*  697:     */       }
/*  698:     */     }
/*  699:1038 */     if (change) {
/*  700:1039 */       fireSelectionChange();
/*  701:     */     }
/*  702:     */   }
/*  703:     */   
/*  704:     */   public boolean isSelection()
/*  705:     */   {
/*  706:1049 */     return this.lastSelected != null;
/*  707:     */   }
/*  708:     */   
/*  709:     */   public boolean isSelectedMember()
/*  710:     */   {
/*  711:1058 */     return this.lastSelected instanceof Member;
/*  712:     */   }
/*  713:     */   
/*  714:     */   public int getAllowedShapeChanges()
/*  715:     */   {
/*  716:1067 */     int mask = 0;
/*  717:1068 */     Iterator<Member> me = this.members.iterator();
/*  718:1069 */     while (me.hasNext())
/*  719:     */     {
/*  720:1070 */       Member member = (Member)me.next();
/*  721:1071 */       if (member.isSelected()) {
/*  722:1072 */         mask |= this.inventory.getAllowedShapeChanges(member.getShape());
/*  723:     */       }
/*  724:     */     }
/*  725:1075 */     return mask;
/*  726:     */   }
/*  727:     */   
/*  728:     */   public Joint getSelectedJoint()
/*  729:     */   {
/*  730:1084 */     return (this.lastSelected instanceof Joint) ? (Joint)this.lastSelected : null;
/*  731:     */   }
/*  732:     */   
/*  733:     */   public boolean selectMember(Member member, boolean select)
/*  734:     */   {
/*  735:1095 */     if (member.setSelected(select))
/*  736:     */     {
/*  737:1096 */       this.lastSelected = member;
/*  738:1097 */       return true;
/*  739:     */     }
/*  740:1099 */     return false;
/*  741:     */   }
/*  742:     */   
/*  743:     */   public Member[] getSelectedMembers()
/*  744:     */   {
/*  745:1108 */     ArrayList<Member> memberList = new ArrayList();
/*  746:1109 */     Iterator<Member> me = this.members.iterator();
/*  747:1110 */     while (me.hasNext())
/*  748:     */     {
/*  749:1111 */       Member member = (Member)me.next();
/*  750:1112 */       if (member.isSelected()) {
/*  751:1113 */         memberList.add(member);
/*  752:     */       }
/*  753:     */     }
/*  754:1116 */     return (Member[])memberList.toArray(new Member[memberList.size()]);
/*  755:     */   }
/*  756:     */   
/*  757:     */   public Joint[] getJointsToDeleteWithSelectedMembers()
/*  758:     */   {
/*  759:1127 */     Set<Joint> jointsTouchingUnselectedMembers = new HashSet();
/*  760:1128 */     Set<Joint> jointsTouchingAnyMember = new HashSet();
/*  761:1129 */     Iterator<Member> me = this.members.iterator();
/*  762:1130 */     while (me.hasNext())
/*  763:     */     {
/*  764:1131 */       Member member = (Member)me.next();
/*  765:1132 */       jointsTouchingAnyMember.add(member.getJointA());
/*  766:1133 */       jointsTouchingAnyMember.add(member.getJointB());
/*  767:1134 */       if (!member.isSelected())
/*  768:     */       {
/*  769:1135 */         jointsTouchingUnselectedMembers.add(member.getJointA());
/*  770:1136 */         jointsTouchingUnselectedMembers.add(member.getJointB());
/*  771:     */       }
/*  772:     */     }
/*  773:1139 */     ArrayList<Joint> jointList = new ArrayList();
/*  774:1140 */     Iterator<Joint> je = this.joints.iterator();
/*  775:1141 */     while (je.hasNext())
/*  776:     */     {
/*  777:1142 */       Joint joint = (Joint)je.next();
/*  778:1143 */       if ((!joint.isFixed()) && (jointsTouchingAnyMember.contains(joint)) && (!jointsTouchingUnselectedMembers.contains(joint))) {
/*  779:1146 */         jointList.add(joint);
/*  780:     */       }
/*  781:     */     }
/*  782:1149 */     return (Joint[])jointList.toArray(new Joint[jointList.size()]);
/*  783:     */   }
/*  784:     */   
/*  785:     */   public void deleteSelection()
/*  786:     */   {
/*  787:1156 */     if ((this.lastSelected instanceof Joint)) {
/*  788:1157 */       new DeleteJointCommand(this).execute(this.undoManager);
/*  789:1158 */     } else if ((this.lastSelected instanceof Member)) {
/*  790:1159 */       new DeleteMembersCommand(this).execute(this.undoManager);
/*  791:     */     }
/*  792:1161 */     this.lastSelected = null;
/*  793:     */   }
/*  794:     */   
/*  795:     */   void changeSelectedMembers(int materialIndex, int sectionIndex, int sizeIndex)
/*  796:     */   {
/*  797:1172 */     if ((this.lastSelected instanceof Member)) {
/*  798:1173 */       new ChangeMembersCommand(this, materialIndex, sectionIndex, sizeIndex).execute(this.undoManager);
/*  799:     */     }
/*  800:     */   }
/*  801:     */   
/*  802:     */   public void delete(Editable element)
/*  803:     */   {
/*  804:1183 */     if ((element instanceof Joint)) {
/*  805:1184 */       new DeleteJointCommand(this, (Joint)element).execute(this.undoManager);
/*  806:1185 */     } else if ((element instanceof Member)) {
/*  807:1186 */       new DeleteMembersCommand(this, (Member)element).execute(this.undoManager);
/*  808:     */     }
/*  809:     */   }
/*  810:     */   
/*  811:     */   public StockSelector.Descriptor getSelectedStock()
/*  812:     */   {
/*  813:1201 */     Iterator<Member> e = this.members.iterator();
/*  814:1202 */     StockSelector.Descriptor descriptor = null;
/*  815:1203 */     while (e.hasNext())
/*  816:     */     {
/*  817:1204 */       Member member = (Member)e.next();
/*  818:1205 */       if (member.isSelected()) {
/*  819:1206 */         if (descriptor == null)
/*  820:     */         {
/*  821:1207 */           descriptor = new StockSelector.Descriptor(member);
/*  822:     */         }
/*  823:     */         else
/*  824:     */         {
/*  825:1210 */           if (descriptor.materialIndex != member.getMaterial().getIndex()) {
/*  826:1211 */             descriptor.materialIndex = -1;
/*  827:     */           }
/*  828:1213 */           if (descriptor.sectionIndex != member.getShape().getSection().getIndex()) {
/*  829:1214 */             descriptor.sectionIndex = -1;
/*  830:     */           }
/*  831:1216 */           if (descriptor.sizeIndex != member.getShape().getSizeIndex()) {
/*  832:1217 */             descriptor.sizeIndex = -1;
/*  833:     */           }
/*  834:     */         }
/*  835:     */       }
/*  836:     */     }
/*  837:1222 */     return descriptor;
/*  838:     */   }
/*  839:     */   
/*  840:     */   public int getSnapMultiple()
/*  841:     */   {
/*  842:1226 */     int rtn = DraftingGrid.maxSnapMultiple;
/*  843:1227 */     Iterator<Joint> e = this.joints.iterator();
/*  844:1228 */     while (e.hasNext())
/*  845:     */     {
/*  846:1229 */       Affine.Point loc = ((Joint)e.next()).getPointWorld();
/*  847:1230 */       rtn = Math.min(rtn, Math.min(DraftingGrid.snapMultipleOf(loc.x), DraftingGrid.snapMultipleOf(loc.y)));
/*  848:     */     }
/*  849:1232 */     return rtn;
/*  850:     */   }
/*  851:     */   
/*  852:     */   public void read(String s)
/*  853:     */   {
/*  854:1242 */     clearIterations();
/*  855:1243 */     super.read(s);
/*  856:     */     
/*  857:1245 */     this.undoManager.newSession();
/*  858:1246 */     this.lastSelected = null;
/*  859:1247 */     saveIteration();
/*  860:1248 */     fireIterationChange();
/*  861:1249 */     fireStructureChange();
/*  862:1250 */     fireSelectionChange();
/*  863:1251 */     fireAnalysisChange();
/*  864:     */   }
/*  865:     */   
/*  866:     */   public void read(File f)
/*  867:     */     throws IOException
/*  868:     */   {
/*  869:1256 */     clearIterations();
/*  870:1257 */     super.read(f);
/*  871:1258 */     this.undoManager.load();
/*  872:1259 */     this.lastSelected = null;
/*  873:1260 */     saveIteration();
/*  874:1261 */     fireIterationChange();
/*  875:1262 */     fireStructureChange();
/*  876:1263 */     fireSelectionChange();
/*  877:1264 */     fireAnalysisChange();
/*  878:     */   }
/*  879:     */   
/*  880:     */   public void write(File f)
/*  881:     */     throws IOException
/*  882:     */   {
/*  883:1269 */     super.write(f);
/*  884:1270 */     this.undoManager.save();
/*  885:     */   }
/*  886:     */   
/*  887:     */   public String toTabDelimitedText()
/*  888:     */   {
/*  889:1280 */     ResourceMap resourceMap = WPBDApp.getResourceMap(EditableBridgeModel.class);
/*  890:1281 */     if (this.analysis.getStatus() <= 2) {
/*  891:1282 */       return resourceMap.getString("invalid.text", new Object[0]);
/*  892:     */     }
/*  893:1284 */     StringBuilder str = new StringBuilder();
/*  894:1285 */     Formatter formatter = new Formatter(str, Locale.US);
/*  895:     */     
/*  896:1287 */     str.append(this.projectName);
/*  897:1288 */     str.append('\n');
/*  898:     */     
/*  899:1290 */     str.append(resourceMap.getString("projectId.text", new Object[0]));
/*  900:1291 */     str.append(this.projectId);
/*  901:1292 */     str.append('\n');
/*  902:     */     
/*  903:1294 */     str.append(resourceMap.getString("designedBy.text", new Object[0]));
/*  904:1295 */     str.append(this.designedBy);
/*  905:1296 */     str.append('\n');
/*  906:     */     
/*  907:1298 */     str.append(resourceMap.getString("cvsHeaders.text", new Object[0]));
/*  908:1299 */     str.append('\n');
/*  909:1300 */     Iterator<Member> em = this.members.iterator();
/*  910:1301 */     while (em.hasNext())
/*  911:     */     {
/*  912:1302 */       Member member = (Member)em.next();
/*  913:1303 */       int i = member.getIndex();
/*  914:1304 */       formatter.format("%d\t%s\t%s\t%s\t%.2f\t%.2f\t%.2f\t%s\t%.2f\t%.2f\t%s\n", new Object[] { Integer.valueOf(member.getNumber()), member.getMaterial().getShortName(), member.getShape().getSection().getName(), member.getShape().getName(), Double.valueOf(member.getLength()), Double.valueOf(this.analysis.getMemberCompressiveForce(i)), Double.valueOf(this.analysis.getMemberCompressiveStrength(i)), MemberTable.getMemberStatusString(member.getCompressionForceStrengthRatio() <= 1.0D ? 1 : false), Double.valueOf(this.analysis.getMemberTensileForce(i)), Double.valueOf(this.analysis.getMemberTensileStrength(i)), MemberTable.getMemberStatusString(member.getTensionForceStrengthRatio() <= 1.0D ? 1 : false) });
/*  915:     */     }
/*  916:1312 */     return str.toString();
/*  917:     */   }
/*  918:     */   
/*  919:     */   public String toHTML()
/*  920:     */   {
/*  921:1379 */     StringBuilder str = new StringBuilder();
/*  922:1380 */     str.append(String.format("<html><head><title>Load Test Results Report</title>\n<style>\n  table { font-size : 8pt; font-family: arial, helvetica, sans-serif }\n  th { font-weight : bold }\n</style></head><body>\n<table border=1 cellspacing=0 cellpadding=2>\n<tr><th colspan=15>Load Test Results Report (Design Iteration #%d, Scenario id %d, number %s, Cost $%.2f)</th></tr>\n<tr>\n <th colspan=5>Member</th><th>&nbsp;</th><th colspan=4>Compression</th><th>&nbsp;</th><th colspan=4>Tension</th></tr>\n<tr>\n <th>#</th><th>Size</th><th>Section</th><th>Matl.</th><th>Length<br>(m)</th>\n <th>&nbsp;</th>\n <th>Force<br>(kN)</th><th>Strength<br>(kN)</th><th>Force/<br>Strength</th><th>Status</th>\n <th>&nbsp;</th>\n <th>Force<br>(kN)</th><th>Strength<br>(kN)</th><th>Force/<br>Strength</th><th>Status</th>\n</tr>\n", new Object[] { Integer.valueOf(this.iterationNumber), Long.valueOf(this.designConditions.getCodeLong()), this.designConditions.getTag(), Double.valueOf(this.analysis.getStatus() == 4 ? getTotalCost() : 0.0D) }));
/*  923:     */     
/*  924:     */ 
/*  925:     */ 
/*  926:     */ 
/*  927:1385 */     Iterator<Member> mi = this.members.iterator();
/*  928:1386 */     while (mi.hasNext())
/*  929:     */     {
/*  930:1387 */       Member m = (Member)mi.next();
/*  931:1388 */       int i = m.getIndex();
/*  932:1389 */       double cr = m.getCompressionForceStrengthRatio();
/*  933:1390 */       double tr = m.getTensionForceStrengthRatio();
/*  934:1391 */       double s = m.getSlenderness();
/*  935:1392 */       if (s > this.designConditions.getAllowableSlenderness()) {
/*  936:1393 */         str.append(String.format("<tr>\n <td align=\"right\">%d</td>\n <td align=\"right\">%s</td>\n <td align=\"center\">%s</td>\n <td align=\"center\">%s</td>\n <td align=\"right\">%.2f</td>\n <td>&nbsp;</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">0.0</td>\n <td align=\"right\">&nbsp;oo</td>\n <td bgcolor=\"#FF60FF\" align=\"center\">Fail</td>\n <td>&nbsp;</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">0.0</td>\n <td align=\"right\">&nbsp;oo</td>\n <td bgcolor=\"#FF60FF\" align=\"center\">Fail</td>\n</tr>\n", new Object[] { Integer.valueOf(m.getNumber()), m.getShape().getName(), m.getShape().getSection().getShortName(), m.getMaterial().getShortName(), Double.valueOf(m.getLength()), Double.valueOf(this.analysis.getMemberCompressiveForce(i)), Double.valueOf(this.analysis.getMemberTensileForce(i)) }));
/*  937:     */       } else {
/*  938:1402 */         str.append(String.format("<tr>\n <td align=\"right\">%d</td>\n <td align=\"right\">%s</td>\n <td align=\"center\">%s</td>\n <td align=\"center\">%s</td>\n <td align=\"right\">%.2f</td>\n <td>&nbsp;</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">%.4f</td>\n %s\n <td>&nbsp;</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">%.1f</td>\n <td align=\"right\">%.4f</td>\n %s\n</tr>\n", new Object[] { Integer.valueOf(m.getNumber()), m.getShape().getName(), m.getShape().getSection().getShortName(), m.getMaterial().getShortName(), Double.valueOf(m.getLength()), Double.valueOf(this.analysis.getMemberCompressiveForce(i)), Double.valueOf(this.analysis.getMemberCompressiveStrength(i)), Double.valueOf(cr), cr > 1.0D ? "<td bgcolor=\"#FFC0C0\" align=\"center\">Fail</td>" : "<td bgcolor=\"#C0FFC0\" align=\"center\">OK</td>", Double.valueOf(this.analysis.getMemberTensileForce(i)), Double.valueOf(this.analysis.getMemberTensileStrength(i)), Double.valueOf(tr), tr > 1.0D ? "<td bgcolor=\"#FFC0C0\" align=\"center\">Fail</td>" : "<td bgcolor=\"#C0FFC0\" align=\"center\">OK</td>" }));
/*  939:     */       }
/*  940:     */     }
/*  941:1418 */     str.append("</table>\n</body></html>");
/*  942:1419 */     return str.toString();
/*  943:     */   }
/*  944:     */   
/*  945:     */   public String toText()
/*  946:     */   {
/*  947:1469 */     StringBuilder str = new StringBuilder();
/*  948:1470 */     str.append(this.iterationNumber);
/*  949:1471 */     str.append('\t');
/*  950:1472 */     str.append(this.designConditions.getCodeLong());
/*  951:1473 */     str.append('\t');
/*  952:1474 */     str.append(this.designConditions.getTag());
/*  953:1475 */     str.append('\t');
/*  954:1476 */     str.append(this.analysis.getStatus() == 4 ? getTotalCost() : 0.0D);
/*  955:1477 */     str.append('\n');
/*  956:1478 */     Iterator<Member> mi = this.members.iterator();
/*  957:1479 */     while (mi.hasNext())
/*  958:     */     {
/*  959:1480 */       Member m = (Member)mi.next();
/*  960:1481 */       int i = m.getIndex();
/*  961:1482 */       double cr = m.getCompressionForceStrengthRatio();
/*  962:1483 */       double tr = m.getTensionForceStrengthRatio();
/*  963:1484 */       double s = m.getSlenderness();
/*  964:1485 */       str.append(m.getNumber());
/*  965:1486 */       str.append('\t');
/*  966:1487 */       str.append(m.getShape().getName());
/*  967:1488 */       str.append('\t');
/*  968:1489 */       str.append(m.getShape().getSection().getShortName());
/*  969:1490 */       str.append('\t');
/*  970:1491 */       str.append(m.getMaterial().getShortName());
/*  971:1492 */       str.append('\t');
/*  972:1493 */       str.append(m.getLength());
/*  973:1494 */       str.append('\t');
/*  974:1495 */       str.append(this.analysis.getMemberCompressiveForce(i));
/*  975:1496 */       str.append('\t');
/*  976:1497 */       str.append(this.analysis.getMemberCompressiveStrength(i));
/*  977:1498 */       str.append('\t');
/*  978:1499 */       str.append(cr);
/*  979:1500 */       str.append('\t');
/*  980:1501 */       str.append(cr > 1.0D ? "Fail" : s > this.designConditions.getAllowableSlenderness() ? "Slenderness" : "OK");
/*  981:1502 */       str.append('\t');
/*  982:1503 */       str.append(this.analysis.getMemberTensileForce(i));
/*  983:1504 */       str.append('\t');
/*  984:1505 */       str.append(this.analysis.getMemberTensileStrength(i));
/*  985:1506 */       str.append('\t');
/*  986:1507 */       str.append(tr);
/*  987:1508 */       str.append('\t');
/*  988:1509 */       str.append(tr > 1.0D ? "Fail" : s > this.designConditions.getAllowableSlenderness() ? "Slenderness" : "OK");
/*  989:1510 */       str.append('\n');
/*  990:     */     }
/*  991:1512 */     return str.toString();
/*  992:     */   }
/*  993:     */   
/*  994:     */   public static void printTestTables()
/*  995:     */   {
/*  996:1516 */     String year = Integer.toString(2014);
/*  997:1517 */     File egDir = new File("eg/" + year);
/*  998:1518 */     File[] files = egDir.listFiles();
/*  999:1519 */     EditableBridgeModel bridge = new EditableBridgeModel();
/* 1000:1520 */     if (files != null) {
/* 1001:1521 */       for (int i = 0; i < files.length; i++) {
/* 1002:1522 */         if (files[i].isFile()) {
/* 1003:     */           try
/* 1004:     */           {
/* 1005:1531 */             System.err.println(files[i] + ":");
/* 1006:1532 */             bridge.read(files[i]);
/* 1007:1533 */             bridge.analyze();
/* 1008:1534 */             String fullName = files[i].getName();
/* 1009:1535 */             String baseName = fullName.substring(0, fullName.lastIndexOf('.'));
/* 1010:1536 */             BufferedWriter out = new BufferedWriter(new FileWriter("eg/" + year + "/html/" + baseName + ".htm"));
/* 1011:1537 */             out.write(bridge.toHTML());
/* 1012:1538 */             out.close();
/* 1013:1539 */             out = new BufferedWriter(new FileWriter("eg/" + year + "/log/" + baseName + ".txt"));
/* 1014:1540 */             out.write(bridge.toText());
/* 1015:1541 */             out.close();
/* 1016:     */           }
/* 1017:     */           catch (IOException ex)
/* 1018:     */           {
/* 1019:1543 */             Logger.getLogger(EditableBridgeModel.class.getName()).log(Level.SEVERE, "Failed to open example.", ex);
/* 1020:     */           }
/* 1021:     */         }
/* 1022:     */       }
/* 1023:     */     }
/* 1024:     */   }
/* 1025:     */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.EditableBridgeModel
 * JD-Core Version:    0.7.0.1
 */