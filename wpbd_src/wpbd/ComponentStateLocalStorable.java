/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import javax.swing.JCheckBox;
/*   5:    */ import javax.swing.JSlider;
/*   6:    */ 
/*   7:    */ public class ComponentStateLocalStorable
/*   8:    */   implements Serializable
/*   9:    */ {
/*  10:    */   boolean[] checkStates;
/*  11:    */   int[] sliderStates;
/*  12:    */   
/*  13:    */   public boolean[] getCheckStates()
/*  14:    */   {
/*  15: 43 */     return this.checkStates;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public void setCheckStates(boolean[] checkStates)
/*  19:    */   {
/*  20: 52 */     this.checkStates = checkStates;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public int[] getSliderStates()
/*  24:    */   {
/*  25: 61 */     return this.sliderStates;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setSliderStates(int[] sliderStates)
/*  29:    */   {
/*  30: 70 */     this.sliderStates = sliderStates;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void add(JCheckBox[] checkBoxes)
/*  34:    */   {
/*  35: 79 */     this.checkStates = new boolean[checkBoxes.length];
/*  36: 80 */     for (int i = 0; i < checkBoxes.length; i++) {
/*  37: 81 */       this.checkStates[i] = checkBoxes[i].isSelected();
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void add(JSlider[] sliders)
/*  42:    */   {
/*  43: 91 */     this.sliderStates = new int[sliders.length];
/*  44: 92 */     for (int i = 0; i < sliders.length; i++) {
/*  45: 93 */       this.sliderStates[i] = sliders[i].getValue();
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void apply(JCheckBox[] checkBoxes)
/*  50:    */   {
/*  51:103 */     if ((this.checkStates != null) && (this.checkStates.length == checkBoxes.length)) {
/*  52:104 */       for (int i = 0; i < checkBoxes.length; i++) {
/*  53:105 */         checkBoxes[i].setSelected(this.checkStates[i]);
/*  54:    */       }
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void apply(JSlider[] sliders)
/*  59:    */   {
/*  60:116 */     if ((this.sliderStates != null) && (this.sliderStates.length == sliders.length)) {
/*  61:117 */       for (int i = 0; i < sliders.length; i++) {
/*  62:118 */         sliders[i].setValue(this.sliderStates[i]);
/*  63:    */       }
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void save(String fileName)
/*  68:    */   {
/*  69:129 */     WPBDApp.saveToLocalStorage(this, fileName);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static ComponentStateLocalStorable load(String fileName)
/*  73:    */   {
/*  74:139 */     return (ComponentStateLocalStorable)WPBDApp.loadFromLocalStorage(fileName);
/*  75:    */   }
/*  76:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.ComponentStateLocalStorable
 * JD-Core Version:    0.7.0.1
 */