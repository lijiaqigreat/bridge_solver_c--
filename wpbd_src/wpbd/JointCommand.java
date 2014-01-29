/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Iterator;
/*   6:    */ 
/*   7:    */ public abstract class JointCommand
/*   8:    */   extends EditCommand
/*   9:    */ {
/*  10:    */   protected Member[] deleteMembers;
/*  11:    */   protected Member[] insertMembers;
/*  12:    */   
/*  13:    */   protected JointCommand(EditableBridgeModel bridge)
/*  14:    */   {
/*  15: 34 */     super(bridge);
/*  16:    */   }
/*  17:    */   
/*  18:    */   private static class JointIndexPair
/*  19:    */   {
/*  20:    */     public int lo;
/*  21:    */     public int hi;
/*  22:    */     
/*  23:    */     public JointIndexPair(int a, int b)
/*  24:    */     {
/*  25: 41 */       if (a < b)
/*  26:    */       {
/*  27: 42 */         this.lo = a;
/*  28: 43 */         this.hi = b;
/*  29:    */       }
/*  30:    */       else
/*  31:    */       {
/*  32: 46 */         this.lo = b;
/*  33: 47 */         this.hi = a;
/*  34:    */       }
/*  35:    */     }
/*  36:    */     
/*  37:    */     public JointIndexPair(Member m)
/*  38:    */     {
/*  39: 52 */       this(m.getJointA().getIndex(), m.getJointB().getIndex());
/*  40:    */     }
/*  41:    */     
/*  42:    */     public JointIndexPair(Joint a, Joint b)
/*  43:    */     {
/*  44: 56 */       this(a.getIndex(), b.getIndex());
/*  45:    */     }
/*  46:    */     
/*  47:    */     public boolean equals(Object o)
/*  48:    */     {
/*  49: 61 */       if ((o instanceof JointIndexPair))
/*  50:    */       {
/*  51: 62 */         JointIndexPair p = (JointIndexPair)o;
/*  52: 63 */         return (p.lo == this.lo) && (p.hi == this.hi);
/*  53:    */       }
/*  54: 65 */       return false;
/*  55:    */     }
/*  56:    */     
/*  57:    */     public int hashCode()
/*  58:    */     {
/*  59: 70 */       return this.lo * 199 + this.hi;
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected void fixUpMembers(Affine.Point pt, Joint joint)
/*  64:    */   {
/*  65: 83 */     ArrayList<Member> toDelete = new ArrayList();
/*  66: 84 */     ArrayList<Member> toInsert = new ArrayList();
/*  67: 85 */     HashSet<JointIndexPair> connectedMemberJointPairs = new HashSet();
/*  68: 86 */     Iterator<Member> m = this.bridge.getMembers().iterator();
/*  69: 87 */     while (m.hasNext())
/*  70:    */     {
/*  71: 88 */       Member member = (Member)m.next();
/*  72: 89 */       if (member.hasJoint(joint)) {
/*  73: 90 */         connectedMemberJointPairs.add(new JointIndexPair(member));
/*  74:    */       }
/*  75:    */     }
/*  76: 93 */     m = this.bridge.getMembers().iterator();
/*  77: 94 */     while (m.hasNext())
/*  78:    */     {
/*  79: 95 */       Member member = (Member)m.next();
/*  80: 96 */       Joint a = member.getJointA();
/*  81: 97 */       Joint b = member.getJointB();
/*  82: 98 */       if ((pt.onSegment(a.getPointWorld(), b.getPointWorld())) && (!connectedMemberJointPairs.contains(new JointIndexPair(member))))
/*  83:    */       {
/*  84:101 */         toDelete.add(member);
/*  85:102 */         if (!connectedMemberJointPairs.contains(new JointIndexPair(a, joint))) {
/*  86:103 */           toInsert.add(new Member(member, a, joint));
/*  87:    */         }
/*  88:105 */         if (!connectedMemberJointPairs.contains(new JointIndexPair(joint, b))) {
/*  89:106 */           toInsert.add(new Member(member, joint, b));
/*  90:    */         }
/*  91:    */       }
/*  92:    */     }
/*  93:110 */     int i = this.bridge.getMembers().size() - toDelete.size();
/*  94:111 */     m = toInsert.iterator();
/*  95:112 */     while (m.hasNext()) {
/*  96:113 */       ((Member)m.next()).setIndex(i++);
/*  97:    */     }
/*  98:115 */     this.deleteMembers = ((Member[])toDelete.toArray(new Member[toDelete.size()]));
/*  99:116 */     this.insertMembers = ((Member[])toInsert.toArray(new Member[toInsert.size()]));
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected void fixUpMembers(Joint joint)
/* 103:    */   {
/* 104:128 */     fixUpMembers(joint.getPointWorld(), joint);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void go()
/* 108:    */   {
/* 109:133 */     for (int i = 0; i < this.deleteMembers.length; i++) {
/* 110:134 */       this.deleteMembers[i].setSelected(false);
/* 111:    */     }
/* 112:136 */     EditCommand.delete(this.bridge.getMembers(), this.deleteMembers);
/* 113:137 */     EditCommand.insert(this.bridge.getMembers(), this.insertMembers);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void goBack()
/* 117:    */   {
/* 118:142 */     EditCommand.delete(this.bridge.getMembers(), this.insertMembers);
/* 119:143 */     EditCommand.insert(this.bridge.getMembers(), this.deleteMembers);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public int execute(ExtendedUndoManager undoManager)
/* 123:    */   {
/* 124:155 */     if (this.bridge.getMembers().size() + this.insertMembers.length - this.deleteMembers.length > 200) {
/* 125:156 */       return 3;
/* 126:    */     }
/* 127:158 */     return super.execute(undoManager);
/* 128:    */   }
/* 129:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.JointCommand
 * JD-Core Version:    0.7.0.1
 */