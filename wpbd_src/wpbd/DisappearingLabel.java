/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.event.ActionEvent;
/*  4:   */ import java.awt.event.ActionListener;
/*  5:   */ import javax.swing.JLabel;
/*  6:   */ import javax.swing.Timer;
/*  7:   */ 
/*  8:   */ public class DisappearingLabel
/*  9:   */   extends JLabel
/* 10:   */   implements ActionListener
/* 11:   */ {
/* 12:   */   private final Timer hider;
/* 13:   */   
/* 14:   */   public DisappearingLabel(int delay)
/* 15:   */   {
/* 16:16 */     this.hider = new Timer(delay, this);
/* 17:17 */     this.hider.setRepeats(false);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setText(String s)
/* 21:   */   {
/* 22:22 */     super.setText(s);
/* 23:23 */     if ((s != null) && (!s.isEmpty())) {
/* 24:24 */       this.hider.start();
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void actionPerformed(ActionEvent e)
/* 29:   */   {
/* 30:29 */     super.setText(null);
/* 31:   */   }
/* 32:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DisappearingLabel
 * JD-Core Version:    0.7.0.1
 */