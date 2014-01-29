/*  1:   */ package wpbd;
import java.io.*;
/*  2:   */ 
/*  3:   */ public class RC4
/*  4:   */ {
/*  5:   */   private int xState;
/*  6:   */   private int yState;
/*  7:24 */   private byte[] buf = new byte[256];
/*  8:   */   
/*  9:   */   public RC4()
/* 10:   */   {
/* 11:30 */     for (int i = 0; i < 256; i++) {
/* 12:31 */       this.buf[i] = ((byte)i);
/* 13:   */     }
/* 14:33 */     this.xState = (this.yState = 0);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void setKey(byte[] key)
/* 18:   */   {
/* 19:43 */     int len = Math.min(256, key.length);
/* 20:   */     int x,y;
/* 21:44 */     for (x = y = 0; x < 256; x++)
/* 22:   */     {
/* 23:45 */       y = y + this.buf[x] + key[(x % len)] & 0xFF;
/* 24:46 */       int tmp = this.buf[x];
/* 25:47 */       this.buf[x] = this.buf[y];
/* 26:48 */       this.buf[y] = ((byte)tmp);
/* 27:   */     }
/* 28:50 */     this.xState = x;
/* 29:51 */     this.yState = y;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setKey(String s)
/* 33:   */   {
/* 34:60 */     setKey(s.getBytes());
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void endecrypt(byte[] buf)
/* 38:   */   {
/* 39:69 */     int x = this.xState;
/* 40:70 */     int y = this.yState;
/* 41:71 */     byte[] s = this.buf;
/* 42:72 */     for (int i = 0; i < buf.length; i++)
/* 43:   */     {
/* 44:73 */       x = x + 1 & 0xFF;
/* 45:74 */       y = y + s[x] & 0xFF;
/* 46:75 */       byte tmp = s[x];
/* 47:76 */       s[x] = s[y];
/* 48:77 */       s[y] = tmp; int 
/* 49:78 */         tmp68_66 = i; byte[] tmp68_65 = buf;tmp68_65[tmp68_66] = ((byte)(tmp68_65[tmp68_66] ^ s[(s[x] + s[y] & 0xFF)]));
/* 50:   */     }
/* 51:80 */     this.xState = x;
/* 52:81 */     this.yState = y;
/* 53:   */   }
public static void main(String[] args) throws Exception{
  RandomAccessFile f=new RandomAccessFile("../Eg/2014/MyDesign.bdc","r");
  byte[] b=new byte[(int)f.length()];
  f.read(b);
  RC4 rc4=new RC4();
  rc4.setKey("QuenchHollow");
  rc4.endecrypt(b);
  System.out.println(new String(b));
}
/* 54:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.RC4
 * JD-Core Version:    0.7.0.1
 */
