/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Cursor;
/*   4:    */ import java.awt.Graphics2D;
/*   5:    */ import java.awt.Rectangle;
/*   6:    */ import java.awt.event.MouseEvent;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import javax.swing.JComponent;
/*  10:    */ import javax.swing.event.MouseInputAdapter;
/*  11:    */ 
/*  12:    */ public class HotMouseListener<TContext>
/*  13:    */   extends MouseInputAdapter
/*  14:    */ {
/*  15:    */   private final JComponent component;
/*  16: 69 */   private final ArrayList<HotItemListener<TContext>> listeners = new ArrayList();
/*  17:    */   private final ViewportTransform viewportTransform;
/*  18:    */   private final Cursor defaultCursor;
/*  19: 72 */   private final Affine.Point ptWorld = new Affine.Point();
/*  20: 73 */   private HotItem<TContext> hot = null;
/*  21: 74 */   private HotItemListener<TContext> hotListener = null;
/*  22: 75 */   private boolean dragging = false;
/*  23: 76 */   private final Rectangle extentViewport = new Rectangle();
/*  24:    */   
/*  25:    */   public HotMouseListener(JComponent component, ViewportTransform viewportTransform, Cursor defaultCursor)
/*  26:    */   {
/*  27: 86 */     this.component = component;
/*  28: 87 */     this.viewportTransform = viewportTransform;
/*  29: 88 */     this.defaultCursor = defaultCursor;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public HotItem<TContext> getHot()
/*  33:    */   {
/*  34: 97 */     return this.hot;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Affine.Point getPtWorld()
/*  38:    */   {
/*  39:106 */     return this.ptWorld;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean isDragging()
/*  43:    */   {
/*  44:116 */     return this.dragging;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Cursor getDefaultCursor()
/*  48:    */   {
/*  49:125 */     return this.defaultCursor;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void clear()
/*  53:    */   {
/*  54:132 */     this.hot = null;
/*  55:133 */     this.component.setToolTipText(null);
/*  56:134 */     this.component.setCursor(this.defaultCursor);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void addListener(HotItemListener<TContext> listener)
/*  60:    */   {
/*  61:143 */     this.listeners.add(listener);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void removeListener(HotItemListener<TContext> listener)
/*  65:    */   {
/*  66:152 */     this.listeners.remove(listener);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void removeAllListeners()
/*  70:    */   {
/*  71:159 */     this.listeners.clear();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void mouseDragged(MouseEvent event)
/*  75:    */   {
/*  76:170 */     if ((this.hot != null) && ((this.hot instanceof HotDraggableItem)))
/*  77:    */     {
/*  78:171 */       HotDraggableItem<TContext> draggable = (HotDraggableItem)this.hot;
/*  79:172 */       if (!this.dragging ? draggable.startDrag(event.getPoint()) : draggable.queryDrag(event.getPoint()))
/*  80:    */       {
/*  81:173 */         this.hot.getViewportExtent(this.extentViewport, this.viewportTransform);
/*  82:    */         
/*  83:175 */         HotItem<TContext> savedHot = this.hot;
/*  84:176 */         this.hot = null;
/*  85:177 */         this.component.paintImmediately(this.extentViewport);
/*  86:178 */         this.hot = savedHot;
/*  87:    */         
/*  88:180 */         draggable.updateDrag(event.getPoint());
/*  89:181 */         Graphics2D g = (Graphics2D)this.component.getGraphics();
/*  90:182 */         this.hot.paint(g, this.viewportTransform, this.hotListener.getContext());
/*  91:183 */         g.dispose();
/*  92:184 */         this.dragging = true;
/*  93:    */       }
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void mouseReleased(MouseEvent event)
/*  98:    */   {
/*  99:196 */     if ((this.dragging) && ((this.hot instanceof HotDraggableItem)))
/* 100:    */     {
/* 101:197 */       ((HotDraggableItem)this.hot).stopDrag(event.getPoint());
/* 102:198 */       this.dragging = false;
/* 103:199 */       mouseMoved(event);
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void mouseMoved(MouseEvent event)
/* 108:    */   {
/* 109:213 */     if (this.dragging) {
/* 110:214 */       return;
/* 111:    */     }
/* 112:216 */     this.viewportTransform.viewportToWorld(this.ptWorld, event.getPoint());
/* 113:217 */     Iterator<HotItemListener<TContext>> e = this.listeners.iterator();
/* 114:218 */     HotItem<TContext> item = null;
/* 115:219 */     HotItemListener<TContext> listener = null;
/* 116:221 */     while ((item == null) && (e.hasNext()))
/* 117:    */     {
/* 118:222 */       listener = (HotItemListener)e.next();
/* 119:223 */       item = listener.getItem(event.getPoint(), this.ptWorld);
/* 120:    */     }
/* 121:225 */     if (item != this.hot)
/* 122:    */     {
/* 123:227 */       Graphics2D g = (Graphics2D)this.component.getGraphics();
/* 124:229 */       if (this.hot != null)
/* 125:    */       {
/* 126:230 */         this.hot.getViewportExtent(this.extentViewport, this.viewportTransform);
/* 127:    */         
/* 128:232 */         this.hot = null;
/* 129:233 */         this.component.paintImmediately(this.extentViewport);
/* 130:    */       }
/* 131:236 */       this.hot = item;
/* 132:237 */       if (this.hot == null)
/* 133:    */       {
/* 134:239 */         this.hotListener = null;
/* 135:240 */         if (this.defaultCursor != null) {
/* 136:241 */           this.component.setCursor(this.defaultCursor);
/* 137:    */         }
/* 138:243 */         this.component.setToolTipText(null);
/* 139:    */       }
/* 140:    */       else
/* 141:    */       {
/* 142:246 */         this.hotListener = listener;
/* 143:247 */         this.hot.paintHot(g, this.viewportTransform, listener.getContext());
/* 144:250 */         if (this.hot.getCursor() != null) {
/* 145:251 */           this.component.setCursor(this.hot.getCursor());
/* 146:253 */         } else if (listener.getCursor() != null) {
/* 147:254 */           this.component.setCursor(listener.getCursor());
/* 148:    */         }
/* 149:256 */         if (listener.showToolTip()) {
/* 150:257 */           this.component.setToolTipText(this.hot.toString());
/* 151:    */         }
/* 152:    */       }
/* 153:260 */       g.dispose();
/* 154:    */     }
/* 155:    */   }
/* 156:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.HotMouseListener
 * JD-Core Version:    0.7.0.1
 */