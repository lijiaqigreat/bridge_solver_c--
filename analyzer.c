#include "analyzer.h"
#include <math.h>
#define CUBE(a) ((a)*(a)*(a))
#define MATRIX(a,b) matrix[MAX_EQUATION*(a)+(b)]

int analyze(Result* result,OptimizeTask* task,const BridgeInfo *bridgeInfo, const PositionHintB *position, const TypeHintCostB *thc){
    //update XY
    Double* XY=(Double*)result->XY;
    Double* matrix=(Double*)result->matrix;
    Double* loads=(Double*)result->loads;
    for(int t1=0;t1<bridgeInfo->fixedJointSize*2;t1++){
        XY[t1]=bridgeInfo->positionHint.xy[t1]*0.25;
    }
    for(int t1=bridgeInfo->fixedJointSize;t1<bridgeInfo->totalJointSize;++t1){
        int t2=t1-bridgeInfo->fixedJointSize;
        XY[t1*2  ]=(bridgeInfo->positionHint.xy[t1*2  ]+((position->joints[t2]>>4)&15)^8-8)/4.;
        XY[t1*2+1]=(bridgeInfo->positionHint.xy[t1*2+1]+((position->joints[t2]   )&15)^8-8)/4.;
    }
   
    int equationSize=bridgeInfo->totalJointSize*2;
    //reset matrix
    for (int t1 = 0; t1 < equationSize; ++t1) {
        for (int t2 = 0; t2 < equationSize; ++t2) {
            MATRIX(t1,t2)=0;
        }
    }
   
    //reset loads[0]
    for (int t1 = 0; t1 < bridgeInfo->totalJointSize; ++t1) {
        loads[t1] = 0;
    }
   
    //add deckWeight to loads[0]
    for (int t1 = 1; t1 < bridgeInfo->deckSize; ++t1) {
        loads[t1] -= bridgeInfo->deckWeight;
    }
    loads[0] -= bridgeInfo->deckWeight / 2;
    loads[bridgeInfo->deckSize] -= bridgeInfo->deckWeight / 2;
   
    for(int t=0;t<4;t++){
        printf("type: %d|%3d|%s\n",t,thc->bundle[t],"");
    }
    //iterate members
    for (int t = 0; t < bridgeInfo->memberSize; ++t) {
        int j1x = bridgeInfo->memberLinks[t].j1 * 2    ;
        int j2x = bridgeInfo->memberLinks[t].j2 * 2    ;
        int j1y = j1x+1;
        int j2y = j2x+1;
        Double mx = XY[j2x] - XY[j1x];
        Double my = XY[j2y] - XY[j1y];
        Double length=sqrt(mx * mx + my * my);
        result->memberLength[t]=length;

        if(length==0.){
            return -1;
        }
        //add member weight
        const TypeB* type = &bridgeInfo->types[thc->bundle[thc->member[t]]];
        //printf("member %3d:%2d|%2d|%6.2lf|%3d|%s\n",t,bridgeInfo->memberLinks[t].j1,bridgeInfo->memberLinks[t].j2,result->memberLength[t],thc->member[t],type->name);
        loads[j1x / 2] -= type->weight * length;
        loads[j2x / 2] -= type->weight * length;
        //set matrix
        double xx = mx * mx * type->AE / CUBE(length);
        double xy = mx * my * type->AE / CUBE(length);
        double yy = my * my * type->AE / CUBE(length);
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
    const Int* tmpt1_=&bridgeInfo->fixedIndex[0];
    while((*tmpt1_)!=-1){
        for (int t2 = 0; t2 < equationSize; ++t2) {
            MATRIX(*tmpt1_,t2) = 0;
            MATRIX(t2,*tmpt1_) = 0;
        }
        MATRIX(*tmpt1_,*tmpt1_) = 1;
        ++tmpt1_;
    }
   
    //solve
    for (int ie = 0; ie < equationSize; ++ie) {
        Double pivot = MATRIX(ie,ie);
        if (-0.99 < pivot && pivot < 0.99) {
            //TODO
            return 2;
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
        result->jdisplacement[t2] = 0;
    }
    //set jd[0]
    for (int t1 = 0; t1 < equationSize; ++t1) {
        Double temp = 0;
        for (int t2 = 0; t2 < bridgeInfo->totalJointSize; ++t2) {
            temp += MATRIX(t1,t2 * 2 + 1) * loads[t2];
        }
        result->jdisplacement[t1] = temp;
    }
    Double backWeight=bridgeInfo->backWeight;
    Double frontWeight=bridgeInfo->frontWeight;
    //set jd[t]
    for (int t1 = 0; t1 < bridgeInfo->deckSize; ++t1) {
        for (int t2 = 0; t2 < equationSize; ++t2) {
            result->jdisplacement[(t1 + 1)*MAX_EQUATION+t2] = result->jdisplacement[t2]
                    - backWeight * MATRIX(t2,t1 * 2 + 1)
                    - frontWeight * MATRIX(t2,t1 * 2 + 3);
        }
    }
    //set constraints on jd
    tmpt1_=&bridgeInfo->fixedIndex[0];
    while((*tmpt1_)!=-1){
        for (int t2 = 0; t2 <= bridgeInfo->deckSize; ++t2) {
            result->jdisplacement[t2*MAX_EQUATION+(*tmpt1_)] = 0;
        }
        ++tmpt1_;
    }
    //set member force
    for (int t1 = 0; t1 < bridgeInfo->memberSize; ++t1) {
        Double aeOverLL = bridgeInfo->types[thc->bundle[thc->member[t1]]].AE / SQR(result->memberLength[t1]);
        int j1x = bridgeInfo->memberLinks[t1].j1 * 2;
        int j2x = bridgeInfo->memberLinks[t1].j2 * 2;
        Double _max = 0;
        Double _min = -0.;
        for (int t2 = 1; t2 <= bridgeInfo->deckSize; ++t2) {
         
            Double f = aeOverLL * (XY[j2x  ]-XY[j1x  ]) *
                 (result->jdisplacement[t2*MAX_EQUATION+j2x  ] - result->jdisplacement[t2*MAX_EQUATION+j1x  ])
                     + aeOverLL * (XY[j2x+1]-XY[j1x+1]) *
                 (result->jdisplacement[t2*MAX_EQUATION+j2x+1] - result->jdisplacement[t2*MAX_EQUATION+j1x+1]);
            result->memberForce[t2*MAX_EQUATION+t1] = f;
            if (f > _max) {
                _max = f;
            } else if (f < _min) {
                _min = f;
            }
        }
        result->maxForce[t1] = _max;
        result->minForce[t1] = _min;
    }
    
    //set optimizeTask
    int typeIndex=0;
    for(int t1=0;t1<MAX_TYPE;++t1){
        const TypeB* type=&bridgeInfo->types[t1];
        TestMask tmp=0;
        for(int t2=0;t2<bridgeInfo->memberSize;++t2){
            if(ifPassType(type,-result->minForce[t2],result->maxForce[t2],result->memberLength[t2],bridgeInfo->slenderness)){
                tmp|=((TestMask)1)<<t2;
            }
        }
        int t2;
        for(t2=0;t2<typeIndex&&(task->typeTestMask[t2]&tmp)!=tmp;++t2){}
        if(t2==typeIndex){
            task->typeTestMask[typeIndex]=tmp;
            task->index[typeIndex]=t1;
            task->cost[typeIndex]=type->cost;
            typeIndex++;
        }
    }
    for(int t1=0;t1<bridgeInfo->memberSize;t1++){
        task->length[t1]=result->memberLength[t1];
    }
    task->typeSize=typeIndex;
    task->memberSize=bridgeInfo->memberSize;
    task->capCost=1000000.;
    task->minLength=0.;
    return 0;
    
}
