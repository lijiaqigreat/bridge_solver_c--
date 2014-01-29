/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import com.jogamp.opengl.util.texture.Texture;
/*   4:    */ import javax.media.opengl.GL2;
/*   5:    */ 
/*   6:    */ public class Overlay
/*   7:    */ {
/*   8: 28 */   private int x = 0;
/*   9: 29 */   private int y = 0;
/*  10: 30 */   private float alpha = 0.2F;
/*  11:    */   private Texture texture;
/*  12:    */   private int size;
/*  13: 34 */   private static final float[] texBox = { 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F };
/*  14: 40 */   private static final float[] boxTemplate = { -0.5F, -0.5F, 0.5F, -0.5F, 0.5F, 0.5F, -0.5F, 0.5F };
/*  15: 46 */   private final float[] box = new float[boxTemplate.length];
/*  16:    */   
/*  17:    */   public int getX()
/*  18:    */   {
/*  19: 54 */     return this.x;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public int getY()
/*  23:    */   {
/*  24: 63 */     return this.y;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void initialize(GL2 gl, String textureResourceName)
/*  28:    */   {
/*  29: 67 */     this.texture = WPBDApp.getApplication().getTextureResource(textureResourceName, false, "png");
/*  30: 68 */     this.texture.setTexParameteri(gl, 10242, 10497);
/*  31: 69 */     this.texture.setTexParameteri(gl, 10243, 10497);
/*  32: 70 */     this.size = this.texture.getImageWidth();
/*  33: 71 */     setPosition(this.size / 2 + 1, this.size / 2 + 1);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setPosition(int x, int y)
/*  37:    */   {
/*  38: 81 */     for (int i = 0; i < this.box.length; i += 2)
/*  39:    */     {
/*  40: 82 */       this.box[(i + 0)] = (boxTemplate[(i + 0)] * this.size + x);
/*  41: 83 */       this.box[(i + 1)] = (boxTemplate[(i + 1)] * this.size + y);
/*  42:    */     }
/*  43: 85 */     this.x = x;
/*  44: 86 */     this.y = y;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public boolean inside(int x, int y)
/*  48:    */   {
/*  49: 97 */     return (this.box[0] <= x) && (x <= this.box[2]) && (this.box[1] <= y) && (y <= this.box[5]);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void mouseMoved(int x, int y)
/*  53:    */   {
/*  54:107 */     this.alpha = (inside(x, y) ? 1.0F : 0.15F);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void display(GL2 gl)
/*  58:    */   {
/*  59:116 */     gl.glActiveTexture(33984);
/*  60:117 */     this.texture.enable(gl);
/*  61:118 */     this.texture.bind(gl);
/*  62:    */     
/*  63:120 */     gl.glDisable(2884);
/*  64:121 */     gl.glDisable(2896);
/*  65:122 */     gl.glEnable(3042);
/*  66:123 */     gl.glBlendFunc(770, 771);
/*  67:124 */     gl.glBegin(7);
/*  68:125 */     gl.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
/*  69:126 */     for (int i = 0; i < this.box.length; i += 2)
/*  70:    */     {
/*  71:127 */       gl.glTexCoord2fv(texBox, i);
/*  72:128 */       gl.glVertex2fv(this.box, i);
/*  73:    */     }
/*  74:130 */     gl.glEnd();
/*  75:131 */     gl.glEnable(2884);
/*  76:132 */     gl.glEnable(2896);
/*  77:133 */     gl.glDisable(3042);
/*  78:134 */     this.texture.disable(gl);
/*  79:    */   }
/*  80:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Overlay
 * JD-Core Version:    0.7.0.1
 */