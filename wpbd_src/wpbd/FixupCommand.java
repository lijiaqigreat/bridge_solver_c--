/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Iterator;
/*  5:   */ 
/*  6:   */ public class FixupCommand
/*  7:   */   extends EditCommand
/*  8:   */ {
/*  9:   */   protected Member[] deleteMembers;
/* 10:   */   protected Member[] insertMembers;
/* 11:   */   
/* 12:   */   public FixupCommand(EditableBridgeModel bridge)
/* 13:   */   {
/* 14:43 */     super(bridge);
/* 15:44 */     ArrayList<Member> toDelete = new ArrayList();
/* 16:45 */     ArrayList<Member> toInsert = new ArrayList();
/* 17:46 */     ArrayList<Joint> transsected = new ArrayList();
/* 18:47 */     Iterator<Member> m = bridge.getMembers().iterator();
/* 19:48 */     while (m.hasNext())
/* 20:   */     {
/* 21:49 */       Member member = (Member)m.next();
/* 22:50 */       bridge.getTranssectedJoints(transsected, member.getJointA(), member.getJointB());
/* 23:51 */       if (transsected.size() > 0)
/* 24:   */       {
/* 25:52 */         toDelete.add(member);
/* 26:53 */         transsected.add(member.getJointB());
/* 27:54 */         Joint a = member.getJointA();
/* 28:55 */         Iterator<Joint> j = transsected.iterator();
/* 29:56 */         while (j.hasNext())
/* 30:   */         {
/* 31:57 */           Joint b = (Joint)j.next();
/* 32:58 */           if (bridge.getMember(a, b) == null) {
/* 33:59 */             toInsert.add(new Member(member, a, b));
/* 34:   */           }
/* 35:61 */           a = b;
/* 36:   */         }
/* 37:   */       }
/* 38:   */     }
/* 39:65 */     int i = bridge.getMembers().size() - toDelete.size();
/* 40:66 */     m = toInsert.iterator();
/* 41:67 */     while (m.hasNext()) {
/* 42:68 */       ((Member)m.next()).setIndex(i++);
/* 43:   */     }
/* 44:70 */     this.deleteMembers = ((Member[])toDelete.toArray(new Member[toDelete.size()]));
/* 45:71 */     this.insertMembers = ((Member[])toInsert.toArray(new Member[toInsert.size()]));
/* 46:72 */     this.presentationName = getMembersMessage("autofix.text", this.deleteMembers);
/* 47:   */   }
/* 48:   */   
/* 49:   */   public int revisedMemberCount()
/* 50:   */   {
/* 51:81 */     return this.deleteMembers.length;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public void go()
/* 55:   */   {
/* 56:86 */     for (int i = 0; i < this.deleteMembers.length; i++) {
/* 57:87 */       this.deleteMembers[i].setSelected(false);
/* 58:   */     }
/* 59:89 */     EditCommand.delete(this.bridge.getMembers(), this.deleteMembers);
/* 60:90 */     EditCommand.insert(this.bridge.getMembers(), this.insertMembers);
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void goBack()
/* 64:   */   {
/* 65:95 */     EditCommand.delete(this.bridge.getMembers(), this.insertMembers);
/* 66:96 */     EditCommand.insert(this.bridge.getMembers(), this.deleteMembers);
/* 67:   */   }
/* 68:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FixupCommand
 * JD-Core Version:    0.7.0.1
 */