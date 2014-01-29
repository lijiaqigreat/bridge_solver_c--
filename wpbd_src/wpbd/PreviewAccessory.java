/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.BorderLayout;
/*  4:   */ import java.awt.Dimension;
/*  5:   */ import java.beans.PropertyChangeEvent;
/*  6:   */ import java.beans.PropertyChangeListener;
/*  7:   */ import java.io.File;
/*  8:   */ import java.io.IOException;
/*  9:   */ import javax.swing.BorderFactory;
/* 10:   */ import javax.swing.JLabel;
/* 11:   */ import javax.swing.JPanel;
/* 12:   */ import org.jdesktop.application.ResourceMap;
/* 13:   */ 
/* 14:   */ public class PreviewAccessory
/* 15:   */   extends JPanel
/* 16:   */   implements PropertyChangeListener
/* 17:   */ {
/* 18:   */   private final BridgeModel cartoonBridge;
/* 19:   */   private final BridgeView cartoonView;
/* 20:   */   private final JLabel drawing;
/* 21:   */   
/* 22:   */   public PreviewAccessory()
/* 23:   */   {
/* 24:44 */     Dimension size = new Dimension(300, 100);
/* 25:45 */     setMinimumSize(size);
/* 26:46 */     setPreferredSize(size);
/* 27:47 */     ResourceMap resourceMap = WPBDApp.getResourceMap(PreviewAccessory.class);
/* 28:48 */     JLabel previewLabel = new JLabel(resourceMap.getString("preview.text", new Object[0]));
/* 29:49 */     int h = previewLabel.getPreferredSize().height;
/* 30:50 */     setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createMatteBorder(0, h, h, h, getBackground())));
/* 31:   */     
/* 32:   */ 
/* 33:53 */     this.cartoonBridge = new BridgeModel();
/* 34:54 */     this.cartoonView = new BridgeDraftingView(this.cartoonBridge);
/* 35:55 */     this.drawing = this.cartoonView.getDrawing(2.0D, resourceMap.getString("noBridge.text", new Object[0]));
/* 36:56 */     setLayout(new BorderLayout());
/* 37:57 */     add(previewLabel, "North");
/* 38:58 */     add(this.drawing, "Center");
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void propertyChange(PropertyChangeEvent evt)
/* 42:   */   {
/* 43:68 */     if ("SelectedFileChangedProperty".equals(evt.getPropertyName()))
/* 44:   */     {
/* 45:69 */       File f = (File)evt.getNewValue();
/* 46:70 */       if ((f != null) && (f.getName().toLowerCase().endsWith(".bdc"))) {
/* 47:   */         try
/* 48:   */         {
/* 49:72 */           this.cartoonBridge.read(f);
/* 50:73 */           this.cartoonView.initialize(this.cartoonBridge.getDesignConditions());
/* 51:   */         }
/* 52:   */         catch (IOException ex)
/* 53:   */         {
/* 54:75 */           this.cartoonView.initialize(null);
/* 55:   */         }
/* 56:   */       } else {
/* 57:79 */         this.cartoonView.initialize(null);
/* 58:   */       }
/* 59:81 */       this.drawing.repaint();
/* 60:   */     }
/* 61:   */   }
/* 62:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.PreviewAccessory
 * JD-Core Version:    0.7.0.1
 */