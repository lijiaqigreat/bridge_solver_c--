#include "analyzer.h"
#include <math.h>
#define CUBE(a) ((a)*(a)*(a))
#define MATRIX(a,b) matrix[MAX_EQUATION*(a)+(b)]

int analyze(Result* result,OptimizeTask* task,const BridgeInfo *bridgeInfo,gconstpointer element){
    //update XY
    Double* XY=(Double*)result->XY;
    Double* matrix=(Double*)result->matrix;
    Double* loads=(Double*)result->loads;
    int hintSize=TYPE_HINT_COST_SIZE(bridgeInfo->memberSize);
    const PositionHintB *position=element+hintSize;
    const TypeHintCostB *thc=(const TypeHintCostB*)element;
    int t1;
    for(t1=0;t1<bridgeInfo->fixedJointSize*2;t1++){
        XY[t1]=bridgeInfo->positionHint.xy[t1]*0.25;
    }
    for(t1=bridgeInfo->fixedJointSize;t1<bridgeInfo->totalJointSize;++t1){
        int t2=t1-bridgeInfo->fixedJointSize;
        XY[t1*2  ]=(bridgeInfo->positionHint.xy[t1*2  ]+(((position->joints[t2]>>4)&15)^8)-8)/4.;
        XY[t1*2+1]=(bridgeInfo->positionHint.xy[t1*2+1]+(((position->joints[t2]   )&15)^8)-8)/4.;
    }
   
    int equationSize=bridgeInfo->totalJointSize*2;
    //reset matrix
    for (t1 = 0; t1 < equationSize; ++t1) {
        int t2;
        for (t2 = 0; t2 < equationSize; ++t2) {
            MATRIX(t1,t2)=0;
        }
    }
   
    //reset loads[0]
    for (t1 = 0; t1 < bridgeInfo->totalJointSize; ++t1) {
        loads[t1] = 0;
    }
   
    //add deckWeight to loads[0]
    for (t1 = 1; t1 < bridgeInfo->deckSize; ++t1) {
        loads[t1] -= bridgeInfo->deckWeight;
    }
    loads[0] -= bridgeInfo->deckWeight / 2;
    loads[bridgeInfo->deckSize] -= bridgeInfo->deckWeight / 2;
   
    for(t1=0;t1<4;t1++){
        //printf("type: %d|%3d|%s\n",t1,thc->bundle[t1],"");
    }
    //iterate members
    for (t1 = 0; t1 < bridgeInfo->memberSize; ++t1) {
        int j1x = bridgeInfo->memberLinks[t1].j1 * 2    ;
        int j2x = bridgeInfo->memberLinks[t1].j2 * 2    ;
        int j1y = j1x+1;
        int j2y = j2x+1;
        Double mx = XY[j2x] - XY[j1x];
        Double my = XY[j2y] - XY[j1y];
        Double length=sqrt(mx * mx + my * my);
        result->memberLength[t1]=length;

        if(length==0.){
            printf("ERROR: length 0!\n");
            return -1;
        }
        //add member weight
        const TypeB* type = &bridgeInfo->types[thc->bundle[thc->member[t1]]];
        //printf("member %3d:%2d|%2d|%6.2lf|%3d|%s\n",t1,bridgeInfo->memberLinks[t1].j1,bridgeInfo->memberLinks[t1].j2,result->memberLength[t1],thc->member[t1],type->name);
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
        int t2;
        for (t2 = 0; t2 < equationSize; ++t2) {
            MATRIX(*tmpt1_,t2) = 0;
            MATRIX(t2,*tmpt1_) = 0;
        }
        MATRIX(*tmpt1_,*tmpt1_) = 1;
        ++tmpt1_;
    }
   
    //solve
    for (t1 = 0; t1 < equationSize; ++t1) {
        Double pivot = MATRIX(t1,t1);
        if (-0.99 < pivot && pivot < 0.99) {
            //TODO
            printf("ERROR: unstable!\n");
            return 2;
        }
        int k;
        Double pivr = 1.0 / pivot;
        for (k = 0; k < equationSize; ++k) {
            MATRIX(t1,k) /= pivot;
        }
        for (k = 0; k < equationSize; ++k) {
            if (k != t1) {
                pivot = MATRIX(k,t1);
                int j;
                for (j = 0; j < equationSize; ++j) {
                    MATRIX(k,j) -= MATRIX(t1,j) * pivot;
                }
                MATRIX(k,t1) = -pivot * pivr;
            }
        }
        MATRIX(t1,t1) = pivr;
    }
   
    //initialize jd[0] (joint displacement)
    for (t1 = 0; t1 < equationSize; ++t1) {
        result->jdisplacement[t1] = 0;
    }
    //set jd[0]
    for (t1 = 0; t1 < equationSize; ++t1) {
        Double temp = 0;
        int t2;
        for (t2 = 0; t2 < bridgeInfo->totalJointSize; ++t2) {
            temp += MATRIX(t1,t2 * 2 + 1) * loads[t2];
        }
        result->jdisplacement[t1] = temp;
    }
    Double backWeight=bridgeInfo->backWeight;
    Double frontWeight=bridgeInfo->frontWeight;
    //set jd[t]
    for (t1 = 0; t1 < bridgeInfo->deckSize; ++t1) {
        int t2;
        for (t2 = 0; t2 < equationSize; ++t2) {
            result->jdisplacement[(t1 + 1)*MAX_EQUATION+t2] = result->jdisplacement[t2]
                    - backWeight * MATRIX(t2,t1 * 2 + 1)
                    - frontWeight * MATRIX(t2,t1 * 2 + 3);
        }
    }
    //set constraints on jd
    tmpt1_=&bridgeInfo->fixedIndex[0];
    while((*tmpt1_)!=-1){
        int t2;
        for (t2 = 0; t2 <= bridgeInfo->deckSize; ++t2) {
            result->jdisplacement[t2*MAX_EQUATION+(*tmpt1_)] = 0;
        }
        ++tmpt1_;
    }
    //set member force
    for (t1 = 0; t1 < bridgeInfo->memberSize; ++t1) {
        Double aeOverLL = bridgeInfo->types[thc->bundle[thc->member[t1]]].AE / SQR(result->memberLength[t1]);
        int j1x = bridgeInfo->memberLinks[t1].j1 * 2;
        int j2x = bridgeInfo->memberLinks[t1].j2 * 2;
        Double _max = 0;
        Double _min = -0.;
        int t2;
        for (t2 = 1; t2 <= bridgeInfo->deckSize; ++t2) {
         
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
    for(t1=0;t1<MAX_TYPE;++t1){
        const TypeB* type=&bridgeInfo->types[t1];
        TestMask tmp=0;
        int t2;
        for(t2=0;t2<bridgeInfo->memberSize;++t2){
            if(ifPassType(type,-result->minForce[t2],result->maxForce[t2],result->memberLength[t2],bridgeInfo->slenderness)){
                tmp|=((TestMask)1)<<t2;
            }
        }
        for(t2=0;t2<typeIndex&&(task->typeTestMask[t2]&tmp)!=tmp;++t2){}
        if(t2==typeIndex){
            task->typeTestMask[typeIndex]=tmp;
            task->index[typeIndex]=t1;
            task->cost[typeIndex]=type->cost;
            typeIndex++;
        }
    }
    for(t1=0;t1<bridgeInfo->memberSize;t1++){
        task->length[t1]=result->memberLength[t1];
    }
    task->typeSize=typeIndex;
    task->memberSize=bridgeInfo->memberSize;
    task->capCost=1000001.;
    task->minLength=0.;
    task->bundleCost=bridgeInfo->bundleCost;
    return 0;
    
}
