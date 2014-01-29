/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Iterator;
/*  5:   */ 
/*  6:   */ public class InsertMemberCommand
/*  7:   */   extends EditCommand
/*  8:   */ {
/*  9:   */   protected Member[] members;
/* 10:   */   
/* 11:   */   public InsertMemberCommand(EditableBridgeModel bridge, Member member)
/* 12:   */   {
/* 13:36 */     super(bridge);
/* 14:37 */     ArrayList<Member> toInsert = new ArrayList();
/* 15:38 */     ArrayList<Joint> transsected = new ArrayList();
/* 16:39 */     bridge.getTranssectedJoints(transsected, member.getJointA(), member.getJointB());
/* 17:41 */     if (transsected.isEmpty())
/* 18:   */     {
/* 19:42 */       this.members = new Member[] { member };
/* 20:   */     }
/* 21:   */     else
/* 22:   */     {
/* 23:45 */       transsected.add(member.getJointB());
/* 24:46 */       Joint a = member.getJointA();
/* 25:47 */       Iterator<Joint> j = transsected.iterator();
/* 26:48 */       while (j.hasNext())
/* 27:   */       {
/* 28:49 */         Joint b = (Joint)j.next();
/* 29:50 */         if (bridge.getMember(a, b) == null) {
/* 30:51 */           toInsert.add(new Member(member, a, b));
/* 31:   */         }
/* 32:53 */         a = b;
/* 33:   */       }
/* 34:55 */       this.members = ((Member[])toInsert.toArray(new Member[toInsert.size()]));
/* 35:   */     }
/* 36:57 */     assignMemberIndices();
/* 37:58 */     this.presentationName = getMembersMessage("insertMember.text", this.members);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public InsertMemberCommand(EditableBridgeModel bridge, Member[] members)
/* 41:   */   {
/* 42:68 */     super(bridge);
/* 43:69 */     this.members = members;
/* 44:70 */     assignMemberIndices();
/* 45:71 */     this.presentationName = getMembersMessage("autoInsertMember.text", members);
/* 46:   */   }
/* 47:   */   
/* 48:   */   private void assignMemberIndices()
/* 49:   */   {
/* 50:75 */     int n = this.bridge.getMembers().size();
/* 51:76 */     for (int i = 0; i < this.members.length; i++) {
/* 52:77 */       this.members[i].setIndex(n++);
/* 53:   */     }
/* 54:   */   }
/* 55:   */   
/* 56:   */   public void go()
/* 57:   */   {
/* 58:85 */     EditCommand.insert(this.bridge.getMembers(), this.members);
/* 59:   */   }
/* 60:   */   
/* 61:   */   public void goBack()
/* 62:   */   {
/* 63:91 */     EditCommand.delete(this.bridge.getMembers(), this.members);
/* 64:   */   }
/* 65:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.InsertMemberCommand
 * JD-Core Version:    0.7.0.1
 */