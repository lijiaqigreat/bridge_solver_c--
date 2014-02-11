#include "Result.h"

void result_print(GLogLevelFlags flags,const Result* result, const BridgeInfo *bridgeInfo,TypeHintCostB *thc){
    g_log(G_LOG_DOMAIN,flags,"--- result ---");
    //print joints
    int jointSize=bridgeInfo->totalJointSize;
    int t1,t2;
    g_log(G_LOG_DOMAIN,flags,"cost: %lf",GET_DOLLAR(thc));
    g_log(G_LOG_DOMAIN,flags,"joints:%d , %d",jointSize,bridgeInfo->fixedJointSize);
    g_log(G_LOG_DOMAIN,flags,"i :  x  |  y  ");
    for(t1=0;t1<jointSize;t1++){
        g_log(G_LOG_DOMAIN,flags,"%2d:%5.2lf|%5.2lf",t1,result->XY[t1*2],result->XY[t1*2+1]);
    }

    //print memebers
    int memberSize=bridgeInfo->memberSize;
    g_log(G_LOG_DOMAIN,flags,"members:%d",memberSize);
    g_log(G_LOG_DOMAIN,flags," i :j1|j2|    T     |    C     |   Tmax   |   Cmax   | T  | C  |length|name");
    for(t1=0;t1<memberSize;t1++){
        const TypeB* type=&bridgeInfo->types[thc->bundle[thc->member[t1]]];
        Double max=result->maxForce[t1];
        Double min=-result->minForce[t1];
        Double max2=type->tensionStrength;
        Double min2=getCompressionStrength(type,result->memberLength[t1]);
        g_log(G_LOG_DOMAIN,flags,"%3d:%2d|%2d|%10.2lf|%10.2lf|%10.2lf|%10.2lf|%1.2lf|%1.2lf|%2.3lf|%s",t1,bridgeInfo->memberLinks[t1].j1,bridgeInfo->memberLinks[t1].j2,max,min,max2,min2,max/max2,min/min2,result->memberLength[t1],type->name);

    }
    return 0;
}
