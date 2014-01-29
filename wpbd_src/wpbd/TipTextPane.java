/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.Font;
/*  4:   */ import javax.swing.JTextPane;
/*  5:   */ import javax.swing.UIManager;
/*  6:   */ import javax.swing.text.html.HTMLDocument;
/*  7:   */ import javax.swing.text.html.StyleSheet;
/*  8:   */ 
/*  9:   */ public class TipTextPane
/* 10:   */   extends JTextPane
/* 11:   */ {
/* 12:   */   public TipTextPane()
/* 13:   */   {
/* 14:34 */     setOpaque(false);
/* 15:35 */     setEditable(false);
/* 16:36 */     setContentType("text/html");
/* 17:37 */     setFocusable(false);
/* 18:38 */     HTMLDocument doc = (HTMLDocument)getDocument();
/* 19:39 */     Font font = UIManager.getFont("Label.font");
/* 20:40 */     doc.getStyleSheet().addRule("body { font-family:" + font.getFamily() + ";" + "font-size:" + font.getSize() + "pt; }");
/* 21:41 */     doc.setBase(WPBDApp.getApplication().getClass().getResource("/wpbd/resources/"));
/* 22:   */   }
/* 23:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.TipTextPane
 * JD-Core Version:    0.7.0.1
 */