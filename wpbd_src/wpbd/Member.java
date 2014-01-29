/*    1:     */ package wpbd;
/*    2:     */ 
/*    3:     */ import java.awt.BasicStroke;
/*    4:     */ import java.awt.Color;
/*    5:     */ import java.awt.Cursor;
/*    6:     */ import java.awt.Font;
/*    7:     */ import java.awt.FontMetrics;
/*    8:     */ import java.awt.Graphics2D;
/*    9:     */ import java.awt.Point;
/*   10:     */ import java.awt.Rectangle;
/*   11:     */ import java.awt.Stroke;
/*   12:     */ import java.awt.font.LineMetrics;
/*   13:     */ import java.awt.geom.AffineTransform;
/*   14:     */ import java.awt.geom.Rectangle2D.Float;
/*   15:     */ import javax.media.opengl.GL2;
/*   16:     */ import org.jdesktop.application.ResourceMap;
/*   17:     */ 
/*   18:     */ public class Member
/*   19:     */   implements HotEditableItem<BridgePaintContext>
/*   20:     */ {
/*   21:  24 */   private int index = -1;
/*   22:     */   private Joint jointA;
/*   23:     */   private Joint jointB;
/*   24:     */   private Material material;
/*   25:     */   private Shape shape;
/*   26:  29 */   private boolean selected = false;
/*   27:  30 */   private double compressionForceStrengthRatio = -1.0D;
/*   28:  31 */   private double tensionForceStrengthRatio = -1.0D;
/*   29:  36 */   private static final Color[] normalColors = getMemberColors(0.0F, 0.0F);
/*   30:  40 */   private static final Color[] hotColors = getMemberColors(0.3F, 0.0F);
/*   31:  44 */   private static final Color[] hotSelectedColors = getMemberColors(0.3F, 0.9F);
/*   32:  49 */   private static final Color[] innerColors = getInnerColors(0.0F, 0.0F);
/*   33:  53 */   private static final Color[] selectedInnerColors = getInnerColors(0.0F, 0.9F);
/*   34:  57 */   private static final Color[] hotInnerColors = getInnerColors(0.3F, 0.0F);
/*   35:  61 */   private static final Color[] hotSelectedInnerColors = getInnerColors(0.3F, 0.9F);
/*   36:  65 */   private static final float[] wireColor = { 0.1F, 0.3F, 0.1F, 1.0F };
/*   37:  69 */   private static final float[] centerLine = { 10.0F, 4.0F, 4.0F, 4.0F };
/*   38:     */   private static BasicStroke[] memberStrokes;
/*   39:     */   private static BasicStroke[] innerStrokes;
/*   40:  78 */   public static final Color[] selectedColors = getMemberColors(0.0F, 0.9F);
/*   41:  82 */   public static final Color labelBackground = new Color(255, 255, 192);
/*   42:  88 */   private static final float[] parabolaWidth = { 0.0F, 0.0625F, 0.125F, 0.15625F, 0.1875F, 0.21875F, 0.25F, 0.28125F, 0.3125F, 0.34375F, 0.375F, 0.40625F, 0.4375F, 0.46875F, 0.5F, 0.53125F, 0.5625F, 0.59375F, 0.625F, 0.65625F, 0.6875F, 0.71875F, 0.75F, 0.78125F, 0.8125F, 0.84375F, 0.875F, 0.90625F, 0.9375F, 0.96875F, 0.984375F, 0.992188F, 1.0F };
/*   43:  98 */   private static final float[] parabolaHeight = { 0.5F, 0.497717F, 0.492161F, 0.488378F, 0.483979F, 0.478991F, 0.473431F, 0.46731F, 0.460633F, 0.453402F, 0.445614F, 0.437259F, 0.428326F, 0.418797F, 0.40865F, 0.397857F, 0.386383F, 0.374182F, 0.361201F, 0.347371F, 0.332606F, 0.316796F, 0.299798F, 0.28142F, 0.261396F, 0.239344F, 0.214672F, 0.186382F, 0.152526F, 0.108068F, 0.076484F, 0.054105F, 0.0F };
/*   44:     */   private static final float dashLength = 3.0F;
/*   45:     */   private static final float spaceLength = 7.0F;
/*   46:     */   
/*   47:     */   public static float getParabolaHeight(float buckledLength, float baseLength)
/*   48:     */   {
/*   49: 118 */     if (baseLength == 0.0F) {
/*   50: 119 */       return 0.0F;
/*   51:     */     }
/*   52: 121 */     float p = Math.min(buckledLength / baseLength, 1.0F);
/*   53:     */     
/*   54: 123 */     int i = Utility.getIndexOfGreatestNotGreaterThan(p, parabolaWidth);
/*   55: 124 */     if (i == parabolaWidth.length - 1) {
/*   56: 125 */       return 0.0F;
/*   57:     */     }
/*   58: 128 */     float t = (p - parabolaWidth[i]) / (parabolaWidth[(i + 1)] - parabolaWidth[i]);
/*   59: 129 */     float h = i == parabolaWidth.length - 1 ? 0.0F : parabolaHeight[i] * (1.0F - t) + parabolaHeight[(i + 1)] * t;
/*   60: 130 */     return h * baseLength;
/*   61:     */   }
/*   62:     */   
/*   63:     */   public Member(Member member, Joint a, Joint b)
/*   64:     */   {
/*   65: 141 */     this(a, b, member.material, member.shape);
/*   66:     */   }
/*   67:     */   
/*   68:     */   public Member(Joint a, Joint b, Material material, Shape shape)
/*   69:     */   {
/*   70: 153 */     this.jointA = a;
/*   71: 154 */     this.jointB = b;
/*   72: 155 */     this.material = material;
/*   73: 156 */     this.shape = shape;
/*   74:     */   }
/*   75:     */   
/*   76:     */   public Member(Member member, Material material, Shape shape)
/*   77:     */   {
/*   78: 167 */     this(member.index, member.jointA, member.jointB, material, shape);
/*   79:     */   }
/*   80:     */   
/*   81:     */   public Member(int index, Joint a, Joint b, Material material, Shape shape)
/*   82:     */   {
/*   83: 180 */     this(a, b, material, shape);
/*   84: 181 */     this.index = index;
/*   85:     */   }
/*   86:     */   
/*   87:     */   public Member(Member member, Inventory inventory, int materialIndex, int sectionIndex, int sizeIndex)
/*   88:     */   {
/*   89: 195 */     this.index = member.index;
/*   90: 196 */     this.jointA = member.jointA;
/*   91: 197 */     this.jointB = member.jointB;
/*   92: 198 */     if (materialIndex == -1) {
/*   93: 199 */       materialIndex = member.getMaterial().getIndex();
/*   94:     */     }
/*   95: 201 */     if (sectionIndex == -1) {
/*   96: 202 */       sectionIndex = member.getShape().getSection().getIndex();
/*   97:     */     }
/*   98: 204 */     if (sizeIndex == -1) {
/*   99: 205 */       sizeIndex = member.getShape().getSizeIndex();
/*  100:     */     }
/*  101: 207 */     this.material = inventory.getMaterial(materialIndex);
/*  102: 208 */     this.shape = inventory.getShape(sectionIndex, sizeIndex);
/*  103:     */   }
/*  104:     */   
/*  105:     */   public Member(Member member, Shape shape)
/*  106:     */   {
/*  107: 218 */     this(member, member.material, shape);
/*  108:     */   }
/*  109:     */   
/*  110:     */   public double getCompressionForceStrengthRatio()
/*  111:     */   {
/*  112: 227 */     return this.compressionForceStrengthRatio;
/*  113:     */   }
/*  114:     */   
/*  115:     */   public void setCompressionForceStrengthRatio(double compressionForceStrengthRatio)
/*  116:     */   {
/*  117: 236 */     this.compressionForceStrengthRatio = compressionForceStrengthRatio;
/*  118:     */   }
/*  119:     */   
/*  120:     */   public double getTensionForceStrengthRatio()
/*  121:     */   {
/*  122: 245 */     return this.tensionForceStrengthRatio;
/*  123:     */   }
/*  124:     */   
/*  125:     */   public void setTensionForceStrengthRatio(double tensionForceStrengthRatio)
/*  126:     */   {
/*  127: 254 */     this.tensionForceStrengthRatio = tensionForceStrengthRatio;
/*  128:     */   }
/*  129:     */   
/*  130:     */   public void swapContents(Editable otherSelectable)
/*  131:     */   {
/*  132: 264 */     Member other = (Member)otherSelectable;
/*  133:     */     
/*  134: 266 */     Joint tmpJoint = this.jointA;
/*  135: 267 */     this.jointA = other.jointA;
/*  136: 268 */     other.jointA = tmpJoint;
/*  137:     */     
/*  138: 270 */     tmpJoint = this.jointB;
/*  139: 271 */     this.jointB = other.jointB;
/*  140: 272 */     other.jointB = tmpJoint;
/*  141:     */     
/*  142: 274 */     Material tmpMaterial = this.material;
/*  143: 275 */     this.material = other.material;
/*  144: 276 */     other.material = tmpMaterial;
/*  145:     */     
/*  146: 278 */     Shape tmpShape = this.shape;
/*  147: 279 */     this.shape = other.shape;
/*  148: 280 */     other.shape = tmpShape;
/*  149:     */   }
/*  150:     */   
/*  151:     */   public void setMaterial(Material material)
/*  152:     */   {
/*  153: 289 */     this.material = material;
/*  154:     */   }
/*  155:     */   
/*  156:     */   public Material getMaterial()
/*  157:     */   {
/*  158: 298 */     return this.material;
/*  159:     */   }
/*  160:     */   
/*  161:     */   public void setShape(Shape shape)
/*  162:     */   {
/*  163: 307 */     this.shape = shape;
/*  164:     */   }
/*  165:     */   
/*  166:     */   public Shape getShape()
/*  167:     */   {
/*  168: 316 */     return this.shape;
/*  169:     */   }
/*  170:     */   
/*  171:     */   public Joint getJointA()
/*  172:     */   {
/*  173: 325 */     return this.jointA;
/*  174:     */   }
/*  175:     */   
/*  176:     */   public Joint getJointB()
/*  177:     */   {
/*  178: 334 */     return this.jointB;
/*  179:     */   }
/*  180:     */   
/*  181:     */   public int getNumber()
/*  182:     */   {
/*  183: 343 */     return this.index + 1;
/*  184:     */   }
/*  185:     */   
/*  186:     */   public void setIndex(int index)
/*  187:     */   {
/*  188: 352 */     this.index = index;
/*  189:     */   }
/*  190:     */   
/*  191:     */   public int getIndex()
/*  192:     */   {
/*  193: 360 */     return this.index;
/*  194:     */   }
/*  195:     */   
/*  196:     */   public double getSlenderness()
/*  197:     */   {
/*  198: 369 */     return getLength() * this.shape.getInverseRadiusOfGyration();
/*  199:     */   }
/*  200:     */   
/*  201:     */   public boolean isSelected()
/*  202:     */   {
/*  203: 378 */     return this.selected;
/*  204:     */   }
/*  205:     */   
/*  206:     */   public boolean setSelected(boolean selected)
/*  207:     */   {
/*  208: 388 */     if (this.selected != selected)
/*  209:     */     {
/*  210: 389 */       this.selected = selected;
/*  211: 390 */       return true;
/*  212:     */     }
/*  213: 392 */     return false;
/*  214:     */   }
/*  215:     */   
/*  216:     */   public double getLength()
/*  217:     */   {
/*  218: 401 */     return this.jointA.getPointWorld().distance(this.jointB.getPointWorld());
/*  219:     */   }
/*  220:     */   
/*  221:     */   public double pickDistanceTo(Affine.Point pt, double jointRadius)
/*  222:     */   {
/*  223: 412 */     Affine.Point a = this.jointA.getPointWorld();
/*  224: 413 */     Affine.Point b = this.jointB.getPointWorld();
/*  225: 414 */     Affine.Vector d = b.minus(a);
/*  226: 415 */     double len = d.length();
/*  227: 417 */     if (4.0D * jointRadius >= len) {
/*  228: 418 */       return pt.distance(a.plus(d.times(0.5D)));
/*  229:     */     }
/*  230: 421 */     Affine.Vector v = d.unit(2.0D * jointRadius);
/*  231: 422 */     return pt.distanceToSegment(a.plus(v), b.minus(v));
/*  232:     */   }
/*  233:     */   
/*  234:     */   public boolean hasJoint(Joint joint)
/*  235:     */   {
/*  236: 432 */     return (joint == this.jointA) || (joint == this.jointB);
/*  237:     */   }
/*  238:     */   
/*  239:     */   public boolean hasJoints(Joint jointA, Joint jointB)
/*  240:     */   {
/*  241: 443 */     return (hasJoint(jointA)) && (hasJoint(jointB));
/*  242:     */   }
/*  243:     */   
/*  244:     */   public Joint otherJoint(Joint joint)
/*  245:     */   {
/*  246: 454 */     return joint == this.jointB ? this.jointA : joint == this.jointA ? this.jointB : null;
/*  247:     */   }
/*  248:     */   
/*  249:     */   public static void initializeDrawing(Inventory inventory)
/*  250:     */   {
/*  251: 464 */     memberStrokes = new BasicStroke[1 + inventory.getNShapes(0)];
/*  252: 465 */     innerStrokes = new BasicStroke[memberStrokes.length];
/*  253: 466 */     memberStrokes[0] = new BasicStroke(0.0F, 0, 0, 10.0F, centerLine, 0.0F);
/*  254: 467 */     for (int i = 1; i < memberStrokes.length; i++)
/*  255:     */     {
/*  256: 468 */       float width = getStrokeWidth(inventory.getShape(0, i - 1));
/*  257: 469 */       memberStrokes[i] = new BasicStroke(width, width <= 16.0F ? 0 : 1, 0);
/*  258:     */       
/*  259:     */ 
/*  260: 472 */       float widthInner = 0.6F * width;
/*  261: 473 */       if (width - widthInner < 2.0F) {
/*  262: 474 */         widthInner = Math.max(1.0F, width - 2.0F);
/*  263:     */       }
/*  264: 476 */       innerStrokes[i] = new BasicStroke(widthInner, 0, 0);
/*  265:     */     }
/*  266:     */   }
/*  267:     */   
/*  268:     */   private static float getStrokeWidth(Shape shape)
/*  269:     */   {
/*  270: 488 */     return Math.max((float)shape.getWidth() * 0.05F, 3.0F);
/*  271:     */   }
/*  272:     */   
/*  273:     */   public float getStrokeWidth()
/*  274:     */   {
/*  275: 497 */     return getStrokeWidth(this.shape);
/*  276:     */   }
/*  277:     */   
/*  278:     */   private static Color getColor(int r, int g, int b, float dBright, float dBlue)
/*  279:     */   {
/*  280: 511 */     r += (int)(dBright * (255 - r));
/*  281: 512 */     g += (int)(dBright * (255 - g));
/*  282: 513 */     b += (int)(dBright * (255 - b));
/*  283: 514 */     r -= (int)(0.25F * dBlue * r);
/*  284: 515 */     g -= (int)(0.25F * dBlue * g);
/*  285: 516 */     b += (int)(dBlue * (255 - b));
/*  286: 517 */     return new Color(r, g, b);
/*  287:     */   }
/*  288:     */   
/*  289:     */   private static Color[] getMemberColors(float dBright, float dBlue)
/*  290:     */   {
/*  291: 528 */     Color[] colors = new Color[3];
/*  292: 529 */     colors[0] = getColor(128, 128, 128, dBright, dBlue);
/*  293: 530 */     colors[1] = getColor(64, 64, 64, dBright, dBlue);
/*  294: 531 */     colors[2] = getColor(192, 192, 192, dBright, dBlue);
/*  295: 532 */     return colors;
/*  296:     */   }
/*  297:     */   
/*  298:     */   private static Color[] getInnerColors(float dBright, float dBlue)
/*  299:     */   {
/*  300: 543 */     Color[] colors = new Color[3];
/*  301: 544 */     colors[0] = getColor(192, 192, 192, dBright, dBlue);
/*  302: 545 */     colors[1] = getColor(128, 128, 128, dBright, dBlue);
/*  303: 546 */     colors[2] = getColor(224, 224, 224, dBright, dBlue);
/*  304: 547 */     return colors;
/*  305:     */   }
/*  306:     */   
/*  307: 555 */   private static final float[] markDashes = { 3.0F, 7.0F };
/*  308:     */   private static final float dashesLength = 10.0F;
/*  309:     */   private static final float halfDashesLength = 5.0F;
/*  310:     */   private static final float lengthScale = 0.1F;
/*  311:     */   private static final float phaseCoeff = 0.3F;
/*  312:     */   
/*  313:     */   private static BasicStroke getMarkStroke(float width, float length)
/*  314:     */   {
/*  315: 569 */     float l = length * 0.1F;
/*  316: 570 */     int n = (int)(l + 0.3F - 1.0E-004F);
/*  317: 571 */     float phase = (1.0F - l + n + 0.3F) * 5.0F;
/*  318: 572 */     return new BasicStroke(width + 4.0F, 0, 0, 10.0F, markDashes, phase);
/*  319:     */   }
/*  320:     */   
/*  321:     */   public static void draw(Graphics2D g, Point a, Point b, int number, int sizeIndex, Color color, Color innerColor, Color markColor)
/*  322:     */   {
/*  323: 588 */     Stroke savedStroke = g.getStroke();
/*  324: 589 */     g.setColor(color);
/*  325: 590 */     BasicStroke stroke = memberStrokes[sizeIndex];
/*  326: 591 */     g.setStroke(stroke);
/*  327: 592 */     g.drawLine(a.x, a.y, b.x, b.y);
/*  328: 593 */     if (innerColor != null)
/*  329:     */     {
/*  330: 594 */       g.setColor(innerColor);
/*  331: 595 */       g.setStroke(innerStrokes[sizeIndex]);
/*  332: 596 */       g.drawLine(a.x, a.y, b.x, b.y);
/*  333:     */     }
/*  334: 598 */     if (markColor != null)
/*  335:     */     {
/*  336: 599 */       float dx = a.x - b.x;
/*  337: 600 */       float dy = a.y - b.y;
/*  338: 601 */       g.setStroke(getMarkStroke(stroke.getLineWidth(), (float)Math.sqrt(dx * dx + dy * dy)));
/*  339: 602 */       g.setColor(markColor);
/*  340: 603 */       g.drawLine(a.x, a.y, b.x, b.y);
/*  341:     */     }
/*  342: 605 */     g.setStroke(savedStroke);
/*  343: 606 */     g.setColor(Color.BLACK);
/*  344: 607 */     if (number >= 0) {
/*  345: 608 */       Labeler.drawJustified(g, Integer.toString(number), (a.x + b.x) / 2, (a.y + b.y) / 2, 2, 2, labelBackground);
/*  346:     */     }
/*  347:     */   }
/*  348:     */   
/*  349: 617 */   private final Point ptA = new Point();
/*  350: 618 */   private final Point ptB = new Point();
/*  351:     */   
/*  352:     */   private void draw(Graphics2D g, ViewportTransform viewportTransform, boolean label, Color color, Color innerColor, Color markColor)
/*  353:     */   {
/*  354: 631 */     viewportTransform.worldToViewport(this.ptA, this.jointA.getPointWorld());
/*  355: 632 */     viewportTransform.worldToViewport(this.ptB, this.jointB.getPointWorld());
/*  356: 633 */     draw(g, this.ptA, this.ptB, label ? getNumber() : -1, 1 + this.shape.getSizeIndex(), color, innerColor, markColor);
/*  357:     */   }
/*  358:     */   
/*  359:     */   private Color getMarkColor(BridgePaintContext ctx)
/*  360:     */   {
/*  361: 644 */     return this.tensionForceStrengthRatio > 1.0D ? Color.BLUE : this.compressionForceStrengthRatio > 1.0D ? Color.RED : getSlenderness() > ctx.allowableSlenderness ? Color.MAGENTA : null;
/*  362:     */   }
/*  363:     */   
/*  364:     */   private void paintBlueprint(Graphics2D g, ViewportTransform viewportTransform)
/*  365:     */   {
/*  366: 657 */     Affine.Point ptAworld = this.jointA.getPointWorld();
/*  367: 658 */     Affine.Point ptBworld = this.jointB.getPointWorld();
/*  368: 659 */     Affine.Vector uPerp = ptBworld.minus(ptAworld).unit(0.5D * getWidthInMeters()).perp();
/*  369: 660 */     viewportTransform.worldToViewport(this.ptA, ptAworld.plus(uPerp));
/*  370: 661 */     viewportTransform.worldToViewport(this.ptB, ptBworld.plus(uPerp));
/*  371: 662 */     g.drawLine(this.ptA.x, this.ptA.y, this.ptB.x, this.ptB.y);
/*  372: 663 */     viewportTransform.worldToViewport(this.ptA, ptAworld.minus(uPerp));
/*  373: 664 */     viewportTransform.worldToViewport(this.ptB, ptBworld.minus(uPerp));
/*  374: 665 */     g.drawLine(this.ptA.x, this.ptA.y, this.ptB.x, this.ptB.y);
/*  375:     */     
/*  376:     */ 
/*  377: 668 */     AffineTransform savedTransform = g.getTransform();
/*  378: 669 */     Stroke savedStroke = g.getStroke();
/*  379: 670 */     Font savedFont = g.getFont();
/*  380:     */     
/*  381: 672 */     viewportTransform.worldToViewport(this.ptA, ptAworld);
/*  382: 673 */     viewportTransform.worldToViewport(this.ptB, ptBworld);
/*  383: 674 */     g.translate((this.ptA.x + this.ptB.x) / 2, (this.ptA.y + this.ptB.y) / 2);
/*  384:     */     
/*  385: 676 */     g.scale(20.0D, 20.0D);
/*  386:     */     
/*  387: 678 */     g.setFont(savedFont.deriveFont(savedFont.getSize() - 1.5F));
/*  388:     */     
/*  389: 680 */     String numberString = Integer.toString(getNumber());
/*  390: 681 */     FontMetrics fm = g.getFontMetrics();
/*  391: 682 */     LineMetrics lm = fm.getLineMetrics(numberString, g);
/*  392: 683 */     Rectangle2D.Float numberStringBounds = new Rectangle2D.Float();
/*  393: 684 */     numberStringBounds.setRect(fm.getStringBounds(numberString, g));
/*  394:     */     
/*  395: 686 */     g.translate(-numberStringBounds.getWidth() / 2.0D, lm.getAscent() - numberStringBounds.getHeight() / 2.0D);
/*  396:     */     
/*  397: 688 */     numberStringBounds.x -= 1.0F;
/*  398: 689 */     numberStringBounds.width += 2.0F;
/*  399: 691 */     if (numberStringBounds.width < numberStringBounds.height)
/*  400:     */     {
/*  401: 692 */       float diff = numberStringBounds.height - numberStringBounds.width;
/*  402: 693 */       numberStringBounds.x -= 0.5F * diff;
/*  403: 694 */       numberStringBounds.width += diff;
/*  404:     */     }
/*  405: 697 */     g.setColor(Color.white);
/*  406: 698 */     g.fill(numberStringBounds);
/*  407: 699 */     g.setColor(Color.black);
/*  408: 700 */     g.setStroke(new BasicStroke(0.25F));
/*  409: 701 */     g.draw(numberStringBounds);
/*  410: 702 */     g.drawString(numberString, 0, 0);
/*  411:     */     
/*  412: 704 */     g.setTransform(savedTransform);
/*  413: 705 */     g.setStroke(savedStroke);
/*  414: 706 */     g.setFont(savedFont);
/*  415:     */   }
/*  416:     */   
/*  417:     */   public void paint(Graphics2D g, ViewportTransform viewportTransform, BridgePaintContext ctx)
/*  418:     */   {
/*  419: 717 */     if (ctx.blueprint)
/*  420:     */     {
/*  421: 718 */       paintBlueprint(g, viewportTransform);
/*  422:     */     }
/*  423:     */     else
/*  424:     */     {
/*  425: 721 */       Color markColor = getMarkColor(ctx);
/*  426: 722 */       Color mainColor = isSelected() ? selectedColors[this.material.getIndex()] : normalColors[this.material.getIndex()];
/*  427: 723 */       Color innerColor = null;
/*  428: 724 */       if (this.shape.getSection().getIndex() == 1) {
/*  429: 726 */         innerColor = isSelected() ? selectedInnerColors[this.material.getIndex()] : innerColors[this.material.getIndex()];
/*  430:     */       }
/*  431: 728 */       draw(g, viewportTransform, (ctx.label) || (isSelected()), mainColor, innerColor, markColor);
/*  432:     */     }
/*  433:     */   }
/*  434:     */   
/*  435:     */   public void paintHot(Graphics2D g, ViewportTransform viewportTransform, BridgePaintContext ctx)
/*  436:     */   {
/*  437: 740 */     Color markColor = getMarkColor(ctx);
/*  438: 741 */     Color hotColor = isSelected() ? hotSelectedColors[this.material.getIndex()] : hotColors[this.material.getIndex()];
/*  439: 742 */     Color innerColor = null;
/*  440: 743 */     if (this.shape.getSection().getIndex() == 1) {
/*  441: 745 */       innerColor = isSelected() ? hotSelectedInnerColors[this.material.getIndex()] : hotInnerColors[this.material.getIndex()];
/*  442:     */     }
/*  443: 747 */     draw(g, viewportTransform, (ctx.label) || (isSelected()), hotColor, innerColor, markColor);
/*  444: 748 */     this.jointA.paint(g, viewportTransform, ctx);
/*  445: 749 */     this.jointB.paint(g, viewportTransform, ctx);
/*  446:     */   }
/*  447:     */   
/*  448:     */   public void getViewportExtent(Rectangle extent, ViewportTransform viewportTransform)
/*  449:     */   {
/*  450: 759 */     viewportTransform.worldToViewport(this.ptA, this.jointA.getPointWorld());
/*  451: 760 */     viewportTransform.worldToViewport(this.ptB, this.jointB.getPointWorld());
/*  452: 761 */     extent.setFrameFromDiagonal(this.ptA, this.ptB);
/*  453: 762 */     int halfWidth = 1 + (int)(getStrokeWidth() / 2.0F);
/*  454: 763 */     extent.grow(halfWidth, halfWidth);
/*  455:     */   }
/*  456:     */   
/*  457:     */   public float getWidthInMeters()
/*  458:     */   {
/*  459: 772 */     return (float)this.shape.getWidth() * 0.001F;
/*  460:     */   }
/*  461:     */   
/*  462: 778 */   private static final float[] cubeVerticesLeft = { 0.0F, 0.5F, 0.5F, 0.0F, 0.5F, -0.5F, 0.0F, -0.5F, -0.5F, 0.0F, -0.5F, 0.5F, 0.0F, 0.5F, 0.5F };
/*  463: 785 */   private static final float[] cubeVerticesRight = { 1.0F, 0.5F, 0.5F, 1.0F, 0.5F, -0.5F, 1.0F, -0.5F, -0.5F, 1.0F, -0.5F, 0.5F, 1.0F, 0.5F, 0.5F };
/*  464: 792 */   private static final float[] cubeNormals = { 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
/*  465:     */   
/*  466:     */   private void paint(GL2 gl, float xa, float ya, float xb, float yb, float z)
/*  467:     */   {
/*  468: 810 */     float width = getWidthInMeters();
/*  469: 811 */     float dx = xb - xa;
/*  470: 812 */     float dy = yb - ya;
/*  471: 813 */     float len = (float)Math.sqrt(dx * dx + dy * dy);
/*  472: 814 */     gl.glPushMatrix();
/*  473: 815 */     gl.glTranslatef(xa, ya, z);
/*  474: 816 */     gl.glMultMatrixf(Utility.rotateAboutZ(dx / len, dy / len), 0);
/*  475: 817 */     gl.glScaled(len, width, width);
/*  476: 818 */     gl.glBegin(7);
/*  477: 819 */     for (int i = 0; i < cubeNormals.length; i += 3)
/*  478:     */     {
/*  479: 820 */       gl.glNormal3fv(cubeNormals, i);
/*  480: 821 */       gl.glVertex3fv(cubeVerticesLeft, i + 0);
/*  481: 822 */       gl.glVertex3fv(cubeVerticesRight, i + 0);
/*  482: 823 */       gl.glVertex3fv(cubeVerticesRight, i + 3);
/*  483: 824 */       gl.glVertex3fv(cubeVerticesLeft, i + 3);
/*  484:     */     }
/*  485: 826 */     gl.glEnd();
/*  486: 827 */     gl.glPopMatrix();
/*  487:     */   }
/*  488:     */   
/*  489:     */   public static void makeParabola(float[] pts, float[] n, double width, double height)
/*  490:     */   {
/*  491: 839 */     float fw = (float)width;
/*  492: 840 */     float fh = (float)height;
/*  493: 842 */     if (fw > 2.0F * fh)
/*  494:     */     {
/*  495: 844 */       float dx = fw / (pts.length / 2 - 1);
/*  496: 845 */       float x = 0.0F;
/*  497: 846 */       for (int i = 0; i < pts.length; x += dx)
/*  498:     */       {
/*  499: 847 */         pts[(i + 0)] = x;i += 2;
/*  500:     */       }
/*  501:     */     }
/*  502:     */     else
/*  503:     */     {
/*  504: 852 */       int nSlices = (pts.length / 2 - 1) / 2;
/*  505: 853 */       double dy = 1.0D / nSlices;
/*  506: 854 */       double y = 0.0D;
/*  507: 856 */       for (int i = 0; i < 2 * nSlices; y += dy)
/*  508:     */       {
/*  509: 857 */         float dx = 0.5F * (float)(width * Math.sqrt(y));
/*  510: 858 */         pts[(i + 0)] = dx;
/*  511: 859 */         pts[(pts.length - 2 - i)] = (fw - dx);i += 2;
/*  512:     */       }
/*  513: 861 */       pts[(i + 0)] = (fw * 0.5F);
/*  514:     */     }
/*  515: 864 */     for (int i = 0; i < pts.length; i += 2)
/*  516:     */     {
/*  517: 865 */       float x = pts[(i + 0)];
/*  518: 866 */       float t = 2.0F * x / fw - 1.0F;
/*  519: 867 */       pts[(i + 1)] = (fh * (1.0F - t * t));
/*  520: 868 */       float nx = 4.0F * fh * t / fw;
/*  521: 869 */       float len = (float)Math.sqrt(nx * nx + 1.0F);
/*  522: 870 */       n[(i + 0)] = (nx / len);
/*  523: 871 */       n[(i + 1)] = (1.0F / len);
/*  524:     */     }
/*  525:     */   }
/*  526:     */   
/*  527: 878 */   private float[] pts = new float[66];
/*  528: 879 */   private float[] normals = new float[this.pts.length];
/*  529: 880 */   private float[] topChord = new float[this.pts.length];
/*  530: 881 */   private float[] bottomChord = new float[this.pts.length];
/*  531:     */   
/*  532:     */   private void paintParabola(GL2 gl, float xa, float ya, float xb, float yb, float z, float arcLen)
/*  533:     */   {
/*  534: 895 */     float wHalf = 0.5F * getWidthInMeters() + 0.005F;
/*  535: 896 */     float dx = xb - xa;
/*  536: 897 */     float dy = yb - ya;
/*  537: 898 */     float len = (float)Math.sqrt(dx * dx + dy * dy);
/*  538: 899 */     makeParabola(this.pts, this.normals, len, getParabolaHeight(len, arcLen));
/*  539: 900 */     for (int i = 0; i < this.pts.length; i += 2)
/*  540:     */     {
/*  541: 901 */       float px = this.pts[(i + 0)];
/*  542: 902 */       float py = this.pts[(i + 1)];
/*  543: 903 */       float nx = this.normals[(i + 0)] * wHalf;
/*  544: 904 */       float ny = this.normals[(i + 1)] * wHalf;
/*  545: 905 */       this.topChord[(i + 0)] = (px + nx);
/*  546: 906 */       this.topChord[(i + 1)] = (py + ny);
/*  547: 907 */       this.bottomChord[(i + 0)] = (px - nx);
/*  548: 908 */       this.bottomChord[(i + 1)] = (py - ny);
/*  549:     */     }
/*  550: 910 */     gl.glPushMatrix();
/*  551: 911 */     gl.glTranslatef(xa, ya, z);
/*  552: 912 */     gl.glMultMatrixf(Utility.rotateAboutZ(dx / len, dy / len), 0);
/*  553:     */     
/*  554: 914 */     gl.glBegin(8);
/*  555: 915 */     gl.glNormal3f(0.0F, 0.0F, 1.0F);
/*  556: 916 */     for (int i = 0; i < this.pts.length; i += 2)
/*  557:     */     {
/*  558: 917 */       gl.glVertex3f(this.topChord[(i + 0)], this.topChord[(i + 1)], wHalf);
/*  559: 918 */       gl.glVertex3f(this.bottomChord[(i + 0)], this.bottomChord[(i + 1)], wHalf);
/*  560:     */     }
/*  561: 920 */     gl.glEnd();
/*  562:     */     
/*  563: 922 */     gl.glBegin(8);
/*  564: 923 */     gl.glNormal3f(0.0F, 0.0F, -1.0F);
/*  565: 924 */     for (int i = 0; i < this.pts.length; i += 2)
/*  566:     */     {
/*  567: 925 */       gl.glVertex3f(this.bottomChord[(i + 0)], this.bottomChord[(i + 1)], -wHalf);
/*  568: 926 */       gl.glVertex3f(this.topChord[(i + 0)], this.topChord[(i + 1)], -wHalf);
/*  569:     */     }
/*  570: 928 */     gl.glEnd();
/*  571:     */     
/*  572: 930 */     gl.glBegin(8);
/*  573: 931 */     for (int i = 0; i < this.pts.length; i += 2)
/*  574:     */     {
/*  575: 932 */       gl.glNormal3f(this.normals[(i + 0)], this.normals[(i + 1)], 0.0F);
/*  576: 933 */       gl.glVertex3f(this.topChord[(i + 0)], this.topChord[(i + 1)], -wHalf);
/*  577: 934 */       gl.glVertex3f(this.topChord[(i + 0)], this.topChord[(i + 1)], wHalf);
/*  578:     */     }
/*  579: 936 */     gl.glEnd();
/*  580:     */     
/*  581: 938 */     gl.glBegin(8);
/*  582: 939 */     for (int i = 0; i < this.pts.length; i += 2)
/*  583:     */     {
/*  584: 940 */       gl.glNormal3f(-this.normals[(i + 0)], -this.normals[(i + 1)], 0.0F);
/*  585: 941 */       gl.glVertex3f(this.bottomChord[(i + 0)], this.bottomChord[(i + 1)], wHalf);
/*  586: 942 */       gl.glVertex3f(this.bottomChord[(i + 0)], this.bottomChord[(i + 1)], -wHalf);
/*  587:     */     }
/*  588: 944 */     gl.glEnd();
/*  589:     */     
/*  590: 946 */     gl.glPopMatrix();
/*  591:     */   }
/*  592:     */   
/*  593: 952 */   private static final float[] brokenEndColor = { 0.3F, 0.3F, 0.3F, 1.0F };
/*  594: 956 */   private final float[] half = new float[8];
/*  595:     */   
/*  596:     */   private void paintWebQuad(GL2 gl, int i, float dz)
/*  597:     */   {
/*  598: 966 */     gl.glVertex3f(this.half[(i + 0)], this.half[(i + 1)], dz);
/*  599: 967 */     gl.glVertex3f(this.half[(i + 0)], this.half[(i + 1)], -dz);
/*  600: 968 */     i += 2;
/*  601: 969 */     gl.glVertex3f(this.half[(i + 0)], this.half[(i + 1)], -dz);
/*  602: 970 */     gl.glVertex3f(this.half[(i + 0)], this.half[(i + 1)], dz);
/*  603:     */   }
/*  604:     */   
/*  605:     */   private void paintBrokenHalf(GL2 gl, float dz)
/*  606:     */   {
/*  607: 980 */     gl.glBegin(7);
/*  608: 981 */     gl.glNormal3f(0.0F, 0.0F, 1.0F);
/*  609: 982 */     for (int i = 0; i < this.half.length; i += 2) {
/*  610: 983 */       gl.glVertex3f(this.half[(i + 0)], this.half[(i + 1)], dz);
/*  611:     */     }
/*  612: 985 */     gl.glNormal3f(0.0F, 0.0F, -1.0F);
/*  613: 986 */     for (int i = this.half.length - 2; i >= 0; i -= 2) {
/*  614: 987 */       gl.glVertex3f(this.half[(i + 0)], this.half[(i + 1)], -dz);
/*  615:     */     }
/*  616: 990 */     gl.glNormal3f(0.0F, -1.0F, 0.0F);
/*  617: 991 */     paintWebQuad(gl, 0, dz);
/*  618:     */     
/*  619: 993 */     gl.glNormal3f(0.0F, 1.0F, 0.0F);
/*  620: 994 */     paintWebQuad(gl, 4, dz);
/*  621:     */     
/*  622: 996 */     gl.glEnd();
/*  623:     */     
/*  624:     */ 
/*  625: 999 */     gl.glPushAttrib(1);
/*  626:1000 */     gl.glColor3fv(brokenEndColor, 0);
/*  627:     */     
/*  628:1002 */     gl.glBegin(7);
/*  629:1003 */     gl.glNormal3f(1.0F, 0.0F, 0.0F);
/*  630:1004 */     paintWebQuad(gl, 2, dz);
/*  631:1005 */     gl.glEnd();
/*  632:     */     
/*  633:     */ 
/*  634:1008 */     gl.glPopAttrib();
/*  635:     */   }
/*  636:     */   
/*  637:     */   private void paintBroken(GL2 gl, float xa, float ya, float xb, float yb, float z, float baseLength)
/*  638:     */   {
/*  639:1023 */     float wHalf = 0.5F * getWidthInMeters();
/*  640:1024 */     float dx = xb - xa;
/*  641:1025 */     float dy = yb - ya;
/*  642:1026 */     float len = (float)Math.sqrt(dx * dx + dy * dy);
/*  643:1027 */     float mid = 0.5F * baseLength;
/*  644:1028 */     float ofs = 0.8F * Math.min(wHalf, mid);
/*  645:1029 */     int i = -2;
/*  646:1030 */     i += 2;
/*  647:1031 */     this.half[(i + 0)] = 0.0F;
/*  648:1032 */     this.half[(i + 1)] = (-wHalf);
/*  649:1033 */     i += 2;
/*  650:1034 */     this.half[(i + 0)] = (mid + ofs);
/*  651:1035 */     this.half[(i + 1)] = (-wHalf);
/*  652:1036 */     i += 2;
/*  653:1037 */     this.half[(i + 0)] = (mid - ofs);
/*  654:1038 */     this.half[(i + 1)] = wHalf;
/*  655:1039 */     i += 2;
/*  656:1040 */     this.half[(i + 0)] = 0.0F;
/*  657:1041 */     this.half[(i + 1)] = wHalf;
/*  658:     */     
/*  659:1043 */     dx /= len;
/*  660:1044 */     dy /= len;
/*  661:     */     
/*  662:1046 */     gl.glPushMatrix();
/*  663:1047 */     gl.glTranslatef(xa, ya, z);
/*  664:1048 */     gl.glMultMatrixf(Utility.rotateAboutZ(dx, dy), 0);
/*  665:1049 */     paintBrokenHalf(gl, wHalf);
/*  666:1050 */     gl.glPopMatrix();
/*  667:     */     
/*  668:1052 */     gl.glPushMatrix();
/*  669:1053 */     gl.glTranslatef(xb, yb, z);
/*  670:1054 */     gl.glMultMatrixf(Utility.rotateAboutZ(-dx, -dy), 0);
/*  671:1055 */     paintBrokenHalf(gl, wHalf);
/*  672:1056 */     gl.glPopMatrix();
/*  673:     */   }
/*  674:     */   
/*  675:1062 */   private final float[] color = { 0.0F, 0.0F, 0.0F, 1.0F };
/*  676:     */   
/*  677:     */   public void paint(GL2 gl, Affine.Vector aDisp, Affine.Vector bDisp, double zOffset, double forceRatio, boolean showColors, double status)
/*  678:     */   {
/*  679:1076 */     float xa = (float)(this.jointA.getPointWorld().x + aDisp.x);
/*  680:1077 */     float yaRaw = (float)this.jointA.getPointWorld().y;
/*  681:1078 */     float ya = (float)(yaRaw + aDisp.y);
/*  682:1079 */     float xb = (float)(this.jointB.getPointWorld().x + bDisp.x);
/*  683:1080 */     float ybRaw = (float)this.jointB.getPointWorld().y;
/*  684:1081 */     float yb = (float)(ybRaw + bDisp.y);
/*  685:1082 */     float z = (float)zOffset;
/*  686:1083 */     if (showColors)
/*  687:     */     {
/*  688:1084 */       if (forceRatio < 0.0D)
/*  689:     */       {
/*  690:1085 */         float f = -(float)forceRatio;
/*  691:1086 */         this.color[0] = (0.5F * (1.0F + f)); float 
/*  692:1087 */           tmp137_136 = (0.5F * (1.0F - f));this.color[2] = tmp137_136;this.color[1] = tmp137_136;
/*  693:     */       }
/*  694:     */       else
/*  695:     */       {
/*  696:1090 */         float f = (float)forceRatio; float 
/*  697:1091 */           tmp165_164 = (0.5F * (1.0F - f));this.color[1] = tmp165_164;this.color[0] = tmp165_164;
/*  698:1092 */         this.color[2] = (0.5F * (1.0F + f));
/*  699:     */       }
/*  700:     */     }
/*  701:     */     else
/*  702:     */     {
/*  703:1096 */       float tmp203_202 = (this.color[2] = 0.5F);this.color[1] = tmp203_202;this.color[0] = tmp203_202;
/*  704:     */     }
/*  705:1098 */     gl.glColor3fv(this.color, 0);
/*  706:1099 */     if (!Analysis.isStatusBaseLength(status))
/*  707:     */     {
/*  708:1101 */       paint(gl, xa, ya, xb, yb, z);
/*  709:1102 */       paint(gl, xa, ya, xb, yb, -z);
/*  710:1103 */       if (((yaRaw >= 5.0D) && (ybRaw >= 5.0D)) || ((yaRaw <= 0.0F) && (ybRaw <= 0.0F)))
/*  711:     */       {
/*  712:1104 */         gl.glColor3fv(wireColor, 0);
/*  713:1105 */         gl.glBegin(1);
/*  714:1106 */         gl.glVertex3f(xa, ya, z);
/*  715:1107 */         gl.glVertex3f(xb, yb, -z);
/*  716:1108 */         gl.glVertex3f(xa, ya, -z);
/*  717:1109 */         gl.glVertex3f(xb, yb, z);
/*  718:1110 */         gl.glEnd();
/*  719:     */       }
/*  720:     */     }
/*  721:1114 */     else if (forceRatio < 0.0D)
/*  722:     */     {
/*  723:1115 */       float baseLength = (float)status;
/*  724:1116 */       paintParabola(gl, xa, ya, xb, yb, z, baseLength);
/*  725:1117 */       paintParabola(gl, xa, ya, xb, yb, -z, baseLength);
/*  726:     */     }
/*  727:     */     else
/*  728:     */     {
/*  729:1120 */       float baseLength = (float)status;
/*  730:1121 */       paintBroken(gl, xa, ya, xb, yb, z, baseLength);
/*  731:1122 */       paintBroken(gl, xa, ya, xb, yb, -z, baseLength);
/*  732:     */     }
/*  733:     */   }
/*  734:     */   
/*  735:     */   public String toString()
/*  736:     */   {
/*  737:1132 */     return WPBDApp.getResourceMap(Member.class).getString("rollover.text", new Object[] { Integer.valueOf(getNumber()), this.shape.getName(), this.material, this.shape.getSection() });
/*  738:     */   }
/*  739:     */   
/*  740:     */   public Cursor getCursor()
/*  741:     */   {
/*  742:1142 */     return null;
/*  743:     */   }
/*  744:     */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Member
 * JD-Core Version:    0.7.0.1
 */