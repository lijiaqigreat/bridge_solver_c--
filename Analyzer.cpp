#include "Analyzer.h"
#define CUBE(a) ((a)*(a)*(a))
#define MATRIX(a,b) result.matrix[MAX_EQUATION*(a)+(b)]

void Analyzer::load(const BridgeInfo& _bridgeInfo){
 bridgeInfo=_bridge_Info;
}
void load(const PositionHintB& _positionHintB, const TypeHintCostB& _typeHintCostB){
 positionHintB=_positionHintB;
 typeHintCostB=_typeHintCostB;
}

void Analyzer::analyze() {
 //update XY
 for(int t1=bridgeInfo.fixedJointSize;t1<bridgeInfo.TotalJointSize;++t1){
  int t2=t1-bridgeInfo.fixedJointSize;
  XY[t1*2  ]=(bridgeInfo.positionHint.xy[t2*2  ]+(positionHintB.joints[t2]>>4)&15-8)/4.;
  XY[t1*2+1]=(bridgeInfo.positionHint.xy[t2*2+1]+(positionHintB.joints[t2]   )&15-8)/4.;
 }

 int equationSize=bridgeInfo.totalJointSize*2;
 //reset matrix
 for (int t1 = 0; t1 < equationSize; ++t1) {
  for (int t2 = 0; t2 < equationSize; ++t2) {
   MATRIX(t1,t2)=0;
  }
 }

 //reset loads[0]
 for (int t1 = 0; t1 < bridgeInfo.totalJointSize; ++t1) {
  result.loads[t1] = 0;
 }

 //add deckWeight to loads[0]
 for (int t1 = 1; t1 < bridgeInfo.deckSize; ++t1) {
  result.loads[t1] -= bridgeInfo.deckWeight;
 }
 result.loads[0] -= bridgeInfo.deckWeight / 2;
 result.loads[bridgeInfo.deckSize] -= bridgeInfo.deckWeight / 2;

 //iterate members
 for (int t = 0; t < bridgeInfo.memberSize; ++t) {
     int j1x = bridgeInfo.memberLinks[t].j1 * 2    ;
     int j2x = bridgeInfo.memberLinks[t].j2 * 2    ;
     int j1y = bridgeInfo.memberLinks[t].j1 * 2 + 1;
     int j2y = bridgeInfo.memberLinks[t].j2 * 2 + 1;
     Double mx = result.XY[j2x] - result.XY[j1x];
     Double my = result.XY[j2y] - result.XY[j1y];
     result.memberLength[t] = Math.sqrt(mx * mx + my * my);
     if(result.memberLength[t]==0){
      //TODO can't be zero! handle error
      return;
     }
     //add member weight
     Type type = bridgeInfo.types[typeHintCostB.bundle[typeHintCostB.member[t]]];
     loads[j1x / 2] -= type.weight * result.memberLength[t];
     loads[j1x / 2] -= type.weight * result.memberLength[t];
     loads[j2x / 2] -= type.weight * result.memberLength[t];
     //set matrix
     double xx = mx * mx * type.AE / CUBE(result.memberLength[t]);
     double xy = mx * my * type.AE / CUBE(result.memberLength[t]);
     double yy = my * my * type.AE / CUBE(result.memberLength[t]);
     MATRIX(j1x,j1x) += xx;
     MATRIX(j1x,j1y) += xy;
     MATRIX(j1x,j2x) -= xx;
     MATRIX(j1x,j2y) -= xy;
     MATRIX(j1y,j1x) += xy;
     MATRIX(j1y,j1y) += yy;
     MATRIX(j1y,j2x) -= xy;
     MATRIX(j1y,j2y) -= yy;
     MATRIX(j2x,j1x) -= xx;
     MATRIX(j2x,j1y) -= xy;
     MATRIX(j2x,j2x) += xx;
     MATRIX(j2x,j2y) += xy;
     MATRIX(j2y,j1x) -= xy;
     MATRIX(j2y,j1y) -= yy;
     MATRIX(j2y,j2x) += xy;
     MATRIX(j2y,j2y) += yy;
 }

 //set constraints on MATRIX
 int tmpt1=0;
 while(bridgeInfo.fixedIndex[tmpt1]!=-1){
     int t1=bridgeInfo.fixedIndex[tmpt1];
     for (int t2 = 0; t2 < bridgeInfo.totalJointSize*2; ++t2) {
         MATRIX(t1,t2) = 0;
         MATRIX(t2,t1) = 0;
     }
     MATRIX(t1,t1) = 1;
     ++tmpt1;
 }

 //solve
 for (int ie = 0; ie < equationSize; ++ie) {
     Double pivot = MATRIX(ie,ie);
     if (-0.99 < pivot && pivot < 0.99) {
         //TODO
         totalCost=1000000;
         status = 1;
         return;
     }
     Double pivr = 1.0 / pivot;
     for (int k = 0; k < equationSize; ++k) {
         MATRIX(ie,k) /= pivot;
     }
     for (int k = 0; k < equationSize; ++k) {
         if (k != ie) {
             pivot = MATRIX(k,ie);
             for (int j = 0; j < equationSize; ++j) {
                 MATRIX(k,j) -= MATRIX(ie,j) * pivot;
             }
             MATRIX(k,ie) = -pivot * pivr;
         }
     }
     MATRIX(ie,ie) = pivr;
 }

 //initialize jd[0] (joint displacement)
 for (int t2 = 0; t2 < equationSize; ++t2) {
     result.jdisplacement[t2] = 0;
 }
 //set jd[0]
 for (int t1 = 0; t1 < equationSize; ++t1) {
     Double temp = 0;
     for (int t2 = 0; t2 < bridgeInfo.totalJointSize; ++t2) {
         temp += MATRIX(t1,t2 * 2 + 1) * result.loads[t2];
     }
     result.jdisplacement[t1] = temp;
 }
 //set jd[t]
 for (int t1 = 0; t1 < bridgeInfo.deckSize; ++t1) {
     for (int t2 = 0; t2 < equationSize; ++t2) {
         jdisplacement[(t1 + 1)*MAX_EQUATION+t2] = jdisplacement[t2]
                 - bridgeInfo.backWeight * MATRIX(t2,t1 * 2 + 1)
                 - bridgeInfo.frontWeight * MATRIX(t2,t1 * 2 + 3);
     }
 }
 //set constraints on jd
 tmpt1=0;
 while(bridgeInfo.fixedIndex[tmpt1]!=-1){
     int t1=bridgeInfo.fixedIndex[tmpt1];
     for (int t2 = 0; t2 <= bridgeInfo.deckSize; ++t2) {
         jdisplacement[t2][t1] = 0;
     }
     tmpt1=0;
 }
 //set member force
 for (int t1 = 0; t1 < memberSize; ++t1) {
     Double aeOverLL = bridgeInfo.types[typeHintCostB.bundle[typeHintCostB.member[t1]]].AE / SQR(result.memberLength[t1]);
     int j1x = bridgeInfo.memberLinks[t].j1 * 2;
     int j2x = bridgeInfo.memberLinks[t].j2 * 2;
     Double _max = 0;
     Double _min = 0;
     for (int t2 = 0; t2 <= loadSize; ++t2) {
      
         Double f = aeOverLL * (result.XY(j2x  )-result.XY(j1x  )) *
              (result.jdisplacement[t2*MAX_EQUATION+j2x  ] - result.jdisplacement[t2*MAX_EQUATION+j1x  ])
                  + aeOverLL * (result.XY(j2x+1)-result.XY(j1x+1)) *
              (result.jdisplacement[t2*MAX_EQUATION+j2x+1] - result.jdisplacement[t2*MAX_EQUATION+j1x+1]);
         result.memberForce[t2*MAX_MEMBER+t1] = f;
         if (f > _max) {
             _max = f;
         } else if (f < _min) {
             _min = f;
         }
     }
     result.maxForce[t1] = _max;
     result.minForce[t1] = _min;
 }
 
 //set optimizeTask
 TestMask typeTestMask[MAX_TYPE];
 for(int t1=0;t1<bridgeInfo.typeSize;++t1){
  Type& type=bridgeInfo.types[t1];
  TestMask tmp=0;
  for(int t2=0;t2<bridgeInfo.memberSize;++t2){
   tmp<<=1;
   if(type.ifPass(result.minForce[t2],result.maxForce[t2],result.memberLength[t2],bridgeInfo.slenderness)){
    ++tmp;
   }
  }
  typeTestMask[t1]=tmp;
 }
 optimizeTask.typeSize=0;
 
}
