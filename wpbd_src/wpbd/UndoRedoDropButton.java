/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.Dimension;
/*  4:   */ import java.awt.Rectangle;
/*  5:   */ import java.awt.event.ItemEvent;
/*  6:   */ import java.awt.event.ItemListener;
/*  7:   */ import javax.swing.Icon;
/*  8:   */ import javax.swing.JToggleButton;
/*  9:   */ import javax.swing.SingleSelectionModel;
/* 10:   */ 
/* 11:   */ public class UndoRedoDropButton
/* 12:   */   extends JToggleButton
/* 13:   */ {
/* 14:   */   private final UndoRedoDropList dropList;
/* 15:   */   
/* 16:   */   private UndoRedoDropButton(ExtendedUndoManager undoManager, Icon icon, int width, int height, boolean redo)
/* 17:   */   {
/* 18:43 */     super(icon);
/* 19:44 */     Dimension size = new Dimension(width, height);
/* 20:45 */     setMaximumSize(size);
/* 21:46 */     setPreferredSize(size);
/* 22:47 */     this.dropList = new UndoRedoDropList(this, undoManager, redo);
/* 23:48 */     addItemListener(this.buttonListener);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static JToggleButton getUndoDropButton(ExtendedUndoManager undoManager)
/* 27:   */   {
/* 28:58 */     return new UndoRedoDropButton(undoManager, WPBDApp.getApplication().getIconResource("drop.png"), 14, 24, false);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static JToggleButton getRedoDropButton(ExtendedUndoManager undoManager)
/* 32:   */   {
/* 33:68 */     return new UndoRedoDropButton(undoManager, WPBDApp.getApplication().getIconResource("drop.png"), 14, 24, true);
/* 34:   */   }
/* 35:   */   
/* 36:71 */   private final ItemListener buttonListener = new ItemListener()
/* 37:   */   {
/* 38:   */     public void itemStateChanged(ItemEvent e)
/* 39:   */     {
/* 40:73 */       if (e.getStateChange() == 1)
/* 41:   */       {
/* 42:74 */         UndoRedoDropButton.this.dropList.getSelectionModel().clearSelection();
/* 43:75 */         Rectangle bounds = UndoRedoDropButton.this.getBounds();
/* 44:76 */         UndoRedoDropButton.this.dropList.show(UndoRedoDropButton.this.getParent(), bounds.x, bounds.y + bounds.height);
/* 45:   */       }
/* 46:   */       else
/* 47:   */       {
/* 48:80 */         UndoRedoDropButton.this.dropList.close();
/* 49:   */       }
/* 50:   */     }
/* 51:   */   };
/* 52:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.UndoRedoDropButton
 * JD-Core Version:    0.7.0.1
 */