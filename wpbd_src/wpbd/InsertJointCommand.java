/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public class InsertJointCommand
/*  6:   */   extends JointCommand
/*  7:   */ {
/*  8:   */   protected Joint[] joints;
/*  9:   */   
/* 10:   */   public InsertJointCommand(EditableBridgeModel bridge, Joint joint)
/* 11:   */   {
/* 12:37 */     super(bridge);
/* 13:38 */     joint.setIndex(bridge.getJoints().size());
/* 14:39 */     this.joints = new Joint[] { joint };
/* 15:40 */     fixUpMembers(joint);
/* 16:41 */     this.presentationName = (getString("insertJoint.text") + " " + this.joints[0].getPointWorld() + ".");
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void go()
/* 20:   */   {
/* 21:49 */     EditCommand.insert(this.bridge.getJoints(), this.joints);
/* 22:   */     
/* 23:51 */     super.go();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void goBack()
/* 27:   */   {
/* 28:60 */     super.goBack();
/* 29:61 */     EditCommand.delete(this.bridge.getJoints(), this.joints);
/* 30:   */   }
/* 31:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.InsertJointCommand
 * JD-Core Version:    0.7.0.1
 */