/*    1:     */ package wpbd;
/*    2:     */ 
/*    3:     */ import java.awt.geom.Rectangle2D;
/*    4:     */ import java.awt.geom.Rectangle2D.Double;
/*    5:     */ import java.io.File;
/*    6:     */ import java.io.FileOutputStream;
/*    7:     */ import java.io.IOException;
/*    8:     */ import java.io.OutputStream;
/*    9:     */ import java.io.PrintWriter;
/*   10:     */ import java.io.UnsupportedEncodingException;
/*   11:     */ import java.text.DecimalFormat;
/*   12:     */ import java.text.NumberFormat;
/*   13:     */ import java.text.SimpleDateFormat;
/*   14:     */ import java.util.ArrayList;
/*   15:     */ import java.util.Arrays;
/*   16:     */ import java.util.Calendar;
/*   17:     */ import java.util.Collection;
/*   18:     */ import java.util.Comparator;
/*   19:     */ import java.util.Formatter;
/*   20:     */ import java.util.HashMap;
/*   21:     */ import java.util.HashSet;
/*   22:     */ import java.util.Iterator;
/*   23:     */ import java.util.Locale;
/*   24:     */ import java.util.Set;
/*   25:     */ import java.util.TreeMap;
/*   26:     */ import java.util.logging.Level;
/*   27:     */ import java.util.logging.Logger;
/*   28:     */ import org.jdesktop.application.ResourceMap;
/*   29:     */ 
/*   30:     */ public class BridgeModel
/*   31:     */ {
/*   32:     */   public static final int version = 2014;
/*   33:     */   protected static final char DELIM = '|';
/*   34:     */   protected static final int JOINT_COORD_LEN = 3;
/*   35:     */   protected static final int MEMBER_JOINT_LEN = 2;
/*   36:     */   protected static final int MEMBER_MATERIAL_LEN = 1;
/*   37:     */   protected static final int MEMBER_SECTION_LEN = 1;
/*   38:     */   protected static final int MEMBER_SIZE_LEN = 2;
/*   39:     */   protected static final int N_JOINTS_LEN = 2;
/*   40:     */   protected static final int N_MEMBERS_LEN = 3;
/*   41:     */   protected static final int SCENARIO_CODE_LEN = 10;
/*   42:     */   protected static final int YEAR_LEN = 4;
/*   43:     */   protected DesignConditions designConditions;
/*   44:     */   protected String designedBy;
/*   45:     */   protected final NumberFormat decimalFormatter;
/*   46:     */   protected final Inventory inventory;
/*   47:     */   protected int iterationNumber;
/*   48:     */   protected final ArrayList<Joint> joints;
/*   49:     */   protected final ArrayList<Member> members;
/*   50:     */   protected TreeMap<MaterialSectionPair, Double> materialSectionPairs;
/*   51:     */   protected TreeMap<MaterialShapePair, Integer> materialShapePairs;
/*   52:     */   protected HashSet<MaterialShapePair> stockSet;
/*   53:     */   protected double labelPosition;
/*   54:     */   protected String projectId;
/*   55:     */   protected String projectName;
/*   56:     */   private byte[] readBuf;
/*   57:     */   private int readPtr;
/*   58:     */   protected final Costs costs;
/*   59:     */   private final NumberFormat currencyFormat;
/*   60:     */   private final NumberFormat intFormat;
/*   61:     */   
/*   62:     */   public BridgeModel()
/*   63:     */   {
/*   64:  98 */     this.designedBy = "";
/*   65:     */     
/*   66:     */ 
/*   67:     */ 
/*   68: 102 */     this.decimalFormatter = new DecimalFormat("0.00");
/*   69:     */     
/*   70:     */ 
/*   71:     */ 
/*   72: 106 */     this.inventory = new Inventory();
/*   73:     */     
/*   74:     */ 
/*   75:     */ 
/*   76: 110 */     this.iterationNumber = 1;
/*   77:     */     
/*   78:     */ 
/*   79:     */ 
/*   80: 114 */     this.joints = new ArrayList();
/*   81:     */     
/*   82:     */ 
/*   83:     */ 
/*   84: 118 */     this.members = new ArrayList();
/*   85:     */     
/*   86:     */ 
/*   87:     */ 
/*   88: 122 */     this.materialSectionPairs = new TreeMap();
/*   89:     */     
/*   90:     */ 
/*   91:     */ 
/*   92: 126 */     this.materialShapePairs = new TreeMap();
/*   93:     */     
/*   94:     */ 
/*   95:     */ 
/*   96: 130 */     this.stockSet = new HashSet();
/*   97:     */     
/*   98:     */ 
/*   99:     */ 
/*  100: 134 */     this.labelPosition = 2.0D;
/*  101:     */     
/*  102:     */ 
/*  103:     */ 
/*  104:     */ 
/*  105:     */ 
/*  106:     */ 
/*  107:     */ 
/*  108: 142 */     this.projectName = "Dennis H. Mahan Memorial Bridge";
/*  109:     */     
/*  110:     */ 
/*  111:     */ 
/*  112:     */ 
/*  113:     */ 
/*  114:     */ 
/*  115:     */ 
/*  116:     */ 
/*  117:     */ 
/*  118:     */ 
/*  119:     */ 
/*  120: 154 */     this.costs = new Costs();
/*  121:     */     
/*  122:     */ 
/*  123:     */ 
/*  124: 158 */     this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
/*  125: 159 */     this.intFormat = NumberFormat.getIntegerInstance();
/*  126:     */   }
/*  127:     */   
/*  128:     */   public void clearStructure()
/*  129:     */   {
/*  130: 165 */     this.members.clear();
/*  131: 166 */     this.joints.clear();
/*  132:     */   }
/*  133:     */   
/*  134:     */   public static class MaterialShapePair
/*  135:     */     implements Comparable
/*  136:     */   {
/*  137:     */     public Material material;
/*  138:     */     public Shape shape;
/*  139:     */     
/*  140:     */     public MaterialShapePair(Material material, Shape shape)
/*  141:     */     {
/*  142: 190 */       this.material = material;
/*  143: 191 */       this.shape = shape;
/*  144:     */     }
/*  145:     */     
/*  146:     */     public int hashCode()
/*  147:     */     {
/*  148: 201 */       return this.material.hashCode() + this.shape.hashCode();
/*  149:     */     }
/*  150:     */     
/*  151:     */     public boolean equals(Object o)
/*  152:     */     {
/*  153: 212 */       if ((o instanceof MaterialShapePair))
/*  154:     */       {
/*  155: 213 */         MaterialShapePair other = (MaterialShapePair)o;
/*  156: 214 */         return (this.material == other.material) && (this.shape == other.shape);
/*  157:     */       }
/*  158: 216 */       return false;
/*  159:     */     }
/*  160:     */     
/*  161:     */     public String toString()
/*  162:     */     {
/*  163: 226 */       return this.shape.getName() + " mm " + this.material + " " + this.shape.getSection();
/*  164:     */     }
/*  165:     */     
/*  166:     */     public int compareTo(Object o)
/*  167:     */     {
/*  168: 236 */       if (o == null) {
/*  169: 237 */         return 1;
/*  170:     */       }
/*  171: 239 */       MaterialShapePair other = (MaterialShapePair)o;
/*  172: 240 */       int cmp = this.shape.getSizeIndex() - other.shape.getSizeIndex();
/*  173: 241 */       if (cmp != 0) {
/*  174: 242 */         return cmp;
/*  175:     */       }
/*  176: 244 */       cmp = this.material.getIndex() - other.material.getIndex();
/*  177: 245 */       if (cmp != 0) {
/*  178: 246 */         return cmp;
/*  179:     */       }
/*  180: 248 */       return this.shape.getSection().getIndex() - other.shape.getSection().getIndex();
/*  181:     */     }
/*  182:     */   }
/*  183:     */   
/*  184:     */   public static class MaterialSectionPair
/*  185:     */     implements Comparable
/*  186:     */   {
/*  187:     */     public Material material;
/*  188:     */     public CrossSection section;
/*  189:     */     
/*  190:     */     public MaterialSectionPair(Material material, CrossSection section)
/*  191:     */     {
/*  192: 272 */       this.material = material;
/*  193: 273 */       this.section = section;
/*  194:     */     }
/*  195:     */     
/*  196:     */     public int hashCode()
/*  197:     */     {
/*  198: 283 */       return this.material.hashCode() + this.section.hashCode();
/*  199:     */     }
/*  200:     */     
/*  201:     */     public boolean equals(Object o)
/*  202:     */     {
/*  203: 294 */       if ((o instanceof MaterialSectionPair))
/*  204:     */       {
/*  205: 295 */         MaterialSectionPair other = (MaterialSectionPair)o;
/*  206: 296 */         return (this.material == other.material) && (this.section == other.section);
/*  207:     */       }
/*  208: 298 */       return false;
/*  209:     */     }
/*  210:     */     
/*  211:     */     public String toString()
/*  212:     */     {
/*  213: 308 */       return this.material + " " + this.section.getName();
/*  214:     */     }
/*  215:     */     
/*  216:     */     public int compareTo(Object o)
/*  217:     */     {
/*  218: 318 */       if (o == null) {
/*  219: 319 */         return 1;
/*  220:     */       }
/*  221: 321 */       MaterialSectionPair other = (MaterialSectionPair)o;
/*  222: 322 */       int cmp = this.material.getIndex() - other.material.getIndex();
/*  223: 323 */       if (cmp != 0) {
/*  224: 324 */         return cmp;
/*  225:     */       }
/*  226: 326 */       return this.section.getIndex() - other.section.getIndex();
/*  227:     */     }
/*  228:     */   }
/*  229:     */   
/*  230:     */   public class Costs
/*  231:     */   {
/*  232:     */     public TreeMap<BridgeModel.MaterialShapePair, Integer> materialShapePairs;
/*  233:     */     public TreeMap<BridgeModel.MaterialSectionPair, Double> materialSectionPairs;
/*  234:     */     public DesignConditions conditions;
/*  235:     */     public Inventory inventory;
/*  236:     */     public int nConnections;
/*  237:     */     public String[] notes;
/*  238:     */     
/*  239:     */     public Costs() {}
/*  240:     */     
/*  241:     */     String toTabDelimitedText()
/*  242:     */     {
/*  243: 365 */       ResourceMap resourceMap = WPBDApp.getResourceMap(CostReportTableModel.class);
/*  244: 366 */       StringBuilder str = new StringBuilder();
/*  245: 367 */       Formatter formatter = new Formatter(str, Locale.US);
/*  246: 368 */       if (this.notes != null) {
/*  247: 369 */         for (int i = 0; i < this.notes.length; i++) {
/*  248: 370 */           formatter.format("%s\n", new Object[] { this.notes[i] });
/*  249:     */         }
/*  250:     */       }
/*  251: 373 */       formatter.format("%s\n", new Object[] { resourceMap.getString("columnIds.text", new Object[0]).replace('|', '\t') });
/*  252: 374 */       int nProductRows = BridgeModel.this.costs.materialShapePairs.size();
/*  253:     */       
/*  254: 376 */       Iterator<BridgeModel.MaterialSectionPair> mtlSecIt = BridgeModel.this.costs.materialSectionPairs.keySet().iterator();
/*  255: 377 */       boolean initial = true;
/*  256: 378 */       double totalMtlCost = 0.0D;
/*  257: 379 */       while (mtlSecIt.hasNext())
/*  258:     */       {
/*  259: 380 */         BridgeModel.MaterialSectionPair pair = (BridgeModel.MaterialSectionPair)mtlSecIt.next();
/*  260: 382 */         if (initial)
/*  261:     */         {
/*  262: 383 */           formatter.format("%s\t", new Object[] { resourceMap.getString("materialCost.text", new Object[0]) });
/*  263: 384 */           initial = false;
/*  264:     */         }
/*  265:     */         else
/*  266:     */         {
/*  267: 387 */           formatter.format("\t", new Object[0]);
/*  268:     */         }
/*  269: 390 */         formatter.format("%s\t", new Object[] { pair.toString() });
/*  270:     */         
/*  271: 392 */         double weight = ((Double)BridgeModel.this.costs.materialSectionPairs.get(pair)).doubleValue();
/*  272: 393 */         double cost = pair.material.getCost(pair.section);
/*  273: 394 */         formatter.format("%s\t", new Object[] { resourceMap.getString("materialCostNote.text", new Object[] { Double.valueOf(weight), BridgeModel.this.currencyFormat.format(cost) }) });
/*  274:     */         
/*  275: 396 */         double mtlCost = 2.0D * weight * cost;
/*  276: 397 */         formatter.format("%s\n", new Object[] { BridgeModel.this.currencyFormat.format(mtlCost) });
/*  277: 398 */         totalMtlCost += mtlCost;
/*  278:     */       }
/*  279: 401 */       formatter.format("\n", new Object[0]);
/*  280:     */       
/*  281: 403 */       formatter.format("%s\t\t", new Object[] { resourceMap.getString("connectionCost.text", new Object[0]) });
/*  282: 404 */       double connectionFee = BridgeModel.this.costs.inventory.getConnectionFee();
/*  283: 405 */       formatter.format("%s\t", new Object[] { resourceMap.getString("connectionCostNote.text", new Object[] { Integer.valueOf(BridgeModel.this.costs.nConnections), Double.valueOf(connectionFee) }) });
/*  284: 406 */       double connectionCost = 2 * this.nConnections * connectionFee;
/*  285: 407 */       formatter.format("%s\n", new Object[] { BridgeModel.this.currencyFormat.format(connectionCost) });
/*  286:     */       
/*  287: 409 */       formatter.format("\n", new Object[0]);
/*  288:     */       
/*  289: 411 */       Iterator<BridgeModel.MaterialShapePair> mtlShpIt = BridgeModel.this.costs.materialShapePairs.keySet().iterator();
/*  290: 412 */       initial = true;
/*  291: 413 */       while (mtlShpIt.hasNext())
/*  292:     */       {
/*  293: 414 */         BridgeModel.MaterialShapePair pair = (BridgeModel.MaterialShapePair)mtlShpIt.next();
/*  294: 416 */         if (initial)
/*  295:     */         {
/*  296: 417 */           formatter.format("%s\t", new Object[] { resourceMap.getString("productCost.text", new Object[0]) });
/*  297: 418 */           initial = false;
/*  298:     */         }
/*  299:     */         else
/*  300:     */         {
/*  301: 421 */           formatter.format("\t", new Object[0]);
/*  302:     */         }
/*  303: 424 */         formatter.format("%2d - %s\t", new Object[] { BridgeModel.this.costs.materialShapePairs.get(pair), pair.toString() });
/*  304:     */         
/*  305: 426 */         formatter.format("%s\t", new Object[] { resourceMap.getString("productCostNote.text", new Object[0]) });
/*  306:     */         
/*  307: 428 */         formatter.format("%s\n", new Object[] { BridgeModel.this.currencyFormat.format(BridgeModel.this.costs.inventory.getOrderingFee()) });
/*  308:     */       }
/*  309: 431 */       formatter.format("\n", new Object[0]);
/*  310: 432 */       double totalProductCost = nProductRows * BridgeModel.this.costs.inventory.getOrderingFee();
/*  311:     */       
/*  312: 434 */       formatter.format("%s\t", new Object[] { resourceMap.getString("siteCost.text", new Object[0]) });
/*  313: 435 */       formatter.format("%s\t", new Object[] { resourceMap.getString("deckCost.text", new Object[0]) });
/*  314: 436 */       formatter.format("%s\t", new Object[] { resourceMap.getString("deckCostNote.text", new Object[] { Integer.valueOf(BridgeModel.this.costs.conditions.getNPanels()), BridgeModel.this.currencyFormat.format(BridgeModel.this.costs.conditions.getDeckCostRate()) }) });
/*  315:     */       
/*  316: 438 */       double deckCost = BridgeModel.this.costs.conditions.getNPanels() * BridgeModel.this.costs.conditions.getDeckCostRate();
/*  317: 439 */       formatter.format("%s\n\t", new Object[] { BridgeModel.this.currencyFormat.format(deckCost) });
/*  318: 440 */       formatter.format("%s\t", new Object[] { resourceMap.getString("excavationCost.text", new Object[0]) });
/*  319: 441 */       formatter.format("%s\t", new Object[] { resourceMap.getString("excavationCostNote.text", new Object[] { BridgeModel.this.intFormat.format(BridgeModel.this.costs.conditions.getExcavationVolume()), BridgeModel.this.currencyFormat.format(1.0D) }) });
/*  320:     */       
/*  321:     */ 
/*  322: 444 */       formatter.format("%s\n\t", new Object[] { BridgeModel.this.currencyFormat.format(BridgeModel.this.costs.conditions.getExcavationCost()) });
/*  323: 445 */       formatter.format("%s\t", new Object[] { resourceMap.getString("abutmentCost.text", new Object[0]) });
/*  324: 446 */       String abutmentType = resourceMap.getString(BridgeModel.this.costs.conditions.isArch() ? "arch.text" : "standard.text", new Object[0]);
/*  325: 447 */       formatter.format("%s\t", new Object[] { resourceMap.getString("abutmentCostNote.text", new Object[] { abutmentType, BridgeModel.this.currencyFormat.format(BridgeModel.this.costs.conditions.getAbutmentCost()) }) });
/*  326:     */       
/*  327: 449 */       formatter.format("%s\n\t", new Object[] { BridgeModel.this.currencyFormat.format(2.0D * BridgeModel.this.costs.conditions.getAbutmentCost()) });
/*  328: 450 */       formatter.format("%s\t", new Object[] { resourceMap.getString("pierCost.text", new Object[0]) });
/*  329: 451 */       if (BridgeModel.this.costs.conditions.isPier()) {
/*  330: 452 */         formatter.format("%s\t", new Object[] { resourceMap.getString("pierNote.text", new Object[] { BridgeModel.this.intFormat.format(BridgeModel.this.costs.conditions.getPierHeight()) }) });
/*  331:     */       } else {
/*  332: 455 */         formatter.format("%s\t", new Object[] { resourceMap.getString("noPierNote.text", new Object[0]) });
/*  333:     */       }
/*  334: 457 */       formatter.format("%s\n\t", new Object[] { BridgeModel.this.currencyFormat.format(BridgeModel.this.costs.conditions.getPierCost()) });
/*  335: 458 */       formatter.format("%s\t", new Object[] { resourceMap.getString("anchorageCost.text", new Object[0]) });
/*  336: 459 */       int nAnchorages = BridgeModel.this.costs.conditions.getNAnchorages();
/*  337: 460 */       if (nAnchorages == 0) {
/*  338: 461 */         formatter.format("%s\t", new Object[] { resourceMap.getString("noAnchoragesNote.text", new Object[0]) });
/*  339:     */       } else {
/*  340: 463 */         formatter.format("%s\t", new Object[] { resourceMap.getString("anchorageNote.text", new Object[] { Integer.valueOf(nAnchorages), BridgeModel.this.currencyFormat.format(6000.0D) }) });
/*  341:     */       }
/*  342: 466 */       double anchorageCost = nAnchorages * 6000.0D;
/*  343: 467 */       formatter.format("%s\n\n", new Object[] { BridgeModel.this.currencyFormat.format(anchorageCost) });
/*  344:     */       
/*  345: 469 */       formatter.format("%s\t", new Object[] { resourceMap.getString("totalCost.text", new Object[0]) });
/*  346: 470 */       formatter.format("%s\t", new Object[] { resourceMap.getString("sum.text", new Object[0]) });
/*  347: 471 */       formatter.format("%s\t", new Object[] { resourceMap.getString("sumNote.text", new Object[] { BridgeModel.this.currencyFormat.format(totalMtlCost), BridgeModel.this.currencyFormat.format(connectionCost), BridgeModel.this.currencyFormat.format(totalProductCost), BridgeModel.this.currencyFormat.format(BridgeModel.this.costs.conditions.getTotalFixedCost()) }) });
/*  348:     */       
/*  349:     */ 
/*  350:     */ 
/*  351:     */ 
/*  352: 476 */       formatter.format("%s\n", new Object[] { BridgeModel.this.currencyFormat.format(totalMtlCost + connectionCost + totalProductCost + BridgeModel.this.costs.conditions.getTotalFixedCost()) });
/*  353:     */       
/*  354: 478 */       return str.toString();
/*  355:     */     }
/*  356:     */   }
/*  357:     */   
/*  358:     */   public Costs getCosts()
/*  359:     */   {
/*  360: 489 */     this.materialShapePairs.clear();
/*  361: 490 */     this.materialSectionPairs.clear();
/*  362: 491 */     Iterator<Member> e = this.members.iterator();
/*  363: 492 */     while (e.hasNext())
/*  364:     */     {
/*  365: 493 */       Member member = (Member)e.next();
/*  366: 494 */       MaterialShapePair msPair = new MaterialShapePair(member.getMaterial(), member.getShape());
/*  367: 495 */       Integer iVal = (Integer)this.materialShapePairs.get(msPair);
/*  368: 496 */       this.materialShapePairs.put(msPair, new Integer(iVal == null ? 1 : iVal.intValue() + 1));
/*  369: 497 */       double weight = member.getShape().getArea() * member.getLength() * member.getMaterial().getDensity();
/*  370: 498 */       MaterialSectionPair mcPair = new MaterialSectionPair(member.getMaterial(), member.getShape().getSection());
/*  371: 499 */       Double dVal = (Double)this.materialSectionPairs.get(mcPair);
/*  372: 500 */       this.materialSectionPairs.put(mcPair, new Double(dVal == null ? weight : dVal.doubleValue() + weight));
/*  373:     */     }
/*  374: 502 */     this.costs.materialShapePairs = this.materialShapePairs;
/*  375: 503 */     this.costs.materialSectionPairs = this.materialSectionPairs;
/*  376: 504 */     this.costs.conditions = this.designConditions;
/*  377: 505 */     this.costs.inventory = this.inventory;
/*  378: 506 */     this.costs.nConnections = this.joints.size();
/*  379: 507 */     this.costs.notes = null;
/*  380: 508 */     return this.costs;
/*  381:     */   }
/*  382:     */   
/*  383:     */   public String[] getNotes()
/*  384:     */   {
/*  385: 520 */     ResourceMap resourceMap = WPBDApp.getResourceMap(BridgeModel.class);
/*  386: 521 */     Calendar calendar = Calendar.getInstance();
/*  387: 522 */     String fmt = resourceMap.getString("dateNote.text", new Object[0]);
/*  388: 523 */     SimpleDateFormat dateFormat = new SimpleDateFormat(fmt == null ? "(EEE, d MMM yyyy)" : fmt);
/*  389: 524 */     String note1 = resourceMap.getString("projectNameNote.text", new Object[] { this.projectName });
/*  390: 525 */     String note2 = resourceMap.getString("projectIdNote.text", new Object[] { this.projectId });
/*  391: 526 */     String note3 = resourceMap.getString("iterationNote.text", new Object[] { Integer.valueOf(this.iterationNumber), dateFormat.format(calendar.getTime()) });
/*  392: 527 */     return new String[] { note1, note2, note3, this.designedBy.trim().length() == 0 ? new String[] { note1, note2, note3 } : resourceMap.getString("iterationNote.text", new Object[] { Integer.valueOf(this.iterationNumber), dateFormat.format(calendar.getTime()) }) };
/*  393:     */   }
/*  394:     */   
/*  395:     */   public Costs getCostsWithNotes()
/*  396:     */   {
/*  397: 540 */     getCosts();
/*  398: 541 */     this.costs.notes = getNotes();
/*  399: 542 */     return this.costs;
/*  400:     */   }
/*  401:     */   
/*  402:     */   public double getTotalCost()
/*  403:     */   {
/*  404: 551 */     double mtlCost = 0.0D;
/*  405: 552 */     this.stockSet.clear();
/*  406: 553 */     Iterator<Member> e = this.members.iterator();
/*  407: 554 */     while (e.hasNext())
/*  408:     */     {
/*  409: 555 */       Member member = (Member)e.next();
/*  410: 556 */       this.stockSet.add(new MaterialShapePair(member.getMaterial(), member.getShape()));
/*  411: 557 */       mtlCost += member.getMaterial().getCost(member.getShape().getSection()) * member.getShape().getArea() * member.getLength() * member.getMaterial().getDensity();
/*  412:     */     }
/*  413: 560 */     double productCost = this.stockSet.size() * this.inventory.getOrderingFee();
/*  414: 561 */     double connectionCost = this.joints.size() * this.inventory.getConnectionFee();
/*  415: 562 */     return 2.0D * (mtlCost + connectionCost) + productCost + this.designConditions.getTotalFixedCost();
/*  416:     */   }
/*  417:     */   
/*  418:     */   public StockSelector.Descriptor getMostCommonStock()
/*  419:     */   {
/*  420: 573 */     HashMap<StockSelector.Descriptor, Integer> map = new HashMap();
/*  421: 574 */     Iterator<Member> e = this.members.iterator();
/*  422: 575 */     StockSelector.Descriptor mostCommon = null;
/*  423: 576 */     int nMostCommon = -1;
/*  424: 577 */     while (e.hasNext())
/*  425:     */     {
/*  426: 578 */       Member member = (Member)e.next();
/*  427: 579 */       StockSelector.Descriptor descriptor = new StockSelector.Descriptor(member);
/*  428: 580 */       Integer n = (Integer)map.get(descriptor);
/*  429: 581 */       int nNew = n == null ? 1 : n.intValue() + 1;
/*  430: 582 */       if (nNew > nMostCommon)
/*  431:     */       {
/*  432: 583 */         mostCommon = descriptor;
/*  433: 584 */         nMostCommon = nNew;
/*  434:     */       }
/*  435: 586 */       map.put(descriptor, Integer.valueOf(nNew));
/*  436:     */     }
/*  437: 588 */     return mostCommon == null ? new StockSelector.Descriptor(0, 0, 16) : mostCommon;
/*  438:     */   }
/*  439:     */   
/*  440:     */   public Member[][] getSelectedStockLists()
/*  441:     */   {
/*  442: 600 */     HashMap<MaterialShapePair, ArrayList<Member>> map = new HashMap();
/*  443: 601 */     Iterator<Member> me = this.members.iterator();
/*  444: 602 */     while (me.hasNext())
/*  445:     */     {
/*  446: 603 */       Member member = (Member)me.next();
/*  447: 604 */       if (member.isSelected())
/*  448:     */       {
/*  449: 605 */         MaterialShapePair msp = new MaterialShapePair(member.getMaterial(), member.getShape());
/*  450: 606 */         ArrayList<Member> v = (ArrayList)map.get(msp);
/*  451: 607 */         if (v == null)
/*  452:     */         {
/*  453: 608 */           v = new ArrayList();
/*  454: 609 */           v.add(member);
/*  455: 610 */           map.put(msp, v);
/*  456:     */         }
/*  457:     */         else
/*  458:     */         {
/*  459: 613 */           v.add(member);
/*  460:     */         }
/*  461:     */       }
/*  462:     */     }
/*  463: 618 */     Member[][] lists = new Member[map.size()][];
/*  464: 619 */     Iterator<ArrayList<Member>> mi = map.values().iterator();
/*  465: 620 */     for (int i = 0; i < lists.length; i++)
/*  466:     */     {
/*  467: 621 */       ArrayList<Member> v = (ArrayList)mi.next();
/*  468: 622 */       lists[i] = ((Member[])v.toArray(new Member[v.size()]));
/*  469:     */     }
/*  470: 624 */     Arrays.sort(lists, new Comparator()
/*  471:     */     {
/*  472:     */       public int compare(Member[] m1, Member[] m2)
/*  473:     */       {
/*  474: 626 */         return m1[0].getNumber() - m2[0].getNumber();
/*  475:     */       }
/*  476: 628 */     });
/*  477: 629 */     return lists;
/*  478:     */   }
/*  479:     */   
/*  480:     */   public DesignConditions getDesignConditions()
/*  481:     */   {
/*  482: 638 */     return this.designConditions;
/*  483:     */   }
/*  484:     */   
/*  485:     */   public void setDesignedBy(String designedBy)
/*  486:     */   {
/*  487: 647 */     this.designedBy = designedBy;
/*  488:     */   }
/*  489:     */   
/*  490:     */   public String getDesignedBy()
/*  491:     */   {
/*  492: 656 */     return this.designedBy;
/*  493:     */   }
/*  494:     */   
/*  495:     */   public Inventory getInventory()
/*  496:     */   {
/*  497: 665 */     return this.inventory;
/*  498:     */   }
/*  499:     */   
/*  500:     */   public Joint findJointAt(Affine.Point ptWorld)
/*  501:     */   {
/*  502: 675 */     Iterator<Joint> e = this.joints.iterator();
/*  503: 676 */     while (e.hasNext())
/*  504:     */     {
/*  505: 677 */       Joint joint = (Joint)e.next();
/*  506: 678 */       if (joint.isAt(ptWorld)) {
/*  507: 679 */         return joint;
/*  508:     */       }
/*  509:     */     }
/*  510: 682 */     return null;
/*  511:     */   }
/*  512:     */   
/*  513:     */   public int getIteration()
/*  514:     */   {
/*  515: 691 */     return this.iterationNumber;
/*  516:     */   }
/*  517:     */   
/*  518:     */   public ArrayList<Joint> getJoints()
/*  519:     */   {
/*  520: 700 */     return this.joints;
/*  521:     */   }
/*  522:     */   
/*  523:     */   public double getLabelPosition()
/*  524:     */   {
/*  525: 709 */     return this.labelPosition;
/*  526:     */   }
/*  527:     */   
/*  528:     */   public ArrayList<Member> getMembers()
/*  529:     */   {
/*  530: 718 */     return this.members;
/*  531:     */   }
/*  532:     */   
/*  533:     */   public void setProjectId(String projectId)
/*  534:     */   {
/*  535: 727 */     this.projectId = projectId;
/*  536:     */   }
/*  537:     */   
/*  538:     */   public String getProjectId()
/*  539:     */   {
/*  540: 736 */     return this.projectId;
/*  541:     */   }
/*  542:     */   
/*  543:     */   public void setProjectName(String projectName)
/*  544:     */   {
/*  545: 745 */     this.projectName = projectName;
/*  546:     */   }
/*  547:     */   
/*  548:     */   public String getProjectName()
/*  549:     */   {
/*  550: 754 */     return this.projectName;
/*  551:     */   }
/*  552:     */   
/*  553:     */   protected String getRatioEncoding(double r)
/*  554:     */   {
/*  555: 764 */     return r < 0.0D ? "--" : String.format((Locale)null, "%.2f", new Object[] { Double.valueOf(r) });
/*  556:     */   }
/*  557:     */   
/*  558:     */   protected static double parseRatioEncoding(String s)
/*  559:     */   {
/*  560:     */     try
/*  561:     */     {
/*  562: 780 */       return Double.parseDouble(s);
/*  563:     */     }
/*  564:     */     catch (NumberFormatException ex) {}
/*  565: 783 */     return -1.0D;
/*  566:     */   }
/*  567:     */   
/*  568:     */   public Rectangle2D getExtent(Rectangle2D extent)
/*  569:     */   {
/*  570: 795 */     if (extent == null) {
/*  571: 796 */       extent = new Rectangle2D.Double();
/*  572:     */     }
/*  573: 798 */     boolean first = true;
/*  574: 799 */     Iterator<Joint> je = this.joints.iterator();
/*  575: 800 */     while (je.hasNext())
/*  576:     */     {
/*  577: 801 */       Joint j = (Joint)je.next();
/*  578: 802 */       if (first)
/*  579:     */       {
/*  580: 803 */         first = false;
/*  581: 804 */         Affine.Point pt = j.getPointWorld();
/*  582: 805 */         extent.setRect(pt.x, pt.y, 0.0D, 0.0D);
/*  583:     */       }
/*  584:     */       else
/*  585:     */       {
/*  586: 808 */         extent.add(j.getPointWorld());
/*  587:     */       }
/*  588:     */     }
/*  589: 811 */     return extent;
/*  590:     */   }
/*  591:     */   
/*  592:     */   public void initialize(DesignConditions conditions, String projectId, String designedBy)
/*  593:     */   {
/*  594: 822 */     this.designConditions = conditions;
/*  595: 823 */     if (projectId != null) {
/*  596: 824 */       this.projectId = projectId;
/*  597:     */     }
/*  598: 826 */     if (designedBy != null) {
/*  599: 827 */       this.designedBy = designedBy;
/*  600:     */     }
/*  601: 829 */     clearStructure();
/*  602: 831 */     for (int i = 0; i < conditions.getNPrescribedJoints(); i++) {
/*  603: 832 */       this.joints.add(conditions.getPrescribedJoint(i));
/*  604:     */     }
/*  605:     */   }
/*  606:     */   
/*  607:     */   public boolean isPassingSlendernessCheck()
/*  608:     */   {
/*  609: 842 */     Iterator<Member> me = this.members.iterator();
/*  610: 843 */     double allowableSlenderness = this.designConditions.getAllowableSlenderness();
/*  611: 844 */     while (me.hasNext()) {
/*  612: 845 */       if (((Member)me.next()).getSlenderness() > allowableSlenderness) {
/*  613: 846 */         return false;
/*  614:     */       }
/*  615:     */     }
/*  616: 849 */     return true;
/*  617:     */   }
/*  618:     */   
/*  619:     */   public boolean isInitialized()
/*  620:     */   {
/*  621: 858 */     return this.designConditions != null;
/*  622:     */   }
/*  623:     */   
/*  624:     */   public void read(File f)
/*  625:     */     throws IOException
/*  626:     */   {
/*  627: 868 */     byte[] bytes = Utility.getBytesFromFile(f);
/*  628: 869 */     RC4 rc4 = new RC4();
/*  629: 870 */     rc4.setKey(RC4Key.getScrambleKey());
/*  630: 871 */     rc4.endecrypt(bytes);
/*  631:     */     
/*  632: 873 */     parseBytes(bytes);
/*  633:     */   }
/*  634:     */   
/*  635:     */   public void write(File f)
/*  636:     */     throws IOException
/*  637:     */   {
/*  638: 884 */     byte[] rtn = toBytes();
/*  639: 885 */     RC4 rc4 = new RC4();
/*  640: 886 */     rc4.setKey(RC4Key.getScrambleKey());
/*  641: 887 */     rc4.endecrypt(rtn);
/*  642: 888 */     OutputStream os = new FileOutputStream(f);
/*  643: 889 */     os.write(rtn);
/*  644: 890 */     os.close();
/*  645:     */   }
/*  646:     */   
/*  647:     */   protected void parseBytes(byte[] readBuf)
/*  648:     */     throws IOException
/*  649:     */   {
/*  650: 900 */     this.readBuf = readBuf;
/*  651: 901 */     this.readPtr = 0;
/*  652: 902 */     DraftingGrid grid = new DraftingGrid(2);
/*  653: 903 */     clearStructure();
/*  654: 904 */     if (scanUnsigned(4, "bridge designer version") != 2014) {
/*  655: 905 */       throw new IOException("bridge design file version is not 2014");
/*  656:     */     }
/*  657: 907 */     long scenarioCode = scanUnsignedLong(10, "scenario code");
/*  658: 908 */     this.designConditions = DesignConditions.getDesignConditions(scenarioCode);
/*  659: 909 */     if (this.designConditions == null) {
/*  660: 910 */       throw new IOException("invalid scenario " + scenarioCode);
/*  661:     */     }
/*  662: 912 */     int n_joints = scanUnsigned(2, "number of joints");
/*  663: 913 */     int n_members = scanUnsigned(3, "number of members");
/*  664: 914 */     int i = 0;
/*  665: 914 */     for (int n = 1; i < n_joints; n++)
/*  666:     */     {
/*  667: 915 */       int x = scanInt(3, "joint " + n + " x-coordinate");
/*  668: 916 */       int y = scanInt(3, "joint " + n + " y-coordinate");
/*  669: 917 */       if (i < this.designConditions.getNPrescribedJoints())
/*  670:     */       {
/*  671: 918 */         Joint joint = this.designConditions.getPrescribedJoint(i);
/*  672: 919 */         if ((x != grid.worldToGridX(joint.getPointWorld().x)) || (y != grid.worldToGridY(joint.getPointWorld().y))) {
/*  673: 920 */           throw new IOException("bad prescribed joint " + n);
/*  674:     */         }
/*  675: 922 */         this.joints.add(joint);
/*  676:     */       }
/*  677:     */       else
/*  678:     */       {
/*  679: 924 */         this.joints.add(new Joint(i, new Affine.Point(grid.gridToWorldX(x), grid.gridToWorldY(y))));
/*  680:     */       }
/*  681: 914 */       i++;
/*  682:     */     }
/*  683: 927 */     int i = 0;
/*  684: 927 */     for (int n = 1; i < n_members; n++)
/*  685:     */     {
/*  686: 928 */       int jointANumber = scanUnsigned(2, "first joint of member " + n);
/*  687: 929 */       int jointBNumber = scanUnsigned(2, "second joint of member " + n);
/*  688: 930 */       int materialIndex = scanUnsigned(1, "material index of member " + n);
/*  689: 931 */       int sectionIndex = scanUnsigned(1, "section index of member " + n);
/*  690: 932 */       int sizeIndex = scanUnsigned(2, "size index of member " + n);
/*  691: 933 */       this.members.add(new Member(i, (Joint)this.joints.get(jointANumber - 1), (Joint)this.joints.get(jointBNumber - 1), this.inventory.getMaterial(materialIndex), this.inventory.getShape(sectionIndex, sizeIndex)));i++;
/*  692:     */     }
/*  693: 936 */     Iterator<Member> e = this.members.iterator();
/*  694: 937 */     while (e.hasNext())
/*  695:     */     {
/*  696: 938 */       Member member = (Member)e.next();
/*  697: 939 */       member.setCompressionForceStrengthRatio(parseRatioEncoding(scanToDelimiter("compression/strength ratio")));
/*  698: 940 */       member.setTensionForceStrengthRatio(parseRatioEncoding(scanToDelimiter("tension/strength ratio")));
/*  699:     */     }
/*  700: 942 */     this.designedBy = scanToDelimiter("name of designer");
/*  701: 943 */     this.projectId = scanToDelimiter("project ID");
/*  702: 944 */     this.iterationNumber = Integer.parseInt(scanToDelimiter("iteration"));
/*  703: 945 */     this.labelPosition = Double.parseDouble(scanToDelimiter("label position"));
/*  704:     */   }
/*  705:     */   
/*  706:     */   private int scanInt(int width, String what)
/*  707:     */     throws IOException
/*  708:     */   {
/*  709: 952 */     int val = 0;
/*  710: 953 */     boolean negate_p = false;
/*  711: 956 */     while ((width > 0) && (this.readBuf[this.readPtr] == 32))
/*  712:     */     {
/*  713: 957 */       width--;
/*  714: 958 */       this.readPtr += 1;
/*  715:     */     }
/*  716: 961 */     if ((width >= 2) && (this.readBuf[this.readPtr] == 45))
/*  717:     */     {
/*  718: 962 */       width--;
/*  719: 963 */       this.readPtr += 1;
/*  720: 964 */       negate_p = true;
/*  721:     */     }
/*  722: 966 */     while (width > 0) {
/*  723: 967 */       if ((48 <= this.readBuf[this.readPtr]) && (this.readBuf[this.readPtr] <= 57))
/*  724:     */       {
/*  725: 968 */         val = val * 10 + (this.readBuf[this.readPtr] - 48);
/*  726: 969 */         width--;
/*  727: 970 */         this.readPtr += 1;
/*  728:     */       }
/*  729:     */       else
/*  730:     */       {
/*  731: 972 */         throw new IOException("couldn't scan " + what);
/*  732:     */       }
/*  733:     */     }
/*  734: 975 */     return negate_p ? -val : val;
/*  735:     */   }
/*  736:     */   
/*  737:     */   private String scanToDelimiter(String what)
/*  738:     */   {
/*  739: 979 */     StringBuilder buf = new StringBuilder(16);
/*  740: 980 */     while (this.readBuf[this.readPtr] != 124)
/*  741:     */     {
/*  742: 981 */       buf.append((char)this.readBuf[this.readPtr]);
/*  743: 982 */       this.readPtr += 1;
/*  744:     */     }
/*  745: 984 */     this.readPtr += 1;
/*  746: 985 */     return buf.toString();
/*  747:     */   }
/*  748:     */   
/*  749:     */   private int scanUnsigned(int width, String what)
/*  750:     */     throws IOException
/*  751:     */   {
/*  752: 989 */     int val = 0;
/*  753: 990 */     while ((width > 0) && (this.readBuf[this.readPtr] == 32))
/*  754:     */     {
/*  755: 991 */       width--;
/*  756: 992 */       this.readPtr += 1;
/*  757:     */     }
/*  758: 994 */     while (width > 0) {
/*  759: 995 */       if ((48 <= this.readBuf[this.readPtr]) && (this.readBuf[this.readPtr] <= 57))
/*  760:     */       {
/*  761: 996 */         val = val * 10 + (this.readBuf[this.readPtr] - 48);
/*  762: 997 */         width--;
/*  763: 998 */         this.readPtr += 1;
/*  764:     */       }
/*  765:     */       else
/*  766:     */       {
/*  767:1000 */         throw new IOException("couldn't scan " + what);
/*  768:     */       }
/*  769:     */     }
/*  770:1003 */     return val;
/*  771:     */   }
/*  772:     */   
/*  773:     */   private long scanUnsignedLong(int width, String what)
/*  774:     */     throws IOException
/*  775:     */   {
/*  776:1007 */     long val = 0L;
/*  777:1008 */     while ((width > 0) && (this.readBuf[this.readPtr] == 32))
/*  778:     */     {
/*  779:1009 */       width--;
/*  780:1010 */       this.readPtr += 1;
/*  781:     */     }
/*  782:1012 */     while (width > 0) {
/*  783:1013 */       if ((48 <= this.readBuf[this.readPtr]) && (this.readBuf[this.readPtr] <= 57))
/*  784:     */       {
/*  785:1014 */         val = val * 10L + (this.readBuf[this.readPtr] - 48);
/*  786:1015 */         width--;
/*  787:1016 */         this.readPtr += 1;
/*  788:     */       }
/*  789:     */       else
/*  790:     */       {
/*  791:1018 */         throw new IOException("couldn't scan " + what);
/*  792:     */       }
/*  793:     */     }
/*  794:1021 */     return val;
/*  795:     */   }
/*  796:     */   
/*  797:     */   public void setLabelPosition(double labelPosition)
/*  798:     */   {
/*  799:1030 */     this.labelPosition = labelPosition;
/*  800:     */   }
/*  801:     */   
/*  802:     */   protected byte[] toBytes()
/*  803:     */   {
/*  804:1038 */     byte[] bytes = null;
/*  805:     */     try
/*  806:     */     {
/*  807:1040 */       bytes = toString().getBytes("ASCII");
/*  808:     */     }
/*  809:     */     catch (UnsupportedEncodingException ex) {}
/*  810:1042 */     return bytes;
/*  811:     */   }
/*  812:     */   
/*  813:     */   public String toString()
/*  814:     */   {
/*  815:1052 */     StringBuilder s = new StringBuilder();
/*  816:1053 */     Formatter f = new Formatter(s, Locale.US);
/*  817:1054 */     DraftingGrid grid = new DraftingGrid(2);
/*  818:1055 */     f.format("%4d", new Object[] { Integer.valueOf(2014) });
/*  819:1056 */     f.format("%10d", new Object[] { Long.valueOf(this.designConditions.getCodeLong()) });
/*  820:1057 */     f.format("%2d", new Object[] { Integer.valueOf(this.joints.size()) });
/*  821:1058 */     f.format("%3d", new Object[] { Integer.valueOf(this.members.size()) });
/*  822:1059 */     Iterator<Joint> ej = this.joints.iterator();
/*  823:1060 */     while (ej.hasNext())
/*  824:     */     {
/*  825:1061 */       Joint joint = (Joint)ej.next();
/*  826:1062 */       f.format("%3d", new Object[] { Integer.valueOf(grid.worldToGridX(joint.getPointWorld().x)) });
/*  827:1063 */       f.format("%3d", new Object[] { Integer.valueOf(grid.worldToGridY(joint.getPointWorld().y)) });
/*  828:     */     }
/*  829:1065 */     Iterator<Member> em = this.members.iterator();
/*  830:1066 */     while (em.hasNext())
/*  831:     */     {
/*  832:1067 */       Member member = (Member)em.next();
/*  833:1068 */       f.format("%2d", new Object[] { Integer.valueOf(member.getJointA().getNumber()) });
/*  834:1069 */       f.format("%2d", new Object[] { Integer.valueOf(member.getJointB().getNumber()) });
/*  835:1070 */       f.format("%1d", new Object[] { Integer.valueOf(member.getMaterial().getIndex()) });
/*  836:1071 */       f.format("%1d", new Object[] { Integer.valueOf(member.getShape().getSection().getIndex()) });
/*  837:1072 */       f.format("%2d", new Object[] { Integer.valueOf(member.getShape().getSizeIndex()) });
/*  838:     */     }
/*  839:1074 */     em = this.members.iterator();
/*  840:1075 */     while (em.hasNext())
/*  841:     */     {
/*  842:1076 */       Member member = (Member)em.next();
/*  843:1077 */       s.append(getRatioEncoding(member.getCompressionForceStrengthRatio()));
/*  844:1078 */       s.append('|');
/*  845:1079 */       s.append(getRatioEncoding(member.getTensionForceStrengthRatio()));
/*  846:1080 */       s.append('|');
/*  847:     */     }
/*  848:1082 */     s.append(this.designedBy);
/*  849:1083 */     s.append('|');
/*  850:1084 */     s.append(this.projectId);
/*  851:1085 */     s.append('|');
/*  852:1086 */     s.append(getIteration());
/*  853:1087 */     s.append('|');
/*  854:1088 */     f.format("%.3f", new Object[] { Double.valueOf(getLabelPosition()) });
/*  855:1089 */     s.append('|');
/*  856:1090 */     return s.toString();
/*  857:     */   }
/*  858:     */   
/*  859:     */   public void writeSample(String id)
/*  860:     */     throws IOException
/*  861:     */   {
/*  862:1101 */     File f = new File("samples.bdf");
/*  863:1102 */     FileOutputStream os = new FileOutputStream(f, true);
/*  864:1103 */     PrintWriter ps = new PrintWriter(os);
/*  865:1104 */     String tag = this.designConditions.getTag();
/*  866:1105 */     ps.println(tag + '_' + id + ".bridgeSampleName=" + this.projectId.substring(0, this.projectId.lastIndexOf('-')));
/*  867:1106 */     ps.println(tag + '_' + id + ".bridgeSample=" + toString());
/*  868:1107 */     ps.close();
/*  869:     */   }
/*  870:     */   
/*  871:     */   public void read(String s)
/*  872:     */   {
/*  873:     */     try
/*  874:     */     {
/*  875:1117 */       parseBytes(s.getBytes("ASCII"));
/*  876:     */     }
/*  877:     */     catch (IOException ex)
/*  878:     */     {
/*  879:1120 */       Logger.getLogger(BridgeModel.class.getName()).log(Level.SEVERE, "syntax error in bridge as string", ex);
/*  880:     */     }
/*  881:     */   }
/*  882:     */   
/*  883:     */   public void writeTemplate(String id)
/*  884:     */     throws IOException
/*  885:     */   {
/*  886:1133 */     File f = new File("templates.bdf");
/*  887:1134 */     FileOutputStream os = new FileOutputStream(f, true);
/*  888:1135 */     PrintWriter ps = new PrintWriter(os);
/*  889:1136 */     String tag = this.designConditions.getTag();
/*  890:1137 */     ps.println(tag + '_' + id + ".bridgeSketchName=" + this.projectId.substring(0, this.projectId.lastIndexOf('-')));
/*  891:1138 */     ps.print(tag + '_' + id + ".bridgeSketch=");
/*  892:1139 */     ps.print(tag);
/*  893:1140 */     ps.print('|');
/*  894:1141 */     ps.print(this.joints.size());
/*  895:1142 */     ps.print('|');
/*  896:1143 */     ps.print(this.members.size());
/*  897:1144 */     ps.print('|');
/*  898:1145 */     Iterator<Joint> ej = this.joints.iterator();
/*  899:1146 */     while (ej.hasNext())
/*  900:     */     {
/*  901:1147 */       Affine.Point pt = ((Joint)ej.next()).getPointWorld();
/*  902:1148 */       ps.print(pt.x);
/*  903:1149 */       ps.print('|');
/*  904:1150 */       ps.print(pt.y);
/*  905:1151 */       ps.print('|');
/*  906:     */     }
/*  907:1153 */     Iterator<Member> em = this.members.iterator();
/*  908:1154 */     if (em.hasNext()) {
/*  909:     */       for (;;)
/*  910:     */       {
/*  911:1156 */         Member member = (Member)em.next();
/*  912:1157 */         ps.print(member.getJointA().getNumber());
/*  913:1158 */         ps.print('|');
/*  914:1159 */         ps.print(member.getJointB().getNumber());
/*  915:1160 */         if (!em.hasNext()) {
/*  916:     */           break;
/*  917:     */         }
/*  918:1161 */         ps.print('|');
/*  919:     */       }
/*  920:     */     }
/*  921:1167 */     ps.println();
/*  922:1168 */     ps.close();
/*  923:     */   }
/*  924:     */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeModel
 * JD-Core Version:    0.7.0.1
 */