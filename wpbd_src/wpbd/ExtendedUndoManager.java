/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.Vector;
/*   6:    */ import javax.swing.AbstractListModel;
/*   7:    */ import javax.swing.ListModel;
/*   8:    */ import javax.swing.event.UndoableEditEvent;
/*   9:    */ import javax.swing.event.UndoableEditListener;
/*  10:    */ import javax.swing.undo.CannotRedoException;
/*  11:    */ import javax.swing.undo.CannotUndoException;
/*  12:    */ import javax.swing.undo.UndoManager;
/*  13:    */ import javax.swing.undo.UndoableEdit;
/*  14:    */ 
/*  15:    */ public class ExtendedUndoManager
/*  16:    */   extends UndoManager
/*  17:    */ {
/*  18:    */   private Object checkpointMark;
/*  19: 43 */   private boolean stored = false;
/*  20: 44 */   private int tailTrimSerial = 0;
/*  21: 45 */   private ArrayList<UndoableEditListener> afterListeners = new ArrayList();
/*  22: 46 */   private boolean enablePosting = true;
/*  23:    */   
/*  24:    */   public ExtendedUndoManager()
/*  25:    */   {
/*  26: 52 */     setLimit(-1);
/*  27: 53 */     this.checkpointMark = getMark();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public synchronized void addUndoableAfterEditListener(UndoableEditListener listener)
/*  31:    */   {
/*  32: 62 */     this.afterListeners.add(listener);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public synchronized void removeUndoableAfterEditListener(UndoableEditListener listener)
/*  36:    */   {
/*  37: 71 */     this.afterListeners.remove(listener);
/*  38:    */   }
/*  39:    */   
/*  40:    */   private static class TailMark
/*  41:    */   {
/*  42:    */     int trimSerial;
/*  43:    */     
/*  44:    */     public TailMark(int trimSerial)
/*  45:    */     {
/*  46: 82 */       this.trimSerial = trimSerial;
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public final Object getMark()
/*  51:    */   {
/*  52: 93 */     Object mark = editToBeUndone();
/*  53: 94 */     if (mark == null) {
/*  54: 95 */       mark = new TailMark(this.tailTrimSerial);
/*  55:    */     }
/*  56: 97 */     return mark;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isAtMark(Object mark)
/*  60:    */   {
/*  61:109 */     if (mark == null) {
/*  62:110 */       return false;
/*  63:    */     }
/*  64:112 */     Object current = editToBeUndone();
/*  65:113 */     if ((mark instanceof UndoableEdit)) {
/*  66:114 */       return mark == current;
/*  67:    */     }
/*  68:117 */     return (current == null) && (((TailMark)mark).trimSerial == this.tailTrimSerial);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void save()
/*  72:    */   {
/*  73:125 */     this.checkpointMark = getMark();
/*  74:126 */     this.stored = true;
/*  75:127 */     postEdit(null, this.afterListeners);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void load()
/*  79:    */   {
/*  80:135 */     discardAllEdits();
/*  81:136 */     this.checkpointMark = getMark();
/*  82:137 */     this.stored = true;
/*  83:138 */     postEdit(null, this.afterListeners);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void clear()
/*  87:    */   {
/*  88:145 */     discardAllEdits();
/*  89:146 */     postEdit(null, this.afterListeners);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void newSession()
/*  93:    */   {
/*  94:154 */     discardAllEdits();
/*  95:    */     
/*  96:156 */     this.stored = false;
/*  97:157 */     this.checkpointMark = new TailMark(this.tailTrimSerial);
/*  98:158 */     postEdit(null, this.afterListeners);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public boolean isStored()
/* 102:    */   {
/* 103:167 */     return this.stored;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean isDirty()
/* 107:    */   {
/* 108:176 */     return !isAtMark(this.checkpointMark);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public synchronized void fireAfter(UndoableEdit edit)
/* 112:    */   {
/* 113:185 */     postEdit(edit, this.afterListeners);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public synchronized void redo()
/* 117:    */     throws CannotRedoException
/* 118:    */   {
/* 119:195 */     UndoableEdit edit = editToBeRedone();
/* 120:196 */     super.redo();
/* 121:197 */     postEdit(edit, this.afterListeners);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public synchronized void undo()
/* 125:    */     throws CannotUndoException
/* 126:    */   {
/* 127:207 */     UndoableEdit edit = editToBeUndone();
/* 128:208 */     super.undo();
/* 129:209 */     postEdit(edit, this.afterListeners);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public synchronized void discardAllEdits()
/* 133:    */   {
/* 134:219 */     super.discardAllEdits();
/* 135:220 */     this.tailTrimSerial += 1;
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected void trimEdits(int from, int to)
/* 139:    */   {
/* 140:233 */     super.trimEdits(from, to);
/* 141:234 */     if ((from == 0) && (to >= 0)) {
/* 142:235 */       this.tailTrimSerial += 1;
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   protected void postEdit(UndoableEdit edit, ArrayList<UndoableEditListener> listeners)
/* 147:    */   {
/* 148:246 */     if (this.enablePosting)
/* 149:    */     {
/* 150:247 */       UndoableEditEvent ev = new UndoableEditEvent(this, edit);
/* 151:248 */       Iterator<UndoableEditListener> le = new ArrayList(listeners).iterator();
/* 152:249 */       while (le.hasNext()) {
/* 153:250 */         ((UndoableEditListener)le.next()).undoableEditHappened(ev);
/* 154:    */       }
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:259 */   private int indexOfEditToBeUndone = -1;
/* 159:    */   
/* 160:    */   private void updateIndexOfEditToBeUndone()
/* 161:    */   {
/* 162:261 */     this.indexOfEditToBeUndone = this.edits.indexOf(editToBeUndone());
/* 163:    */   }
/* 164:    */   
/* 165:264 */   private final AbstractListModel sharedUndoInstance = new AbstractListModel()
/* 166:    */   {
/* 167:    */     public Object getElementAt(int index)
/* 168:    */     {
/* 169:266 */       return ExtendedUndoManager.this.edits.get(ExtendedUndoManager.this.indexOfEditToBeUndone - index);
/* 170:    */     }
/* 171:    */     
/* 172:    */     public int getSize()
/* 173:    */     {
/* 174:269 */       return ExtendedUndoManager.this.indexOfEditToBeUndone + 1;
/* 175:    */     }
/* 176:    */   };
/* 177:273 */   private final AbstractListModel sharedRedoInstance = new AbstractListModel()
/* 178:    */   {
/* 179:    */     public Object getElementAt(int index)
/* 180:    */     {
/* 181:275 */       return ExtendedUndoManager.this.edits.get(ExtendedUndoManager.this.indexOfEditToBeUndone + index + 1);
/* 182:    */     }
/* 183:    */     
/* 184:    */     public int getSize()
/* 185:    */     {
/* 186:278 */       return ExtendedUndoManager.this.edits.size() - ExtendedUndoManager.this.indexOfEditToBeUndone - 1;
/* 187:    */     }
/* 188:    */   };
/* 189:    */   
/* 190:    */   public ListModel getUndoListModel()
/* 191:    */   {
/* 192:293 */     updateIndexOfEditToBeUndone();
/* 193:294 */     return this.sharedUndoInstance;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public ListModel getRedoListModel()
/* 197:    */   {
/* 198:308 */     updateIndexOfEditToBeUndone();
/* 199:309 */     return this.sharedRedoInstance;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void undoTo(UndoableEdit edit)
/* 203:    */   {
/* 204:322 */     this.enablePosting = false;
/* 205:323 */     super.undoTo(edit);
/* 206:324 */     this.enablePosting = true;
/* 207:325 */     postEdit(edit, this.afterListeners);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public void redoTo(UndoableEdit edit)
/* 211:    */   {
/* 212:338 */     this.enablePosting = false;
/* 213:339 */     super.redoTo(edit);
/* 214:340 */     this.enablePosting = true;
/* 215:341 */     postEdit(edit, this.afterListeners);
/* 216:    */   }
/* 217:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.ExtendedUndoManager
 * JD-Core Version:    0.7.0.1
 */