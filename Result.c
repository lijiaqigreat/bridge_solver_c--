#include "Result.h"

void result_print(const Result* result, const BridgeInfo *bridgeInfo,TypeHintCostB *thc){
    printf("--- result ---\n");
    //print joints
    int jointSize=bridgeInfo->totalJointSize;
    int t1,t2;
    printf("joints:%d , %d\n",jointSize,bridgeInfo->fixedJointSize);
    printf("i :  x  |  y  \n");
    for(t1=0;t1<jointSize;t1++){
        printf("%2d:%5.2lf|%5.2lf",t1,result->XY[t1*2],result->XY[t1*2+1]);
        //TODO print others
        printf("\n");
    }

    //print memebers
    int memberSize=bridgeInfo->memberSize;
    printf("members:%d\n",memberSize);
    printf(" i :j1|j2|    T     |    C     |   Tmax   |   Cmax   | T  | C  |length|name\n");
    for(t1=0;t1<memberSize;t1++){
        const TypeB* type=&bridgeInfo->types[thc->bundle[thc->member[t1]]];
        Double max=result->maxForce[t1];
        Double min=-result->minForce[t1];
        Double max2=type->tensionStrength;
        Double min2=getCompressionStrength(type,result->memberLength[t1]);
        printf("%3d:%2d|%2d|%10.2lf|%10.2lf|%10.2lf|%10.2lf|%1.2lf|%1.2lf|%2.3lf|%s\n",t1,bridgeInfo->memberLinks[t1].j1,bridgeInfo->memberLinks[t1].j2,max,min,max2,min2,max/max2,min/min2,result->memberLength[t1],type->name);

    }
    return 0;
}
